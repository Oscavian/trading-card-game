package game.models;

import game.dto.UserCredentials;
import game.dto.UserData;
import game.dto.UserStats;
import lombok.*;

import java.util.ArrayList;
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

    private int elo = 0;

    private int wins = 0;
    private int losses = 0;

    private int coins = 0;

    private ArrayList<Card> deck = new ArrayList<>();

    private ArrayList<Card> stack = new ArrayList<>();

    public User(UUID id, String username, String password, String bio, String image, int elo, int wins, int losses, int coins) {
        setId(id);
        setUsername(username);
        setPassword(password);
        setBio(bio);
        setImage(image);
        setElo(elo);
        setWins(wins);
        setLosses(losses);
        setCoins(coins);
    }

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
