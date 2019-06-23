package com.samisari.cezmi.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ConsolePropertyManager {
	public static int										SELECTION_RADIUS	= 5;
	private static HashMap<String, ConsolePropertyManager>	instance;
	private Color											foregroundColor		= Color.BLACK;
	private JPanel											consolePanel;
	private boolean											showGrid;
	private double											scaleFactor			= 1.00;
	private Point											offset				= new Point(-10, -10);
	private JFrame											applicationFrame;
	private AbstractCommand									activeCommand;
	private Operation										activeOp;
	private int												stepping			= 1;

	private ConsolePropertyManager() {
		super();
	}

	public static ConsolePropertyManager getDefaultInstance() {
		return getInstance("DEFAULT");
	}

	public static ConsolePropertyManager getInstance(String key) {
		if (instance == null)
			instance = new HashMap<>();
		if (instance.get(key) == null)
			instance.put(key, new ConsolePropertyManager());

		return instance.get(key);

	}

	public Color getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	public void setConsolePanel(Java2DFPanel consolePanel) {
		this.consolePanel = consolePanel;
	}

	public JPanel getConsolePanel() {
		return consolePanel;
	}

	public double getScaleFactor() {
		// int i = 0;
		// float scaleFactor = 1;
		// if (CommandManager.getInstance().getHistory().size() > 0) {
		// AbstractCommand nextCommand =
		// CommandManager.getInstance().getHistory().get(i);
		// while (nextCommand instanceof CmdZoom) {
		// scaleFactor *= 1.00 / ((CmdZoom) nextCommand).getZoomFactor();
		// i++;
		// nextCommand = CommandManager.getInstance().getHistory().get(i);
		// }
		// }
		return scaleFactor;
	}

	public Point getOffset() {
		// int i = 0;
		// Point offset = new Point(0,0);
		// if (CommandManager.getInstance().getHistory().size() > 0) {
		// AbstractCommand nextCommand =
		// CommandManager.getInstance().getHistory().get(i);
		// while (nextCommand instanceof CmdNavigator) {
		// offset = ((CmdNavigator) nextCommand).getOffset();
		// i++;
		// nextCommand = CommandManager.getInstance().getHistory().get(i);
		// }
		// }
		return offset;
	}

	public void setScaleFactor(double scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	public void setOffset(Point offset) {
		this.offset = offset;
	}

	public void paint(Graphics g) {
		if (((int) (getScaleFactor() * 1000)) != 1000) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
			g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

			AffineTransform scale = AffineTransform.getScaleInstance(this.getScaleFactor(), this.getScaleFactor());
			g2d.transform(scale);
		}

		if (getOffset().x != 0 || getOffset().y != 0) {
			Graphics2D g2d = (Graphics2D) g;
			AffineTransform translate = AffineTransform.getTranslateInstance(-offset.x, -offset.y);
			g2d.transform(translate);
		}
	}

	public void setApplicationFrame(JFrame frame) {
		applicationFrame = frame;
	}

	public JFrame getApplicationFrame() {
		return applicationFrame;
	}

	public boolean isShowGrid() {
		return showGrid;
	}

	public void setShowGrid(boolean showGrid) {
		this.showGrid = showGrid;
	}

	public AbstractCommand getActiveCommand() {
		return activeCommand;
	}

	public void setActiveCommand(AbstractCommand activeCommand) {
		this.activeCommand = activeCommand;
	}

	public Operation getActiveOp() {
		return activeOp;
	}

	public void setActiveOp(Operation activeOp) {
		this.activeOp = activeOp;
	}

	public int getStepping() {
		return stepping;
	}

	public void setStepping(int stepping) {
		this.stepping = stepping;
	}
}
