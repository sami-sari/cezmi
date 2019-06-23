package com.samisari.cezmi.interpreter;

import java.io.Reader;
import java.text.MessageFormat;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.cezmi.core.Operation;
import com.samisari.common.util.ParserUtils;

public class ScriptParser {

	public static interface CommandParser {
		void parse(Reader reader);
	}

	private static final Logger							LOGGER			= Logger.getLogger(ScriptParser.class);

	public static Stack<ScriptingContext>				stack			= new Stack<>();
	public static ScriptingContext						ctx				= new ScriptingContext();
	private static HashMap<String, List<String>>		scripts			= new HashMap<>();
	private static List<String>							lines			= new ArrayList<>();

	public static final LinkedHashMap<String, Symbol>	SYMBOL_TABLE	= new LinkedHashMap<>();
	static {
		SYMBOL_TABLE.put("create", new Symbol("create", 1, CreateStatement.class));
		SYMBOL_TABLE.put("func", new Symbol("func", 2, StartFunctionStatement.class));
		SYMBOL_TABLE.put("endFunc", new Symbol("endFunc", 3, EndFunctionStatement.class));
		SYMBOL_TABLE.put("get", new Symbol("get", 4, GetStatement.class));
		SYMBOL_TABLE.put("setProp", new Symbol("setProp", 5, SetPropStatement.class));
		SYMBOL_TABLE.put("getProp", new Symbol("getProp", 6, GetPropStatement.class));
		SYMBOL_TABLE.put("play", new Symbol("play", 7, PlayStatement.class));
		SYMBOL_TABLE.put("exec", new Symbol("exec", 8, ExecStatement.class));
		SYMBOL_TABLE.put("save", new Symbol("save", 9, SaveStatement.class));
		SYMBOL_TABLE.put("end", new Symbol("end", 10, EndStatement.class));
		SYMBOL_TABLE.put("var", new Symbol("var", 11, VarStatement.class));
		SYMBOL_TABLE.put("=", new Symbol("=", 12, AssignmentStatement.class));
		SYMBOL_TABLE.put("\n", new Symbol("\n", 13, LineEnd.class));
		SYMBOL_TABLE.put(" ", new Symbol(" ", 14, Separator.class));
		SYMBOL_TABLE.put(".", new Symbol("", 15, Period.class));

	}
	private int fps = 30;

	//	private CommandProperty				currentProperty	= null;
	//	private AbstractCommand				currentCommand;

	public ScriptParser() {
		super();
	}

	StringBuilder						buffer			= new StringBuilder();
	String								token			= "";
	boolean								function		= false;
	String								functionName	= null;
	static final Map<String, Function>	FUNCTIONS		= new HashMap<>();

	//	public static class Function {
	//		private List<String>	statements	= new ArrayList<>();
	//		private Object			returnValue	= null;
	//
	//		public List<String> getStatements() {
	//			return statements;
	//		}
	//
	//		public void setStatements(List<String> statements) {
	//			this.statements = statements;
	//		}
	//
	//		public Object getReturnValue() {
	//			return returnValue;
	//		}
	//
	//		public void setReturnValue(Object returnValue) {
	//			this.returnValue = returnValue;
	//		}
	//	}

	public Object parse(String line) throws InstantiationException, IllegalAccessException {
		String[] tokens = line.split(" ");
		if (SYMBOL_TABLE.containsKey(tokens[0])) {
			LOGGER.info(MessageFormat.format("Matched: {0}", tokens[0]));
			Class<?> handlerClass = SYMBOL_TABLE.get(tokens[0]).getHandler();
			Statement handler = (Statement) handlerClass.newInstance();
			handler.setContext(ctx);
			handler.setText(line);
			handler.parse();
			handler.run();
			return handler;
		}
		if (function) {
			Statement stm = new Statement();
			stm.setText(line);
			stm.parse();
			ctx.getFunctions().get(functionName).getStatements().add(stm);
		} else if (line.startsWith("create ")) {
			CreateStatement stm = new CreateStatement();
			stm.setCtx(ctx);
			stm.setText(line.substring("create ".length()));
			stm.parse();
			ctx.getStatements().add(stm);
		} else if (line.startsWith("func ")) {
			function = true;
			functionName = line.substring("func ".length());
			FUNCTIONS.put(functionName, new Function());
		} else if (line.startsWith("endFunc ")) {
			function = false;
			functionName = null;
		} else if (line.startsWith("exec ")) {
			List<Statement> statements = ctx.getFunctions().get(line.substring("exec ".length())).getStatements();
			for (Statement statement : statements) {
				statement.parse();
				statement.run();
			}
			//		} else if (line.startsWith("set "))
			//			set(line.substring("set ".length()));
			//		else if (line.startsWith("val "))
			//			value(line.substring("val ".length()));
			//		else if (line.equalsIgnoreCase("clear props"))
			//			clearProperties();
			//		else if (line.equalsIgnoreCase("sel"))
			//			this.sel();
		} else if (line.startsWith("mov ")) {
			int[] pos = new int[] { 0 };
			String var = ParserUtils.getTextBetween(line, "mov ", " ", pos);
			int x = Integer.parseInt(ParserUtils.getTextBefore(line, " ", pos));
			int y = Integer.parseInt(line.substring(pos[0] + 1));
			move(var, x, y);
		} else if (line.startsWith("scale ")) {
			int[] pos = new int[] { 0 };
			int sx = Integer.parseInt(ParserUtils.getTextBetween(line, "scale ", " ", pos));
			int sy = Integer.parseInt(line.substring(pos[0]));
			this.scale(sx, sy);
		} else if (line.startsWith("save ")) {
			scripts.put(line.substring("save ".length()), copyOf(lines));
			lines.clear();
		} else if (line.startsWith("play ")) {
			int[] pos = new int[] { 0 };
			String script = ParserUtils.getTextBetween(line, "play ", " ", pos);
			int times = Integer.parseInt(line.substring(pos[0]));
			this.play(times, script);
			//		} else if (line.startsWith("var ")) {
			//			String varName = line.substring("var ".length());
			//			ctx.getVariables().put(varName, new Variable(varName, null));
		} else if (line.startsWith("=")) {
			String valueExpression = line.substring("=".length());
			setVar(valueExpression);
		}
		return null;
	}

	private void setVar(String valueExpression) {
		int[] pos = { 0 };
		String varName = ParserUtils.getTextBefore(valueExpression, " ", pos);
		pos[0]++;
		String command = ParserUtils.getTextBefore(valueExpression, " ", pos);
		pos[0]++;
		if (command.equalsIgnoreCase("create")) {
			String type = valueExpression.substring(pos[0]);
			CreateStatement stm = new CreateStatement("create " + type);
			ctx.getVariables().get(varName).setValue(stm.run());
		} else if (command.equalsIgnoreCase("get")) {
			String id = valueExpression.substring(pos[0]);
			ctx.getVariables().get(varName).setValue(get(id));
		}
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
				runScript(script);
				if (count > times) {
					timer.cancel();
				}
			}
		};
		timer.scheduleAtFixedRate(task, new Date(), 1000 / fps);
	}

	protected void runScript(String script) {
		for (String line : scripts.get(script)) {
			try {
				parse(line);
			} catch (InstantiationException | IllegalAccessException e) {

			}
		}
	}

	//	private CommandProperty set(String propName) {
	//		currentProperty = new CommandProperty(propName, "");
	//		return currentProperty;
	//	}

	private AbstractCommand get(String id) {
		return CommandManager.getDeaultInstance().getCommand(id);
	}

	//	private Object getProp(String prop) {
	//		for (CommandProperty p : currentCommand.listProperties(null)) {
	//			if (p.getName().equals(prop)) {
	//				currentProperty = p;
	//				return p.getValue();
	//			}
	//		}
	//
	//		currentProperty = null;
	//		return null;
	//	}

	//	private void value(String propValue) {
	//		if (currentProperty != null) {
	//			currentProperty.setValue(propValue);
	//			props.add(currentProperty);
	//		}
	//	}
	//
	//	private void sel() {
	//		for (AbstractCommand c : CommandManager.getDeaultInstance().getSelectedCommands()) {
	//			for (CommandProperty prop : props)
	//				c.setPropertyValue(prop.getName(), prop.getValue());
	//		}
	//	}

	private void move(String var, int dx, int dy) {
		AbstractCommand c = (AbstractCommand) ctx.getVariables().get(var).getValue();
		c.move(Operation.MOVE, dx, dy);
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
	}

	private void scale(int sx, int sy) {
		for (AbstractCommand c : CommandManager.getDeaultInstance().getSelectedCommands()) {
			c.move(Operation.MOVE, sx, sy);
		}
	}

	private void var(String name) {
		Variable var = new Variable();
		var.setName(name);
		ctx.getVariables().put(name, var);
	}
}
