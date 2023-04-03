package com.oscavian.tradingcardgame.game.repos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter(AccessLevel.PROTECTED)
@Getter(AccessLevel.PROTECTED)
public abstract class Repository<ID, T> {

    protected abstract ArrayList<T> getAll();

    protected abstract T getById(ID id);

    protected abstract void refreshCache();
}
