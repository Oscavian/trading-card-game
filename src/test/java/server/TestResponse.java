package server;
import http.ContentType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestResponse {

    @Test
    public void testJsonContentType() {
        Response response = new Response();
        response.setContentType(ContentType.JSON);
        response.setContent("{\"hello\": \"world\"}");
        response.setDescription("This is a JSON object");
        response.setStatusCode(200);
        response.setStatusMessage("OK");

        String expectedOutput = "HTTP/1.1 200 OK\r\nContent-Type: JSON\r\nContent-Length: 71\r\n\r\n{\"content\": {\"hello\": \"world\"}, \"description\": \"This is a JSON object\"}";
        String actualOutput = response.build();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTextContentType() {
        Response response = new Response();
        response.setContentType(ContentType.TEXT);
        response.setContent("This is some text");
        response.setDescription("This is a description of the text");
        response.setStatusCode(200);
        response.setStatusMessage("OK");

        String expectedOutput = "HTTP/1.1 200 OK\r\nContent-Type: TEXT\r\nContent-Length: 64\r\n\r\nThis is some text\nDescription: This is a description of the text";
        String actualOutput = response.build();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testIllegalContentType() {
        Response response = new Response();
        response.setContentType(ContentType.UNKNOWN);
        assertThrows(IllegalArgumentException.class, response::build);
    }
}