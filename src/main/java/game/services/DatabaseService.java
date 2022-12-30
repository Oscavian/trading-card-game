package game.services;

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
                    "jdbc:postgresql://localhost:5431/swe1db",
                    "swe1user",
                    "swe1pw"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
