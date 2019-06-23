package com.samisari.cezmi.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public interface KeyInputListener extends KeyListener {
	public void keyUpPressed();

	public void keyDownPressed();

	public void keyLeftPressed();

	public void keyRightPressed();

	default public void keyFirePressed() {
	}

	default public void keyJumpPressed() {
	}

	default public void keyKneelPressed() {
	}

	default public void keyUpReleased() {
	}

	default public void keyDownReleased() {
	}

	default public void keyLeftReleased() {
	}

	default public void keyRightReleased() {
	}

	default public void keyFireReleased() {
	}

	default public void keyJumpReleased() {
	}

	default public void keyKneelReleased() {
	}

	@Override
	public default void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			keyUpPressed();
			break;
		case KeyEvent.VK_DOWN:
			keyDownPressed();
			break;
		case KeyEvent.VK_LEFT:
			keyLeftPressed();
			break;
		case KeyEvent.VK_RIGHT:
			keyRightPressed();
			break;
		case KeyEvent.VK_ENTER:
			keyCommitPressed();
			break;
		default:

		}
		return;
	}

	public void keyCommitPressed();

	@Override
	public default void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			keyUpReleased();
			break;
		case KeyEvent.VK_DOWN:
			keyDownReleased();
			break;
		case KeyEvent.VK_LEFT:
			keyLeftReleased();
			break;
		case KeyEvent.VK_RIGHT:
			keyRightReleased();
			break;
		default:
		}
		return;

	}

}
