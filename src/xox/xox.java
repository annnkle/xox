package xox;
import java.io.*;
import java.nio.*;
import java.awt.*;
import java.awt.event.*;  
import java.util.Random;
import java.util.Vector;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

import javafx.scene.paint.Color;
public class xox {
	static int n = 3;
	static int winPoint = 3;
	static int i = 0;
	static int j = 0;
	static int x = 10;
	static int y = 55;
	static int turn = 0;
	static int mode;
	static int time = 0;
	static int[][] state= new int[n][n];
	static int[][] spacesp= new int[n][n];
	static Vector<boardTile> tiles = new Vector<>();
	static ImageIcon blankTile, oIcon, xIcon, oIconCrossed, xIconCrossed, activeX, activeO, inactiveX, inactiveO;
	static Vector<JLabel> turnSigns = new Vector<>();
	static JTextArea clock = new JTextArea();
	public static void main(String[] args) throws IOException {
		frame();
}
public static JFrame frame() {
	JFrame frame = new JFrame("XOX");

	JDialog mode_chooser = new JDialog();
	mode_chooser.setSize(240, 160);
	mode_chooser.setTitle("Game mode");
	ButtonGroup modeChoices = new ButtonGroup();
	JRadioButton ai = new JRadioButton("vs AI", true);
	JRadioButton learn = new JRadioButton("vs AI(learn mode)");
	JRadioButton frend = new JRadioButton("vs friend");

	modeChoices.add(ai);modeChoices.add(frend);modeChoices.add(learn);
	ActionListener modec = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==ai) {
				mode = 0;
				mode_chooser.setVisible(false);
			}
			if(e.getSource()==learn) {
				mode = 1;
				mode_chooser.setVisible(false);
			}
			if(e.getSource()==frend) {
				mode = 2;
				mode_chooser.setVisible(false);
			}
		}
	};
	ai.addActionListener(modec);learn.addActionListener(modec);frend.addActionListener(modec);

	mode_chooser.add(ai, BorderLayout.WEST);mode_chooser.add(frend, BorderLayout.EAST);mode_chooser.add(learn, BorderLayout.CENTER);
	mode_chooser.setVisible(true);
	mode_chooser.setAlwaysOnTop(true);

	BackgroundMenuBar menuBar=new BackgroundMenuBar();

	JMenu menu = new JMenu("Size");
	JMenu menu1 = new JMenu("Win conditions");
	JMenu menu2 = new JMenu("Theme");
	JMenu menu3 = new JMenu("Mode");
	JMenuItem size1, size2, size3, size4, winCondition1, winCondition2, winCondition3, theme7;
	JRadioButtonMenuItem theme1, theme2, theme3, theme4, theme5, theme6;
	JRadioButtonMenuItem modeAI, modeAILearn, modeFriend;


  size1=new JMenuItem("3x3");  size2=new JMenuItem("4x4"); size3=new JMenuItem("5x5");size4=new JMenuItem("6x6");

  final JOptionPane optionPane = new JOptionPane(
  "WARNING. Current game will be lost. Continue?",
	JOptionPane.QUESTION_MESSAGE,
	JOptionPane.YES_NO_OPTION);

  ActionListener al = new ActionListener() {
  	public void actionPerformed(ActionEvent e) {
  		int o = JOptionPane.showConfirmDialog(null, "WARNING. The current game will be lost. Continue?", "Warning",
  					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

  		if(o==JOptionPane.YES_OPTION) {
  		if(e.getSource()==size1)
    	n = 3;
    	if(e.getSource()==size2)
    	n = 4;
    	if(e.getSource()==size3)
    	n = 5;
    	if(e.getSource()==size4)
    	n = 6;

    	if(winPoint > n) {
  			winPoint = n;
  		}
    	x = 10;
    	y = 55;
    	state = new int [n][n];
    	tiles.clear();
  		resetButton();
  		frame.setVisible(false);
    	frame.dispose();
    	frame();
  		}
  	}
  };
  size1.addActionListener(al);size2.addActionListener(al);size3.addActionListener(al);size4.addActionListener(al);

  winCondition1=new JMenuItem("3 in row");winCondition2=new JMenuItem("4 in row");winCondition3=new JMenuItem("5 in row");
  ActionListener al1 = new ActionListener() {
  	public void actionPerformed(ActionEvent e) {
  		int o = JOptionPane.showConfirmDialog(null, "WARNING. The current game will be lost. Continue?", "Warning",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
  		if(o==JOptionPane.YES_OPTION) {
  			if(e.getSource()==winCondition1)
  		    	winPoint = 3;
		    if(e.getSource()==winCondition2)
		    	if(n > 3) {
		    		winPoint = 4;
		    	}
		    	else {
		    		JOptionPane.showMessageDialog(frame,"Setting not allowed for current board size.","Error",JOptionPane.WARNING_MESSAGE);
		    	}
		    if(e.getSource()==winCondition3)
		    	if(n > 4) {
		    		winPoint = 5;
		    	}
	    	else {
	    		JOptionPane.showMessageDialog(frame,"Setting not allowed for current board size.","Error",JOptionPane.WARNING_MESSAGE);
	    	}
    		resetButton();
  		}

  	}
   };

  winCondition1.addActionListener(al1);winCondition2.addActionListener(al1);winCondition3.addActionListener(al1);
  if(n == 3) {
  	winCondition2.setEnabled(false);
  	winCondition3.setEnabled(false);
  }
  if(n == 4) {
  	winCondition3.setEnabled(false);
  }
  ButtonGroup themes = new ButtonGroup();

  theme1=new JRadioButtonMenuItem("Tech", true);theme2=new JRadioButtonMenuItem("Notebook");theme3=new JRadioButtonMenuItem("Concrete");theme4=new JRadioButtonMenuItem("Chalk");
  theme5=new JRadioButtonMenuItem("Antiqua");theme6=new JRadioButtonMenuItem("TechWarm");theme7=new JMenuItem("Your theme...");
  themes.add(theme1);themes.add(theme2);themes.add(theme3);themes.add(theme4);themes.add(theme5);themes.add(theme6);

  JLabel activeXlab = new JLabel();
  JLabel activeOlab = new JLabel();

  turnSigns.addElement(activeXlab);turnSigns.addElement(activeOlab);

  JButton reset = new JButton("RESET");
	if(n == 3) {
		reset.setBounds(110, 360, 100, 50);
		}
	if(n == 4) {
		reset.setBounds(160, 460, 100, 50);
		}
	if(n == 5) {
		reset.setBounds(210, 560, 100, 50);
		}
	if(n == 6) {
		reset.setBounds(260, 660, 100, 50);
		}
	reset.addActionListener(new ActionListener() {
		 public void actionPerformed(ActionEvent e){
			 resetButton();
		 }
	 });

  setTheme(theme1, theme1, theme2, theme3, theme4, theme5, theme6, frame, menuBar, menu, menu1, menu2, menu3, reset, activeXlab, activeOlab);

  activeOlab.setIcon(inactiveO);
  activeXlab.setIcon(inactiveX);

  ActionListener al2 = new ActionListener() {
	public void actionPerformed(ActionEvent e) {
		int o = JOptionPane.showConfirmDialog(null, "WARNING. The current game will be lost. Continue?", "Warning",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	//1 is O, 2 is X
		if(o==JOptionPane.YES_OPTION) {
			if(e.getSource()==theme1) {
				setTheme(theme1, theme1, theme2, theme3, theme4, theme5, theme6, frame, menuBar, menu, menu1, menu2, menu3, reset, activeXlab, activeOlab);
			}
    	if(e.getSource()==theme2) {
    		setTheme(theme2, theme1, theme2, theme3, theme4, theme5, theme6, frame, menuBar, menu, menu1, menu2, menu3, reset, activeXlab, activeOlab);
    	}
    	if(e.getSource()==theme3) {
    		setTheme(theme3, theme1, theme2, theme3, theme4, theme5, theme6, frame, menuBar, menu, menu1, menu2, menu3, reset, activeXlab, activeOlab);
    	}
    	if(e.getSource()==theme4) {
    		setTheme(theme4, theme1, theme2, theme3, theme4, theme5, theme6, frame, menuBar, menu, menu1, menu2, menu3, reset, activeXlab, activeOlab);
    	}
    	if(e.getSource()==theme5) {
    		setTheme(theme5, theme1, theme2, theme3, theme4, theme5, theme6, frame, menuBar, menu, menu1, menu2, menu3, reset, activeXlab, activeOlab);
    	}
    	if(e.getSource()==theme6) {
    		setTheme(theme6, theme1, theme2, theme3, theme4, theme5, theme6, frame, menuBar, menu, menu1, menu2, menu3, reset, activeXlab, activeOlab);
    	}
    	if(e.getSource()==theme7) {
  		//URTHEME!
  		JDialog d = new JDialog(frame , "Choose your theme!", true);
  		JLabel l1,l2,l3,l4,l5, l6, l7;
  		JPanel pd1, pd2, pd3, pd4, pd5, pd6, pd7;
  		JButton setb = new JButton("SET");
  		setb.setSize(30,20);
  		JTabbedPane tp=new JTabbedPane();
  		tp.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);

  		//PANELS
  		pd1 = new JPanel();pd2 = new JPanel();pd3 = new JPanel();pd4 = new JPanel();pd5 = new JPanel();pd5 = new JPanel(); pd6 = new JPanel(); pd7 = new JPanel();
  		pd1.setLayout(new FlowLayout(FlowLayout.CENTER));pd2.setLayout(new FlowLayout(FlowLayout.CENTER));
  		pd3.setLayout(new FlowLayout(FlowLayout.CENTER));pd4.setLayout(new FlowLayout(FlowLayout.CENTER));pd5.setLayout(new FlowLayout(FlowLayout.CENTER));

  		//LABELS
    	l1=new JLabel("Pick a background color.");l2=new JLabel("Pick menu bar color.");l3=new JLabel("Pick blank image.");l4=new JLabel("Pick O image.");
    	l5=new JLabel("Pick X image."); l6=new JLabel("Pick O endgame image.");l7=new JLabel("Pick X endgame image.");

    	//COLOR CHOOSER
    	JColorChooser bkg_c = new JColorChooser();JColorChooser mb_c = new JColorChooser();

    	//FILE CHOOSER
    	JFileChooser blank = new JFileChooser(); JFileChooser Xi = new JFileChooser();JFileChooser Xiend = new JFileChooser();JFileChooser Oi = new JFileChooser();JFileChooser Oiend = new JFileChooser();
    	//blank.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);Xi.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);Oi.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    	//PANEL DECLARATIONS
    	pd1.add(l1); pd1.add(bkg_c);
    	pd2.add(l2); pd2.add(mb_c);
    	pd3.add(l3); pd3.add(blank);
    	pd4.add(l4); pd4.add(Oi);
    	pd5.add(l5); pd5.add(Xi);
    	pd6.add(l6); pd6.add(Oiend);
    	pd7.add(l7); pd7.add(Xiend);
    	//TABBED PANE
    	tp.add("Background color", pd1);
  		tp.add("Menu bar color", pd2);
  		tp.add("Blank image", pd3);
  		tp.add("O image", pd4);
  		tp.add("X image", pd5);
  		tp.add("O end image", pd6);
  		tp.add("X end image", pd7);
  		//*********************
  		// ACTION LISTENERS!
  		//*********************
  		ActionListener theme = new ActionListener() {
  			public void actionPerformed(ActionEvent e){
  				java.awt.Color c = bkg_c.getColor();
  				frame.getContentPane().setBackground(c);
  				java.awt.Color co = mb_c.getColor();
  				menuBar.setColor(co);
  				blankTile = new ImageIcon(blank.getSelectedFile().getPath());
  				oIcon = new ImageIcon(Oi.getSelectedFile().getPath());
  				xIcon = new ImageIcon(Xi.getSelectedFile().getPath());
  				oIconCrossed = new ImageIcon(Oiend.getSelectedFile().getPath());
  				xIconCrossed = new ImageIcon(Xiend.getSelectedFile().getPath());
  				resetButton();
  			}
  		 };
  		 setb.addActionListener(theme);
	  		d.add(tp, BorderLayout.NORTH);
	  		d.add(setb, BorderLayout.SOUTH);
	    	d.setSize(700, 500);
	  		d.setVisible(true);
	    	}
  		 }
    	}
      };
  theme1.addActionListener(al2);theme2.addActionListener(al2);theme3.addActionListener(al2);theme4.addActionListener(al2);theme5.addActionListener(al2);theme6.addActionListener(al2);theme7.addActionListener(al2);

  modeAI=new JRadioButtonMenuItem("vs AI");modeAILearn=new JRadioButtonMenuItem("vs AI(learn mode)");modeFriend=new JRadioButtonMenuItem("vs friend");
  ButtonGroup modes = new ButtonGroup();
	modes.add(modeAI);modes.add(modeAILearn);modes.add(modeFriend);
  ActionListener modeChooser = new ActionListener() {
  	public void actionPerformed(ActionEvent e) {
  		int o = JOptionPane.showConfirmDialog(null, "WARNING. The current game will be lost. Continue?", "Warning",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
  		if(o==JOptionPane.YES_OPTION) {
    		if(e.getSource() == modeAI) {
    			mode = 0;
    			resetButton();
    		}
    		if(e.getSource() == modeAILearn) {
    			mode = 1;
    			resetButton();
    		}
    		if(e.getSource() == modeFriend) {
    			mode = 2;
    			resetButton();
    		}
  		}
  	 }
    };

  modeAI.addActionListener(modeChooser); modeAILearn.addActionListener(modeChooser); modeFriend.addActionListener(modeChooser);

  menu.add(size1); menu.add(size2); menu.add(size3);menu.add(size4);
  menu1.add(winCondition1); menu1.add(winCondition2); menu1.add(winCondition3);
  menu2.add(theme1);menu2.add(theme2);menu2.add(theme3);menu2.add(theme4);menu2.add(theme5);menu2.add(theme6);menu2.add(theme7);
  menu3.add(modeAI);menu3.add(modeAILearn);menu3.add(modeFriend);
  menuBar.add(menu);menuBar.add(menu1);menuBar.add(menu2);menuBar.add(menu3);
  menuBar.setBounds(0, 0, 850, 40);
  frame.add(menuBar);

  JLabel xTurn = new JLabel("           X turn");
  JLabel oTurn = new JLabel("           O turn");

  if(n == 3) {
      xTurn.setBounds(350, 85, 100, 15);
      activeXlab.setBounds(350, 100, 100, 100);
      oTurn.setBounds(350, 200, 100, 15);
      activeOlab.setBounds(350, 215, 100, 100);
  }
  if(n == 4) {
      xTurn.setBounds(450, 85, 100, 15);
      activeXlab.setBounds(450, 100, 100, 100);
      oTurn.setBounds(450, 200, 100, 15);
      activeOlab.setBounds(450, 215, 100, 100);
  }
  if(n == 5) {
      xTurn.setBounds(550, 85, 100, 15);
      activeXlab.setBounds(550, 100, 100, 100);
      oTurn.setBounds(550, 200, 100, 15);
      activeOlab.setBounds(550, 215, 100, 100);
  }
  if(n == 6) {
      xTurn.setBounds(650, 85, 100, 15);
      activeXlab.setBounds(650, 100, 100, 100);
      oTurn.setBounds(650, 200, 100, 15);
      activeOlab.setBounds(650, 215, 100, 100);
  }

  frame.add(activeOlab);
  frame.add(oTurn);
  frame.add(activeXlab);
  frame.add(xTurn);
  JPanel p1 = new JPanel();
  JPanel p2 = new JPanel();

  // TIMER
	javax.swing.Timer timer = new javax.swing.Timer(1000, new ActionListener() {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		clock.setText(String.valueOf(time));
		time ++;
	}
	});
	clock.setEditable(false);
	if(n == 3) {
		clock.setBounds(380, 325, 40, 20);
	}
	if(n == 4) {
		clock.setBounds(480, 325, 40, 20);
	}
	if(n == 5) {
		clock.setBounds(580, 325, 40, 20);
	}
	if(n == 6) {
		clock.setBounds(680, 325, 40, 20);
	}

	frame.add(clock);

	//set game board
	for(i = 0; i < n; i++) {
		for(j= 0; j < n; j++) {
			boardTile tile = new boardTile();
			tile.xLocation = j;
			tile.yLocation = i;
			tile.setBounds(x, y, 100, 100);
			tile.setOpaque(true);
			tile.setIcon(blankTile);
			tiles.add(tile);
			x+=100;
			frame.add(tile);
			//what happens after clicking a tile; some game logic
		 	tile.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					if(turn == 0) {
						timer.start();
					}
					if(tile.clicked == 1 || turn == n*n) {
					 //do nothing
					}
					else if(turn%2 == 0) {
					  tile.setIcon(oIcon);
					  turn++;
					  tile.clicked = 1;
					  state[tile.yLocation][tile.xLocation] = 1;
					  activeXlab.setIcon(activeX);
					  activeOlab.setIcon(inactiveO);
					}
					else {
					 if(mode == 2) {
					 tile.setIcon(xIcon);
					 turn++;
					 tile.clicked = 1;
					 state[tile.yLocation][tile.xLocation] = 2;
					 activeXlab.setIcon(inactiveX);
					 activeOlab.setIcon(activeO);
					 }
				 	}
				 // Checking if there's winner
				 int c = winnerChk();
				 if(c>0) {
					 // Reasigning turn variable to make not possible to click buttons
					 turn = n * n;
					 timer.stop();
					 if(c == 2) {
							int a = JOptionPane.showConfirmDialog(frame,"X won, restart?");
							if(a==JOptionPane.YES_OPTION){
							   resetButton();
							}
						}
					else if(c == 1) {
							try {
								moveCleaner();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							int a =JOptionPane.showConfirmDialog(frame,"O won, restart?");
							if(a==JOptionPane.YES_OPTION){
							    resetButton();
							}
					}
				 }
				 else if(c == 0 && turn == (n*n)) {
					 timer.stop();
					 try {
						theTactic();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					 int a =JOptionPane.showConfirmDialog(frame,"It's a tie! Restart?");
						if(a==JOptionPane.YES_OPTION){
						    resetButton();
						}
				 }
				 //
				if(turn%2 != 0 && turn != n*n && (mode == 1 || mode == 0)) {
					if(mode == 0) {
						try {
							AI();
							c = winnerChk();
							if(c == 2) {
								theTactic();
								int a =JOptionPane.showConfirmDialog(frame,"X won, restart?");
								if(a==JOptionPane.YES_OPTION){
								    resetButton();
								}
							}
						}catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if(mode == 1) {
						try {
							AI_learn();
							c = winnerChk();
							if(c == 2) {
								theTactic();
								int a =JOptionPane.showConfirmDialog(frame,"X won, restart?");
								if(a==JOptionPane.YES_OPTION){
								    resetButton();
								}
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		 });

		}
			x = 10;
			y+=100;
		}
		frame.add(reset);

		if(n == 3) {
			frame.setSize(500, 500);
		}
		else if(n == 4) {
			frame.setSize(600, 600);
		}
		else if(n == 5) {
			frame.setSize(700, 700);
		}
		else if(n == 6) {
			frame.setSize(800, 800);
		}

		frame.setLayout(null);
		frame.setVisible(true);

		return frame;

	}

//*****************************************************************************************************************************************************************************************************************************************
	//RESET
//***************************************************************************************************************************************************************************************************************************************
	protected static void resetButton() {
		for(i = 0; i < n; i++) {
			for(j = 0; j < n; j++) {
				state[i][j] = 0;
			}
		}
		for(i = 0; i < tiles.size(); i++) {
			tiles.get(i).setIcon(blankTile);
			tiles.get(i).clicked = 0;
			turn = 0;
		}
		turnSigns.get(0).setIcon(inactiveX);
		turnSigns.get(1).setIcon(inactiveO);

		clock.setText("0");
		time = 0;
		try {
			moveCleaner();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static int winnerChk(){
		for(i = 0; i < n; i++) {
			for(j = 0; j < n; j++) {
				if(state[i][j]>0) {

					int i_placeholder = i;
					int j_placeholder = j;

					int z = XOCounter(i, j);

					i = i_placeholder;
					j = j_placeholder;

					if(z == 2) {
						turn = n*n;
						return 2;
					}
					else if(z == 1) {
						turn = n*n;
						return 1;
					}

				}
			}
		}
	return 0;
	}
/////////////////GAME LOGIC ///////////////////////////////////////////////////
	private static int XOCounter(int coordinateA, int coordinateB) {
	int xCounter = 0;
	int oCounter = 0;
	int a_placeholder = coordinateA;
	int b_placeholder = coordinateB;
	int i = 0;

	// count Xs and Os horizontally
	for(i = 0; i < winPoint; i++) {
		if(coordinateA>=n || coordinateB >= n || coordinateA < 0 || coordinateB < 0) {
			break;
		}

		if(state[coordinateA][coordinateB] == 2) {
			xCounter++;
		}
		else if(state[coordinateA][coordinateB] == 1) {
			oCounter++;
		}

		//X won
		if(xCounter == winPoint) {
			coordinateA = a_placeholder;
			coordinateB = b_placeholder;
			//swap icons with crossed
			for(i = 0; i < winPoint; i++) {
				int q = getTile(coordinateA, coordinateB);
				tiles.get(q).setIcon(xIconCrossed);
				coordinateB++;
			}
			return 2;
		}

		//O won
		else if(oCounter == winPoint) {
			coordinateA = a_placeholder;
			coordinateB = b_placeholder;
			//swap icons with crossed
			for(i = 0; i < winPoint; i++) {
				int q = getTile(coordinateA, coordinateB);
				tiles.get(q).setIcon(oIconCrossed);
				coordinateB++;
			}
			return 1;
		}
		//move on to the next row
		coordinateB++;
	}
//////////////////////////////////////////////////////////////////////////////
	coordinateA = a_placeholder;
	coordinateB = b_placeholder;
	xCounter = 0;
	oCounter = 0;

	for(i = 0; i < winPoint; i++) {

		if(coordinateA>=n || coordinateB >= n || coordinateA < 0 || coordinateB < 0) {
			break;
		}

		if(state[coordinateA][coordinateB] == 2) {
			xCounter++;
		}
		if(state[coordinateA][coordinateB] == 1) {
			oCounter++;
		}

		if(xCounter == winPoint) {
			coordinateA = a_placeholder;
			coordinateB = b_placeholder;
			for(i = 0; i < winPoint; i++) {
			 int q = getTile(coordinateA, coordinateB);
			 tiles.get(q).setIcon(xIconCrossed);
			 coordinateA++;
			}
			return 2;
		}
		else if(oCounter == winPoint) {
			coordinateA = a_placeholder;
			coordinateB = b_placeholder;
			for(i = 0; i < winPoint; i++) {
				int q = getTile(coordinateA, coordinateB);
				tiles.get(q).setIcon(oIconCrossed);
				coordinateA++;
			}
			return 1;
		}
	//// make this a function holy hell
		coordinateA++;
	}
////////////////////////////////////////////////////////////////////////////////
	coordinateA = a_placeholder;
	coordinateB = b_placeholder;
	xCounter = 0;
	oCounter = 0;

	for(i = 0; i < winPoint; i++) {

		if(coordinateA>=n || coordinateB >= n || coordinateA < 0 || coordinateB < 0) {
			break;
		}

		if(state[coordinateA][coordinateB] == 2) {
			xCounter++;
		}
		if(state[coordinateA][coordinateB] == 1) {
			oCounter++;
		}
		//winnerChk
		if(xCounter == winPoint) {
			coordinateA = a_placeholder;
			coordinateB = b_placeholder;
			for(i = 0; i < winPoint; i++) {
				int q = getTile(coordinateA, coordinateB);
				tiles.get(q).setIcon(xIconCrossed);
				coordinateA++;
				coordinateB++;
			}
			return 2;
		}
		else if(oCounter == winPoint) {
			coordinateA = a_placeholder;
			coordinateB = b_placeholder;
			for(i = 0; i < winPoint; i++) {
				int q = getTile(coordinateA, coordinateB);
				tiles.get(q).setIcon(oIconCrossed);
				coordinateA++;
				coordinateB++;
			}
			return 1;
		}
		coordinateA++;
		coordinateB++;
	}
////////////////////////////////////////////////////////////////////////////////
	coordinateA = a_placeholder;
	coordinateB = b_placeholder;
	xCounter = 0;
	oCounter = 0;
	for(i = 0; i < winPoint; i++) {

		if(coordinateA>=n || coordinateB >= n || coordinateA < 0 || coordinateB < 0) {
			break;
		}

		if(state[coordinateA][coordinateB] == 2) {
			xCounter++;
		}
		if(state[coordinateA][coordinateB] == 1) {
			oCounter++;
		}
		//winnerChk
		if(xCounter == winPoint) {
			coordinateA = a_placeholder;
			coordinateB = b_placeholder;
			for(i = 0; i < winPoint; i++) {
				int q = getTile(coordinateA, coordinateB);
				tiles.get(q).setIcon(xIconCrossed);
				coordinateA++;
				coordinateB--;
			}
			return 2;
		}
		else if(oCounter == winPoint) {
			coordinateA = a_placeholder;
			coordinateB = b_placeholder;
			for(i = 0; i < winPoint; i++) {
				int q = getTile(coordinateA, coordinateB);
				tiles.get(q).setIcon(oIconCrossed);
				coordinateA++;
				coordinateB--;
			}
			return 1;
		}
		coordinateA++;
		coordinateB--;
	}
////////////////////////////////////////////////////////////////////////////////
	return 0;
	}

	private static int getTile(int coordinateA, int coordinateB) {
		int i = 0;
		for(i = 0; i < tiles.size(); i++) {
			if(tiles.get(i).xLocation == coordinateB && tiles.get(i).yLocation == coordinateA) {
				return i;
			}
		}
		return -1;
	}

	//EOC
	//////////////////////////////////////////
	//AI

	static void setTheme(JRadioButtonMenuItem theme, JRadioButtonMenuItem theme1, JRadioButtonMenuItem theme2, JRadioButtonMenuItem theme3,
			JRadioButtonMenuItem theme4, JRadioButtonMenuItem theme5, JRadioButtonMenuItem theme6, JFrame frame, BackgroundMenuBar menuBar,
			JMenu menu, JMenu menu1, JMenu menu2, JMenu menu3, JButton reset,JLabel activeXlab, JLabel activeOlab){
		if(theme==theme1) {
			//TECH
			 frame.getContentPane().setBackground(new java.awt.Color(40, 40, 40));
			 menuBar.setColor(new java.awt.Color(0, 24, 34));

			 menu.setForeground(new java.awt.Color(23, 220, 255));
			 menu1.setForeground(new java.awt.Color(23, 220, 255));
			 menu2.setForeground(new java.awt.Color(23, 220, 255));
			 menu3.setForeground(new java.awt.Color(23, 220, 255));

			 reset.setBackground(new java.awt.Color(0, 24, 34));
			 reset.setForeground(new java.awt.Color(23, 220, 255));

			 blankTile = new ImageIcon("../icons/tech0.png");
			 oIcon = new ImageIcon("../icons/tech1.png");
			 xIcon = new ImageIcon("../icons/tech2.png");
			 oIconCrossed = new ImageIcon("../icons/tech11.png");
			 xIconCrossed = new ImageIcon("../icons/tech22.png");

			 activeX = new ImageIcon("../icons/techX.png");
			 inactiveX = new ImageIcon("../icons/techXi.png");
			 activeO = new ImageIcon("../icons/techO.png");
			 inactiveO = new ImageIcon("../icons/techOi.png");

			 resetButton();
			}
		else if(theme==theme2) {
  		//NOTEBOOK
  		frame.getContentPane().setBackground(new java.awt.Color(146, 150, 208));
  		menuBar.setColor(new java.awt.Color(225, 220, 241));

    	menu.setForeground(new java.awt.Color(74, 79, 128));
			menu1.setForeground(new java.awt.Color(74, 79, 128));
			menu2.setForeground(new java.awt.Color(74, 79, 128));
			menu3.setForeground(new java.awt.Color(74, 79, 128));

  		reset.setBackground(new java.awt.Color(225, 220, 241));
  		reset.setForeground(new java.awt.Color(74, 79, 128));

    	blankTile = new ImageIcon("../icons/note0.png");
			oIcon = new ImageIcon("../icons/note1.png");
			xIcon = new ImageIcon("../icons/note2.png");
			oIconCrossed = new ImageIcon("../icons/note11.png");
			xIconCrossed = new ImageIcon("../icons/note22.png");

			activeX = new ImageIcon("../icons/notebookX.png");
			inactiveX = new ImageIcon("../icons/notebookXi.png");
			activeO = new ImageIcon("../icons/notebookO.png");
			inactiveO = new ImageIcon("../icons/notebookOi.png");

			resetButton();
    	}
		else if(theme==theme3) {
  		//CONCRETE
  		frame.getContentPane().setBackground(new java.awt.Color(189, 189, 183));
  		menuBar.setColor(new java.awt.Color(193, 186, 194));

    	menu.setForeground(new java.awt.Color(90, 90, 90));
			menu1.setForeground(new java.awt.Color(90, 90, 90));
			menu2.setForeground(new java.awt.Color(90, 90, 90));
			menu3.setForeground(new java.awt.Color(90, 90, 90));

  		reset.setBackground(new java.awt.Color(193, 186, 194));
  		reset.setForeground(new java.awt.Color(90, 90, 90));

    	blankTile = new ImageIcon("../icons/concrete0.png");
			oIcon = new ImageIcon("../icons/concrete1.png");
			xIcon = new ImageIcon("../icons/concrete2.png");
			oIconCrossed = new ImageIcon("../icons/concrete11.png");
			xIconCrossed = new ImageIcon("../icons/concrete22.png");

			activeX = new ImageIcon("../icons/concreteX.png");
			inactiveX = new ImageIcon("../icons/concreteXi.png");
			activeO = new ImageIcon("../icons/concreteO.png");
			inactiveO = new ImageIcon("../icons/concreteOi.png");

			resetButton();
    	}
		else if(theme==theme4) {
  		//CHALK
  		frame.getContentPane().setBackground(new java.awt.Color(27, 48, 45));
  		menuBar.setColor(new java.awt.Color(37, 35, 51));

    	menu.setForeground(new java.awt.Color(115, 108, 155));
			menu1.setForeground(new java.awt.Color(115, 108, 155));
			menu2.setForeground(new java.awt.Color(115, 108, 155));
			menu3.setForeground(new java.awt.Color(115, 108, 155));

  		reset.setBackground(new java.awt.Color(37, 35, 51));
  		reset.setForeground(new java.awt.Color(115, 108, 155));

    	blankTile = new ImageIcon("../icons/school0.png");
			oIcon = new ImageIcon("../icons/school1.png");
			xIcon = new ImageIcon("../icons/school2.png");
			oIconCrossed = new ImageIcon("../icons/school11.png");
			xIconCrossed = new ImageIcon("../icons/school22.png");

			activeX = new ImageIcon("../icons/chalkX.png");
			inactiveX = new ImageIcon("../icons/chalkXi.png");
			activeO = new ImageIcon("../icons/chalkO.png");
			inactiveO = new ImageIcon("../icons/chalkOi.png");

			resetButton();
    	}
		else if(theme==theme5) {
  		//ANTIQUA
  		frame.getContentPane().setBackground(new java.awt.Color(58, 31, 41));
  		menuBar.setColor(new java.awt.Color(31, 52, 58));

    	menu.setForeground(new java.awt.Color(77, 130, 145));
			menu1.setForeground(new java.awt.Color(77, 130, 145));
			menu2.setForeground(new java.awt.Color(77, 130, 145));
			menu3.setForeground(new java.awt.Color(77, 130, 145));

  		reset.setBackground(new java.awt.Color(32, 68, 74));
  		reset.setForeground(new java.awt.Color(77, 130, 145));

    	blankTile = new ImageIcon("../icons/antiqua0.png");
			oIcon = new ImageIcon("../icons/antiqua1.png");
			xIcon = new ImageIcon("../icons/antiqua2.png");
			oIconCrossed = new ImageIcon("../icons/antiqua11.png");
			xIconCrossed = new ImageIcon("../icons/antiqua22.png");

			activeX = new ImageIcon("../icons/antiquaX.png");
			inactiveX = new ImageIcon("../icons/antiquaXi.png");
			activeO = new ImageIcon("../icons/antiquaO.png");
			inactiveO = new ImageIcon("../icons/antiquaOi.png");

			resetButton();
    	}
		else if(theme==theme6) {
  		//TECHWARM
  		frame.getContentPane().setBackground(new java.awt.Color(40, 40, 40));
  		menuBar.setColor(new java.awt.Color(32, 0, 16));

    	menu.setForeground(new java.awt.Color(255, 57, 0));
			menu1.setForeground(new java.awt.Color(255, 57, 0));
		  menu2.setForeground(new java.awt.Color(255, 57, 0));
			menu3.setForeground(new java.awt.Color(255, 57, 0));

  		reset.setBackground(new java.awt.Color(32, 0, 16));
  		reset.setForeground(new java.awt.Color(255, 57, 0));

  		blankTile = new ImageIcon("../icons/tech0.png");
			oIcon = new ImageIcon("../icons/techwarm1.png");
			xIcon = new ImageIcon("../icons/techwarm2.png");
			oIconCrossed = new ImageIcon("../icons/techwarm11.png");
			xIconCrossed = new ImageIcon("../icons/techwarm22.png");

			activeX = new ImageIcon("../icons/techWX.png");
			inactiveX = new ImageIcon("../icons/techWXi.png");
			activeO = new ImageIcon("../icons/techWO.png");
			inactiveO = new ImageIcon("../icons/techWOi.png");

			resetButton();
    	}
	}

	public static void AI() throws IOException {
		int a, b;
		String gameState = new String();
		for(i = 0; i < n; i++) {
			for(j = 0; j < n; j++) {
				gameState += String.valueOf(state[i][j]);
			}
		}

		String gameStateFilename = new String();
		gameStateFilename = "../tactics/" + gameState +".txt";

		File file = new File(gameStateFilename);
			//if file with tactics doesn't exist make a random move
		if (!file.exists()) {
				do {
				a = randomNumber(0, n - 1);
				b = randomNumber(0, n - 1);
				}
				while(!movePossibilityChk(a,b));

			state[a][b] = 2;
			//change icon on board and set space as clicked
			for(i = 0; i < tiles.size(); i++) {
				if(tiles.get(i).xLocation == b && tiles.get(i).yLocation == a) {
					boardTile p = tiles.get(i);
					p.clicked = 1;
					p.setIcon(xIcon);
					turn++;
				}
			}
		 }
		 // else read move from tactics file
		 else {
			  FileReader fr = new FileReader(gameStateFilename);
			  BufferedReader br = new BufferedReader(fr);

			  String line;
			  int tacticMoveCount = 0;
			  while((line = br.readLine()) != null) {
				  tacticMoveCount++;
			  }
			  String[]moves = new String[tacticMoveCount];
			  i=0;

			  fr = new FileReader(gameStateFilename);
			  br = new BufferedReader(fr);

			  while((line = br.readLine()) != null) {
				  moves[i] = line;
				  i++;
			  }


			  br.close();
			  fr.close();

			  if(tacticMoveCount>1) {
					//choose a random move from available moves
				  int r = randomNumber(0, tacticMoveCount-1);
				  String m = new String(moves[r]);
				  String[] coordinates =  m.split(" ");
				  a = Integer.valueOf(coordinates[0]);
				  b = Integer.valueOf(coordinates[1]);
			  }
			  else if(tacticMoveCount == 1) {
				  String m = new String(moves[0]);
				  String[] coordinates =  m.split(" ");
				  a = Integer.valueOf(coordinates[0]);
				  b = Integer.valueOf(coordinates[1]);
			  }
				//choose a random move if no moves are available
			  else {
				   do {
						a = randomNumber(0, n - 1);
						b = randomNumber(0, n - 1);
						}
						while(!movePossibilityChk(a,b));
			  }
			  state[a][b] = 2;

				for(i = 0; i < tiles.size(); i++) {
					if(tiles.get(i).xLocation == b && tiles.get(i).yLocation == a) {
						boardTile p = tiles.get(i);
						p.clicked = 1;
						p.setIcon(xIcon);
						turn++;
					}
				}
		  }
	}

	//moves are saved as tactics
	public static void AI_learn() throws IOException {
		//write gamestate as string
		String gameState = new String();
		for(i = 0; i < n; i++) {
			for(j = 0; j < n; j++) {
				gameState += String.valueOf(state[i][j]);
			}
		}

		int a, b;
		do {
			a = randomNumber(0, n - 1);
			b = randomNumber(0, n - 1);
		}
		while(!movePossibilityChk(a,b));

    FileWriter fw = new FileWriter("gamestates.txt", true);
    BufferedWriter bw=new BufferedWriter(fw);

		state[a][b] = 2;
		for(i = 0; i < tiles.size(); i++) {
			if(tiles.get(i).xLocation == b && tiles.get(i).yLocation == a) {
				boardTile p = tiles.get(i);
				p.clicked = 1;
				p.setIcon(xIcon);
				turn++;
				//write gamestate and moves
				bw.write("S");
				bw.write(gameState);
				bw.newLine(); /// are those newlines necessary?
				bw.write(String.valueOf(a) + " " + String.valueOf(b));
				bw.newLine();
			}
		}

		bw.close();
		fw.close();
	}

//remove moves after a lost game
	private static void moveCleaner() throws IOException {
		FileWriter fw = new FileWriter("gamestates.txt", false);
	    fw.write("");
	    fw.close();
	}

//write moves ad tactic after a win or a draw
	private static void theTactic() throws IOException {
		FileReader gameStateFileReader = new FileReader("gamestates.txt");
		BufferedReader gameStateReader = new BufferedReader(gameStateFileReader);

		String games, move;
		while((games = gameStateReader.readLine()) != null) {
				if(games.matches("S(.*)")) {
					String state = "../tactics/"  + games.substring(1)+".txt";
					File file = new File(state);
				  if (!file.exists()) {
            file.createNewFile();
		      }
					FileWriter tacticCreator = new FileWriter(state, true);
					BufferedWriter tacticWriter = new BufferedWriter(tacticCreator);
					while((move = gameStateReader.readLine()).matches("[0-9]+ [0-9]+")){
						//check if move doesn't already exist in a file
						if(originalMoveChk(move, state)) {
							tacticWriter.write(move);
							tacticWriter.newLine();
						}
					}
					tacticWriter.close();
					tacticCreator.close();
				}
		}
		gameStateFileReader.close();
		gameStateReader.close();



	}
	private static boolean originalMoveChk(String move, String state) throws IOException {
		FileReader fr = new FileReader(state);
		BufferedReader br = new BufferedReader(fr);
		String chk = new String();
		while((chk = br.readLine()) != null) {
			if(chk.equals(move)) {
				return false;
			}
		}
		return true;
	}

	private static boolean movePossibilityChk(int a, int b) {
		if(state[a][b] != 0) {
			return false;
		}
		return true;
		}

	private static int randomNumber(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max musi byc wiekszy od min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
		}


}
