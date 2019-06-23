package com.samisari.commands.uml;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.samisari.cezmi.core.CezmiScrollableRegion;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.Operation;
import com.samisari.cezmi.core.Status;
import com.samisari.common.util.bean.BeanUtils;
import com.samisari.guidesigner.CmdWindow;

@XmlRootElement
public class CmdClass extends CmdWindow {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1L;
	protected List<CmdClassField>	fields				= new ArrayList<>();
	protected List<CmdClassMethod>	methods				= new ArrayList<>();
	private String					sourceCode;

	@Override
	public void paint(Graphics g) {
		super.beforePaint(g, ConsolePropertyManager.getDefaultInstance().getScaleFactor(), ConsolePropertyManager.getDefaultInstance().getOffset());
		Color tempColor = g.getColor();
		Font tempFont = g.getFont();
		Graphics2D g2;
		
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		
		int maxWidth = 1;
		for (int i = 0; fields != null && i < fields.size(); i++) {
			int w = fields.get(i).getBounds().width;
			if (w > maxWidth)
				maxWidth = w;
		}
		for (int i = 0; methods != null && i < methods.size(); i++) {
			int w = methods.get(i).getWidth();
			if (w > maxWidth)
				maxWidth = w;
		}
		int h = 20 * ((fields == null ? 0 : fields.size()) + (methods == null ? 0 : methods.size()) + 1);

		BufferedImage img = new BufferedImage(Math.max(maxWidth, getBounds().width), Math.max(h, getBounds().height), BufferedImage.TYPE_INT_ARGB);
		g2 = (Graphics2D) img.getGraphics();
		g2.setBackground(Color.WHITE);
		g2.setColor(Color.BLACK);
		if (fields == null)
			fields = new ArrayList<CmdClassField>();
		for (int i = 0; fields != null && i < fields.size(); i++) {
			CmdClassField f = getFields().get(i);
			f.getBounds().x = 10;
			f.getBounds().y = 35 + (i * 20);
			f.paint(g2);
			//g2.drawString(fields.get(i).toString(), 10, 50 + (i * 20));
		}
		g2.drawLine(0, 50 + (fields.size() * 20), getBounds().width, 50 + (fields.size() * 20));
		for (int i = 0; methods != null && i < methods.size(); i++) {
			CmdClassMethod m = getMethods().get(i);
			m.getBounds().x = 10;
			m.getBounds().y = 50 + ((fields.size() + i) * 20);
			m.paint(g2);
			//g2.drawString(methods.get(i), 10, 70 + ((fields.size() + i) * 20));
		}

		new CezmiScrollableRegion(img, getBounds()).paint((Graphics2D) g);

		g.setFont(tempFont);
		g.setColor(tempColor);

		super.paint(g);
		super.afterPaint(g2);
	}

	public void setFields(String fields) {
		try {
			BeanUtils.string2Value(this, List.class, CmdClass.class.getMethod("setFields", List.class), fields);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setMethods(String methods) {
		try {
			BeanUtils.string2Value(this, List.class, CmdClass.class.getMethod("setMethods", List.class), methods);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getCommandName() {
		return "UML Class";
	}

	public List<CmdClassField> getFields() {
		return fields;
	}

	public void setFields(List<CmdClassField> fields) {
		this.fields = fields;
	}

	public List<CmdClassMethod> getMethods() {
		return methods;
	}

	public void setMethods(List<CmdClassMethod> methods) {
		this.methods = methods;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	@Override
	public String getTitleText() {
		return getName() == null ? "" : getName();
	}

	@Override
	public void openContextMenu() {
		super.openContextMenu();
		CmdClassWizard wizard = new CmdClassWizard(this);
		wizard.setVisible(true);
		wizard.pack();
		setCurrentStatus(Status.START);
	}

	@Override
	public Operation getOperation(Point click) {
		Operation op = super.getOperation(click);
		if (op == Operation.NONE || op == null) {
			for (CmdClassField f : fields) {
				if (f.isInRange(click.x - getX(), click.y - getY(), 1)) {
					CmdClassField field = (CmdClassField) f.duplicate();
					field.setSelected(true);
					CommandManager.getDeaultInstance().getHistory().add(field);
				}
			}
			for (CmdClassMethod m : methods) {
				if (m.isInRange(click.x - getX(), click.y - getY(), 1)) {
					CmdClassMethod method = (CmdClassMethod) m.duplicate();
					method.setSelected(true);
					CommandManager.getDeaultInstance().getHistory().add(method);
				}
			}
		}
		return op;
	}

}
