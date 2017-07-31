import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;
public class DrawPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	public static final int CHESS_SIZE = 45;
	private static final int RADIUS=40;
	
	private static int put_times=0;
	private static boolean isPlayer=true;
	
	BufferedImage bi;
//	static boolean isBlack=false;
//	boolean isBlocked=false;//决定棋板是否响应用户请求，如果是电脑正在下棋，则不响应。
//	NewGame game;
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2=(Graphics2D)g;
		g2.setColor(Color.BLACK);
		for(int i=1;i<=Gomoku.Length;i+=1){
			g2.drawLine(NewGame.step,i*NewGame.step,NewGame.step*Gomoku.Length,i*NewGame.step);
			g2.drawLine(i*NewGame.step,NewGame.step,i*NewGame.step,NewGame.step*Gomoku.Length);
		}
		
		g2.setColor(Color.BLACK);
		g2.fillOval(353,353,14,14);
		g2.fillOval(218,218,14,14);
		g2.fillOval(488,218,14,14);
		g2.fillOval(488,488,14,14);
		g2.fillOval(218,488,14,14);
		for (Step step : State.steps) {
			g2.setColor(step.color);
			g2.fillOval((step.x+1)*CHESS_SIZE - CHESS_SIZE / 2, (step.y+1)* CHESS_SIZE - CHESS_SIZE / 2, RADIUS, RADIUS);
		}
		
		
		System.out.println("In DrawPanl.paint Display State.rec");
	//	for()
		for(int i=0;i<Gomoku.Length;++i){
			for(int j=0;j<Gomoku.Length;++j)
				System.out.print(State.rec[i][j]+" ");
			System.out.println("the "+i+"line");
		}
	}
	
	public void putChess(int x,int y, boolean isBlack){
		State.addPoint(new Step(x , y , isBlack ? Color.BLACK : Color.WHITE));
		//更改availble的值
		State.putInAvailables(x, y,State.availables,State.rec);
	//	isBlocked=!isBlocked;
		put_times++;
		
		System.out.println("In DrawPanel.putChess x:"+x+" y:"+y+" color isBlack?"+isBlack+" put times:"+put_times);

		isBlack=!isBlack;
		int result=Gomoku.JudgeResult(State.rec,State.steps,State.availables);
		if(result==1 || result==-1){
	//		JOptionPane.showConfirmDialog(this, "The game is over! Another one?")
			System.out.println("The game has ended!");
			init();
		}
	}
	
	int round(int x){
		return ((x%NewGame.step)<(NewGame.step/2))?x/NewGame.step:(x/NewGame.step+1);
	}
	public void init(){
		State.empty();
		State.initState();
		
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
	//		if(isBlocked) {

					//Ai2 computer=new Ai2();
				//	Pos p=computer.maxmin(roles, 4, 0);
			//		AI.updateScore(roles, 8, 7);
				//	AI.putChess(roles, p.x, p.y, 1, true);
			//		putChess(p.x, p.y, isBlack);
				
	//			Evaluate evaluate=new Evaluate();
		//		System.out.println("(9,8)位置处黑子的得分是："+evaluate.(roles, 9, 8, 1));
				
				
		//		}
			//	else{
			//	if(isPlayer){
					int x=round(e.getX())-1,y=round(e.getY())-1;
				
					System.out.println("In DrawPanel player: x: "+x+"and y:"+y+"Isblack? ");
					
					if(x>=0 && x<Gomoku.Length 
							&& y>=0 && y<Gomoku.Length
							&& State.rec[x][y] == 0){
						System.out.println("In DrawPanel.MouseListener The real player is putting chesses.");
						putChess(x,y,false);
						repaint();
						isPlayer=!isPlayer;
					}
					else System.out.println("The wrong position: has been occupied.");
				
				
			int action=((MTCT1)NewGame.ply[0]).getAction();
			System.out.println("In DrawPanel.MouseListener The computer is putting chess, the action is "
					+action/Gomoku.Length+" y: "+action%Gomoku.Length);
			putChess(action/Gomoku.Length, action%Gomoku.Length, true);
			//	isBlack=!isBlack;
		//		isBlocked=!isBlocked;
			
			}
		});
	}

	
}
