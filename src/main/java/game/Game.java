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

        if (path.matches("/users/?")){
            return this.userController.getUsers();
        }

        if (path.matches("/users/[A-Za-z0-9]+/?")) {
            return this.userController.getUserByName(request.getPathname().split("/")[2]);
        }


        return new Response(
                HttpStatus.NOT_FOUND,
                ContentType.JSON,
                null,
                "Not found"
        );
    }

    private Response handlePOST(Request request) {
        return switch (request.getPathname()) {
            case "/users" -> this.userController.registerUser(request.getBody());
            default -> new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.JSON,
                    null,
                    "Not found"
            );
        };
    }

    private Response handlePUT(Request request) {


        return new Response(
                HttpStatus.NOT_FOUND,
                ContentType.JSON,
                null,
                "Not found"
        );
    }

    private Response handleDELETE(Request request) {
        return new Response(
                HttpStatus.NOT_FOUND,
                ContentType.JSON,
                null,
                "Not found"
        );
    }
}
