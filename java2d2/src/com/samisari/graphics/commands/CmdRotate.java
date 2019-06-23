package com.samisari.graphics.commands;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.List;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.Status;

public class CmdRotate extends AbstractCommand {
	public static final Logger logger = Logger.getLogger(CmdRotate.class);
	private double angle = 0.0d;
	private Point origin = null;
	private String commandId;

	protected Status status = Status.START;

	public static String getCommandName() {
		return "Rot";
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	@Override
	public void mouseClicked(int x, int y) {
		logger.debug("Mouse clicked: left=" + x + ", right=" + y);
		switch (status) {
		case START: {
			setX(x);
			setY(y);
			origin = new Point(x, y);
			angle = 0.0d;
			setWidth(0);
			setHeight(0);
			status = Status.DRAGGING;
		}
			break;
		case DRAGGING: {
			setWidth(x - getX());
			setHeight(y - getY());

			if (x - getX() == 0) {
				angle = 0.0d;
			} else {
				angle = Math.atan((double)(y - getY()) / (double)(x - getX()));
			}
			logger.debug(origin);
			logger.debug(angle);
			CommandManager.getDeaultInstance().getCommand(commandId).setTransform(AffineTransform.getRotateInstance(angle, getX(), getY()));
			CommandManager.getDeaultInstance().endCommand();
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
			status = Status.START;
		}
		}

	}

	@Override
	public void mouseMoved(int x, int y) {
		if (status.equals(Status.DRAGGING)) {
			if (x - getX() == 0) {
				angle = 0.0d;
			} else {
				angle = Math.atan((double)(y - getY()) / (double)(x - getX()));
			}
			logger.debug("Rotating" + angle);
			setWidth(x-getX());
			setHeight(y-getY());
			CommandManager.getDeaultInstance().getCommand(commandId).setTransform(AffineTransform.getRotateInstance(angle, getX(), getY()));
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
		}
	}

	@Override
	public void paint(Graphics g) {
		Color originalColor = g.getColor();
		g.setColor(Color.BLACK);
		drawDebug(g);
		if (status.equals(Status.DRAGGING)) {
			logger.debug("drawing line from (" + getX() + "," + getY() + ") to (" + (getX() + getWidth()) + "," + (getY() + getHeight()));
			g.drawLine(getX(), getY(), getX() + getWidth(), getY() + getHeight());
		}
		g.setColor(originalColor);
	}

	private void drawDebug(Graphics g) {
		Graphics2D gr = (Graphics2D) g;
		gr.drawString("Rotating object x=" + getX() + " y=" + getY() + " angle=" + angle, 20, 20);
	}

	@Override
	public void run() {
		List<AbstractCommand> selected = CommandManager.getDeaultInstance().getSelectedCommands();
		if (selected.size() == 1) {
			commandId = selected.get(0).getId();
		} else {
			CommandManager.getDeaultInstance().endCommand();
		}
	}

	public Point getOrigin() {
		return origin;
	}

	public void setOrigin(Point origin) {
		this.origin = origin;
	}

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}
}
