class S{
	public static int ONE = 10, TWO = 100, THREE=1000, FOUR = 100000, FIVE = 1000000,
			B_ONE = 1, B_TWO = 10, B_THREE = 100, B_FOUR = 10000;//B_����blocked
}

public class Score {
	static public int score_type(int count, int block, int empty){
		if(empty<=0){
			if(count>=5)return S.FIVE;
			if(block==0){
				switch (count) {
				case 1: return S.ONE;
				case 2: return S.TWO;
				case 3: return S.THREE;
				case 4: return S.FOUR;
				}
			}
			if(block == 1){
				switch (count) {
				case 1:return S.B_ONE;
				case 2:return S.B_TWO;
				case 3:return S.B_THREE;
				case 4:return S.B_FOUR;
				}
			}
		}
		else {
			if(empty == 1||empty == count-1){//��ߵ�һ�������ұߵ�һ���ǿ�λ
				if(count>=6) return S.FIVE;
				if(block == 0) switch (count) {
				case 2: return S.TWO/2;
				case 3: return S.THREE;
				case 4: return S.B_FOUR;
				case 5: return S.FOUR;
				}
				if(block == 1)
					switch (count) {
					case 2:return S.B_TWO;
					case 3: return S.B_THREE;
					case 4: return S.B_FOUR;
					case 5:return S.B_FOUR;
					}
			}
			else if (empty == 2 || empty == count-2){
				if(count>=7) return S.FIVE;
				if(block == 0)
					switch (count) {
					case 3:return S.THREE;
					case 4: case 5: return S.B_FOUR;
					case 6: return S.FOUR;
					}
				if(block == 1)
					switch (count) {
					case 3:return S.B_THREE;
					case 4: case 5: return S.B_FOUR;
					case 6: return S.FOUR;
					}
				if(block == 2)
					return S.B_FOUR;
			}
			else if(empty == 3 || empty == count-3){
				 if(count>=8) return S.FIVE;
				 if(block == 0)
					 switch (count) {
					case 4:case 5: return S.THREE;
					case 6: return S.B_FOUR;
					case 7:return S.FOUR;
					}
				 if(block == 1)
					 switch (count) {
					case 4:case 5: case 6: return S.B_FOUR;
					case 7:return S.FOUR;
					}
				 if(block == 2)
					 return S.B_FOUR;
			}
			else if(empty == 4 || empty == count-4){
				if(count>=9)return S.FIVE;
				if(block ==0)
					return S.FOUR;
				if(block == 1)
					switch (count) {
					case 4:case 5: case 6:case 7:return S.B_FOUR;
					case 8:return S.FOUR;
					}
				if(block == 2)return S.B_FOUR;
			}
			else if(empty == 5 || empty == count-5)return S.FIVE;
		}
		return 0;
	}
}
