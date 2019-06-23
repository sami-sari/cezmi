package com.samisari.trash.com.samisari.graphics.core;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.samisari.graphics.core.util.ImageTool;

public class ImageCache extends HashMap<String, BufferedImage> {
	private static ImageCache	instance;

	public static synchronized ImageCache getInstance() {
		if (instance == null) {
			instance = new ImageCache();
			instance.loadToolButtonIcons();
		}
		return instance;
	}

	private void loadToolButtonIcons() {
		put("",ImageTool.getResourceAsImage(""));
		
	}

	private ImageCache() {

	}
}
