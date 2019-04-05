package tennis.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TennisPlayerScore {
	
	LOVE("Love"),
	FIFTEEN("Fifteen"),
	THIRTY("Thirty"),
	FORTY("Forty"),
	GAME("Game"),
	ADVANTAGE("Advantage"),
	WIN("Win");
	
	private @Getter @NonNull String scoreCall;
	
    public TennisPlayerScore winPoint(TennisPlayerScore enemyScore) {
		TennisPlayerScore resultScore;

		if (this.equals(GAME) && enemyScore.equals(ADVANTAGE)) {
		    resultScore = this;
		} else if (this.equals(FORTY) && this.compareTo(enemyScore) > 0) {
		    resultScore = WIN; // GAME and WIN
		}else {
		    resultScore = TennisPlayerScore.values()[this.ordinal() + 1];
		}

		return resultScore;
	}
	
    public TennisPlayerScore loosePoint() {
		TennisPlayerScore resultScore = this;
		if (this.equals(ADVANTAGE)) {
			resultScore = GAME;
		}
		return resultScore;
	}

}
