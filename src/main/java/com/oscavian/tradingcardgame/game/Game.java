package com.oscavian.tradingcardgame.game;

import com.oscavian.tradingcardgame.game.controllers.BattleController;
import com.oscavian.tradingcardgame.game.controllers.CardController;
import com.oscavian.tradingcardgame.game.controllers.UserController;
import com.oscavian.tradingcardgame.game.dao.*;
import com.oscavian.tradingcardgame.game.repos.CardRepo;
import com.oscavian.tradingcardgame.game.repos.UserRepo;
import com.oscavian.tradingcardgame.game.services.AuthService;
import com.oscavian.tradingcardgame.game.services.CacheService;
import com.oscavian.tradingcardgame.game.services.DatabaseService;
import com.oscavian.tradingcardgame.http.ContentType;
import com.oscavian.tradingcardgame.http.HttpMethod;
import com.oscavian.tradingcardgame.http.HttpStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import com.oscavian.tradingcardgame.server.Request;
import com.oscavian.tradingcardgame.server.Response;
import com.oscavian.tradingcardgame.server.ServerApp;

import java.io.IOException;

@Setter(AccessLevel.PRIVATE)
@Getter
public class Game implements ServerApp {


    //CONTROLLER
    private UserController userController;
    private CardController cardController;
    private BattleController battleController;

    //SERVICES
    private DatabaseService databaseService = new DatabaseService();
    private AuthService authService = new AuthService();
    private CacheService cacheService = new CacheService();

    private final String UUID_REGEX = "^[0-9a-fA-F]{8}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{12}$";

    @Getter(AccessLevel.PRIVATE)
    private String userLogin;

    public Game() throws IOException {
        setUserController(
                new UserController(
                        new UserRepo(
                                new UserDao(databaseService.getConnection()),
                                getCacheService()
                        ),
                        getAuthService()
                )
        );
        setCardController(
                new CardController(
                        new CardRepo(
                                new CardDao(databaseService.getConnection()),
                                new UserDao(databaseService.getConnection()),
                                new StackEntryDao(databaseService.getConnection()),
                                new DeckEntryDao(databaseService.getConnection()),
                                new PackageDao(databaseService.getConnection()),
                                getCacheService()
                        )
                )
        );
        setBattleController(
                new BattleController(
                        new UserRepo(
                                new UserDao(databaseService.getConnection()),
                                getCacheService()
                        ),
                        new CardRepo(
                                new CardDao(databaseService.getConnection()),
                                new UserDao(databaseService.getConnection()),
                                new StackEntryDao(databaseService.getConnection()),
                                new DeckEntryDao(databaseService.getConnection()),
                                new PackageDao(databaseService.getConnection()),
                                getCacheService()
                        )
                )
        );
    }

    @Override
    public Response handleRequest(Request request) {

        setUserLogin(authService.getLogin(request.getAuthorization()));

        if (userLogin == null && request.getMethod() == HttpMethod.POST) {
            if (request.getPathname().matches("/users/?")) {
                return this.userController.registerUser(request.getBody());
            }

            if (request.getPathname().matches("/sessions/?")) {
                return this.userController.loginUser(request.getBody());
            }
        }

        if (userLogin == null) {
            return new Response(HttpStatus.UNAUTHORIZED);
        }

        return switch (request.getMethod()) {
            case GET -> handleGET(request);
            case POST -> handlePOST(request);
            case PUT -> handlePUT(request);
            case DELETE -> handleDELETE(request);
            default -> new Response(
                    HttpStatus.METHOD_NOT_ALLOWED,
                    ContentType.JSON,
                    null,
                    "Method not allowed, use GET, POST, PUT or DELETE"
            );
        };
    }

    private Response handleGET(Request request) {
        String path = request.getPathname();

        //only allowed for admin
        if (path.matches("/users/?")) {
            if (userLogin.equals("admin")) {
                return this.userController.getUsers();
            } else {
                return new Response(HttpStatus.UNAUTHORIZED);
            }
        }

        // Only allowed for admin or maching user
        if (path.matches("/users/[A-Za-z0-9]+/?")) {
            if (userLogin.equals("admin") || userLogin.equals(path.split("/")[2])) {
                return this.userController.getUserByName(path.split("/")[2]);
            } else {
                return new Response(HttpStatus.UNAUTHORIZED);
            }
        }

        if (path.matches("/users/" + UUID_REGEX + "/?")) {
            return new Response(HttpStatus.NOT_FOUND);
        }

        if (path.matches("/cards/?")) {
            return this.cardController.getCards(getUserLogin());
        }

        if (path.matches("/decks/?")) {
            return cardController.getDeck(userLogin);
        }

        if (path.matches("/stats/?")) {
            return battleController.getStats(userLogin);
        }

        if (path.matches("/scores/?")) {
            return battleController.getScores();
        }

        if (path.matches("/tradings/?")) {
            return new Response(HttpStatus.NOT_FOUND);
        }

        return new Response(
                HttpStatus.NOT_FOUND,
                ContentType.JSON,
                null,
                "Not found"
        );
    }

    private Response handlePOST(Request request) {
        String path = request.getPathname();


        if (path.matches("/packages/?")) {
            if (userLogin.equals("admin")) {
                return cardController.postPackage(request.getBody());
            } else {
                return new Response(HttpStatus.FORBIDDEN);
            }
        }

        if (path.matches("/transactions/packages?")) {
            return cardController.postTransactionsPackages(userLogin);
        }

        if (path.matches("/battles/?")) {
            return battleController.postBattle(userLogin);
        }

        if (path.matches("/tradings/?")) {
            return new Response(HttpStatus.NOT_FOUND);
        }

        if (path.matches("/tradings/" + UUID_REGEX + "/?")) {
            return new Response(HttpStatus.NOT_FOUND);
        }

        return new Response(
                HttpStatus.NOT_FOUND,
                ContentType.JSON,
                null,
                "Not found"
        );
    }

    private Response handlePUT(Request request) {

        String path = request.getPathname();

        if (path.matches("/users/[A-Za-z0-9]+/?")) {
            if (userLogin.equals("admin") || userLogin.equals(path.split("/")[2])){
                return this.userController.updateUser(request.getBody(), path.split("/")[2]);
            } else {
                return new Response(HttpStatus.UNAUTHORIZED);
            }
        }

        if (path.matches("/decks/?")) {
            return cardController.putDeck(request.getBody(), userLogin);
        }

        return new Response(
                HttpStatus.NOT_FOUND,
                ContentType.JSON,
                null,
                "Not found"
        );
    }

    private Response handleDELETE(Request request) {

        if (request.getPathname().matches("/tradings/" + UUID_REGEX + "/?")) {
            return new Response(HttpStatus.NOT_FOUND);
        }

        return new Response(
                HttpStatus.NOT_FOUND,
                ContentType.JSON,
                null,
                "Not found"
        );
    }
}
