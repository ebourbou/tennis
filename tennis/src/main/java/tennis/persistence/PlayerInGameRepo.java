package tennis.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tennis.domain.PlayerInGame;

@Repository
public interface PlayerInGameRepo extends CrudRepository<PlayerInGame, Integer>{


}
