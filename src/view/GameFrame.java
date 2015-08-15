package view;

import java.awt.BorderLayout;

import javax.inject.Inject;
import javax.swing.JFrame;


public class GameFrame extends JFrame{

  @Inject
	public  GameFrame(GamePanel gamePanel, ControlPanel controlPanel){
		this.setLayout(new BorderLayout());
		this.setVisible(true);
		this.add(BorderLayout.CENTER, gamePanel);
		this.add(BorderLayout.EAST,controlPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}
}
