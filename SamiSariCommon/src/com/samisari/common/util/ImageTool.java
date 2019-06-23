package com.samisari.common.util;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

public class ImageTool {
	private static final org.apache.log4j.Logger logger = Logger
			.getLogger(ImageTool.class);
	public static BufferedImage getResourceAsImage(String resourcePath) {
		BufferedImage image = null;// new
		// BufferedImage(16,16,BufferedImage.TYPE_INT_RGB);
		try {
			image = ImageIO
					.read(ClassLoader
							.getSystemResourceAsStream(resourcePath));
		} catch (Exception e) {
			logger.debug(resourcePath + "not found");
		}
		return image;
	}
	
	
	public static Cursor createCursor(String name, String resourcePath, int hotSpotX, int hotSpotY) {
		BufferedImage image = getResourceAsImage(resourcePath);
		Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(image,
				new Point(hotSpotX, hotSpotY), name);
		return cursor;
	}
	
}
