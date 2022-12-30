package game.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import game.dto.UserCredentials;
import game.dto.UserData;
import game.repos.UserRepo;
import http.ContentType;
import http.HttpStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import server.Response;

import java.util.List;
import java.util.UUID;

public class UserController extends Controller {

    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
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

            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /users/{username}
     * TODO: auth (401)
     *
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

                return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
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
     *
     */
    public Response registerUser(String body) {
        UUID uuid;
        try {
            uuid = getUserRepo().addUser(getObjectMapper().readValue(body, UserCredentials.class));

            if (uuid != null) {
                String uuid_string = getObjectMapper().writeValueAsString(uuid.toString());
                return new Response(
                        HttpStatus.CREATED,
                        ContentType.JSON,
                        uuid_string,
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

            return new Response(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * PUT /users/{username}
     * TODO: add auth (401)
     *
     */
    public Response updateUser(String body) {
        try {
            if (getUserRepo().updateUser(getObjectMapper().readValue(body, UserData.class))) {
               return new Response(
                       HttpStatus.OK,
                       ContentType.JSON,
                       null,
                       "User sucessfully updated."
               );
            } else {
                return new Response(HttpStatus.NOT_FOUND);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
