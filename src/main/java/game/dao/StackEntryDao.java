package game.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import game.models.StackDeckEntry;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class StackEntryDao implements Dao<UUID, StackDeckEntry>{

    @Getter
    @Setter
    private Connection connection;

    public StackEntryDao(Connection connection) {
        setConnection(connection);
    }
    @Override
    public UUID create(StackDeckEntry stackEntry) throws SQLException {
        String query = "INSERT INTO stacks (user_uuid, card_uuid) VALUES (?, ?) RETURNING entry_uuid";

        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setObject(1, stackEntry.getUser_uuid());
        statement.setObject(2, stackEntry.getCard_uuid());

        ResultSet res = statement.executeQuery();

        if (res.next()) {
            return UUID.fromString(res.getString(1));
        }
        return null;
    }

    @Override
    public HashMap<UUID, StackDeckEntry> read() throws SQLException {
        String query = "SELECT entry_uuid, user_uuid, card_uuid from stacks";

        PreparedStatement statement = getConnection().prepareStatement(query);

        ResultSet res = statement.executeQuery();

        HashMap<UUID, StackDeckEntry> stackEntryHashMap = new HashMap<>();

        while (res.next()){
            StackDeckEntry stackEntry = new StackDeckEntry(
                    res.getObject(1, UUID.class),
                    res.getObject(2, UUID.class),
                    res.getObject(3, UUID.class)
            );

            stackEntryHashMap.put(stackEntry.getEntry_uuid(), stackEntry);
        }

        statement.close();

        return stackEntryHashMap;
    }

    @Override
    public void update(StackDeckEntry stackEntry) throws SQLException {

    }

    @Override
    public void delete(StackDeckEntry stackEntry) throws SQLException {
        String query = "DELETE FROM stacks WHERE entry_uuid = ?";

        PreparedStatement statement = getConnection().prepareStatement(query);

        statement.setObject(1, stackEntry.getEntry_uuid());
    }
}
