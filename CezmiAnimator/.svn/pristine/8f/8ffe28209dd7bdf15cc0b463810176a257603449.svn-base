package com.samisari.cezmi.animator.animationcommands;

import java.awt.geom.AffineTransform;
import java.math.BigDecimal;

import com.samisari.cezmi.core.AbstractCommand;

public class CezmiScaling extends CezmiAbstractTransformation {
	// Name of the command
	public static final String	COMMAND_STRING	= "S";
	// Rotation center point
	private double				sx, sy;

	public CezmiScaling(AbstractCommand object, double sx, double sy, int startFrame, int endFrame) {
		setObject(object);
		setSx(sx);
		setSy(sy);
		setStartFrame(startFrame);
		setEndFrame(endFrame);
	}

	@Override
	public void transform() {
		AffineTransform c = getObject().getTransform();
		AffineTransform n = AffineTransform.getScaleInstance(sx, sy);
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
				sx = new BigDecimal(arg1).doubleValue();
				pos++;
				while ((ch = line.charAt(pos)) != ')') {
					arg2 += ch;
					pos++;
				}
				sy = new BigDecimal(arg2).doubleValue();

			} else {
				throw new Exception("Missing parameters");
			}
		}
		return false;
	}

	public double getSx() {
		return sx;
	}

	public void setSx(double sx) {
		this.sx = sx;
	}

	public double getSy() {
		return sy;
	}

	public void setSy(double sy) {
		this.sy = sy;
	}

}