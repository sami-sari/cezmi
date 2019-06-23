package com.samisari.cezmi.animator.gui;

import javax.swing.JDialog;

import com.samisari.cezmi.animator.core.Animation;
import com.samisari.gui.table.historytable.HistoryTable;

public class AnimationEditor extends JDialog {
	private static final long	serialVersionUID	= -6302471904083677681L;
	TimeLine					timeLine;
	Animation					animation;
	HistoryTable				assets				= new HistoryTable();

	public AnimationEditor(Animation animation) {
		this.setModal(true);
		this.getContentPane().add(assets);
	}
}
