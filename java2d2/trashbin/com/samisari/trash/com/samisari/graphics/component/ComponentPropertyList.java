package com.samisari.trash.com.samisari.graphics.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ComponentPropertyList {
	List<ComponentProperty>	properties	= new ArrayList<ComponentProperty>();
	HashMap					index		= new HashMap<String, ComponentProperty>();

	public void add(ComponentProperty<?> property) {
		properties.add(property);
		index.put(property.getName(), property);
	}

	public void get(int i) {
		properties.get(i);
	}

	public void get(String name) {
		index.get(name);
	}

}
