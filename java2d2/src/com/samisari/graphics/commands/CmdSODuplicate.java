package com.samisari.graphics.commands;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.Operation;

public class CmdSODuplicate extends AbstractCommand implements Serializable {
	private static final long	serialVersionUID	= 5226672462820897805L;
	public static final Logger	logger				= Logger.getLogger(CmdSODuplicate.class);

	@Override
	public void run() {
		logger.debug("Copying Selected Objects");

		for (AbstractCommand cmd : CommandManager.getDeaultInstance().getSelectedCommands()) {
			cmd.setSelected(false);
			AbstractCommand clone = cmd.duplicate();
			clone.move(Operation.MOVE, 10, 10);
			CommandManager.getDeaultInstance().getHistory().add(clone);
			clone.setSelected(true);
		}
		CommandManager.getDeaultInstance().endCommand();
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
	}

}
