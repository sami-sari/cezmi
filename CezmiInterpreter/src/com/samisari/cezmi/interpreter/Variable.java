package com.samisari.cezmi.interpreter;

public class Variable extends Statement{
	private String	name;
	private Object	value;

	public Variable() {
		super();
	}

	public Variable(String name) {
		this();
		setName(name);
	}

	public Variable(String name, Object value) {
		this();
		setName(name);
		setValue(value);
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
	}
}
