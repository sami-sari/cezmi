package com.samisari.graphics.commands;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.History;

public class CmdClearHistory extends AbstractCommand {
	private static final long	serialVersionUID	= 5226672462820897805L;
	public static final Logger	logger				= Logger.getLogger(CmdClearHistory.class);
	public final History		deletedObjects		= new History();

	@Override
	public void run() {
		int choice = JOptionPane.showConfirmDialog(null, "Bu işlem geri alınamaz devam etmek istiyor musunuz?");
		if (choice == JOptionPane.YES_OPTION) {
			logger.debug("Compacting history");
			CommandManager.getDeaultInstance().getHistory().clear();
		}
		CommandManager.getDeaultInstance().endCommand();
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
	}

}
