package com.samisari.graphics.commands;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.guidesigner.CmdWindow;

public class CmdTable extends CmdWindow implements Serializable {
	private static final Logger		logger				= Logger.getLogger(CmdTable.class);
	private static final long		serialVersionUID	= 1L;

	private List<CmdTableColumn>	columns				= new ArrayList<CmdTableColumn>();
	private String					cmdDatabaseId;

	private enum Status {
		START, DRAGGING, WIZARD
	}

	private Status myStatus;

	public CmdTable() {
		super();
		setTitleText("Table*");
		setBorderColor(Color.BLACK);
	}

	@Override
	public void run() {
		myStatus = Status.START;

	}

	// public List<CmdTableColumn> getColumns() {
	// return columns;
	// }
	//
	// public void setColumns(History<CmdTableColumn> columns) {
	// this.columns = columns;
	// getComponents().clear();
	// addComponent(command);
	// for (CmdTableColumn column : columns) {
	// column.setX(0);
	// column.setY(0);
	// column.setWidth(getWidth());
	// addComponent(column);
	// }
	// }

	public String getCmdDatabaseId() {
		return cmdDatabaseId;
	}

	public void setCmdDatabaseId(String cmdDatabaseId) {
		this.cmdDatabaseId = cmdDatabaseId;
	}

	@Override
	public void paint(Graphics g) {
		if (myStatus == Status.DRAGGING) {
			super.paint(g);
		} else {
			logger.debug(getClass().getSimpleName() + "painting @(" + getX() + "," + getY() + ")");
			Color tempColor = g.getColor();
			Font tempFont = g.getFont();
			g.setColor(Color.BLACK);
			int row = 0;
			for (CmdTableColumn col : getColumns()) {
				try {
					super.beforePaint(g, ConsolePropertyManager.getDefaultInstance().getScaleFactor(), ConsolePropertyManager.getDefaultInstance().getOffset());
					AffineTransform translate = AffineTransform.getTranslateInstance(getX(), getY());
					AffineTransform inverse = translate.createInverse();
					AffineTransform currentTransform = ((Graphics2D) g).getTransform();
					currentTransform.concatenate(translate);
					((Graphics2D) g).setTransform(currentTransform);
					col.setX(0);
					col.setY(row++ * 20 + 30);
					col.paint(g);
					currentTransform.concatenate(inverse);
					((Graphics2D) g).setTransform(currentTransform);
					super.afterPaint(g);

				} catch (Exception e) {

				}
			}
			super.paint(g);
			g.setFont(tempFont);
			g.setColor(tempColor);
		}
	}

	@Override
	public void mouseMoved(int x, int y) {
		if (myStatus.equals(Status.DRAGGING)) {
			super.mouseMoved(x, y);
		}
	}

	public static String getCommandName() {
		return "DB Table";
	}

	public void showWizard() {
		new CmdTableWizard(ConsolePropertyManager.getDefaultInstance().getApplicationFrame(), this);
	}

	@Override
	public void mouseClicked(int x, int y) {
		if (myStatus == Status.START) {
			logger.debug("START MouseClicked");
			super.mouseClicked(x, y);
			myStatus = Status.DRAGGING;
		} else if (myStatus == Status.DRAGGING) {
			logger.debug("DRAGGING MouseClicked");
			super.mouseClicked(x, y);
			super.setSelected(true);
			myStatus = Status.WIZARD;
			CommandManager.getDeaultInstance().setCommand(this);
		}
		if (myStatus == Status.WIZARD) {
			showWizard();
			myStatus = Status.START;
			CommandManager.getDeaultInstance().endCommand();
		}

	}

	@Override
	public void openContextMenu() {
		super.openContextMenu();
		showWizard();
	}

	@Override
	public void deserialise(Reader in) throws Exception {
		super.deserialise(in);
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public void setName(String name) {
		setTitleText(name);
	}

	public void setColumns(List<CmdTableColumn> columns) {
		this.columns = columns;
	}

	public List<CmdTableColumn> getColumns() {
		return columns;
	}
}
