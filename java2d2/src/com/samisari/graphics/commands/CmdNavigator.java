package com.samisari.graphics.commands;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.Status;

public class CmdNavigator extends CmdZoom {
	private static final Logger logger = Logger.getLogger(CmdNavigator.class);
	private double zoomFactor;
	private Point offset = new Point(0, 0);

	protected Status status = Status.START;

	@Override
	public void run() {
		if (CommandManager.getDeaultInstance().getHistory().isEmpty()) {
			CommandManager.getDeaultInstance().endCommand();
		}
		// if (CommandManager.getInstance().getHistory().get(0) instanceof
		// CmdZoom) {
		// CommandManager.getInstance().getHistory().remove(0);
		// }
		Rectangle r = CommandManager.getDeaultInstance().getHistory().getMaxBounds();
		double curScale = ConsolePropertyManager.getDefaultInstance().getScaleFactor();
		Dimension consoleSize = ConsolePropertyManager.getDefaultInstance().getConsolePanel().getSize();
		double xScale = ((double) r.width) / (((double) consoleSize.width) / curScale);
		double yScale = ((double) r.height) / (((double) consoleSize.height) / curScale);
		double scale = 1.00 / Math.ceil(Math.max(xScale, yScale));
		logger.debug("Scale factor=" + scale);
		if (scale >= 1.00) {
			ConsolePropertyManager.getDefaultInstance().setOffset(new Point(0, 0));
			CommandManager.getDeaultInstance().endCommand();
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
		} else {
			setZoomFactor(1.0);
			ConsolePropertyManager.getDefaultInstance().setScaleFactor(scale);
			ConsolePropertyManager.getDefaultInstance().setOffset(new Point(0, 0));
			Thread robotThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						Robot robot = new Robot();
//						robot.setAutoWaitForIdle(false);
//						robot.setAutoDelay(0);
						Point locationOnScreen = ConsolePropertyManager.getDefaultInstance().getConsolePanel().getLocationOnScreen();
//						robot.waitForIdle();
						robot.mouseMove(locationOnScreen.x , locationOnScreen.y );
					} catch (AWTException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			robotThread.run();
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
			status = Status.END;
		}
	}

	@Override
	public void mouseClicked(int x, int y) {
		super.mouseClicked(x, y);
		ConsolePropertyManager.getDefaultInstance().setOffset(new Point((int) (x / getZoomFactor()), (int) (y / getZoomFactor())));
		ConsolePropertyManager.getDefaultInstance().setScaleFactor(1.00);
		status = Status.START;
		CommandManager.getDeaultInstance().endCommand();
		// CommandManager.getInstance().getHistory().add(0, this);
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
	}

	@Override
	public void mouseMoved(int x, int y) {
		super.mouseMoved(x, y);
		offset = new Point((int) (x / getZoomFactor()), (int) (y / getZoomFactor()));
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
	}

	@Override
	public void paint(Graphics g) {
		if (status.equals(Status.END)) {
			// Graphics2D g2d = (Graphics2D) g;
			Dimension size = ConsolePropertyManager.getDefaultInstance().getConsolePanel().getSize();
			// double sx = getZoomFactor(), sy = getZoomFactor();
			// AffineTransform scale = AffineTransform.getScaleInstance(sx, sy);
			// g2d.transform(scale);
			Color tmp = ConsolePropertyManager.getDefaultInstance().getForegroundColor();
			((Graphics2D) g).setStroke(new BasicStroke(1.0f));
			g.setColor(Color.RED);
			g.drawRect(offset.x, offset.y, size.width, size.height);
			g.setColor(tmp);
		}
	}

	public static String getCommandName() {
		return "Nav";
	}

	@Override
	public double getZoomFactor() {
		return zoomFactor;
	}

	@Override
	public void setZoomFactor(double zoomFactor) {
		this.zoomFactor = zoomFactor;
	}

	@Override
	public String toString() {
		return "Navigator";
	}

	public Point getOffset() {
		return offset;
	}

	public void setOffset(Point offset) {
		this.offset = offset;
	}
}