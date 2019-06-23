package com.samisari.cezmi.core;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.samisari.common.util.bean.BeanUtils;

public class CommandProperty implements Serializable {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(CommandProperty.class);
	private String name;
	private Object value;
	private String declaringClassName;
	private String serializedForm;

	public CommandProperty(String name, Object value) {
		if (value == null)
			value = "";
		setName(name);
		setValue(value);
		setSerializedForm(BeanUtils.value2String(value));
	}
	public CommandProperty(String name, Object value, String declaringClassName) {
		this(name,value);
		setDeclaringClassName(declaringClassName);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
		setSerializedForm(BeanUtils.value2String(value));
	}

	public String toString() {
		try {
			return name + "=" + BeanUtils.value2String(value);
		} catch (Exception e) {
			logger.error("Property " + name + " can not be converted to String!", e); 
		}
		return "";
	}
	public String getDeclaringClassName() {
		return declaringClassName;
	}
	public void setDeclaringClassName(String declaringClassName) {
		this.declaringClassName = declaringClassName;
	}
	public String getSerializedForm() {
		return serializedForm;
	}
	public void setSerializedForm(String serializedForm) {
		this.serializedForm = serializedForm;
	}
}
