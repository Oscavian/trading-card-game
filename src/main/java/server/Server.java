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
import java.net.ServerSocket;
import java.net.Socket;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class Server {

    private Request request;
    private Response response;
    private ServerSocket serverSocket;

    private Socket clientSocket;
    private PrintWriter outputStream;
    private BufferedReader inputStream;
    private Game game;

    private int port;

    public Server(Game game, int port) {
        setGame(game);
        setPort(port);
    }

    public void start() throws IOException {
        setServerSocket(new ServerSocket(getPort()));
        run();
    }

    public void run() {
        while (true) {
            try {
                setClientSocket(getServerSocket().accept());
                setInputStream(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
                setRequest(new Request(getInputStream()));
                setOutputStream(new PrintWriter(clientSocket.getOutputStream(), false));

                if (request.getPathname() == null) {
                    setResponse(new Response(
                            HttpStatus.BAD_REQUEST,
                            ContentType.TEXT,
                            "No resource specified!"
                    ));
                } else {
                    setResponse(getGame().handleRequest(request));
                }

                getOutputStream().write(getResponse().build());

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
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
    }
}
