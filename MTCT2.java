import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;


public class MTCT2 {
	Hashtable<Record, Node> data;
	void expand(Node root){
		//given a root, update its own score. If it is unexpanded, add all of its children, each doing one quick imitation and update their and its record.
		//if it has been expanded, choose the best child, do expand and then update its score, according to the recorded winning rounds.
		
		/*
		 * 1. judge whether expanded? according to root's children's list.
		 * 2.1 if expanded:
		 * 2.1.1. choose the best child
		 * 2.1.2. expand this child
		 * 2.1.3  update its own recording
		 * 2.2 if unexpanded:
		 * 2.2.1 find all of its children record and add them in its children's list
		 * 2.2.2 do quick-simulate to each child; update each child's record
		 * 2.2.3 according to each child's record, update its own one.
		 */
		if(root.children.isEmpty()){
			//if it is not expanded
			findNext(root.children,root.state,root.role);//findChildren(List<Integer> children, Record currentstate) return none;
			//given a state, find all of possible next steps; save the next state in a global Node hash table and add its index into the children list.
			//hash according to the Node.state, to each possible next state, hash and check first, if doesn't exist, build one; or directly add the current node to the list.
			for(Record child: root.children){
				int role;
				role = (root.role==1)?2:1;
				Node childnode=data.get(child);
				
				//update each dhild's record
				int result=quick_simu(child, role);//do a transfer, find the Record according to the index: child
				if(result==1) {
					childnode.wins++;
					root.wins++;
				}
				else if(result==2) {
					childnode.loses++;
					root.wins++;
				}
				childnode.total++;
				root.total++;
			}
		}
		else{
			//if the node has been expanded
			Node selected=theBest(root.children,root);//choose the best of its children; 
			int original=selected.wins;//save the original wins record, for further comparison
			expand(selected);
			//update the records
			root.total++;
			if(selected.wins==original){
				root.loses++;
			}
			else{
				root.wins++;
			}
			
			
		}
	}
	int quick_simu(Record root, int role){
		//role: black: 1; white: 2； the same with return value. return value: 0: not finished; -1: finish and no winner
		// given a record, put the other color's chess; if the game finishes, return the winning color; else repeat the simulation with new record.
		//how to put the chess? heuristic rules.
		/*
		 * 1.given the root, choose one from its next steps, put that step, form a new state
		 * 1.1. how to choose?
		 * 2.judge this state, if the game ends, return the result; else quick_simu with the new one;
		 */
		Record succeed = putNewCheese(root, role);//according to the state and who is the player, decided where to put the next chess.
		int result = theGameResult(succeed,root);//call the Game rule function to judge the result: -1: finished and no winner;):not finished; 1: the black wins; 2: the white wins
		if(result!=0)	return quick_simu(succeed, (role==1)?2:1);
		else return result;
	}
			
	Record putNewCheese(Record root, int role){
		/*according to the state and who is the player, decided where to put the next chess.
		 * given the root record and role, find the possible next states.
		 * 1. is there any unavoidable place? if so, put on this location.
		 * 2. use the best to choose next state.
		 */
		List<Record> succeed = new LinkedList<>();
		findNext(succeed, root,role);
		Record possible = unavoidable(succeed, root,role);// to judge if there is any unavoidable next steps
		if(possible.isNull()){
			possible = theBest(succeed,data.get(root)).state;
		}
		possible.leftnum--;
		return possible; 
	}
	int theGameResult(Record state,Record former){//call the Game rule function to judge the result: -1: finished and no winner;):not finished; 1: the black wins; 2: the white wins
		//a unit function: to judge whether the game is over.
		//according to the newest added step to judge the result
		/*
		 * 1. get the newest added step
		 * 2. judge whether it forms a five-chess line.
		 * 2.1. if so, according to its color, return the result
		 * 2.2  else, judge the number of left empty locations. 
		*/
		//get the newest added step
		int group=0,index=0;
		for(int i=0;i<state.size;++i){
			index=state.bitmap[i]^former.bitmap[i];
			if(index!=0) {
				index =former.ValueToIndex(index);//in each int, transfer the value to the index;
				group=i;
				break;
			}
		}
		//from the loc value to the real (x,y)
		int[] real = new int[2];
		real=former.VirToReal(group, index);//from the virtual location to real location.
		//from four directions to judge whether it forms a five-chess line
		return Gomoku.JudgeResult(state, real[0], real[1]);
	}
	Record unavoidable(List<Record> group,Record root, int role){
		//to judge whether there is a win-asured or lose-asured location. if so, return this; or return null.
		for(Record current: group){
			//if there exists a place, the current player can win, then return the place;
			//get the last chess's location, and judge the result
			int[] real = Gomoku.CompareGet(current,root);
			if(Gomoku.JudgeResult(current, real[0], real[1])!=0) 
				return current;
		}
		//No win-assured place, judge whether there is a place, the opposite can win.
		List<Record> children = new LinkedList<>();
		findNext(children, root, (role==1)?2:1);
		for(Record current: children){
			int[] real = Gomoku.CompareGet(current, root);
			if(Gomoku.JudgeResult(current, real[0], real[1])!=0)
				return current;
		}
		return new Record();
	}

	void findNext(List<Record> children, Record root, int role){
		//maintain a global next record group: the possible next record 
		//given a state, find all of possible next steps; save the next state in a global Node hash table and add its index into the children list.
		//hash according to the Node.state, to each possible next state, hash and check first, if doesn't exist, build one; or directly add the current node to the list.
		//the unit function: not rely on any other functions
		//note: create a new succeed: it's lefttime copied from its former.
		//according to the role: decide the player's color
		/*
		 * 1. get the root possible next steps.
		 * 2. iterate them: to any step: replace it with a new Record and update the Record's possible next steps.
		 */
		List<Location> rootPossible=root.possible;
		for(Location alternate: rootPossible){
			List<Location> newPossible = alternate.surround();
			Record child = root;
			child.putInRec(alternate.x, alternate.y, role);
			child.possible.removeAll(newPossible);
			child.possible.remove(alternate);
			child.possible.addAll(newPossible);
			children.add(child);
			Node childroot=new Node(child);//create the method to create a new node from the record
			data.put(child, childroot);
		}
		
	}
	Node theBest(List<Record> children, Node root){
		//choose the best of its children according to a given rule. according to UCB
		 /* 1. for each Record in children, find the corresponding node.
		 * 2. randomly choose one from the nodes.
		 * 3. quick-simulation the node and get the result.
		 */
		double c_coefficient=0.7;
		double log_total=Math.log(root.total);
		Node theBest = new Node();
		double ucb_tmp=0.0;
		
		for(Record child: children){
			Node childnode=data.get(child);
			if(childnode.role==1){
				double cur=childnode.wins/childnode.total+c_coefficient*Math.sqrt(log_total/childnode.total);
				if(cur>ucb_tmp){
					ucb_tmp=cur;
					theBest=childnode;
				}
			}
			else{
				double cur=childnode.loses/childnode.total+c_coefficient*Math.sqrt(log_total/childnode.total);
				if(cur>ucb_tmp){
					ucb_tmp=cur;
					theBest=childnode;
				}
			}
			
		}
		return theBest;
	}

}
class Node{
	/*
	 * the total rounds, the winning rounds, the losing rounds, the color, a list save the tokens of its child, a token for its parents.
	 */
	int total;
	int wins;//whichever the role of the Node is, wins always record the win times of black player finally.
	int loses;//whichever the role of the Node is, loses always record the lose times of white player
	int role;//black or white
	int father;
	List<Record> children;
	Record state;
	Node(){
		children = new LinkedList<>();
	}
	Node(Record state){
		this.state=state;
		children=new LinkedList<>();
	}
}
