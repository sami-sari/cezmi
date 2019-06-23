package com.samisari.graphics.commands;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.samisari.cezmi.component.CmdConnectableRectangle;
import com.samisari.cezmi.core.CommandManager;

public class CmdTableColumn extends CmdConnectableRectangle {
	private static final long	serialVersionUID	= -6965094548384137443L;
	private static final String	SPACES_100			= "                                                                                                    ";
	private String				name;
	private String				type;
	private int					size;
	private int					decimalDigits;
	private boolean				nullable;
	private boolean				unique;
	private int					index;
	private String				remark;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getDecimalDigits() {
		return decimalDigits;
	}

	public void setDecimalDigits(int decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	private CmdTable getTable() {
		return (CmdTable) CommandManager.getDeaultInstance().getCommand(getParentId());
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (getName().length()>24)
			sb.append(getName().substring(0,24));
		else
			sb.append(getName());
		sb.append(SPACES_100.substring(0, 25 - sb.length()));
		sb.append(getType());
		sb.append(SPACES_100.substring(0, 35 - sb.length()));
		sb.append(getSize());
		sb.append(SPACES_100.substring(0, 40 - sb.length()));
		sb.append(getDecimalDigits());
		sb.append(SPACES_100.substring(0, 45 - sb.length()));
		sb.append(isNullable() ? "" : "NOT NULL");
		sb.append(SPACES_100.substring(0, 55 - sb.length()));

		return sb.toString();
	}

	@Override
	public void paint(Graphics g) {
		logger.debug(getClass().getSimpleName() + " painting @(" + getX() + "," + getY() + ")");
		Color tempColor = g.getColor();
		Font tempFont = g.getFont();
		g.setColor(Color.BLACK);
		g.setFont(new Font("Courier", Font.PLAIN, 12));
		g.drawString(getName(), getX() + 5, getY() + 15);
		g.setFont(tempFont);
		g.setColor(tempColor);
		//super.paint(g);
	}
}
