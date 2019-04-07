package tennis.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Game {

	public enum Status {
		NOT_YET_STARTED, 
		RUNNING, 
		PAUSED, 
		FINISHED, 
		ABANDONED;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private @Getter Integer gameNo = 0;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "gameNo", referencedColumnName = "gameNo")
	private @Getter Collection<PlayerInGame> playersInGame = new ArrayList<PlayerInGame>();

	@Enumerated(EnumType.STRING)
	private @Getter @Setter Status status;

	public Game(@NonNull Player player1, @NonNull Player player2) {
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
