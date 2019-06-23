package com.samisari.java2d.textwriter.commands.label;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STRawGroupDir;

import com.samisari.graphics.commands.ICmdText;
import com.samisari.java2d.textwriter.JavaCodeWriter;
import com.samisari.java2d.textwriter.commands.Indent;

public class Label implements JavaCodeWriter {
	private static final Logger logger = Logger
			.getLogger(Label.class);
	private ICmdText cmd;
	String name = "label";
	STRawGroupDir stgd = new STRawGroupDir(
			"com/samisari/java2d/textwriter/commands/label","UTF-8");
	public Label(ICmdText cmd, String name) {
		setCmd(cmd);
		setName(name);
	}

	public ICmdText getCmd() {
		return cmd;
	}

	public void setCmd(ICmdText cmd) {
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

	public static void main(String[] args) {
		logger.debug("");
	}
}
