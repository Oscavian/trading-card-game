package game.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class StackDeckEntry {
    private UUID entry_uuid;
    private UUID user_uuid;
    private UUID card_uuid;
}
