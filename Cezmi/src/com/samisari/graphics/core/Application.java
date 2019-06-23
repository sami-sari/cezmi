package com.samisari.graphics.core;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

public class Application {
	public static final org.apache.log4j.Logger	logger	= Logger.getLogger(Application.class);
	private static final Application			instance;

	static {
		instance = new Application();
	}

	public static Application getInstance() {
		return instance;
	}

	private Application() {
		try {
			uiManager = new UiManager();
			Preferences.getInstance();
		} catch (Throwable t) {
			JOptionPane.showMessageDialog(null, t.getMessage());
		}
	}
	private UiManager uiManager;




	public static void main(String[] args)  {
		Application.getInstance().getUiManager().setUi();
	}


	public UiManager getUiManager() {
		return uiManager;
	}

	public void setUiManager(UiManager uiManager) {
		this.uiManager = uiManager;
	}
}
