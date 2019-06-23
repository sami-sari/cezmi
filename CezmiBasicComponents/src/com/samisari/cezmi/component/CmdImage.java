package com.samisari.cezmi.component;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.ContextMenuAction;
import com.samisari.cezmi.core.ContextMenuItem;
import com.samisari.cezmi.core.Status;
import com.samisari.cezmi.util.FileChooserHistory;
import com.samisari.cezmi.util.Util;

public class CmdImage extends CmdConnectableRectangle {
	private static final long	serialVersionUID	= 1L;
	private static final Logger	LOGGER				= Logger.getLogger(CmdImage.class);
	private BufferedImage		image;

	@Override
	public void run() {
		super.run();
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		String lastPath = FileChooserHistory.get(this.getClass().getName());
		if (lastPath != null)
			fileChooser.setCurrentDirectory(new File(lastPath));
		int result = fileChooser.showOpenDialog(null);
		try {
			if (result != JFileChooser.CANCEL_OPTION) {
				File fileName = fileChooser.getSelectedFile();

				FileChooserHistory.put(this.getClass().getName(), fileName.getParent());
				image = ImageIO.read(fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
		//		test();
	}

	@Override
	public void mouseClicked(int x, int y) {
		super.mouseClicked(x, y);
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		LOGGER.debug("width:" + width + "height" + height);
		super.mouseClicked(x + width, y + height);
		setSelected(true);
	}

	@Override
	public void paint(Graphics g) {

		if (Status.DRAGGING != getCurrentStatus()) {
			super.beforePaint(g, ConsolePropertyManager.getDefaultInstance().getScaleFactor(), ConsolePropertyManager.getDefaultInstance().getOffset());

		}

		if (image != null && getBounds() != null) {
			LOGGER.debug("Painting");
			g.drawImage(image, getX(), getY(), getWidth(), getHeight(), null);
		} else {
			LOGGER.debug("Not Painting");
		}

		if (Status.DRAGGING != getCurrentStatus()) {
			super.afterPaint(g);
		}
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	@Override
	public void openContextMenu() {
		super.openContextMenu();

	}

	@Override
	public List<ContextMenuItem> getContextMenuItems() {
		List<ContextMenuItem> menus = new ArrayList<>();
		ContextMenuItem item = new ContextMenuItem();
		item.setText("Zoom");
		item.setAction(new ContextMenuAction() {

			@Override
			public void run() {
				CmdImage _self = CmdImage.this;
				ConsolePropertyManager cons = ConsolePropertyManager.getDefaultInstance();
				double csf = cons.getScaleFactor();
				System.out.println(csf);
				double sf = (double) cons.getConsolePanel().getHeight() / (double) _self.getHeight();
				Point p = new Point((int)((double)_self.getX() * sf), (int)((double)_self.getY() * sf));
				cons.setOffset(p);
				cons.setScaleFactor(sf);
				ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
			}

		});
		menus.add(item);
		return menus;
	}

	public void test() {
		BufferedImage img = getImage();
		int height = img.getHeight();
		int width = img.getWidth();

		CmdFreehand cmd = new CmdFreehand();
		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				int c[] = new int[10];
				img.getData().getPixel(w, h, c);
				if (c[0] == 255 && c[1] == 255 && c[2] == 255) {
					cmd.getOriginalVertices().add(new Point(w, h));
				}
			}
			System.out.println(h);
		}
		CommandManager.getDeaultInstance().getHistory().add(cmd);
	}

}
