import java.awt.Color;
import java.util.ArrayList;

public class AI {
	static Evaluate eva=new Evaluate();
	public static int[][][] scores=new int[2][Gomoku.Length][Gomoku.Length];//每个位置的信息；一方面来自已有经验，另一方面来自随机训练
	

	final static int countLimit = 8;//函数返回的结点数量上限，超过之后会按照分数进行截断
	//初始化分数，对每个位置，分黑白两种角色进行打分
	public void initScore(int[][] roles){
		int scole=2;
		int cnt=2;
		for(int i=0;i<Gomoku.Length;++i)
			for(int j = 0;j<Gomoku.Length;++j){
				if(roles[i][j] == 0)
					if(eva.hasNeighbor(roles,i,j,scole,cnt)){
						int ws=eva.pointscore(roles, i, j, -1);
						int bs=eva.pointscore(roles, i, j, 1);
						scores[0][i][j]=bs;
						scores[1][i][j]=ws;
					}
			}
	}

	//更新一个点附近的分数
	public static void updateScore(int[][]roles,int x,int y) {
		int bs=eva.pointscore(roles, x, y, 1);
		int ws=eva.pointscore(roles, x, y, -1);
		scores[0][x][y]=bs;
		scores[1][x][7]=ws;
		
		System.out.println("白色棋子的分数是"+scores[1][x][y]+"；黑色棋子的分数是："+scores[0][x][y]);
	}
	
	public static void updateAllDirec(int[][]roles,int x,int y){
		int rad=8;//这个半径是什么意思？
		//更新行
		int low = Math.max(0, y-rad);
		int high=Math.min(Gomoku.Length, y+rad);
		for(int i = low;i<high;++i){
			if(roles[x][i]==0)
				updateScore(roles,x, i);
		}
		//更新列
		low=Math.max(0, x-rad);
		high=Math.min(Gomoku.Length, x+rad);
		for(int i = low;i<high;++i){
			if(roles[i][y]==0)
				updateScore(roles, i, y);
		}
		//更新右下对角线
		for(int i = -rad;i<rad;++i){
			int _x=x+i;
			int _y=y+i;
			if(_x<0||_y<0)continue;
			if(_x>=Gomoku.Length||_y>=Gomoku.Length)break;
			if(roles[_x][_y]==0)
				updateScore(roles,_x, _y);
		}
		//更新左下对角线
		for(int i = -rad;i<rad;++i){
			int _x=x+i;
			int _y=y-i;
			if(_x<0||_y<0)continue;
			if(_x>=Gomoku.Length||_y>=Gomoku.Length)break;
			if(roles[_x][_y]==0)
				updateScore(roles,_x, _y);
		}
	}
	
	public static void putChess(int[][]roles, int x, int y,int role,boolean record){
		roles[x][y]=role;
		updateAllDirec(roles, x, y);
		if(record)
			NewGame.steps.add(new NewGame.Step(x,y,role==1?Color.black:Color.white));
		
		
		for(int i = 0;i<Gomoku.Length;++i){
			for(int j=0;j<Gomoku.Length;++j)
			{
				System.out.print(roles[i][j]);
			}
			System.out.println("----the "+i+"line");
			}
		
	}
	public void removeChess(int[][]roles,int x,int y){
		roles[x][y]=0;
		updateAllDirec(roles, x, y);
	//	NewGame.steps.remove(new Pos(x, y));
	}

	public static int evaluateState(int[][]roles, int role){
		int bMax=-S.FIVE;
		int wMax=-S.FIVE;
		
		//遍历得出位置的最高分
		for(int i=0;i<Gomoku.Length;++i)
			for(int j=0;j<Gomoku.Length;++j){
				if(roles[i][j]==0){
					bMax=Math.max(bMax,scores[0][i][j]);
					wMax=Math.max(wMax, scores[1][i][j]);
				}
			}
		return ((role==1)?1:-1)*(bMax-wMax);
	}
	
	public static ArrayList<Pos> heuristic(int[][]roles){
		//启发函数
		ArrayList<Pos> fives=new ArrayList<Pos>();
		ArrayList<Pos> fours=new ArrayList<Pos>();
		ArrayList<Pos> b_fours=new ArrayList<Pos>();
		ArrayList<Pos> double_three=new ArrayList<Pos>();
		ArrayList<Pos> threes=new ArrayList<Pos>();
		ArrayList<Pos> twos=new ArrayList<Pos>();
		ArrayList<Pos> neighbours=new ArrayList<Pos>();
		
		for(int i=0;i<Gomoku.Length;++i)
			for(int j=0;j<Gomoku.Length;++j){
				if(roles[i][j] !=0) continue;
				if(eva.hasNeighbor(roles, i, j, 2, 2)){
					int bscore=scores[0][i][j];
					int wscore=scores[1][i][j];
					
					if(bscore>=S.FIVE){
						ArrayList<Pos> result=new ArrayList<Pos>();
						result.add(new Pos(i, j));
						return result;
					}
					if(wscore>=S.FIVE){
						fives.add(new Pos(i,j));
					}
					else if(bscore>=S.FOUR) fours.add(0, new Pos(i, j));
					else if(wscore>=S.FOUR) fours.add(new Pos(i, j));
					else if(bscore>=S.B_FOUR) b_fours.add(0, new Pos(i, j));
					else if(wscore>=S.B_FOUR) b_fours.add(new Pos(i,j));
					else if(bscore>=2*S.THREE) threes.add(0, new Pos(i, j));
					else if(wscore>=2*S.THREE) threes.add(new Pos(i,j));
					else if(bscore>=S.THREE) double_three.add(0, new Pos(i, j));
					else if(wscore>=S.THREE) double_three.add(new Pos(i,j));
					else if(bscore>=S.TWO) twos.add(0, new Pos(i, j));
					else if(wscore>=S.TWO) twos.add(new Pos(i,j));
					else neighbours.add(new Pos(i, j));
				}
			}
		//如果成五，是必杀棋，直接返回
		if(fives.size()>0){
			ArrayList<Pos> res=new ArrayList<Pos>();
			res.add(fives.get(0));
			return res;
		}
		//如果一个活三可以有多个位置形成活四，不能只考虑其中一个，要从多个中取优
		if(fours.size()>0)return fours;
		if(b_fours.size()>0){
			ArrayList<Pos> res=new ArrayList<Pos>();
			res.add(b_fours.get(0));
			return res;
		}
		if(double_three.size()>0) {
			double_three.addAll(threes);
			return double_three;
		}
		
		ArrayList<Pos> res=new ArrayList<Pos>();
		res.addAll(threes);
		res.addAll(twos);
		res.addAll(neighbours);
		
		if(res.size()>countLimit)
			for(int j =res.size()-1;j>=countLimit;--j)
				res.remove(j);
		return res;
	}
}
class Pos{
	int x,y;
	public Pos(int _x,int _y){
		x=_x;
		y=_y;
	}
}
