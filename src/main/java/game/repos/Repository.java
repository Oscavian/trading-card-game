package game.repos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

@Setter(AccessLevel.PROTECTED)
@Getter(AccessLevel.PROTECTED)
public abstract class Repository<ID, T> {

    protected HashMap<ID, T> cache;

    public Repository() {
        cache = new HashMap<>();
    }

    protected abstract ArrayList<T> getAll();

    protected abstract T getById(ID id);

    protected abstract void refreshCache();

    protected void checkCache() {
        if (cache.isEmpty()) {
            refreshCache();
        }
    }
}
