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
import java.util.HashMap;
import java.util.UUID;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class UserRepo extends Repository<UUID, UserData> {

    private UserDao userDao;

    public UserRepo(UserDao userDao) {
        super();
        setUserDao(userDao);
    }

    @Override
    protected ArrayList<UserData> getAll() {
        return getAllUserData();
    }

    public ArrayList<UserData> getAllUserData() {
        checkCache();
        return new ArrayList<>(cache.values());
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
        return getCache().values().stream()
                .filter(user1 -> name.equals(user1.getUsername()))
                .findAny().orElse(null);

    }

    @Override
    public UserData getById(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        checkCache();
        return getCache().get(uuid);

    }

    @Override
    protected void refreshCache() {
        try {
            setUserCache(getUserDao().read());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setUserCache(HashMap<UUID, User> cache) {
        getCache().clear();
        for (var user : cache.values()) {
            getCache().put(user.getId(), user.toUserData());
        }
    }
}
