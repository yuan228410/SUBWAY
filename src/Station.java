import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.SystemColor;
import java.awt.geom.Ellipse2D;

import javax.swing.JButton;
import javax.swing.plaf.basic.BasicButtonUI;

public class Station extends JButton{
	private String name;//վ������
	private int x,y;//վ������
	private int [] line;//�ɻ�����·
	private int linenum;//�ɻ�����·��
	private Color [] color;//�ɻ�����·��ɫ
	final private int width =10;
	final private int height =10;
	public boolean pointing=false;
	Shape shape;
	Color bgColor = SystemColor.control;

	//String 	first_run;//�װ೵
	//String 	last_run;//ĩ�೵
	
	boolean draggable = false;//���϶���־
	Station(){
		super();
		}
	Station(String name,int x,int y,int linenum,int[] line) {
		super();
		super.setUI(new BasicButtonUI());// �ָ������Ӿ�Ч��
		super.setContentAreaFilled(false);
		Dimension size = this.getPreferredSize();
	    size.width = size.height = Math.max(size.width, size.height);
	    this.setPreferredSize(size); // ���ÿ�ߵȾ�
	    this.setContentAreaFilled(false); // ��������������
	    this.setBorderPainted(false); // �����Ʊ߿�
	    this.setFocusPainted(false); // �����ƽ���״̬
	    this.setBackground(bgColor);
	    this.setOpaque(false);
	    this.setVisible(false);
	    setStation(name,x,y,linenum,line);
		}
	protected void paintComponent(Graphics g) {
	    // �����갴�£�isArmed()��������true
	    if (this.getModel().isArmed()) {
	      g.setColor(java.awt.SystemColor.controlHighlight);
	    } else {
	      //g.setColor(java.awt.SystemColor.controlShadow);
	      g.setColor(this.bgColor); // ���ñ�����ɫ
	    }
	    g.fillOval(0, 0, this.getSize().width - 1, this.getSize().height - 1); // ����Բ�α�������
	    g.setColor(java.awt.SystemColor.controlShadow); // ���ñ߿���ɫ
	    g.drawOval(0, 0, this.getSize().width - 1, this.getSize().height - 1); // ���Ʊ߿���
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
