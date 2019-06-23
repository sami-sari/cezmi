package com.samisari.graphics.commands;

import java.text.NumberFormat;

import javax.swing.table.DefaultTableCellRenderer;

class BigDecimalRendererComponent extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 2303397574559958803L;

	public BigDecimalRendererComponent() {
		super();
		setHorizontalAlignment(RIGHT);
	}

	@Override
	protected void setValue(Object value) {
		if (value == null) {
			setText("");
		} else {
			setText(NumberFormat.getInstance().format(value));
		}
	}
}