import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import games.PrisonerDilemma;
import loggers.MoveLogger;
import loggers.ScoreLogger;
import models.History;
import robots.Cooperator;
import robots.Defector;
import robots.Reciprocator;
import robots.Robot;
import tournaments.RoundRobin;

import java.util.ArrayList;

class PrisonerDilemmaTest {
	
	Robot alice;
    Robot bob;
    Robot charles;
    PrisonerDilemma pd;
    RoundRobin robin;
    ArrayList<History> history;
	
	@BeforeEach
	void setUp() throws Exception {
		
		alice = new Cooperator("Alice");
        bob = new Defector("Bob");
        charles = new Reciprocator("Charles");
        pd = new PrisonerDilemma();
        history = new ArrayList<>();
        
        pd.addListener("moves", new MoveLogger("moves.txt"));
        pd.addListener("scores", new ScoreLogger("scores.txt"));
	}
	
	@Test
	void testPlayerActions() {
		history = new ArrayList<History>();
		history.add(new History("Bob", "Charles", "DEFECT", "COOPERATE", 0, 5));
		
		assertEquals("COOPERATE", alice.getAction("Bob", history));
		assertEquals("DEFECT", bob.getAction("Charles", history));
		assertEquals("DEFECT", charles.getAction("Bob", history));
		
		history.add(new History("Bob", "Charles", "COOPERATE", "COOPERATE", 0, 5));
		assertEquals("COOPERATE", charles.getAction("Bob", history));
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
        pd.addListener("scores", new ScoreLogger("scores.txt"));
        assertEquals(1, pd.getListeners("scores").size());
        
        pd.removeListener("scores", null);
        
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
