package game.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.StandardException;

public abstract class Controller {

    @Getter
    @Setter
    private ObjectMapper objectMapper;

    Controller(){ setObjectMapper(new ObjectMapper()); }
}
