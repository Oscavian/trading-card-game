package com.oscavian.tradingcardgame.game.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oscavian.tradingcardgame.game.models.Card;
import com.oscavian.tradingcardgame.game.repos.CardRepo;
import com.oscavian.tradingcardgame.http.ContentType;
import com.oscavian.tradingcardgame.http.HttpStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import com.oscavian.tradingcardgame.server.Response;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Setter(AccessLevel.PRIVATE)
@Getter(AccessLevel.PRIVATE)
public class CardController extends Controller {

    private CardRepo cardRepo;

    public CardController(CardRepo cardRepo) {
        setCardRepo(cardRepo);
    }

    /**
     * GET /cards
     * Show a user's cards/stack
     * @param username of the requesting User
     * @return Array of cards
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
     * @param username of the requesting User
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

    /**
     * PUT /decks
     * @param body list of UUIDs
     * @param userlogin of the requesting User
     * @return Response message
     */
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

    /**
     * POST /packages
     * @param body list of cards
     * @return Response message
     */
    public Response postPackage(String body) {

        try {
            List<Card> cards = Arrays.asList(getObjectMapper().readValue(body, Card[].class));

            if (cards.size() != 5) {
                return new Response(HttpStatus.BAD_REQUEST);
            }

            if (getCardRepo().createPackage(cards)) {
                return new Response(
                        HttpStatus.CREATED,
                        ContentType.JSON,
                        null,
                        "Package and cards successfully created"
                );
            } else {
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        null,
                        "At least one card in the packages already exists"
                );
            }




        } catch (JsonProcessingException | IllegalArgumentException e) {
            return new Response(HttpStatus.BAD_REQUEST);
        }
    }

    public Response postTransactionsPackages(String userlogin) {

        try {
            List<Card> aquiredPackage = getCardRepo().aquirePackage(userlogin);

            if (aquiredPackage == null) {
                return new Response(
                        HttpStatus.FORBIDDEN,
                        ContentType.JSON,
                        null,
                        "Not enough money for buying a card package"
                );
            }

            if (aquiredPackage.isEmpty()) {
                return new Response(
                        HttpStatus.NOT_FOUND,
                        ContentType.JSON,
                        null,
                        "No card package available for buying"
                );
            }

            String cardJSON = getObjectMapper().writeValueAsString(aquiredPackage);

            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    cardJSON,
                    "A package has been successfully bought"
            );
        } catch (JsonProcessingException e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

