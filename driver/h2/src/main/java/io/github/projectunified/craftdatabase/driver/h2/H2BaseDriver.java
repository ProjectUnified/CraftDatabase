package io.github.projectunified.craftdatabase.driver.h2;

import io.github.projectunified.craftdatabase.Driver;
import io.github.projectunified.craftdatabase.Setting;

/**
 * A driver for H2
 */
public interface H2BaseDriver extends Driver {
    @Override
    default Class<? extends java.sql.Driver> getDriverClass() {
        return org.h2.Driver.class;
    }

    @Override
    default String convertURL(Setting setting) {
        return "jdbc:h2:" +
                getConnectionString(setting) +
                Driver.createPropertyString(setting, ";", ";");
    }

    String getConnectionString(Setting setting);
}
