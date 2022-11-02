package game.services;

import game.models.User;
import lombok.AccessLevel;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class UserService extends View {

    @Setter(AccessLevel.PRIVATE)
    private List<User> userData;

    public UserService() {
        setUserData(new ArrayList<>());
        userData.add(new User(UUID.fromString("a62e69b8-f6b4-40e0-9ff8-aceb80a40f17"), "admin", "admin", 1000));
        userData.add(new User(UUID.fromString("cdaf29ff-c68d-45c4-8a7e-86f49d537e4f"), "user", "123456", 500));
    }

    public User getUserByUuid(UUID uuid){
        return userData.stream()
                .filter(user -> Objects.equals(uuid, user.getUuid()))
                .findAny()
                .orElse(null);
    }

    public List<User> getUsers() {
        return userData;
    }

    public User addUser(User user) {
        user.setUuid(UUID.randomUUID());
        userData.add(user);
        return user;
    }

    public void removeUser(UUID uuid) {
        userData.removeIf(user -> Objects.equals(uuid, user.getUuid()));
    }
}
