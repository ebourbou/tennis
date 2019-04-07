package tennis.service;

import lombok.NonNull;
import tennis.domain.Game;
import tennis.domain.Game.Status;
import tennis.domain.Player;

/**
 * This interface represents the interactions possible to a system of keeping tabs of Tennis- or any other games.
 * 
 *
 */
public interface Match {

	/**
	 * Creates and persists a new {@link Game} with it's {@link Player}. Its status is set to {@link Status#RUNNING}
	 * @param player1 New or already existing player
	 * @param player2 New or already existing player
	 * @return The game number
	 */
	Integer startGame(@NonNull Player player1, @NonNull Player player2);

	/**
	 * Registers a point made at the specified {@link Game} made by one of its {@link Player}. A point can only
	 * be made in a game which is started and {@link Status#RUNNING}
	 * @param gameNo The game number
	 * @param player The point making player
	 */
	void winPoint(@NonNull Integer gameNo, @NonNull Player player);

	/**
	 * Abandons the {@link Game} by setting its status to {@link Status#ABANDONED}. No more points can be made afterwards.
	 * @param gameNo The game number
	 */
	void abandonGame(@NonNull Integer gameNo);

	/**
	 * Returns a string representation of the current score of this {@link Game} according to the USTA rule-book. 
	 * The current {@link Status} is not of relevance.
	 * @see <a href="https://en.wikipedia.org/wiki/Tennis_scoring_system#Description_2">Scoring system</a>
	 * @see <a href="https://en.wikipedia.org/wiki/United_States_Tennis_Association">United States Tennis Association</a>
	 * @param gameNo
	 * @return A string representing the current score, e.g. "Advantage Federer"
	 */
	String getScoreCall(@NonNull Integer gameNo);

	/**
	 * 
	 * @param gameNo
	 * @return The current {@link Status} of this game
	 */
	Status getStatus(@NonNull Integer gameNo);

}