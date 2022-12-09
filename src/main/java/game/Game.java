package game;

import game.controllers.UserController;
import game.services.UserService;
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

    public static final String UUID_STRING = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";


    public Game() {
        setUserController(new UserController(new UserService()));
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

        if (path.equals("/users")){
            return this.userController.getUsers();
        }

        if (path.matches("/users/" + UUID_STRING)){
            return this.userController.getUserByUuid(request.getPathname().split("/")[2]);
        }

        /*
        return switch (request.getPathname()) {
            case "/users" -> this.userController.getUsers();
            case "/users/".matches()
            default -> new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.JSON,
                    "{\"error\": \"Not Found\", \"data\": null}"
            );
        };
         */
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
