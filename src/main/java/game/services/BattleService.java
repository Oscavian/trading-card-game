package game.services;

import game.models.Card;
import game.models.User;
import game.utils.AttackModifier;
import game.utils.BattleResult;
import game.utils.CardType;
import game.utils.Element;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Setter
@Getter
public class BattleService {

    User player;
    User opponent;

    User winner;
    CompletableFuture<Boolean> completed = new CompletableFuture<>();

    Random random = new Random();

    public BattleService(User player) {
        setPlayer(player);
    }

    public void startBattle() throws InterruptedException {
        if (player.getId().equals(opponent.getId())) {
            throw new IllegalArgumentException("A Player can't battle himself!");
        }


        System.out.println("Battle started..." + player.getUsername() + " vs. " + opponent.getUsername());

        Card playerCard;
        Card opponentCard;
        BattleResult roundResult;

        for (int i = 0; i < 100; i++) {

            if (player.getDeck().isEmpty()) {
                setWinner(opponent);
                System.out.println(opponent.getUsername() + " has won!");
                break;
            }
            if (opponent.getDeck().isEmpty()) {
                setWinner(player);
                System.out.println(player.getUsername() + " has won!");
                break;
            }

            playerCard = player.getDeck().get(random.nextInt(player.getDeck().size()));
            opponentCard = opponent.getDeck().get(random.nextInt(opponent.getDeck().size()));

            System.out.printf("R%d - %s (%d): %s (%.1f dmg) vs. %s (%d): %s (%.1f dmg) => ", i, player.getUsername(), player.getDeck().size(), playerCard.getName(), playerCard.getDamage(), opponent.getUsername(), opponent.getDeck().size(), opponentCard.getName(), opponentCard.getDamage());

            if (playerCard.getType() != CardType.SPELL && opponentCard.getType() != CardType.SPELL) {
                roundResult = monsterFight(playerCard, opponentCard);
            } else {
                roundResult = mixedFight(playerCard, opponentCard);
            }

            if (roundResult == null) {
                System.err.println("Round result null. Abort.");
                return;
            }

            //
            switch (roundResult) {
                case WIN -> {
                    System.out.printf("%s defeats %s!", playerCard.getName(), opponentCard.getName());
                    player.getDeck().add(opponentCard);
                    opponent.getDeck().remove(opponentCard);
                    System.out.printf(" - %s took %s (%.1f) from %s!\n", player.getUsername(), opponentCard.getName(), opponentCard.getDamage(), opponent.getUsername());
                }
                case LOSE -> {
                    System.out.printf("%s defeats %s!", opponentCard.getName(), playerCard.getName());
                    opponent.getDeck().add(playerCard);
                    player.getDeck().remove(playerCard);
                    System.out.printf(" - %s took %s (%.1f) from %s!\n", opponent.getUsername(), playerCard.getName(), playerCard.getDamage(), player.getUsername());
                }
                case DRAW -> System.out.print("DRAW!\n");
            }


        }

        if (!player.getDeck().isEmpty() && !opponent.getDeck().isEmpty()) {
            System.out.println("DRAW!!!!");
            setWinner(null);
        }

        completed.complete(true);
        System.out.println("Complete!");
    }


    private BattleResult mixedFight(Card playerCard, Card opponentCard) {

        //Special cases
        if (playerCard.getType().equals(CardType.KNIGHT) && opponentCard.getElement().equals(Element.WATER)) {
            return BattleResult.LOSE;
        } else if (opponentCard.getType().equals(CardType.KNIGHT) && playerCard.getElement().equals(Element.WATER)) {
            return BattleResult.WIN;
        } else if (playerCard.getType().equals(CardType.KRAKEN) && opponentCard.getType().equals(CardType.SPELL)) {
            return BattleResult.WIN;
        } else if (opponentCard.getType().equals(CardType.KRAKEN) && playerCard.getType().equals(CardType.SPELL)) {
            return BattleResult.LOSE;
        }


        //Define attack modifier
        AttackModifier modifier;

        if (playerCard.getElement().equals(Element.FIRE) && opponentCard.getElement().equals(Element.WATER)) {
            modifier = AttackModifier.NOT_EFFECTIVE;
        } else if (playerCard.getElement().equals(Element.WATER) && opponentCard.getElement().equals(Element.FIRE)) {
            modifier = AttackModifier.EFFECTIVE;
        } else if (playerCard.getElement().equals(Element.FIRE) && opponentCard.getElement().equals(Element.NORMAL)) {
            modifier = AttackModifier.EFFECTIVE;
        } else if (playerCard.getElement().equals(Element.NORMAL) && opponentCard.getElement().equals(Element.FIRE)) {
            modifier = AttackModifier.NOT_EFFECTIVE;
        } else if (playerCard.getElement().equals(Element.NORMAL) && opponentCard.getElement().equals(Element.WATER)) {
            modifier = AttackModifier.EFFECTIVE;
        } else if (playerCard.getElement().equals(Element.WATER) && opponentCard.getElement().equals(Element.NORMAL)) {
            modifier = AttackModifier.NOT_EFFECTIVE;
        } else {
            modifier = AttackModifier.NO_EFFECT;
        }

        switch (modifier) {
            case EFFECTIVE -> {
                System.out.printf("%.1f vs. %.1f -> ", playerCard.getDamage() * 2, opponentCard.getDamage() / 2);
                return playerCard.getDamage() * 2 != opponentCard.getDamage() / 2
                        ? (playerCard.getDamage() * 2 > opponentCard.getDamage() / 2 ? BattleResult.WIN : BattleResult.LOSE)
                        : BattleResult.DRAW;
            }
            case NOT_EFFECTIVE -> {
                System.out.printf("%.1f vs. %.1f -> ", playerCard.getDamage() / 2, opponentCard.getDamage() * 2);
                return playerCard.getDamage() / 2 != opponentCard.getDamage() * 2
                        ? (playerCard.getDamage() / 2 > opponentCard.getDamage() * 2 ? BattleResult.WIN : BattleResult.LOSE)
                        : BattleResult.DRAW;
            }
            case NO_EFFECT -> {
                return !Objects.equals(playerCard.getDamage(), opponentCard.getDamage())
                        ? (playerCard.getDamage() > opponentCard.getDamage() ? BattleResult.WIN : BattleResult.LOSE)
                        : BattleResult.DRAW;
            }
        }

        return null;
    }

    private BattleResult monsterFight(Card playerCard, Card opponentCard) {
        CardType pType = playerCard.getType();
        CardType oType = opponentCard.getType();

        if (playerCard.getType().equals(CardType.GOBLIN) && opponentCard.getType().equals(CardType.DRAGON)) { //Goblin too afraid of dragon
            return BattleResult.LOSE;
        } else if (playerCard.getType().equals(CardType.DRAGON) && oType.equals(CardType.GOBLIN)) {
            return BattleResult.WIN;
        } else if (playerCard.getType().equals(CardType.WIZARD) && opponentCard.getType().equals(CardType.ORK)) {
            return BattleResult.WIN;
        } else if (pType.equals(CardType.ORK) && oType.equals(CardType.WIZARD)) {
            return BattleResult.LOSE;
        } else if (playerCard.getDamage().equals(opponentCard.getDamage())) {
            return BattleResult.DRAW;
        } else if (playerCard.getDamage() < opponentCard.getDamage()) {
            return BattleResult.LOSE;
        } else if (playerCard.getDamage() > opponentCard.getDamage()) {
            return BattleResult.WIN;
        } else {
            return BattleResult.DRAW;
        }
    }
}
