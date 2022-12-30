package game.models.user;

import game.dto.UserCredentials;
import game.dto.UserData;
import game.dto.UserStats;
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

    private Integer elo;

    private Integer wins;

    private Integer losses;

    public UserCredentials toUserCredentials() {
        return new UserCredentials(username, password);
    }

    public UserData toUserData() {
        return new UserData(username, bio, image);
    }

    public UserStats toUserStats() {
        return new UserStats(username, elo, wins, losses);
    }
}
