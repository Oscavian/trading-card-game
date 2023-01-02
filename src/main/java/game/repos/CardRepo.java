package game.repos;

import game.dao.CardDao;
import game.dao.StackEntryDao;
import game.dao.UserDao;
import game.models.Card;
import game.models.StackEntry;
import game.models.User;
import game.services.CacheService;
import game.services.DatabaseService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class CardRepo extends Repository<UUID, Card> {

    private CardDao cardDao;
    private StackEntryDao stackEntryDao;
    private CacheService cacheService;

    public CardRepo(CardDao cardDao, StackEntryDao stackEntryDao, CacheService cacheService) {
        setCardDao(cardDao);
        setStackEntryDao(stackEntryDao);
        setCacheService(cacheService);
    }

    @Override
    public ArrayList<Card> getAll() {
        checkCache();
        return new ArrayList<>(getCacheService().getUuidCardCache().values());
    }

    public ArrayList<Card> getUserStack(String username) {
        checkCache();

        if (username == null) {
            return null;
        }

        var stack = new ArrayList<Card>();

        //TODO: REMOVE
        User user = null;
        try {
            user = new UserDao(new DatabaseService().getConnection()).read().values().stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO: write more efficient

        // join stack entries together by matching the user id
        User finalUser = user;
        var stackEntries = getCacheService().getUuidStackEntryCache().values().stream()
                .filter(stackEntry -> {
                    assert finalUser != null;
                    return finalUser.getId().equals(stackEntry.getUser_uuid());
                })
                .toList();

        //get the corresponding card objects
        for (var entry : stackEntries) {
            stack.add(getById(entry.getCard_uuid()));
        }

        return stack;
    }

    @Override
    public Card getById(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        checkCache();
        return getCacheService().getUuidCardCache().get(uuid);
    }

    @Override
    protected void refreshCache() {
        try {
            getCacheService().refreshUuidCardCache(getCardDao().read());
            getCacheService().refreshStackEntryCache(getStackEntryDao().read());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void checkCache() {
        if (getCacheService().getUuidCardCache().isEmpty()) {
            refreshCache();
        }
    }
}
