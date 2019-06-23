package com.samisari.graphics.commands;

import java.util.List;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;

public class CmdSOUp extends AbstractCommand {
	@Override
	public void run() {
		List<AbstractCommand> list = CommandManager.getDeaultInstance().getSelectedCommands();
		for (AbstractCommand cmd : list) {
			int index = CommandManager.getDeaultInstance().getHistory().getIndex(cmd);
			if (index > 0)
				CommandManager.getDeaultInstance().getHistory().add(index--, CommandManager.getDeaultInstance().getHistory().remove(index));
		}
		CommandManager.getDeaultInstance().endCommand();
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
	}

}
