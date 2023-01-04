package game.services;

import game.models.Card;
import game.models.StackDeckEntry;
import game.models.User;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.*;

@Getter(AccessLevel.PUBLIC)
public class CacheService {
    private final Map<UUID, User> uuidUserCache = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, User> usernameUserCache = Collections.synchronizedMap(new HashMap<>());
    private final Map<UUID, Card> uuidCardCache = Collections.synchronizedMap(new HashMap<>());
    private final Map<UUID, StackDeckEntry> uuidStackEntryCache = Collections.synchronizedMap(new HashMap<>());
    private final Map<UUID, StackDeckEntry> uuidDeckEntryCache = Collections.synchronizedMap(new HashMap<>());


    public synchronized void refreshUuidUserCache(HashMap<UUID, User> newCache) {

        newCache.forEach((key, value) ->
                uuidUserCache.merge(
                        key, value,
                        (u1, u2) ->
                                u1.equals(u2) ? u1 : u2
                )
        );

        System.out.println("Cache<UUID, User> refreshed! Contents: \n" + uuidUserCache);

    }

    public synchronized void refreshUsernameUserCache(HashMap<String, User> newCache) {

        newCache.forEach((key, value) ->
                usernameUserCache.merge(
                        key, value,
                        (u1, u2) ->
                                u1.equals(u2) ? u1 : u2
                )
        );

        System.out.println("Cache<Username, User> refreshed! Contents: \n" + usernameUserCache);

    }

    public synchronized void refreshUuidCardCache(HashMap<UUID, Card> newCache) {

        newCache.forEach((key, value) ->
                uuidCardCache.merge(
                        key, value,
                        (u1, u2) ->
                                u1.equals(u2) ? u1 : u2
                )
        );

        System.out.println("Cache<UUID, Card> refreshed! Contents: \n" + uuidCardCache);

    }

    public synchronized void refreshStackEntryCache(HashMap<UUID, StackDeckEntry> newCache) {

        newCache.forEach((key, value) ->
                uuidStackEntryCache.merge(
                        key, value,
                        (u1, u2) ->
                                u1.equals(u2) ? u1 : u2
                )
        );

        System.out.println("Cache<UUID, StackEntry> refreshed! Contents: \n" + uuidStackEntryCache);


    }

    public synchronized void refreshDeckEntryCache(HashMap<UUID, StackDeckEntry> newCache) {

        newCache.forEach((key, value) ->
                uuidDeckEntryCache.merge(
                        key, value,
                        (u1, u2) ->
                                u1.equals(u2) ? u1 : u2
                )
        );

        System.out.println("Cache<UUID, DeckEntry> refreshed! Contents: \n" + uuidDeckEntryCache);


    }
}
