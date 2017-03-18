
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
public class SubwayMap extends JFrame  implements MouseListener,MouseMotionListener{
	private int stationnum,waynum;
	private static int INF=Integer.MAX_VALUE;  
    //dist[i][j]=INF<==>no edges between i and j  
	private int[][] dist;  
    //the distance between i and j.At first,dist[i][j] is the weight of edge [i,j]    
	private int[][] path;    
	private int [] pathway;
	private int [] pathstation;
	private ArrayList<Integer> result=new ArrayList<Integer>();
	private boolean stationplace=false;//false�޳�վ true�г�վ
	private int startplace=-1;//-1�޲���  0 �ȴ�ѡ��  1ѡ��ȷ��
	private int endplace=-1;//-1�޲���  0 �ȴ�ѡ��  1ѡ��ȷ��
	private int removeplace=-1;//-1�޲���  0 �ȴ�ѡ��  1ѡ��ȷ��
	private boolean viewing=true;
	private boolean calculating=false;
	private boolean caldone=false;
	private boolean addstart=false;
	private boolean add=false;
	private boolean nline=false;
	private boolean addready=false;
	private boolean	remove=false;
	private boolean clearneeded=false;
	private boolean showpath=false;
	private String startst=null,endst=null,removest=null;
	private int addx=800,addy=800,order=1,full=1;
	//JFrame mainwindow;
	JFrame frame1,frame2;
	JMenuBar menubar;
	JMenu searchmenu,editmenu,filemenu,operationmenu;
	JLabel label,label2,label3,label4,label5,label6,label7;
	JTextField tf1,tf2;
	JComboBox<String> linenumbox,linebox1,linebox2,linebox3,linebox4,
				neighbournumbox,nlinebox1,nlinebox2,nlinebox3,nlinebox4
				,nlinebox5,nlinebox6,nlinebox7,nlinebox8
				,nstation1,nstation2,nstation3,nstation4
				,nstation5,nstation6,nstation7,nstation8
				,ndist1,ndist2,ndist3,ndist4,ndist5,ndist6,ndist7,ndist8;
	JButton savebutton;
	Container con;
	Station [] station;
	Way [] way;
	String SName="";
	Station newstation,abandonstation;
	Station []linest;
	Way [] newway,abandonway;
	SubwayMap(String filename1,String filename2) throws IOException{
		super("�Ϻ�����");
		super.setBounds(0, 0,1300,800);
		super.setBackground(SystemColor.control);
		super.setVisible(true);
		super.setResizable(false);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menubar = new JMenuBar();
		/*�˵�*/
    	filemenu = new JMenu("File");
    	searchmenu =new JMenu("Search");
    	editmenu = new JMenu("Edit");
    	operationmenu = new JMenu("operation");
    	
    	JMenuItem city = new JMenuItem("�Ϻ�");
		JMenuItem shortcut = new JMenuItem("���·��");
		JMenuItem newstation = new JMenuItem("���վ��");
		JMenuItem dropstation = new JMenuItem("ɾ��վ��");
		JMenuItem newline = new JMenuItem("�����·");
		JMenuItem back = new JMenuItem("����");
		
		filemenu.add(city);
		searchmenu.add(shortcut);
		editmenu.add(newstation);
		editmenu.add(dropstation);
		editmenu.add(newline);
		operationmenu.add(back);
		menubar.add(filemenu);
		menubar.add(searchmenu);
		menubar.add(editmenu);
		menubar.add(operationmenu);
		
		BufferedReader readTxt=null;
		readTxt=new BufferedReader(new FileReader(new File(filename1)));
	    stationnum=Integer.parseInt(readTxt.readLine());
		station=new Station[stationnum];
	    String textLine="";
	    for(int i=0;i<stationnum&&( textLine=readTxt.readLine())!=null;i++){
	    	String[] infoArray=textLine.split("\\s+");
	    	String name=infoArray[0];
	    	int x=Integer.parseInt(infoArray[1])/3-50;
	    	int y=Integer.parseInt(infoArray[2])/3-50;
	    	int linenum=infoArray.length-3;
	    	int []line =new int[linenum];
	    	for(int j=0;j<linenum;j++){
	    		line[j]=Integer.parseInt(infoArray[3+j]);
	    		}
	    	station[i]=new Station(name,x,y,linenum,line);
	    	}
	    readTxt.close();
	    readTxt=null;
		readTxt=new BufferedReader(new FileReader(new File(filename2)));
	    waynum=Integer.parseInt(readTxt.readLine());
	    way=new Way[waynum];
	    textLine="";
	    for(int i=0;i<waynum&&( textLine=readTxt.readLine())!=null;i++){
	    	String[] infoArray=textLine.split("\\s+");
	    	int time;
	    	if(isNumeric(infoArray[2]))
	    		time=Integer.parseInt(infoArray[2]);
	    	else 
	    		time=3;
	    	int s1=0,s2=0;
	    	boolean find1=false;
	    	boolean find2=false;
	    	for(int j=0;j<stationnum;j++){
	    		
	    		if(infoArray[0].equals(station[j].getstname())){
	    			s1=j;
	    			find1=true;
	    			}
	    		else if(infoArray[1].equals(station[j].getstname())){
	    			s2=j;
	    			find2=true;
	    			}
	    		}
	    	if(find1==false)
	    		System.out.println(infoArray[0]+"can't be found!");
	    	if(find2==false)
	    		System.out.println(infoArray[1]+"can't be found!");
	    	way[i]=new Way(station[s1],station[s2],time);
	    	for(int j=0;j<station[s1].getstlinenum();j++)
	    		System.out.print(" "+station[s1].getstline()[j]);
	    	System.out.print(way[i].getwname1()+" "+way[i].getwline()+" "+way[i].getwname2());
	    	for(int j=0;j<station[s2].getstlinenum();j++)
	    		System.out.print(" "+station[s2].getstline()[j]);
	    	
	    	System.out.println(" ");
	    	}
		for(int i=0;i<stationnum;i++)
	    	super.add(station[i]);
    	//paint(getGraphics());
    	addMouseMotionListener(this);
    	addMouseListener(this);
    	super.setJMenuBar(menubar);
    	menubar.setVisible(true);
    	ActionListener shortcutListener = new ActionListener( ) {
            public void actionPerformed(ActionEvent event) {
            	startplace=0;
            	calculating=true;
            	}
        	};
        shortcut.addActionListener(shortcutListener);
        ActionListener backListener = new ActionListener( ) {
            public void actionPerformed(ActionEvent event) {
            	caldone=false;
            	for(int i=0;i<waynum;i++){
            		way[i].chosen=true;
            		}
            	viewing=true;
            	calculating=false;
            	caldone=false;
            	add=false;
            	addready=false;
            	nline=false;
            	remove=false;
            	stationplace=false;
            	showpath=false;
            	startplace=-1;
            	endplace=-1;
            	removeplace=-1;
            	paint(getGraphics());
        	    }
        	};
        back.addActionListener(backListener);
        ActionListener addListener = new ActionListener( ) {
            public void actionPerformed(ActionEvent event) {
            	addstation();
        	    }
        	};
        newstation.addActionListener(addListener);
        ActionListener removeListener = new ActionListener( ) {
            public void actionPerformed(ActionEvent event) {
            	removeplace=0;
            	remove=true;
            	paint(getGraphics());
        	    }
        	};
        dropstation.addActionListener(removeListener);
        ActionListener addlineListener = new ActionListener( ) {
            public void actionPerformed(ActionEvent event) {
            	nline=true;
            	addline();
        	    }
        	};
        newline.addActionListener(addlineListener);
        
    	paint(getGraphics());
		this.paintComponents(getGraphics());
	}
	public void addline(){
		nline=true;
		frame2=new JFrame("�����·");
		frame2.setBounds(100, 100,300, 400);
		frame2.setVisible(true);
		frame2.setResizable(false);
		frame2.setBackground(Color.white);//new Color(255, 164, 85));
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.setLayout(null);
		JLabel jlabel1,jlabel2,jlabel3,jlabel4,jlabel5,jlabel6,jlabel7,jlabel8,jlabel9,jlabel10;
		jlabel1=new JLabel("��·�ţ�");
		jlabel2=new JLabel("��·����վ����");
		jlabel3=new JLabel("վ��1��");
		jlabel4=new JLabel("վ��2��");
		jlabel5=new JLabel("վ��3��");
		jlabel6=new JLabel("վ��4��");
		jlabel7=new JLabel("վ��5��");
		jlabel8=new JLabel("����");
		jlabel9=new JLabel("������");
		jlabel10=new JLabel("������");
		
		jlabel1.setBounds(5,5 , 100, 20);
		jlabel2.setBounds(5,30 , 100, 20);
		jlabel3.setBounds(5,80 , 70, 20);
		jlabel4.setBounds(5,105 , 70, 20);
		jlabel5.setBounds(5,130, 70, 20);
		jlabel6.setBounds(5,155 , 70, 20);
		jlabel7.setBounds(5,180 , 70, 20);
		jlabel8.setBounds(70,55, 70, 20);
		jlabel9.setBounds(130,55 , 70, 20);
		jlabel10.setBounds(210,55 , 70, 20);
		
		frame2.add(jlabel1);
		frame2.add(jlabel2);
		frame2.add(jlabel3);
		frame2.add(jlabel4);
		frame2.add(jlabel5);
		frame2.add(jlabel6);
		frame2.add(jlabel7);
		frame2.add(jlabel8);
		frame2.add(jlabel9);
		frame2.add(jlabel10);
		
		JTextField jtf1,jtf2,jtf3,jtf4,jtf5;
		jtf1=new JTextField();
		jtf2=new JTextField();
		jtf3=new JTextField();
		jtf4=new JTextField();
		jtf5=new JTextField();
		jtf1.setBounds(55,80 , 70, 20);
		jtf2.setBounds(55,105 , 70, 20);
		jtf3.setBounds(55,130 , 70, 20);
		jtf4.setBounds(55,155 , 70, 20);
		jtf5.setBounds(55,180 , 70, 20);
		frame2.add(jtf1);
		frame2.add(jtf2);
		frame2.add(jtf3);
		frame2.add(jtf4);
		frame2.add(jtf5);
		String [] stnum={"2","3","4","5"};
		String [] linecode={"14","15","17","18","19","20"};
		String []pos=new String[750];
		for(int i=0;i<pos.length;i++)
			pos[i]=Integer.toString(i+50);
		JComboBox box1,box2,boxx1,boxx2,boxx3,boxx4,boxx5,boxy1,boxy2,boxy3,boxy4,boxy5;
		box2=new JComboBox(stnum);
		box1=new JComboBox(linecode);
		boxx1=new JComboBox(pos);
		boxx2=new JComboBox(pos);
		boxx3=new JComboBox(pos);
		boxx4=new JComboBox(pos);
		boxx5=new JComboBox(pos);
		boxy1=new JComboBox(pos);
		boxy2=new JComboBox(pos);
		boxy3=new JComboBox(pos);
		boxy4=new JComboBox(pos);
		boxy5=new JComboBox(pos);
		box1.setBounds(100, 5,70,20);
		box2.setBounds(100, 30,70,20);
		boxx1.setBounds(130, 80,60,20);
		boxx2.setBounds(130, 105,60,20);
		boxx3.setBounds(130, 130,60,20);
		boxx4.setBounds(130, 155,60,20);
		boxx5.setBounds(130, 180,60,20);
		boxy1.setBounds(200, 80,60,20);
		boxy2.setBounds(200, 105,60,20);
		boxy3.setBounds(200, 130,60,20);
		boxy4.setBounds(200, 155,60,20);
		boxy5.setBounds(200, 180,60,20);
		frame2.add(box1);
		frame2.add(box2);
		frame2.add(boxx1);
		frame2.add(boxx2);
		frame2.add(boxx3);
		frame2.add(boxx4);
		frame2.add(boxx5);
		frame2.add(boxy1);
		frame2.add(boxy2);
		frame2.add(boxy3);
		frame2.add(boxy4);
		frame2.add(boxy5);
		jlabel5.setVisible(false);
        jlabel6.setVisible(false);
        jlabel7.setVisible(false);
        jtf3.setVisible(false);
        jtf4.setVisible(false);
        jtf5.setVisible(false);
        boxx3.setVisible(false);
        boxx4.setVisible(false);
        boxx5.setVisible(false);
        boxy3.setVisible(false);
        boxy4.setVisible(false);
        boxy5.setVisible(false);
		box2.addItemListener(new ItemListener(){
    		public void itemStateChanged(final ItemEvent e) {
    	        jlabel5.setVisible(box2.getSelectedIndex()>0);
    	        jlabel6.setVisible(box2.getSelectedIndex()>1);
    	        jlabel7.setVisible(box2.getSelectedIndex()>2);
    	        jtf3.setVisible(box2.getSelectedIndex()>0);
    	        jtf4.setVisible(box2.getSelectedIndex()>1);
    	        jtf5.setVisible(box2.getSelectedIndex()>2);
    	        boxx3.setVisible(box2.getSelectedIndex()>0);
    	        boxx4.setVisible(box2.getSelectedIndex()>1);
    	        boxx5.setVisible(box2.getSelectedIndex()>2);
    	        boxy3.setVisible(box2.getSelectedIndex()>0);
    	        boxy4.setVisible(box2.getSelectedIndex()>1);
    	        boxy5.setVisible(box2.getSelectedIndex()>2);
    	        
    	      }
    	});
		JButton addgo=new JButton("����");
		addgo.setBounds(50, 300, 200, 30);
		frame2.add(addgo);
		addgo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				frame2.setVisible(false);
				linest=new Station[box2.getSelectedIndex()+2];
				for(int i=0;i<box2.getSelectedIndex()+2;i++){
					if(i==0){
						int [] l={Integer.parseInt((String) box1.getSelectedItem())};
						linest[i]=new Station(jtf1.getText(),
								boxx1.getSelectedIndex()+50,
								boxy1.getSelectedIndex()+50,1,l);
						}
					else if(i==1){
						int [] l={Integer.parseInt((String) box1.getSelectedItem())};
						linest[i]=new Station(jtf1.getText(),
								boxx2.getSelectedIndex()+50,
								boxy2.getSelectedIndex()+50,1,l);
						}
					else if(i==2){
						int [] l={Integer.parseInt((String) box1.getSelectedItem())};
						linest[i]=new Station(jtf1.getText(),
								boxx3.getSelectedIndex()+50,
								boxy3.getSelectedIndex()+50,1,l);
						}
					else if(i==3){
						int [] l={Integer.parseInt((String) box1.getSelectedItem())};
						linest[i]=new Station(jtf1.getText(),
								boxx4.getSelectedIndex()+50,
								boxy4.getSelectedIndex()+50,1,l);
						}
					else if(i==4){
						int [] l={Integer.parseInt((String) box1.getSelectedItem())};
						linest[i]=new Station(jtf1.getText(),
								boxx5.getSelectedIndex()+50,
								boxy5.getSelectedIndex()+50,1,l);
						}
					}
				newway=new Way[box2.getSelectedIndex()+1];
				for(int i=0;i<newway.length;i++){
					newway[i]=new Way(linest[i],linest[i+1],3);
					}
				refresh();
			}
		});
		}
	public void removestation(){
		abandonstation=this.findstname(removest);
		abandonway=this.getassociatedway(removest);
		System.out.println("associatedway.length="+abandonway.length);
		refresh();
		}
	public Way[] getassociatedway(String name){
		ArrayList <Way> asway=new ArrayList<Way>();
		for(int i=0;i<waynum;i++){
			if(way[i].getwname1().equals(name) || way[i].getwname2().equals(name))
				asway.add(way[i]);
			}
		return asway.toArray(new Way[]{});
		}

	public void addstation(){
		ArrayList <String> stname1=new ArrayList<String>();
		ArrayList <String> stname2=new ArrayList<String>();
		ArrayList <String> stname3=new ArrayList<String>();
		ArrayList <String> stname4=new ArrayList<String>();
		ArrayList <String> stname5=new ArrayList<String>();
		ArrayList <String> stname6=new ArrayList<String>();
		ArrayList <String> stname7=new ArrayList<String>();
		ArrayList <String> stname8=new ArrayList<String>();
		ArrayList <String> stname9=new ArrayList<String>();
		ArrayList <String> stname10=new ArrayList<String>();
		ArrayList <String> stname11=new ArrayList<String>();
		ArrayList <String> stname12=new ArrayList<String>();
		ArrayList <String> stname13=new ArrayList<String>();
		ArrayList <String> stname14=new ArrayList<String>();
		ArrayList <String> stname15=new ArrayList<String>();
		ArrayList <String> stname16=new ArrayList<String>();
	
		for(int i=0;i<stationnum;i++){
			for(int j=0;j<station[i].getstlinenum();j++){
				if(station[i].getstline()[j]==1)
					stname1.add(station[i].getstname());
				else if(station[i].getstline()[j]==2)
					stname2.add(station[i].getstname());
				else if(station[i].getstline()[j]==3)
					stname3.add(station[i].getstname());
				else if(station[i].getstline()[j]==4)
					stname4.add(station[i].getstname());
				else if(station[i].getstline()[j]==5)
					stname5.add(station[i].getstname());
				else if(station[i].getstline()[j]==6)
					stname6.add(station[i].getstname());
				else if(station[i].getstline()[j]==7)
					stname7.add(station[i].getstname());
				else if(station[i].getstline()[j]==8)
					stname8.add(station[i].getstname());
				else if(station[i].getstline()[j]==9)
					stname9.add(station[i].getstname());
				else if(station[i].getstline()[j]==10)
					stname10.add(station[i].getstname());
				else if(station[i].getstline()[j]==11)
					stname11.add(station[i].getstname());
				else if(station[i].getstline()[j]==12)
					stname12.add(station[i].getstname());
				else if(station[i].getstline()[j]==13)
					stname13.add(station[i].getstname());
				else if(station[i].getstline()[j]==14)
					stname14.add(station[i].getstname());
				else if(station[i].getstline()[j]==15)
					stname15.add(station[i].getstname());
				else if(station[i].getstline()[j]==16)
					stname16.add(station[i].getstname());
				
				}
			}
		String [][]stname={stname1.toArray(new String[]{}),
				stname2.toArray(new String[]{}),
				stname3.toArray(new String[]{}),
				stname4.toArray(new String[]{}),
				stname5.toArray(new String[]{}),
				stname6.toArray(new String[]{}),
				stname7.toArray(new String[]{}),
				stname8.toArray(new String[]{}),
				stname9.toArray(new String[]{}),
				stname10.toArray(new String[]{}),
				stname11.toArray(new String[]{}),
				stname12.toArray(new String[]{}),
				stname13.toArray(new String[]{}),
				stname14.toArray(new String[]{}),
				stname15.toArray(new String[]{}),
				stname16.toArray(new String[]{})};
		frame1=new JFrame("���վ��");
		frame1.setBounds(100, 100,300, 400);
		frame1.setVisible(true);
		frame1.setResizable(false);
		frame1.setBackground(Color.white);//new Color(255, 164, 85));
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setLayout(null);
		
		label=new JLabel("��վ���ƣ�");
		label2=new JLabel("��·��Ŀ��");
		label3=new JLabel("������·��");
		label4=new JLabel("��վ��Ŀ��");
		label5=new JLabel("��վ����");
		label6=new JLabel("������վ����ʱ��");
		label7=new JLabel("��վ������·");
    	tf1=new JTextField();
    	String [] linenums={"1","2","3","4"};
    	String [] neighbournums={"1","2","3","4","5","6","7","8"};
    	String [] lines={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16"};
    	String [] time={"1","2","3","4","5","6","7","8","9","10"};
    	linenumbox=new <String>JComboBox(linenums);
    	linebox1 =new <String>JComboBox(lines);
    	linebox2 =new <String>JComboBox(lines);
    	linebox3 =new <String>JComboBox(lines);
    	linebox4 =new <String>JComboBox(lines);
    	nlinebox1 =new <String>JComboBox(lines);
    	nlinebox2 =new <String>JComboBox(lines);
    	nlinebox3 =new <String>JComboBox(lines);
    	nlinebox4 =new <String>JComboBox(lines);
    	nlinebox5 =new <String>JComboBox(lines);
    	nlinebox6 =new <String>JComboBox(lines);
    	nlinebox7 =new <String>JComboBox(lines);
    	nlinebox8 =new <String>JComboBox(lines);
    	nstation1 =new <String>JComboBox(stname1.toArray(new String[]{}));
    	nstation1 =new <String>JComboBox(stname1.toArray(new String[]{}));
    	nstation2 =new <String>JComboBox(stname1.toArray(new String[]{}));
    	nstation3 =new <String>JComboBox(stname1.toArray(new String[]{}));
    	nstation4 =new <String>JComboBox(stname1.toArray(new String[]{}));
    	nstation5 =new <String>JComboBox(stname1.toArray(new String[]{}));
    	nstation6 =new <String>JComboBox(stname1.toArray(new String[]{}));
    	nstation7 =new <String>JComboBox(stname1.toArray(new String[]{}));
    	nstation8 =new <String>JComboBox(stname1.toArray(new String[]{}));
    	ndist1 = new <String>JComboBox(time);
    	ndist2 = new <String>JComboBox(time);
    	ndist3 = new <String>JComboBox(time);
    	ndist4 = new <String>JComboBox(time);
    	ndist5 = new <String>JComboBox(time);
    	ndist6 = new <String>JComboBox(time);
    	ndist7 = new <String>JComboBox(time);
    	ndist8 = new <String>JComboBox(time);
    	nlinebox1.addItemListener(new ItemListener(){
    		public void itemStateChanged(final ItemEvent e) {
    	        nstation1.removeAllItems();
    	        int j=nlinebox1.getSelectedIndex();
    	        for(int k=0;k<stname[j].length;k++)
    	        	nstation1.addItem(stname[j][k]);
    	      }
    	});
    	nlinebox2.addItemListener(new ItemListener(){
    		public void itemStateChanged(final ItemEvent e) {
    	        nstation2.removeAllItems();
    	        int j=nlinebox2.getSelectedIndex();
    	        for(int k=0;k<stname[j].length;k++)
    	        	nstation2.addItem(stname[j][k]);
    	      }
    	});
    	nlinebox3.addItemListener(new ItemListener(){
    		public void itemStateChanged(final ItemEvent e) {
    	        nstation3.removeAllItems();
    	        int j=nlinebox3.getSelectedIndex();
    	        for(int k=0;k<stname[j].length;k++)
    	        	nstation3.addItem(stname[j][k]);
    	      }
    	});
    	nlinebox4.addItemListener(new ItemListener(){
    		public void itemStateChanged(final ItemEvent e) {
    	        nstation4.removeAllItems();
    	        int j=nlinebox4.getSelectedIndex();
    	        for(int k=0;k<stname[j].length;k++)
    	        	nstation4.addItem(stname[j][k]);
    	      }
    	});
    	nlinebox5.addItemListener(new ItemListener(){
    		public void itemStateChanged(final ItemEvent e) {
    	        nstation5.removeAllItems();
    	        int j=nlinebox5.getSelectedIndex();
    	        for(int k=0;k<stname[j].length;k++)
    	        	nstation5.addItem(stname[j][k]);
    	      }
    	});
    	nlinebox6.addItemListener(new ItemListener(){
    		public void itemStateChanged(final ItemEvent e) {
    	        nstation6.removeAllItems();
    	        int j=nlinebox6.getSelectedIndex();
    	        for(int k=0;k<stname[j].length;k++)
    	        	nstation6.addItem(stname[j][k]);
    	      }
    	});
    	nlinebox7.addItemListener(new ItemListener(){
    		public void itemStateChanged(final ItemEvent e) {
    	        nstation7.removeAllItems();
    	        int j=nlinebox7.getSelectedIndex();
    	        for(int k=0;k<stname[j].length;k++)
    	        	nstation7.addItem(stname[j][k]);
    	      }
    	});
    	nlinebox8.addItemListener(new ItemListener(){
    		public void itemStateChanged(final ItemEvent e) {
    	        nstation8.removeAllItems();
    	        int j=nlinebox8.getSelectedIndex();
    	        for(int k=0;k<stname[j].length;k++)
    	        	nstation8.addItem(stname[j][k]);
    	      }
    	});
    	neighbournumbox=new JComboBox(neighbournums);
    	linenumbox.addItemListener(new ItemListener(){
    		public void itemStateChanged(final ItemEvent e) {
    	        linebox2.setVisible(linenumbox.getSelectedIndex()>=1);
    	        linebox3.setVisible(linenumbox.getSelectedIndex()>=2);
    	        linebox4.setVisible(linenumbox.getSelectedIndex()==3);
    	      }
    	});
    	neighbournumbox.addItemListener(new ItemListener(){
    		public void itemStateChanged(final ItemEvent e) {
    	        nlinebox2.setVisible(neighbournumbox.getSelectedIndex()>=1);
    	        nstation2.setVisible(neighbournumbox.getSelectedIndex()>=1);
    	        ndist2.setVisible(neighbournumbox.getSelectedIndex()>=1);
    	        nlinebox3.setVisible(neighbournumbox.getSelectedIndex()>=2);
    	        nstation3.setVisible(neighbournumbox.getSelectedIndex()>=2);
    	        ndist3.setVisible(neighbournumbox.getSelectedIndex()>=2);
    	        nlinebox4.setVisible(neighbournumbox.getSelectedIndex()>=3);
    	        nstation4.setVisible(neighbournumbox.getSelectedIndex()>=3);
    	        ndist4.setVisible(neighbournumbox.getSelectedIndex()>=3);
    	        nlinebox5.setVisible(neighbournumbox.getSelectedIndex()>=4);
    	        nstation5.setVisible(neighbournumbox.getSelectedIndex()>=4);
    	        ndist5.setVisible(neighbournumbox.getSelectedIndex()>=4);
    	        nlinebox6.setVisible(neighbournumbox.getSelectedIndex()>=5);
    	        nstation6.setVisible(neighbournumbox.getSelectedIndex()>=5);
    	        ndist6.setVisible(neighbournumbox.getSelectedIndex()>=5);
    	        nlinebox7.setVisible(neighbournumbox.getSelectedIndex()>=6);
    	        nstation7.setVisible(neighbournumbox.getSelectedIndex()>=6);
    	        ndist7.setVisible(neighbournumbox.getSelectedIndex()>=6);
    	        nlinebox8.setVisible(neighbournumbox.getSelectedIndex()>=7);
    	        nstation8.setVisible(neighbournumbox.getSelectedIndex()>=7);
    	        ndist8.setVisible(neighbournumbox.getSelectedIndex()>=7);
    	      }
    	});
    	linebox1.setVisible(true);
    	linebox2.setVisible(false);
    	linebox3.setVisible(false);
    	linebox4.setVisible(false);
    	nlinebox2.setVisible(false);
    	nlinebox3.setVisible(false);
    	nlinebox4.setVisible(false);
    	nlinebox5.setVisible(false);
    	nlinebox6.setVisible(false);
    	nlinebox7.setVisible(false);
    	nlinebox8.setVisible(false);
    	nstation2.setVisible(false);
    	nstation3.setVisible(false);
    	nstation4.setVisible(false);
    	nstation5.setVisible(false);
    	nstation6.setVisible(false);
    	nstation7.setVisible(false);
    	nstation8.setVisible(false);
    	ndist2.setVisible(false);
    	ndist3.setVisible(false);
    	ndist4.setVisible(false);
    	ndist5.setVisible(false);
    	ndist6.setVisible(false);
    	ndist7.setVisible(false);
    	ndist8.setVisible(false);
    	savebutton=new JButton("����������ݲ��ڵ�ͼ�Ϸ���վ��");
    	//tf1.setName("վ��");
    	label.setBounds(5,5 , 70, 20);
    	label2.setBounds(5, 30, 70, 20);
    	label3.setBounds(5, 55, 70, 20);
    	label4.setBounds(5, 80, 70, 20);
    	label5.setBounds(100, 105, 70, 20);
    	label6.setBounds(180, 105, 200, 20);
    	label7.setBounds(5, 105, 90, 20);
    	tf1.setBounds(80, 5,200,20);
    	
    	linenumbox.setBounds(80, 30,200,20);
    	linebox1.setBounds(80,55,45,20);
    	linebox2.setBounds(130,55,45,20);
    	linebox3.setBounds(180,55,45,20);
    	linebox4.setBounds(230,55,45,20);
    	nlinebox1.setBounds(5,130,45,20);
    	nlinebox2.setBounds(5,155,45,20);
    	nlinebox3.setBounds(5,180,45,20);
    	nlinebox4.setBounds(5,205,45,20);
    	nlinebox5.setBounds(5,230,45,20);
    	nlinebox6.setBounds(5,255,45,20);
    	nlinebox7.setBounds(5,280,45,20);
    	nlinebox8.setBounds(5,305,45,20);
    	nstation1.setBounds(70,130,120,20);
    	nstation2.setBounds(70,155,120,20);
    	nstation3.setBounds(70,180,120,20);
    	nstation4.setBounds(70,205,120,20);
    	nstation5.setBounds(70,230,120,20);
    	nstation6.setBounds(70,255,120,20);
    	nstation7.setBounds(70,280,120,20);
    	nstation8.setBounds(70,305,120,20);
    	ndist1.setBounds(200, 130, 75, 20);
    	ndist2.setBounds(200, 155, 75, 20);
    	ndist3.setBounds(200, 180, 75, 20);
    	ndist4.setBounds(200, 205, 75, 20);
    	ndist5.setBounds(200, 230, 75, 20);
    	ndist6.setBounds(200, 255, 75, 20);
    	ndist7.setBounds(200, 280, 75, 20);
    	ndist8.setBounds(200, 305, 75, 20);
    	neighbournumbox.setBounds(80, 80,200,20);
    	savebutton.setBounds(5, 330,280, 30);
    	frame1.add(label);
    	frame1.add(label2);
    	frame1.add(label3);
    	frame1.add(label4);
    	frame1.add(label5);
    	frame1.add(label6);
    	frame1.add(label7);
    	frame1.add(tf1);
    	frame1.add(linenumbox);
    	frame1.add(savebutton);
    	frame1.add(linebox1);
    	frame1.add(linebox2);
    	frame1.add(linebox3);
    	frame1.add(linebox4);
    	frame1.add(neighbournumbox);
    	frame1.add(nlinebox1);
    	frame1.add(nlinebox2);
    	frame1.add(nlinebox3);
    	frame1.add(nlinebox4);
    	frame1.add(nlinebox5);
    	frame1.add(nlinebox6);
    	frame1.add(nlinebox7);
    	frame1.add(nlinebox8);
    	frame1.add(nstation1);
    	frame1.add(nstation2);
    	frame1.add(nstation3);
    	frame1.add(nstation4);
    	frame1.add(nstation5);
    	frame1.add(nstation6);
    	frame1.add(nstation7);
    	frame1.add(nstation8);
    	frame1.add(ndist1);
    	frame1.add(ndist2);
    	frame1.add(ndist3);
    	frame1.add(ndist4);
    	frame1.add(ndist5);
    	frame1.add(ndist6);
    	frame1.add(ndist7);
    	frame1.add(ndist8);
    	savebutton.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e) {
    			frame1.setVisible(false);
    			String tname=tf1.getText();
    			int tlinenum=linenumbox.getSelectedIndex()+1;
    			int [] tline=new int[tlinenum];
    			for(int i=0;i<tlinenum;i++){
    				if(i==0)
    					tline[i]=linebox1.getSelectedIndex()+1;
    				else if(i==1)
    					tline[i]=linebox2.getSelectedIndex()+1;
    				else if(i==2)
    					tline[i]=linebox3.getSelectedIndex()+1;
    				else if(i==3)
    					tline[i]=linebox4.getSelectedIndex()+1;
    				}
    			newstation=new Station(tname,addx-8,addy-31,tlinenum,tline);
    			newway=new Way[neighbournumbox.getSelectedIndex()+1];
    			for(int i=0;i<newway.length;i++){
    				if(i==0)
    					newway[i]=new Way(newstation,findstname(nstation1.getSelectedItem().toString()),ndist1.getSelectedIndex()+1);
    				else if(i==1)
    					newway[i]=new Way(newstation,findstname(nstation2.getSelectedItem().toString()),ndist2.getSelectedIndex()+1);
    				else if(i==2)
    					newway[i]=new Way(newstation,findstname(nstation3.getSelectedItem().toString()),ndist3.getSelectedIndex()+1);
    				else if(i==3)
    					newway[i]=new Way(newstation,findstname(nstation4.getSelectedItem().toString()),ndist4.getSelectedIndex()+1);
    				else if(i==4)
    					newway[i]=new Way(newstation,findstname(nstation5.getSelectedItem().toString()),ndist5.getSelectedIndex()+1);
    				else if(i==5)
    					newway[i]=new Way(newstation,findstname(nstation6.getSelectedItem().toString()),ndist6.getSelectedIndex()+1);
    				else if(i==6)
    					newway[i]=new Way(newstation,findstname(nstation7.getSelectedItem().toString()),ndist7.getSelectedIndex()+1);
    				else if(i==7)
    					newway[i]=new Way(newstation,findstname(nstation8.getSelectedItem().toString()),ndist8.getSelectedIndex()+1);
    				}
    			add=true;
    			refresh();
    			addstart=false;
            }
    	});
		}
	public void refresh(){//����station��way
		if(add){//���վ��
			System.out.println("�������վ��"+newstation.getstname());
			Station []tstation=new Station[stationnum+1];
			Way [] tway=new Way[waynum+newway.length];
			for(int i=0;i<stationnum;i++)
				tstation[i]=new Station(station[i].getstname(),station[i].getstx()-8,station[i].getsty()-31,
						station[i].getstlinenum(),station[i].getstline());
			tstation[stationnum]=new Station(newstation.getstname(),newstation.getstx()-8,newstation.getsty()-31,
					newstation.getstlinenum(),newstation.getstline());
			stationnum++;
			for(int i=0;i<waynum;i++)
				tway[i]=new Way(way[i].getwstation1(),way[i].getwstation2(),way[i].gettime());
			for(int i=0;i<newway.length;i++){
				tway[i+waynum]=new Way(newway[i].getwstation1(),newway[i].getwstation2(),newway[i].gettime());
				//tway[i+waynum].setLine(newway[i].getwstation1().getstx()-8,newway[i].getwstation1().getsty()-31);
				}
			waynum=waynum+newway.length;
			station=new Station[stationnum];
			station=tstation.clone();
			way=new Way[waynum];
			way=tway.clone();
			for(int i=0;i<waynum;i++){
				System.out.println("("+way[i].getwx1()+","+way[i].getwy1()+")---("+way[i].getwx2()+","+way[i].getwy2()+")");
				}
			addready=true;
			}
		else if(remove){//ɾ��վ��
			Station []tstation=new Station[stationnum-1];
			Way [] tway=new Way[waynum-abandonway.length];
			int j=0;
			for(int i=0;i<stationnum;i++){
				if(!(station[i].getstname().equals(abandonstation.getstname()))){
					tstation[j++]=new Station(station[i].getstname(),station[i].getstx()-8,station[i].getsty()-31,
							station[i].getstlinenum(),station[i].getstline());
					}
				}
			j=0;
			for(int i=0;i<waynum;i++){
				boolean exist=false;
				for(int k=0;k<abandonway.length;k++){
					if((way[i].getwname1().equals(abandonway[k].getwname1()) && 
							way[i].getwname2().equals(abandonway[k].getwname2()))||
							(way[i].getwname1().equals(abandonway[k].getwname2()) && 
							way[i].getwname2().equals(abandonway[k].getwname1()))
							){
						exist=true;
						}
					}
				//System.out.println("waynum-abandonway.length="+(waynum-abandonway.length)+" j="+j);
				if(exist==false)
					tway[j++]=new Way(way[i].getwstation1(),way[i].getwstation2(),way[i].gettime());
				}
			stationnum--;
			waynum=waynum-abandonway.length;
			station=new Station[stationnum];
			way=new Way[waynum];
			station=new Station[stationnum];
			station=tstation.clone();
			way=new Way[waynum];
			way=tway.clone();
			}
		else if(nline){
			Station []tstation=new Station[stationnum+linest.length];
			Way [] tway=new Way[waynum+newway.length];
			for(int i=0;i<stationnum;i++)
				tstation[i]=new Station(station[i].getstname(),station[i].getstx()-8,station[i].getsty()-31,
						station[i].getstlinenum(),station[i].getstline());
			for(int i=stationnum;i<stationnum+linest.length;i++)
				tstation[i]=new Station(linest[i-stationnum].getstname(),linest[i-stationnum].getstx()-8,
						linest[i-stationnum].getsty()-31,
						linest[i-stationnum].getstlinenum(),linest[i-stationnum].getstline());
			stationnum=stationnum+linest.length;
			for(int i=0;i<waynum;i++)
				tway[i]=new Way(way[i].getwstation1(),way[i].getwstation2(),way[i].gettime());
			for(int i=0;i<newway.length;i++)
				tway[i+waynum]=new Way(newway[i].getwstation1(),newway[i].getwstation2(),newway[i].gettime());
			waynum=waynum+newway.length;
			station=new Station[stationnum];
			station=tstation.clone();
			way=new Way[waynum];
			way=tway.clone();
			}
		clearneeded=true;
		paint(getGraphics());
		
		}
	public Station findstname(String name){
		for(int i=0;i<stationnum;i++){
			if(station[i].getstname().equals(name))
				return station[i];
			}
		return null;
		}
	public void shortcut(String starts,String ends){//�������·������ʾ
		caldone=false;
		this.path=new int[stationnum][stationnum];  
        this.dist=new int[stationnum][stationnum];
		int [][]matrix =new int [stationnum][stationnum];
		int begin=0;  
        int end=stationnum-1;
		for(int i=0;i<stationnum;i++){
			if(starts.equals(station[i].getstname()))
				begin=i;
			else if(ends.equals(station[i].getstname()))
				end=i;
			for(int j=0;j<stationnum;j++){
				matrix[i][j]=INF;
				}
			}
		for(int i=0;i<waynum;i++){
			int s1=0,s2=0;
			for(int j=0;j<stationnum;j++){
				if(station[j].getstname().equals(way[i].getwname1())){
					s1=j;
					}
				else if(station[j].getstname().equals(way[i].getwname2())){
					s2=j;
					}
				}
			matrix[s1][s2]=matrix[s2][s1]=way[i].gettime();
			}  
        findCheapestPath(begin,end,matrix);  
        ArrayList<Integer> list=result;  
        System.out.println(begin+" to "+end+",the cheapest path is:");  
        System.out.println(list.toString());
        for(int i=0;i<waynum;i++)
        	way[i].chosen=false;
        caldone=true;
        pathstation=new int[list.size()];
        for(int i=0;i<list.size();i++){
        	pathstation[i]=list.get(i);
        	}
        pathway=new int[list.size()];
        for(int i=1;i<list.size();i++){
        	for(int j=0;j<waynum;j++){
	        	if((station[list.get(i-1)].getstname().equals(way[j].getwname1()) && 
	        		station[list.get(i)].getstname().equals(way[j].getwname2()))||
	        		(station[list.get(i-1)].getstname().equals(way[j].getwname2()) && 
	    	       	station[list.get(i)].getstname().equals(way[j].getwname1()))){
	        		way[j].chosen=true;
	        		pathway[i]=j;
	        		}
	        	}
        	System.out.println(station[list.get(i)].getstname());
        	}
        showpath=true;
        System.out.println(dist[begin][end]); 
		caldone=false;
		startst=null;
		endst=null;
		result=new ArrayList<Integer>();
		}
	public  void findCheapestPath(int begin,int end,int[][] matrix){//���������㷨  
        floyd(matrix);  
        result.add(begin);  
        findPath(begin,end);  
        result.add(end);  
    }  
      
    public void findPath(int i,int j){  //���������㷨  
        int k=path[i][j];  
        if(k==-1)return;  
        findPath(i,k);  
        result.add(k);  
        findPath(k,j);  
    }  
    public  void floyd(int[][] matrix){  //���������㷨  
        int size=matrix.length;  
        //initialize dist and path  
        for(int i=0;i<size;i++){  
            for(int j=0;j<size;j++){  
                path[i][j]=-1;  
                dist[i][j]=matrix[i][j];  
            }  
        }  
        for(int k=0;k<size;k++){  
            for(int i=0;i<size;i++){  
                for(int j=0;j<size;j++){  
                    if(dist[i][k]!=INF&&  
                        dist[k][j]!=INF&&  
                        dist[i][k]+dist[k][j]<dist[i][j]){//dist[i][k]+dist[k][j]>dist[i][j]-->longestPath  
                        dist[i][j]=dist[i][k]+dist[k][j];  
                        path[i][j]=k;  
                    }  
                }  
            }  
        }  
          
    }  
	public static boolean isNumeric(String str){//�ж��ַ����Ƿ�Ϊ����
		for (int i = 0; i < str.length(); i++){
			//System.out.println(str.charAt(i));
			if (!Character.isDigit(str.charAt(i))){
			  	return false;
			  	}
		  	}
		return true;
		}
	
	
	public void paint(Graphics gg){
		
		Graphics2D g=(Graphics2D)gg;
		float thick=8.0f;
	    float thin=1.0f;
		if(clearneeded){
			clearneeded=false;
			g.setColor(SystemColor.control);
			g.fillRect(5, 50, 800, 800);
			}
			
	    
		/*������·*/
		
	    g.setStroke((Stroke) new BasicStroke(thick, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
	    for(int i=0;i<waynum;i++){
	    	if(way[i].chosen==false){
	    		g.setColor(Color.gray);
	    		g.drawLine(way[i].getwx1(), way[i].getwy1(),way[i].getwx2(), way[i].getwy2());
				}
			}
	    for(int i=0;i<waynum;i++){
	    	if(way[i].chosen==true){
	    		g.setColor(way[i].getwcolor());
	    		g.drawLine(way[i].getwx1(), way[i].getwy1(),way[i].getwx2(), way[i].getwy2());
				}
	    	}
	    /*����վ��*/
	    int width=10;
	    int height=10;
	    g.setStroke((Stroke) new BasicStroke(thin, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
	    
	    for(int i=0;i<stationnum;i++){
		    g.setColor(Color.white);
		    //System.out.println("stationnum = "+stationnum+" i = "+i+"("+station[i].getstx()+"," +station[i].getsty()+")");
	    	g.fillOval(station[i].getstx()-5, station[i].getsty()-5, width, height);
	    	g.setColor(java.awt.SystemColor.controlShadow); // ���ñ߿���ɫ
	    	g.drawOval(station[i].getstx()-5, station[i].getsty()-5, width, height);
	    	}
	    
	    /*��ӡ�Ҳ��ɫ��ɫ*/
	    g.setColor(Color.LIGHT_GRAY);
	    g.fillRect(700, 50, 600, 800);
	    
	    /*��ӡ��ǰ��վ����*/
	    g.setColor(Color.BLACK);
	    g.setFont(new Font("����",Font.BOLD ,30));
	    if(stationplace)
	    	g.drawString("Station:"+SName,750 ,100 );
	    else
	    	g.drawString("Station:",750 ,100 );
	    
	    
	    /*��ӡʼĩվ����*/
		g.setColor(Color.BLACK);
	    g.setFont(new Font("����",Font.BOLD ,30));
	    if(startplace==1 && startst!=null)
	    	g.drawString("Start:"+startst,750 ,200 );
	    else if(startplace==0)
	    	g.drawString("Start:���ڵ�ͼ�ϵ������վ",750 ,200 );
	    if(endplace==0)
	    	g.drawString("End:���ڵ�ͼ�ϵ���յ�վ",750 ,250 );
	    else if(endplace==1 && endst!=null)
	    	g.drawString("End:"+endst,750 ,250 );
	    
	    /*��ӡɾ��վ������*/
	    if(removeplace==0)
	    	g.drawString("ɾ��վ�����ڵ�ͼ�ϵ��Ҫɾ����վ",750 ,200 );
	    else if(removeplace==1)
	    	g.drawString("ɾ��վ��"+removest,750 ,200 );
	    
	    
	    /*��ӡ·�߻���վ����*/
	    if(showpath){
	    	g.drawString("������"+station[pathstation[0]].getstname()+" ����"+way[pathway[1]].getwline()+"����", 
	    			750, 300);
	    	int interchange=1;
	    	for(int i=1;i<pathway.length-1;i++){
	    		boolean istrans=true;
	    		for(int j=0;j<station[pathstation[i-1]].getstlinenum();j++){
	    			for(int k=0;k<station[pathstation[i]].getstlinenum();k++){
	    				for(int l=0;l<station[pathstation[i+1]].getstlinenum();l++){
	    					if(station[pathstation[i-1]].getstline()[j]==station[pathstation[i]].getstline()[k] && 
	    							station[pathstation[i-1]].getstline()[j]==station[pathstation[i+1]].getstline()[l])
	    						istrans=false;
	    					}
	    				}
	    			}
	    		System.out.println(station[pathstation[i-1]].getstname()+" "+way[pathway[i]].getwline()+"���� "
	    			+station[pathstation[i]].getstname());
	    		if(istrans && way[pathway[i]].getwline()!=way[pathway[i+1]].getwline()){//�Ƿ�Ϊ����վ
	    			g.drawString("���ɣ�"+station[pathstation[i]].getstname()+" ����"+way[pathway[i+1]].getwline()+"����", 
	    	    			750, 300+interchange*50);
	    			interchange++;
	    			}
	    		}
			g.drawString("���"+station[pathstation[pathway.length-1]].getstname(), 
	    			750, 300+interchange*50);
	    	}
	}
	public static void main(String[] args) throws IOException 
	{
		String filename1="station.txt";
		String filename2="way.txt";
		SubwayMap map=new SubwayMap(filename1,filename2);
	
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO �Զ����ɵķ������
		int xx=e.getX();
		int yy=e.getY();
		
		//System.out.println("pointing!!!pointing!!!pointing!!!pointing!!!");
		if(viewing){
			if(addready){
				Graphics2D g=(Graphics2D)getGraphics();
				float thick=15.0f;
				float thin =8.0f;
				
				station[stationnum-1].setPosition(xx-8, yy-31);
				for(int i=0;i<newway.length;i++){
					g.setColor(SystemColor.control);
					g.setStroke((Stroke) new BasicStroke(thick, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
					g.drawLine(addx+(addx-way[i+waynum-newway.length].getwx2())/20,
							   addy+(addy-way[i+waynum-newway.length].getwy2())/20, 
							   way[i+waynum-newway.length].getwx2(), 
							   way[i+waynum-newway.length].getwy2());
					g.setColor(way[i+waynum-newway.length].getwcolor());
					g.setStroke((Stroke) new BasicStroke(thin, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
					//System.out.println("("+xx+","+yy+")---("+way[i+waynum-newway.length].getwx2()+","+way[i+waynum-newway.length].getwy2()+")");
					g.drawLine(xx,
							   yy, 
							   way[i+waynum-newway.length].getwx2(), 
							   way[i+waynum-newway.length].getwy2());
					g.setColor(Color.white);
				    g.fillOval(xx-5, yy-5, 10, 10);
			    	g.setColor(java.awt.SystemColor.controlShadow); // ���ñ߿���ɫ
			    	g.setStroke((Stroke) new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
			    	g.drawOval(xx-5, yy-5, 10, 10);
					way[i+waynum-newway.length].setLine(xx, yy);
					}
				
				if(addx-800<5&&800-addx<5&&addy-800<5&&800-addy<5)
					clearneeded=true;
				paint(getGraphics());
				addx=xx;
				addy=yy;
				//clear((Graphics2D)getGraphics());
				//paint((Graphics2D)getGraphics());
				}
			else{
				boolean pointing=false;
				int size=5;
				for(int i=0;i<stationnum;i++){
					if(xx-station[i].getstx()<=size && station[i].getstx()-xx<=size && yy-station[i].getsty()<=size && station[i].getsty()-yy<=size){
						pointing=true;
						if(stationplace==false){
							stationplace=true;
							SName=station[i].getstname();
							paint(getGraphics());
							break;
							}
						}
					}
				if(stationplace==true && !pointing){
					stationplace=false;
					paint(getGraphics());
					} 
				}
		    }
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO �Զ����ɵķ������
			}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO �Զ����ɵķ������
			}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO �Զ����ɵķ������
		//System.out.println("start="+"   end = ");
		addready=false;
		add=false;
		
		int xx=arg0.getX();
		int yy=arg0.getY();
		
		if(calculating || remove){
			boolean pointing=false;
			int size=5;
			int i;
			for(i=0;i<stationnum;i++){
				if(xx-station[i].getstx()<=size && station[i].getstx()-xx<=size && yy-station[i].getsty()<=size && station[i].getsty()-yy<=size){
					pointing=true;
					break;
					}
				}
			if(pointing){
				if(startplace==0 && startst==null){
					startst=station[i].getstname();
					startplace=1;
					endplace=0;
					paint(getGraphics());
					}
				else if(endplace==0 && endst==null && !startst.equals(station[i].getstname())){
					endst=station[i].getstname();
					endplace=1;
					paint(getGraphics());
					}
					
			    if(startplace==1 && endplace==1 && startst!=null && endst!=null){
			    	shortcut(startst,endst);
			    	paint(getGraphics());
					}
			    if(removeplace==0){
			    	removest=station[i].getstname();
			    	removeplace=1;
			    	removestation();
			    	}
		    	} 
			}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO �Զ����ɵķ������
		
	}
}
