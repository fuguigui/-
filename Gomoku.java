import java.awt.Color;
import java.util.*;

public class Gomoku {
	static public int Length=15;
	public int JudgeTerminal() {
		return 0;
	}
	public float JudgeState() {
		return 0;
	}	
	public static int JudgeResult(int[][] rec,List<Step> steps,Set<Integer> availables){
		//每一次只判断最后一次加进去的是否形成了胜局。1：black win；-1：white win；0：not finished；10：no winner
		Step laststep=steps.get(steps.size()-1);
		int x=laststep.x;
		int y=laststep.y;
		
		int role=(laststep.color==Color.BLACK)?1:-1;
		int x0=Math.max(x-4, 0);
		int y0=Math.max(y-4, 0);
		int x1=Math.min(x+4, Gomoku.Length-1);
		int y1=Math.min(y+4, Gomoku.Length-1);
		int cnt=0;
		boolean before=true;
		for(int i=0;i<9;++i){
			if(before){
				while(y0+i<=y1&&rec[x][y0+i]!=role) ++i;
				before=false;
			}
			if(y0+i<=y1&&rec[x][y0+i]!=role)break;
			cnt++;		
		}
		if(cnt>=5) {
			System.out.println("In Gomoku.JudgeResult: finished!");
			return role;
			}
		
		cnt=0;
		before=true;
		for(int i=0;i<9;++i){
			if(before){
				while(x0+i<=x1&&rec[x0+i][y]!=role) ++i;
				before=false;
			}
			if(x0+i<=x1&&rec[x0+i][y]!=role)break;
			cnt++;		
		}
		if(cnt>=5) return role;
		

		cnt=0;
		before=true;
		int num=Math.min(y1-y0, x1-x0);


		System.out.println("In Gomoku.JudgeResult: X:"+x+"; Y:"+y+" whether form a five-row");
		
		for(int i=0;i<=num;++i){
			if(before){
				while(i<=num&&rec[x0+i][y0+i]!=role) ++i;
				before=false;
			}
			if(i<=num&&rec[x0+i][y0+i]!=role)break;
			cnt++;		
		}
		if(cnt>=5) return role;
		

		cnt=0;
		before=true;
		for(int i=0;i<=num;++i){
			if(before){
				while(i<=num && rec[x0+i][y1-i]!=role) ++i;
				before=false;
			}
			if(i<=num&&rec[x0+i][y1-i]!=role)break;
			cnt++;		
		}
		if(cnt>=5) 
			return role;
		
		
		if(availables.size()>0) return 0;
		else return 10;
	}

	
}
