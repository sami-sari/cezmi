package com.samisari.graphics.commands;

import java.text.NumberFormat;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;

public abstract class CmdZoom extends AbstractCommand {

	@Override
	public void run() {
		ConsolePropertyManager prefs = ConsolePropertyManager.getDefaultInstance();
		prefs.setScaleFactor(prefs.getScaleFactor()*this.getZoomFactor());
//		AbstractCommand cmd = CommandManager.getInstance().getHistory().get(0);
//		if (cmd instanceof CmdZoom) {
//			if ((int)(((CmdZoom) cmd).getZoomFactor() * getZoomFactor() * 100) == 100) {
//				CommandManager.getInstance().getHistory().remove(0);
//			} else {
//				setZoomFactor(((CmdZoom) cmd).getZoomFactor() * this.getZoomFactor());
//				CommandManager.getInstance().getHistory().remove(0);
//				CommandManager.getInstance().getHistory().add(0, this);
//			}
//		} else {
//			CommandManager.getInstance().getHistory().add(0, this);
//		}
		CommandManager.getDeaultInstance().endCommand();
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
	}

	public static String getCommandName() {
		return "Z+";
	}

	public abstract double getZoomFactor();

	public abstract void setZoomFactor(double zoomFactor);
	
	@Override
	public String toString() {
		return "Zoom " + NumberFormat.getPercentInstance().format(getZoomFactor());
	}

}
