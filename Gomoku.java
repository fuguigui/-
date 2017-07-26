import java.util.*;

public class Gomoku {
	static public int Length=14;//棋盘大小
	//知识库集合
	public int JudgeTerminal() {
		return 0;
	}//结局判断函数：-1，先手输；0：平局；1：先手赢
	public float JudgeState() {
		return 0;
	}//站在先手的角度，越有利于先手，得分越高，根据结点的状态的不同分数，给结点进行打分
	
}
