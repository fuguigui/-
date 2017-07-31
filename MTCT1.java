import java.awt.Choice;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class MTCT1 extends Player{
	//使用蒙特卡洛树搜索进行模拟，使用UCB
	//boolean isblack=true;
	float calculation_time=1000000;//最大运算时间，用于剪枝
	int max_actions;//每次模拟对局最多进行的步数
	int search_scope=2;//定义adjacent的搜索范围
	double confident=1.96;//UCB中的常数
	int total;
	int wins;//记录获胜的次数
	int expand_depth=50;//展开的最深层数
	int simulations=0;//模仿的次数计数
	

    Date begin=new Date();
	Date now;
	
//	static LinkedList<Integer, Pair> simulated=new LinkedList<>();//记录树的数据结构;//Integer为父亲的位置，Pair为父亲-孩子对的位置
//	Iterator< >
	static Node simulated=new Node();

	//每次计算下一步时都要清空plays和wins表，因为经过
//	static HashMap<Pair, Integer> posToTotal=new HashMap<>();
//	static HashMap<Pair, Integer> posToWins=new HashMap<>();
//	static HashMap<Pair, Integer> posToLoses=new HashMap<>();

	static HashMap<Integer, Integer> posToTotal=new HashMap<>();
	static HashMap<Integer, Integer> posToWins=new HashMap<>();
	static HashMap<Integer,Integer> posToLoses=new HashMap<>();

	public int[][] rec= new int[Gomoku.Length][Gomoku.Length];//0:没有棋子；1：黑棋；-1：白棋
	public List<Step> steps;
	public Set<Integer> availables = new HashSet<>();//存放当前的可供选择的结点
	
	List<Integer> adjacents;
	
	
	public MTCT1(boolean isBlack){
		super(isBlack);
	}
	
	//返回move
	@SuppressWarnings("deprecation")
	public int getAction(){
		posToTotal=new HashMap<>();
		posToWins=new HashMap<>();
		posToLoses=new HashMap<>();
		
//do some copy
	    availables = new HashSet<Integer>(State.availables.size());   
	    Iterator<Integer> it_ava = State.availables.iterator();   
	    while(it_ava.hasNext()){   
	        availables.add(it_ava.next());   
	    }  
	    
	    for(int i=0;i<Gomoku.Length;++i){
	    	for(int j=0;j<Gomoku.Length;++j)
	    		rec[i][j]=State.rec[i][j];
	    }
	    
	    
	    steps=new LinkedList();
	    for(int i=0;i<State.steps.size();++i){
	    	steps.add(new Step(State.steps.get(i)));
	    	int x=i/Gomoku.Length, y=i%Gomoku.Length;
	    	rec[x][y]=State.rec[x][y];
	    }
	    if(availables.size() == 1){
	    	Iterator<Integer> it=availables.iterator();
	    	return it.next();
	    }
		
		int move;
		simulations=0;
		begin=new Date();
		while(true){
			now = new Date();
			
			System.out.println("the time span is"+(now.getTime()-begin.getTime()));
			
			if(now.getTime()-begin.getTime()>calculation_time) break;
			if(simulations>max_actions)break;
			
			int rootx=State.steps.get(State.steps.size()-1).x;
			int rooty=State.steps.get(State.steps.size()-1).y;
			int root=rootx*Gomoku.Length+rooty;
			simulated=new Node(root, true);
			//把准备工作做好之后，开始进行模仿
			for(Integer child:availables){
			//	Pair root_child=new Pair(root,child);
				simulated.addChild(new Node(child, false));
				posToTotal.put(child, 0);
				posToWins.put(child, 0);
				posToLoses.put(child, 0);
			}
			runSimulation(1, false,simulated);
			simulations++;
			
			System.out.println("In MTCT1.getAction : imitate for "+simulations+"times");
		}

		move=selectOneMove(simulated);
		int move_x = move/Gomoku.Length;
		int move_y=move%Gomoku.Length;
		System.out.println("In MTCT1.getAction: The computer choose: "+move_x+", "+move_y);
		
		return move;
	}
	
	void runSimulation(int depth,boolean isblack,Node root){
		//这个函数：为了得到所有的availables的备选位置的分数值,total和wins
		//如果时间在规定的范围内，就选择随机数，进行展开实验，展开实验的结果为
		
		  //输出availables内容来查看***************8
	    Iterator<Integer> it_ava_1 = availables.iterator();
	    while(it_ava_1.hasNext()){
	    	Integer temp=it_ava_1.next();
	    	System.out.println("In MTCT1.runSimulation: availables x:"+temp/Gomoku.Length+" y:"+temp%Gomoku.Length);
	    }

	    
		if(depth>expand_depth)return;
		simulations++;
		
		System.out.println("In MTCT1.runSimulation: The root is ("+root.root/Gomoku.Length+", "+root.root%Gomoku.Length);

	//	simulated.put(root);
//		System.out.println("In MTCT1.runSimulation: simulated is not null");
//		posToTotal.put(simulated.g, posToTotal.get(simulated.get(root))+1);
	

		int x=root.root/Gomoku.Length,y=root.root%Gomoku.Length;
		
		List<Integer> peripherals=new LinkedList<>();
		if(IsallExpanded( root,peripherals)){
			double max_frac=0.0;
			double log_total=0.0;
			
			System.out.println("The children of the root are all expanded!");
			
			//UCB
			int sum=0;
			for(Integer it_pair: posToTotal.keySet()){
				if(it_pair!=root.root) continue;
				sum+=posToTotal.get(it_pair);
			}
			log_total=Math.log(sum);
			if(isblack){
				//如果root是黑棋，就计算wins的最大值
				for(Integer cur_pair:posToTotal.keySet()){
					//只计算当前结点后缀中的那些位置的值
					if(!root.child.contains(cur_pair)) continue;
					if(posToTotal.get(cur_pair)>0){
						double cur_value=(double)(posToWins.get(cur_pair)/posToTotal.get(cur_pair))+
							Math.sqrt(confident)*log_total/posToTotal.get(cur_pair);
						if(cur_value>max_frac){
							max_frac=cur_value;
							x=cur_pair/Gomoku.Length;
							y=cur_pair%Gomoku.Length;
						}
					}
				}
				
				System.out.println("In MTCT1.isallexpanded: the UCB's result ("+x+", "+y+" )");
				
			}
			else{
				//如果root是白棋，就计算loss的最大值
				for(Integer cur_pair:posToTotal.keySet()){
					//只计算当前结点后缀中的那些位置的值
					if(!root.child.contains(cur_pair)) continue;
					if(posToTotal.get(cur_pair)>0){
					double cur_value=(double)(posToLoses.get(cur_pair)/posToTotal.get(cur_pair))+
							Math.sqrt(confident)*log_total/posToTotal.get(cur_pair);
					
					if(cur_value>max_frac){
						max_frac=cur_value;
						x=cur_pair/Gomoku.Length;
						y=cur_pair%Gomoku.Length;
					}
					}
				}
				
			}
		}
		else{
			int index=(int)(Math.random()*peripherals.size());
			int move=peripherals.get(index);
			x=move/Gomoku.Length;
			y=move%Gomoku.Length;
		}
		//在Node树上定位到这个结点
		Integer new_root=x*Gomoku.Length+y;

		Node selected=new Node(new_root,isblack);
	//	for(Node child: root.child){
		//	if(child.root == new_root){
			//	selected=child;
				//break;
	//		}
	//	}
		//如果选出来一个结点之后，就在备选列表里把它挪走，在棋盘上标志出来，同时，在候选列表里把它加进去
		State.simu_putinChess(x, y,isblack,steps,rec,availables);
		
		int result=Gomoku.JudgeResult(rec,steps,availables);
		//把准备工作做好之后，开始进行模仿
		for(Integer child:availables){
			selected.addChild(new Node(child, isblack));
			if(posToTotal.containsKey(child)){
			//	Pair old=posToTotal.c
				posToTotal.replace(child, posToTotal.get(child)+1);
			}
			else{
				posToTotal.put(child, 0);
				posToWins.put(child, 0);
				posToLoses.put(child, 0);
			}
			if(result==1){
				System.out.println("In MTCT1.runSimulation: before");
		//		for()
				posToWins.replace(child, posToWins.get(child)+1);
				return;
			}
			if(result==-1){
				posToLoses.replace(child, posToLoses.get(child)+1);
				return;
			}
			posToWins.put(child, 0);
			posToLoses.put(child, 0);
		}
		
		//看看puttototal里究竟是什么
	//	for(Entry<Pair, Integer> print_pair:posToTotal.entrySet())
		//	System.out.println("In MTCT1.runSimulation: Total "+print_pair);
		System.out.println(" ");
		System.out.println("**************The next simulation********");
		runSimulation(depth+1,!isblack, selected);
	}
	
		

	boolean IsallExpanded(Node root,
			List<Integer> peri){
		
		System.out.println("In MTCT1.IsallExpanded, it has "+root.child.size()+" children" );
		
		for(int i=0;i<root.child.size();++i){
			Integer child=root.child.get(i).root;
	//		Pair cur_pair=new Pair(root.root, child);
			
	//		System.out.println("In MTCT1.IsallExpanded: the cur_pair is "+cur_pair);
			
		//	if(posToTotal.containsKey(child))
				if(posToTotal.get(child)>0);
				else peri.add(child);
			
		}
	//	for(Integer child: availables. (root)){
	//		if(posToTotal.get(rec)>0)
	//			peri.add(rec.child);
	//	}
		if(peri.size()>0)return false;
		
		System.out.println("In MTCT1.IsallExpanded: is True"+" the peri size is"+peri.size());
		return true;
	}
	
	boolean isAvailable(int x,int y){
		if(0<=x&&x<Gomoku.Length && 0<=y&&y<Gomoku.Length
				&& State.rec[x][y]==0)
			return true;
		return false;
	}
	
	
	int selectOneMove(Node root){
		int index=(int)Math.random()*root.child.size();
		int x=index/Gomoku.Length,y=index%Gomoku.Length;
		double max_frac=0.0;
		double log_total=0.0;
		//UCB
		int sum=0;
		for(Node child: root.child){
			int nums=posToTotal.get(child);
			sum+=nums;
		}
		log_total=Math.log(sum);
		
		if(root.isBlack){
			//如果root是黑棋，就计算wins的最大值
			for(Node child: root.child){
				Integer cur_pair=child.root;
				//只计算当前结点后缀中的那些位置的值
				if(posToTotal.get(cur_pair)>0){
				double cur_value=(double)(posToWins.get(cur_pair)/posToTotal.get(cur_pair))+
						Math.sqrt(confident)*log_total/posToTotal.get(cur_pair);
				if(cur_value>max_frac){
					max_frac=cur_value;
					x=cur_pair/Gomoku.Length;
					y=cur_pair%Gomoku.Length;
				}
				}
			}
			
		}
		else{
			//如果root是白棋，就计算loss的最大值
			for(Integer cur_pair:posToTotal.keySet()){
				//只计算当前结点后缀中的那些位置的值
				double cur_value=(double)(posToLoses.get(cur_pair)/posToTotal.get(cur_pair))+
						Math.sqrt(confident)*log_total/posToTotal.get(cur_pair);
				if(cur_value>max_frac){
					max_frac=cur_value;
					x=cur_pair/Gomoku.Length;
					y=cur_pair%Gomoku.Length;
				}
			}
			
		}
		return x*Gomoku.Length+y;
	}
	
	void addPoint(Step newone){
		steps.add(newone);
		rec[newone.x][newone.y]=(newone.color==Color.black)?1:-1;	
	}
	class Pair{
		Integer root;
		Integer child;
		public Pair(Integer i1,Integer i2){
			root=i1;
			child=i2;
		}
		boolean equals(Pair other){
			if(other.root != this.root )return false;
			if(other.child!=this.child)return false;
			return true;
		}
	    @Override  
        public int hashCode() {  
            int result = 17;  
            result = result * 31 + root;  
            result = result * 31 + child;  
              
            return result;  
        }  
	    @Override
	    public String toString(){
	    	String result = "root: "+root+" child "+child;
	    	return result;
	    }
	}
	static class Node{
		public Integer root;
		public ArrayList<Node> child=new ArrayList<Node>();
//		public Point p=new Point();
		public boolean isBlack;
		Node(){
			this.child.clear();
			isBlack=true;
		}
		public Node(Integer _root,boolean isblack){
			root=_root;
			isBlack=isblack;
			this.child.clear();
		}
		public boolean equals(Node other){
			if(other.isBlack!=this.isBlack)return false;
			if(other.root!=this.root) return false;
			if(other.child!=this.child)return false;
			return true;
		}
		
		public int hashCode(){
			 int result = 17;  
	            result = result * 31 + root+(isBlack?1:0);  
	            for(Node childs: child){
	            	result= result * 31 + childs.root+(childs.isBlack?1:0);  
	            }
	              
	            return result;  
		}
		public void addChild(Node r){
			this.child.add(r);
		}
		public void setXY(int _x,int _y){
			root=_x*Gomoku.Length+_y;
		}
	}

}

