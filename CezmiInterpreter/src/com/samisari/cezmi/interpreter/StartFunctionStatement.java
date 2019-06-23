package com.samisari.cezmi.interpreter;

public class StartFunctionStatement extends Statement {
	@Override
	public void parse() {
		super.parse();
		Function func = new Function();
		func.setName(getText().substring("func ".length()));
		getContext().getFunctions().put(func.getName(),func);
		ScriptingContext ctx = new ScriptingContext();
		ctx.setParent(getContext());
		setContext(ctx);
	}
}
