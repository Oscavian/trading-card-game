package game.dao;

import game.models.user.User;
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
        String query = "INSERT INTO users (username, password) VALUES (?,?) RETURNING uuid";

        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());

        ResultSet res = statement.executeQuery();

        if (res.next()){
            user.setId(UUID.fromString(res.getString(1)));
            return user;
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<User> read() throws SQLException {

        String query = "SELECT uuid, username, password, bio, image, elo, wins, losses from users;";

        PreparedStatement statement = getConnection().prepareStatement(query);

        ResultSet resultSet = statement.executeQuery();

        ArrayList<User> list = new ArrayList<>();

        while (resultSet.next()) {
            User user = new User(
                    UUID.fromString(resultSet.getString("uuid")),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("bio"),
                    resultSet.getString("image"),
                    resultSet.getInt("elo"),
                    resultSet.getInt("wins"),
                    resultSet.getInt("losses")
            );

            list.add(user);
        }

        return list;
    }

    @Override
    public void update(User user) throws SQLException {
        /*
        String query = "UPDATE users SET" +
                        "username = ?," +
                        "password = ?," +
                        "bio = ?," +
                        "image = ?," +
                        "elo = ?," +
                        "wins = ?," +
                        "losses = ?," +
                "WHERE uuid = ?";

         */

        var params = new ArrayList<String>();

        if (!user.getPassword().isEmpty()){
            params.add("password = ?");
        }
        if (!user.getBio().isEmpty()) {
            params.add("bio = ?");
        }
        if (!user.getImage().isEmpty()) {
            params.add("image = ?");
        }
        if (user.getElo() != 0){
            params.add("elo = ?");
        }
        if (user.getWins() != 0) {
            params.add("wins = ?");
        }
        if (user.getLosses() != 0) {
            params.add("losses = ?");
        }

        if (params.isEmpty()) {
            return;
        }

        String paramStr = String.join(",", params);

        String query = "UPDATE users SET " + paramStr + "WHERE username = ?";

        PreparedStatement statement = getConnection().prepareStatement(query);

        int parameterIndex = 1;

        if (params.contains("password = ?")) {
            statement.setString(parameterIndex++, user.getPassword());
        }
        if (params.contains("bio = ?")) {
            statement.setString(parameterIndex++, user.getBio());
        }
        if (params.contains("elo = ?")) {
            statement.setInt(parameterIndex++, user.getElo());
        }
        if (params.contains("image = ?")) {
            statement.setString(parameterIndex++, user.getImage());
        }
        if (params.contains("wins = ?")) {
            statement.setInt(parameterIndex++, user.getWins());
        }
        if (params.contains("losses = ?")) {
            statement.setInt(parameterIndex++, user.getLosses());
        }

        statement.setString(parameterIndex, user.getUsername());

        statement.executeUpdate();
    }

    @Override
    public void delete(User user) throws SQLException {
        String query = "DELETE FROM users WHERE uuid = ?";

        PreparedStatement statement = getConnection().prepareStatement(query);

        statement.setString(1, user.getId().toString());

        statement.executeUpdate();
    }
}
