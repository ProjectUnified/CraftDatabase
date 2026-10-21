package io.github.projectunified.craftdatabase.client.sql.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A builder for {@code INSERT} queries
 */
public final class InsertBuilder {
    private final String table;
    private final List<String> columns = new ArrayList<>();
    private Object[] values;

    private InsertBuilder(String table) {
        this.table = table;
    }

    /**
     * Create a new builder
     *
     * @param table the table
     * @return the builder
     */
    public static InsertBuilder create(String table) {
        return new InsertBuilder(Objects.requireNonNull(table, "table"));
    }

    /**
     * Add columns to insert into
     *
     * @param columns the columns
     * @return this builder for chaining
     */
    public InsertBuilder columns(String... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    /**
     * Set the values of a row, omit this to build the statement only, e.g. for a batch
     *
     * @param values the values
     * @return this builder for chaining
     */
    public InsertBuilder values(Object... values) {
        this.values = values.clone();
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
        if (values != null && values.length != columns.size()) {
            throw new IllegalStateException("The values count (" + values.length + ") does not match the columns count (" + columns.size() + ")");
        }
        String placeholders = String.join(", ", Collections.nCopies(columns.size(), "?"));
        String sql = "INSERT INTO " + table + " (" + String.join(", ", columns) + ") VALUES (" + placeholders + ")";
        return new Query(sql, values == null ? new Object[0] : values.clone());
    }
}
