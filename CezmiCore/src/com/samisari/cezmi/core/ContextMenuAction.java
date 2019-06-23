package com.samisari.cezmi.core;

public abstract class ContextMenuAction implements Runnable {
	private AbstractCommand command;

	
	public AbstractCommand getCommand() {
		return command;
	}

	public void setCommand(AbstractCommand command) {
		this.command = command;
	}

}
