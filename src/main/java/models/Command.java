package models;

public interface Command {
	void execute();
	void undo();
}
