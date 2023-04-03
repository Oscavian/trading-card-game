package com.oscavian.tradingcardgame.game.dao;

import java.sql.SQLException;
import java.util.HashMap;

public interface Dao<ID, T> {
    ID create(T t) throws SQLException;
    HashMap<ID, T> read() throws SQLException;
    void update(T t) throws SQLException;
    void delete(T t) throws SQLException;
}
