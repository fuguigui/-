import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;

import javax.swing.*;


enum Mode{
	PVSC,CVSC;
}

public class NewGame{//����Ϸ�Ķ��壺�����˳�ʼ״̬�����֣��Ϳ��Գ�֮Ϊ����Ϸ����һ������ӿ����̿�ʼ

	//���ģ�
	public Mode mode;//train:��̨��������ж�ս
	public State initState, currentState;//��ʼ״̬�͵�ǰ״̬
	public Player[] ply=new Player[2];//���������ߣ�player[0]Ϊ���ӣ�player[1]Ϊ����
	protected int initPlayer, currentPlayer;//���ַ��͵�ǰ������Player��˳���Ӧ
	public static boolean[][] points=new boolean[Gomoku.Length][Gomoku.Length];//true:�����ǿյģ�false�����ǿյ�
	public static Point[][] scores=new Point[Gomoku.Length][Gomoku.Length];//ÿ��λ�õ���Ϣ��һ�����������о��飬��һ�����������ѵ��
	public static DrawPanel panel;//���̻��ƹ���
	private static JFrame f=new JFrame("Gomoku game");
	public static int step=45;
	
	public NewGame(){
		//��ʼ����Ĭ��Ϊ�˻���սģʽ
		mode=mode.PVSC;
		initPlayer=0;
		currentPlayer=0;
	//	initState.empty();//�������
	//	this.setSize(720,700);
		for(int i=0;i<Gomoku.Length;i++)
			for(int j=0;j<Gomoku.Length;j++){
				points[i][j]=true;
				scores[i][j]=new Point();
			}
		panel=new DrawPanel();
		
		panel.init();
		
		
		
	}
	
	//��ʼ����Ϸ���������̣�Ȼ�������������
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
		
		
		// �����ϵ������λ�����㣬ͼ�е�СԲȦ
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



