package game.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    @JsonAlias({"uuid"})
    private String uuid;
    @JsonAlias({"username"})
    private String username;
    @JsonAlias({"password"})
    private String password;
    @JsonAlias({"elo"})
    private int elo;

    User(){}
}
