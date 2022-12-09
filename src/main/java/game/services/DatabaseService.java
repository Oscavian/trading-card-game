package game.services;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseService extends Service {
    @Getter(AccessLevel.PUBLIC)
    private Connection connection;

    public DatabaseService() {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5431/",
                    "swe1user",
                    "swe1pw"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
