package models;

import javafx.collections.ObservableList;

public class PlaybackCommand implements Command {
	
	History match;
	ObservableList<History> events;

	public PlaybackCommand(History newMatch, ObservableList<History> newEvents) {
		// TODO Auto-generated constructor stub
		this.match = newMatch;
		this.events = newEvents;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		this.events.add(this.match);
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		this.events.remove(match);
	}

}
