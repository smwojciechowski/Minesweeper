import javax.swing.*;

public class MineButton extends JButton {
	
	int isMine;
	String buttonLabel;
	
	boolean isFlagged = false;
	
	int positionX;
	int positionY;
	
	public MineButton(String bLabel) {
		
		buttonLabel = bLabel;
	}
	
	public int getMineFlag() {
		return isMine;
	}
	
	public void setMineFlag() {
		
		int x = (int) (Math.random() * 10);

		if (x < 3) {
			isMine = 1;
		} else {
			isMine = 0;
		}
	}
	
	public void setPositionX(int x) {
		positionX = x;
	}
	
	public int getPositionX() {
		return positionX;
	}
	
	public void setPositionY(int y) {
		positionY = y;
	}
	
	public int getPositionY() {
		return positionY;
	}
}


