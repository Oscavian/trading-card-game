package com.oscavian.tradingcardgame.game.services;

import com.oscavian.tradingcardgame.game.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestCacheService {

    CacheService cacheService;

    @BeforeEach
    void setup() { cacheService = new CacheService();}

    @Test
    @DisplayName("Test Cache Refreshing Updated Fields")
    void testRefreshCacheUpdatedField() {
        //arrange
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        User u1 = new User();
        u1.setUsername("osi");
        User u2 = new User();
        HashMap<UUID, User> cache = new HashMap<>();
        HashMap<UUID, User> newCache = new HashMap<>();
        newCache.put(id1, u1);
        newCache.put(id2, u2);

        //act
        cacheService.refresh(newCache, cache);
        newCache.get(id1).setUsername("admin");
        cacheService.refresh(newCache, cache);

        //assert
        //test if field got properly updated
        assertEquals("admin", cache.get(id1).getUsername());
    }

    @Test
    @DisplayName("Test Cache Refreshing for deleted fields")
    void testRefreshCacheDeletedEntry() {
        //arrange
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        User u1 = new User();
        User u2 = new User();
        HashMap<UUID, User> cache = new HashMap<>();
        HashMap<UUID, User> newCache = new HashMap<>();
        newCache.put(id1, u1);
        newCache.put(id2, u2);

        //act
        cacheService.refresh(newCache, cache);
        newCache.remove(id2);
        cacheService.refresh(newCache, cache);

        //assert
        //test if entry got removed
        assertFalse(cache.containsKey(id2));
    }






}
