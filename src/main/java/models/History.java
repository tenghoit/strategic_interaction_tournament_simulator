package models;

public record History(String player1, String player2, String action1, String action2, int result1, int result2) {
	@Override
	public String toString() {
		return String.format("%s (%s) %s - %s (%s) %s", 
				player1, action1, result1, result2, action2, player2);
	}
}
