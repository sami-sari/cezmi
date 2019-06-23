package com.samisari.cezmi.interpreter;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.samisari.cezmi.component.CmdConnectableRectangle;
import com.samisari.cezmi.component.CmdLine;
import com.samisari.cezmi.component.CmdText;
import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.CommandProperty;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.Operation;
import com.samisari.cezmi.core.Status;

public class StreamingScriptParser {
	public static interface CommandParser {
		void parse(Reader reader);
	}

	private static final Logger											LOGGER		= Logger.getLogger(StreamingScriptParser.class);
	private static HashMap<String, Class<? extends AbstractCommand>>	commands	= new HashMap<>();
	private static HashMap<String, List<String>>						scripts		= new HashMap<>();
	private static List<String>											lines		= new ArrayList<>();
	private static HashMap<String, CommandParser>						tokenMap	= new HashMap<>();

	static {
		commands.put("Line", CmdLine.class);
		commands.put("Rectangle", CmdConnectableRectangle.class);
		commands.put("Text", CmdText.class);
	}

	private int							fps				= 30;
	private Color						color			= Color.BLACK;
	private Color						fillColor		= Color.WHITE;
	private Rectangle					bounds			= new Rectangle(100, 100, 100, 50);
	private List<CommandProperty>		props			= new ArrayList<>();
	private CommandProperty				currentProperty	= null;
	private HashMap<String, Variable>	variables		= new HashMap<>();
	private AbstractCommand				currentCommand;

	public static class Variable {
		public String	name;
		public Object	value;
	}

	public StreamingScriptParser() {
		super();
		tokenMap.put("create", (Reader reader) -> {
			StringBuilder buffer = new StringBuilder();
			try {
				trimReadSingleStringParameter(reader, buffer);
				StreamingScriptParser.this.create(buffer.toString());
			} catch (Exception e) {
				LOGGER.error(e);
			}
		});
		tokenMap.put("set", (Reader reader) -> {
			StringBuilder buffer = new StringBuilder();
			try {
				trimReadSingleStringParameter(reader, buffer);
				StreamingScriptParser.this.set(buffer.toString());
			} catch (Exception e) {
				LOGGER.error(e);
			}
		});
		tokenMap.put("val", (Reader reader) -> {
			StringBuilder buffer = new StringBuilder();
			try {
				trimReadSingleStringParameter(reader, buffer);
				StreamingScriptParser.this.value(buffer.toString());
			} catch (Exception e) {
				LOGGER.error(e);
			}
		});
		tokenMap.put("val", (Reader reader) -> {
			StringBuilder buffer = new StringBuilder();
			try {
				trimReadSingleStringParameter(reader, buffer);
				StreamingScriptParser.this.var(buffer.toString());
			} catch (Exception e) {
				LOGGER.error(e);
			}
		});
		tokenMap.put("=", (Reader reader) -> {
			StringBuilder buffer = new StringBuilder();
			try {
				readToken(reader, buffer);
				String var = buffer.toString();
				skipBlank(reader);
				variables.get(var).value = parse(reader);
			} catch (Exception e) {
				LOGGER.error(e);
			}
		});
		tokenMap.put("clp", (Reader reader) -> {
			try {
				skipBlank(reader);
				StreamingScriptParser.this.clearProperties();
			} catch (Exception e) {
				LOGGER.error(e);
			}
		});
		tokenMap.put("sel", (Reader reader) -> {
			try {
				skipBlank(reader);
				StreamingScriptParser.this.sel();
			} catch (Exception e) {
				LOGGER.error(e);
			}
		});
		tokenMap.put("mov", (Reader reader) -> {
			StringBuilder buffer = new StringBuilder();
			try {
				skipBlank(reader);
				readToken(reader, buffer);
				String var = buffer.toString();
				skipBlank(reader);
				readToken(reader, buffer);
				int dx = Integer.parseInt(buffer.toString());
				skipBlank(reader);
				readToken(reader, buffer);
				int dy = Integer.parseInt(buffer.toString());
				skipBlank(reader);
				StreamingScriptParser.this.move(var, dx, dy);
			} catch (Exception e) {
				LOGGER.error(e);
			}
		});
		tokenMap.put("scl", (Reader reader) -> {
			StringBuilder buffer = new StringBuilder();
			try {
				skipBlank(reader);
				readToken(reader, buffer);
				String var = buffer.toString();
				skipBlank(reader);
				readToken(reader, buffer);
				int sx = Integer.parseInt(buffer.toString());
				skipBlank(reader);
				readToken(reader, buffer);
				int sy = Integer.parseInt(buffer.toString());
				skipBlank(reader);
				StreamingScriptParser.this.scale(var, sx, sy);
			} catch (Exception e) {
				LOGGER.error(e);
			}
		});
		tokenMap.put("save", (Reader reader) -> {
			StringBuilder buffer = new StringBuilder();
			try {
				skipBlank(reader);
				readToken(reader, buffer);
				String macro = buffer.toString();
				skipBlank(reader);
				StreamingScriptParser.scripts.put(macro, lines);
				lines.clear();
			} catch (Exception e) {
				LOGGER.error(e);
			}
		});
		tokenMap.put("play", (Reader reader) -> {
			StringBuilder buffer = new StringBuilder();
			try {
				skipBlank(reader);
				readToken(reader, buffer);
				String macro = buffer.toString();
				skipBlank(reader);
				readToken(reader, buffer);
				int repetitions = Integer.parseInt(buffer.toString());
				skipBlank(reader);
				StreamingScriptParser.this.play(repetitions, macro);
			} catch (Exception e) {
				LOGGER.error(e);
			}
		});
	}

	private void trimReadSingleStringParameter(Reader reader, StringBuilder buffer) throws IOException {
		// Read trailing spaces read single token then read trailing spaces
		skipBlank(reader);
		readToken(reader, buffer);
		skipBlank(reader);
	}

	private void readToken(Reader reader, StringBuilder buffer) throws IOException {
		char c;
		// Read while not blank or end of line
		while ((c = (char) reader.read()) != ' ' && c != '\n') {
			buffer.append(c);
		}
	}

	private void skipBlank(Reader reader) throws IOException {
		// read while blank
		while ((char) reader.read() == ' ') {
			reader.mark(1);
		}
		reader.reset();
	}

	StringBuilder	buff	= new StringBuilder();
	String			token	= "";

	public Object parse(Reader reader) throws IOException {
		char c = (char) reader.read();
		buff.append(c);
		if (c == ' ') {
			token = buff.toString();
			buff = new StringBuilder();
			tokenMap.get(token).parse(reader);
		} else if (c == '\n')
			lines.add(buff.toString());

		return null;
	}

	private List<String> copyOf(List<String> lines) {
		ArrayList<String> lst = new ArrayList<>();
		for (int i = 0; i < lines.size() - 1; i++) {
			lst.add(new String(lines.get(i)));
		}
		return lst;
	}

	private void play(final int times, final String script) {
		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			private int count = 0;

			@Override
			public void run() {
				count++;
				try {
					runScript(script);
				} catch (IOException e) {
					LOGGER.error(e);
				}
				if (count > times) {
					timer.cancel();
				}
			}
		};
		timer.scheduleAtFixedRate(task, new Date(), 1000 / fps);
	}

	protected void runScript(String script) throws IOException {
		for (String line : scripts.get(script)) {
			parse(new StringReader(line));
		}
	}

	private void clearProperties() {
		props.clear();
	}

	private AbstractCommand create(String cmd) throws InstantiationException, IllegalAccessException {
		if (commands.containsKey(cmd)) {
			AbstractCommand c = commands.get(cmd).newInstance();
			c.setPropertyValue("color", color);
			c.setPropertyValue("borderColor", color);
			c.setPropertyValue("fillColor", fillColor);
			c.setCurrentStatus(Status.END);
			c.setBounds(bounds);
			for (CommandProperty prop : props)
				c.setPropertyValue(prop.getName(), prop.getValue());
			CommandManager.getDeaultInstance().getHistory().add(c);
			return c;
		}
		return null;
	}

	private CommandProperty set(String propName) {
		currentProperty = new CommandProperty(propName, "");
		return currentProperty;
	}

	private AbstractCommand get(String id) {
		currentCommand = CommandManager.getDeaultInstance().getCommand(id);
		return currentCommand;
	}

	private Object getProp(String prop) {
		for (CommandProperty p : currentCommand.listProperties(null)) {
			if (p.getName().equals(prop)) {
				currentProperty = p;
				return p.getValue();
			}
		}

		currentProperty = null;
		return null;
	}

	private void value(String propValue) {
		if (currentProperty != null) {
			currentProperty.setValue(propValue);
			props.add(currentProperty);
		}
	}

	private void sel() {
		for (AbstractCommand c : CommandManager.getDeaultInstance().getSelectedCommands()) {
			for (CommandProperty prop : props)
				c.setPropertyValue(prop.getName(), prop.getValue());
		}
	}

	private void move(String var, int dx, int dy) {
		AbstractCommand c = (AbstractCommand) variables.get(var).value;
		c.move(Operation.MOVE, dx, dy);
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
	}

	private void scale(String var, int sx, int sy) {
		AbstractCommand c = (AbstractCommand) variables.get(var).value;
		c.move(Operation.MOVE, sx, sy);
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
	}

	private void var(String name) {
		Variable var = new Variable();
		var.name = name;
		variables.put(name, var);
	}
}
