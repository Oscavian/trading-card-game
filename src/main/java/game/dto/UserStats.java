package game.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import game.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserStats {

    @JsonAlias({"Username"})
    private String username;

    @JsonAlias({"Elo"})
    private int elo;

    @JsonAlias({"Wins"})
    private int wins;

    @JsonAlias({"Losses"})
    private int losses;

    public UserStats(){}

    public UserStats(User user) {
        if (user == null) {
            return;
        }
        setUsername(user.getUsername());
        setElo(user.getElo());
        setWins(user.getWins());
        setLosses(user.getLosses());
    }

    /**
     * Converts an instance of UserStats to User.
     * USE WITH CAUTION as the returned instance does not map to the database.
     * @return an instance of User with the fields 'username', 'elo', 'wins' & 'losses' set.
     */
    public User toUser() {
        User u = new User();
        setUsername(getUsername());
        setElo(getElo());
        setWins(getWins());
        setLosses(getLosses());
        return u;
    }

}
