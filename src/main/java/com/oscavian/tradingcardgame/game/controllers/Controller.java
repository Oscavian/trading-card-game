package com.oscavian.tradingcardgame.game.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

public abstract class Controller {

    @Getter
    @Setter
    private ObjectMapper objectMapper;

    Controller(){ setObjectMapper(new ObjectMapper()); }
}
