package com.samisari.cezmi.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.Status;
import com.samisari.common.util.bean.BeanUtils;

public class CmdRectangle extends AbstractCommand implements Serializable, Cloneable {
	private static final long	serialVersionUID	= 5226672462820897805L;
	public static final Logger	logger				= Logger.getLogger(CmdRectangle.class);
	private Color				borderColor;
	private Color				fillColor			= null;

	@Override
	public void run() {
		super.run();
		setBorderColor(ConsolePropertyManager.getDefaultInstance().getForegroundColor());
	}

	@Override
	public void mouseClicked(int x, int y) {
		logger.debug("X=" + x + " Y=" + y);
		switch (getCurrentStatus()) {
		case START: 
			setX(x);
			setY(y);
			setWidth(0);
			setHeight(0);
			setCurrentStatus(Status.DRAGGING);
			break;
		case DRAGGING: 
			setWidth(x - getX());
			setHeight(y - getY());
			CommandManager.getDeaultInstance().getHistory().add(this);
			CommandManager.getDeaultInstance().endCommand();
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
			setCurrentStatus(Status.START);
			break;
		default:
			break;
		}

	}

	@Override
	public void mouseMoved(int x, int y) {
		if (Status.DRAGGING == getCurrentStatus()) {
			setWidth(x - getX());
			setHeight(y - getY());
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
		}
	}

	@Override
	public void paint(Graphics g) {
		super.beforePaint(g, ConsolePropertyManager.getDefaultInstance().getScaleFactor(), ConsolePropertyManager.getDefaultInstance().getOffset());
		Color tmp = ConsolePropertyManager.getDefaultInstance().getForegroundColor();
		((Graphics2D) g).setStroke(new BasicStroke(1.0f));
		if (fillColor != null) {
			g.setColor(fillColor);
			g.fillRect(getX(), getY(), getWidth(), getHeight());
		}
		if (borderColor != null) {
			g.setColor(borderColor);
			g.drawRect(getX(), getY(), getWidth(), getHeight());
		}
		g.setColor(tmp);
		super.afterPaint(g);
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = BeanUtils.string2Color(borderColor);
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
	}

	public void setFillColor(String fillColor) {
		this.fillColor = BeanUtils.string2Color(fillColor);
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
	}

}
