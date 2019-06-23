package com.samisari.graphics.core;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsoleKeyListener;
import com.samisari.cezmi.core.ConsoleMouseListener;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.Java2DFPanel;
import com.samisari.cezmi.util.FileChooserHistory;
import com.samisari.gui.dockablepanel.DockablePanel.Position;
import com.samisari.gui.panel.HistoryPanel;
import com.samisari.gui.panel.PropertiesPanel;

public class UiManager extends WindowAdapter {
	public static final org.apache.log4j.Logger	logger	= Logger.getLogger(UiManager.class);
	private JFrame			frame;
	private Java2DFPanel	consolePanel;
	private Component		toolBox;
	private HistoryPanel	historyPanel;
	private PropertiesPanel	propertiesPanel;
	private JSplitPane		horizontalSplit;
	private JSplitPane		rightSplit;
	private JTabbedPane		tabs;
	/**
	 * Sets up user interface components of the application
	 */
	public void setUi() {
		logger.debug("---- Starting Application ----");
		Preferences prefs = Preferences.getInstance();
		frame = new JFrame(Messages.getString("Application.1"));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(this);
		frame.setBounds(prefs.getWindow());
		frame.setPreferredSize(prefs.getWindow().getSize());

		logger.debug("---- Creating Java2DFPanel ----");
		consolePanel = new Java2DFPanel();
		consolePanel.setBackground(Color.black);
		toolBox = ToolBoxFactory.getInstance();

		propertiesPanel = new PropertiesPanel();
		propertiesPanel.setPreferredSize(new Dimension(200, 800));
		historyPanel = new HistoryPanel(rightSplit, Position.BOTTOM);

		tabs = new JTabbedPane();
		tabs.addTab(Messages.getString("Application.3"), propertiesPanel);
		tabs.addTab(Messages.getString("Application.4"), historyPanel);

		rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, toolBox, tabs);
		rightSplit.setDividerLocation(prefs.getRightSplitLocation());
		rightSplit.setOneTouchExpandable(true);

		horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, consolePanel, rightSplit);
		horizontalSplit.setDividerLocation(prefs.getHorizontalSplitLocation());
		horizontalSplit.setOneTouchExpandable(true);

		frame.add(horizontalSplit);
		logger.debug("---- Initializing ConsolePropertyManager instance ----");
		ConsolePropertyManager.getDefaultInstance().setConsolePanel(consolePanel);
		ConsolePropertyManager.getDefaultInstance().setApplicationFrame(frame);
		logger.debug("---- Initializing CommandManager instance ----");
		CommandManager.getDeaultInstance();
		logger.debug("---- Adding ConsoleMouseListener ----");
		ConsoleMouseListener masterListener = new ConsoleMouseListener();
		ConsoleKeyListener keyListener = new ConsoleKeyListener();
		consolePanel.addMouseListener(masterListener);
		consolePanel.addMouseWheelListener(masterListener);
		logger.debug("---- Adding ConsoleMouseMotionListener ----");
		consolePanel.addMouseMotionListener(masterListener);
		//		ConsoleMouseMotionListener motionListener = new ConsoleMouseMotionListener();
		//		consolePanel.addMouseMotionListener(motionListener);
		consolePanel.addKeyListener((KeyListener) keyListener);
		logger.debug("---- Adding Java2DPanelComponentListener ----");
		consolePanel.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentResized(ComponentEvent e) {
				ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();

			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub

			}
		});
		frame.pack();
		logger.debug("---- Display Main Frame ----");
		frame.setVisible(true);

	}
	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public Java2DFPanel getConsolePanel() {
		return consolePanel;
	}

	public void setConsolePanel(Java2DFPanel consolePanel) {
		this.consolePanel = consolePanel;
	}

	public Component getToolBox() {
		return toolBox;
	}

	public void setToolBox(Component toolBox) {
		this.toolBox = toolBox;
	}

	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {
		Preferences.getInstance().save();
		FileChooserHistory.save();
	}

	public JSplitPane getHorizontalSplit() {
		return horizontalSplit;
	}

	public void setHorizontalSplit(JSplitPane horizontalSplit) {
		this.horizontalSplit = horizontalSplit;
	}

	public JSplitPane getRightSplit() {
		return rightSplit;
	}

	public void setRightSplit(JSplitPane rightSplit) {
		this.rightSplit = rightSplit;
	}

	public HistoryPanel getHistoryPanel() {
		return historyPanel;
	}

	public void setHistoryPanel(HistoryPanel historyPanel) {
		this.historyPanel = historyPanel;
	}

	public PropertiesPanel getPropertiesPanel() {
		return propertiesPanel;
	}

	public void setPropertiesPanel(PropertiesPanel propertiesPanel) {
		this.propertiesPanel = propertiesPanel;
	}

	public JTabbedPane getTabs() {
		return tabs;
	}

	public void setTabs(JTabbedPane tabs) {
		this.tabs = tabs;
	}

}
