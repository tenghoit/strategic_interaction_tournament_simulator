package models;

public class PlaybackInvoker extends Invoker {
	
	int index;
	Boolean pause;

	public PlaybackInvoker() {
		// TODO Auto-generated constructor stub
		pause = false;
		index = -1;
	}
	
	public void add(Command newCmd) {
		boolean wasAtHead = (index == getLastIndex());
        
        this.commands.add(newCmd);
        
        if ((!pause && wasAtHead) || index == -1) {
            forward();
        }
	
	}
	
	public int getLastIndex() {
		return commands.size() - 1;
	}
	
	
	public void forward() {
	    if (index < getLastIndex()) {
	        index++;
	        executeCommand(commands.get(index));
	    }
	}
	
	public void previous() {
		if(index >= 0) {
			undoCommand(commands.get(index));
			index--;
		}
		

	}
	
	public void fastForward() {
		while(index < getLastIndex()) {
			forward();
		}
	}
	
	public void togglePlayback() {
		if(pause) {
			pause = false;
			fastForward();
		}else if(!pause) {
			pause = true;
		}
	}

}
