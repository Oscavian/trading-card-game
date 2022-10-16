package game.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import game.models.User;
import game.views.UserView;
import http.ContentType;
import http.HttpStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import server.Response;

import java.util.List;

public class UserController extends Controller {

    @Setter
    @Getter
    private UserView userView;

    public UserController(UserView userView){
        setUserView(userView);
    }

    public Response getUsers() {
        List<User> userData = getUserView().getUsers();
        try {
            String userDataJSON = getObjectMapper().writeValueAsString(userData);
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    "\"data\": " + userDataJSON + ", \"error\": null"
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "\"error\": \"Internal Server Error\", \"data\": null"
            );
        }
    }
}
