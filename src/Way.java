import java.awt.Color;

public class Way {
	private Station station1,station2;//两端车站
	private int x1,x2,y1,y2;
	private int time;
	private int linenum;//涉及线路数目
	private int [] line;//涉及线路
	private Color [] color;//涉及线路颜色
	public boolean chosen=true;
	Way(Station s1,Station s2,int t){
		station1=s1;
		station2=s2;
		x1=s1.getstx();
		y1=s1.getsty();
		x2=s2.getstx();
		y2=s2.getsty();
		//System.out.print("from ("+x1+","+y1+") to ("+x2+","+ y2+")");
		time=t;
		linenum=0;
		line = new int[s1.getstlinenum()];
		for(int i=0;i<s1.getstlinenum();i++){
			for(int j=0;j<s2.getstlinenum();j++){
				if(s1.getstline()[i]==s2.getstline()[j]){
					line[linenum]=s1.getstline()[i];
					linenum++;
					}
				}
			}
		setColor();
		}
	public int getwline(){
		return line[0];
		}
	public int gettime(){
		return time;
		}
	public String getwname1(){
		return station1.getstname();
		}
	public String getwname2(){
		return station2.getstname();
		}
	public void setLine(int xx1,int yy1){
		x1=xx1;
		y1=yy1;
		String name=station1.getstname();
		int linenum=station1.getstlinenum();
		int [] line=station1.getstline();
		station1=new Station(name,x1-8,y1-31,linenum,line);
		}
	public int getwx1(){
		return x1;
		}
	public int getwx2(){
		return x2;
		}
	public int getwy1(){
		return y1;
		}
	public int getwy2(){
		return y2;
		}
	public Station getwstation1(){
		return station1;
		}
	public Station getwstation2(){
		return station2;
		}
	public Color getwcolor(){
		return color[0];
		}
	public void setColor(){
		color=new Color[1];
		if(line[0]==1)
			color[0]=new Color(238,50,41);
		else if(line[0]==2)
			color[0]=new Color(54,184,84);
		else if(line[0]==3)
			color[0]=new Color(255,216,35);
		else if(line[0]==4)
			color[0]=new Color(50,1,119);
		else if(line[0]==5)
			color[0]=new Color(130,48,148);
		else if(line[0]==6)
			color[0]=new Color(206,4,122);
		else if(line[0]==7)
			color[0]=new Color(243,86,15);
		else if(line[0]==8)
			color[0]=new Color(0,140,193);
		else if(line[0]==9)
			color[0]=new Color(145,197,219);
		else if(line[0]==10)
			color[0]=new Color(199,175,211);
		else if(line[0]==11)
			color[0]=new Color(142,35,35);
		else if(line[0]==12)
			color[0]=new Color(0,123,99);
		else if(line[0]==13)
			color[0]=new Color(242,147,209);
		else if(line[0]==16)
			color[0]=new Color(50,212,203);
		else if(line[0]==14)
			color[0]=Color.BLACK;
		else if(line[0]==15)
			color[0]=Color.GREEN;
		else if(line[0]==17)
			color[0]=Color.MAGENTA;
		else if(line[0]==18)
			color[0]=Color.PINK;
		
		
		}
	public void setway(Station s1,Station s2){
		station1=s1;
		station2=s2;
		}
}
