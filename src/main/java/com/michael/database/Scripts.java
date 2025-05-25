package com.michael.database;

public class Scripts {
    private Scripts() {}

    public static void init(Database database) throws Exception {
        try(var connection = database.dataSource().getConnection()) {
            connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS todo(
                       id INTEGER NOT NULL PRIMARY KEY,
                       title TEXT NOT NULL,
                       completed BOOLEAN NOT NULL DEFAULT false,
                       "order" INTEGER NOT NULL DEFAULT 0
                    )
                    """).execute();
        }
    }
}
