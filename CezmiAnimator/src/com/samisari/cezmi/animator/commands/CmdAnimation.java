package com.samisari.cezmi.animator.commands;

import java.util.List;

import com.samisari.cezmi.animator.gui.Player;
import com.samisari.cezmi.animator.sound.CmdSound;
import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;

public class CmdAnimation extends AbstractCommand {
	private static final long	serialVersionUID	= -7031983334556222057L;
	private Player				player;
	private List<CmdSound>		sounds;
	private List<CmdAnimation>	animations;
	private int					numberOfFrames;

	@Override
	public void run() {
		super.run();
		if (player == null) {
			player = new Player();
		}
		player.start();
		CommandManager.getDeaultInstance().getHistory().add(this);
	}

	@Override
	public void mouseClicked(int x, int y) {
		super.mouseClicked(x, y);
	}

	@Override
	public void openContextMenu() {
		super.openContextMenu();
		CmdAnimationWizard wizard = new CmdAnimationWizard(this);
		wizard.setVisible(true);
		wizard.pack();
	}
}
