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

        System.out.println(uuidUserCache);
        newCache.forEach((key, value) ->
                uuidUserCache.merge(
                        key, value,
                        (u1, u2) ->
                                u1.equals(u2) ? u1 : u2
                )
        );

        System.out.println(uuidUserCache);

    }

    public synchronized void refreshUsernameUserCache(HashMap<String, User> newCache) {

        System.out.println(usernameUserCache);
        newCache.forEach((key, value) ->
                usernameUserCache.merge(
                        key, value,
                        (u1, u2) ->
                                u1.equals(u2) ? u1 : u2
                )
        );

        System.out.println(usernameUserCache);

    }

    public synchronized void refreshUuidCardCache(HashMap<UUID, Card> newCache) {

        System.out.println(uuidCardCache);
        newCache.forEach((key, value) ->
                uuidCardCache.merge(
                        key, value,
                        (u1, u2) ->
                                u1.equals(u2) ? u1 : u2
                )
        );

        System.out.println(uuidCardCache);

    }

    public synchronized void refreshStackEntryCache(HashMap<UUID, StackDeckEntry> newCache) {

        System.out.println(uuidStackEntryCache);
        newCache.forEach((key, value) ->
                uuidStackEntryCache.merge(
                        key, value,
                        (u1, u2) ->
                                u1.equals(u2) ? u1 : u2
                )
        );

        System.out.println(uuidStackEntryCache);

    }

    public synchronized void refreshDeckEntryCache(HashMap<UUID, StackDeckEntry> newCache) {

        System.out.println(uuidDeckEntryCache);
        newCache.forEach((key, value) ->
                uuidDeckEntryCache.merge(
                        key, value,
                        (u1, u2) ->
                                u1.equals(u2) ? u1 : u2
                )
        );

        System.out.println(uuidDeckEntryCache);

    }
}
