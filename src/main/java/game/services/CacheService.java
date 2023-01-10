package game.services;

import game.models.Card;
import game.models.StackDeckEntry;
import game.models.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PACKAGE)
public class CacheService {
    private final Map<UUID, User> uuidUserCache = new ConcurrentHashMap<>();
    private final Map<String, User> usernameUserCache = new ConcurrentHashMap<>();
    private final Map<UUID, Card> uuidCardCache = new ConcurrentHashMap<>();
    private final Map<UUID, StackDeckEntry> uuidStackEntryCache = new ConcurrentHashMap<>();
    private final Map<UUID, StackDeckEntry> uuidDeckEntryCache = new ConcurrentHashMap<>();

    public void refreshUuidUserCache(HashMap<UUID, User> newCache) {

        refresh(newCache, uuidUserCache);
        //System.out.println("Cache<UUID, User> refreshed! Contents: \n" + uuidUserCache);
    }

    public void refreshUsernameUserCache(HashMap<String, User> newCache) {
        refresh(newCache, usernameUserCache);
        //System.out.println("Cache<Username, User> refreshed! Contents: \n" + usernameUserCache);
    }

    public void refreshUuidCardCache(HashMap<UUID, Card> newCache) {
        refresh(newCache, uuidCardCache);
        //System.out.println("Cache<UUID, Card> refreshed! Contents: \n" + uuidCardCache);
    }

    public void refreshStackEntryCache(HashMap<UUID, StackDeckEntry> newCache) {
        refresh(newCache, uuidStackEntryCache);
        //System.out.println("Cache<UUID, StackEntry> refreshed! Contents: \n" + uuidStackEntryCache);
    }

    public void refreshDeckEntryCache(HashMap<UUID, StackDeckEntry> newCache) {

        refresh(newCache, uuidDeckEntryCache);
        //System.out.println("Cache<UUID, DeckEntry> refreshed! Contents: \n" + uuidDeckEntryCache);
    }

    protected <ID, T> void refresh(Map<ID, T> newCache, Map<ID, T> oldCache) {

        //prune deleted entries
        if (!newCache.keySet().equals(oldCache.keySet())) {
            List<ID> oldKeys = new ArrayList<>();
            for (var key : oldCache.keySet()) {
                if (oldCache.containsKey(key) && !newCache.containsKey(key)) {
                    oldKeys.add(key);
                }
            }
            for (var key : oldKeys) {
                oldCache.remove(key);
            }
        }

        //merge new cache entries
        for (var entry : newCache.entrySet()) {
            oldCache.merge(
                    entry.getKey(), entry.getValue(),
                    (u1, u2) ->
                            u1.equals(u2) ? u1 : u2
            );
        }
    }
}
