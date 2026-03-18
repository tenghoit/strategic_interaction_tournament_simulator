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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
        
        int[] dc = pd.getOutcome("DEFECT", "COOPERATE");
        assertArrayEquals(new int[]{5, 0}, dc);

        int[] dd = pd.getOutcome("DEFECT", "DEFECT");
        assertArrayEquals(new int[]{1, 1}, dd);

        int[] nn = pd.getOutcome("a", "a");
        assertArrayEquals(new int[]{0, 0}, nn);
    }
	
	@Test
	void testLoggers() {
		History mockHistory = new History("Charles", "Bob", "COOPERATE", "COOPERATE", 3, 3);
		
		assertEquals(true, pd.addListener("moves", new MoveLogger("moves.txt")));
		assertEquals(true, pd.addListener("moves", new MoveLogger("moves2.txt")));
        
		
		assertEquals(false, pd.notify("scores", mockHistory));
		
		
        ScoreLogger scoreLogger = new ScoreLogger("scores.txt");
        ScoreLogger scoreLogger2 = new ScoreLogger("scores2.txt");
        
        assertEquals(false, pd.removeListener("scores", scoreLogger));
        pd.addListener("scores", scoreLogger);
        assertEquals(false, pd.removeListener("scores", scoreLogger2));
        assertEquals(true, pd.removeListener("scores", scoreLogger));
        
        assertEquals(true, pd.notify("scores", mockHistory));
        
		scoreLogger.update(mockHistory);
		try {
			List<String> scoreLines = Files.readAllLines(Paths.get("scores.txt"));
	        assertEquals("Charles 3", scoreLines.get(0));
	        assertEquals("Bob 3", scoreLines.get(1));
	        
	        String testFile = "test.txt";
	        MoveLogger moveLogger = new MoveLogger(testFile);
	        moveLogger.update(mockHistory);
	        
	        List<String> lines = Files.readAllLines(Paths.get(testFile));
	        assertEquals("Charles COOPERATE", lines.get(0));
	        assertEquals("Bob COOPERATE", lines.get(1));
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		
		
}
		

        
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
