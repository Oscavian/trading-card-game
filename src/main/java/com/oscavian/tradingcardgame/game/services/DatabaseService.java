package com.oscavian.tradingcardgame.game.services;

import lombok.AccessLevel;
import lombok.Getter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseService {
    @Getter(AccessLevel.PUBLIC)
    private Connection connection;

    public DatabaseService() throws IOException {
        try {
            assert System.getenv("POSTGRES_URL") != null;
            assert System.getenv("POSTGRES_DB") != null;

            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://" + (System.getenv("POSTGRES_URL") + "/" + System.getenv("POSTGRES_DB")),
                    System.getenv("POSTGRES_USER"),
                    System.getenv("POSTGRES_PASSWORD")
            );
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOException("Database not available");
        }
    }
}
