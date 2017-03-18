import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.SystemColor;
import java.awt.geom.Ellipse2D;

import javax.swing.JButton;
import javax.swing.plaf.basic.BasicButtonUI;

public class Station extends JButton{
	private String name;//站点名称
	private int x,y;//站点坐标
	private int [] line;//可换乘线路
	private int linenum;//可换乘线路数
	private Color [] color;//可换乘线路颜色
	final private int width =10;
	final private int height =10;
	public boolean pointing=false;
	Shape shape;
	Color bgColor = SystemColor.control;

	//String 	first_run;//首班车
	//String 	last_run;//末班车
	
	boolean draggable = false;//可拖动标志
	Station(){
		super();
		}
	Station(String name,int x,int y,int linenum,int[] line) {
		super();
		super.setUI(new BasicButtonUI());// 恢复基本视觉效果
		super.setContentAreaFilled(false);
		Dimension size = this.getPreferredSize();
	    size.width = size.height = Math.max(size.width, size.height);
	    this.setPreferredSize(size); // 设置宽高等距
	    this.setContentAreaFilled(false); // 不绘制内容区域
	    this.setBorderPainted(false); // 不绘制边框
	    this.setFocusPainted(false); // 不绘制焦点状态
	    this.setBackground(bgColor);
	    this.setOpaque(false);
	    this.setVisible(false);
	    setStation(name,x,y,linenum,line);
		}
	protected void paintComponent(Graphics g) {
	    // 如果鼠标按下，isArmed()方法返回true
	    if (this.getModel().isArmed()) {
	      g.setColor(java.awt.SystemColor.controlHighlight);
	    } else {
	      //g.setColor(java.awt.SystemColor.controlShadow);
	      g.setColor(this.bgColor); // 设置背景颜色
	    }
	    g.fillOval(0, 0, this.getSize().width - 1, this.getSize().height - 1); // 绘制圆形背景区域
	    g.setColor(java.awt.SystemColor.controlShadow); // 设置边框颜色
	    g.drawOval(0, 0, this.getSize().width - 1, this.getSize().height - 1); // 绘制边框线
	    super.paintComponent(g);
	}
	public boolean contains(int x, int y) {
		if ((shape == null) || (!shape.getBounds().equals(this.getBounds()))) {
			this.shape = new Ellipse2D.Float(0, 0, this.getWidth(), this.getHeight());
	    	}
	    return shape.contains(x, y);
	}
	public String getstname(){
		return name; 
		}
	public int getstx(){
		//System.out.print(" this.x = "+x);
		return x+8;
		}
	public int getsty(){
		//System.out.print(" this.y = "+y);
		return y+31;
		}
	public int getstlinenum(){
		return linenum;
		}
	public int[] getstline(){
		return line;
		}
	public void setStation(String name,int x,int y,int linenum,int []line){
		this.name=name;
		this.line=line;
		this.x=x;///3-50;
		this.y=y;///3-30;
		super.setLocation(this.x, this.y-25);
		this.linenum=linenum;
		this.line=new int[linenum];
		this.line=line.clone();
		setColor();
		this.setBounds(this.x, this.y-25, width, height);
		//System.out.print("x = "+this.x+"  y = "+this.y);
		}
	public void setPosition(int x,int y){
		this.x=x;///3-50;
		this.y=y;///3-30;
		super.setLocation(this.x, this.y-25);
		}
	public void setLine(int linenum,int []line){
		this.linenum=linenum;
		this.line=new int[linenum];
		this.line=line.clone();
		setColor();
		}
	private void setColor() {
		bgColor=Color.white;
		}
	
	
}
