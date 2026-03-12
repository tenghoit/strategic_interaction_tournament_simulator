package loggers;

public class MovePrinter implements MoveListener {

	public MovePrinter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void moveMade(String name, String action) {
		// TODO Auto-generated method stub
		System.out.print(name + " " + action);
	}

}
