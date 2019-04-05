package tennis.service.impl;

import static tennis.service.impl.ScoreCallAccordingUSTA.*;

import org.springframework.stereotype.Component;

import tennis.domain.Game;
import tennis.domain.Player;
import tennis.domain.PlayerInGame;
import tennis.domain.TennisPlayerScore;
import tennis.domain.Game.Status;
import tennis.service.Match;

public class TennisMatch implements Match {

    private Game game;

    
    public Integer startGame(Player player1, Player player2) {
        game = new Game(player1, player2);
        game.setStatus(Game.Status.RUNNING);
        return game.getGameNo();
    }

    
    public void winPoint(Integer gameNo, Player player) {
        assert !game.getStatus().equals(Game.Status.FINISHED);
        PlayerInGame playerWinningPoint = game.getPlayerInGame(player);
        PlayerInGame playerNotWinningPoint = game.getPlayerInGameOtherThan(player);
        playerWinningPoint.setScore(playerWinningPoint.getScore().winPoint(playerNotWinningPoint.getScore()));
        playerNotWinningPoint.setScore(playerNotWinningPoint.getScore().loosePoint());
        finishGameIfConditionsAreMet(playerWinningPoint.getScore(), playerNotWinningPoint.getScore());
    }

    
    public void abandonGame( Integer gameNo) {
        game.setStatus(Game.Status.ABANDONED);

    }

    private void finishGameIfConditionsAreMet(TennisPlayerScore winning, TennisPlayerScore loosing) {
        if (winning.equals(TennisPlayerScore.WIN)) {
            game.setStatus(Game.Status.FINISHED);
        }
    }

    
    public String getScoreCall( Integer gameNo) {
        return getScoreCallAccordingUSTA(game);
    }


    
    public Status getStatus(Integer gameNo) {
        assert gameNo != null; 
        return game.getStatus();
    }

}
