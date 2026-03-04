
public class PrisonerDilemma extends Game {

	public PrisonerDilemma() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int[] getOutcome(String action1, String action2) {
		// TODO Auto-generated method stub
		
		if (action1.equals("COOPERATE") && action2.equals("COOPERATE")) {
            return new int[] {3, 3};
        } else if (action1.equals("COOPERATE") && action2.equals("DEFECT")) {
            return new int[] {0, 5};
        } else if (action1.equals("DEFECT") && action2.equals("COOPERATE")) {
            return new int[] {5, 0};
        } else if (action1.equals("DEFECT") && action2.equals("DEFECT")) {
            return new int[] {1, 1};
        } else {
            return new int[] {0, 0}; // Default case for invalid actions
        }
	}

}
