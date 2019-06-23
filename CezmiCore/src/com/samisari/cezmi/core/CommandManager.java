package com.samisari.cezmi.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * This is a singleton that manages commands entered by the user
 * 
 * @author sami
 * 
 */
public class CommandManager {
	/**
	 * Log4J Logger
	 */
	private static Logger							logger	= Logger.getLogger(CommandManager.class);

	/**
	 * Singleton private static instance
	 */
	private static HashMap<String, CommandManager>	instance;
	/**
	 * Command history
	 */
	private History									history;
	/**
	 * Current command
	 */
	private AbstractCommand							command;

	/**
	 * Singleton private constructor
	 */
	private CommandManager() {
		super();
		initialize();
	}

	/**
	 * Singleton public getter returns single instance of the class
	 * 
	 * @return
	 */
	public static CommandManager getInstance(String commandSet) {
		if (instance == null) {
			logger.debug("Create command manager instance");
			instance = new HashMap<>();
		}
		if (instance.get(commandSet) == null)
			instance.put(commandSet, new CommandManager());

		return instance.get(commandSet);
	}

	/**
	 * Singleton public getter returns single instance of the class
	 * 
	 * @return
	 */
	public static CommandManager getDeaultInstance() {
		return getInstance("DEFAULT");
	}

	private void initialize() {
		history = new History();
	}

	/**
	 * Set current active command
	 * 
	 * @param command
	 */
	public void setCommand(AbstractCommand command) {
		this.command = command;
	}

	/**
	 * Get current active command
	 * 
	 * @return active command
	 */
	public AbstractCommand getCommand() {
		return command;
	}

	/**
	 * Set current command to null
	 */
	public void endCommand() {
		setCommand(null);
	}

	/**
	 * Get command history
	 * 
	 * @return command history
	 */
	public History getHistory() {
		return history;
	}

	/**
	 * Calls the isInRange(...) method of each command in history and returns a
	 * list containing those commands that returned true
	 * 
	 * @param x
	 *            location in x coordinate
	 * @param y
	 *            location in y coordinate
	 * @param radius
	 *            of the area that will be checked
	 * @return list of commands in history that are appropriate
	 */
	public AbstractCommand getSmallestAreaCommandInRange(int x, int y, int radius) {
		int smallestArea = Integer.MAX_VALUE;
		AbstractCommand retval = null;
		Iterator<AbstractCommand> iter = history.getSelectedCommands().iterator();
		while (iter.hasNext()) {
			AbstractCommand cmd = iter.next();
			if (cmd.isInRange(x, y, radius) && cmd.getArea() < smallestArea) {
				smallestArea = cmd.getArea();
				retval = cmd;
			}
		}
		if (retval == null) {
			iter = history.iterator();
			while (iter.hasNext()) {
				AbstractCommand cmd = iter.next();
				if (cmd.isInRange(x, y, radius) && cmd.getArea() < smallestArea) {
					smallestArea = cmd.getArea();
					retval = cmd;
				}

			}
		}
		return retval;
	}

	public List<AbstractCommand> getSelectedCommands() {
		ArrayList<AbstractCommand> list = new ArrayList<>();
		for (AbstractCommand cmd : getHistory()) {
			if (cmd.isSelected()) {
				list.add(cmd);
			}
		}
		return list;
	}

	public void setHistory(History history) {
		this.history.clear();
		this.history.addAll(history);
	}

	private List<CommandSelectionChangeListener> commandSelectionChangeListeners = new ArrayList<>();

	public void addSelectionChangeListener(CommandSelectionChangeListener l) {
		commandSelectionChangeListeners.add(l);
	}

	public void removeSelectionChangeListener(CommandSelectionChangeListener l) {
		commandSelectionChangeListeners.remove(l);
	}

	public void fireCommandSelectionChanged() {
		for (CommandSelectionChangeListener l : commandSelectionChangeListeners) {
			if (l != null)
				l.handleCommandSelectionChange(null);
		}
	}

	public void fireCommandSelectionChanged(AbstractCommand cmd) {
		for (CommandSelectionChangeListener l : commandSelectionChangeListeners) {
			if (l != null)
				l.handleCommandSelectionChange(cmd);
		}
	}

	public AbstractCommand getCommand(String id) {
		if (id == null)
			return null;
		return getHistory().getCommand(id);
	}
}
