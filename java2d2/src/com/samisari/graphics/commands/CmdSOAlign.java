package com.samisari.graphics.commands;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.Status;

public class CmdSOAlign extends AbstractCommand {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5226672462820897805L;
	public static final Logger	logger				= Logger.getLogger(CmdSOAlign.class);
	private int					left, top, width, height;
	private Color				color;

	private Status	status	= Status.START;

	@Override
	public void mouseClicked(int x, int y) {
		logger.debug("Mouse clicked: x1=" + x + "x2=" + width);
			int minx=999999,maxx=0,miny=999999,maxy=0,minwidth=999999,maxwidth=0,minheight=999999,maxheight=0;
			List<AbstractCommand> selected= CommandManager.getDeaultInstance().getSelectedCommands();
			for (AbstractCommand cmd:selected) {
				Rectangle r=cmd.getBounds();
				if (r.x<minx) minx=r.x;
				if (r.x+r.width>maxx) maxx=r.x+r.width;
				if (r.y<miny) miny=r.y;
				if (r.y+r.height>maxy) maxy=r.y+r.height;
				if (r.width<minwidth) minwidth=r.width;
				if (r.width>maxwidth) maxwidth=r.width;
				if (r.height<minheight) minheight=r.height;
				if (r.height>maxheight) maxheight=r.height;
			}
			Rectangle leftAlign=new Rectangle(left - 3 * width / 2, top - height / 2, width, height);
			Rectangle rightAlign=new Rectangle(left + width / 2, top - height / 2, width, height);
			Rectangle topAlign=new Rectangle(left - width / 2, top - 3 * height / 2, width, height);
			Rectangle bottomAlign=new Rectangle(left - width / 2, top + height / 2, width, height);
			Rectangle sameSize=new Rectangle(left - width / 2, top - height / 2, width, height);

			if (leftAlign.contains(x, y)) {
				for (AbstractCommand cmd:selected) {
					cmd.setX(minx);
				}
			}
			if (rightAlign.contains(x, y)) {
				for (AbstractCommand cmd:selected) {
					cmd.setWidth(maxx-cmd.getX());
				}
			}
			if (topAlign.contains(x, y)) {
				for (AbstractCommand cmd:selected) {
					cmd.setY(miny);
				}
			}
			if (bottomAlign.contains(x, y)) {
				for (AbstractCommand cmd:selected) {
					cmd.setHeight(maxy-cmd.getY());
				}
			}
			if (sameSize.contains(x, y)) {
				for (AbstractCommand cmd:selected) {
					cmd.setHeight(maxheight);
					cmd.setWidth(maxwidth);
				}
			}
			
			CommandManager.getDeaultInstance().getHistory().remove(this);
			CommandManager.getDeaultInstance().endCommand();
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();

	}

	@Override
	public void run() {
		// DisplayMenu
		left=ConsolePropertyManager.getDefaultInstance().getOffset().x + 100;
		top=ConsolePropertyManager.getDefaultInstance().getOffset().y + 100;
		width=height=26;
		CommandManager.getDeaultInstance().getHistory().add(this);
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(ConsolePropertyManager.getDefaultInstance().getForegroundColor());
		g.drawRect(left - 3 * width / 2, top - height / 2, width, height);
		g.drawRect(left + width / 2, top - height / 2, width, height);
		g.drawRect(left - width / 2, top - 3 * height / 2, width, height);
		g.drawRect(left - width / 2, top + height / 2, width, height);

	}

}
