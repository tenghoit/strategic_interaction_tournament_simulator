package games;
import java.util.ArrayList;

import loggers.MoveListener;
import loggers.ScoreListener;
import models.History;
import robots.Robot;

public abstract class Game {
	ArrayList<MoveListener> moveListeners;
	ArrayList<ScoreListener> scoreListeners;
	
	public History play(Robot player1, Robot player2, ArrayList<History> history) {
		String action1 = player1.getAction(player2.getName(), history);
		String action2 = player2.getAction(player1.getName(), history);
		
		int[] outcomes = this.getOutcome(action1, action2);
		
		History match = new History(player1.getName(), player2.getName(), action1, action2, outcomes[0], outcomes[1]);
		
		return match;
	}
	
	public Game() {
		super();
		this.moveListeners = new ArrayList<MoveListener>();
		this.scoreListeners = new ArrayList<ScoreListener>();
	}

	public abstract int[] getOutcome(String action1, String action2);
	
	public void addMoveListener(MoveListener listener) {
		this.moveListeners.add(listener);
	}
	
	public void addScoreListener(ScoreListener listener) {
		this.scoreListeners.add(listener);
	}
	
	public void notifyMove(String name, String action) {
		for(MoveListener listener: this.moveListeners) {
			listener.moveMade(name, action);
		}
	}
	public void notifyScore(History match) {
		for(ScoreListener listener: this.scoreListeners) {
			listener.matchComplete(match);
		}
	}
	
	
}
