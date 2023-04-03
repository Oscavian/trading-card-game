package com.oscavian.tradingcardgame.game.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oscavian.tradingcardgame.game.utils.CardType;
import com.oscavian.tradingcardgame.game.utils.Element;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class Card {

    @JsonAlias({"Id"})
    @JsonProperty("Id")
    private UUID id;

    @JsonAlias({"Name"})
    @JsonProperty("Name")
    private String name;
    @JsonAlias({"Damage"})
    @JsonProperty("Damage")
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
        } else if (getName().contains("Goblin")) {
            setType(CardType.GOBLIN);
        } else if (getName().contains("Dragon")) {
            setType(CardType.DRAGON);
        } else if (getName().contains("Wizard")) {
            setType(CardType.WIZARD);
        } else if (getName().contains("Ork")) {
            setType(CardType.ORK);
        } else if (getName().contains("Kraken")) {
            setType(CardType.KRAKEN);
        } else if (getName().contains("Knight")) {
            setType(CardType.KNIGHT);
        } else if (getName().contains("Elve")) {
            setType(CardType.ELVE);
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
