package com.samisari.cezmi.animator.core;

import java.awt.Rectangle;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class KeyFrame {
	private int			frameNumber;
	private Rectangle	bounds;
	private String		commandId;

	public KeyFrame() {
		super();
	}

	public KeyFrame(int fn, Rectangle bounds, String commandId) {
		this();
		setFrameNumber(fn);
		setBounds(bounds);
		setCommandId(commandId);
	}

	public void serialise(ByteArrayOutputStream baos) throws IOException {
		baos.write("<KeyFrame><fn>".getBytes());
		baos.write(Integer.toString(frameNumber).getBytes());
		baos.write("</fn><b>".getBytes());
		baos.write(Integer.toString(bounds.x).getBytes());
		baos.write(",".getBytes());
		baos.write(Integer.toString(bounds.y).getBytes());
		baos.write(",".getBytes());
		baos.write(Integer.toString(bounds.width).getBytes());
		baos.write(",".getBytes());
		baos.write(Integer.toString(bounds.height).getBytes());
		baos.write("</b><cId>".getBytes());
		baos.write(commandId.getBytes());
		baos.write("</cId></KeyFrame>".getBytes());
	}

	public void deserialise(ByteArrayInputStream bais) {
		int c;
		int state = 0;
		StringBuilder text = new StringBuilder();
		while ((c = bais.read()) != -1) {
			if (c == (int) '>') {
				if (text.toString().equals("KeyFrame")) {
					state = 1;
				} else if (text.toString().equals("fn")) {
					state = 2; // nextValue is frameNumber
				} else if (text.toString().equals("b")) {
					state = 4; // nextValue is bounds
				} else if (text.toString().equals("cId")) {
					state = 6; // nextValue is bounds
				} else if (text.toString().equals("/KeyFrame")) {
					state = 8;
				} else if (text.toString().equals("/fn")) {
					state = 3; // nextValue is frameNumber
				} else if (text.toString().equals("/b")) {
					state = 5; // nextValue is bounds
				} else if (text.toString().equals("/cId")) {
					state = 7; // nextValue is bounds
				}
				text.delete(0, text.length());
			} else if (c == (int) '<') {
				if (state == 2) {
					frameNumber = Integer.parseInt(text.toString());
				} else if (state == 4) {
					String[] args = text.toString().split(",");
					bounds = new Rectangle(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
				} else if (state == 6) {
					commandId = text.toString();
				}
				text.delete(0, text.length());
			} else {
				text.append((char) c);
			}
		}
	}

	public int getFrameNumber() {
		return frameNumber;
	}

	public void setFrameNumber(int frameNumber) {
		this.frameNumber = frameNumber;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}
}