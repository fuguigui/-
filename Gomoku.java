import java.awt.Color;
import java.util.*;

public class Gomoku {
	static public int Length=15;//the number of elements in each line.

	public static int JudgeResult(int[][] rec,List<Step> steps,Set<Integer> availables){
		//need parameters: rec: a special data structure to record the arrangement of the current game.
		//x,y: the location of the last step.
		//each time, judge whether the game is end based on the last step.
		//return value: 01(1) the black wins, 10(2) the white wins; 11(3): nobody wins and the game is over; 00(0): not finished.
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

	public static int[] CompareGet(Record succeed, Record root){
		//compare the current state and root state and get the real location of the newest chess
		int[] real=new int[2];
		for(int i=0;i<root.size;++i){
			if(succeed.bitmap[i]==root.bitmap[i]) continue;
			int value=succeed.bitmap[i]^root.bitmap[i];
			real[0]=i;
			real[1]=Record.ValueToIndex(value);
		}
		return real;
	}
	
	public static int JudgeResult(Record rec, int x, int y){
		//judge all of the four directions
		int result;
		int[] dir={0,1,1,0,1,1,1,-1};
		for(int i=0;i<4;++i){
			//judge from four directions.
			result=JudgeResultDir(rec, x, y,dir[2*i] ,dir[2*i+1]);//change here
			if(result!=0) return result;
		}
		if(rec.leftnum==0) return -1;
		else return 0;
	}
	
	public static int JudgeResultDir(Record rec, int x, int y, int dirx, int diry){
		//rec: the record of the current state, (x, y) the location of the judge point, (dirx, diry): the judge direction
		/*
		 * 1. get the designated line from the record;
		 * 2. check whether the line is satisfied.
		 */
		int line=rec.getLine(x,y,dirx,diry);
		//how can we say five chesses in a line?
		int blackwin=0b0101010101;
		int whitewin=0b1010101010;
		int standard=0b10000000000;
		for(int i=0;i<22;++i){
			if((line^blackwin)%standard == 0) return 1;
			else {
				line=line>>1;
				if((line^whitewin)%standard==0) return 2;
			}
		}
		return 0;
	}
}
