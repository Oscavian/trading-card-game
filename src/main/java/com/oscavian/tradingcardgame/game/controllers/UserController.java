package com.oscavian.tradingcardgame.game.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oscavian.tradingcardgame.game.dto.UserCredentials;
import com.oscavian.tradingcardgame.game.dto.UserData;
import com.oscavian.tradingcardgame.game.repos.UserRepo;
import com.oscavian.tradingcardgame.game.services.AuthService;
import com.oscavian.tradingcardgame.http.ContentType;
import com.oscavian.tradingcardgame.http.HttpStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import com.oscavian.tradingcardgame.server.Response;

import java.util.List;
import java.util.UUID;

@Setter(AccessLevel.PRIVATE)
@Getter(AccessLevel.PRIVATE)
public class UserController extends Controller {


    private UserRepo userRepo;
    private AuthService authService;

    public UserController(UserRepo userRepo, AuthService authService) {
        setUserRepo(userRepo);
        setAuthService(authService);
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
     *
     */
    public Response getUserByName(String name) {
        UserData user = getUserRepo().getUserDataByName(name);

        if (user != null) {
            try {
                String userDataJSON = getObjectMapper().writeValueAsString(user);
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


        } catch (JsonProcessingException | IllegalArgumentException e) {
            return new Response(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * PUT /users/{username}
     */
    public Response updateUser(String body, String username) {
        try {
            UserData userData = getObjectMapper().readValue(body, UserData.class);

            if (getUserRepo().updateUserData(userData, username)) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        null,
                        "User sucessfully updated."
                );
            } else {
                return new Response(HttpStatus.NOT_FOUND);
            }
        } catch (JsonProcessingException | IllegalArgumentException e) {
            return new Response(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * POST /sessions
     * @param body
     * @return
     */

    public Response loginUser(String body) {

        try {
            UserCredentials credentials = getObjectMapper().readValue(body, UserCredentials.class);

            if (getUserRepo().checkCredentials(credentials)) {
                var token = getAuthService().login(credentials.getUsername());

                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        token,
                        "User login successful"
                );
            }

            return new Response(
                    HttpStatus.UNAUTHORIZED,
                    ContentType.JSON,
                    null,
                    "Invalid username/password provided"
            );

        } catch (JsonProcessingException | IllegalArgumentException e) {
            return new Response(HttpStatus.BAD_REQUEST);
        }
    }

}
