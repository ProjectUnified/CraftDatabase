package io.github.projectunified.craftdatabase.client.sql.helper;

import io.github.projectunified.craftdatabase.client.sql.BatchBuilder;
import io.github.projectunified.craftdatabase.client.sql.SqlClient;
import io.github.projectunified.craftdatabase.client.sql.StatementBuilder;
import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A SQL statement with its ordered parameter values
 */
public final class Query {
    private final @Language("SQL") String sql;
    private final Object[] values;

    Query(@Language("SQL") String sql, Object[] values) {
        this.sql = sql;
        this.values = values;
    }

    /**
     * Get the SQL statement
     *
     * @return the SQL statement
     */
    public @Language("SQL") String getSql() {
        return sql;
    }

    /**
     * Get the values for the {@code ?} placeholders, in order, empty if only the statement is built
     *
     * @return the values
     */
    public Object[] getValues() {
        return values;
    }

    /**
     * Convert this query to a statement builder
     *
     * @param connection the connection
     * @return the statement builder
     */
    public StatementBuilder toStatementBuilder(Connection connection) {
        return StatementBuilder.create(connection).setStatement(sql).setValues(values);
    }

    /**
     * Convert this query to a statement builder
     *
     * @param client the client
     * @return the statement builder
     * @throws SQLException if there is an SQL error
     */
    public StatementBuilder toStatementBuilder(SqlClient<?> client) throws SQLException {
        return client.createStatementBuilder().setStatement(sql).setValues(values);
    }

    /**
     * Convert this query to a statement builder
     *
     * @param client the client
     * @return the statement builder
     * @throws IllegalStateException if there is an SQL error
     */
    public StatementBuilder toStatementBuilderUnsafe(SqlClient<?> client) {
        return client.createStatementBuilderUnsafe().setStatement(sql).setValues(values);
    }

    /**
     * Convert this query to a batch builder, the values of this query, if any, are added as the first entry
     *
     * @param connection the connection
     * @return the batch builder
     */
    public BatchBuilder toBatchBuilder(Connection connection) {
        return addValues(BatchBuilder.create(connection, sql));
    }

    /**
     * Convert this query to a batch builder, the values of this query, if any, are added as the first entry
     *
     * @param client the client
     * @return the batch builder
     * @throws SQLException if there is an SQL error
     */
    public BatchBuilder toBatchBuilder(SqlClient<?> client) throws SQLException {
        return addValues(client.createBatchBuilder(sql));
    }

    /**
     * Convert this query to a batch builder, the values of this query, if any, are added as the first entry
     *
     * @param client the client
     * @return the batch builder
     * @throws IllegalStateException if there is an SQL error
     */
    public BatchBuilder toBatchBuilderUnsafe(SqlClient<?> client) {
        return addValues(client.createBatchBuilderUnsafe(sql));
    }

    private BatchBuilder addValues(BatchBuilder builder) {
        if (values.length > 0) {
            builder.addValues(values);
        }
        return builder;
    }

    @Override
    public String toString() {
        return sql;
    }
}
