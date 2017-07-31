
public class Evaluate {
	int result=0, count = 0, block= 0, empty;
	
	public void reset(){
		count = 1;
		block = 0;
		empty = -1;
	}
	
	public int refine(int score){
		if(score<S.FOUR && score >= S.B_FOUR)
			if(score>=S.B_FOUR && score<(S.B_FOUR + S.THREE))
				return S.THREE;
			else if(score>=S.B_FOUR+S.THREE && score<2*S.B_FOUR)
				return S.FOUR;
			else return S.FOUR * 2;
		return score;
	}
	
	public int pointscore(int[][] roles, int x,int y, int role){
		int len = Gomoku.Length;
		
		reset();
		//先计算横向的分数
		for(int i = y+1;true;i++){
			int _role = roles[x][i];
			if(i>=len || _role == -role){
				block++;
				break;
			}
			if(_role == 0){
				if(empty == -1 && i<len-1 
						&& roles[x][i+1]==role){
					empty = count;
				}
				else  break;
			}
			else if(_role == role){
				count++;
			}			
		}
		for(int i = y-1;true;i--){
			int _role = roles[x][i];
			if(i<0 || _role == -role){
				block++;
				break;
			}
			if(_role == 0){
				if(empty == -1 && i>0
						&& roles[x][i-1]==role){
					empty = 1;//���﾿����1����0������
				}
				else break;
			}
			else if(_role == role){
				count++;
			}
		}
//		count+=secondCount;
		result+=Score.score_type(count,block,empty);
		
		//����,������
		reset();
		for(int i = x+1;true;i++){
			int _role = roles[i][y];
			if(i>=len || _role == -role){//�������λ��λ�ڱ߽紦�����߱��Է�����ռ�죬����������
				block++;
				break;
			}
			if(_role == 0){//����ǵ�һ�������հ״������±߱���������ռ�죬���¼��ȱλ��
				if(empty == -1 && i<len-1 
						&& roles[i+1][y]==role){
					empty = count;
				}
				else  break;
			}
			else if(_role == role){
				count++;
			}			
		}
		//�ӵ�ǰλ�����ϼ��
		for(int i = y-1;true;i--){
			int _role = roles[i][x];
			if(i<0 || _role == -role){
				block++;
				break;
			}
			if(_role == 0){
				if(empty == -1 && i>0
						&& roles[i-1][x]==role){
					empty = 1;//���﾿����1����0������
				}
				else break;
			}
			else if(_role == role){
				count++;
			}
		}
		result+=Score.score_type(count,block,empty);
		
		//б�ż�飬�ȼ��\��������
		reset();
		int _x,_y;
		for(int i = 1;true;i++){
			_x = x+i; _y = y+i;
			int _role = roles[_x][_y];
			if(_x>=len || _y>=len || _role == -role){//�������λ��λ�ڱ߽紦�����߱��Է�����ռ�죬����������
				block++;
				break;
			}
			if(_role == 0){//����ǵ�һ�������հ״��������±���������ռ�죬���¼��ȱλ��
				if(empty == -1 && _x<len-1 && _y<len-1 
						&& roles[_x+1][_y+1]==role){
					empty = count;
				}
				else  break;
			}
			else if(_role == role){
				count++;
			}			
		}
		//�ӵ�ǰλ�������ϼ��
		for(int i = 1;true;i++){
			_x= x-i;_y=y-i;
			int _role = roles[_x][_y];
			if(_x<0 ||_y<0 || _role == -role){
				block++;
				break;
			}
			if(_role == 0){
				if(empty == -1 && _x>0 && _y>0
						&& roles[_x-1][_y-1]==role){
					empty = 1;
				}
				else break;
			}
			else if(_role == role){
				count++;
			}
		}
		result+=Score.score_type(count,block,empty);
		
		//б�ż�飬���/��������
		reset();
		for(int i = 1;true;i++){
			_x = x+i; _y = y-i;
			int _role = roles[_x][_y];
			if(_x>=len || _y<len || _role == -role){//�������λ��λ�ڱ߽紦�����߱��Է�����ռ�죬����������
				block++;
				break;
			}
			if(_role == 0){//����ǵ�һ�������հ״��������±���������ռ�죬���¼��ȱλ��
				if(empty == -1 && _x<len-1 && _y<len-1 
						&& roles[_x+1][_y-1]==role){
					empty = count;
				}
				else  break;
			}
			else if(_role == role){
				count++;
			}			
		}
		//�ӵ�ǰλ�������ϼ��
		for(int i = 1;true;i++){
			_x= x-i;_y=y+i;
			int _role = roles[_x][_y];
			if(_x<0 ||_y>=len || _role == -role){
				block++;
				break;
			}
			if(_role == 0){
				if(empty == -1 && _x>0 && _y>0
						&& roles[_x-1][_y+1]==role){
					empty = 1;
				}
				else break;
			}
			else if(_role == role){
				count++;
			}
		}
		result+=Score.score_type(count,block,empty);
		return refine(result);
	}

	
	public boolean hasNeighbor(int[][] roles, int x,int y,int dis, int count){
		int x0=x-dis;int x1=x+dis;
		int y0=y-dis;int y1=y+dis;
		x0=Math.max(0, x0);
		y0=Math.max(0, y0);
		x1=Math.min(Gomoku.Length, x1);
		y1=Math.min(Gomoku.Length, y1);
		
		for(int i = x0;i<x1;++i){
			for(int j = y0;j<y1;++j){
				if(i==x && j == y)continue;
				if(roles[i][j]!=0){
					count--;
					System.out.println("have neighbor!");
					if(count<=0)return true;
				}
			}
		}
		System.out.println("no neighbour!");
		return false;
	}
}
