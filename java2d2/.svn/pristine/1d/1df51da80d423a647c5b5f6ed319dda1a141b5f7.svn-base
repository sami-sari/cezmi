package com.samisari.trash.com.samisari.graphics.component;

import javax.swing.JComponent;
import javax.swing.JTextField;

public abstract class ComponentPropertyEditor<T extends ComponentProperty> {
	private static final String PROPERTY_EDITOR_CLASS_PATH = "com.samisari.utils.propertyeditor.editorclasses.";
	public Object getEditor(T property) throws Exception {
		String className = property.getValue().getClass().getSimpleName();
		return Class.forName(PROPERTY_EDITOR_CLASS_PATH + className + "Editor").newInstance();
	}
	public abstract void edit();
}
