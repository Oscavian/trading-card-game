package game.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Package {

    private UUID uuid;
    private UUID owner;
    private List<Card> cards;

    Package(){}
}
