package com.samisari.cezmi.animator.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.Timer;
import javax.swing.event.ListDataListener;

import com.samisari.cezmi.animator.core.Animation;

public class AnimationScriptEditor extends JDialog {
	private static final long	serialVersionUID	= -2792478444723251243L;
	private JList<String>		listBox				= new JList<>();
	private JTextArea			text				= new JTextArea();
	private JTextField			txtName				= new JTextField();

	private JButton				saveBtn				= new JButton("Save");
	private JButton				runBtn				= new JButton("Run");

	private ScriptEngineManager	sem					= new ScriptEngineManager();
	private ScriptEngine		engine				= sem.getEngineByName("nashorn");

	private transient Animation	animation;
	private PlayerPanel			console;

	public AnimationScriptEditor(final Player player, Frame parent, String title) {
		super(parent, title);
		this.animation = player.getAnimation();
		this.console = player.getConsole();

		saveBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}

		});
		runBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				run();
			}

		});

		this.setLayout(new BorderLayout());
		this.setModal(true);
		setupListBox();

		this.add(new JScrollPane(listBox), BorderLayout.WEST);
		this.add(new JScrollPane(text), BorderLayout.CENTER);

		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BorderLayout());
		northPanel.add(new JLabel("Name"), BorderLayout.WEST);
		northPanel.add(txtName, BorderLayout.CENTER);

		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		southPanel.add(runBtn, BorderLayout.SOUTH);
		southPanel.add(saveBtn, BorderLayout.SOUTH);

		this.add(northPanel, BorderLayout.NORTH);
		this.add(southPanel, BorderLayout.SOUTH);
		this.pack();
	}

	private void setupListBox() {
		listBox.setModel(new ListModel<String>() {
			List<ListDataListener> listeners = new ArrayList<>();

			@Override
			public int getSize() {
				if (animation.getFrameScripts() == null || animation.getFrameScripts().get(animation.getCurrentFrameNumber()) == null
						|| animation.getFrameScripts().get(animation.getCurrentFrameNumber()).isEmpty())
					return 1;
				return animation.getFrameScripts().get(animation.getCurrentFrameNumber()).size();
			}

			@Override
			public String getElementAt(int index) {
				if (animation.getFrameScripts() == null || animation.getFrameScripts().get(animation.getCurrentFrameNumber()) == null
						|| animation.getFrameScripts().get(animation.getCurrentFrameNumber()).size() <= index) {
					if (index == 0)
						return "Boş";
					else
						return null;
				}
				return animation.getFrameScripts().get(animation.getCurrentFrameNumber()).get(index);
			}

			@Override
			public void addListDataListener(ListDataListener l) {
				listeners.remove(l);
			}

			@Override
			public void removeListDataListener(ListDataListener l) {
				listeners.add(l);
			}
		});
	}

	private void save() {
		if (animation.getFrameScripts() == null) {
			animation.setFrameScripts(new HashMap<Integer, List<String>>());
		}
		if (animation.getFrameScripts().get(animation.getCurrentFrameNumber()) == null) {
			animation.getFrameScripts().put(animation.getCurrentFrameNumber(), new ArrayList<String>());
		}
		List<String> scripts = animation.getFrameScripts().get(animation.getCurrentFrameNumber());
		scripts.add(text.getText());
	}

	private void onTimer(final String script, final int[] i) {
		try {
			System.out.println(i[0]);
			engine.put("i", i[0]++);
			engine.put("view", animation.getCurrentView());
			if (animation.getCurrentView().getSelectedCommands() != null && animation.getCurrentView().getSelectedCommands().size() > 0)
				engine.put("object", animation.getCurrentView().getSelectedCommands().get(0));
			engine.eval(script);
			console.repaint();
		} catch (ScriptException e1) {
			e1.printStackTrace();
		}
	}

	private void run() {
		final String script = text.getText();
		final int end = 200;
		final int[] i = { 0 };
		engine.put("animation", animation);
		engine.put("end", end);
		final Timer timer = new Timer((int) ((1.0 / (double) animation.getFramesPerSecond()) * 1000.0), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onTimer(script, i);
			}

		});
		engine.put("timer", timer);
		timer.start();
	}
}
