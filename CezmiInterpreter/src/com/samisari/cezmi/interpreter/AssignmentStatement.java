package com.samisari.cezmi.interpreter;

import com.samisari.common.util.ParserUtils;

public class AssignmentStatement extends Statement {
	private Variable	lValue;
	private Statement	stm;

	@Override
	public void parse() {
		super.parse();
		int[] pos = { 0 };
		String valueExpression = ParserUtils.getTextAfter(getText(), " ");
		String varName = ParserUtils.getTextBefore(valueExpression, " ", pos);
		lValue = getContext().getVariables().get(varName);
		pos[0]++;
		String command = ParserUtils.getTextBefore(valueExpression, " ", pos);
		pos[0]++;
		if (command.equalsIgnoreCase("create")) {
			String type = valueExpression.substring(pos[0]);
			stm = new CreateStatement("create " + type);
			stm.setContext(getContext());
			stm.parse();
			ScriptParser.stack.pop();
		} else if (command.equalsIgnoreCase("get")) {
			String id = valueExpression.substring(pos[0]);
			stm = new GetStatement("get " + id);
			stm.parse();
			getContext().getVariables().get(varName).setValue(stm);
		}
	}

	@Override
	public Object run() {
		super.run();
		Object value = stm.run();
		lValue.setValue(value);
		return value;
	}
}
