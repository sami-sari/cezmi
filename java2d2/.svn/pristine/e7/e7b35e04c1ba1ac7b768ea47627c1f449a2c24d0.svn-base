package com.samisari.trash.com.samisari.graphics.uml.classdiagram;

import java.awt.Graphics;
import java.util.Properties;

public class Box {

	Properties properties;
	
	public void draw(Graphics g){
		int x1 = Integer.parseInt((String) properties.get("start.x"));
		int y1 = Integer.parseInt((String) properties.get("start.y"));
		int x2 = Integer.parseInt((String) properties.get("end.x"));
		int y2 = Integer.parseInt((String) properties.get("end.y"));
		g.drawRect(x1, y1, x2-x1, y2 - y1);
		String className = (String) properties.get("className");
		g.drawString(className, x1 + 5, y1 + 20);
		g.drawLine(x1, y1 + 25, x2, y2 + 25);
		int x = x1 + 5; 
		int y = y1 + 25;
		
		for (int i = 1; properties.containsKey("field" + i);i++) {
			String field = "field" + i;
			String fieldName = (String) properties.get(field + ".name"); // name of the field
			String fieldType = (String) properties.get(field + ".type"); // Class or primitive type
			String isArray   = (String) properties.get(field + ".isArray"); 
			String fieldAccess = (String) properties.get(field + ".access"); // public, private, protected, package
			String fieldModifiers = (String) properties.get(field + ".modifier"); // static, final, volatile
			g.drawString(fieldName, x , y + 20);
			y += 20;
		}
		g.drawLine(x1, y, x2, y);
		for (int i = 1; properties.containsKey("operation" + i);i++) {
			String operation = "operation" + i;
			String operationName = (String) properties.get(operation + ".name"); // name of the operation
			String operationType = (String) properties.get(operation + ".type"); // Class or primitive type or void 
			String isArray   = (String) properties.get(operation + ".isArray"); 
			String operationAccess = (String) properties.get(operation + ".access"); // public, private, protected, package
			String operationModifiers = (String) properties.get(operation + ".modifier"); // static, final, volatile
			g.drawString(operationName, x , y + 20);
			y += 20;
		}

	}

}
