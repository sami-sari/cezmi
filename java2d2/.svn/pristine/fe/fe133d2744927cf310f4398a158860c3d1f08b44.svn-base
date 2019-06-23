package com.samisari.trash.com.samisari.graphics.uml;

import java.awt.Graphics;
import java.util.Properties;

public class Connector {
	Properties properties;
	
	public void draw(Graphics g){
		int x1 = Integer.parseInt((String) properties.get("start.x"));
		int y1 = Integer.parseInt((String) properties.get("start.y"));
		int x2 = -1;
		int y2 = -1;
		for (int i = 1; properties.containsKey("node" + i);i++) {
			String node = "node" + i;
			x2 = Integer.parseInt((String) properties.get(node + ".x"));
			y2 = Integer.parseInt((String) properties.get(node + ".y"));
			g.drawLine(x1, y1, x2, y2);
			x1 = x2;
			y1 = y2;
		}
		x2 = Integer.parseInt((String) properties.get("end.x"));
		y2 = Integer.parseInt((String) properties.get("end.y"));
		g.drawLine(x1, y1, x2, y2);
		if (properties.containsKey("style")) {
			String style = (String) properties.get("style");
			if ("arrowed".equals(style)) {
				drawArrowHead(x1,y1,x2,y2,g);
			}
		}
	}
	private void drawArrowHead(int x1,int y1,int x2,int y2, Graphics g){
		
	}
}
