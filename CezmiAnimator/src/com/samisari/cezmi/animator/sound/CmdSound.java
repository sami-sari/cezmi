package com.samisari.cezmi.animator.sound;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.Operation;
import com.samisari.cezmi.core.Status;
import com.samisari.common.util.ImageTool;

public class CmdSound extends AbstractCommand {
	private static final long	serialVersionUID	= 1948656504902446016L;
	private int					timeMilisecond;
	private String				filename;
	private byte[]				cachedBuffer;
	SoundPlayer					player;
	SoundRecorder				recorder;
	JDialog						dialog;

	@Override
	public void run() {
		super.run();
		if (filename != null)
			this.player = new SoundPlayer(filename);
	}

	@Override
	public void mouseClicked(int x, int y) {
		// logger.debug("Mouse clicked: left=" + x + ", right=" + y);
		switch (getCurrentStatus()) {
		case START: {
			setX(x);
			setY(y);
			setWidth(0);
			setHeight(0);
			setCurrentStatus(Status.DRAGGING);
		}
			break;
		case DRAGGING: {
			setWidth(x - getX());
			setHeight(y - getY());
			CommandManager.getDeaultInstance().getHistory().add(this);
			CommandManager.getDeaultInstance().endCommand();
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
			setCurrentStatus(Status.START);
		}
		default:
			break;
		}

	}
	@Override
	public void mouseMoved(int x, int y) {
		if (getCurrentStatus()==Status.DRAGGING) {
			setWidth(x - getX());
			setHeight(y - getY());
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
		}
	}
	@Override
	public void paint(Graphics g) {
		super.beforePaint(g, ConsolePropertyManager.getDefaultInstance().getScaleFactor(), ConsolePropertyManager.getDefaultInstance().getOffset());
		g.setColor(Color.BLACK);
		if (getCurrentStatus() == Status.DRAGGING) {
			g.drawRect(getX(), getY(), getWidth(), getHeight());
		} else {
			g.setFont(new Font("Dialog", Font.PLAIN, 10));
			g.drawImage(ImageTool.getResourceAsImage("com/samisari/graphics/commands/resources/Sound.png"), getX(), getY(), getWidth(), getHeight(), null);
			if (getName() != null)
				g.drawString(getName(), getX(), getY() + getHeight() + 20);
		}
		super.afterPaint(g);
	}

	@Override
	public void move(Operation operation, int dx, int dy) {
		super.move(operation, dx, dy);
		if (dialog != null && dialog.isVisible()) {
			dialog.setLocation(dialog.getLocation().x + dx, dialog.getLocation().y + dy);
		}
	}

	public int getTimeMilisecond() {
		return timeMilisecond;
	}

	public void setTimeMilisecond(int timeMilisecond) {
		this.timeMilisecond = timeMilisecond;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public byte[] getCachedBuffer() {
		return cachedBuffer;
	}

	public void setCachedBuffer(byte[] cachedBuffer) {
		this.cachedBuffer = cachedBuffer;
	}

	@Override
	public void openContextMenu() {
		super.openContextMenu();
		dialog = new JDialog();
		Point componentPos = ConsolePropertyManager.getDefaultInstance().getConsolePanel().getLocation();
		Point framePos = ConsolePropertyManager.getDefaultInstance().getApplicationFrame().getLocationOnScreen();
		dialog.setLocation(framePos.x + componentPos.x + getX(), framePos.y + componentPos.y + getY());
		dialog.setUndecorated(true);
		dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton playBtn = new JButton(">");
		playBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (player != null)
					player.finish();

				if (getFilename() != null) {
					File file = new File(getFilename());
					if (file != null && (file.getParentFile().exists())) {
						player = new SoundPlayer(getFilename(), false);
						new Thread(new Runnable() {

							@Override
							public void run() {
								player.play();

							}
						}).start();
					}
				}
			}
		});
		JButton stopBtn = new JButton("||");
		stopBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (player != null)
					try {
						player.finish();
					} catch (Exception e2) {
					}
				if (recorder != null)
					try {
						recorder.finish();

					} catch (Exception e2) {
					}
				dialog.dispose();
			}
		});
		JButton recordBtn = new JButton("!");
		recordBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (recorder != null)
					recorder.finish();
				if (getFilename() != null) {
					File file = new File(getFilename());
					if (file.getParentFile().exists()) {
						new Thread(new Runnable() {

							@Override
							public void run() {
								try {
									recorder = new SoundRecorder(new File(getFilename()));
									recorder.record();
								} catch (Throwable e) {
									e.printStackTrace();
								}
							}
						}).start();
					}
				}
			}
		});
		dialog.add(playBtn);
		dialog.add(stopBtn);
		dialog.add(recordBtn);
		dialog.setModal(false);
		dialog.pack();
		dialog.setVisible(true);

	}
}
