package tennis.service.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tennis.domain.Game;
import tennis.domain.Player;
import tennis.service.Match;
import tennis.service.impl.TennisMatch;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {TennisMatchTest.AppConfig.class})
class TennisMatchTest {
    
   // @Autowired
    private Match match = new TennisMatch();
//    private Match gameUnderTest = new TennisMatch();


    @Test
    void testStartGameSuccessfully() {
        Integer gameNo = match.startGame(new Player("Federer"), new Player("Nadal"));
        assertThat(match.getStatus(gameNo), is(Game.Status.RUNNING));
        assertThat(match.getScoreCall(gameNo), equalTo("Love - all"));
    }
    
    @Test
    void testStartGameWithPreconditionsNotMet() {
        assertThrows(NullPointerException.class, ()->match.startGame(new Player("Federer"), null));
        assertThrows(NullPointerException.class, ()->match.startGame(null, null));
        assertThrows(NullPointerException.class, ()->match.startGame(new Player(null), new Player(null)));

    }
    
    @Test
    void testAbandonGame() {
        Integer gameNo = match.startGame(new Player("Federer"), new Player("Nadal"));
        match.abandonGame(gameNo);
        assertThat(match.getStatus(gameNo), is(Game.Status.ABANDONED));
    }
    
    @Test
    void testLoveAll() {
        Player federer = new Player("Federer");
        Player nadal =  new Player("Nadal");
        Integer gameNo = match.startGame(federer, nadal);
        assertThat(match.getScoreCall(gameNo), equalTo("Love - all"));
   }
    
    @Test
    void testFifteenAll() {
        Player federer = new Player("Federer");
        Player nadal =  new Player("Nadal");
        Integer gameNo = match.startGame(federer, nadal);
        
        match.winPoint(gameNo, federer);
        match.winPoint(gameNo, nadal);

        assertThat(match.getScoreCall(gameNo), equalTo("Fifteen - all"));
   }
    
    @Test
    void testDeuceWith40() {
        Player federer = new Player("Federer");
        Player nadal =  new Player("Nadal");
        Integer gameNo = match.startGame(federer, nadal);
        
        match.winPoint(gameNo, federer);  // 15
        match.winPoint(gameNo, nadal);
        match.winPoint(gameNo, federer);  // 30
        match.winPoint(gameNo, nadal);
        match.winPoint(gameNo, federer);  // deuce
        match.winPoint(gameNo, nadal);

        assertThat(match.getScoreCall(gameNo), equalTo("Deuce"));
   }
    
    @Test
    void testDeuceWithGame() {
        Player federer = new Player("Federer");
        Player nadal =  new Player("Nadal");
        Integer gameNo = match.startGame(federer, nadal);
        
        match.winPoint(gameNo, federer);  // 15
        match.winPoint(gameNo, nadal);
        match.winPoint(gameNo, federer);  // 30
        match.winPoint(gameNo, nadal);
        match.winPoint(gameNo, federer);  // 40
        match.winPoint(gameNo, nadal);
        
        match.winPoint(gameNo, federer);  // deuce with game
        match.winPoint(gameNo, nadal);

        assertThat(match.getScoreCall(gameNo), equalTo("Deuce"));
   }
    
    @Test
    void testDeuceWithSeveralGame() {
        Player federer = new Player("Federer");
        Player nadal =  new Player("Nadal");
        Integer gameNo = match.startGame(federer, nadal);
        
        match.winPoint(gameNo, federer);  // 15
        match.winPoint(gameNo, nadal);
        match.winPoint(gameNo, federer);  // 30
        match.winPoint(gameNo, nadal);
        match.winPoint(gameNo, federer);  // 40
        match.winPoint(gameNo, nadal);
        
        match.winPoint(gameNo, federer);  // deuce with game
        match.winPoint(gameNo, nadal);
        
        match.winPoint(gameNo, federer);  // again deuce with game
        match.winPoint(gameNo, nadal);

        assertThat(match.getScoreCall(gameNo), equalTo("Deuce"));
   }
    
    @Test
    void testFedererFifteenLove() {
        Player federer = new Player("Federer");
        Player nadal =  new Player("Nadal");
        Integer gameNo = match.startGame(federer, nadal);

        match.winPoint(gameNo, federer);
        assertThat(match.getScoreCall(gameNo), equalTo("Fifteen - Love"));
    }
    
    @Test
    void testNadalWinsGameLove() {
        Player federer = new Player("Federer");
        Player nadal =  new Player("Nadal");
        Integer gameNo = match.startGame(federer, nadal);
        
        match.winPoint(gameNo, nadal);  // 15
        match.winPoint(gameNo, nadal);  // 30
        match.winPoint(gameNo, nadal);  // 40
        match.winPoint(gameNo, nadal);  // game and win

        assertThat(match.getScoreCall(gameNo), equalTo("Win Nadal"));
        assertThat(match.getStatus(gameNo), is(Game.Status.FINISHED));

    }
    
    @Test
    void testNadalWinsGame30() {
        Player federer = new Player("Federer");
        Player nadal =  new Player("Nadal");
        Integer gameNo = match.startGame(federer, nadal);
        
        match.winPoint(gameNo, federer);  // 15
        match.winPoint(gameNo, federer);  // 30

        match.winPoint(gameNo, nadal);  // 15
        match.winPoint(gameNo, nadal);  // 30
        match.winPoint(gameNo, nadal);  // 40
        match.winPoint(gameNo, nadal);  // game and win

        assertThat(match.getScoreCall(gameNo), equalTo("Win Nadal"));
        assertThat(match.getStatus(gameNo), is(Game.Status.FINISHED));

    }
    
    @Test
    void testGame40DoesNotFinishMatch() {
        Player federer = new Player("Federer");
        Player nadal =  new Player("Nadal");
        Integer gameNo = match.startGame(federer, nadal);
        
        match.winPoint(gameNo, federer);  // 15
        match.winPoint(gameNo, federer);  // 30
        match.winPoint(gameNo, federer);  // 40

        match.winPoint(gameNo, nadal);  // 15
        match.winPoint(gameNo, nadal);  // 30
        match.winPoint(gameNo, nadal);  // 40
        match.winPoint(gameNo, nadal);  // game and win

        assertThat(match.getScoreCall(gameNo), equalTo("Game Nadal"));
        assertThat(match.getStatus(gameNo), is(Game.Status.RUNNING));
    }
    
    @Test
    void testContinueAlreadyFinishedMatch() {
        Player federer = new Player("Federer");
        Player nadal =  new Player("Nadal");
        Integer gameNo = match.startGame(federer, nadal);
        
        match.winPoint(gameNo, nadal);  // 15
        match.winPoint(gameNo, nadal);  // 30
        match.winPoint(gameNo, nadal);  // 40
        match.winPoint(gameNo, nadal);  // game and win

        assertThrows(AssertionError.class, ()->match.winPoint(gameNo, nadal));

    }
    
    @Test
    void testAdvantageFedererTwice() {
        Player federer = new Player("Federer");
        Player nadal =  new Player("Nadal");
        Integer gameNo = match.startGame(federer, nadal);
        
        match.winPoint(gameNo, federer);  // 15
        match.winPoint(gameNo, federer);  // 30
        match.winPoint(gameNo, federer);  // 40

        match.winPoint(gameNo, nadal);  // 15
        match.winPoint(gameNo, nadal);  // 30
        match.winPoint(gameNo, nadal);  // 40
        
        match.winPoint(gameNo, federer);  // game
        match.winPoint(gameNo, nadal);  // game and win
        
        match.winPoint(gameNo, federer);  // advantage
        assertThat(match.getScoreCall(gameNo), equalTo("Advantage Federer"));

        match.winPoint(gameNo, nadal);  
        assertThat(match.getScoreCall(gameNo), equalTo("Deuce"));

        match.winPoint(gameNo, federer);  // advantage
        assertThat(match.getScoreCall(gameNo), equalTo("Advantage Federer"));
        
        match.winPoint(gameNo, nadal);  
        assertThat(match.getScoreCall(gameNo), equalTo("Deuce"));
        
        match.winPoint(gameNo, federer);  // advantage
        match.winPoint(gameNo, federer);  // win
        assertThat(match.getScoreCall(gameNo), equalTo("Win Federer"));

        assertThat(match.getStatus(gameNo), is(Game.Status.FINISHED));
    }

//    @Configuration
//    //@ComponentScan(basePackageClasses = {TennisMatch.class})
//    public static class AppConfig {
//        
//        @Bean
//        public Match match() {
//            return new TennisMatch();
//        }
//    }

}


