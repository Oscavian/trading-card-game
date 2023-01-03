package game.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import game.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserData {
    @JsonAlias({"Name"})
    private String username;
    @JsonAlias({"Bio"})
    private String bio;
    @JsonAlias({"Image"})
    private String image;

    UserData(){}

    public UserData(User user) {
        if (user == null) {
            return;
        }
        setUsername(user.getUsername());
        setBio(user.getBio());
        setImage(user.getImage());
    }

    /**
     * Converts an instance of UserData to User.
     * USE WITH CAUTION as the returned instance does not map to the database.
     * @return an instance of User with the fields 'username', 'bio' & 'image' set.
     */
    public User toUser() {
        User u = new User();
        u.setUsername(getUsername());
        u.setBio(getBio());
        u.setImage(getImage());
        return u;
    }
}
