package com.samisari.cezmi.component;

import java.awt.Rectangle;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.History;

public class CmdUnLayer extends AbstractCommand {
	private static final long	serialVersionUID	= 5226672462820897805L;
	public static final Logger	logger				= Logger.getLogger(CmdUnLayer.class);

	@Override
	public void run() {
		logger.debug("Deleting Selected Objects");
		
		for (AbstractCommand cmd : CommandManager.getDeaultInstance().getSelectedCommands()) {
			if (cmd instanceof CmdLayer) {
				CommandManager.getDeaultInstance().getHistory().remove(cmd);
				History h = new History(((CmdLayer<?>)cmd).getComponents());
				int x = cmd.getBounds().x;
				int y = cmd.getBounds().y;
				
				for (AbstractCommand c:h) {
					Rectangle oldBounds = c.getBounds();
					int dx = c.getBounds().x;
					int dy = c.getBounds().y;
					int width = c.getBounds().width;
					int height = c.getBounds().height;
					c.setBounds(new Rectangle(x+dx, y+dy, width, height));
					c.boundaryChanged(oldBounds, c.getBounds());
					CommandManager.getDeaultInstance().getHistory().add(c);
				}
			}
			cmd.onDelete();
			CommandManager.getDeaultInstance().getHistory().remove(cmd);
		}
		CommandManager.getDeaultInstance().endCommand();
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
	}

}
