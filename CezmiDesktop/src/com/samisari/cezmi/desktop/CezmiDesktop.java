package com.samisari.cezmi.desktop;

import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsoleMouseListener;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.Java2DFPanel;
import com.samisari.graphics.core.Preferences;

public class CezmiDesktop extends JFrame {
	public static final org.apache.log4j.Logger logger = Logger.getLogger(CezmiDesktop.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<DesktopItem> item;
	private Java2DFPanel panel;

	public CezmiDesktop() {
		super("Cezmi");
		setUndecorated(true);
		logger.debug("---- Starting Application ----"); 
		Preferences prefs = Preferences.getInstance();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(prefs.getWindow());
		setPreferredSize(prefs.getWindow().getSize());
		
		logger.debug("---- Creating Java2DFPanel ----"); 
		panel = new Java2DFPanel();
		panel.setBackground(Color.white);	
		add(panel);
		ConsolePropertyManager.getDefaultInstance().setConsolePanel(panel);
		setVisible(true);
		pack();
		DesktopMouseListener masterListener = new DesktopMouseListener("DEFAULT");
		panel.addMouseListener(masterListener);
		panel.addMouseWheelListener(masterListener);
		logger.debug("---- Adding ConsoleMouseMotionListener ----"); 
		panel.addMouseMotionListener(masterListener);
	}
	
	public List<DesktopItem> getItem() {
		return item;
	}

	public void setItem(List<DesktopItem> item) {
		this.item = item;
	}

	
	public static void main(String[] args) {
		CezmiDesktop desktop = new CezmiDesktop();
		CommandManager.getDeaultInstance().getHistory().add(new DesktopItem());
		desktop.repaint();

	}
	
}
