import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;

public class MineSweeper {
	
	JFrame mainFrame;
	JPanel minePanel;
	JPanel counterPanel;
	JPanel outerCounterPanel;
	JLabel mineCounterLabel;
	JLabel timerLabel;
	
	ArrayList<ArrayList<MineButton>> mineButtonArrayList = new ArrayList<ArrayList<MineButton>>();
	MineButton tempMineCheckButton;
	
	int tempUncoveredTileCount;
	int generatedMineAmount;
	int timeElapsed = 0;
	
	Timer timer;
	
	public static void main(String[] args) {
		MineSweeper sweeper = new MineSweeper();
		sweeper.buildGui();
		
		//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	}
	
	public void buildGui() {
		
		mainFrame = new JFrame("MineSweeper");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		BorderLayout layout = new BorderLayout();
		
		JPanel background = new JPanel(layout);
		background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		BorderLayout counterLayout = new BorderLayout();
		BorderLayout outerCounterLayout = new BorderLayout();
		
		JPanel counterPanel = new JPanel(counterLayout);
		JPanel outerCounterPanel = new JPanel(outerCounterLayout);
		
		Box utilityButtons = new Box(BoxLayout.Y_AXIS);
		
		mineCounterLabel = new JLabel("Mine count: " + generatedMineAmount);
		timerLabel = new JLabel("TIME: ");
		
		JButton startButton = new JButton("START");
		startButton.addActionListener(new StartListener());
		utilityButtons.add(startButton);
		
		JButton closeButton = new JButton("CLOSE");
		closeButton.addActionListener(new CloseListener());
		utilityButtons.add(closeButton);
		
		background.add(BorderLayout.EAST, utilityButtons);
		background.add(BorderLayout.NORTH, outerCounterPanel);
		
		counterPanel.add(BorderLayout.SOUTH, mineCounterLabel);
		counterPanel.add(BorderLayout.NORTH, timerLabel);
		outerCounterPanel.add(BorderLayout.WEST, counterPanel);
		
		mainFrame.getContentPane().add(background);
		
		GridLayout mineGrid = new GridLayout(15, 15);
		mineGrid.setVgap(2);
		mineGrid.setHgap(2);
		minePanel = new JPanel(mineGrid);
		background.add(BorderLayout.CENTER, minePanel);
		
		generateMineButtons();
		
		mainFrame.setBounds(100, 100, 800, 600);
		//mainFrame.pack();
		mainFrame.setVisible(true);
	}
	
	public void generateMineButtons() {
	
	for (int i = 0; i < 15; i++) {
			
			ArrayList<MineButton> innerList = new ArrayList<MineButton>();
			mineButtonArrayList.add(innerList);
			
			for (int j = 0; j < 15; j++) {
				
				MineButton x = new MineButton("");
				//x.setActionCommand("" + (j + 1));
				x.setPositionX(i);
				x.setPositionY(j);
				x.setMineFlag();
				x.addActionListener(new MineListener());
				x.addMouseListener(new FlagMouseListener());
				x.setEnabled(false);
				innerList.add(x);
				
				//minePanel.add(x);
			}	
		}
		
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
			minePanel.add(mineButtonArrayList.get(i).get(j));
			}
		}
	}
	
	public class StartListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
				
			timeElapsed = 0;
			
			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < 15; j++) {
					mineButtonArrayList.get(i).get(j).setMineFlag();
				}
			}
			
			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < 15; j++) {
					mineButtonArrayList.get(i).get(j).setEnabled(true);
					mineButtonArrayList.get(i).get(j).setText("");
					mineButtonArrayList.get(i).get(j).setIcon(null);
				}
			}
		generatedMineAmount = 0;
		for (int a = 0; a < 15; a++) {
			for (int b = 0; b < 15; b++) {
				generatedMineAmount = generatedMineAmount + mineButtonArrayList.get(a).get(b).getMineFlag();
			}
		}
		mineCounterLabel.setText("Mine count: " + generatedMineAmount);
		System.out.println(generatedMineAmount);
		
		timer = new Timer(1000, new TimerUpdateListener());
		timer.start();
		}
		public class TimerUpdateListener implements ActionListener {
		
			public void actionPerformed (ActionEvent e) {
				
				timeElapsed = timeElapsed + 1;
				
				timerLabel.setText("TIME: " + timeElapsed + "sec");
			}
		}
	}
	
	
	
	public class CloseListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	public class FlagMouseListener extends MouseAdapter {
	
		public void mouseReleased(MouseEvent e) {
		
			MineButton flaggedButton = (MineButton) e.getSource();
		
			System.out.println(flaggedButton.getPositionX());
		
			ImageIcon flag = new ImageIcon("resources/flag_resize.gif");
			
			System.out.println("mouse clicked");
		
			if (SwingUtilities.isRightMouseButton(e)) {
				
				System.out.println("right mouse clicked");
			
				if (!(flaggedButton.isFlagged) && flaggedButton.isEnabled()) {
					System.out.print("this should change the icon");
					flaggedButton.setIcon(flag);
					flaggedButton.isFlagged = true;
					generatedMineAmount = generatedMineAmount - 1;
					mineCounterLabel.setText("Mine count: " + generatedMineAmount);
				} else {
					System.out.print("this should remove the icon");
					flaggedButton.isFlagged = false;
					flaggedButton.setIcon(null);
					generatedMineAmount = generatedMineAmount + 1;
					mineCounterLabel.setText("Mine count: " + generatedMineAmount);
				}
			}
		}
	}
	
	public class MineListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			MineButton b = ((MineButton) e.getSource());
			
			//MineButton tempMineCheckButton;
			
			int posX = b.getPositionX();
			int posY = b.getPositionY();
			
			tempUncoveredTileCount = 0;
			
			floodFill(posX, posY);
			System.out.println("Uncovered tiles: " + tempUncoveredTileCount);
			mineCheck(b);
			//checkAdjacentTiles(b);
		}
		
		void floodFill(int btnX, int btnY) {
	
			int x = btnX;
			int y = btnY;
			
			System.out.println("x is:" + x);
			System.out.println("y is:" + y);
			
			//MineButton tempMineCheckButton = mineButtonArrayList.get(x).get(y);
			
			if ((x > -1 && x < 15) && (y > -1 && y < 15) && mineButtonArrayList.get(x).get(y).isEnabled() && mineButtonArrayList.get(x).get(y).isMine == 0 && tempUncoveredTileCount < 6) {
				
				mineButtonArrayList.get(x).get(y).setEnabled(false);
				
				MineButton forTextOnly = mineButtonArrayList.get(x).get(y);
				
				Integer adjacentMineCount = 0;
				
				if (x - 1 > -1) {
					adjacentMineCount = adjacentMineCount + mineButtonArrayList.get(x - 1).get(y).isMine;
				}
				if ((x - 1 > -1) && (y + 1 < 15)) {
					adjacentMineCount = adjacentMineCount + mineButtonArrayList.get(x - 1).get(y + 1).isMine;
				}
				if (y + 1 < 15) {
					adjacentMineCount = adjacentMineCount + mineButtonArrayList.get(x).get(y + 1).isMine;
				}
				if ((x + 1 < 15) && (y + 1 < 15)) {
					adjacentMineCount = adjacentMineCount + mineButtonArrayList.get(x + 1).get(y + 1).isMine;
				}
				if (x + 1 < 15) {
					adjacentMineCount = adjacentMineCount + mineButtonArrayList.get(x + 1).get(y).isMine;
				}
				if ((x + 1 < 15) && (y - 1 > -1)) {
					adjacentMineCount = adjacentMineCount + mineButtonArrayList.get(x + 1).get(y - 1).isMine;
				}
				if (y - 1 > -1) {
					adjacentMineCount = adjacentMineCount + mineButtonArrayList.get(x).get(y - 1).isMine;
				}
				if ((x - 1 > -1) && (y - 1 > -1)) {
					adjacentMineCount = adjacentMineCount + mineButtonArrayList.get(x - 1).get(y - 1).isMine;
				}
				String mineCount = adjacentMineCount.toString();
			
				forTextOnly.setText(mineCount);
				
				tempUncoveredTileCount++;
				floodFill((x + 1), y);
				floodFill((x - 1), y);
				floodFill(x, (y - 1));
				floodFill(x, (y + 1));
			} else {
				return;
			}
		}
	
		public void mineCheck(MineButton b) {
			
			if (b.isMine == 1) {
				
				timer.stop();
				timer = null;		
				
				ImageIcon bomb = new ImageIcon("resources/bomb_resize.gif");
							
				for (ArrayList<MineButton> inner : mineButtonArrayList) {
					for (int i = 0; i < 15; i++) {
						if (inner.get(i).getMineFlag() == 1) {
							inner.get(i).setIcon(bomb);
							inner.get(i).setDisabledIcon(bomb);
							inner.get(i).setEnabled(false);
						}
					}
				}
							
				for (ArrayList<MineButton> inner : mineButtonArrayList) {
					for (int i = 0; i < 15; i++) {
						if (inner.get(i).getMineFlag() == 0) {
							inner.get(i).setText("");
							inner.get(i).setEnabled(false);
						}
					}
				}	
			} else {
				//b.setText("");
				b.setEnabled(false);
			}
		}
	}
}







	