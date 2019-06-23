package com.samisari.cezmi.networkcommands.wizards;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.networkcommands.CmdServer;

public class CmdServerWizard extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1455314886791561797L;
	CmdServer command;
	JTextField hostNameTxt = new JTextField();
	JTextField sshUserTxt = new JTextField();
	JTextField sshPasswordTxt = new JTextField();
	JButton commtBtn = new JButton("Ok");
	
	public CmdServerWizard(AbstractCommand cmd) {
		command = (CmdServer) cmd;
		setUndecorated(true);
	}
	
	public void display() {
		setLayout(null);
		setPreferredSize(new Dimension(500, 400));
		JLabel lbl = new JLabel("Host name");
		lbl.setBounds(10,10,100,20);
		add(lbl);
		
		hostNameTxt.setBounds(110,10,200,20);
		add(hostNameTxt);

		lbl = new JLabel("SSH User");
		lbl.setBounds(10,35,100,20);
		add(lbl);
		
		sshUserTxt.setBounds(110,35,200,20);
		add(sshUserTxt);

		lbl = new JLabel("SSH Password");
		lbl.setBounds(10,60,100,20);
		add(lbl);
		
		sshPasswordTxt.setBounds(110,60,200,20);
		add(sshPasswordTxt);
		
		commtBtn.setBounds(400, 300, 70, 30);
		add(commtBtn);
		commtBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
				CmdServerWizard.this.dispose();
			}
		});

		load();
		setVisible(true);
		pack();
	}

	private void load() {
		hostNameTxt.setText(command.getHostName());
		sshUserTxt.setText(command.getSshUser());
		sshPasswordTxt.setText(command.getSshPassword());
	}
	private void save() {
		command.setHostName(hostNameTxt.getText());
		command.setSshUser(sshUserTxt.getText());
		command.setSshPassword(sshPasswordTxt.getText());
	}

}
