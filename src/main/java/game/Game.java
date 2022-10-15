package game;

import http.ContentType;
import http.HttpStatus;
import server.Request;
import server.Response;
import server.ServerApp;

public class Game implements ServerApp {
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
        return new Response(
                HttpStatus.NOT_FOUND,
                ContentType.JSON,
                "{\"error\": \"Not Found\", \"data\": null}"
        );
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
