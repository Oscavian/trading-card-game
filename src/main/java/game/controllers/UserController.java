package game.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import game.models.User;
import game.services.UserService;
import http.ContentType;
import http.HttpStatus;
import lombok.Getter;
import lombok.Setter;
import server.Response;

import java.util.List;
import java.util.UUID;

public class UserController extends Controller {

    @Setter
    @Getter
    private UserService userService;

    public UserController(UserService userService){
        setUserService(userService);
    }

    public Response getUsers() {
        List<User> userData = getUserService().getUsers();
        try {
            String userDataJSON = getObjectMapper().writeValueAsString(userData);
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    "{\"data\": " + userDataJSON + ", \"error\": null}"
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{\"error\": \"Internal Server Error\", \"data\": null}"
            );
        }
    }

    public Response getUserByUuid(String uuid) {
        User user = getUserService().getUserByUuid(UUID.fromString(uuid));

        if (user != null){
            try {
                String userDataJSON = getObjectMapper().writeValueAsString(user);
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        "{\"data\": " + userDataJSON + ", \"error\": null}"
                );

            } catch (JsonProcessingException e){
                e.printStackTrace();

                return new Response(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ContentType.JSON,
                        "{\"error\": \"Internal Server Error\", \"data\": null}"
                );
            }
        } else {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.JSON,
                    "{\"data\": null, \"error\": \"UUID Not found\"}"
            );
        }

    }

    public Response registerUser(String body) {
        User newUser;

        try {
            newUser = getObjectMapper().readValue(body, User.class);
            newUser = getUserService().addUser(newUser);

            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    "{\"error\": \"null\", \"uuid\":\"" + newUser.getUuid().toString() + "\"}"
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{\"error\": \"Internal Server Error\", \"data\": null}"
            );
        }



    }
}
