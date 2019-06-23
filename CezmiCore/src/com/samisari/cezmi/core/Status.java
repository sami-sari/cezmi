package com.samisari.cezmi.core;

import java.io.IOException;
import java.io.Reader;

public enum Status {
	START, DRAGGING, END, CONTEXT_MENU, TYPING, CONNECT, OBJECT_1_SELECTED, OBJECT_2_SELECTED, EDIT_POINTS;
	public Status deserialize(Reader reader) {
		StringBuffer sb = new StringBuffer();
		int b;
		try {
			b = reader.read();
			while (b > -1) {
				sb.append((char) b);
				b = reader.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return valueOf(sb.toString());
	}
};
