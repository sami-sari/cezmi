package com.samisari.cezmi.animator.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.History;

public class FrameDiff extends LinkedHashMap<String, CommandDiff> implements Serializable{
	private static final long	serialVersionUID	= 1L;

	public FrameDiff(History oldFrame, History newFrame) {
		super();
		HashMap<String, AbstractCommand> commandsMap = new HashMap<String, AbstractCommand>();
		for (AbstractCommand c : newFrame) {
			commandsMap.put(c.getId(), c);
		}

		for (AbstractCommand c : oldFrame) {
			String id = c.getId();
			AbstractCommand newCommand = commandsMap.get(c.getId());
			if (newCommand != null) {
				CommandDiff diff = new CommandDiff(c, newCommand);
				if (diff.getChanges() != null && diff.getChanges().size() > 0)
					put(id, diff);
			}
		}
	}
	
	public void save(OutputStream os) throws IOException {
		os.write("<frameDiff>".getBytes(Charset.forName("UTF8")));
		for (CommandDiff diff:this.values()) {
			diff.save(os);
		}
		os.write("</frameDiff>".getBytes(Charset.forName("UTF8")));
	}
	
	public void load(InputStream is) {
		
	}

}
