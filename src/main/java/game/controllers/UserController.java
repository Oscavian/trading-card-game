package game.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import game.models.user.User;
import game.models.user.UserCredentials;
import game.models.user.UserData;
import game.repos.UserRepo;
import game.services.DatabaseService;
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
    private UserRepo userRepo;

    public UserController(UserRepo userRepo){
        setUserRepo(userRepo);
    }

    /**
     * GET /users
     * *Temporary*
     */
    public Response getUsers() {
        List<UserData> userData = getUserRepo().getAllUserData();
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

    /**
     * GET /users/{username}
     * @param name
     * @return
     */
    public Response getUserByName(String name) {
        UserData user = getUserRepo().getByName(name);

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

    /**
     * POST /users
     * @param body
     * @return
     */
    public Response registerUser(String body) {
        User newUser;
        try {
            newUser = getUserRepo().addUser(getObjectMapper().readValue(body, UserCredentials.class));

            if (newUser != null) {
                String uuid = getObjectMapper().writeValueAsString(newUser.getId());
                return new Response(
                        HttpStatus.CREATED,
                        ContentType.JSON,
                        uuid,
                        "User successfully created"
                );

            } else {
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        null,
                        "User with same username already registered"
                );
            }


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
