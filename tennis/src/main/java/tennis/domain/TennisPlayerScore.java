package tennis.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * The scoring system as {@link Enum} whose items are linked, so that they can be
 * traversed according to the official tennis scoring system.
 * @see <a href="https://en.wikipedia.org/wiki/Tennis_scoring_system#Description_2">Scoring system</a>
 *
 */
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

	/**
	 * Returns the next {@link TennisPlayerScore} towards victory.
	 * @param enemyScore The enemy's score which is relevant for the computation of the nexr score.
	 * @return A immutable {@link Enum} object representing the next score.
	 */
	public TennisPlayerScore winPoint(TennisPlayerScore enemyScore) {
		TennisPlayerScore resultScore;

		if (this.equals(GAME) && enemyScore.equals(ADVANTAGE)) {
			resultScore = this;
		} else if (this.equals(FORTY) && this.compareTo(enemyScore) > 0) {
			resultScore = WIN; // GAME and WIN
		} else {
			resultScore = TennisPlayerScore.values()[this.ordinal() + 1];
		}

		return resultScore;
	}

	/**
	 * Returns the score a player with this {@link TennisPlayerScore} receives when loosing a point.
	 * @return The previous (or current) score.
	 */
	public TennisPlayerScore loosePoint() {
		TennisPlayerScore resultScore = this;
		if (this.equals(ADVANTAGE)) {
			resultScore = GAME;
		}
		return resultScore;
	}

}
