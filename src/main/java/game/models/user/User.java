package game.models.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private UUID id;
    private String username;
    private String password;

    private String bio;
    private String image;

    private int elo;

    private int wins;

    private int losses;

    public User(UserCredentials credentials) {
        setUsername(credentials.getUsername());
        setPassword(credentials.getPassword());
    }

    public UserCredentials getUserCredentials() {
        return new UserCredentials(username, password);
    }

    public UserData getUserData() {
        return new UserData(username, bio, image);
    }

    public UserStats getUserStats() {
        return new UserStats(username, elo, wins, losses);
    }
}
