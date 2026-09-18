package io.github.projectunified.craftdatabase.client.sql;

import io.github.projectunified.craftdatabase.Client;
import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The interface for SQL client
 *
 * @param <T> the original
 */
public interface SqlClient<T> extends Client<T> {

    /**
     * Get the connection
     *
     * @return the connection
     * @throws SQLException if there is an SQL error
     */
    Connection getConnection() throws SQLException;

    /**
     * Create a new statement builder for this client
     *
     * @return the statement builder
     * @throws SQLException if there is an SQL error
     */
    default StatementBuilder createStatementBuilder() throws SQLException {
        return StatementBuilder.create(this.getConnection());
    }

    /**
     * Create a new statement builder for this client
     *
     * @return the statement builder
     * @throws IllegalStateException if there is an SQL error
     */
    default StatementBuilder createStatementBuilderUnsafe() {
        try {
            return this.createStatementBuilder();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create the statement builder", e);
        }
    }

    /**
     * Create a new batch builder for this client
     *
     * @param statement the statement
     * @return the batch builder
     * @throws SQLException if there is an SQL error
     */
    default BatchBuilder createBatchBuilder(@Language("SQL") String statement) throws SQLException {
        return BatchBuilder.create(this.getConnection(), statement);
    }

    /**
     * Create a new batch builder for this client
     *
     * @param statement the statement
     * @return the batch builder
     * @throws IllegalStateException if there is an SQL error
     */
    default BatchBuilder createBatchBuilderUnsafe(@Language("SQL") String statement) {
        try {
            return this.createBatchBuilder(statement);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot create the batch builder", e);
        }
    }
}
