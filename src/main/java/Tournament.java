import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public abstract class Tournament {
	public Tournament(Robot[] players, Game game) {
		super();
		this.players = players;
		this.game = game;
		this.history = new ArrayList<History>();
		this.bracket = new ArrayList<Robot[]>();
	}
	
	Robot[] players;
	Game game;
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
	    Robot[] rankings = this.players.clone(); // Clone so we don't mess up the original order
	    
	    Arrays.sort(rankings, new Comparator<Robot>() {
	        @Override
	        public int compare(Robot r1, Robot r2) {
	            // Sort in descending order (highest score first)
	            return Integer.compare(r2.getScore(history), r1.getScore(history));
	        }
	    });
	    
	    return rankings;
	}
	
	public abstract boolean checkEnd();
	
	public abstract void getBracket();
	
	public abstract void updateBracket();
}
