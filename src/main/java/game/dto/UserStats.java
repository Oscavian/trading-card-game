package game.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserStats implements Comparable<UserStats> {

    @JsonAlias({"Name"})
    @JsonProperty("Name")
    private String name;

    @JsonAlias({"Elo"})
    @JsonProperty("Elo")
    private Integer elo;

    @JsonAlias({"Wins"})
    @JsonProperty("Wins")
    private int wins;

    @JsonAlias({"Losses"})
    @JsonProperty("Losses")
    private int losses;

    public UserStats(){}

    @Override
    public int compareTo(UserStats stats) {
        return getElo().compareTo(stats.getElo());
    }
}
