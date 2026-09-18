package io.github.projectunified.craftdatabase.driver.h2;

import io.github.projectunified.craftdatabase.Setting;

/**
 * A driver for H2 (In-Memory Mode)
 */
public class H2MemoryDriver implements H2BaseDriver {
    @Override
    public String getConnectionString(Setting setting) {
        return "mem:" + setting.getDatabaseName();
    }
}
