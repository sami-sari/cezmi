package com.samisari.java2d.textwriter.commands.button;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STRawGroupDir;

import com.samisari.graphics.commands.ICmdButton;
import com.samisari.java2d.textwriter.JavaCodeWriter;
import com.samisari.java2d.textwriter.commands.Indent;

public class Button implements JavaCodeWriter{
	private static final Log logger = LogFactory.getLog(Button.class);
	private ICmdButton cmd;
	String name = "button";
	STRawGroupDir stgd = new STRawGroupDir(
			"com/samisari/java2d/textwriter/commands/button","UTF-8");

	public Button(ICmdButton cmd, String name) {
		setCmd(cmd);
		setName(name);
	}

	public ICmdButton getCmd() {
		return cmd;
	}

	public void setCmd(ICmdButton cmd) {
		this.cmd = cmd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJavaImportDeclaration(Indent indent) throws Exception {
		ST st = stgd.getInstanceOf("importDecl");
		return st.render();
	}

	public String getJavaFieldDeclaration(Indent indent) throws Exception {
		ST st = stgd.getInstanceOf("fieldDecl");
		st.add("name", name);
		st.add("indent", indent);
		return st.render();
	}

	public class IntRenderer implements AttributeRenderer {
		@Override
		public String toString(Object arg0, String arg1, Locale arg2) {
			return ((Integer) arg0).toString();
		}
	}

	public String getJavaBuildMethodStatements(Indent indent) throws Exception {
		stgd.registerRenderer(Integer.class, new IntRenderer());
		ST st = stgd.getInstanceOf("initialization");
		st.add("name", name);
		st.add("button", cmd);
		st.add("width", cmd.getBounds().width);
		st.add("height", cmd.getBounds().height);
		st.add("indent", indent);
		return st.render();
	}

	public String getJavaActionListenerStatements(Indent indent)
			throws Exception {
		if (cmd.getActionHandler() != null
				&& cmd.getActionHandler().length() > 0) {
			stgd.registerRenderer(Integer.class, new IntRenderer());
			ST st = stgd.getInstanceOf("actionListener");
			st.add("name", name);
			st.add("button", cmd);
			st.add("indent", indent);
			return st.render();
		}
		return "";
	}

	public static void main(String[] args) {
		ICmdButton cmd = new ICmdButton() {

			@Override
			public int getX() {
				return 0;
			}

			@Override
			public int getY() {
				return 0;
			}

			@Override
			public int getWidth() {
				return 0;
			}

			@Override
			public int getHeight() {
				return 0;
			}

			@Override
			public Color getBorderColor() {
				return null;
			}

			@Override
			public Color getFillColor() {
				return null;
			}

			@Override
			public Rectangle getBounds() {
				return new Rectangle(10, 10, 100, 100);
			}

			@Override
			public String getText() {
				return "Test Button";
			}

			@Override
			public String getActionHandler() {
				return "do();";
			}

		};

		Button buttonWriter = new Button(cmd, "btnTest");
		System.out.println(buttonWriter.getCode());
	}

	public String getCode() {
		StringBuilder sb = new StringBuilder();
		try {
			Indent indent = new Indent();
			sb.append(getJavaImportDeclaration(indent));
			indent.indent();
			sb.append(getJavaFieldDeclaration(indent));
			indent.indent();
			sb.append(getJavaBuildMethodStatements(indent));
			sb.append(getJavaActionListenerStatements(indent));

		} catch (Exception e) {
			logger.error(null, e);
		}
		return sb.toString();
	}
}
