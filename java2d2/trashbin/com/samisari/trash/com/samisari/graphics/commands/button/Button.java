package com.samisari.trash.com.samisari.graphics.commands.button;

import java.awt.Color;
import java.awt.Rectangle;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.samisari.graphics.commands.CmdButton;
import com.samisari.graphics.commands.Indent;

public class Button {
	private static Log LOGGER=LogFactory.getLog(Button.class);
	private CmdButton cmd;
	String name = "button";

	public Button(CmdButton cmd, String name) {
		setCmd(cmd);
		setName(name);
	}

	public CmdButton getCmd() {
		return cmd;
	}

	public void setCmd(CmdButton cmd) {
		this.cmd = cmd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJavaImportDeclaration(Indent indent) throws Exception {
		return eval("import javax.swing.JButton;\n", indent);
	}

	public String getJavaFieldDeclaration(Indent indent) throws Exception {
		return eval("${indent}private JButton ${name};\n", indent);
	}

	public String getJavaBuildMethodStatements(Indent indent) throws Exception {
		String code = "${indent}${name} = new JButton();\n"
				+ "${indent}${name}.setPreferredSize(new Dimension(${.bounds.width}, ${.bounds.height}));\n"
				+ "${indent}${name}.setText(\"${.text}\");\n" + "";
		return eval(code, indent);
	}

	public String getJavaActionListenerStatements(Indent indent)
			throws Exception {
		String code = "${indent}${name}.addActionListener( new ActionListener() {\n";
		code = code + "${indent}\tpublic void actionPerformed() {\n";
		code = code + "${indent}\t\t${.actionHandler}\n";
		code = code + "${indent}\t}\n";
		code = code + "${indent}});";

		return eval(code, indent);
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
						.getMethod(getGetterName(property)).invoke(object, null);
				property = temp;
			} else {
				object = object.getClass()
						.getMethod(getGetterName(property)).invoke(object, null);
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
		CmdButton cmd = new CmdButton();
		cmd.setBorderColor(Color.RED);
		cmd.setActionHandler("do();");
		cmd.boundaryChanged(null, new Rectangle(10, 10, 100, 100));
		cmd.setText("Test Button");
		Button buttonWriter = new Button(cmd, "btnTest");
		try {
			Indent indent = new Indent();
			System.out.print(buttonWriter.getJavaImportDeclaration(indent));
			indent.indent();
			System.out.print(buttonWriter.getJavaFieldDeclaration(indent));
			indent.indent();
			System.out.print(buttonWriter
					.getJavaBuildMethodStatements(indent));
			System.out.print(buttonWriter
					.getJavaActionListenerStatements(indent));

		} catch (Exception e) {
			LOGGER.error("Error", e);
		}

	}
}
