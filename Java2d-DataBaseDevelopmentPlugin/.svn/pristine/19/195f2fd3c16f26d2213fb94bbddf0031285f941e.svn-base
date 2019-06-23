package com.samisari.graphics.commands;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class LayoutUtil extends GridBagConstraints {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 271649339460091657L;
	private GridBagConstraints	base;

	public LayoutUtil() {
		super(0, 0, 1, 1, 1.0d, 1.0d, GridBagConstraints.BASELINE, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		// base = new GridBagConstraints();
	}

	public LayoutUtil incrementX() {
		// base.gridx++;
		gridx++;
		return this;
	}

	public LayoutUtil incrementY() {
		// base.gridy++;
		gridy++;
		return this;
	}

	public LayoutUtil setX(int x) {
		// base.gridx++;
		gridx = x;
		return this;
	}

	public LayoutUtil setY(int y) {
		// base.gridy++;
		gridy = y;
		return this;
	}

	public LayoutUtil setWidth(int width) {
		// base.gridwidth=width;
		gridwidth = width;
		return this;
	}

	public LayoutUtil setHeight(int height) {
		// base.gridwidth=width;
		gridheight = height;
		return this;
	}

	public LayoutUtil setWeightX(double d) {
		weightx = d;
		return this;
	}

	public LayoutUtil setWeightY(double d) {
		weighty = d;
		return this;
	}

}
