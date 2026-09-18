package io.github.projectunified.craftdatabase.driver.mariadb;

import io.github.projectunified.craftdatabase.Driver;
import io.github.projectunified.craftdatabase.Setting;

/**
 * A driver for MariaDB
 */
public class MariaDBDriver implements Driver {
    @Override
    public Class<? extends java.sql.Driver> getDriverClass() {
        return org.mariadb.jdbc.Driver.class;
    }

    @Override
    public String convertURL(Setting setting) {
        return "jdbc:mariadb://" + setting.getNormalizedHost() + '/' + setting.getDatabaseName() + Driver.createPropertyString(setting);
    }

    @Override
    public void applyDefaultSetting(Setting setting) {
        setting.setPort("3306").setUsername("root");
    }
}
