import org.omg.CORBA.PUBLIC_MEMBER;

public class State {

	public int[][] rec= new int[Gomoku.Length][Gomoku.Length];//记录当前棋盘状态,0无子，1黑子，-1白子
	public void empty(){
		rec=new int[Gomoku.Length][Gomoku.Length];
	}
	public void addPoint(int num, boolean isblack){//向特定位置增加一个棋子
	
		
	}
}
