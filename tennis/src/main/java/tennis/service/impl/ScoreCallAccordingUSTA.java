package tennis.service.impl;

import java.util.Comparator;

import tennis.domain.Game;
import tennis.domain.PlayerInGame;
import tennis.domain.TennisPlayerScore;

/**
 * Implements the string representation of score calls according to the USTA
 * rule-book.
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Tennis_scoring_system#Description_2">Scoring system</a>
 * @see <a href="https://en.wikipedia.org/wiki/United_States_Tennis_Association">United States Tennis Association</a>
 */
final class ScoreCallAccordingUSTA {

	public static String getScoreCallAccordingUSTA(Game game) {
		String scoreCall = null;

		if (isDraw(game)) {
			scoreCall = getScoreCallForDraw(game);
		} else {
			scoreCall = getScoreCallForAdvantage(game);
		}
		return scoreCall;
	}

	private static String getScoreCallForAdvantage(Game game) {
		StringBuilder callBuilder = new StringBuilder();
		PlayerInGame winner = getWinnerOrAny(game);
		PlayerInGame looser = getLooserOrAny(game);

		if (isGameOrAbove(game)) {
			callBuilder.append(winner.getScore().getScoreCall());
			callBuilder.append(" ");
			callBuilder.append(winner.getPlayer().getName());
		} else {
			callBuilder.append(winner.getScore().getScoreCall());
			callBuilder.append(" - ");
			callBuilder.append(looser.getScore().getScoreCall());
		}
		return callBuilder.toString();
	}

	private static String getScoreCallForDraw(Game game) {
		StringBuilder callBuilder = new StringBuilder();
		if (isDeuceOrAbove(game)) {
			callBuilder.append("Deuce");
		} else {
			callBuilder.append(getWinnerOrAny(game).getScore().getScoreCall());
			callBuilder.append(" - all");
		}
		return callBuilder.toString();
	}

	private static boolean isDraw(Game game) {
		return getWinnerOrAny(game).getScore().equals(getLooserOrAny(game).getScore());
	}

	private static boolean isDeuceOrAbove(Game game) {
		return getWinnerOrAny(game).getScore().compareTo(TennisPlayerScore.FORTY) >= 0;
	}

	private static boolean isGameOrAbove(Game game) {
		return getWinnerOrAny(game).getScore().compareTo(TennisPlayerScore.GAME) >= 0;
	}

	private static PlayerInGame getWinnerOrAny(Game game) {
		return game.getPlayersInGame().stream().sorted(Comparator.comparing(PlayerInGame::getScore).reversed())
				.findFirst().get();
	}

	private static PlayerInGame getLooserOrAny(Game game) {
		return game.getPlayersInGame().stream().sorted(Comparator.comparing(PlayerInGame::getScore)).findFirst().get();
	}

}
