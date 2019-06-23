package com.samisari.graphics.core;

import java.awt.Component;

import com.samisari.graphics.toolbox.ToolBox;
//import com.samisari.graphics.toolbox.ToolBoxSwing;

public class ToolBoxFactory {
	private static Component instance;

	public static Component getInstance() {
		if (instance == null) {
			instance = new ToolBox();
		}
		return instance;
	}
	
	private ToolBoxFactory() {
	}
}
