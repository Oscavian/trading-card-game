package dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import game.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserData {
    @JsonAlias({"Username"})
    private String username;
    @JsonAlias({"Bio"})
    private String bio;
    @JsonAlias({"Image"})
    private String image;

    UserData(){}

    UserData(User user) {
        setUsername(user.getUsername());
        setBio(user.getBio());
        setImage(user.getImage());
    }
}
