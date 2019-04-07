package tennis.persistence;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import tennis.domain.Game;
import tennis.domain.Player;
import tennis.domain.TennisPlayerScore;

/**
 * Unit-Test class for {@link GameRepo}. Sets up a H2-database to test the repo
 * against a real persistence-layer.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@Transactional
@Component
class GameRepoTest {

	@Autowired
	private PlayerRepo playerRepo;

	@Autowired
	private GameRepo gameRepo;

	@Test
	void testRepositoryInjection() {
		assertThat(gameRepo, notNullValue());
	}

	@Test
	void testInsertOneWithPlayerCascading() {
		Player one = new Player("Federer");
		Player two = new Player("Nadal");
		Game game = new Game(one, two);
		Game persisted = gameRepo.save(game);

		assertThat(persisted, is(notNullValue()));
	}

	@Test
	void testInsertOneWithPersistedPlayer() {
		Player one = playerRepo.save(new Player("Steffi"));
		Player two = playerRepo.save(new Player("Graf"));
		Game game = new Game(one, two);
		Game persisted = gameRepo.save(game);

		assertThat(persisted, is(notNullValue()));
	}

	@Test
	void testReadAndCheckContent() {
		Player one = new Player("Anna");
		Player two = new Player("Berta");
		Game game = new Game(one, two);
		gameRepo.save(game);

		Player three = new Player("Carlo");
		Player four = new Player("Daniel");
		Game game2 = new Game(three, four);
		Game persisted2 = gameRepo.save(game2);

		persisted2.getPlayerInGame(three).setScore(TennisPlayerScore.GAME);
		persisted2.setStatus(Game.Status.ABANDONED);

		Game persisted3 = gameRepo.findById(persisted2.getGameNo()).get();
		assertThat(persisted3.getPlayerInGame(three).getScore(), equalTo(TennisPlayerScore.GAME));
		assertThat(persisted3.getStatus(), equalTo(Game.Status.ABANDONED));

	}

	/**
	 * Alternate starting point for tests. Can be called to run tests outside a Junit-Runner.
	 */
	public static void main(String[] args) {

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

		ctx.register(AppConfig.class);
		ctx.refresh();

		GameRepoTest sut = ctx.getBean(GameRepoTest.class);
		sut.testRepositoryInjection();
		sut.testInsertOneWithPlayerCascading();
		// sut.testInsertOneWithPersistedPlayer(); // cascade merge does not work!
		sut.testReadAndCheckContent();
	}
}
