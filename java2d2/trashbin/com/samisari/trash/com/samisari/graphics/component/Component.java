package com.samisari.trash.com.samisari.graphics.component;

import java.util.List;

public abstract class Component {
	private Component parentComponent;
	private ComponentPropertyList properties;
	private List<ComponentPropertyChangeListener> listeners;
	
}
