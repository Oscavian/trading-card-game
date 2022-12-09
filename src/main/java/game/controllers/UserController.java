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
                    userDataJSON,
                    "Data retrieved."
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    null,
                    "Internal Server error"
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
                        userDataJSON,
                        "Data retrieved."
                );

            } catch (JsonProcessingException e){
                e.printStackTrace();

                return new Response(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ContentType.JSON,
                        null,
                        "Internal Server Error"
                );
            }
        } else {
            return new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.JSON,
                    null,
                    "UUID not found"
            );
        }

    }

    public Response registerUser(String body) {
        User newUser;

        try {
            newUser = getObjectMapper().readValue(body, User.class);
            newUser = getUserService().addUser(newUser);

            String uuid = getObjectMapper().writeValueAsString(newUser.getUuid());

            return new Response(
                    HttpStatus.CREATED,
                    ContentType.JSON,
                    uuid,
                    "User successfully created"

            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    null,
                    "Internal Server error"

            );
        }



    }
}
