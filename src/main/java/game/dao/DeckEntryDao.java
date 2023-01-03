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

public class DeckEntryDao implements Dao<UUID, StackDeckEntry>{

    @Getter
    @Setter
    private Connection connection;

    public DeckEntryDao(Connection connection) {
        setConnection(connection);
    }
    @Override
    public UUID create(StackDeckEntry deckEntry) throws SQLException {
        String query = "INSERT INTO decks (user_uuid, card_uuid) VALUES (?, ?) RETURNING entry_uuid";

        PreparedStatement statement = getConnection().prepareStatement(query);
        statement.setObject(1, deckEntry.getUser_uuid());
        statement.setObject(2, deckEntry.getCard_uuid());

        ResultSet res = statement.executeQuery();

        if (res.next()) {
            return UUID.fromString(res.getString(1));
        }
        return null;
    }

    @Override
    public HashMap<UUID, StackDeckEntry> read() throws SQLException {
        String query = "SELECT entry_uuid, user_uuid, card_uuid from decks";

        PreparedStatement statement = getConnection().prepareStatement(query);

        ResultSet res = statement.executeQuery();

        HashMap<UUID, StackDeckEntry> deckEntryHashMap = new HashMap<>();

        while (res.next()){
            StackDeckEntry deckEntry = new StackDeckEntry(
                    res.getObject(1, UUID.class),
                    res.getObject(2, UUID.class),
                    res.getObject(3, UUID.class)
            );

            deckEntryHashMap.put(deckEntry.getEntry_uuid(), deckEntry);
        }

        statement.close();

        return deckEntryHashMap;
    }

    @Override
    public void update(StackDeckEntry deckEntry) throws SQLException {

    }

    @Override
    public void delete(StackDeckEntry deckEntry) throws SQLException {
        String query = "DELETE FROM decks WHERE entry_uuid = ?";

        PreparedStatement statement = getConnection().prepareStatement(query);

        statement.setObject(1, deckEntry.getEntry_uuid());
    }
}
