package tennis.service.integration;

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

import tennis.domain.Game;
import tennis.domain.Player;
import tennis.service.Match;

/**
 * Tests {@link Match}. This is an integrative with all layers present. Uses a
 * H2-database for persistency.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@Component
class AllLayersIntegrationTest {

	@Autowired
	private Match tennisMatch;

	@Test
	void testBeanInjectionTest() {
		assertThat(tennisMatch, is(notNullValue()));
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

	/**
	 * Alternate starting point for tests. Can be called to run tests outside a Junit-Runner.
	 */
	public static void main(String[] args) {

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

		ctx.register(AppConfig.class);
		ctx.refresh();

		AllLayersIntegrationTest sut = ctx.getBean(AllLayersIntegrationTest.class);
		sut.testBeanInjectionTest();
		sut.testAdvantageFedererTwice();
	}

}
