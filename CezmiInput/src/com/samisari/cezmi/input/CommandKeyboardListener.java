package com.samisari.cezmi.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CommandKeyboardListener extends KeyAdapter {
	KeyInputListener listener;

	@Override
	public void keyPressed(KeyEvent e) {
		super.keyPressed(e);
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			listener.keyUpPressed();
			break;
		case KeyEvent.VK_DOWN:
			listener.keyDownPressed();
			break;
		case KeyEvent.VK_LEFT:
			listener.keyLeftPressed();
			break;
		case KeyEvent.VK_RIGHT:
			listener.keyRightPressed();
			break;
		default:
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		super.keyReleased(e);
	}
}
