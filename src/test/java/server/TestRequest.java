package server;

import game.Game;
import http.HttpMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.Request;
import server.Response;

import javax.print.PrintService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class TestRequest {


    @BeforeEach
    void setup() {
    }

    @Test
    void testRequestAssembling() throws Exception {

        //arrange
        BufferedReader inputStream = mock(BufferedReader.class);
        when(inputStream.readLine()).thenReturn(
                "POST /users/admin?format=json HTTP/1.1",
                "Content-Type: application/json",
                "Content-Length: 2",
                "Authorization: Bearer admin-mtcgToken",
                "",
                "{}");

        when(inputStream.read()).thenReturn((int)'{', (int)'}');

        //act
        Request request = new Request(inputStream);

        //assert
        assertEquals(HttpMethod.POST, request.getMethod());
        assertEquals("/users/admin", request.getPathname());
        assertEquals("application/json", request.getContentType());
        assertEquals(2, request.getContentLength().intValue());
        assertEquals("admin-mtcgToken", request.getAuthorization());
        assertEquals("format=json", request.getParams());
        assertEquals("{}", request.getBody());

    }

}
