package robots;
import java.util.ArrayList;

import models.History;

public abstract class Robot {
	String name;
	
	public Robot(String name) {
		super();
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getScore(ArrayList<History> history) {
		int total = 0;
		
		for (History match : history) {
			if(match.player1() == this.name) {
				total += match.result1();
			}else if(match.player2() == this.name){
				total += match.result2();
			}else {
				continue;
			}
		}
		
		return total;
	}
	
	public abstract String getAction(String opponentName, ArrayList<History> history);
}


