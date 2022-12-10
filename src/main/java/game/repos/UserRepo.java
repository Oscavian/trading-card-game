package game.repos;

import game.dao.UserDao;
import game.models.user.User;
import game.models.user.UserCredentials;
import game.models.user.UserData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;
import java.util.ArrayList;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class UserRepo {

    UserDao userDao;
    ArrayList<User> userCache = new ArrayList<>();

    public UserRepo(UserDao userDao) {
        setUserDao(userDao);
    }

    public ArrayList<UserData> getAllUserData() {
        checkCache();
        refreshCache();

        var userData = new ArrayList<UserData>();

        for(var user : userCache) {
            userData.add(user.getUserData());
        }

        return userData;
    }

    public UserData getByName(String name) {
        checkCache();

        User user = userCache.stream()
                .filter(user1 -> name.equals(user1.getUsername()))
                .findAny()
                .orElse(null);

        if (user != null) {
            return user.getUserData();
        } else {
            return null;
        }
    }

    public User addUser(UserCredentials credentials) {
        User newUser = null;

        refreshCache();

        if (userCache.stream()
                .filter(user -> credentials.getUsername().equals(user.getUsername()))
                .findAny()
                .orElse(null) != null) {
            return null;
        }

        try {
            newUser = getUserDao().create(new User(credentials));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        refreshCache();
        return newUser;
    }

    private void checkCache() {
        if (userCache.isEmpty()) {
            try {
                setUserCache(getUserDao().read());
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    private void refreshCache() {
        try {
            setUserCache(getUserDao().read());
        } catch (SQLException e){
            e.printStackTrace();
        }
    }


}
