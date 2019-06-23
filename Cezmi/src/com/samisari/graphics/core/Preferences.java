package com.samisari.graphics.core;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JOptionPane;

public class Preferences {
	private static Preferences instance;
	private Properties properties;

	public static Preferences getInstance() {
		if (instance == null) {
			instance = new Preferences();
		}
		return instance;
	}

	private Preferences() {
		properties = new Properties();
		try {
			properties.load(this.getClass().getClassLoader().getResourceAsStream(Messages.getString("Preferences.0"))); 
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public void save() {
		try {
			final UiManager uiManager = Application.getInstance().getUiManager();
			set(Messages.getString("Preferences.1"), uiManager.getFrame().getLocation().x); 
			set(Messages.getString("Preferences.2"), uiManager.getFrame().getLocation().y); 
			set(Messages.getString("Preferences.3"), uiManager.getFrame().getBounds().width); 
			set(Messages.getString("Preferences.4"), uiManager.getFrame().getBounds().height); 
			set(Messages.getString("Preferences.5"), uiManager.getConsolePanel().getBounds().width); 
			set(Messages.getString("Preferences.6"), uiManager.getConsolePanel().getBounds().height); 
			set(Messages.getString("Preferences.7"), uiManager.getHorizontalSplit().getDividerLocation()); 
			set(Messages.getString("Preferences.8"), uiManager.getRightSplit().getDividerLocation()); 

			properties.store(new FileOutputStream(this.getClass().getClassLoader().getResource(Messages.getString("Preferences.9")).getFile()), null); 
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public Rectangle getWindow() {
		int x = getInt(Messages.getString("Preferences.10")) == -1 ? 0 : getInt(Messages.getString("Preferences.11"));  //$NON-NLS-2$
		int y = getInt(Messages.getString("Preferences.12"), 0); 
		int width = getInt(Messages.getString("Preferences.13"), 600); 
		int height = getInt(Messages.getString("Preferences.14"), 800); 
		return new Rectangle(x, y, width, height);
	}

	public Dimension getConsoleDimensions() {
		int width = getInt(Messages.getString("Preferences.15")); 
		int height = getInt(Messages.getString("Preferences.16")); 
		return new Dimension(width, height);
	}

	public int getHorizontalSplitLocation() {
		return getInt(Messages.getString("Preferences.17")); 
	}

	public int getRightSplitLocation() {
		return getInt(Messages.getString("Preferences.18")); 
	}

	public String getString(String key) {
		return properties.getProperty(key);
	}

	public int getInt(String key) {
		return getInt(key,0);
	}

	public int getInt(String key, int def) {
		if (properties.getProperty(key) == null)
			return def;
		return Integer.parseInt(properties.getProperty(key));
	}

	public void setString(String key, String value) {
		properties.setProperty(key, value);
	}

	public void set(String key, int value) {
		properties.setProperty(key, Integer.toString(value));
	}
}
