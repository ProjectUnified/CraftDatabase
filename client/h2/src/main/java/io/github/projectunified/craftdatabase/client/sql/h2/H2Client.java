package io.github.projectunified.craftdatabase.client.sql.h2;

import io.github.projectunified.craftdatabase.Driver;
import io.github.projectunified.craftdatabase.Setting;
import io.github.projectunified.craftdatabase.client.sql.SqlClient;
import io.github.projectunified.craftdatabase.driver.h2.H2BaseDriver;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class H2Client implements SqlClient<JdbcDataSource> {
    private final Setting setting;
    private final JdbcDataSource dataSource;

    /**
     * Create a new H2 client
     *
     * @param setting the setting
     */
    public H2Client(Setting setting) {
        this.setting = setting;
        Driver driver = setting.getDriver();
        if (!(driver instanceof H2BaseDriver)) {
            throw new IllegalArgumentException("The driver must be H2");
        }
        dataSource = new JdbcDataSource();
        dataSource.setURL(driver.convertURL(setting));
        dataSource.setUser(setting.getUsername());
        dataSource.setPassword(setting.getPassword());
    }

    @Override
    public Setting getSetting() {
        return setting;
    }

    @Override
    public JdbcDataSource getOriginal() {
        return dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
