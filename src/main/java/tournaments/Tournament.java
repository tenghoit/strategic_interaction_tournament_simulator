import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import games.Game;
import models.History;
import robots.Robot;

public abstract class Tournament {
	public Tournament(Game game) {
		super();
		this.game = game;
		this.players = new ArrayList<Robot>();
		this.history = new ArrayList<History>();
		this.bracket = new ArrayList<Robot[]>();
	}
	
	Game game;
	ArrayList<Robot> players;
	ArrayList<History> history;
	ArrayList<Robot[]> bracket;
	
	public Robot[] run() {
		
		this.getBracket();
				
		while (this.checkEnd() == false) {
			Robot[] currentMatchup = this.bracket.get(0);
			Robot player1 = currentMatchup[0];
			Robot player2 = currentMatchup[1];
			
			History match  = this.game.play(player1, player2, this.history);
			this.history.add(match);
			this.updateBracket();
		}
		
		return this.getRankings();
	}
	
	public Robot[] getRankings() {
	    Robot[] rankings = (Robot[]) this.players.clone(); // Clone so we don't mess up the original order
	    
	    Arrays.sort(rankings, new Comparator<Robot>() {
	        @Override
	        public int compare(Robot r1, Robot r2) {
	            // Sort in descending order (highest score first)
	            return Integer.compare(r2.getScore(history), r1.getScore(history));
	        }
	    });
	    
	    return rankings;
	}
	
	public Boolean addPlayer(Robot player) {
		if(!this.isOpen()) {
			return false;
		}
		
		this.players.add(player);
		return true;
	}
	
	public abstract boolean checkEnd();
	
	public abstract void getBracket();
	
	public abstract void updateBracket();
	
	public abstract Boolean isOpen();
	
}
