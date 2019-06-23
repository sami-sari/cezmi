package com.samisari.cezmi.interpreter;

public class GetStatement extends Statement{
	public GetStatement() {
		super();
	}
	public GetStatement(String text) {
		this();
		setText(text);
	}
	
	@Override
	public void parse() {
		super.parse();
		
	}
}
