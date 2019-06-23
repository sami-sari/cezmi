package com.samisari.cezmi.core;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.samisari.common.util.ImageTool;

public interface ICommandConstants {
	public static final Color			SELECTED_COLOR			= Color.RED;
	public static final int				SELECTION_MARKER_SIZE	= 5;
	public static final BufferedImage	PROCESS_ICON			= ImageTool.getResourceAsImage("com/samisari/graphics/resources/process-icon-10x10.png");
	public static final BufferedImage	LT_ICON					= ImageTool.getResourceAsImage("com/samisari/graphics/resources/topleft-icon-10x10.png");
	public static final BufferedImage	LB_ICON					= ImageTool.getResourceAsImage("com/samisari/graphics/resources/bottomleft-icon-10x10.png");
	public static final BufferedImage	RT_ICON					= ImageTool.getResourceAsImage("com/samisari/graphics/resources/topright-icon-10x10.png");
	public static final BufferedImage	RB_ICON					= ImageTool.getResourceAsImage("com/samisari/graphics/resources/bottomright-icon-10x10.png");
	public static final BufferedImage	T_ICON					= ImageTool.getResourceAsImage("com/samisari/graphics/resources/top-icon-10x10.png");
	public static final BufferedImage	B_ICON					= ImageTool.getResourceAsImage("com/samisari/graphics/resources/bottom-icon-10x10.png");
	public static final BufferedImage	L_ICON					= ImageTool.getResourceAsImage("com/samisari/graphics/resources/left-icon-10x10.png");
	public static final BufferedImage	R_ICON					= ImageTool.getResourceAsImage("com/samisari/graphics/resources/right-icon-10x10.png");

}
