package game.dao;

import game.models.User;
import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class UserDao implements Dao<User> {

    @Getter
    @Setter
    private Connection connection;

    public UserDao(Connection connection) { setConnection(connection); }


    @Override
    public User create(User user) throws SQLException {
        String query = "INSERT INTO users (username, password, elo) VALUES (?,?,?)";

        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setInt(3, user.getElo());

        ResultSet res = statement.executeQuery();

        user.setUuid(UUID.fromString(res.getString(1)));

        return user;
    }

    @Override
    public ArrayList<User> read() throws SQLException {

        String query = "SELECT uuid, username, password, elo from users";

        PreparedStatement statement = getConnection().prepareStatement(query);

        ResultSet resultSet = statement.executeQuery();

        ArrayList<User> list = new ArrayList<>();

        while (resultSet.next()) {
            User user = new User(
                    UUID.fromString(resultSet.getString("uuid")),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getInt("elo")
            );

            list.add(user);
        }

        return list;
    }

    @Override
    public void update(User user) throws SQLException {
        String query = "UPDATE users SET" +
                        "username = ?," +
                        "password = ?," +
                        "elo = ?" +
                "WHERE uuid = ?";

        PreparedStatement statement = getConnection().prepareStatement(query);

        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setInt(3, user.getElo());
        statement.setString(4, user.getUuid().toString());

        statement.execute();
    }

    @Override
    public void delete(User user) throws SQLException {
        String query = "DELETE FROM users WHERE uuid = ?";

        PreparedStatement statement = getConnection().prepareStatement(query);

        statement.setString(1, user.getUuid().toString());

        statement.execute();
    }
}
