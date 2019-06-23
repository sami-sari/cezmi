package com.samisari.graphics.commands;

import java.awt.Graphics;

public class CmdZoomIn extends CmdZoom {
	private double zoomFactor = 1.10;

	public static String getCommandName() {
		return "Z+";
	}

	public double getZoomFactor() {
		return zoomFactor;
	}

	public void setZoomFactor(double zoomFactor) {
		this.zoomFactor = zoomFactor;
	}

	@Override
	public void paint(Graphics g) {
	}

}
