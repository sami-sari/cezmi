package com.samisari.graphics.commands;

import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.Operation;
import com.samisari.gui.panel.PropertiesPanel;

public class CmdProperties extends AbstractCommand implements Serializable {
	private static final long	serialVersionUID	= 5226672462820897805L;
	private static final Logger	logger				= Logger.getLogger(CmdProperties.class);

	@Override
	public void run() {
		super.run();
		List<AbstractCommand> commandList = CommandManager.getDeaultInstance().getHistory();
		for (AbstractCommand command : commandList) {
			if (command.isSelected()) {
				new PropertiesPanel().setVisible(true);
			}
		}
		CommandManager.getDeaultInstance().endCommand();
	}

	@Override
	public Point getOperationPoint(Operation operation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub

	}

}
