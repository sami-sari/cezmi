package com.samisari.cezmi.interpreter;

import java.util.List;

public class Function {
	private String		name;
	private Variable[]	arguments;
	private Object		returnValue;
	private List<Statement> statements;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Variable[] getArguments() {
		return arguments;
	}

	public void setArguments(Variable[] arguments) {
		this.arguments = arguments;
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}

	public List<Statement> getStatements() {
		return statements;
	}

	public void setStatements(List<Statement> statements) {
		this.statements = statements;
	}
}
