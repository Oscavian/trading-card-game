package game.views;

import game.models.User;
import lombok.AccessLevel;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserView extends View {

    @Setter(AccessLevel.PRIVATE)
    private List<User> userData;

    public UserView() {
        setUserData(new ArrayList<>());
        userData.add(new User("100", "admin", "admin", 1000));
        userData.add(new User("200", "user", "123456", 500));
    }

    public User getUserByUuid(String uuid){
        return userData.stream()
                .filter(user -> Objects.equals(uuid, user.getUuid()))
                .findAny()
                .orElse(null);
    }

    public List<User> getUsers() {
        return userData;
    }

    public void addUser(User user) {
        userData.add(user);
    }

    public void removeUser(String uuid) {
        userData.removeIf(user -> Objects.equals(uuid, user.getUuid()));
    }
}
