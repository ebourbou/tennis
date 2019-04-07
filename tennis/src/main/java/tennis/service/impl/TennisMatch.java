package tennis.service.impl;

import static tennis.service.impl.ScoreCallAccordingUSTA.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tennis.domain.Game;
import tennis.domain.Player;
import tennis.domain.PlayerInGame;
import tennis.domain.TennisPlayerScore;
import tennis.domain.Game.Status;
import tennis.persistence.GameRepo;
import tennis.service.Match;

/**
 * Implements {@link Match} for the game of tennis.
 *
 */
@Component
public class TennisMatch implements Match {

	@Autowired
	private GameRepo gameRepo;

	@Override
	public Integer startGame(Player player1, Player player2) {
		Game game = new Game(player1, player2);
		game.setStatus(Game.Status.RUNNING);
		return gameRepo.save(game).getGameNo();
	}

	@Override
	public void winPoint(Integer gameNo, Player player) {
		Game game = getGame(gameNo);
		assert !game.getStatus().equals(Game.Status.FINISHED);
		PlayerInGame playerWinningPoint = game.getPlayerInGame(player);
		PlayerInGame playerNotWinningPoint = game.getPlayerInGameOtherThan(player);
		playerWinningPoint.setScore(playerWinningPoint.getScore().winPoint(playerNotWinningPoint.getScore()));
		playerNotWinningPoint.setScore(playerNotWinningPoint.getScore().loosePoint());
		finishGameIfConditionsAreMet(game, playerWinningPoint.getScore(), playerNotWinningPoint.getScore());
	}

    @Override
	public void abandonGame(Integer gameNo) {
		getGame(gameNo).setStatus(Game.Status.ABANDONED);

	}
    
    @Override
	public String getScoreCall(Integer gameNo) {
		return getScoreCallAccordingUSTA(getGame(gameNo));
	}

    @Override
	public Status getStatus(Integer gameNo) {
		return getGame(gameNo).getStatus();
	}
    
	private void finishGameIfConditionsAreMet(Game game, TennisPlayerScore winning, TennisPlayerScore loosing) {
		if (winning.equals(TennisPlayerScore.WIN)) {
			game.setStatus(Game.Status.FINISHED);
		}
	}

	private Game getGame(Integer gameNo) {
		return gameRepo.findById(gameNo).orElseThrow();
	}

}
