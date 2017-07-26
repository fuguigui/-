import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;

import javax.swing.*;


enum Mode{
	PVSC,CVSC;
}

public class NewGame{//新游戏的定义：给定了初始状态和先手，就可以称之为新游戏，不一定必须从空棋盘开始

	//核心：
	public Mode mode;//train:两台计算机进行对战
	public State initState, currentState;//初始状态和当前状态
	public Player[] ply=new Player[2];//两个参与者，player[0]为黑子，player[1]为白子
	protected int initPlayer, currentPlayer;//开局方和当前方。和Player的顺序对应
	public static boolean[][] points=new boolean[Gomoku.Length][Gomoku.Length];//true:代表是空的，false代表不是空的
	public static Point[][] scores=new Point[Gomoku.Length][Gomoku.Length];//每个位置的信息；一方面来自已有经验，另一方面来自随机训练
	public static DrawPanel panel;//棋盘绘制工具
	private static JFrame f=new JFrame("Gomoku game");
	public static int step=45;
	
	public NewGame(){
		//初始化，默认为人机对战模式
		mode=mode.PVSC;
		initPlayer=0;
		currentPlayer=0;
	//	initState.empty();//清空棋盘
	//	this.setSize(720,700);
		for(int i=0;i<Gomoku.Length;i++)
			for(int j=0;j<Gomoku.Length;j++){
				points[i][j]=true;
				scores[i][j]=new Point();
			}
		panel=new DrawPanel();
		
		panel.init();
		
		
		
	}
	
	//初始化游戏，画出棋盘，然后监听更新棋盘
	public void initGame(){
		
		panel.setBackground(Color.GRAY);
		panel.setPreferredSize(new Dimension(NewGame.step*(Gomoku.Length+2),NewGame.step*(Gomoku.Length+2)));
		panel.init();
		f.add(panel);
		f.pack();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	public static void main(String[] args) {
		NewGame game=new NewGame();
		game.initGame();
	}
	

	
}

class DrawPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	BufferedImage bi;
	boolean isBlack=true;
	Graphics2D g2;
	JFrame frame;
	public void paint(Graphics g){
		super.paint(g);
		g2=(Graphics2D)g;
		g2.setColor(Color.BLACK);
		for(int i=1;i<=Gomoku.Length;i+=1){
			g2.drawLine(NewGame.step,i*NewGame.step,NewGame.step*Gomoku.Length,i*NewGame.step);
			g2.drawLine(i*NewGame.step,NewGame.step,i*NewGame.step,NewGame.step*Gomoku.Length);
		}
		
		
		// 棋盘上的五个定位基本点，图中的小圆圈
		g2.setColor(Color.BLACK);
		g2.fillOval(353,353,14,14);
		g2.fillOval(218,218,14,14);
		g2.fillOval(488,218,14,14);
		g2.fillOval(488,488,14,14);
		g2.fillOval(218,488,14,14);
	}
	
	void putChess(int x,int y, boolean isBlack){
		if(isBlack)
			g2.setColor(Color.BLACK);
		else 
			g2.setColor(Color.WHITE);
		g2.fillOval(x-NewGame.step/2,y-NewGame.step/2,NewGame.step,NewGame.step);
		
		
		System.out.println("g2 has painted");
	}
	
	int round(int x){
		return ((x%NewGame.step)<(NewGame.step/2))?x/NewGame.step:(x/NewGame.step+1);
	}
	public void init(){
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				int x=round(e.getX()),y=round(e.getY());
				
				System.out.println(x+"and y:"+y);
				
				if(x>0 && x<=Gomoku.Length 
						&& y>0 && y<=Gomoku.Length
						&& NewGame.points[y][x]){
					putChess(x*NewGame.step,y*NewGame.step,isBlack);
				}
				repaint();
			}
		});
	}

	
}

class Point {
	int total,win;
	
	public Point(){
		total=0;
		win=0;
	}
}



