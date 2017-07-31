import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Ai2 {
	//运用算杀的方法：算杀的原理和极大极小值搜索是一样的
	/*
	 * 不过，算杀只考虑冲死活三这类对方必须防守的棋，因此，算杀的复杂度虽然是M^N
	 * 但是底数M特别小，可以算到16步
	 */
	/*
	 * 基本思路：
	 * 电脑有活三或者冲四，认为是玩家必须防守的
	 * 玩家防守的时候却不一定根据电脑的棋来走，而是选择走自己最好的棋，比如，自己冲四
	 */
	Evaluate eva=new Evaluate();
	public static int[][][] scores=new int[2][Gomoku.Length][Gomoku.Length];//每个位置的信息；一方面来自已有经验，另一方面来自随机训练
	ArrayList<Pos> steps=new ArrayList<Pos>();
	final int cacheDepth=5;
	final int MAX=S.FIVE*10;
	final float deepDecrease=(float) 0.8;
	AI ai1;
	ArrayList<Pos> bestpoints=new ArrayList<Pos>();
	final float threshod= (float) 1.1;

	int NodeNumber=0, StepNumber=0,count, ABcut, cacheNumber=0,cacheHit=0,checkmatedepth;//count"每次思考的节点数
	Set<Pos> cache=new HashSet<Pos>();
	
	public Pos maxmin(int[][] roles,int deep, int checkdeep){
		int best=-10*S.FIVE;
		int checkmatedeep = (checkdeep>0)?checkdeep:cacheDepth;
		ArrayList<Pos> points=ai1.heuristic(roles);
		
		for(int i=0;i<points.size();++i){
			Pos pos = points.get(i);
			ai1.putChess(roles, pos.x, pos.y,1 , true);
			int[] scores={deep-1,-MAX, -best, -1};
			int v = deep-1;
			v=(v>-MAX)?v:-MAX;
			v=(v>-best)?v:-best;
			v=(v>-1)?v:-1;
			
			//对于边缘的棋子，分数要打折
			if(pos.x<3||pos.x>11||pos.y<3||pos.y>11)v*=0.5;
			//如果跟之前的一样好，则把当前位子加入待选位子
			if(v==best)bestpoints.add(pos);
			//如果得到一个更好的分数，就把以前存的位子全部清除
			if(v>=best*threshod){
				best=v;
				bestpoints.removeAll(bestpoints);
				bestpoints.add(pos);
			}
			ai1.removeChess(roles, pos.x, pos.y);
		}
		
		Pos result=bestpoints.get((int)Math.floor(bestpoints.size()*Math.random()));
		scores[0][result.x][result.y]=best;
		return result;
		
	}

	ArrayList<Pos> checkmateFast(int[][]roles, int role, int score){
		return new ArrayList<Pos>();
	}
	
	
	public double maxlayer(int[][]roles, int deep, int alpha, int beta, int role){
		int v=ai1.evaluateState(roles, role);
		count++;
		if(deep<=0 || v*threshod>=S.FIVE)return v;
		int best=-MAX;
		ArrayList<Pos> points=ai1.heuristic(roles);
		
		for(int i = 0;i<points.size();i++){
			Pos p = points.get(i);
			ai1.putChess(roles, p.x, p.y, role, true);
			
			float v1 = deep-1;
			v1=(v1>-beta)?v1:-beta;
			int tmp=(best>alpha)?best:alpha;
			v1=(v1>-tmp)?v1:-tmp;
			v1=(v1>-1)?v1:-1;
			v1=-v1*deepDecrease;
			ai1.removeChess(roles, p.x, p.y);
			
			if(v>=best*threshod) best=v;
			if(v*threshod>=beta){
				//AB剪枝
				ABcut++;
				return v1;
			}
		}
		if((deep==2 || deep == 3) && best*threshod<=2*S.THREE
				&&best>=(S.THREE-1)*threshod){
			ArrayList<Pos> mate=checkmateFast(roles, role, checkmatedepth);
			if(mate.size()>0){
				Pos p=mate.get(0);
				int index=1;
				if(role == 1) index=0;
				double score=scores[index][p.x][p.y]*Math.pow(0.8,mate.size())*role;
				return score;
			}
		}
		return best;
	}
/*
	public int deeping(int[][]roles, int role, int depth){
		for(int i = 1;i<depth;++i){
			int result=i;
			result=Math.max(result, role);
		//	result=Math.max(result)
		}
	}
*/
}


