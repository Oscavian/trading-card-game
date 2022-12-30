package server;

import game.Game;
import http.ContentType;
import http.HttpStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class RequestHandler implements Runnable {

    private Socket clientSocket;
    private PrintWriter outputStream;
    private BufferedReader inputStream;
    private Game game;

    RequestHandler(Game game, Socket clientSocket){
        setGame(game);
        setClientSocket(clientSocket);
    }

    @Override
    public void run() {
        System.out.println("Executing in " + Thread.currentThread().getName());
        try {
            Request req = getRequest();
            System.out.println(req.getMethod() + " " + req.getPathname());

            Response res = getResponse(req);

            sendResponse(res);

        } catch (IOException | IllegalArgumentException e) {
            sendResponse(new Response(HttpStatus.INTERNAL_SERVER_ERROR));
            e.printStackTrace();
        }
        closeRequest();

    }

    private void sendResponse(Response response) {
        if (response == null){
            throw new IllegalArgumentException("Empty response!");
        }
        getOutputStream().write(response.build());
    }

    private Request getRequest() throws IOException {
        setInputStream(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
        return new Request(getInputStream());
    }

    private Response getResponse(Request request) throws IOException {
        setOutputStream(new PrintWriter(clientSocket.getOutputStream(), false));

        if (request.getPathname() == null) {
            return new Response(
                    HttpStatus.BAD_REQUEST,
                    ContentType.TEXT,
                    null,
                    "No resource specified!"
            );
        } else {
            return getGame().handleRequest(request);
        }
    }

    private void closeRequest() {
        try {
            if (getOutputStream() != null) {
                getOutputStream().close();
            }

            if (getInputStream() != null) {
                getInputStream().close();
                getClientSocket().close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
