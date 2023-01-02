package game;

import game.controllers.CardController;
import game.controllers.UserController;
import game.dao.CardDao;
import game.dao.StackEntryDao;
import game.dao.UserDao;
import game.repos.CardRepo;
import game.repos.UserRepo;
import game.services.AuthService;
import game.services.CacheService;
import game.services.DatabaseService;
import http.ContentType;
import http.HttpMethod;
import http.HttpStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import server.Request;
import server.Response;
import server.ServerApp;

import java.util.HashMap;

@Setter(AccessLevel.PRIVATE)
public class Game implements ServerApp {


    //CONTROLLER & SERVICES
    private UserController userController;
    private CardController cardController;
    private DatabaseService databaseService = new DatabaseService();
    private AuthService authService = new AuthService();

    private final String UUID_REGEX = "^[0-9a-fA-F]{8}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{12}$";

    @Getter(AccessLevel.PRIVATE)
    private String userLogin;

    public Game() {
        setUserController(
                new UserController(
                        new UserRepo(
                                new UserDao(databaseService.getConnection()),
                                new CacheService()
                        ),
                        authService
                )
        );
        setCardController(
                new CardController(
                        new CardRepo(
                                new CardDao(databaseService.getConnection()),
                                new StackEntryDao(databaseService.getConnection()),
                                new CacheService()
                        ),
                        authService
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

        if (path.matches("/users/?")) {
            return this.userController.getUsers();
        }

        if (path.matches("/users/[A-Za-z0-9]+/?")) {
            return this.userController.getUserByName(request.getPathname().split("/")[2]);
        }

        if (path.matches("/users/" + UUID_REGEX + "/?")) {
            return null;
        }

        if (path.matches("/cards/?")) {
            return this.cardController.getCards(getUserLogin());
        }

        if (path.matches("/decks/?")) {
            return null;
        }

        if (path.matches("/stats/?")) {
            return null;
        }

        if (path.matches("/scores/?")) {
            return null;
        }

        if (path.matches("/tradings/?")) {
            return null;
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
            return null;
        }

        if (path.matches("/transactions/packages?")) {
            return null;
        }

        if (path.matches("/battles/?")) {
            return null;
        }

        if (path.matches("/tradings/?")) {
            return null;
        }

        if (path.matches("/tradings/" + UUID_REGEX + "/?")) {
            return null;
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
            return this.userController.updateUser(request.getBody());
        }

        if (path.matches("/decks/?")) {
            return null;
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
            return null;
        }

        return new Response(
                HttpStatus.NOT_FOUND,
                ContentType.JSON,
                null,
                "Not found"
        );
    }
}
