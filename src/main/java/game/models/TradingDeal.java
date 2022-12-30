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
public class TradingDeal {

    // ID of the deal
    @JsonAlias({"Id"})
    private UUID id;

    // The card ID offered
    @JsonAlias({"CardToTrade"})
    private UUID cardToTrade;

    // Required card type of the received card
    @JsonAlias("Type")
    private String type;

    //Required minimum damage of the received card
    @JsonAlias({"MinimumDamage"})
    private float minimumDamage;

    public TradingDeal(){}
}
