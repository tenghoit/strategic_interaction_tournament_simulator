package loggers;

import java.io.FileWriter;
import java.io.IOException;

import models.History;

public class ScoreLogger implements Listener {

	public ScoreLogger(String fileName) {
		// TODO Auto-generated constructor stub
		this.fileName = fileName;
	}
	String fileName;
	@Override
	public void update(History history) {
		// TODO Auto-generated method stub
	    try {
	        FileWriter myWriter = new FileWriter(this.fileName, true);
	        myWriter.write(history.player1() + " " + history.result1() + "\n");
	        myWriter.write(history.player2() + " " + history.result2() + "\n");
	        myWriter.close();  // must close manually
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}
