package com.oscavian.tradingcardgame.game.repos;

import com.oscavian.tradingcardgame.game.dao.UserDao;
import com.oscavian.tradingcardgame.game.dto.UserStats;
import com.oscavian.tradingcardgame.game.models.User;
import com.oscavian.tradingcardgame.game.dto.UserCredentials;
import com.oscavian.tradingcardgame.game.dto.UserData;
import com.oscavian.tradingcardgame.game.services.CacheService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;
import java.util.*;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class UserRepo extends Repository<UUID, User> {

    private UserDao userDao;

    private CacheService cacheService;

    public UserRepo(UserDao userDao, CacheService cacheService) {
        setCacheService(cacheService);
        setUserDao(userDao);
        refreshCache();
    }

    @Override
    protected ArrayList<User> getAll() {
        checkCache();
        return new ArrayList<>(getCacheService().getUuidUserCache().values());
    }

    public ArrayList<UserData> getAllUserData() {
        checkCache();
        var list = new ArrayList<UserData>();
        getCacheService().getUuidUserCache().values().forEach((user) -> list.add(user.toUserData()));
        return list;
    }

    public ArrayList<UserStats> getAllUserStats() {
        checkCache();
        var list = new ArrayList<UserStats>();
        getCacheService().getUuidUserCache().values().forEach((user -> list.add(user.toUserStats())));

        Collections.sort(list);
        Collections.reverse(list);

        return list;
    }

    public boolean checkCredentials(UserCredentials credentials) {
        refreshCache();
        User foundUser = getCacheService().getUsernameUserCache().get(credentials.getUsername());

        if (foundUser != null) {
            return Objects.equals(credentials.getPassword(), foundUser.getPassword());
        }

        return false;
    }

    public UUID addUser(UserCredentials credentials) {
        checkCache();
        UUID uuid = null;

        //check for duplicate username
        if (getByName(credentials.getUsername()) != null) {
            return null;
        }

        try {
            uuid = getUserDao().create(credentials.toUser());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        refreshCache();
        return uuid;
    }

    public boolean updateUserData(UserData userData, String username) {

        checkCache();
        User user = getCacheService().getUsernameUserCache().get(username);

        if (user == null) {
            return false;
        }

        user.setFullname(userData.getName());
        user.setBio(userData.getBio());
        user.setImage(userData.getImage());

        try {
            getUserDao().update(user);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        refreshCache();
        return true;
    }

    public User getByName(String name) {
        if (name == null) {
            return null;
        }
        checkCache();
        return getCacheService().getUsernameUserCache().get(name);
    }

    public void updateUserStats(UserStats stats, UUID userid) {
        checkCache();

        User user = getCacheService().getUuidUserCache().get(userid);

        if (user == null) {
            return;
        }

        user.setElo(stats.getElo());
        user.setWins(stats.getWins());
        user.setLosses(stats.getLosses());

        try {
            getUserDao().update(user);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        refreshCache();
    }

    public UserData getUserDataByName(String name) {
        if (name == null) {
            return null;
        }
        checkCache();

        //Cache uses UUID as key, so
        User user = getCacheService().getUsernameUserCache().get(name);
        if (user != null) {
            return user.toUserData();
        } else {
            return null;
        }
    }

    @Override
    public User getById(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        checkCache();
        return getCacheService().getUuidUserCache().get(uuid);
    }

    @Override
    protected void refreshCache() {
        try {
            getCacheService().refreshUuidUserCache(getUserDao().read());
            getCacheService().refreshUsernameUserCache(getUserDao().read_returningMapByName());
            //setCache(getUserDao().read());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void checkCache() {
        if (getCacheService().getUuidUserCache().isEmpty() || getCacheService().getUsernameUserCache().isEmpty()) {
            refreshCache();
        }
    }
}
