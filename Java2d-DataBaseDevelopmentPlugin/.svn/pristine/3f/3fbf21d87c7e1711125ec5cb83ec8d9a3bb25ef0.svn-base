package com.samisari.graphics.commands;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;

import com.samisari.cezmi.component.CmdRectangle;
import com.samisari.common.util.ImageTool;

public class CmdDropDown extends CmdRectangle{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private String name;
	private String items;
	private String data;
	
	// Console paint
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Color originalColor = g.getColor();
		g.setColor(Color.BLACK);
		g.fillRect(getRight() - 22, getX()+ 2, 20, 20);
		g.setColor(Color.WHITE);
		Polygon triangle = new Polygon();
		triangle.addPoint(getRight() - 17, getX() + 8);
		triangle.addPoint(getRight() - 7, getX() + 8);
		triangle.addPoint(getRight() - 12, getX() + 16);
		g.fillPolygon(triangle);
		g.setColor(originalColor);
		
		
	}
	// ToolBox button
	public static String getCommandButtonText(){
		return "Combo box";
	}
	public static Image getCommandButtonIcon(){
		return ImageTool.getResourceAsImage("com/samisari/graphics/commands/resources/CmdDropDown.png");
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getItems() {
		return items;
	}
	public void setItems(String items) {
		this.items = items;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	// Generation

}
