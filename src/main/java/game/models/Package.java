package game.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Package {

    private UUID uuid;
    private UUID owner;
    private ArrayList<Card> cards;

    Package(){}
}
