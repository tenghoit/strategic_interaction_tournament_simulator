import java.util.ArrayList;

public class Cooperator extends Robot {

	public Cooperator(String name) {
		super(name);
	}

	@Override
	public String getAction(String opponentName, ArrayList<History> history) {
		// TODO Auto-generated method stub
		return "COOPERATE";
	}

}
