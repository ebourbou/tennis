package tennis.service;

import lombok.NonNull;
import tennis.domain.Game.Status;
import tennis.domain.Player;

public interface Match {

    Integer startGame(@NonNull Player player1, @NonNull Player player2);

    void winPoint(@NonNull Integer gameNo, @NonNull Player player);

    void abandonGame(@NonNull Integer gameNo);

    String getScoreCall(@NonNull Integer gameNo);

    Status getStatus(@NonNull Integer gameNo);

}