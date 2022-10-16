package game;

import game.controllers.UserController;
import game.models.User;
import game.views.UserView;
import http.ContentType;
import http.HttpStatus;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.StandardException;
import server.Request;
import server.Response;
import server.ServerApp;

public class Game implements ServerApp {

    @Setter(AccessLevel.PRIVATE)
    private UserController userController;

    public Game() {
        setUserController(new UserController(new UserView()));
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
                    ContentType.TEXT,
                    "Method not allowed, use GET, POST, PUT or DELETE"
            );
        };
    }

    private Response handleGET(Request request) {

        return switch (request.getPathname()) {
            case "/users" -> this.userController.getUsers();
            default -> new Response(
                    HttpStatus.NOT_FOUND,
                    ContentType.JSON,
                    "{\"error\": \"Not Found\", \"data\": null}"
            );
        };
    }

    private Response handlePOST(Request request) {
        return new Response(
                HttpStatus.NOT_FOUND,
                ContentType.JSON,
                "{\"error\": \"Not Found\", \"data\": null}"
        );
    }

    private Response handlePUT(Request request) {

        return new Response(
                HttpStatus.NOT_FOUND,
                ContentType.JSON,
                "{\"error\": \"Not Found\", \"data\": null}"
        );
    }

    private Response handleDELETE(Request request) {
        return new Response(
                HttpStatus.NOT_FOUND,
                ContentType.JSON,
                "{\"error\": \"Not Found\", \"data\": null}"
        );
    }
}
