package games;
import java.util.ArrayList;
import java.util.HashMap;

import loggers.Listener;
import models.History;
import robots.Robot;

public abstract class Game {
	HashMap<String, ArrayList<Listener>> listeners;
	
	public History play(Robot player1, Robot player2, ArrayList<History> history) {
		String name1 = player1.getName();
		String name2 = player2.getName();
		String action1 = player1.getAction(name2, history);
		String action2 = player2.getAction(name1, history);
		
		int[] outcomes = this.getOutcome(action1, action2);
		
		History match = new History(name1, name2, action1, action2, outcomes[0], outcomes[1]);
		
		this.notify("moves", match);
		this.notify("scores", match);
		
		return match;
	}
	
	public Game() {
		super();
		this.listeners = new HashMap<>();
	}

	public abstract int[] getOutcome(String action1, String action2);
	
	public Boolean addListener(String event, Listener listener) {
		if(!this.listeners.containsKey(event)) {
			this.listeners.put(event, new ArrayList<Listener>());
		}
		this.listeners.get(event).add(listener);
		return true;
	}
	
	public Boolean removeListener(String event, Listener listener) {
		if(!this.listeners.containsKey(event)) {
			return false;
		}
		
		ArrayList<Listener> list = this.listeners.get(event);
		
		if(!list.contains(listener)) {
			return false;
		}
		
		list.remove(listener);
		return true;
	}
	
	public Boolean notify(String event, History history) {
		if(!this.listeners.containsKey(event)) {
			return false;
		}
		
		for(Listener listener : this.listeners.get(event)) {
			listener.update(history);
		}
		return true;
	}
	
	public ArrayList<Listener> getListeners(String event){
		return this.listeners.get(event);
	}
	
	
}
