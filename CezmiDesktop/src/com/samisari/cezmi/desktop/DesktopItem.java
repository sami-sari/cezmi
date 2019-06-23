package com.samisari.cezmi.desktop;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.samisari.cezmi.component.CmdConnectableRectangle;
import com.samisari.cezmi.component.CmdImage;
import com.samisari.cezmi.component.CmdLayer;
import com.samisari.cezmi.component.CmdText;
import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.History;

public class DesktopItem extends CmdLayer<AbstractCommand> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public DesktopItem() {
		super();
		CmdConnectableRectangle outerRectangle = new CmdConnectableRectangle();
		outerRectangle.setBorderColor(Color.BLACK);
		outerRectangle.setFillColor(Color.WHITE);
		
		outerRectangle.setBounds(new Rectangle(10, 10, 100, 100));
		
		CmdConnectableRectangle shadowRectangle = new CmdConnectableRectangle();
		shadowRectangle.setBorderColor(Color.BLACK);
		shadowRectangle.setFillColor(Color.BLACK);
		shadowRectangle.setBounds(new Rectangle(12, 12, 100, 100));
		
		CmdImage image = new CmdImage();
		try {
			image.setImage(ImageIO.read(this.getClass().getResourceAsStream("/folder.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		image.setBounds(new Rectangle(12,12,96,96));
		
		CmdText text = new CmdText();
		text.setText("Test");
		text.setBounds(new Rectangle(12, 115, 96, 20));
		text.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		addComponent(shadowRectangle);
		addComponent(outerRectangle);
		addComponent(image);
		addComponent(text);
		
	}
	
	public History parse(String description) {
		CezmiParser parser = new CezmiParser();
		return null;
	}
}
