package com.samisari.cezmi.animator.gui.action;

import java.io.IOException;

import javax.swing.JFileChooser;

import com.samisari.cezmi.animator.gui.Player;

public class ActionLoad implements Runnable{
	private Player player;
	
	public ActionLoad(Player player) {
		this.player=player;
	}
	@Override
	public void run() {
		try {
			JFileChooser fc = new JFileChooser();
			if (fc.showOpenDialog(player.getConsole()) == JFileChooser.APPROVE_OPTION) {
				player.getAnimation().load(fc.getSelectedFile().getAbsolutePath());
				player.getAnimation().loadDelta();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
