package tennis.persistence;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import tennis.domain.Player;

/**
 * Unit-Test class for {@link PlayerRepo}. Sets up a H2-database to test the repo
 * against a real persistence-layer.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@Transactional
@Component
class PlayerRepoTest {

	@Autowired
	private PlayerRepo playerRepo;

	@Test
	void testRepositoryInjection() {
		assertThat(playerRepo, notNullValue());
	}

	@Test
	void testInsertWithoutName() {
		Player persistedPlayer = playerRepo.save(new Player());
		assertThat(persistedPlayer, is(notNullValue()));
	}

	@Test
	void testInsertOne() {
		Player persistedPlayer = playerRepo.save(new Player("Federer"));
		assertThat(persistedPlayer, is(notNullValue()));
		assertThat(persistedPlayer.getName(), equalTo("Federer"));
	}

	@Test
	void testInsertAnotherWithSameName() {
		playerRepo.save(new Player("Federer"));
		playerRepo.save(new Player("Federer"));

	}

	@Test
	void testInsertThreeAndReadAll() {
		Set<Player> persisted = new HashSet<Player>();
		persisted.add(playerRepo.save(new Player("Federer")));
		persisted.add(playerRepo.save(new Player("Nadal")));
		persisted.add(playerRepo.save(new Player("Djokovic")));

		Iterable<Player> players = playerRepo.findAll();
		for (Player player : players) {
			assertTrue(persisted.contains(player));
		}
	}

	@Test
	void testInsertThreeAndReadOne() {
		Set<Player> persisted = new HashSet<Player>();
		persisted.add(playerRepo.save(new Player("Federer")));
		persisted.add(playerRepo.save(new Player("Nadal")));
		persisted.add(playerRepo.save(new Player("Djokovic")));

		assertTrue(playerRepo.existsById("Nadal"));
		assertTrue(playerRepo.findById("Federer").isPresent());
	}

	/**
	 * Alternate starting point for tests. Can be called to run tests outside a Junit-Runner.
	 */
	public static void main(String[] args) {

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

		ctx.register(AppConfig.class);
		ctx.refresh();

		PlayerRepoTest sut = ctx.getBean(PlayerRepoTest.class);
		sut.testRepositoryInjection();
		// sut.testInsertWithoutName();
		sut.testInsertOne();
		sut.testInsertThreeAndReadAll();
		sut.testInsertAnotherWithSameName(); // WTF???
	}
}
