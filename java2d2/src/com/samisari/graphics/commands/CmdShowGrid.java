package com.samisari.graphics.commands;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.Operation;

public class CmdShowGrid extends AbstractCommand {
	private int		gridSize	= 5;
	private Color	gridColor	= Color.LIGHT_GRAY;
	
	@Override
	public void run() {
		ConsolePropertyManager.getDefaultInstance().setShowGrid(ConsolePropertyManager.getDefaultInstance().isShowGrid());
		CommandManager.getDeaultInstance().endCommand();
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		int height=ConsolePropertyManager.getDefaultInstance().getConsolePanel().getSize().height;
		int width=ConsolePropertyManager.getDefaultInstance().getConsolePanel().getSize().width;
		Color temp = ConsolePropertyManager.getDefaultInstance().getForegroundColor();
		g.setColor(gridColor);
		//((Graphics2D) g).getPaint().;// ConsolePropertyManager.getInstance().getConsolePanel().getGraphics()
		for (int hly=0;hly<=height;hly+=gridSize) {
			for (int vlx=0;vlx<=width;vlx+=gridSize) {
				g.drawRect(vlx, hly, 1, 1);
			}
		}
//		for (int vlx=0;vlx<=width;vlx+=gridSize) {
//			g.drawLine(vlx, 0, vlx, height);
//		}
		g.setColor(temp);
	}

	@Override
	public Point getOperationPoint(Operation operation) {
		// TODO Auto-generated method stub
		return null;
	}

}
