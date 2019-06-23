package com.samisari.graphics.toolbox;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.AbstractCommand;

public class ToolboxMouseListener implements MouseListener {
	public static final Logger logger = Logger
			.getLogger(ToolboxMouseListener.class);

	private String hoveringCommandName;
	
	@Override
	public void mouseClicked(MouseEvent e) {
		logger.debug(e.paramString());
		AbstractCommand cmd = null;
		try {
			ToolBox toolbox = (ToolBox) e.getComponent();
			cmd = toolbox.getCommand(e.getX(), e.getY());
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		logger.debug("\tActive command: "
				+ (cmd == null ? "NULL" : cmd.getClass().getName()));
		if (cmd != null)
			cmd.run();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
//		logger.debug(e.paramString());
//		AbstractCommand cmd = null;
//		try {
//			cmd = ((ToolBox)e.getComponent()).getCommand(e.getX(), e.getY());
//		} catch (InstantiationException e1) {
//			e1.printStackTrace();
//		} catch (IllegalAccessException e1) {
//			e1.printStackTrace();
//		}
//		logger.debug("\tActive command: "
//				+ (cmd == null ? "NULL" : cmd.getClass().getName()));
//		if (cmd == null)
//			setHoveringCommandName("");
//		else
//			setHoveringCommandName(cmd.getClass().getSimpleName());
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public String getHoveringCommandName() {
		return hoveringCommandName;
	}

	public void setHoveringCommandName(String hoveringCommandName) {
		this.hoveringCommandName = hoveringCommandName;
	}

}
