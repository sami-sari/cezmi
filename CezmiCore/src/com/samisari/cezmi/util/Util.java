package com.samisari.cezmi.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;

public class Util {

	public static void drawRect(final Graphics g, final int x1, final int y1, final int x2, final int y2) {
		g.drawRect(x1 > x2 ? x2 : x1, y1 > y2 ? y2 : y1, Math.abs(x1 - x2), Math.abs(y1 - y2));
	}

	public static void drawRect(final Graphics g, Rectangle rectangle) {
		g.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
	}

	public static void drawLine(final Graphics g, Point p1, Point p2) {
		g.drawLine(p1.x, p1.y, p2.x, p2.y);
	}

	public static void drawCircle(final Graphics g, Point p1, double r) {
		((Graphics2D) g).drawOval((int) Math.round(p1.x - r), (int) Math.round(p1.y - r), (int) Math.round(2 * r), (int) Math.round(2 * r));
	}

	public static Rectangle getScreenBounds() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
	}

	public static double slope(Point lineStart, Point lineEnd) {
		return Math.atan2((double) (lineEnd.y - lineStart.y), (double) (lineEnd.x - lineStart.x));
	}
	public static double distance(Point start, Point end) {
		return Math.sqrt(Math.pow((double) (end.y - start.y), 2.0) + Math.pow((double) (end.x - start.x),2.0));
	}

	public static void drawString(Graphics g, String string, int x, int y) {
		int h = ((Graphics2D)g).getFontMetrics().getHeight();
		g.drawString(string,x, y - h);
		
	}

	
}
