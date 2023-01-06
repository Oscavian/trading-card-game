package game.repos;

import game.dao.BattleLogDao;
import game.models.BattleLog;
import game.models.Card;
import game.models.User;
import game.services.CacheService;
import game.utils.BattleResult;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.UUID;

@Setter(AccessLevel.PRIVATE)
@Getter(AccessLevel.PRIVATE)
public class BattleLogRepo extends Repository<UUID, BattleLog> {

    CacheService cacheService;

    BattleLogDao battleLogDao;

    public BattleLogRepo(CacheService cacheService, BattleLogDao battleLogDao) {
        setCacheService(cacheService);
        setBattleLogDao(battleLogDao);
    }


    @Override
    protected ArrayList<BattleLog> getAll() {
        return null;
    }

    @Override
    protected BattleLog getById(UUID uuid) {
        return null;
    }

    @Override
    protected void refreshCache() {

    }
}
