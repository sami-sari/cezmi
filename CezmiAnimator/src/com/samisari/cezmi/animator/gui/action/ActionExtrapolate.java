package com.samisari.cezmi.animator.gui.action;

import java.util.List;

import com.samisari.cezmi.animator.gui.Player;
import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.History;

public class ActionExtrapolate implements Runnable {
	private Player player;

	public ActionExtrapolate(Player player) {
		this.player = player;
	}

	public void run() {
		int selectionStart = player.getAnimation().getSelectionStart();
		int current = player.getAnimation().getCurrentFrameNumber();
		History view = player.getAnimation().getCurrentView();
		History startView = player.getAnimation().getViews().get(selectionStart);
		List<AbstractCommand> selectedList = view.getSelectedCommands();
		for (AbstractCommand c : selectedList) {
			String id = c.getId();
			AbstractCommand startCommand = startView.getCommand(id);
			int x0 = startCommand.getX();
			int y0 = startCommand.getY();
			int width0 = startCommand.getWidth();
			int height0 = startCommand.getHeight();
			double dx = ((double) c.getX()) - (double) x0;
			double dy = ((double) c.getY()) - (double) y0;
			double dWidth = ((double) c.getWidth()) - (double) width0;
			double dHeight = ((double) c.getHeight()) - (double) height0;
			for (int i = selectionStart + 1; i < current; i++) {
				double factor = ((double) (i - selectionStart)) / (double) (current - selectionStart);
				AbstractCommand cmd = player.getAnimation().getViews().get(i).getCommand(id);
				cmd.setX(cmd.getX() + (int) (factor * dx));
				cmd.setY(cmd.getY() + (int) (factor * dy));
				cmd.setWidth(cmd.getWidth() + (int) (factor * dWidth));
				cmd.setHeight(cmd.getHeight() + (int) (factor * dHeight));

			}
			player.getConsole().repaint();
		}
	}
}
