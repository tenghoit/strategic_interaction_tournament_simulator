package tournaments;
import games.Game;
import robots.Robot;

public class RoundRobin extends Tournament {

	public RoundRobin(String name, Game game) {
		super(name, game);
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
		for (int i = 0; i < this.players.size(); i++) {
            for (int j = i + 1; j < this.players.size(); j++) {
                this.bracket.add(new Robot[]{this.players.get(i), this.players.get(j)});
            }
        }
	}

	@Override
	public void updateBracket() {
		// TODO Auto-generated method stub
		this.bracket.remove(0);
	}

	@Override
	public Boolean isOpen() {
		// TODO Auto-generated method stub
		return (this.players.size() < 4);
	}

}
