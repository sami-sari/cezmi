package com.samisari.graphics.commands;

import javax.swing.JOptionPane;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.sun.glass.ui.Application;

public class CmdNewDocument extends AbstractCommand{
	@Override
	public void run() {
		super.run();
		if (CommandManager.getDeaultInstance().getHistory().isDirty()) {
			int result = JOptionPane.showConfirmDialog(null, "Son yapılan değişiklkler kaydedilmemiş. Devam etmek istiyor musunuz?");
			if (result==JOptionPane.YES_OPTION) {
				return;
			}
		} 
		CommandManager.getDeaultInstance().getHistory().clear();
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
		
	}

}
