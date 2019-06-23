package com.samisari.cezmi.animator.animationcommands;

import java.awt.geom.AffineTransform;
import java.math.BigDecimal;

import com.samisari.cezmi.core.AbstractCommand;

public class CezmiTranslation extends CezmiAbstractTransformation {
	// Name of the command
	public static final String	COMMAND_STRING	= "T";
	// Rotation center point
	private double				dx, dy;

	public CezmiTranslation(AbstractCommand object, double dx, double dy, int startFrame, int endFrame) {
		setObject(object);
		setDx(dx);
		setDy(dy);
		setStartFrame(startFrame);
		setEndFrame(endFrame);
	}

	@Override
	public void transform() {
		AffineTransform c = getObject().getTransform();
		AffineTransform n = AffineTransform.getTranslateInstance(dx, dy);
		c.concatenate(n);
		getObject().setTransform(c);
	}

	public boolean parse(String line) throws Exception {
		line = line.trim();
		String arg1 = "", arg2 = "";
		if (line.startsWith(COMMAND_STRING)) {
			int pos = COMMAND_STRING.length();
			while (line.charAt(pos) == ' ') {
				pos++;
			}

			if (line.charAt(pos) == '(') {
				char ch = '0';
				while ((ch = line.charAt(pos)) != ',') {
					arg1 += ch;
					pos++;
				}
				dx = new BigDecimal(arg1).doubleValue();
				pos++;
				while ((ch = line.charAt(pos)) != ')') {
					arg2 += ch;
					pos++;
				}
				dy = new BigDecimal(arg2).doubleValue();

			} else {
				throw new Exception("Missing parameters");
			}
		}
		return false;
	}

	public double getDx() {
		return dx;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}

	public double getDy() {
		return dy;
	}

	public void setDy(double dy) {
		this.dy = dy;
	}

}