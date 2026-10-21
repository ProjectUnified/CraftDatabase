package io.github.projectunified.craftdatabase.client.sql.helper;

import java.util.Objects;

/**
 * A builder for {@code ALTER TABLE} queries
 */
public final class AlterTableBuilder {
    private final String table;
    private String action;

    private AlterTableBuilder(String table) {
        this.table = table;
    }

    /**
     * Create a new builder
     *
     * @param table the table
     * @return the builder
     */
    public static AlterTableBuilder create(String table) {
        return new AlterTableBuilder(Objects.requireNonNull(table, "table"));
    }

    /**
     * Add a column, e.g. {@code "INT NOT NULL"}. Some databases restrict the supported definitions
     *
     * @param name       the column name
     * @param definition the column definition
     * @return this builder for chaining
     */
    public AlterTableBuilder addColumn(String name, String definition) {
        requireValue(name, "Column name");
        requireValue(definition, "Column definition");
        return action("ADD COLUMN " + name + " " + definition);
    }

    /**
     * Drop a column
     *
     * @param name the column name
     * @return this builder for chaining
     */
    public AlterTableBuilder dropColumn(String name) {
        requireValue(name, "Column name");
        return action("DROP COLUMN " + name);
    }

    /**
     * Rename a column
     *
     * @param name    the column name
     * @param newName the new column name
     * @return this builder for chaining
     */
    public AlterTableBuilder renameColumn(String name, String newName) {
        requireValue(name, "Column name");
        requireValue(newName, "New column name");
        return action("RENAME COLUMN " + name + " TO " + newName);
    }

    /**
     * Rename the table
     *
     * @param newName the new table name
     * @return this builder for chaining
     */
    public AlterTableBuilder renameTo(String newName) {
        requireValue(newName, "New table name");
        return action("RENAME TO " + newName);
    }

    /**
     * Set a raw action, e.g. {@code "ALTER COLUMN id SET DATA TYPE BIGINT"} for dialect-specific changes
     *
     * @param action the action
     * @return this builder for chaining
     */
    public AlterTableBuilder action(String action) {
        requireValue(action, "Action");
        if (this.action != null) {
            throw new IllegalStateException("Only one action can be set");
        }
        this.action = action;
        return this;
    }

    /**
     * Build the query
     *
     * @return the query
     */
    public Query build() {
        if (action == null) {
            throw new IllegalStateException("No action is set");
        }
        return new Query("ALTER TABLE " + table + " " + action, new Object[0]);
    }

    private static void requireValue(String value, String name) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(name + " must not be empty");
        }
    }
}
