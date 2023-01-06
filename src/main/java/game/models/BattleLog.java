package game.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
public class BattleLog {
    private UUID uuid;

    private UUID player1;
    private UUID player2;
    private UUID winner;
    private String actions;
    private ArrayList<Card> cards;

    public BattleLog(UUID player1, UUID player2, UUID winner, String actions) {
        setWinner(winner);
        setPlayer1(player1);
        setPlayer2(player2);
        setActions(actions);
    }

    public BattleLog() {}
}
