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

    public Response getUserByUuid(String uuid) {
        User user = getUserView().getUserByUuid(uuid);

        if (user != null){
            try {
                String userDataJSON = getObjectMapper().writeValueAsString(user);

                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        "\"data\": " + userDataJSON + ", \"error\": null"
                );

            } catch (JsonProcessingException e){
                e.printStackTrace();

                return new Response(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ContentType.JSON,
                        "\"error\": \"Internal Server Error\", \"data\": null"
                );
            }
        } else {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.JSON,
                    "\"data\": null, \"error\": UUID Not found"
            );
        }

    }

    public Response registerUser(String body) {
        User newUser;

        try {
            newUser = getObjectMapper().readValue(body, User.class);
            getUserView().addUser(newUser);

            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    "{\"msg\": \"success\"}"
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
