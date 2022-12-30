package game.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public interface Dao<ID, T> {
    ID create(T t) throws SQLException;
    HashMap<ID, T> read() throws SQLException;
    void update(T t) throws SQLException;
    void delete(T t) throws SQLException;
}
