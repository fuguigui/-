
abstract class Player {
	protected boolean isBlack;
	
	public Player(boolean isblack){
		isBlack = isblack;
	}
}

class Human extends Player{

	public Human(boolean isblack) {
		super(isblack);
	}
	
}