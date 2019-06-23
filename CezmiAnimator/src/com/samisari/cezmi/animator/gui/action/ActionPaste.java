package com.samisari.cezmi.animator.gui.action;

import com.samisari.cezmi.animator.gui.Player;
import com.samisari.cezmi.core.History;

public class ActionPaste implements Runnable{
	private Player player;
	
	public ActionPaste(Player player) {
		this.player=player;
	}
	@Override
	public void run() 
	{
		if (player.getCopyBuffer() != null && player.getCopyBuffer().size() > 0)
			for (int i = player.getAnimation().getSelectionStart(); i <= player.getAnimation().getSelectionEnd();) {
				player.getAnimation().setCurrentFrameNumber(i);
				for (History h : player.getCopyBuffer()) {
					player.getAnimation().getViews().add(player.getAnimation().getCurrentFrameNumber(), (History) h.clone());
					i++;
				}
			}
	}
}
