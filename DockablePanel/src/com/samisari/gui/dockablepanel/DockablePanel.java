package com.samisari.gui.dockablepanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class DockablePanel extends JPanel {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	JComponent					parentComponent;
	JFrame						dialog;
	JPanel						outerPanel;
	JPanel						titlePanel;

	public enum Position {
		LEFT, RIGHT, TOP, BOTTOM;
	}

	Position position;

	public DockablePanel(JComponent parent, Position position) {
		super();
		this.parentComponent = parent;
		this.position = position;

		outerPanel = new JPanel();
		outerPanel.setLayout(new BorderLayout());

		titlePanel = new JPanel();
		titlePanel.setLayout(new FlowLayout());
		titlePanel.add(new JLabel("Title"));
		titlePanel.setBackground(Color.RED);
		outerPanel.add(titlePanel, BorderLayout.NORTH);
		
		outerPanel.add(this, BorderLayout.CENTER);
		titlePanel.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				// Nothing to override
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				Point l = e.getLocationOnScreen();
				//Point cl = parentComponent.getLocationOnScreen();
				Point t = DockablePanel.this.getLocationOnScreen();
				Rectangle bounds = DockablePanel.this.getBounds();
				bounds.x = t.x;
				bounds.y = t.y;
				if (bounds.contains(l) && dialog != null) {
					dialog.dispose();
					dialog = null;
					parent.add(DockablePanel.this);
				} else if (dialog == null) {
					parent.remove(DockablePanel.this);
					dialog = new JFrame();
					dialog.add(DockablePanel.this);
					dialog.setVisible(true);
					dialog.pack();
				}

			}
		});
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		outerPanel.paint(g);
		titlePanel.paint(g);
	}
	
	public static void main(String[] args) {
		JSplitPane sp = new JSplitPane();
		DockablePanel dockPanel = new DockablePanel(sp, Position.RIGHT);
		dockPanel.setLayout(new BorderLayout());
		dockPanel.add(new JLabel("Dockable Panel sample"));
		JFrame frame = new JFrame("Test DockablePanel");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		sp.setRightComponent(dockPanel);
		sp.setLeftComponent(new JPanel());
		frame.add(sp, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.pack();

	}
}
