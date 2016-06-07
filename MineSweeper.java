import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class MineSweeper {
	
	JFrame mainFrame;
	JPanel minePanel;
	ArrayList<JButton> mineButtonArrayList = new ArrayList<JButton>();
	ArrayList<String> randomMinesArrayList;
	
	public static void main(String[] args) {
		MineSweeper sweeper = new MineSweeper();
		sweeper.buildGui();
		//sweeper.go();
	}
	
	public void buildGui() {
		mainFrame = new JFrame("MineSweeper");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		BorderLayout layout = new BorderLayout();
		
		JPanel background = new JPanel(layout);
		background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		Box utilityButtons = new Box(BoxLayout.Y_AXIS);
		
		JButton startButton = new JButton("START");
		startButton.addActionListener(new StartListener());
		utilityButtons.add(startButton);
		
		JButton closeButton = new JButton("CLOSE");
		closeButton.addActionListener(new CloseListener());
		utilityButtons.add(closeButton);
		
		//background.add(BorderLayout.CENTER, mineButtons);
		background.add(BorderLayout.EAST, utilityButtons);
		
		mainFrame.getContentPane().add(background);
		
		GridLayout mineGrid = new GridLayout(15, 15);
		mineGrid.setVgap(2);
		mineGrid.setHgap(2);
		minePanel = new JPanel(mineGrid);
		background.add(BorderLayout.CENTER, minePanel);
		
		for (int i = 0; i < 225; i++) {
			JButton x = new JButton("");
			x.setActionCommand("" + (i + 1));
			x.addActionListener(new MineListener());
			x.setEnabled(false);
			mineButtonArrayList.add(x);
			//minePanel.add(x);
		}
		
		for (int i = 0; i < 225; i++) {
			minePanel.add(mineButtonArrayList.get(i));
		}
		
		mainFrame.setBounds(100, 100, 500, 500);
		//mainFrame.pack();
		mainFrame.setVisible(true);
	}
	
	public class StartListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			for (int i = 0; i < 225; i++) {
				mineButtonArrayList.get(i).setEnabled(true);
				mineButtonArrayList.get(i).setText("");
				mineButtonArrayList.get(i).setIcon(null);
			}
			
			randomMinesArrayList = null;
			randomMinesArrayList = new ArrayList<String>();
			
			while (randomMinesArrayList.size() < 100) {
				int c = (int) Math.ceil(Math.random() * 225);
				String d = new Integer(c).toString();
				System.out.println(d);
				
				if (!(randomMinesArrayList.contains(d))) {
					randomMinesArrayList.add(d);
				}
			}
			System.out.println(randomMinesArrayList);
		}
	}
	
	public class CloseListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	public class MineListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			
			JButton b = ((JButton) e.getSource());
			
			if (randomMinesArrayList.contains(b.getActionCommand())) {
			
				ImageIcon bomb = new ImageIcon("resources/bomb_resize.gif");
				
				for (String mine : randomMinesArrayList) {				
					for (JButton btn : mineButtonArrayList) {
						if (btn.getActionCommand().equals(mine)) {
							//System.out.println(btn.getActionCommand());
							btn.setIcon(bomb);
							btn.setDisabledIcon(bomb);
							btn.setEnabled(false);
						}
					}
				}
				
				for (String mine : randomMinesArrayList) {				
					for (JButton btn : mineButtonArrayList) {
						if (!(btn.getActionCommand().equals(mine))) {
							btn.setText("");
							btn.setEnabled(false);
						}
					}
				}
			} else {
				b.setText("");
				b.setEnabled(false);
			}
		}
	}
	
}







	