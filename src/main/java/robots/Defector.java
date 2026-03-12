package robots;
import java.util.ArrayList;

import models.History;

public class Defector extends Robot {

	public Defector(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getAction(String opponentName, ArrayList<History> history) {
		// TODO Auto-generated method stub
		return "DEFECT";
	}

}
