package com.samisari.cezmi.animator.animationcommands;

import com.samisari.cezmi.core.AbstractCommand;

public class CezmiPropertyTransformation extends CezmiAbstractTransformation {
	private String	propertyName;
	private Object	oldValue;
	private Object	newValue;

	public CezmiPropertyTransformation(AbstractCommand object, String propertyName, Object oldValue, Object newValue, int startFrame, int endFrame) {
		setObject(object);
		setOldValue(oldValue);
		setPropertyName(propertyName);
		setNewValue(newValue);
	}

	@Override
	public void transform() {
		getObject().setPropertyValue(propertyName, newValue);
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}

	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}
}
