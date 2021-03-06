import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;


//enum Mode{
//	PVSC,CVSC;
//}

public class NewGame{//新游戏的定义：给定了初始状态和先手，就可以称之为新游戏，不一定必须从空棋盘开始
//The overall structure of a new game, including: 
	//two players,  a panel, the record of the current player, an evaluation function and a list of steps from beginning to now.
	
	//核心:
//	public Mode mode;//train:两台计算机进行对战
	public State currentState;//初始状态和当前状态
	public static Player[] ply=new Player[2];//两个参与者，player[0]为黑子，player[1]为白子
	//*******here can be upgraded: e.g. the first player is randomly selected or chosen by the user.
	
	protected int initPlayer, currentPlayer;//开局方和当前方。和Player的顺序对应
	//******is initPlayer necessary?
	
//	public static int[][] currentState.rec=new int[Gomoku.Length][Gomoku.Length];//0:代表是空的，1:代表是黑子，-1代表是白子
	public static DrawPanel panel;//棋盘绘制工具
	private static JFrame f=new JFrame("Gomoku game");
	public static int step=45;
	//*****what is it??
	
	
	//public Evaluate eva;//an evaluation function of the score of each location under the current cass
	//this function may work based on the former result? is it more efficient??
	
	
	static ArrayList<Pos> steps=new ArrayList<Pos>();

	
	public NewGame(){
		//初始化，默认为人机对战模式
		//mode=mode.PVSC;
		initPlayer=0;
		//******is it neccessary?
		
		
		currentPlayer=0;
	//	eva = new Evaluate();
		currentState=new State();
		currentState.empty();
//		currentState.initState();
		for(int i=0;i<Gomoku.Length;i++)
			for(int j=0;j<Gomoku.Length;j++){
				currentState.rec[i][j]=0;
			}
		panel=new DrawPanel();	
		
		
		
		//*******here, make changes: add three modes: randomly, i choose to be the black and i choose to be the white,
		//the below may be transfered into the initGame part.
		ply[0]=new MTCT1(true);
		ply[1]=new Human(false);
	}
	
	//初始化游戏，画出棋盘，然后监听更新棋盘
	public void initGame(){
		
		panel.setBackground(Color.GRAY);
		panel.setPreferredSize(new Dimension(NewGame.step*(Gomoku.Length+2),NewGame.step*(Gomoku.Length+2)));
		currentState.empty();
		currentState.initState();
		panel.init();
		
		f.add(panel);
		f.pack();
		f.setLocation(400, 0);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		//*******add a pop-out window,to ask the role the player wants to be, and then update the ply[]
		
	}
	public static void main(String[] args) {
		NewGame game=new NewGame();
		game.initGame();
	}
}




