package view;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import player.*;


public class GameFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	public static void win(Player p){
		new WinDialog(p.toString());
		System.out.println(p+"wins");
	}
	JPanel controlPanel;
	static GameFrame instance=null;
	public static GameFrame getInstance(){
		if(instance==null){
			instance=new GameFrame();
		}
		return instance;
	}
	private GameFrame(){
		controlPanel= ControlPanel.getInstance();;
		
		this.setLayout(new BorderLayout());
		this.setVisible(true);
		this.add(BorderLayout.CENTER, GamePanel.getInstance());
		this.add(BorderLayout.EAST,controlPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}
}
