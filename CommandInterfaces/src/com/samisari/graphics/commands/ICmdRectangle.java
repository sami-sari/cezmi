package com.samisari.graphics.commands;

import java.awt.Color;

import com.samisari.graphics.core.IAbstractCommand;

public interface ICmdRectangle extends IAbstractCommand{
	public int getX();
	public int getY();
	public int getWidth();
	public int getHeight();
	public Color getBorderColor();
	public Color getFillColor();
}
