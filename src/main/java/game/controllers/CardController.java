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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
     * Show a user's cards/stack
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

    /**
     * GET /decks
     * show a users configured deck
     * @param username
     * @return 200 or 500
     */
    public Response getDeck(String username) {
        if(username == null || username.isEmpty()) {
            return null;
        }

        var deck = getCardRepo().getUserDeck(username);

        if (deck.isEmpty()) {
            return new Response(
                    HttpStatus.NO_CONTENT,
                    ContentType.JSON,
                    "[]",
                    "The request was fine, but the deck doesn't have any cards"
            );
        }

        try {

            String deckJSON = getObjectMapper().writeValueAsString(deck);
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    deckJSON,
                    "The deck has cards, the response contains these."
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Response putDeck(String body, String userlogin) {

        try {
            List<UUID> cards = Arrays.asList(getObjectMapper().readValue(body, UUID[].class));

            if (cards.size() != 4) {
                return new Response(HttpStatus.BAD_REQUEST);
            }

            if (getCardRepo().setUserDeck(cards, userlogin)) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        null,
                        "The deck has been successfully configured"
                );
            } else {
                return new Response(
                        HttpStatus.FORBIDDEN,
                        ContentType.JSON,
                        null,
                        "At least one of the provided cards does not belong to the user or is not available."
                );
            }

        } catch (JsonProcessingException | IllegalArgumentException e) {
            return new Response(HttpStatus.BAD_REQUEST);
        }

    }
}

