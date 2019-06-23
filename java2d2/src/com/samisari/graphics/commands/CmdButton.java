package com.samisari.graphics.commands;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import com.samisari.cezmi.component.CmdConnectableRectangle;

public class CmdButton extends CmdConnectableRectangle implements ICmdButton{
	private String	text	= "Button";
	private String	actionHandler;

	private enum TextAlignment {
		LEFT, CENTER, RIGHT
	}

	TextAlignment	textAlign;

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		try {
			Color tmpColor = g.getColor();
			Font tmpFont = g.getFont();
			g.setFont(new Font("Dialog", Font.BOLD, 14));
			Rectangle2D textBounds = g.getFontMetrics().getStringBounds(text, g);
			int textX = getBounds().x + ((getBounds().width - textBounds.getBounds().width) / 2);
			int textY = getBounds().y + ((getBounds().height - textBounds.getBounds().height) / 2) + textBounds.getBounds().height;
			g.drawString(text, textX, textY);
			g.setFont(tmpFont);
			g.setColor(tmpColor);
		} catch (Throwable t) {
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getActionHandler() {
		return actionHandler;
	}

	public void setActionHandler(String actionHandler) {
		this.actionHandler = actionHandler;
	}

}
