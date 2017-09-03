import java.util.ArrayList;
import java.util.List;

public class Location {
	int x,y;
	List<Location> surround(){
		List<Location> sur= new ArrayList<>();
		int x0=Math.max(0, x-2);
		int x1=Math.min(Gomoku.Length, x+2);
		int y0=Math.max(0, y-2);
		int y1=Math.min(Gomoku.Length, y+2);
		for(int i=0;i<5 && x0+i<=x1;++i){
			for(int j=0;j<5 && y0+j<=y1;++j){
				sur.add(new Location(x0+i,y0+j));
			}
		}
		sur.remove(new Location(x,y));
		return sur;
	}
	Location(){
		x=0;y=0;
	}
	Location(int _x,int _y){
		x=_x;
		y=_y;
	}
}
