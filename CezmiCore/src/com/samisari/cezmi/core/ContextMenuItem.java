package com.samisari.cezmi.core;

public class ContextMenuItem {
	private String text;
	private ContextMenuAction action;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ContextMenuAction getAction() {
		return action;
	}

	public void setAction(ContextMenuAction action) {
		this.action = action;
	}
}
