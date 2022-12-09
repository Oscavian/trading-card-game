package game.models.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserCredentials {

    @JsonAlias({"Name"})
    private String username;
    @JsonAlias({"Password"})
    private String password;
    UserCredentials(){}

}
