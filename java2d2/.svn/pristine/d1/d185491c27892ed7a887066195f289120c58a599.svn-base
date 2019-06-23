package com.samisari.graphics.commands;

public class Indent {
	int level = 0;
	private static final String unit = "\t";

	public void indent() {
		level++;

	}

	public void unindent() throws Exception {
		if (level > 0)
			level--;
		else
			throw new Exception("Negative Indent");
	}

	public String toString() {
		String indent = "";
		for (int i = 0; i < level; i++)
			indent = indent + unit;
		return indent;
	}
}
