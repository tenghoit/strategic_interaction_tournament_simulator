package robots;

import java.util.ArrayList;

import models.History;

public class Reciprocator extends Robot {

	public Reciprocator(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getAction(String opponentName, ArrayList<History> history) {
		// TODO Auto-generated method stub
		
		History lastMatch = null;
		
		// start from end
		for (int i = history.size() - 1; i >= 0; i--) {
            History curr = history.get(i);
            
            Boolean condition1  = curr.player1().equals(opponentName) && curr.player2().equals(this.name);
            Boolean condition2  = curr.player2().equals(opponentName) && curr.player1().equals(this.name);
            
            if (condition1 || condition2 ) {
                lastMatch = curr;
                break;
            }
            
        }
		
		if (lastMatch == null) {
			return "COOPERATE";
		}
		
		if (lastMatch.player1().equals(opponentName)) {
            return lastMatch.action1(); // Opponent was player 1
        } else {
            return lastMatch.action2(); // Opponent was player 2
        }
		
		
	}

}
