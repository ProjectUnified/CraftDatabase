package io.github.projectunified.craftdatabase.client.sql.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The shared {@code WHERE} clause of a query builder
 */
abstract class WhereBuilder {
    private final List<String> conditions = new ArrayList<>();
    private final List<Object> values = new ArrayList<>();

    /**
     * Add a condition, joined to the previous ones with {@code AND}
     *
     * @param condition the condition, the {@code ?} placeholders must match the values
     * @param values    the values for the placeholders
     */
    final void addWhere(String condition, Object... values) {
        if (condition == null || condition.trim().isEmpty()) {
            throw new IllegalArgumentException("Condition must not be empty");
        }
        this.conditions.add(condition);
        this.values.addAll(Arrays.asList(values));
    }

    /**
     * Append the {@code WHERE} clause to the builder
     *
     * @param builder the builder
     */
    final void appendWhere(StringBuilder builder) {
        if (!conditions.isEmpty()) {
            builder.append(" WHERE ").append(String.join(" AND ", conditions));
        }
    }

    /**
     * Get the values for the {@code WHERE} placeholders
     *
     * @return the values
     */
    final Object[] whereValues() {
        return values.toArray();
    }
}
