package com.samisari.java2d.textwriter.commands.textField;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STRawGroupDir;

import com.samisari.graphics.commands.ICmdJTextBox;
import com.samisari.java2d.textwriter.JavaCodeWriter;
import com.samisari.java2d.textwriter.commands.Indent;

public class TextField implements JavaCodeWriter {
	private static final Log logger = LogFactory
			.getLog(TextField.class);
	private ICmdJTextBox cmd;
	String name = "button";
	STRawGroupDir stgd = new STRawGroupDir(
			"com/samisari/java2d/textwriter/commands/textField","UTF-8");
	public TextField(ICmdJTextBox cmd, String name) {
		setCmd(cmd);
		setName(name);
	}

	public ICmdJTextBox getCmd() {
		return cmd;
	}

	public void setCmd(ICmdJTextBox cmd) {
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
		if (cmd.getChangeEventHandler() != null
				&& cmd.getChangeEventHandler().length() > 0) {
			stgd.registerRenderer(Integer.class, new IntRenderer());
			ST st = stgd.getInstanceOf("actionListener");
			st.add("name", name);
			st.add("button", cmd);
			st.add("indent", indent);
			return st.render();
		}
		return "";
	}

	String getGetterName(String name) {
		return "get" + name.substring(0, 1).toUpperCase(Locale.ENGLISH)
				+ name.substring(1);
	}

	public String getCommandProperty(String expression) throws Exception {
		String property = expression;
		Object object = cmd;
		while (object!=null && property!=null && property.length()>0) {
			if (property.indexOf('.')>-1) {
				String temp = property.substring(property.indexOf(".") + 1);
				property=property.substring(0, property.indexOf("."));
				object = object.getClass()
						.getMethod(getGetterName(property)).invoke(object, (Object[])null);
				property = temp;
			} else {
				object = object.getClass()
						.getMethod(getGetterName(property)).invoke(object, (Object[])null);
				property = null;
			}
		}
		if (object!=null)
		return object.toString();
		return "null";
	}

	public String eval(String template, Indent indent) throws Exception {
		String code = template.replaceAll("\\$\\{indent\\}", indent.toString());
		code = code.replaceAll("\\$\\{name\\}", name);

		int pos = 0;
		Pattern pattern = Pattern.compile("\\$\\{\\.([^}]*)\\}");
		Matcher matcher = pattern.matcher(template);
		while (matcher.find(pos)) {
			String propName = matcher.group(1);
			String replacement = getCommandProperty(propName);
			code = code.replace("${." + propName + "}", replacement);
			pos = matcher.start() + 1;
		}

		return code;
	}

	public static void main(String[] args) {
		ICmdJTextBox cmd = new ICmdJTextBox(){

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
			public String getName() {
				return "txtTest";
			}

			@Override
			public String getMask() {
				return null;
			}

			@Override
			public int getMaxLength() {
				return 10;
			}

			@Override
			public String getChangeEventHandler() {
				return "do();";
			}
			
		};
		TextField textFieldWriter = new TextField(cmd, "btnTest");
		try {
			Indent indent = new Indent();
			System.out.print(textFieldWriter.getJavaImportDeclaration(indent));
			indent.indent();
			System.out.print(textFieldWriter.getJavaFieldDeclaration(indent));
			indent.indent();
			System.out.print(textFieldWriter
					.getJavaBuildMethodStatements(indent));

		} catch (Exception e) {
			logger.error(null, e);
		}

	}
}

