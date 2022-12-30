package game;

import game.controllers.UserController;
import game.dao.UserDao;
import game.repos.UserRepo;
import game.services.DatabaseService;
import http.ContentType;
import http.HttpStatus;
import lombok.AccessLevel;
import lombok.Setter;
import server.Request;
import server.Response;
import server.ServerApp;


public class Game implements ServerApp {


    //CONTROLLER
    @Setter(AccessLevel.PRIVATE)
    private UserController userController;
    @Setter(AccessLevel.PRIVATE)
    private DatabaseService databaseService = new DatabaseService();

    private final String UUID_REGEX = "^[0-9a-fA-F]{8}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{12}$";

    public Game() {
        setUserController(new UserController(new UserRepo(new UserDao(databaseService.getConnection()))));
    }

    @Override
    public Response handleRequest(Request request) {

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
            return null;
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

        if (path.matches("/users/?")) {
            return this.userController.registerUser(request.getBody());
        }

        if (path.matches("/sessions/?")) {
            return null;
        }

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
