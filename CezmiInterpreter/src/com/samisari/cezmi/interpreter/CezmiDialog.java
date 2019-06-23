package com.samisari.cezmi.interpreter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import com.samisari.cezmi.component.CmdMultiText;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.Status;
import com.samisari.graphics.commands.CmdButton;
import com.samisari.guidesigner.CmdWindow;
import com.sun.javafx.geom.Point2D;

public class CezmiDialog {
	private Point2D	topLeft;
	private String	source;
	CmdWindow		wnd;
	CmdButton		runButton;
	CmdMultiText	editor;
	CmdButton		sveButton;

	public CezmiDialog() {
		initWindow();
		initEditor();
		initRunButton();
		initSaveButton();
	}

	private void initWindow() {
		wnd = new CmdWindow();
		wnd.setBounds(new Rectangle(0, 0, 200, 300));
		wnd.setTitleText("Scripting Editor");
		wnd.setBorderColor(Color.BLACK);
		CommandManager.getDeaultInstance().getHistory().add(wnd);
	}

	private void initEditor() {
		editor = new CmdMultiText() {
			/**
			 * 
			 */
			private static final long	serialVersionUID	= 1L;
			private static final int	INITIAL				= 1;
			private static final int	EDITING				= 1;
			private int					state				= INITIAL;

			@Override
			public void mouseClicked(int x, int y) {
				if (state == INITIAL) {
					super.mouseClicked(x, y);
					state = EDITING;
				} else if (state == EDITING) {
					CommandManager.getDeaultInstance().getHistory().remove(editor);
					CommandManager.getDeaultInstance().setCommand(editor);
					editor.setCurrentStatus(Status.DRAGGING);
					editor.mouseClicked(editor.getRight(), editor.getBottom());
				}
			}
		};
		editor.setBounds(new Rectangle(10, 40, 180, 200));
		editor.setBorderColor("BLACK");
		CommandManager.getDeaultInstance().setCommand(editor);
		editor.setCurrentStatus(Status.DRAGGING);
		editor.mouseClicked(editor.getRight(), editor.getBottom());
		//editor.setCurrentStatus(Status.TYPING);
	}

	private void initSaveButton() {
		sveButton = new CmdButton() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void mouseClicked(int x, int y) {
				List<String> script = editor.getText();
				ScriptParser scriptParser = new ScriptParser();

				for (String line : script) {
					try {
						scriptParser.parse(line);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				CommandManager.getDeaultInstance().getHistory().remove(editor);
				CommandManager.getDeaultInstance().getHistory().remove(wnd);
				CommandManager.getDeaultInstance().getHistory().remove(sveButton);
			}
		};
		sveButton.setCaptureMouse(true);
		sveButton.setBounds(new Rectangle(25, 250, 50, 25));
		sveButton.setText("Kaydet");
		sveButton.setBorderColor(Color.BLACK);

		CommandManager.getDeaultInstance().getHistory().add(sveButton);
	}

	private void initRunButton() {
		runButton = new CmdButton() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void mouseClicked(int x, int y) {
				List<String> script = editor.getText();
				ScriptParser scriptParser = new ScriptParser();

				for (String line : script) {
					try {
						scriptParser.parse(line);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				CommandManager.getDeaultInstance().getHistory().remove(editor);
				CommandManager.getDeaultInstance().getHistory().remove(wnd);
				CommandManager.getDeaultInstance().getHistory().remove(runButton);
			}
		};
		runButton.setCaptureMouse(true);
		runButton.setBounds(new Rectangle(100, 250, 50, 25));
		runButton.setText("Tamam");
		runButton.setBorderColor(Color.BLACK);
		CommandManager.getDeaultInstance().getHistory().add(runButton);
	}

	public void paint(Graphics g) {
		wnd.paint(g);
		editor.paint(g);
		runButton.paint(g);
	}

	public void edit() {

	}

	public void delete() {

	}

	public void debug() {

	}

	public void run() {
		CommandManager.getDeaultInstance().getHistory().add(wnd);
		CommandManager.getDeaultInstance().getHistory().add(runButton);
	}

	public void load(String filename) {

	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
