import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class State {

	public static int[][] rec= new int[Gomoku.Length][Gomoku.Length];//0:没有棋子；1：黑棋；-1：白棋
	public static List<Step> steps = new LinkedList<>();
	public static Set<Integer> availables = new HashSet<>();//存放当前的可供选择的结点
	
	public static void initState(){

		steps.add(new Step(7, 7,Color.BLACK));
		rec[7][7]=1;
		putInAvailables(7, 7,availables,rec);
	}
	
	public static void empty(){
		for(int i=0;i<Gomoku.Length;++i){
			for(int j = 0;j<Gomoku.Length;++j)
				rec[i][j]=0;
		}
		steps=new LinkedList<>();
		availables=new HashSet<>();
	}
	public static void addPoint(Step newone){
		System.out.println("In State.addPoint: the step is "+newone.x+"; "+newone.y);
		steps.add(newone);
		rec[newone.x][newone.y]=(newone.color==Color.black)?1:-1;
	//	availables.remove(newone.x*Gomoku.Length+newone.y);
		putInAvailables(newone.x, newone.y, availables,rec);
		
		 Iterator<Integer> it_ava_1 = availables.iterator();
		    while(it_ava_1.hasNext()){
		    	Integer temp=it_ava_1.next();
		    	System.out.println("In State.addPoint ---check Availables:availables x:"+temp/Gomoku.Length+" y:"+temp%Gomoku.Length);
		    }
	}
	public static void putInAvailables(int x,int y,Set<Integer> ava,int[][] rec){
		int x0=Math.max(x-2, 0);
		int x1=Math.min(x+3, Gomoku.Length);
		int y0=Math.max(y-2, 0);
		int y1=Math.min(y+3, Gomoku.Length);
		for(int i=0;i<x1-x0;++i){
			for(int j=0;j<y1-y0;++j){
				if(i==2&&j==2)continue;
				if(rec[i][j]==0)
					ava.add((x0+i)*Gomoku.Length+y0+j);
			}
		}
		ava.remove(x*Gomoku.Length+y);
	}
	public static void simu_putinChess(int x,int y, boolean isBlack,List<Step> steps,int[][]rec,Set<Integer> ava){
		steps.add(new Step(x , y , isBlack ? Color.BLACK : Color.WHITE));
		rec[x][y]=isBlack?1:-1;
		ava.remove(x*Gomoku.Length+y);
		System.out.println("In State.simu_putinChess the last step is x:"+steps.get(steps.size()-1).x+" y:"+steps.get(steps.size()-1).y);
		putInAvailables(x, y,ava,rec);
	}
}

class Step {
	public int x;
	
	public int y;
	
	public Color color;
	
	public Step(int x, int y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
	public Step(Step old){
		this.x=old.x;
		this.y=old.y;
		this.color=old.color;
	}

}


