package view;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;


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
