package io.github.projectunified.craftdatabase.driver.sqlite;

import io.github.projectunified.craftdatabase.Setting;

/**
 * A driver for SQLite (In-Memory Mode)
 */
public class SqliteMemoryDriver implements SqliteBaseDriver {
    @Override
    public String createConnectionString(Setting setting) {
        return ":memory:";
    }
}
