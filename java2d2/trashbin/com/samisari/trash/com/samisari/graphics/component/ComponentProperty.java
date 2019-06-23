package com.samisari.trash.com.samisari.graphics.component;

import java.util.ArrayList;
import java.util.List;

public abstract class ComponentProperty<T> {
	private String										name;
	private T											value;
	private List<ComponentPropertyChangeListener<T>>	listeners;

	public ComponentProperty() {
		listeners = new ArrayList<ComponentPropertyChangeListener<T>>();
		value.getClass();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		T oldValue = this.value;
		this.value = value;
		firePropertyChange(oldValue);
	}

	private void firePropertyChange(T oldValue) {
		for (ComponentPropertyChangeListener<T> l : listeners) {
			l.propertyChanged(this, oldValue);
		}

	}

	public abstract String toDisplayString();

	public String toXmlString() {
		return "<" + name + ">" + value.toString() + "</" + name + ">";
	}

	public byte[] toCompressedForm() {
		return toXmlString().getBytes();
	}

	public abstract String toFormattedString(String format);

	public void addComponentPropertyChangeListener(ComponentPropertyChangeListener<T> l) {
		listeners.add(l);
	}
}
