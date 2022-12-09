package game.repos;

import game.dao.UserDao;
import game.models.User;
import game.services.DatabaseService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;
import java.util.ArrayList;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
public class UserRepo {

    UserDao userDao;
    ArrayList<User> userCache = new ArrayList<>();

    public UserRepo(UserDao userDao) {
        setUserDao(userDao);
    }

    public ArrayList<User> getAll() {
        if (!userCache.isEmpty()) {
            return getUserCache();
        }

        try {
            //CityRepo cityRepo = new CityRepo(new CityDao(new DbService().getConnection()));

            //ArrayList<City> cities = cityRepo.getAll();


            setUserCache(getUserDao().read());
        } catch (SQLException e){
            e.printStackTrace();
        }

        return getUserCache();
    }

    public User getById() {
        return null;
    }


}
