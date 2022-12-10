package game.models.user;

import com.fasterxml.jackson.annotation.JsonAlias;
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
}
