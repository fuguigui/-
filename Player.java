
abstract class Player {
	protected boolean isBlack;
	
	public Player(boolean isblack){
		isBlack = isblack;
	}
	
	public int nextPoint(State curr){
		
		return 0;
	}

}

class Human extends Player{

	public Human(boolean isblack) {
		super(isblack);
	}
	
}