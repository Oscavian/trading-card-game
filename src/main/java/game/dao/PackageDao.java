package game.dao;

import game.models.Package;
import lombok.Getter;
import lombok.Setter;

import java.sql.*;
import java.util.ArrayList;
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
        //String query = "SELECT uuid as package, owner, card from packages join packages_cards pc on packages.uuid = pc.package";
        String query = "SELECT uuid, owner from packages";

        PreparedStatement statement = getConnection().prepareStatement(query);
        ResultSet res = statement.executeQuery();

        var map = new HashMap<UUID, Package>();

        while (res.next()) {
            Package pack = new Package(
                    res.getObject(1, UUID.class),
                    res.getObject(2, UUID.class),
                    null
            );
            map.put(pack.getUuid(), pack);
        }

        return map;
    }

    public ArrayList<Package> readOrderedByCreation() throws SQLException {
        String query = "SELECT uuid, owner from packages ORDER BY creation";

        PreparedStatement statement = getConnection().prepareStatement(query);
        ResultSet res = statement.executeQuery();

        var list = new ArrayList<Package>();

        while (res.next()) {
            Package pack = new Package(
                    res.getObject(1, UUID.class),
                    res.getObject(2, UUID.class),
                    null
            );
            list.add(pack);
        }

        return list;
    }

    @Override
    public void update(Package pack) throws SQLException {

        String query = "UPDATE packages SET owner = ? WHERE uuid = ?";

        PreparedStatement statement = getConnection().prepareStatement(query);

        statement.setObject(1, pack.getOwner());
        statement.setObject(2, pack.getUuid());

        statement.executeUpdate();
    }

    @Override
    public void delete(Package pack) throws SQLException {

    }

    public ArrayList<UUID> readCardIds(UUID packageId) throws SQLException {
        String query = "SELECT card from packages_cards where package = ?";

        PreparedStatement statement = getConnection().prepareStatement(query);

        statement.setObject(1, packageId);

        ResultSet res = statement.executeQuery();

        var list = new ArrayList<UUID>();

        while (res.next()) {
            list.add(res.getObject(1, UUID.class));
        }

        return list;
    }
}
