package tennis.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tennis.domain.Game;

@Repository
public interface GameRepo extends CrudRepository<Game, Integer>{


}
