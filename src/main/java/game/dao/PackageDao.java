package game.dao;

import game.models.Deck;
import game.models.Package;
import lombok.Getter;
import lombok.Setter;

import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

public class PackageDao implements Dao<UUID, Package> {

    @Getter
    @Setter
    private Connection connection;

    public PackageDao(Connection connection) {
        setConnection(connection);
    }
    @Override
    public UUID create(Package pack) throws SQLException {

        //create Package
        String query = "INSERT INTO packages (uuid, owner) VALUES (uuid_generate_v4(), null) RETURNING uuid";

        PreparedStatement statement = getConnection().prepareStatement(query);

        ResultSet res = statement.executeQuery();
        if (!res.next()) {
            return null;
        }
        UUID new_id = res.getObject(1, UUID.class);

        statement.close();

        //associate cards with package
        for (var card : pack.getCards()) {
            String query2 = "INSERT INTO packages_cards (package, card) VALUES (?, ?)";

            statement = getConnection().prepareStatement(query2);

            statement.setObject(1, new_id);
            statement.setObject(2, card.getId());

            statement.execute();
            statement.close();
        }

        return new_id;

    }


    @Override
    public HashMap<UUID, Package> read() throws SQLException {
        return null;
    }

    @Override
    public void update(Package pack) throws SQLException {

    }

    @Override
    public void delete(Package pack) throws SQLException {

    }
}
