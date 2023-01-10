package game.repos;

import game.dao.UserDao;
import game.dto.UserCredentials;
import game.models.User;
import game.services.CacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestUserRepo {

    UserDao m_userDao;
    CacheService m_cacheService;

    UserRepo userRepo;

    UUID uuid = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        m_userDao = mock(UserDao.class);
        m_cacheService = mock(CacheService.class);
        userRepo = new UserRepo(m_userDao, m_cacheService);

        //arrange
        User user = new User();
        user.setUsername("admin");
        user.setPassword("123456");
        user.setId(uuid);

        var map = new HashMap<UUID, User>();
        map.put(uuid, user);

        var map2 = new HashMap<String, User>();
        map2.put("admin", user);

        when(m_cacheService.getUuidUserCache()).thenReturn(map);
        when(m_cacheService.getUsernameUserCache()).thenReturn(map2);


    }

    @Test
    void testGetAll() {
        //act
        var list = userRepo.getAll();

        //assert
        assertEquals(1, list.size());
        assertEquals("admin", list.get(0).getUsername());
    }

    @Test
    void testGetAllUserData() {
        var list = userRepo.getAllUserData();

        assertEquals(1, list.size());
        assertNotNull(list.get(0));
        assertEquals("admin", list.get(0).getName());
    }

    @Test
    void testCheckCredentials() {
        UserCredentials credentials = new UserCredentials("admin", "123456");

        //act & assert
        assertTrue(userRepo.checkCredentials(credentials));
    }

    @Test
    void getByName() {

        User user = userRepo.getByName("admin");

        assertEquals("admin", user.getUsername());
        assertEquals("123456", user.getPassword());
    }

    @Test
    void getById() {
        User user = userRepo.getById(this.uuid);

        assertEquals("admin", user.getUsername());
        assertEquals("123456", user.getPassword());
    }
}