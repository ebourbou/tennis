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

import tennis.domain.Player;
import tennis.domain.PlayerInGame;
import tennis.domain.TennisPlayerScore;

/**
 * Unit-Test class for {@link PlayerInGameRepo}. Sets up a H2-database to test
 * the repo against a real persistence-layer.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@Transactional
@Component
class PlayerInGameRepoTest {

	@Autowired
	private PlayerRepo playerRepo;

	@Autowired
	private PlayerInGameRepo playerInGameRepo;

	@Test
	void testRepositoryInjection() {
		assertThat(playerInGameRepo, notNullValue());
	}

	@Test
	void testInsertOneWithPlayerCascading() {
		PlayerInGame persisted = playerInGameRepo.save(new PlayerInGame(new Player("Federer")));
		assertThat(persisted, is(notNullValue()));
	}

	@Test
	void testInsertOneWithPersistedPlayer() {
		Player persistedPlayer = playerRepo.save(new Player("Steffi"));
		PlayerInGame persisted = playerInGameRepo.save(new PlayerInGame(persistedPlayer));
		assertThat(persisted, is(notNullValue()));
	}

	@Test
	void testInsertTwoWithCascadingPlayerSameName() {
		PlayerInGame persisted = playerInGameRepo.save(new PlayerInGame(new Player("Federer")));
		playerInGameRepo.save(new PlayerInGame(new Player("Federer")));
		assertThat(persisted, is(notNullValue()));
	}

	@Test
	void testReadTwoAndCheckContent() {
		PlayerInGame one = new PlayerInGame(new Player("Max"));
		one.setScore(TennisPlayerScore.ADVANTAGE);

		PlayerInGame two = new PlayerInGame(new Player("Moritz"));
		two.setScore(TennisPlayerScore.GAME);

		PlayerInGame persistedOne = playerInGameRepo.save(one);
		PlayerInGame persistedTwo = playerInGameRepo.save(two);

		assertThat(playerInGameRepo.findById(persistedTwo.id).get().getScore(), equalTo(TennisPlayerScore.GAME));
		assertThat(playerInGameRepo.findById(persistedOne.id).get().getScore(), equalTo(TennisPlayerScore.ADVANTAGE));

		assertThat(playerInGameRepo.findById(persistedTwo.id).get().getPlayer().getName(), equalTo("Moritz"));
		assertThat(playerInGameRepo.findById(persistedOne.id).get().getPlayer().getName(), equalTo("Max"));

	}
	
	/**
	 * Alternate starting point for tests. Can be called to run tests outside a Junit-Runner.
	 */
	public static void main(String[] args) {

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

		ctx.register(AppConfig.class);
		ctx.refresh();

		PlayerInGameRepoTest sut = ctx.getBean(PlayerInGameRepoTest.class);
		sut.testRepositoryInjection();
		sut.testInsertOneWithPlayerCascading();
		// sut.testInsertOneWithPersistedPlayer(); // cascade merge does not work!
		sut.testReadTwoAndCheckContent();
	}
}
