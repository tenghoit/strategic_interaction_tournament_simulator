package tournaments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import games.Game;
import loggers.Listener;
import models.History;
import robots.Robot;

public abstract class Tournament {
	
	String name;
	Game game;
	ArrayList<Robot> players;
	ArrayList<History> history;
	ArrayList<Robot[]> bracket;
	ArrayList<Listener> listeners;
	int delay;
	
	public Tournament(String name, Game game) {
		super();
		this.name = name;
		this.game = game;
		this.players = new ArrayList<Robot>();
		this.history = new ArrayList<History>();
		this.bracket = new ArrayList<Robot[]>();
		this.listeners = new ArrayList<Listener>();
		this.delay = 0;
	}
	
	public Robot[] run() {
		this.delay = 100;
		this.getBracket();
				
		while (this.checkEnd() == false) {
			Robot[] currentMatchup = this.bracket.get(0);
			Robot player1 = currentMatchup[0];
			Robot player2 = currentMatchup[1];
			
			History match  = this.game.play(player1, player2, this.getHistory());
			this.history.add(match);
			this.notify(match);
			this.updateBracket();
			
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return this.getRankings();
	}
	
	public ArrayList<Robot[]> getCurrentBracket(){
		return this.bracket;
	}
	
	public Robot[] getRankings() {
	    Robot[] rankings = this.players.toArray(new Robot[this.players.size()]);
	    HashMap<String, Integer> scores = new HashMap<>();
	    
	    for(Robot bot : this.players) {
	    	scores.put(bot.getName(), bot.getScore(this.getHistory())); // filling scores
	    }
	    
	    Arrays.sort(rankings, new Comparator<Robot>() {
	        @Override
	        public int compare(Robot r1, Robot r2) {
	            // Sort in descending order (highest score first
	            return Integer.compare(scores.get(r2.getName()), scores.get(r1.getName()));
	        }
	    });
	    
	    return rankings;
	}
	
	public String getName() {
		return name;
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
	
	public ArrayList<Listener> getListeners() {
		return listeners;
	}

	public abstract void updateBracket();
	
	public abstract Boolean isOpen();

	public ArrayList<History> getHistory() {
		return history;
	}
	
	public void addListener(Listener l) {
		if(!this.listeners.contains(l)) {
			this.listeners.add(l);
		}
	}
	
	public void removeListener(Listener l) {
		if(this.listeners.contains(l)) {
			this.listeners.remove(l);
		}
	}
	
	public void notify(History match) {
		for(Listener l: this.listeners) {
			l.update(match);
		}
	}
}
