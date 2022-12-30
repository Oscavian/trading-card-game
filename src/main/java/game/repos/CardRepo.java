package game.repos;

import game.models.Card;

import java.util.ArrayList;
import java.util.UUID;

public class CardRepo extends Repository<UUID, Card> {

    @Override
    public ArrayList<Card> getAll() {
        return null;
    }

    @Override
    public Card getById(UUID uuid) {
        return null;
    }

    @Override
    protected void refreshCache() {

    }
}
