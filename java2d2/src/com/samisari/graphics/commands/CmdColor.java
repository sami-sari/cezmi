package com.samisari.graphics.commands;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JColorChooser;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.Java2DFPanel;
import com.samisari.cezmi.core.Operation;

public class CmdColor extends AbstractCommand {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3501282346107558607L;
	private Java2DFPanel panel;

	@Override
	public void redo() {
		// TODO implementation pending
	}

	@Override
	public void undo() {
		// TODO implementation pending

	}

	public void run() {
		ConsolePropertyManager.getDefaultInstance().setForegroundColor(
				JColorChooser
						.showDialog(panel, "Bir renk seçiniz", ConsolePropertyManager.getDefaultInstance().getForegroundColor()));
		CommandManager.getDeaultInstance().endCommand();
	}

	@Override
	public void mouseClicked(int x, int y) {
		// TODO implementation pending

	}

	@Override
	public void mouseMoved(int x, int y) {
	}

	@Override
	public void mousePressed(int x, int y) {
	}

	@Override
	public void mouseReleased(int x, int y) {
	}

	@Override
	public void paint(Graphics g) {
	}

	@Override
	public Point getOperationPoint(Operation operation) {
		return null;
	}

}
