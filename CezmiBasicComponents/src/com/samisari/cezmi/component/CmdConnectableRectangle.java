package com.samisari.cezmi.component;

import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;

public class CmdConnectableRectangle extends CmdRectangle implements CmdConnectable {
	private static final long	serialVersionUID	= 1L;

	HashMap<String, Point>		connectionPoints	= new HashMap<>();

	public CmdConnectableRectangle() {
		super();
	}

	@Override
	public void paintConnectionPoints(Graphics g) {

	}

	@Override
	public void putConnectionPoint(String name, Point p) {
		connectionPoints.put(name, p);
	}

	@Override
	public Point getConnectionPoint(String name) {
		return connectionPoints.get(name);
	}

}
