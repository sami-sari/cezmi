package com.samisari.graphics.commands;

public class FieldInfo {
	String name;
	Class<?> type;
	String getterMethodName;
	String setterMethodName;
	Class<?> propertyEditor;

	public FieldInfo(String name, Class<?> type, String getterMethodName, String setterMethodName, Class<?> propertyEditor) {
		setName(name);
		setType(type);
		setGetterMethodName(getterMethodName);
		setSetterMethodName(setterMethodName);
		setPropertyEditor(propertyEditor);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public String getGetterMethodName() {
		return getterMethodName;
	}

	public void setGetterMethodName(String getterMethodName) {
		this.getterMethodName = getterMethodName;
	}

	public String getSetterMethodName() {
		return setterMethodName;
	}

	public void setSetterMethodName(String setterMethodName) {
		this.setterMethodName = setterMethodName;
	}

	public Class<?> getPropertyEditor() {
		return propertyEditor;
	}

	public void setPropertyEditor(Class<?> propertyEditor) {
		this.propertyEditor = propertyEditor;
	}

}
