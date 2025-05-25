package com.michael.database;

import org.sqlite.SQLiteDataSource;

public class Database {
    private final SQLiteDataSource dataSource;

    public Database() {
        String jdbcUrl = "jdbc:sqlite:todo.db";
        this.dataSource = new SQLiteDataSource();
        dataSource.setUrl(jdbcUrl);
    }

    public SQLiteDataSource dataSource() {
        return dataSource;
    }
}
