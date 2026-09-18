package io.github.projectunified.craftdatabase.driver.sqlite;

import io.github.projectunified.craftdatabase.LocalDriver;
import io.github.projectunified.craftdatabase.Setting;

import java.io.File;
import java.nio.file.Paths;

/**
 * A driver for SQLite (File Mode)
 */
public class SqliteFileDriver extends LocalDriver implements SqliteBaseDriver {
    public SqliteFileDriver() {
        super();
    }

    public SqliteFileDriver(File folder) {
        super(folder);
    }

    @Override
    public String createConnectionString(Setting setting) {
        return Paths.get(getFolder().getAbsolutePath(), setting.getDatabaseName() + ".db").toString();
    }
}
