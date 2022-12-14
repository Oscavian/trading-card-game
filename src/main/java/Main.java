import game.Game;
import server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        Server server = new Server(game, 10001);

        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
