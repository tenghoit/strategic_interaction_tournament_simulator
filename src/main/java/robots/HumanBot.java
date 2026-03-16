package robots;

import java.util.ArrayList;
import java.util.Scanner;

import models.History;

public class HumanBot extends Robot {

	public HumanBot(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getAction(String opponentName, ArrayList<History> history) {
		// TODO Auto-generated method stub
		ArrayList<History> interactions = new ArrayList<History>();
		
		for(History match: history) {
            Boolean condition1  = match.player1().equals(opponentName) && match.player2().equals(this.name);
            Boolean condition2  = match.player1().equals(this.name) && match.player2().equals(opponentName);
            
            if (condition1 || condition2 ) {
                interactions.add(match);
            }
		}
		
		Scanner reader = new Scanner(System.in);
	    System.out.println("Match Against " + opponentName);
	    System.out.println("Previous Interactions: ");
	    for(History match: interactions) {
	    	System.out.println(match);
	    }
	    System.out.println("Enter action: ");
	    String action = reader.nextLine();
		
		return action;
		
	}

}
