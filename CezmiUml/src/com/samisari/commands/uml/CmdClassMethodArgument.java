package com.samisari.commands.uml;

public class CmdClassMethodArgument {
	private String	cmdClassId;
	private String	name;
	private String	type;
	private boolean	_final;

	public String getCmdClassId() {
		return cmdClassId;
	}

	public void setCmdClassId(String cmdClassId) {
		this.cmdClassId = cmdClassId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean is_final() {
		return _final;
	}

	public void set_final(boolean _final) {
		this._final = _final;
	}

}
