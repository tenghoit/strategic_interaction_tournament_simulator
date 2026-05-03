package models;

import java.util.ArrayList;

public abstract class Invoker {
	
	ArrayList<Command> commands;
	
	public Invoker() {
		// TODO Auto-generated constructor stub
		commands = new ArrayList<Command> ();
	}
	
	public void add(Command newCmd) {
		this.commands.add(newCmd);
	}
	
	public void executeCommand(Command cmd) {
		cmd.execute();
	}
	
	public void undoCommand(Command cmd) {
		cmd.undo();
	}
	
	

}
