package com.samisari.java2d.textwriter;

import com.samisari.graphics.commands.ICmdButton;
import com.samisari.graphics.commands.ICmdJTextBox;
import com.samisari.graphics.commands.ICmdRectangle;
import com.samisari.graphics.commands.ICmdText;
import com.samisari.graphics.commands.ICmdWindow;
import com.samisari.java2d.textwriter.commands.button.Button;
import com.samisari.java2d.textwriter.commands.label.Label;
import com.samisari.java2d.textwriter.commands.textField.TextField;
import com.samisari.java2d.textwriter.commands.window.Window;

public class CodeWriterFactory {
	public static JavaCodeWriter getJavaCodeWriter(ICmdRectangle comp) {
		JavaCodeWriter writer = null;
		if (comp instanceof ICmdText) {
			writer = new Label((ICmdText) comp, "");
		} else if (comp instanceof ICmdJTextBox) {
			writer = new TextField((ICmdJTextBox) comp, "");
		} else if (comp instanceof ICmdButton) {
			writer = new Button((ICmdButton) comp, "");
		} else if (comp instanceof ICmdWindow) {
			writer = new Window((ICmdWindow) comp, "");
		}
		return writer;
	}
}
