package com.samisari.commands.uml;

import java.util.List;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.graphics.commands.CmdStatement;

public class CmdCodeBlock extends AbstractCommand{
	private static final long serialVersionUID = 8992275833960288045L;
	private boolean _static;
	private List<CmdStatement> statements;
	private String text;
	public boolean is_static() {
		return _static;
	}
	public void set_static(boolean _static) {
		this._static = _static;
	}
	public List<CmdStatement> getStatements() {
		return statements;
	}
	public void setStatements(List<CmdStatement> statements) {
		this.statements = statements;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
