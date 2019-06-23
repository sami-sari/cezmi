package com.samisari.cezmi.interpreter;

public class Statement {
	private String				text;
	private ScriptingContext	context;

	public void parse() {
		
	}

	public Object run() {
		return null;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ScriptingContext getContext() {
		return context;
	}

	public void setContext(ScriptingContext context) {
		this.context = context;
	}

}
