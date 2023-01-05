package game;

import game.Game;
import http.HttpMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.Request;
import server.Response;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestGame {

    Game game;
    Request requestMock;

    @BeforeEach
    void beforeEach() {
        game = new Game();
        requestMock = mock(Request.class);
    }

    @Test
    @DisplayName("Test unauthenticated call to GET /users")
    void test_unauthenticated_call_to_get_users() {
        //arrange
        //when(requestMock.getAuthorization()).thenReturn("rndguy-mtcgToken");
        when(requestMock.getMethod()).thenReturn(HttpMethod.GET);
        when(requestMock.getPathname()).thenReturn("/users");

        //act
        Response res = game.handleRequest(requestMock);

        //assert
        assertEquals(401, res.getStatusCode());
    }

    @Test
    @DisplayName("Test unauthenticated call to POST /users")
    void test_unauthenticated_call_to_post_users() {
        //arrange
        //when(requestMock.getAuthorization()).thenReturn("rndguy-mtcgToken");
        when(requestMock.getMethod()).thenReturn(HttpMethod.POST);
        when(requestMock.getPathname()).thenReturn("/users");

        //act
        Response res = game.handleRequest(requestMock);

        //assert
        assertEquals(400, res.getStatusCode());
    }
}
