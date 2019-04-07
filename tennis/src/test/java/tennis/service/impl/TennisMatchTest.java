package tennis.service.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tennis.domain.Game;
import tennis.domain.Player;
import tennis.persistence.GameRepo;
import tennis.service.Match;

/**
 * Tests the functionnality of {@link TennisMatch} as described in {@link Match}
 * Concentrates on the service layer and mocks persistency and any other dependant components.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@Component
class TennisMatchTest {

//	@Autowired
//    private Match tennisMatch;
	private Match tennisMatch = new TennisMatch();

	@Before
	public void setUpMocks() {

		GameRepo mockRepo = mock(GameRepo.class);
		ArgumentCaptor<Game> gameCaptor = ArgumentCaptor.forClass(Game.class);

		Mockito.verify(mockRepo).save(gameCaptor.capture());
		when(mockRepo.findById(any())).thenReturn(Optional.of(gameCaptor.getValue()));

	}

	@Test
	void testBeanInjectionTest() {
		assertThat(tennisMatch, is(notNullValue()));
	}

	@Test
	void testStartGameSuccessfully() {
		Integer gameNo = tennisMatch.startGame(new Player("Federer"), new Player("Nadal"));
		assertThat(tennisMatch.getStatus(gameNo), is(Game.Status.RUNNING));
		assertThat(tennisMatch.getScoreCall(gameNo), equalTo("Love - all"));
	}

	@Test
	void testStartGameWithPreconditionsNotMet() {
		assertThrows(NullPointerException.class, () -> tennisMatch.startGame(new Player("Federer"), null));
		assertThrows(NullPointerException.class, () -> tennisMatch.startGame(null, null));
		assertThrows(NullPointerException.class, () -> tennisMatch.startGame(new Player(null), new Player(null)));

	}

	@Test
	void testAbandonGame() {
		Integer gameNo = tennisMatch.startGame(new Player("Federer"), new Player("Nadal"));
		tennisMatch.abandonGame(gameNo);
		assertThat(tennisMatch.getStatus(gameNo), is(Game.Status.ABANDONED));
	}

	@Test
	void testLoveAll() {
		Player federer = new Player("Federer");
		Player nadal = new Player("Nadal");
		Integer gameNo = tennisMatch.startGame(federer, nadal);
		assertThat(tennisMatch.getScoreCall(gameNo), equalTo("Love - all"));
	}

	@Test
	void testFifteenAll() {
		Player federer = new Player("Federer");
		Player nadal = new Player("Nadal");
		Integer gameNo = tennisMatch.startGame(federer, nadal);

		tennisMatch.winPoint(gameNo, federer);
		tennisMatch.winPoint(gameNo, nadal);

		assertThat(tennisMatch.getScoreCall(gameNo), equalTo("Fifteen - all"));
	}

	@Test
	void testDeuceWith40() {
		Player federer = new Player("Federer");
		Player nadal = new Player("Nadal");
		Integer gameNo = tennisMatch.startGame(federer, nadal);

		tennisMatch.winPoint(gameNo, federer); // 15
		tennisMatch.winPoint(gameNo, nadal);
		tennisMatch.winPoint(gameNo, federer); // 30
		tennisMatch.winPoint(gameNo, nadal);
		tennisMatch.winPoint(gameNo, federer); // deuce
		tennisMatch.winPoint(gameNo, nadal);

		assertThat(tennisMatch.getScoreCall(gameNo), equalTo("Deuce"));
	}

	@Test
	void testDeuceWithGame() {
		Player federer = new Player("Federer");
		Player nadal = new Player("Nadal");
		Integer gameNo = tennisMatch.startGame(federer, nadal);

		tennisMatch.winPoint(gameNo, federer); // 15
		tennisMatch.winPoint(gameNo, nadal);
		tennisMatch.winPoint(gameNo, federer); // 30
		tennisMatch.winPoint(gameNo, nadal);
		tennisMatch.winPoint(gameNo, federer); // 40
		tennisMatch.winPoint(gameNo, nadal);

		tennisMatch.winPoint(gameNo, federer); // deuce with game
		tennisMatch.winPoint(gameNo, nadal);

		assertThat(tennisMatch.getScoreCall(gameNo), equalTo("Deuce"));
	}

	@Test
	void testDeuceWithSeveralGame() {
		Player federer = new Player("Federer");
		Player nadal = new Player("Nadal");
		Integer gameNo = tennisMatch.startGame(federer, nadal);

		tennisMatch.winPoint(gameNo, federer); // 15
		tennisMatch.winPoint(gameNo, nadal);
		tennisMatch.winPoint(gameNo, federer); // 30
		tennisMatch.winPoint(gameNo, nadal);
		tennisMatch.winPoint(gameNo, federer); // 40
		tennisMatch.winPoint(gameNo, nadal);

		tennisMatch.winPoint(gameNo, federer); // deuce with game
		tennisMatch.winPoint(gameNo, nadal);

		tennisMatch.winPoint(gameNo, federer); // again deuce with game
		tennisMatch.winPoint(gameNo, nadal);

		assertThat(tennisMatch.getScoreCall(gameNo), equalTo("Deuce"));
	}

	@Test
	void testFedererFifteenLove() {
		Player federer = new Player("Federer");
		Player nadal = new Player("Nadal");
		Integer gameNo = tennisMatch.startGame(federer, nadal);

		tennisMatch.winPoint(gameNo, federer);
		assertThat(tennisMatch.getScoreCall(gameNo), equalTo("Fifteen - Love"));
	}

	@Test
	void testNadalWinsGameLove() {
		Player federer = new Player("Federer");
		Player nadal = new Player("Nadal");
		Integer gameNo = tennisMatch.startGame(federer, nadal);

		tennisMatch.winPoint(gameNo, nadal); // 15
		tennisMatch.winPoint(gameNo, nadal); // 30
		tennisMatch.winPoint(gameNo, nadal); // 40
		tennisMatch.winPoint(gameNo, nadal); // game and win

		assertThat(tennisMatch.getScoreCall(gameNo), equalTo("Win Nadal"));
		assertThat(tennisMatch.getStatus(gameNo), is(Game.Status.FINISHED));

	}

	@Test
	void testNadalWinsGame30() {
		Player federer = new Player("Federer");
		Player nadal = new Player("Nadal");
		Integer gameNo = tennisMatch.startGame(federer, nadal);

		tennisMatch.winPoint(gameNo, federer); // 15
		tennisMatch.winPoint(gameNo, federer); // 30

		tennisMatch.winPoint(gameNo, nadal); // 15
		tennisMatch.winPoint(gameNo, nadal); // 30
		tennisMatch.winPoint(gameNo, nadal); // 40
		tennisMatch.winPoint(gameNo, nadal); // game and win

		assertThat(tennisMatch.getScoreCall(gameNo), equalTo("Win Nadal"));
		assertThat(tennisMatch.getStatus(gameNo), is(Game.Status.FINISHED));

	}

	@Test
	void testGame40DoesNotFinishMatch() {
		Player federer = new Player("Federer");
		Player nadal = new Player("Nadal");
		Integer gameNo = tennisMatch.startGame(federer, nadal);

		tennisMatch.winPoint(gameNo, federer); // 15
		tennisMatch.winPoint(gameNo, federer); // 30
		tennisMatch.winPoint(gameNo, federer); // 40

		tennisMatch.winPoint(gameNo, nadal); // 15
		tennisMatch.winPoint(gameNo, nadal); // 30
		tennisMatch.winPoint(gameNo, nadal); // 40
		tennisMatch.winPoint(gameNo, nadal); // game and win

		assertThat(tennisMatch.getScoreCall(gameNo), equalTo("Game Nadal"));
		assertThat(tennisMatch.getStatus(gameNo), is(Game.Status.RUNNING));
	}

	@Test
	void testContinueAlreadyFinishedMatch() {
		Player federer = new Player("Federer");
		Player nadal = new Player("Nadal");
		Integer gameNo = tennisMatch.startGame(federer, nadal);

		tennisMatch.winPoint(gameNo, nadal); // 15
		tennisMatch.winPoint(gameNo, nadal); // 30
		tennisMatch.winPoint(gameNo, nadal); // 40
		tennisMatch.winPoint(gameNo, nadal); // game and win

		assertThrows(AssertionError.class, () -> tennisMatch.winPoint(gameNo, nadal));

	}

	@Test
	void testAdvantageFedererTwice() {
		Player federer = new Player("Federer");
		Player nadal = new Player("Nadal");
		Integer gameNo = tennisMatch.startGame(federer, nadal);

		tennisMatch.winPoint(gameNo, federer); // 15
		tennisMatch.winPoint(gameNo, federer); // 30
		tennisMatch.winPoint(gameNo, federer); // 40

		tennisMatch.winPoint(gameNo, nadal); // 15
		tennisMatch.winPoint(gameNo, nadal); // 30
		tennisMatch.winPoint(gameNo, nadal); // 40

		tennisMatch.winPoint(gameNo, federer); // game
		tennisMatch.winPoint(gameNo, nadal); // game and win

		tennisMatch.winPoint(gameNo, federer); // advantage
		assertThat(tennisMatch.getScoreCall(gameNo), equalTo("Advantage Federer"));

		tennisMatch.winPoint(gameNo, nadal);
		assertThat(tennisMatch.getScoreCall(gameNo), equalTo("Deuce"));

		tennisMatch.winPoint(gameNo, federer); // advantage
		assertThat(tennisMatch.getScoreCall(gameNo), equalTo("Advantage Federer"));

		tennisMatch.winPoint(gameNo, nadal);
		assertThat(tennisMatch.getScoreCall(gameNo), equalTo("Deuce"));

		tennisMatch.winPoint(gameNo, federer); // advantage
		tennisMatch.winPoint(gameNo, federer); // win
		assertThat(tennisMatch.getScoreCall(gameNo), equalTo("Win Federer"));

		assertThat(tennisMatch.getStatus(gameNo), is(Game.Status.FINISHED));
	}

}
