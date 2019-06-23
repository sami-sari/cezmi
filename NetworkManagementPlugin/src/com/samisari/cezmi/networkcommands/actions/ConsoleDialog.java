package com.samisari.cezmi.networkcommands.actions;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;

import com.jcraft.jcterm.Connection;
import com.jcraft.jcterm.JCTermSwing;
import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.networkcommands.CmdServer;

public class ConsoleDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1455314886791561797L;
	CmdServer command;
	Connection connection;
	JCTermSwing console = new JCTermSwing();
	JButton commtBtn = new JButton("Close");

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public JCTermSwing getConsole() {
		return console;
	}

	public void setConsole(JCTermSwing console) {
		this.console = console;
	}

	public ConsoleDialog(AbstractCommand cmd) {
		command = (CmdServer) cmd;
		console.setAntiAliasing(true);
		console.setBackground(Color.black);
		console.setForeground(Color.white);
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				try {
				connection.close();
				} catch(Exception x) {
					
				}
			}
			
		});
		display();

	}

	public void display() {
//		setLayout(BoxLayout);
		setPreferredSize(new Dimension(500, 400));

		add(console);

		commtBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				connection.close();
				ConsoleDialog.this.dispose();
			}
		});

		setVisible(true);
		pack();
	}

}
