package com.samisari.cezmi.interpreter;

public class VarStatement extends Statement{
	@Override
	public void parse() {
		super.parse();
		String line = getText();
		String varName = line.substring("var ".length());
		getContext().getVariables().put(varName, new Variable(varName, null));
	}
}
