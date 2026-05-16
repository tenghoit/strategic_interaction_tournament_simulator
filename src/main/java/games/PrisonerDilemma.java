package games;

import java.util.Arrays;

public class PrisonerDilemma extends Game {

	public PrisonerDilemma() {
		// Constructor
	}

	@Override
	public int[] getOutcome(String action1, String action2) {
		// Bundle the actions into an array for direct comparison
		String[] actions = {action1, action2};
		
		if (Arrays.equals(actions, new String[]{"COOPERATE", "COOPERATE"})) {
			return new int[] {3, 3};
		} else if (Arrays.equals(actions, new String[]{"COOPERATE", "DEFECT"})) {
			return new int[] {0, 5};
		} else if (Arrays.equals(actions, new String[]{"DEFECT", "COOPERATE"})) {
			return new int[] {5, 0};
		} else if (Arrays.equals(actions, new String[]{"DEFECT", "DEFECT"})) {
			return new int[] {1, 1};
		} else {
			return new int[] {0, 0}; // Default case for invalid actions
		}
	}
}