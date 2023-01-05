package server;

import game.Game;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import org.junit.jupiter.api.*;
import server.Request;
import server.RequestHandler;
import server.Response;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
public class TestRequestHandler {

    @Mock
    private Socket clientSocket;

    @Mock
    private Game game;

    @Mock
    private Request request;

    @Mock
    private Response response;

    @Mock
    private BufferedReader inputStream;

    @Mock
    private PrintWriter outputStream;

    /*
    @Test
    public void testRun() throws IOException {

        game = mock(Game.class);
        clientSocket = mock(Socket.class);
        request = mock(Request.class);
        response = mock(Response.class);
        inputStream = mock(BufferedReader.class);
        outputStream = mock(PrintWriter.class);


        when(game.handleRequest(request)).thenReturn(response);
        when(clientSocket.getInputStream()).thenReturn(System.in);
        when(clientSocket.getOutputStream()).thenReturn(System.out);

        RequestHandler handler = new RequestHandler(game, clientSocket);
        handler.run();

        verify(game).handleRequest(request);
        verify(response).build();
        verify(clientSocket).getInputStream();
        verify(clientSocket).getOutputStream();
        verify(clientSocket).close();
        verify(outputStream).close();
        verify(inputStream).close();
    }
    */

    @Test
    public void testSendResponseWithNullResponse() throws IOException {
        RequestHandler handler = new RequestHandler(game, clientSocket);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> handler.sendResponse(null));
        assertEquals("Empty response!", exception.getMessage());
    }
}