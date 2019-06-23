package com.samisari.cezmi.interpreter;

public class EndFunctionStatement extends Statement{
	@Override
	public void parse() {
		super.parse();
		setContext(getContext().getParent());
	}
}
