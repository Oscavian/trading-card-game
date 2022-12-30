package game.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import game.utils.Element;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
public class Card {

    @JsonAlias({"Id"})
    private UUID id;

    @JsonAlias({"Name"})
    private String name;
    @JsonAlias({"Damage"})
    private float damage;
    @JsonAlias({"Element"})
    private Element element;

    Card(){}

}
