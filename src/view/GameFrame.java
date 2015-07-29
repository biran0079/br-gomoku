package view;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import player.*;


public class GameFrame extends JFrame{

	public  GameFrame(GamePanel gamePanel, ControlPanel controlPanel){
		this.setLayout(new BorderLayout());
		this.setVisible(true);
		this.add(BorderLayout.CENTER, gamePanel);
		this.add(BorderLayout.EAST,controlPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}
}
