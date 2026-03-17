import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import games.PrisonerDilemma;
import loggers.MoveLogger;
import loggers.ScoreLogger;
import models.History;
import robots.Cooperator;
import robots.Defector;
import robots.HumanBot;
import robots.Reciprocator;
import robots.Robot;
import tournaments.RoundRobin;

import java.util.ArrayList;

class PrisonerDilemmaTest {
	
	Robot alice;
    Robot bob;
    Robot charles;
    Robot darren;
    PrisonerDilemma pd;
    RoundRobin robin;
    ArrayList<History> history;
    ArrayList<History> emptyHistory;
	
	@BeforeEach
	void setUp() throws Exception {
		
		alice = new Cooperator("Alice");
        bob = new Defector("Bob");
        charles = new Reciprocator("Charles");
        darren = new HumanBot("Darren");
        pd = new PrisonerDilemma();
        
        history = new ArrayList<History>();
        emptyHistory = new ArrayList<>();
        
        
	}
	
	@Test
	void testPlayerActions() {
		history.add(new History("Bob", "Charles", "DEFECT", "COOPERATE", 0, 5));
		
		assertEquals("COOPERATE", alice.getAction("Bob", emptyHistory));
		assertEquals("DEFECT", bob.getAction("Charles", emptyHistory));
		assertEquals("COOPERATE", charles.getAction("Bob", emptyHistory));
		
		assertEquals("DEFECT", charles.getAction("Bob", history));
		
		history.add(new History("Charles", "Bob", "COOPERATE", "COOPERATE", 0, 5));
		assertEquals("COOPERATE", charles.getAction("Bob", history));
		
//		assertEquals("DEFECT", darren.getAction("Bob", emptyHistory));
	}
	
	@Test
    void testGameOutcomes() {
        int[] cc = pd.getOutcome("COOPERATE", "COOPERATE");
        assertArrayEquals(new int[]{3, 3}, cc);

        int[] cd = pd.getOutcome("COOPERATE", "DEFECT");
        assertArrayEquals(new int[]{0, 5}, cd);
    }
	
	@Test
	void testLoggers() {
        pd.addListener("moves", new MoveLogger("moves.txt"));
        
        ScoreLogger scoreLogger = new ScoreLogger("scores.txt");
        pd.addListener("scores", scoreLogger);
        assertEquals(1, pd.getListeners("scores").size());
        
        pd.removeListener("scores", scoreLogger);
        assertEquals(0, pd.getListeners("scores").size());
        
	}
	
	@Test
    void testMatch() {
		history = new ArrayList<History>();
        History match = pd.play(alice, bob, history);
        
        assertEquals(0, match.result1());
        assertEquals(5, match.result2());
    }
	
	@Test
    void testRRBracket() {
        Robot[] players = {alice, bob, charles};
        robin = new RoundRobin("PDRR", pd);
        for(Robot bot : players) {
        	robin.addPlayer(bot);
        }
        robin.getBracket();
        ArrayList<Robot[]> curr = robin.getCurrentBracket();
        assertEquals(3, curr.size());
    }
	
	@Test
    void testTournamentRun() {
        Robot[] players = {alice, bob};
        robin = new RoundRobin("PDRR", pd);
        for(Robot bot : players) {
        	robin.addPlayer(bot);
        }
        
        Robot[] rankings = robin.run();
        
        assertEquals("Bob", rankings[0].getName());
        assertEquals(5, rankings[0].getScore(robin.getHistory()));
    }
	
}
