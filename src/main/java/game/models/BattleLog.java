package game.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
public class BattleLog {
    private UUID uuid;
    private UUID player1;
    private UUID player2;
    private String actions;
    private ArrayList<Card> cards;

    BattleLog() {}
}
