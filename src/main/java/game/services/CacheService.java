package game.services;

import game.models.Card;
import game.models.StackEntry;
import game.models.User;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter(AccessLevel.PUBLIC)
public class CacheService {
    private final Map<UUID, User> uuidUserCache = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, User> usernameUserCache = Collections.synchronizedMap(new HashMap<>());
    private final Map<UUID, Card> uuidCardCache = Collections.synchronizedMap(new HashMap<>());

    private final Map<UUID, StackEntry> uuidStackEntryCache = Collections.synchronizedMap(new HashMap<>());

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

    public synchronized void refreshStackEntryCache(HashMap<UUID, StackEntry> newCache) {

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
}
