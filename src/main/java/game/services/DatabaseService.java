package game.services;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseService extends Service {
    @Getter(AccessLevel.PRIVATE)
    private final Connection connection;

    public DatabaseService() throws SQLException {
        this.connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432",
                "",
                ""
        );
    }
}
