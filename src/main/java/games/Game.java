package games;
import java.util.ArrayList;
import java.util.HashMap;

import loggers.Listener;
import models.History;
import robots.Robot;

public abstract class Game {
	
	public History play(Robot player1, Robot player2, ArrayList<History> history) {
		String name1 = player1.getName();
		String name2 = player2.getName();
		String action1 = player1.getAction(name2, history);
		String action2 = player2.getAction(name1, history);
		
		int[] outcomes = this.getOutcome(action1, action2);
		
		History match = new History(name1, name2, action1, action2, outcomes[0], outcomes[1]);
		
		return match;
	}
	
	public Game() {
		super();
	}

	public abstract int[] getOutcome(String action1, String action2);
	
	
}
