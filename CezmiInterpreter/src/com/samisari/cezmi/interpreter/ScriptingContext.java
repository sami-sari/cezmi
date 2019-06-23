package com.samisari.cezmi.interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScriptingContext {
	private String					functionName	= "";
	private Map<String, Variable>	variables		= new HashMap<>();
	private Map<String, Function>	functions		= new HashMap<>();
	private List<Statement>			statements		= new ArrayList<>();
	private ScriptingContext		parent			= null;
	private int						currentIndex	= 0;
	private boolean					active			= false;

	public ScriptingContext() {
		super();
	}

	public Map<String, Variable> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Variable> variables) {
		this.variables = variables;
	}

	public Map<String, Function> getFunctions() {
		return functions;
	}

	public void setFunctions(Map<String, Function> functions) {
		this.functions = functions;
	}

	public List<Statement> getStatements() {
		return statements;
	}

	public void setStatements(List<Statement> statements) {
		this.statements = statements;
	}

	public ScriptingContext getParent() {
		return parent;
	}

	public void setParent(ScriptingContext parent) {
		this.parent = parent;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
}
