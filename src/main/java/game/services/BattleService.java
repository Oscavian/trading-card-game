package game.services;

import game.models.BattleLog;
import game.models.Card;
import game.models.User;
import game.utils.BattleResult;
import game.utils.CardType;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Setter
@Getter
public class BattleService {

    User player;
    User opponent;
    CompletableFuture<Boolean> completed = new CompletableFuture<>();

    Random random = new Random();
    BattleLog log;

    public BattleService(User player) {;
        setPlayer(player);
        setLog(new BattleLog());
        log.setPlayer1(player.getId());
    }

    public void startBattle() throws InterruptedException {
        System.out.println("Battle started..." + player.getUsername() + " vs. " + opponent.getUsername());

        Card playerCard;
        Card opponentCard;
        BattleResult roundResult;

        for(int i = 0; i < 100; i++) {

            if (player.getDeck().isEmpty()) {
                System.out.println(opponent.getUsername() + " has won!");
                return;
            }
            if (opponent.getDeck().isEmpty()) {
                System.out.println(player.getUsername() + " has won!");
                return;
            }

            playerCard = getPlayer().getDeck().get(random.nextInt(4));
            opponentCard = getOpponent().getDeck().get(random.nextInt(4));

            if (playerCard.getType() != CardType.SPELL && opponentCard.getType() != CardType.SPELL) {
                roundResult = monsterFight(playerCard, opponentCard);
            } else {
                roundResult = spellFight(playerCard, opponentCard);
            }

            switch (Objects.requireNonNull(roundResult)) {
                case WIN -> System.out.printf("%s by %s wins!%n", playerCard.getName(), player.getUsername());
                case LOSE -> System.out.printf("%s by %s wins!%n", opponentCard.getName(), opponent.getUsername());
                case DRAW -> System.out.printf("%s and %s fought. Draw.", playerCard.getName(), opponentCard.getName());
            }






        }

        if (!player.getDeck().isEmpty() && !opponent.getDeck().isEmpty()) {
            System.out.println("DRAW!!!!");
        }

        completed.complete(true);
        System.out.println("Complete!");
    }



    private BattleResult spellFight(Card playerCard, Card opponentCard) {
        return null;
    }

    private BattleResult monsterFight(Card playerCard, Card opponentCard) {
        return null;
    }

    private BattleResult mixedFight(Card playerCard, Card opponentCard) {
        return null;
    }
}
