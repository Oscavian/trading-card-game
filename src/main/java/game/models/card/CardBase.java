package game.models.card;

import com.fasterxml.jackson.annotation.JsonAlias;
import game.utils.Element;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter(AccessLevel.PRIVATE)
@AllArgsConstructor
public abstract class CardBase {

    @JsonAlias({"name"})
    private String name;
    @JsonAlias({"damage"})
    private int damage;
    @JsonAlias({"element"})
    private Element element;

    CardBase(){}

}
