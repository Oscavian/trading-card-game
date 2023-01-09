package game.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import game.dto.UserStats;
import game.models.User;
import game.repos.CardRepo;
import game.repos.UserRepo;
import game.services.BattleService;
import http.ContentType;
import http.HttpStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import server.Response;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

@Setter(AccessLevel.PRIVATE)
@Getter(AccessLevel.PRIVATE)
public class BattleController extends Controller {

    public static final BlockingQueue<BattleService> queue = new ArrayBlockingQueue<>(1);
    private UserRepo userRepo;

    private CardRepo cardRepo;


    public BattleController(UserRepo userRepo, CardRepo cardRepo) {
        setUserRepo(userRepo);
        setCardRepo(cardRepo);
    }

    public Response postBattle(String username) {

        User user = getUserRepo().getByName(username);

        if (user == null) {
            throw new IllegalArgumentException("Username is null!");
        }

        user.getDeck().addAll(getCardRepo().getUserDeck(user.getUsername()));

        BattleService battle = new BattleService(user);
        BattleService incomingBattle = null;
        try {

            while (incomingBattle == null && !queue.offer(battle)) {
                incomingBattle = queue.poll();
            }

            if (incomingBattle == null) { //thread offering
                battle.getCompleted().get(); //future blocking, until fight is done
            } else { //thread receiving
                battle = incomingBattle;
                battle.setOpponent(user);
                battle.startBattle();
            }

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (battle.getWinner() != null) {
            if (battle.getWinner().equals(user)) { // current thread is winning one
                user.setElo(user.getElo() + 3);
                user.setWins(user.getWins() + 1);
            } else { // losing one
                user.setLosses(user.getLosses() + 1);
                user.setElo(user.getElo() - 5);
            }
        }

        getUserRepo().updateUserStats(user.toUserStats(), user.getId());

        return new Response(
                HttpStatus.OK,
                ContentType.TEXT,
                null,
                "The battle has been carried out successfully"
        );
    }

    /**
     * GET /stats
     *
     * @param userLogin
     * @return
     */
    public Response getStats(String userLogin) {

        User user = Objects.requireNonNull(getUserRepo().getByName(userLogin));

        try {
            String statsJson = getObjectMapper().writeValueAsString(user.toUserStats());

            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    statsJson,
                    "The stats could be retrieved successfully."
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /scores
     *
     * @return
     */
    public Response getScores() {

        List<UserStats> list = getUserRepo().getAllUserStats();

        try {
            String listJson = getObjectMapper().writeValueAsString(list);
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    listJson,
                    "The scoreboard could be retrieved successfully."
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
