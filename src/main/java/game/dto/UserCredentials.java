package game.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import game.models.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserCredentials {

    @JsonAlias({"Username"})
    private String username;
    @JsonAlias({"Password"})
    private String password;
    UserCredentials(){}

    public UserCredentials(User user) {
        if (user == null) {
            return;
        }
        setUsername(user.getUsername());
        setPassword(user.getPassword());
    }

    /**
     * Converts an instance of UserCredentials to User.
     * USE WITH CAUTION as the returned instance does not map to the database.
     * @return an instance of User with the fields 'username' & 'password' set.
     */
    public User toUser() {
        User u = new User();
        u.setUsername(getUsername());
        u.setPassword(getPassword());
        return u;
    }

}
