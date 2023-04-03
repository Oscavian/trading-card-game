package com.oscavian.tradingcardgame.game.services;

import lombok.AccessLevel;
import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseService {
    @Getter(AccessLevel.PUBLIC)
    private Connection connection;

    public DatabaseService() {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://" + System.getenv("POSTGRES_URL"),
                    System.getenv("POSTGRES_USER"),
                    System.getenv("POSTGRES_PASSWORD")
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
