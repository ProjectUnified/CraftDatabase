package io.github.projectunified.craftdatabase.client.sql.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A builder for {@code UPDATE} queries
 */
public final class UpdateBuilder extends WhereBuilder {
    private final String table;
    private final List<String> assignments = new ArrayList<>();
    private final List<Object> values = new ArrayList<>();

    private UpdateBuilder(String table) {
        this.table = table;
    }

    /**
     * Create a new builder
     *
     * @param table the table
     * @return the builder
     */
    public static UpdateBuilder create(String table) {
        return new UpdateBuilder(Objects.requireNonNull(table, "table"));
    }

    /**
     * Add an assignment, e.g. {@code set("name = ?", "new name")} or {@code set("count = count + 1")}
     *
     * @param assignment the assignment, the {@code ?} placeholders must match the values
     * @param values     the values for the placeholders, omitted to provide them later in a batch
     * @return this builder for chaining
     */
    public UpdateBuilder set(String assignment, Object... values) {
        if (assignment == null || assignment.trim().isEmpty()) {
            throw new IllegalArgumentException("Assignment must not be empty");
        }
        this.assignments.add(assignment);
        this.values.addAll(Arrays.asList(values));
        return this;
    }

    /**
     * Add a condition, joined to the previous ones with {@code AND}
     *
     * @param condition the condition, the {@code ?} placeholders must match the values
     * @param values    the values for the placeholders, omitted to provide them later in a batch
     * @return this builder for chaining
     */
    public UpdateBuilder where(String condition, Object... values) {
        addWhere(condition, values);
        return this;
    }

    /**
     * Build the query
     *
     * @return the query
     */
    public Query build() {
        if (assignments.isEmpty()) {
            throw new IllegalStateException("No assignments are set");
        }
        StringBuilder builder = new StringBuilder("UPDATE ")
                .append(table)
                .append(" SET ")
                .append(String.join(", ", assignments));
        appendWhere(builder);
        List<Object> allValues = new ArrayList<>(values);
        allValues.addAll(Arrays.asList(whereValues()));
        return new Query(builder.toString(), allValues.toArray());
    }
}
