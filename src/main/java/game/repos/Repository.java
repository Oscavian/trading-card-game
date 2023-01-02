package game.repos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Setter(AccessLevel.PROTECTED)
@Getter(AccessLevel.PROTECTED)
public abstract class Repository<ID, T> {

    protected abstract ArrayList<T> getAll();

    protected abstract T getById(ID id);

    protected abstract void refreshCache();
}
