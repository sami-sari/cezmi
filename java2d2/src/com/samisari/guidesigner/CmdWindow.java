package com.samisari.guidesigner;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import com.samisari.cezmi.component.CmdConnectableRectangle;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.common.util.ParserUtils;
import com.samisari.graphics.commands.ICmdRectangle;
import com.samisari.graphics.commands.ICmdWindow;
import com.samisari.java2d.textwriter.commands.window.Window;

public class CmdWindow extends CmdConnectableRectangle implements ICmdWindow, Serializable {
	private static final long serialVersionUID = 1L;
	protected Font titleFont = new Font("Sans Serif", Font.BOLD, 14);
	protected String titleText = "Window Title";
	protected Color titleColor = Color.BLUE;
	private String code;
	private String windowListener;

	@Override
	public void paint(Graphics g) {
		logger.debug(getClass().getSimpleName() + "painting @(" + getX() + "," + getY() + ")");
		super.beforePaint(g, ConsolePropertyManager.getDefaultInstance().getScaleFactor(), ConsolePropertyManager.getDefaultInstance().getOffset());
		Color tempColor = g.getColor();
		Font tempFont = g.getFont();
		g.setColor(titleColor);
		g.drawRect(getX(), getY(), getRight() - getX(), 30);
		g.fillRect(getX(), getY(), getRight() - getX(), 30);
		g.setFont(titleFont);
		g.setColor(Color.WHITE);
		if (titleText != null)
			g.drawString(getTitleText(), getX() + 10, getY() + 20);
		super.afterPaint(g);
		super.paint(g);
		g.setFont(tempFont);
		g.setColor(tempColor);
	}

	public Font getTitleFont() {
		return titleFont;
	}

	public void setTitleFont(Font titleFont) {
		this.titleFont = titleFont;
	}

	public void setTitleFont(String titleFontProperty) {
		if (titleFontProperty.startsWith("Font(")) {
			int[] start = { 0 };
			String familly = ParserUtils.getTextBetween(titleFontProperty, "(", ",", start);
			int style = Integer.parseInt(ParserUtils.getTextBetween(titleFontProperty, "", ",", start));
			int size = Integer.parseInt(ParserUtils.getTextBetween(titleFontProperty, "", ")", start));
			Font font = new Font(familly, style, size);
			setTitleFont(font);
		}
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
	}

	public String getTitleText() {
		return titleText;
	}

	public void setTitleText(String titleText) {
		this.titleText = titleText;
	}

	public Color getTitleColor() {
		return titleColor;
	}

	public void setTitleColor(Color titleColor) {
		this.titleColor = titleColor;
	}

	public void setTitleColor(String titleColorProperty) {
		if (titleColorProperty.startsWith("ColorRGB(")) {
			int[] start = { 0 };
			int r = Integer.parseInt(ParserUtils.getTextBetween(titleColorProperty, "(", ",", start));
			int g = Integer.parseInt(ParserUtils.getTextBetween(titleColorProperty, "", ",", start));
			int b = Integer.parseInt(ParserUtils.getTextBetween(titleColorProperty, "", ")", start));
			Color color = new Color(r, g, b);
			setTitleColor(color);
		}
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
	}

	public String getCode(String language, Locale locale) {
		return new Window(this, "aWindow").getCode();
	}

	public String getCode() {
		return new Window(this, "aWindow").getCode();
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getWindowListener() {
		return windowListener;
	}

	public void setWindowListener(String windowListener) {
		this.windowListener = windowListener;
	}

	@Override
	public List<ICmdRectangle> getComponents() {
		// TODO Auto-generated method stub
		return null;
	}
}
