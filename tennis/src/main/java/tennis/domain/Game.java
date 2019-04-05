package tennis.domain;

import java.util.ArrayList;
import java.util.Collection;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class Game {
	
	 public enum Status {
		NOT_YET_STARTED,
		RUNNING,
		PAUSED,
		FINISHED,
		ABANDONED;
	}
	
	private @Getter Integer gameNo = 0;
	private @Getter Collection<PlayerInGame> playersInGame = new ArrayList<PlayerInGame>();
	private @Getter @Setter Status status;
	
	
    public Game( @NonNull Player player1, @NonNull Player player2) {
        super();
        this.playersInGame.add(new PlayerInGame(player1));
        this.playersInGame.add(new PlayerInGame(player2));
        this.status = Status.NOT_YET_STARTED;
    }
	
	
	public PlayerInGame getPlayerInGame(Player player) {
        return playersInGame.stream().filter(e -> e.getPlayer().equals(player)).findAny().get();
    }
	
	public PlayerInGame getPlayerInGameOtherThan(Player player) {
	    return playersInGame.stream().filter(e -> !e.getPlayer().equals(player)).findAny().get();
	}

}
