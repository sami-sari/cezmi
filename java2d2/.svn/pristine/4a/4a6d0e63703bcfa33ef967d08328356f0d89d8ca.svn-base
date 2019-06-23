package com.samisari.tools;

import java.awt.Component;
import java.awt.Container;

import org.apache.log4j.Logger;

public class DebugTools {
	public static final Logger logger = Logger.getLogger(DebugTools.class);
	
	public static void printComponents(Component c, int indent) {
		StringBuilder message = new StringBuilder();
		for (int j=0;j<indent;j++)
			message.append("\t");
		message.append(c.getClass().getName());
		message.append("\n");
		logger.debug(message.toString());
		if (c instanceof Container) {
			for (int i = 0; i < ((Container)c).getComponentCount(); i++) {
				printComponents(((Container)c).getComponent(i), indent+1);
			}
		}
	}

}
