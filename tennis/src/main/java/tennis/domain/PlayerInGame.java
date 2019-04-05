package tennis.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class PlayerInGame {
    
    public @NonNull @Getter Player player;
    private @NonNull @Setter TennisPlayerScore score = TennisPlayerScore.LOVE;
    public TennisPlayerScore getScore() {
        return this.score;
    }

}