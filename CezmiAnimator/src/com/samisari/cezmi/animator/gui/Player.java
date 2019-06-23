package com.samisari.cezmi.animator.gui;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.Timer;

import com.samisari.cezmi.animator.core.Animation;
import com.samisari.cezmi.animator.core.Tick;
import com.samisari.cezmi.animator.gui.action.ActionPaste;
import com.samisari.cezmi.animator.gui.action.ActionExtrapolate;
import com.samisari.cezmi.animator.gui.action.ActionLoad;
import com.samisari.cezmi.animator.gui.action.ActionSave;
import com.samisari.cezmi.animator.sound.CmdSound;
import com.samisari.cezmi.animator.sound.SoundClipBoundary;
import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.History;
import com.samisari.gui.dialog.PropertiesDialog;

public class Player extends JFrame implements TimeLineListener {
	private static final long		serialVersionUID	= 2920862071256946809L;
	private Animation				animation;
	private Timer					ticker;
	private PlayerPanel				console;
	private TimeLine				timeline;
	private volatile boolean		playing				= false;
	private volatile List<History>	copyBuffer;
	public static GraphicsDevice[]	devices;
	public static GraphicsDevice	defaultDevice;

	static {
		Rectangle bounds = new Rectangle();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		devices = ge.getScreenDevices();

		for (GraphicsDevice device : devices) {
			Rectangle dr = device.getDefaultConfiguration().getBounds();
			if (dr.getWidth() > bounds.getWidth()) {
				defaultDevice = device;
			}
		}
	}

	public Player() {
		super(defaultDevice.getDefaultConfiguration());
		animation = new Animation(500);
		setAnimation(animation);
		setLayout(new BorderLayout());
		setUndecorated(true);
		console = new PlayerPanel(this);
		animation.addListener(console);
		add(console, BorderLayout.CENTER);
		timeline = new TimeLine(500);
		timeline.addListener(this);
		add(timeline, BorderLayout.NORTH);
	}

	public void start() {
		setVisible(true);
		pack();
	}

	public void end() {
		setVisible(false);
	}

	public void play() {
		int start = getAnimation().getSelectionStart();
		int end = getAnimation().getSelectionEnd();
		int current = getAnimation().getCurrentFrameNumber();
		if (end > start && current >= end - 1)
			getTimeline().setFrame(start);

		setPlaying(true);
		int delay = 1000 / getAnimation().getFramesPerSecond();
		ticker = new Timer(delay, new Tick(this));
		ticker.setInitialDelay(100);
		ticker.setCoalesce(false);
		ticker.start();
		// setPlaying(false);
	}

	public void stop() {
	}

	public Animation getAnimation() {
		return animation;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	public Timer getTicker() {
		return ticker;
	}

	public void setTicker(Timer ticker) {
		this.ticker = ticker;
	}

	public PlayerPanel getConsole() {
		return console;
	}

	public void setConsole(PlayerPanel console) {
		this.console = console;
	}

	@Override
	public void onTimelineEvent(TimeLineEvent event) {
		switch (event.getEventType()) {
		case TimeLineEvent.ACTION_PLAY:
			play();
			break;
		case TimeLineEvent.ACTION_PAUSE:
			stop();
			break;
		case TimeLineEvent.FRAME_CHANGED:
			getAnimation().setCurrentFrameNumber(event.getArguments()[0]);
			break;
		case TimeLineEvent.SELECTION_CHANGED:
			getAnimation().setSelectionStart(event.getArguments()[0]);
			getAnimation().setSelectionEnd(event.getArguments()[1]);
			setPlaying(false);
			break;
		case TimeLineEvent.ACTION_COPY: {
			int start = getAnimation().getSelectionStart();
			int end = getAnimation().getSelectionEnd();
			copyBuffer = new ArrayList<>();
			for (int i = start; i <= end; i++) {
				copyBuffer.add((History) getAnimation().getViews().get(i).clone());
			}
		}
			break;
		case TimeLineEvent.ACTION_PASTE:
			new ActionPaste(this).run();
			break;
		case TimeLineEvent.ACTION_EXTRAPOLATE:
			new ActionExtrapolate(this).run();
			break;
		case TimeLineEvent.ACTION_SAVE:
			new ActionSave(this).run();
			break;
		case TimeLineEvent.ACTION_LOAD: 
			new ActionLoad(this).run();
			break;
		case TimeLineEvent.ACTION_DELETE:
			getAnimation().delete();
			break;
		case TimeLineEvent.ACTION_SHOW_PROPERTIES: {
			List<AbstractCommand> selectedList = getAnimation().getCurrentView().getSelectedCommands();
			for (AbstractCommand c : selectedList) {
				new PropertiesDialog(c).setVisible(true);
			}
		}
			break;
		case TimeLineEvent.ACTION_EDIT: {
			CommandManager.getDeaultInstance().setHistory(getAnimation().getCurrentView());
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().grabFocus();
			ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
		}
			break;
		case TimeLineEvent.ACTION_MAKEMOVIE: {
			try {
				new MovieMaker().start(ConsolePropertyManager.getDefaultInstance().getConsolePanel().getWidth(),
						ConsolePropertyManager.getDefaultInstance().getConsolePanel().getHeight(), animation);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			break;
		case TimeLineEvent.ACTION_ADD_SOUND: {
			JFileChooser fc = new JFileChooser();
			if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(this)) {
				File file = fc.getSelectedFile();
				CmdSound cmd = new CmdSound();
				cmd.setFilename(file.getAbsolutePath());
				cmd.setBounds(new Rectangle(0, 0, 40, 40));
				cmd.run();
				animation.getCurrentView().add(cmd);
				SoundClipBoundary scb = new SoundClipBoundary();
				scb.setSound(cmd);
				if (animation.getSelectionStart() == -1) {
					scb.setStartPlayingAt(0);
				} else {
					scb.setStartPlayingAt(animation.getSelectionStart());
				}
				if (animation.getSelectionEnd() == -1) {
					scb.setStopPlayingAt(animation.getNumberOfFrames());
				} else {
					scb.setStartPlayingAt(animation.getSelectionEnd());
				}
				timeline.addListener(scb);
			}
			getContentPane().repaint();
		}
			break;
		case TimeLineEvent.ACTION_SHOWSCRIPT:
			AnimationScriptEditor scriptDialog = new AnimationScriptEditor(this, this, "Betik Editörü");
			scriptDialog.setVisible(true);
			break;
		default:
			break;
		}
	}

	public TimeLine getTimeline() {
		return timeline;
	}

	public void setTimeline(TimeLine timeline) {
		this.timeline = timeline;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public List<History> getCopyBuffer() {
		return copyBuffer;
	}

	public void setCopyBuffer(List<History> copyBuffer) {
		this.copyBuffer = copyBuffer;
	}

}
