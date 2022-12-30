package game.repos;

import game.dao.UserDao;
import game.models.User;
import game.dto.UserCredentials;
import game.dto.UserData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class UserRepo {

    private UserDao userDao;
    private ConcurrentHashMap<UUID, User> userCache = new ConcurrentHashMap<>();

    public UserRepo(UserDao userDao) {
        setUserDao(userDao);
    }

    public ArrayList<UserData> getAllUserData() {
        checkCache();

        var userData = new ArrayList<UserData>();

        for(var user : userCache.values()) {
            userData.add(user.toUserData());
        }

        return userData;
    }

    public UUID addUser(UserCredentials credentials) {
        checkCache();
        UUID uuid = null;

        //check for duplicate username
        if (getByName(credentials.getUsername()) != null){
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

    public boolean updateUser(UserData userData) {

        checkCache();

        //Check if given user exists
        if (getByName(userData.getUsername()) == null) {
            return false;
        }

        try {
            getUserDao().update(userData.toUser());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        refreshCache();
        return true;
    }

    public UserData getByName(String name) {
        if (name == null) {
            return null;
        }
        checkCache();

        //Cache uses UUID as key, so
        User user = userCache.values().stream()
                .filter(user1 -> name.equals(user1.getUsername()))
                .findAny()
                .orElse(null);

        if (user != null) {
            return user.toUserData();
        } else {
            return null;
        }
    }

    public UserData getById(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        checkCache();
        return new UserData(userCache.get(uuid));

    }

    private void checkCache() {
        if (userCache.isEmpty()) {
            try {
                setUserCache(new ConcurrentHashMap<>(getUserDao().read()));
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    private void refreshCache() {
        try {
            setUserCache(new ConcurrentHashMap<>(getUserDao().read()));
        } catch (SQLException e){
            e.printStackTrace();
        }
    }


}
