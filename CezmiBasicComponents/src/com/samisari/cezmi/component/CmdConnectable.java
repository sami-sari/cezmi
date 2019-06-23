package com.samisari.cezmi.component;

import java.awt.Graphics;
import java.awt.Point;

public interface CmdConnectable {
	public static final String TOP_LEFT = "TL";
	public static final String TOP_CENTER = "TC";
	public static final String TOP_RIGHT = "TR";
	public static final String MIDDLE_LEFT = "ML";
	//public static final String MIDDLE_CENTER = "MC";
	public static final String MIDDLE_RIGHT = "MR";
	public static final String BOTTOM_LEFT = "BL";
	public static final String BOTTOM_CENTER = "BC";
	public static final String BOTTOM_RIGHT = "BR";
	
	
	void putConnectionPoint(String name, Point p);
	Point getConnectionPoint(String name);
	void paintConnectionPoints(Graphics g);
}
