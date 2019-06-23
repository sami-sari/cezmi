package com.samisari.cezmi.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.Status;
import com.samisari.gui.dialog.JFontChooser;

public class CmdMultiText extends CmdConnectableRectangle implements Cloneable, KeyListener {
	private static final Logger	logger				= Logger.getLogger(CmdMultiText.class);
	private static final long	serialVersionUID	= 5226672462820897805L;
	private List<String>		text				= new ArrayList<>();
	private int					cursorPos			= 0;
	private int					cursorLine			= 0;
	private Font				font;
	private Color				color;

	public List<String> getText() {
		return text;
	}

	public void setText(List<String> text) {
		this.text = text;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.CHAR_UNDEFINED) {
			logger.debug("UNDEFINED Character");
			return;
		}
		logger.debug("key typed " + e.getKeyChar());
		if (e.getKeyChar() == '\b' && cursorPos > 0) {
			logger.debug("BACKSPACE " + cursorPos);
			cursorPos--;
			text.set(cursorLine, text.get(cursorLine).substring(0, cursorPos) + text.get(cursorLine).substring(cursorPos + 1, text.get(cursorLine).length()));
			logger.debug("BACKSPACE 2 " + text);
		} else if (e.getKeyChar() == '\n') {
			logger.debug("ENTER " + cursorPos);
			text.add("");
			cursorLine = text.size() - 1;
			cursorPos = 0;
			logger.debug("ENTER 2 " + text);

		} else {
			logger.debug("INSERT " + e.getKeyChar());
			if (text.size() <= cursorLine) {
				text.add("");
				cursorLine = text.size() - 1;
			}
			text.set(cursorLine, text.get(cursorLine).substring(0, cursorPos) + e.getKeyChar() + text.get(cursorLine).substring(cursorPos));
			cursorPos++;
		}
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getExtendedKeyCode() == KeyEvent.VK_LEFT && cursorPos > 0) {
			logger.debug("Key Released LEFT");
			cursorPos--;
		} else if (e.getExtendedKeyCode() == KeyEvent.VK_RIGHT && cursorPos < text.get(cursorLine).length()) {
			logger.debug("Key Released RIGHT");
			cursorPos++;
		} else if (e.getExtendedKeyCode() == KeyEvent.VK_DELETE) {
			logger.debug("DELETE");
			text.set(cursorLine, text.get(cursorLine).substring(0, text.get(cursorLine).length() - 1));
		} else if (e.getExtendedKeyCode() == KeyEvent.VK_UP) {
			logger.debug("UP " + cursorPos);
			if (cursorLine > 0) {
				cursorLine--;
				if (cursorPos >= text.get(cursorLine).length())
					cursorPos = text.get(cursorLine).length();
			}
		} else if (e.getExtendedKeyCode() == KeyEvent.VK_DOWN) {
			logger.debug("DOWN " + cursorPos);
			if (cursorLine + 1 < text.size()) {
				cursorLine++;
				if (cursorPos >= text.get(cursorLine).length())
					cursorPos = text.get(cursorLine).length();
			}
		} else {
			// Ignore
			return;
		}
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
	}

	private String getAllLines() {
		StringBuffer result = new StringBuffer();
		for (String line : text) {
			result.append(line + "\n");
		}
		return result.toString();
	}

	public void showDialog() {
		final JDialog dialog = new JDialog();
		final JTextArea textArea = new JTextArea();
		textArea.setText(getAllLines());
		final JFontChooser fontChooser = new JFontChooser();
		JButton fontButton = new JButton("");
		fontButton.setBounds(10, 10, 25, 25);
		fontButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				fontChooser.showDialog(dialog);
				Font font = fontChooser.getSelectedFont();
				if (font != null)
					CmdMultiText.this.font = font;
			}

		});
		dialog.add(fontButton);
		dialog.setLayout(null);
		textArea.setBounds(10, 40, 200, 100);
		dialog.add(textArea);
		dialog.setModal(true);
		dialog.setSize(220, 200);
		dialog.add(textArea);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent arg0) {
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				// text = textArea.getText();
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
			}
		});
		dialog.setVisible(true);
	}

	@Override
	public void mouseClicked(int x, int y) {
		if (getCurrentStatus() == Status.CONTEXT_MENU) {
			final JFontChooser fontChooser = new JFontChooser();
			fontChooser.setFont(getFont());
			fontChooser.setText(getText().toArray(new String[0]));
			int result = fontChooser.showDialog(ConsolePropertyManager.getDefaultInstance().getConsolePanel());
			if (result == JFontChooser.OK_OPTION) {
				Font font = fontChooser.getSelectedFont();
				if (font != null)
					CmdMultiText.this.font = font;
				CmdMultiText.this.setText(Arrays.asList(fontChooser.getText()));
			}
			setCurrentStatus(Status.START);
			return;
		}
		switch (getCurrentStatus()) {
		case START: {
			super.mouseClicked(x, y);
			/*
			 * setBounds(x, y, 0, 0);
			 */
			setCurrentStatus(Status.DRAGGING);
		}
			break;
		case DRAGGING:
			setWidth(x - getX());
			setHeight(y - getY());
			setColor(ConsolePropertyManager.getDefaultInstance().getForegroundColor());
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().setFocusable(true);
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().addKeyListener(this);
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().grabFocus();
			setCurrentStatus(Status.TYPING);
			break;
		case TYPING:
			setBorderColor((Color) null);
			Graphics graphics = ConsolePropertyManager.getDefaultInstance().getConsolePanel().getGraphics();
			if (font == null) {
				font = graphics.getFont();
			}
			CommandManager.getDeaultInstance().getHistory().add(this);
			CommandManager.getDeaultInstance().endCommand();
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().removeKeyListener(this);
			// get metrics from the graphics
			// FontMetrics metrics = graphics.getFontMetrics(font);
			// get the height of a line of text in this
			// font and render context
			// int hgt = metrics.getHeight();
			// get the advance of my text in this font
			// and render context
			// int adv = metrics.stringWidth(text);
			// calculate the size of a box to hold the
			// text with some padding.
			// Dimension size = new Dimension(adv + 2, hgt + 2);
			// setBounds(getX(), getY() - hgt, adv + 2, hgt + 2);
			setCurrentStatus(Status.START);
			break;
		default:
			break;
		}

	}

	@Override
	public void mouseMoved(int x, int y) {
		if (getCurrentStatus() == Status.START || getCurrentStatus() == Status.DRAGGING)
			super.mouseMoved(x, y);
	}

	@Override
	public void paint(Graphics g) {
		Color tmp = g.getColor();
		if (getCurrentStatus() == Status.DRAGGING) {
			super.paint(g);
		} else {
			super.paint(g);
			int x = getX();
			int y = getY();
			g.setColor(getColor() == null ? ConsolePropertyManager.getDefaultInstance().getForegroundColor() : getColor());
			g.setFont(font);
			logger.debug("Painting " + text + " (" + getX() + "," + getY());
			int rowIndex = 0;
			if (text != null)
				for (int i = 0; i < text.size(); i++) {
					String line = text.get(i);
					int lh = (int) g.getFontMetrics().getStringBounds(line, g).getHeight();
					List<String> lines = splitOverflows(line, g);
					for (String row : lines) {
						g.drawString(row, x, y + lh + rowIndex++ * (lh + 3));
					}
					if (getCurrentStatus() == Status.TYPING && cursorLine == i) {
						Rectangle2D cursorBounds = g.getFontMetrics().getStringBounds(line.substring(0, cursorPos), g);
						int cursorX = x + ((int) cursorBounds.getX()) + ((int) cursorBounds.getWidth());
						int cursorY1 = y + i * (lh + 3);
						int cursorY2 = y + lh + i * (lh + 3);
						g.drawLine(cursorX, cursorY1, cursorX, cursorY2);
					}
				}
		}
		g.setColor(tmp);
	}

	private List<String> splitOverflows(final String line, Graphics g) {
		List<String> result = new ArrayList<>();
		String ln = line;
		int lineWidth = getStringWidth(g, ln);
		if (getWidth() > 20) {
			while (lineWidth > getWidth()) {
				int subWidth = 0;
				int endIndex = 0;
				while (subWidth < getWidth() && endIndex < ln.length()) {
					subWidth = getStringWidth(g, ln.substring(0, endIndex++));
				}
				endIndex--;
				while (endIndex > 2 && ln.charAt(endIndex) != ' ') {
					endIndex--;
				}
				if (endIndex == 2) {
					endIndex = ln.length() - 1;
				}
				result.add(ln.substring(0, endIndex));
				ln = ln.substring(endIndex);
				lineWidth = getStringWidth(g, ln);
			}
		}

		result.add(ln);
		return result;
	}

	int getStringWidth(Graphics g, String txt) {
		return (int) g.getFontMetrics().getStringBounds(txt, g).getWidth();
	}
	// @Override
	// public void boundaryChanged(Rectangle oldBounds, Rectangle newBounds) {
	// int dx = newBounds.x - (oldBounds == null ? 0 : oldBounds.x);
	// int dy = newBounds.y - (oldBounds == null ? 0 : oldBounds.y);
	// setX(getX() + dx);
	// setY(getY() + dy);
	// }

	@Override
	public void buildBounds() {
		// Graphics g =
		// ConsolePropertyManager.getInstance().getConsolePanel().getGraphics();
		// if (font == null) {
		// font = g.getFont();
		// }
		// g.setFont(font);
		// Rectangle2D textBounds = g.getFontMetrics().getStringBounds(text, g);
		// setBounds(((int) textBounds.getX()) + getBounds().x, ((int)
		// textBounds.getY()) + getBounds().y, (int) textBounds.getWidth(),
		// (int) textBounds.getHeight());
	}

	@Override
	public void openContextMenu() {
		super.openContextMenu();
		String[] txt = getText().toArray(new String[0]);
		Font fnt = getFont();
		if (txt.length == 0) {
			txt = new String[] { "abc" };
		}
		if (fnt == null) {
			fnt = ConsolePropertyManager.getDefaultInstance().getConsolePanel().getFont();
		}
		final JFontChooser fontChooser = new JFontChooser();
		fontChooser.setText(txt);
		fontChooser.setSelectedFont(fnt);
		fontChooser.showDialog(ConsolePropertyManager.getDefaultInstance().getConsolePanel());

		Font font = fontChooser.getSelectedFont();
		if (font != null) {
			setFont(font);
			setText(Arrays.asList(fontChooser.getText()));
		}

		setCurrentStatus(Status.START);
	}

	@Override
	public void paintContextMenu(Graphics g) {
		// super.paintContextMenu(g);
		// Color tmpColor = g.getColor();
		// Font tmpFont = g.getFont();
		// g.setColor(Color.BLUE);
		// g.setFont(new Font("SansSerif", Font.ITALIC | Font.BOLD, 12));
		// g.drawRect(getX() + 5, getY() + 5, 100, 100);
		// g.drawString("Düzenle", getX(), getY());
		// g.setColor(tmpColor);
		// g.setFont(tmpFont);
	}
}
