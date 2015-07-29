package view;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class WinDialog extends JDialog {

	WinDialog(JFrame parent, String name, ActionListener clickListener) {
		String s = "<html><h2>" + name + " Wins!</h2><br>"
				+ "<pre> like to try again?</pre></html>";
		JLabel msg = new JLabel(s);
		JButton ok = new JButton("ok");
		this.setVisible(true);
		this.setLayout(null);
		this.setPreferredSize(new Dimension(200, 200));
		this.pack();
		msg.setBounds(30, 10, 200, 100);
		ok.setBounds(70, 120, 60, 30);
		this.add(msg);
		this.add(ok);
		this.setResizable(false);
		this.setLocation(parent.getX(), parent.getY());
		ok.addActionListener(clickListener);
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

	}
}