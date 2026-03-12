package tournaments;
import robots.Robot;

public class RoundRobin extends Tournament {

	public RoundRobin(Robot[] players, Game game) {
		super(players, game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean checkEnd() {
		// TODO Auto-generated method stub
		if(this.bracket.size() == 0) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public void getBracket() {
		// TODO Auto-generated method stub
		for (int i = 0; i < this.players.length; i++) {
            for (int j = i + 1; j < this.players.length; j++) {
                this.bracket.add(new Robot[]{this.players[i], this.players[j]});
            }
        }
	}

	@Override
	public void updateBracket() {
		// TODO Auto-generated method stub
		this.bracket.remove(0);
	}

}
