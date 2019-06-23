package com.samisari.cezmi.interpreter;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.samisari.cezmi.component.CmdConnectableRectangle;
import com.samisari.cezmi.component.CmdLine;
import com.samisari.cezmi.component.CmdText;
import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandProperty;
import com.samisari.common.util.ParserUtils;

public class CreateStatement extends Statement {
	private static final Logger											logger		= Logger.getLogger(CreateStatement.class);
	private Color														color		= Color.BLACK;
	private Color														fillColor	= Color.WHITE;
	private Rectangle													bounds		= new Rectangle(100, 100, 100, 50);
	private List<CommandProperty>										props		= new ArrayList<>();
	private static HashMap<String, Class<? extends AbstractCommand>>	commands	= new HashMap<>();
	static {
		commands.put("Line", CmdLine.class);
		commands.put("Rectangle", CmdConnectableRectangle.class);
		commands.put("Text", CmdText.class);
	}

	private ScriptingContext					ctx;
	private AbstractCommand						result;
	private Class<? extends AbstractCommand>	type;

	public CreateStatement() {
		super();
	}
	public CreateStatement(String text) {
		this();
		setText(text);
	}
	@Override
	public Object run() {
		try {
			result = type.newInstance();
			result.run();
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error(e);
		}
		return result;
	}

	@Override
	public void parse() {
		String cmd = ParserUtils.getTextAfter(getText(), " ");
		if (commands.containsKey(cmd)) {
			type = commands.get(cmd);
			ScriptParser.stack.push(ctx);
		}
	}
	

	public ScriptingContext getCtx() {
		return ctx;
	}

	public void setCtx(ScriptingContext ctx) {
		this.ctx = ctx;
	}

	public AbstractCommand getResult() {
		return result;
	}

	public void setResult(AbstractCommand result) {
		this.result = result;
	}

	public Class<? extends AbstractCommand> getType() {
		return type;
	}

	public void setType(Class<? extends AbstractCommand> type) {
		this.type = type;
	}
}
