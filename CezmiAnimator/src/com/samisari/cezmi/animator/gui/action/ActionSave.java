package com.samisari.cezmi.animator.gui.action;

import java.io.IOException;

import javax.swing.JFileChooser;

import com.samisari.cezmi.animator.gui.Player;

public class ActionSave implements Runnable{
	private Player player;
	
	public ActionSave(Player player) {
		this.player=player;
	}
	@Override
	public void run() {
		try {
			JFileChooser fc = new JFileChooser();
			if (fc.showSaveDialog(player.getConsole()) == JFileChooser.APPROVE_OPTION) {
				player.getAnimation().save(fc.getSelectedFile().getAbsolutePath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
