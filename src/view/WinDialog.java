package view;

import game.Game;
import view.GameFrame;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class WinDialog extends JDialog implements ActionListener {

	JLabel msg;
	JButton ok;

	WinDialog(String name) {

		String s = "<html><h2>" + name + " Wins!</h2><br>"
				+ "<pre> like to try again?</pre></html>";
		msg = new JLabel(s);
		ok = new JButton("ok");
		this.setVisible(true);
		this.setLayout(null);
		this.setPreferredSize(new Dimension(200, 200));
		this.pack();
		msg.setBounds(30, 10, 200, 100);
		ok.setBounds(70, 120, 60, 30);
		this.add(msg);
		this.add(ok);
		this.setResizable(false);
		this.setLocation(GameFrame.getInstance().getX(),GameFrame.getInstance().getY());
		ok.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Game.getInstance().setReadyToStart(true);
		this.dispose();
	}
}