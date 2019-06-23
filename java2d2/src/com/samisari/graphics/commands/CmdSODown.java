package com.samisari.graphics.commands;

import java.util.List;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.History;

public class CmdSODown extends AbstractCommand {
	@Override
	public void run() {
		History history = CommandManager.getDeaultInstance().getHistory();
		List<AbstractCommand> list = CommandManager.getDeaultInstance().getSelectedCommands();
		for (AbstractCommand cmd : list) {
			int index = history.getIndex(cmd);
			if (index < history.size() - 1)
				history.add(index++, history.remove(index));
		}
		CommandManager.getDeaultInstance().endCommand();
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();

	}

}
