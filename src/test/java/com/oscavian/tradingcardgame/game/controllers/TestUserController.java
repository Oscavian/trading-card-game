package com.oscavian.tradingcardgame.game.controllers;
import com.oscavian.tradingcardgame.game.dto.UserCredentials;
import com.oscavian.tradingcardgame.game.dto.UserData;
import com.oscavian.tradingcardgame.game.repos.UserRepo;
import com.oscavian.tradingcardgame.game.services.AuthService;
import com.oscavian.tradingcardgame.http.ContentType;
import com.oscavian.tradingcardgame.http.HttpStatus;
import org.junit.jupiter.api.*;
import com.oscavian.tradingcardgame.server.Response;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestUserController {

    UserController userController;
    UserRepo m_userRepo;

    AuthService m_authService;

    @BeforeAll
    static void setup() {

    }

    @BeforeEach
    void beforeEach() {
        //arrange
        m_userRepo = mock(UserRepo.class);
        m_authService = mock(AuthService.class);
        userController = new UserController(m_userRepo, m_authService);
    }

    @Test
    @DisplayName("Test getUsers for OK")
    void testGetUsers() {
        //arrange
        var list = new ArrayList<UserData>();
        list.add(new UserData("admin", "hi", ":)"));
        when(m_userRepo.getAllUserData()).thenReturn(list);

        //act
        Response res = userController.getUsers();

        //assert
        verify(m_userRepo).getAllUserData();
        assertEquals(HttpStatus.OK.getMsg(), res.getStatusMessage());
        assertEquals("[{\"Name\":\"admin\",\"Bio\":\"hi\",\"Image\":\":)\"}]", res.getContent());
    }

    @Test
    @DisplayName("Test getUserByName for OK")
    void testGetUserByNameValidName() {
        //arrange
        when(m_userRepo.getUserDataByName("admin")).thenReturn(new UserData("admin", "hi", ":)"));

        //act
        Response response = userController.getUserByName("admin");

        //assert
        verify(m_userRepo).getUserDataByName("admin");
        assertEquals(HttpStatus.OK.getMsg(), response.getStatusMessage());
        assertEquals(ContentType.JSON, response.getContentType());
        assertEquals("{" +
                "\"Name\":\"admin\"," +
                "\"Bio\":\"hi\"," +
                "\"Image\":\":)\"" +
                "}", response.getContent());
    }

    @Test
    @DisplayName("Test registerUser for CONFLICT")
    void testRegisterUserValidBodyDuplicateUsername() {
        //arrange
        String body = "{\"Username\":\"admin\",\"Password\":\"123456\"}";
        when(m_userRepo.addUser(any(UserCredentials.class))).thenReturn(null);

        //act
        Response res = userController.registerUser(body);
        //assert
        assertEquals(HttpStatus.CONFLICT.getCode(), res.getStatusCode());
        assertNull(res.getContent());
    }

    @Test
    @DisplayName("Test registerUser for CREATED")
    void testRegisterUserValidBody() {
        //arrange
        String body = "{\"Username\":\"admin\",\"Password\":\"123456\"}";
        UUID uuid = UUID.randomUUID();
        when(m_userRepo.addUser(any(UserCredentials.class))).thenReturn(uuid);

        //act
        Response res = userController.registerUser(body);
        //assert
        assertEquals(HttpStatus.CREATED.getCode(), res.getStatusCode());
        assertEquals("\"" + uuid + "\"", res.getContent());
    }

    @Test
    @DisplayName("Test registerUser for BAD REQUEST")
    void testRegisterUserInvalidPayload() {
        //arrange
        String body = "{\"name\":\"admin\",\"pass\":\"123456\"}";
        //act
        Response res = userController.registerUser(body);
        //assert
        assertEquals(HttpStatus.BAD_REQUEST.getCode(), res.getStatusCode());
    }

    @Test
    @DisplayName("Test updateUSer for OK")
    void testUpdateUserValidPayload() {
        //arrange
        String body = "{" +
                "\"Name\":\"admin\"," +
                "\"Bio\":\"hi\"," +
                "\"Image\":\":)\"" +
                "}";
        String username = "admin";
        when(m_userRepo.updateUserData(any(UserData.class), any(String.class))).thenReturn(true);
        //act
        Response res = userController.updateUser(body, username);

        //assert
        verify(m_userRepo).updateUserData(any(), any());
        assertEquals(HttpStatus.OK.getCode(), res.getStatusCode());
    }

    @Test
    @DisplayName("Test updateUser for NOT_FOUND")
    void testUpdateUserInvalidUser() {
        String body = "{" +
                "\"Name\":\"admin\"," +
                "\"Bio\":\"hi\"," +
                "\"Image\":\":)\"" +
                "}";
        String username = "admin";

        //arrange
        when(m_userRepo.updateUserData(any(UserData.class), any(String.class))).thenReturn(false);

        //act
        Response res = userController.updateUser(body, username);

        //assert
        verify(m_userRepo).updateUserData(any(), any());
        assertEquals(HttpStatus.NOT_FOUND.getCode(), res.getStatusCode());

    }

    @Test
    @DisplayName("Test updateUser for BAD REQUEST")
    void testUpdateUserInvalidPayload() {
        //arrange
        String body = "{" +
                "\"bla\":\"admin\"," +
                "\"bli\":\"hi\"," +
                "\"blub\":\":)\"" +
                "}";
        //act
        Response res = userController.updateUser(body, "admin");
        //assert
        assertEquals(HttpStatus.BAD_REQUEST.getCode(), res.getStatusCode());
    }

    @Test
    @DisplayName("Test loginUser for OK")
    void testLoginUserValidUser() {
        //arrange
        String body = "{\"Username\":\"admin\",\"Password\":\"123456\"}";
        when(m_userRepo.checkCredentials(any(UserCredentials.class))).thenReturn(true);
        when(m_authService.login("admin")).thenReturn("admin-mtcgToken");

        //act
        Response res = userController.loginUser(body);
        //assert
        verify(m_authService).login("admin");
        assertEquals(HttpStatus.OK.getCode(), res.getStatusCode());
        assertEquals("admin-mtcgToken", res.getContent());
    }

    @Test
    @DisplayName("Test loginUser for UNAUTHORIZED")
    void testLoginUserInalidUser() {
        //arrange
        String body = "{\"Username\":\"admin\",\"Password\":\"654321\"}";
        when(m_userRepo.checkCredentials(any(UserCredentials.class))).thenReturn(false);

        //act
        Response res = userController.loginUser(body);
        //assert
        assertEquals(HttpStatus.UNAUTHORIZED.getCode(), res.getStatusCode());
        assertNull(res.getContent());
    }

    @Test
    @DisplayName("Test loginUser for BAD REQUEST")
    void testLoginUserInvalidPayload() {
        //arrange
        String body = "{\"bla\":\"admin\",\"blub\":\"123456\"}";
        //act
        Response res = userController.loginUser(body);
        //assert
        assertEquals(HttpStatus.BAD_REQUEST.getCode(), res.getStatusCode());
    }

    @Test
    @DisplayName("Test UserController parameter validation")
    void testUserControllerparameterValidation() {
        //act & assert
        assertDoesNotThrow(() -> userController.registerUser(null));

        assertDoesNotThrow(() -> userController.getUserByName(null));

        assertDoesNotThrow(() -> userController.updateUser(null, null));

        assertDoesNotThrow(() -> userController.loginUser(null));
    }
}
