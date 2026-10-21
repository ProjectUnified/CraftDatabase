package io.github.projectunified.craftdatabase.client.sql.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A builder for {@code SELECT} queries
 */
public final class SelectBuilder extends WhereBuilder {
    private final String table;
    private final List<String> columns = new ArrayList<>();
    private final List<String> joins = new ArrayList<>();
    private final List<Object> joinValues = new ArrayList<>();
    private final List<String> groupBy = new ArrayList<>();
    private final List<String> having = new ArrayList<>();
    private final List<Object> havingValues = new ArrayList<>();
    private final List<String> orderBy = new ArrayList<>();
    private boolean distinct;
    private Integer limit;

    private SelectBuilder(String table) {
        this.table = table;
    }

    /**
     * Create a new builder
     *
     * @param table the table
     * @return the builder
     */
    public static SelectBuilder create(String table) {
        return new SelectBuilder(Objects.requireNonNull(table, "table"));
    }

    /**
     * Add the {@code DISTINCT} option
     *
     * @return this builder for chaining
     */
    public SelectBuilder distinct() {
        this.distinct = true;
        return this;
    }

    /**
     * Add columns to select, the default is {@code *}
     *
     * @param columns the columns
     * @return this builder for chaining
     */
    public SelectBuilder columns(String... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    /**
     * Add a join clause, e.g. {@code join("LEFT JOIN orders o ON o.user_id = u.id")}
     *
     * @param join   the join clause
     * @param values the values for the placeholders, omitted to provide them later in a batch
     * @return this builder for chaining
     */
    public SelectBuilder join(String join, Object... values) {
        if (join == null || join.trim().isEmpty()) {
            throw new IllegalArgumentException("Join must not be empty");
        }
        this.joins.add(join);
        this.joinValues.addAll(Arrays.asList(values));
        return this;
    }

    /**
     * Add a condition, joined to the previous ones with {@code AND}
     *
     * @param condition the condition, the {@code ?} placeholders must match the values
     * @param values    the values for the placeholders, omitted to provide them later in a batch
     * @return this builder for chaining
     */
    public SelectBuilder where(String condition, Object... values) {
        addWhere(condition, values);
        return this;
    }

    /**
     * Add columns to group by
     *
     * @param columns the columns
     * @return this builder for chaining
     */
    public SelectBuilder groupBy(String... columns) {
        this.groupBy.addAll(Arrays.asList(columns));
        return this;
    }

    /**
     * Add a condition to the {@code HAVING} clause, joined to the previous ones with {@code AND}
     *
     * @param condition the condition, the {@code ?} placeholders must match the values
     * @param values    the values for the placeholders, omitted to provide them later in a batch
     * @return this builder for chaining
     */
    public SelectBuilder having(String condition, Object... values) {
        if (condition == null || condition.trim().isEmpty()) {
            throw new IllegalArgumentException("Condition must not be empty");
        }
        this.having.add(condition);
        this.havingValues.addAll(Arrays.asList(values));
        return this;
    }

    /**
     * Add an order by expression, e.g. {@code "id DESC"}
     *
     * @param orderBy the order by expression
     * @return this builder for chaining
     */
    public SelectBuilder orderBy(String... orderBy) {
        this.orderBy.addAll(Arrays.asList(orderBy));
        return this;
    }

    /**
     * Set the maximum number of rows
     *
     * @param limit the limit
     * @return this builder for chaining
     */
    public SelectBuilder limit(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Limit must not be negative");
        }
        this.limit = limit;
        return this;
    }

    /**
     * Build the query
     *
     * @return the query
     */
    public Query build() {
        StringBuilder builder = new StringBuilder("SELECT ");
        if (distinct) {
            builder.append("DISTINCT ");
        }
        builder.append(columns.isEmpty() ? "*" : String.join(", ", columns));
        builder.append(" FROM ").append(table);
        for (String join : joins) {
            builder.append(' ').append(join);
        }
        appendWhere(builder);
        if (!groupBy.isEmpty()) {
            builder.append(" GROUP BY ").append(String.join(", ", groupBy));
        }
        if (!having.isEmpty()) {
            builder.append(" HAVING ").append(String.join(" AND ", having));
        }
        if (!orderBy.isEmpty()) {
            builder.append(" ORDER BY ").append(String.join(", ", orderBy));
        }
        if (limit != null) {
            builder.append(" LIMIT ").append(limit);
        }
        List<Object> values = new ArrayList<>(joinValues);
        values.addAll(Arrays.asList(whereValues()));
        values.addAll(havingValues);
        return new Query(builder.toString(), values.toArray());
    }
}
