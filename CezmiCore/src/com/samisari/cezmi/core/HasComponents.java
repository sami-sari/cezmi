package com.samisari.cezmi.core;

public interface HasComponents {
	public History getComponents();
	public void setComponents(History history);
	public void addComponent(AbstractCommand cmd);
	public void removeComponent(AbstractCommand cmd);
	
}
