package com.oscavian.tradingcardgame.game.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserData {
    @JsonAlias({"Name"})
    @JsonProperty("Name")
    private String name;
    @JsonAlias({"Bio"})
    @JsonProperty("Bio")
    private String bio;
    @JsonAlias({"Image"})
    @JsonProperty("Image")
    private String image;

    UserData(){}
}
