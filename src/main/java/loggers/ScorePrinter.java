package loggers;
import History;

public class ScorePrinter implements ScoreListener {

	public ScorePrinter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void matchComplete(History match) {
		// TODO Auto-generated method stub
		System.out.print(match.player1() + " " + match.result1() + " - " + match.result2() + " " + match.player2());
	}

}
