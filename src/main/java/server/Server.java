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
        while(true) {
            try {
                Socket clientSocket = getServerSocket().accept();
                RequestHandler requestHandler = new RequestHandler(getGame(), clientSocket);
                Thread th = new Thread(requestHandler);
                th.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
