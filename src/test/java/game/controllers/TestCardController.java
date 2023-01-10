package game.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.models.Card;
import game.repos.CardRepo;
import http.ContentType;
import http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestCardController {

    CardController cardController;

    CardRepo cardRepo;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        cardRepo = mock(CardRepo.class);
        cardController = new CardController(cardRepo);
    }

    @Test
    @DisplayName("Test getCards")
    void testGetCards() {
        //arrange
        when(cardRepo.getUserDeck("admin")).thenReturn(new ArrayList<>());

        //act
        var res = cardController.getCards("admin");

        //assert
        assertEquals(HttpStatus.OK.getCode(), res.getStatusCode());
        assertEquals(ContentType.JSON, res.getContentType());
        assertEquals("[]", res.getContent());
    }

    @Test
    @DisplayName("Test getUserDeck")
    void testGetDeck() {
        //arrange
        var deck = new ArrayList<Card>();
        UUID uuid = UUID.randomUUID();
        deck.add(new Card(uuid, "WaterSpell", 10F));

        when(cardRepo.getUserDeck("admin")).thenReturn(deck);

        //act
        var res = cardController.getDeck("admin");

        //assert
        assertEquals(HttpStatus.OK.getCode(), res.getStatusCode());
        assertEquals(ContentType.JSON, res.getContentType());
        assertEquals("[{\"Id\":\"" + uuid + "\",\"Name\":\"WaterSpell\",\"Damage\":10.0}]", res.getContent());
    }

    @Test
    @DisplayName("Test getUserDeck - No cards")
    void testGetDeckNoCards() {
        //arrange
        when(cardRepo.getUserDeck("admin")).thenReturn(new ArrayList<>());

        //act
        var res = cardController.getDeck("admin");

        //assert
        assertEquals(HttpStatus.NO_CONTENT.getCode(), res.getStatusCode());
        assertEquals(ContentType.JSON, res.getContentType());
        assertEquals("[]", res.getContent());
    }

    @Test
    @DisplayName("Test putDeck")
    void testPutDeck() throws JsonProcessingException {
        when(cardRepo.setUserDeck(any(List.class), any(String.class))).thenReturn(true);

        var ids = new ArrayList<UUID>();
        ids.add(UUID.randomUUID());
        ids.add(UUID.randomUUID());
        ids.add(UUID.randomUUID());
        ids.add(UUID.randomUUID());
        ObjectMapper objectMapper = new ObjectMapper();

        var res = cardController.putDeck(objectMapper.writeValueAsString(ids), "admin");

        assertEquals(HttpStatus.OK.getCode(), res.getStatusCode());
        assertEquals(ContentType.JSON, res.getContentType());
    }

    @Test
    @DisplayName("Test putDeck for BAD REQUEST")
    void testPutDeckInvalidCardAmount() throws JsonProcessingException {
        var ids = new ArrayList<UUID>();
        ids.add(UUID.randomUUID());
        ids.add(UUID.randomUUID());
        ids.add(UUID.randomUUID());

        ObjectMapper objectMapper = new ObjectMapper();

        var res = cardController.putDeck(objectMapper.writeValueAsString(ids), "admin");

        assertEquals(HttpStatus.BAD_REQUEST.getCode(), res.getStatusCode());
        assertEquals(ContentType.JSON, res.getContentType());
    }

    @Test
    @DisplayName("Test putDeck for FORBIDDEN")
    void testPutDeckInvalidCard() throws JsonProcessingException {
        when(cardRepo.setUserDeck(any(List.class), any(String.class))).thenReturn(false);

        var ids = new ArrayList<UUID>();
        ids.add(UUID.randomUUID());
        ids.add(UUID.randomUUID());
        ids.add(UUID.randomUUID());
        ids.add(UUID.randomUUID());

        var res = cardController.putDeck(objectMapper.writeValueAsString(ids), "admin");

        assertEquals(HttpStatus.FORBIDDEN.getCode(), res.getStatusCode());
        assertEquals(ContentType.JSON, res.getContentType());
    }

    @Test
    @DisplayName("Test postPackage for CREATED")
    void testPostPackage() throws JsonProcessingException {

        when(cardRepo.createPackage(any(List.class))).thenReturn(true);
        var pack = new ArrayList<Card>();
        UUID uuid = UUID.randomUUID();
        pack.add(new Card(uuid, "WaterSpell", 10F));
        pack.add(new Card(uuid, "WaterSpell", 10F));
        pack.add(new Card(uuid, "WaterSpell", 10F));
        pack.add(new Card(uuid, "WaterSpell", 10F));
        pack.add(new Card(uuid, "WaterSpell", 10F));

        var res = cardController.postPackage(objectMapper.writeValueAsString(pack));

        assertEquals(HttpStatus.CREATED.getCode(), res.getStatusCode());
        assertEquals(ContentType.JSON, res.getContentType());
    }

    @Test
    @DisplayName("Test postPackage for CONFLICT")
    void testPostPackageConflict() throws JsonProcessingException {

        when(cardRepo.createPackage(any(List.class))).thenReturn(false);
        var pack = new ArrayList<Card>();
        UUID uuid = UUID.randomUUID();
        pack.add(new Card(uuid, "WaterSpell", 10F));
        pack.add(new Card(uuid, "WaterSpell", 10F));
        pack.add(new Card(uuid, "WaterSpell", 10F));
        pack.add(new Card(uuid, "WaterSpell", 10F));
        pack.add(new Card(uuid, "WaterSpell", 10F));

        var res = cardController.postPackage(objectMapper.writeValueAsString(pack));

        assertEquals(HttpStatus.CONFLICT.getCode(), res.getStatusCode());
        assertEquals(ContentType.JSON, res.getContentType());
    }

    @Test
    @DisplayName("Test postPackage for BAD REQUEST")
    void testPostPackageWrongSize() throws JsonProcessingException {

        when(cardRepo.createPackage(any(List.class))).thenReturn(false);
        var pack = new ArrayList<Card>();
        UUID uuid = UUID.randomUUID();
        pack.add(new Card(uuid, "WaterSpell", 10F));
        pack.add(new Card(uuid, "WaterSpell", 10F));
        pack.add(new Card(uuid, "WaterSpell", 10F));

        var res = cardController.postPackage(objectMapper.writeValueAsString(pack));

        assertEquals(HttpStatus.BAD_REQUEST.getCode(), res.getStatusCode());
        assertEquals(ContentType.JSON, res.getContentType());
    }

    @Test
    @DisplayName("Test postTransactionsPackages for OK")
    void testPostTransactionsPackages() throws JsonProcessingException {
        var list = new ArrayList<Card>();
        UUID uuid = UUID.randomUUID();
        list.add(new Card(uuid, "WaterSpell", 10F));
        when(cardRepo.aquirePackage("admin")).thenReturn(list);

        var res = cardController.postTransactionsPackages("admin");

        assertEquals(HttpStatus.OK.getCode(), res.getStatusCode());
        assertEquals(ContentType.JSON, res.getContentType());
        assertEquals(objectMapper.writeValueAsString(list), res.getContent());
    }

    @Test
    @DisplayName("Test postTransactions for FORBIDDEN")
    void testPostTransactionsNoMoney() {
        when(cardRepo.aquirePackage("admin")).thenReturn(null);

        var res = cardController.postTransactionsPackages("admin");

        assertEquals(HttpStatus.FORBIDDEN.getCode(), res.getStatusCode());
        assertEquals(ContentType.JSON, res.getContentType());
        assertNull(res.getContent());
    }

    @Test
    @DisplayName("Test postTransactions for NOT FOUND")
    void testPostTransactionsNoPackages() {
        when(cardRepo.aquirePackage("admin")).thenReturn(new ArrayList<>());

        var res = cardController.postTransactionsPackages("admin");

        assertEquals(HttpStatus.NOT_FOUND.getCode(), res.getStatusCode());
        assertEquals(ContentType.JSON, res.getContentType());
        assertNull(res.getContent());
    }

}
