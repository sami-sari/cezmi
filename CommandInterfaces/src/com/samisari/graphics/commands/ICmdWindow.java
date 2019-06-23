package com.samisari.graphics.commands;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

public interface ICmdWindow extends ICmdRectangle  {
	public Font getTitleFont();
	public String getTitleText();
	public Color getTitleColor();
	public String getWindowListener();
	List<ICmdRectangle> getComponents();
}
