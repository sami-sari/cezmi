package com.samisari.graphics.commands;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.History;

public class CmdSODelete extends AbstractCommand {
	private static final long	serialVersionUID	= 5226672462820897805L;
	public static final Logger	logger				= Logger.getLogger(CmdSODelete.class);
	public final History		deletedObjects		= new History();

	@Override
	public void run() {
		logger.debug("Deleting Selected Objects");

		for (AbstractCommand cmd : CommandManager.getDeaultInstance().getSelectedCommands()) {
			deletedObjects.add(cmd);
			cmd.onDelete();
			CommandManager.getDeaultInstance().getHistory().remove(cmd);
		}
		CommandManager.getDeaultInstance().endCommand();
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
		CommandManager.getDeaultInstance().getHistory().add(this);
	}

	@Override
	public void undo() {
		super.undo();
		for (AbstractCommand cmd : deletedObjects) {
			CommandManager.getDeaultInstance().getHistory().add(cmd);
		}
	}
}
