package game.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import game.utils.CardType;
import game.utils.Element;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class Card {

    @JsonAlias({"Id"})
    private UUID id;

    @JsonAlias({"Name"})
    private String name;
    @JsonAlias({"Damage"})
    private Float damage;

    @JsonIgnore
    private Element element;

    @JsonIgnore
    private CardType type;

    Card(){}

    public Card(UUID id, String name, Float damage) {
        setId(id);
        setName(name);
        setDamage(damage);

        if (getName().contains("Spell")){
            setType(CardType.SPELL);
        } else {
            setType(CardType.MONSTER);
        }

        if (getName().contains("Regular")){
            setElement(Element.NORMAL);
        } else if (getName().contains("Fire")) {
            setElement(Element.FIRE);
        } else if (getName().contains("Water")) {
            setElement(Element.WATER);
        } else {
            setElement(Element.NONE);
        }
    }

}
