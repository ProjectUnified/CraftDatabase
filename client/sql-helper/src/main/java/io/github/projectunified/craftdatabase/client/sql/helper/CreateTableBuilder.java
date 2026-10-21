package io.github.projectunified.craftdatabase.client.sql.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A builder for {@code CREATE TABLE} queries
 */
public final class CreateTableBuilder {
    private final String table;
    private final List<String> columns = new ArrayList<>();
    private final List<String> constraints = new ArrayList<>();
    private boolean ifNotExists;

    private CreateTableBuilder(String table) {
        this.table = table;
    }

    /**
     * Create a new builder
     *
     * @param table the table
     * @return the builder
     */
    public static CreateTableBuilder create(String table) {
        return new CreateTableBuilder(Objects.requireNonNull(table, "table"));
    }

    /**
     * Add the {@code IF NOT EXISTS} option
     *
     * @return this builder for chaining
     */
    public CreateTableBuilder ifNotExists() {
        this.ifNotExists = true;
        return this;
    }

    /**
     * Add a column
     *
     * @param name       the column name
     * @param definition the column definition, e.g. {@code "INT PRIMARY KEY"} or {@code "VARCHAR(100) NOT NULL"}
     * @return this builder for chaining
     */
    public CreateTableBuilder column(String name, String definition) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Column name must not be empty");
        }
        if (definition == null || definition.trim().isEmpty()) {
            throw new IllegalArgumentException("Column definition must not be empty");
        }
        this.columns.add(name + " " + definition);
        return this;
    }

    /**
     * Add a table constraint
     *
     * @param constraint the constraint, e.g. {@code "PRIMARY KEY (a, b)"}
     * @return this builder for chaining
     */
    public CreateTableBuilder constraint(String constraint) {
        if (constraint == null || constraint.trim().isEmpty()) {
            throw new IllegalArgumentException("Constraint must not be empty");
        }
        this.constraints.add(constraint);
        return this;
    }

    /**
     * Build the query
     *
     * @return the query
     */
    public Query build() {
        if (columns.isEmpty()) {
            throw new IllegalStateException("No columns are set");
        }
        List<String> parts = new ArrayList<>(columns);
        parts.addAll(constraints);
        String sql = "CREATE TABLE " + (ifNotExists ? "IF NOT EXISTS " : "") + table + " (" + String.join(", ", parts) + ")";
        return new Query(sql, new Object[0]);
    }
}
