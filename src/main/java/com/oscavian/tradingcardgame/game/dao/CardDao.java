package com.oscavian.tradingcardgame.game.dao;

import com.oscavian.tradingcardgame.game.models.Card;
import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class CardDao implements Dao<UUID, Card> {

    @Getter
    @Setter
    private Connection connection;

    public CardDao(Connection connection) {
        setConnection(connection);
    }

    @Override
    public UUID create(Card card) throws SQLException {
        String query;

        if (card.getId() != null) {
            query = "INSERT INTO cards (uuid, name, damage) VALUES (?, ?, ?) RETURNING uuid";
        } else {
            query = "INSERT INTO cards (name, damage) VALUES (?, ?) RETURNING uuid";
        }

        PreparedStatement statement = getConnection().prepareStatement(query);

        if (card.getId() != null) {
            statement.setObject(1, card.getId());
            statement.setString(2, card.getName());
            statement.setFloat(3, card.getDamage());
        } else {
            statement.setString(1, card.getName());
            statement.setFloat(2, card.getDamage());
        }

        ResultSet res = statement.executeQuery();


        if (res.next()) {
            return res.getObject(1, UUID.class);
        }
        return null;
    }

    @Override
    public HashMap<UUID, Card> read() throws SQLException {

        String query = "SELECT uuid, name, damage FROM cards";

        PreparedStatement statement = getConnection().prepareStatement(query);

        ResultSet res = statement.executeQuery();

        HashMap<UUID, Card> cardHashMap = new HashMap<>();

        while (res.next()) {
            Card card = new Card(
                    UUID.fromString(res.getString("uuid")),
                    res.getString("name"),
                    res.getFloat("damage")
            );

            cardHashMap.put(card.getId(), card);
        }

        statement.close();
        return cardHashMap;
    }

    @Override
    public void update(Card card) throws SQLException {

    }

    @Override
    public void delete(Card card) throws SQLException {

    }
}
