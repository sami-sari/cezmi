package com.samisari.graphics.commands;


public interface ICmdJTextBox extends ICmdRectangle {
	public String getName();
	public String getMask();
	public int getMaxLength();
	public String getChangeEventHandler();
}
