package loggers;

import java.io.FileWriter;
import java.io.IOException;

import models.History;

public class MoveLogger implements Listener {
	public MoveLogger(String fileName) {
		super();
		this.fileName = fileName;
	}
	String fileName;
	@Override
	public void update(History history) {
		// TODO Auto-generated method stub
	    try {
	        FileWriter myWriter = new FileWriter(this.fileName, true);
	        myWriter.write(history.player1() + " " + history.action1());
	        myWriter.write(history.player2() + " " + history.action2());
	        myWriter.close();  // must close manually
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}
