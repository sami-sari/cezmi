package com.samisari.cezmi.animator.gui.action;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.samisari.cezmi.animator.gui.Player;

public class ActionBuilder {
	private Player player;

	public ActionBuilder(Player player) {
		this.player = player;
	}

	public Runnable getAction(String name) {
		try {
			Class<?> actionClass = Class.forName(name);
			@SuppressWarnings("unchecked")
			Constructor<? extends Runnable> constructor = (Constructor<? extends Runnable>) actionClass.getConstructor(Player.class);
			return constructor.newInstance(player);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}
