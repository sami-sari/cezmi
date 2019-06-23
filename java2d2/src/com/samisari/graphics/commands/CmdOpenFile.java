package com.samisari.graphics.commands;

import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.swing.JFileChooser;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.History;
import com.samisari.cezmi.core.Operation;
import com.samisari.cezmi.util.FileChooserHistory;

public class CmdOpenFile extends AbstractCommand {
	private static final long	serialVersionUID	= -4566584904534584098L;
	public static final Logger	logger				= Logger.getLogger(CmdOpenFile.class);

	@Override
	public void redo() {

	}

	@Override
	public void undo() {

	}

	public void run() {
		// new JFileChooser JFileChooser.SAVE_DIALOG
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		String lastPath = FileChooserHistory.get(this.getClass().getName());
		if (lastPath != null)
			fileChooser.setCurrentDirectory(new File(lastPath));
		int result = fileChooser.showOpenDialog(null);
		InputStreamReader in = null;
		try {
			if (result != JFileChooser.CANCEL_OPTION) {
				File fileName = fileChooser.getSelectedFile();
				FileChooserHistory.put(this.getClass().getName(), fileName.getParent());

				in = new InputStreamReader(new FileInputStream(fileName), Charset.forName("UTF-8"));
				parse(in);
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
			}
			CommandManager.getDeaultInstance().endCommand();
		}
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
	}

	private void parse(InputStreamReader in) throws Exception {
		CommandManager.getDeaultInstance().getHistory().clear();
		History history = CommandManager.getDeaultInstance().getHistory();
		int c;
		StringBuilder text = new StringBuilder();
		try {
			while ((c = in.read()) >= 0) {
				text.append((char) c);
				logger.debug(text);
				if (text.charAt(text.length()-1)=='>') {
					String className = text.substring(text.indexOf("<") + 1, text.indexOf(">"));
					logger.debug("PARSING\n\tClass " + className);
					AbstractCommand command = null;
					try {
						command = (AbstractCommand) Class.forName(className).newInstance();
					} catch (Exception e) {
						if (!className.equals("root") && !className.equals("/root") )
							logger.error("Komut Sınıfı bilnmiyor!" + className);
						text.setLength(0);;
					}
					if (command != null) {
						try {
							command.deserialise(in);
						} catch (Exception e) {
							logger.error("gele", e);
						}
						history.add(command);
						text.setLength(0);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Dosya okunamadı!", e);
			throw e;
		}
	}

	@Override
	public void mouseClicked(int x, int y) {
	}

	@Override
	public void mouseMoved(int x, int y) {
	}

	@Override
	public void mousePressed(int x, int y) {
	}

	@Override
	public void mouseReleased(int x, int y) {
	}

	@Override
	public void paint(Graphics g) {
	}

	@Override
	public Point getOperationPoint(Operation operation) {
		return null;
	}

}
