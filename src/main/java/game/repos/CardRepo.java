package game.repos;

import game.dao.CardDao;
import game.dao.DeckEntryDao;
import game.dao.PackageDao;
import game.dao.StackEntryDao;
import game.models.Card;
import game.models.Package;
import game.models.StackDeckEntry;
import game.models.User;
import game.services.CacheService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class CardRepo extends Repository<UUID, Card> {

    private CardDao cardDao;
    private StackEntryDao stackEntryDao;
    private DeckEntryDao deckEntryDao;

    private PackageDao packageDao;
    private CacheService cacheService;

    public CardRepo(CardDao cardDao, StackEntryDao stackEntryDao, DeckEntryDao deckEntryDao, PackageDao packageDao, CacheService cacheService) {
        setCardDao(cardDao);
        setStackEntryDao(stackEntryDao);
        setDeckEntryDao(deckEntryDao);
        setPackageDao(packageDao);
        setCacheService(cacheService);
        refreshCache();
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

        User user = getCacheService().getUsernameUserCache().get(username);

        if (user == null) {
            return null;
        }

        // join stack entries together by matching the user id
        var stackEntries = getCacheService().getUuidStackEntryCache().values().stream()
                .filter(stackEntry -> user.getId().equals(stackEntry.getUser_uuid()))
                .toList();

        //get the corresponding card objects
        for (var entry : stackEntries) {
            stack.add(getById(entry.getCard_uuid()));
        }

        return stack;
    }


    public ArrayList<Card> getUserDeck(String username) {
        checkCache();

        var deck = new ArrayList<Card>();

        User user = getCacheService().getUsernameUserCache().get(username);

        if (user == null) {
            return null;
        }

        // join deck entries together by matching the user id
        var deckEntries = getCacheService().getUuidDeckEntryCache().values().stream()
                .filter(deckEntry -> user.getId().equals(deckEntry.getUser_uuid()))
                .toList();

        //get the corresponding card objects
        for (var entry : deckEntries) {
            deck.add(getById(entry.getCard_uuid()));
        }

        return deck;
    }

    public boolean setUserDeck(List<UUID> cardIds, String username) {
        checkCache();

        if (cardIds.isEmpty()) {
            return false;
        }

        var cards = new ArrayList<Card>();

        User user = getCacheService().getUsernameUserCache().get(username);

        //get cards from cache
        cardIds.forEach(uuid -> cards.add(getCacheService().getUuidCardCache().get(uuid)));

        //check if cards all belong to user
        for (var id : cardIds) {
            //check if card exists
            Card card = getCacheService().getUuidCardCache().get(id);

            if (card == null) {
                return false;
            }

            //check if card is in a stack
            var stackEntry = getCacheService().getUuidStackEntryCache().values().stream().filter(
                            (entry) -> entry.getCard_uuid().equals(card.getId()))
                    .findAny()
                    .orElse(null);

            if (stackEntry == null) {
                return false;
            }

            //check if it belongs to the right user
            if (!stackEntry.getUser_uuid().equals(user.getId())) {
                return false;
            }
        }

        //all cards okay, proceed

        //clear user's deck
        for (var entry : getCacheService().getUuidDeckEntryCache().values()) {
            if (entry.getUser_uuid().equals(user.getId())) {
                try {
                    getDeckEntryDao().delete(entry);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        //configure deck with new cards
        cards.forEach((card -> {
            try {
                getDeckEntryDao().create(new StackDeckEntry(null, user.getId(), card.getId()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }));

        refreshCache();

        return true;
    }

    public boolean createPackage(List<Card> cards) {
        checkCache();

        //check if any provided card already exists
        for (var card : cards) {
            if (getCacheService().getUuidCardCache().containsKey(card.getId())) {
                return false;
            }
        }

        //create cards
        cards.forEach((card) -> {
            try {
                if (card.getId() == null) {
                    card.setId(getCardDao().create(card));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });



        try {
            //create package
            getPackageDao().create(new Package(null, null, cards));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
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
            getCacheService().refreshDeckEntryCache(getDeckEntryDao().read());
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
