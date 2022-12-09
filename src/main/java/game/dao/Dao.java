package game.dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface Dao<T> {
    T create(T t) throws SQLException;
    ArrayList<T> read() throws SQLException;
    void update(T t) throws SQLException;
    void delete(T t) throws SQLException;
}
