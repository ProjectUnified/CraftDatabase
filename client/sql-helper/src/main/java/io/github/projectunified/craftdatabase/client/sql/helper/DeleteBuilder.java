package io.github.projectunified.craftdatabase.client.sql.helper;

import java.util.Objects;

/**
 * A builder for {@code DELETE} queries
 */
public final class DeleteBuilder extends WhereBuilder {
    private final String table;

    private DeleteBuilder(String table) {
        this.table = table;
    }

    /**
     * Create a new builder
     *
     * @param table the table
     * @return the builder
     */
    public static DeleteBuilder create(String table) {
        return new DeleteBuilder(Objects.requireNonNull(table, "table"));
    }

    /**
     * Add a condition, joined to the previous ones with {@code AND}
     *
     * @param condition the condition, the {@code ?} placeholders must match the values
     * @param values    the values for the placeholders, omitted to provide them later in a batch
     * @return this builder for chaining
     */
    public DeleteBuilder where(String condition, Object... values) {
        addWhere(condition, values);
        return this;
    }

    /**
     * Build the query
     *
     * @return the query
     */
    public Query build() {
        StringBuilder builder = new StringBuilder("DELETE FROM ").append(table);
        appendWhere(builder);
        return new Query(builder.toString(), whereValues());
    }
}
