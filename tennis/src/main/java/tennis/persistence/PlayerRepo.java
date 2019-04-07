package tennis.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tennis.domain.Player;

@Repository
public interface PlayerRepo extends CrudRepository<Player, String>{


}
