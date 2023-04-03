package com.oscavian.tradingcardgame.server;

import com.oscavian.tradingcardgame.game.Game;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
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
        System.out.println("Game server started successfully!\n" +
                "Accepting connections...");
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
