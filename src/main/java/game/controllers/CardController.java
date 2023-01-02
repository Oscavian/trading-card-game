package game.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import game.models.Card;
import game.models.User;
import game.repos.CardRepo;
import game.services.AuthService;
import http.ContentType;
import http.HttpStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import server.Response;

@Setter(AccessLevel.PRIVATE)
@Getter(AccessLevel.PRIVATE)
public class CardController extends Controller {

    private CardRepo cardRepo;
    private AuthService authService;

    public CardController(CardRepo cardRepo, AuthService authService) {
        setCardRepo(cardRepo);
        setAuthService(authService);
    }

    /**
     * GET /cards
     * Show a user's cards
     */
    public Response getCards(String username) {
        if (username == null || username.isEmpty()) {
            return null;
        }

        var stack = getCardRepo().getUserStack(username);

        try {
            String stackJSON = getObjectMapper().writeValueAsString(stack);
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    stackJSON,
                    "Data retrieved."
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();

            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

