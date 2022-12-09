package game.services;

import game.models.user.UserData;
import lombok.AccessLevel;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class UserService extends Service {
    /*
    @Setter(AccessLevel.PRIVATE)
    private List<User> userData;

    public UserService() {
        setUserData(new ArrayList<>());
        userData.add(new UserData("oskar", "hi there!", "B-)"));
        userData.add(new UserData("kekl", "I'm superior!", "^o^"));
    }

    public UserData getUserByName(String name){
        return userData.stream()
                .filter(user -> Objects.equals(name, user.getUsername()))
                .findAny()
                .orElse(null);
    }

    public List<UserData> getUsers() {
        return userData;
    }

    public UserData addUser(UserData user) {
        userData.add(user);
        return user;
    }

    public void removeUser(String name) {
        userData.removeIf(user -> Objects.equals(name, user.getUsername()));
    }
    */
}
