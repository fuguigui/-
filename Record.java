import java.util.List;

import javax.management.relation.Role;

/*
 * The class is a data structure to record the layout of the cheeses on the board. It is realized by a char list. 
 * Transfer the two dimension board into one dimension list. The size of the list is determined by the size of the board. Each 
 * element on the board need 2 bit, 00 showing that the location is empty, 01 as black , 10 as white.
 * The two dimension looks like (0,0),(1,0),(2,0);(0,1),(1,1),(2,1);(0,2),(1,2),(2,2)
 * 
 * It realizes the following function:
 * getRec(int x,int y): given the real location, returen the record
 * putInRec(int x, int y): given the real location and the info needed to be saved, then save into the map.
 * empty(): empty all of the current info.
*/
public class Record {
	int[] bitmap;//bitmap to save the transformed information of the chesses.
	int size;//the size of bitmap as int type
	int real_len;//the real length of the board.
	int leftnum;//the number of empty locations.
	List<Location> possible;//the next possible locations of current board.
	
	Record(int len){
		real_len=len;
		leftnum=len*len;
		
		if(len%4==0) this.size=len*len/16;
		else this.size=len*len/16+1;
		if(this.size==0);
		else bitmap = new int[this.size];
	}
	Record(){
		this(0);
	}
	void empty(){
		for(int i=0;i<size;++i)
			bitmap[i]=0;
	}
	int[] RealToVir(int x,int y){//map the real location to the one-dimension bitmap order.
		//map the real location to the current bits in the charmap. returned value is the first bit's location
		int[] vir=new int[2];
		int index=2*(y*real_len+x);
		vir[1]=index/32;
		vir[0]=index%32;
		return vir;
	}
    int[] VirToReal(int index, int group){//map the two-dimension virtual location to the real lpcation.
		int vir = group*32+index;
		int[] real=new int[2];
		real[0]=(int)(vir/2)%real_len;
		real[1]=(int)(vir/2)/real_len;
		return real;
	}
	public int ValueToIndex(int value){
		int index = 0;
		while(true){
			if(value==0) return index;
			value=value>>1;
			index++;
		}
	}
	public int getLine(int x,int y,int dirx,int diry){
		int line=0;
		int x0 = Math.max(0, x-4*dirx);
		int x1=Math.min(real_len, x+4*dirx);
		int y0,y1;
		if(diry>=0){
			y0=Math.max(0, y-4*diry);
			y1=Math.min(real_len, y+4*diry);
			
			System.out.println("the real_len is "+ real_len+"x0: "+x0+" x1: "+x1+" y0: "+y0+" y1: "+y1);
			
			for(;x0<=x1&&y0<=y1;){
				line=line<<2+getRec(x0, y0);
				x0+=dirx;
				y0+=diry;
			}
		}
		else{
			y0=Math.min(real_len, y-4*diry);
			y1=Math.max(0, y+4*diry);
			
			System.out.println("the real_len is "+ real_len+"x0: "+x0+" x1: "+x1+" y0: "+y0+" y1: "+y1);
			
			for(;x0<=x1&&y0>=y1;){
				line=line<<2+getRec(x0, y0);
				x0+=dirx;
				y0+=diry;
			}
		}
		
		return line;
	}
	void putInRec(int x,int y, int role){
		int real[] = RealToVir(x, y);
		bitmap[real[1]]=bitmap[real[1]]+(role<<real[0]);
//		if(isBlack){
//			System.out.println("In putInRec.for.isBlack"+(bitmap[group]|1<<bitindex));
//			bitmap[group]=(char)(bitmap[group]|1<<bitindex);
//		}
//		else
//			bitmap[group]=(char)(bitmap[group]|1<<(bitindex+1));
		leftnum--;
	}
	int getRec(int x,int y){
		int[] real = RealToVir(x, y);
		
		System.out.println("In getRec: bitindex = "+real[0]+"the value is "+(1&(bitmap[real[1]]>>(real[0])+1)));
		return (1&(bitmap[real[1]]>>(real[0]+1)))*2+((bitmap[real[1]]>>real[0])&1);
	}
	void removeRec(int x,int y){
		int real[] = RealToVir(x, y);
		
		int toreverse=3<<real[0];
		toreverse=~toreverse;
		bitmap[real[1]]=bitmap[real[1]]&toreverse;
	}
	void show(){
		//show the value of the bitmap
		System.out.println("In show: the size is "+size+"\t the value is:");
		for(int i=0;i<bitmap.length;++i){
			System.out.println(Integer.toBinaryString(bitmap[i]));
		}
	}
	
	Boolean isNull(){
		if(size==0)return true;
		else return false;
	}
	
	//test case:

	void testcase(int x,int y,int role){
		//this module test the putInRec, getRec, MoveRec method
		System.out.println("in testcase :("+x+","+y+"), role:"+role);
		show();
		putInRec(x, y, role);
		show();
		int rec= getRec(x, y);
		show();
		System.out.println("In testcase after getRec: the rec = "+rec);
		removeRec(x, y);
		show();
	}
	public static void main(String[] args) {
		Record rec=new Record(3);
		rec.testcase(1,1,1);
		rec.testcase(1,2,2);
		Record rec2=new Record();
		System.out.println("Test isNull: whether the rec2 is null? "+rec2.isNull());
		
		System.out.println("**********Test RealToVir and VirToReal**********");
		Record rec3=new Record(Gomoku.Length);
		int[] temp=new int[2];
		temp=rec3.RealToVir(4, 5);
		System.out.println("the length is "+rec3.real_len+" Real: (4,5) To Virtual ("+temp[0]+","+temp[1]+")");
		temp=rec3.VirToReal(temp[0], temp[1]);
		System.out.println("Virtual To Real ("+temp[0]+","+temp[1]+")");

		temp=rec.RealToVir(1, 2);
		System.out.println("the length is "+rec.real_len+" Real: (1,2) To Virtual ("+temp[0]+","+temp[1]+")");
		temp=rec.VirToReal(temp[0], temp[1]);
		System.out.println("Virtual To Real ("+temp[0]+","+temp[1]+")");
		
		System.out.println("********Test ValueToIndex********");
		rec.putInRec(2, 0, 2);
		int index=rec.ValueToIndex(rec.bitmap[0]);
		System.out.println("Test White version: "+index);
		rec.removeRec(2, 0);
		rec.putInRec(2, 0, 1);
		rec.empty();
		rec.show();
	//	index=rec.ValueToIndex(rec.bitmap[0]);
	//	System.out.println("Test Black version: "+index);
		
		System.out.println("*********Test Getline******");
		rec.putInRec(1, 0, 1);
		int line=rec.getLine(1, 1, 0, 1);
		System.out.println(Integer.toBinaryString(line));
		
		
		
		
//		int leftmove=5;
//		int moved=3<<leftmove;
//		System.out.println("3 to left move "+leftmove+" index, and the result is "+Integer.toBinaryString(moved));
//		for(;leftmove<20;++leftmove){
//			int moveded = 3<<leftmove;
//			System.out.println("3 to left move "+leftmove+" index, and the result is "+Integer.toBinaryString(moveded));
//		}
	}
	

}
