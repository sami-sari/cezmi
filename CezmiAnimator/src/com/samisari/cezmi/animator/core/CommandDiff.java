package com.samisari.cezmi.animator.core;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandProperty;
import com.samisari.common.util.bean.BeanUtils;

public class CommandDiff {
	private Class<? extends AbstractCommand>	commandClass;
	private String								commandId;
	private List<CommandProperty>				changes;

	CommandDiff(AbstractCommand oldCommand, AbstractCommand newCommand) {
		List<CommandProperty> oldProperties = oldCommand.listProperties(null);
		List<CommandProperty> newProperties = newCommand.listProperties(null);
		HashMap<String, CommandProperty> newPropertyMap = new HashMap<String, CommandProperty>();
		changes = new ArrayList<CommandProperty>();
		commandClass = newCommand.getClass();
		commandId = newCommand.getId();
		if (!oldCommand.getClass().equals(newCommand.getClass())) {
			commandClass = newCommand.getClass();
		}
		for (CommandProperty p : newProperties) {
			newPropertyMap.put(p.getName(), p);
		}
		for (CommandProperty p : oldProperties) {
			String name = p.getName();
			Object oldValue = p.getValue();
			Object newValue = newPropertyMap.get(name).getValue();
			if (newValue == null) {
				if (oldValue != null) {
					changes.add(new CommandProperty(name, null));
				}
			} else {
				if (oldValue == null || !newValue.equals(oldValue)) {
					changes.add(new CommandProperty(name, newValue));
				}
			}
		}
	}

	public Class<? extends AbstractCommand> getCommandClass() {
		return commandClass;
	}

	public void setCommandClass(Class<? extends AbstractCommand> commandClass) {
		this.commandClass = commandClass;
	}

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	public List<CommandProperty> getChanges() {
		return changes;
	}

	public void setChanges(List<CommandProperty> changes) {
		this.changes = changes;
	}

	public void save(OutputStream os) throws IOException {
		try {
			XMLStreamWriter xw = XMLOutputFactory.newInstance().createXMLStreamWriter(os, "UTF-8");

			if (changes != null && changes.size() > 0) {
				xw.writeStartElement(commandClass.getName());
				xw.writeStartElement("id");
				xw.writeCharacters(commandId);
				xw.writeEndElement();
				for (CommandProperty p : changes) {
					xw.writeStartElement(p.getName());
					// TODO: Might be characters or xml elements
					xw.writeCharacters(BeanUtils.value2String(p.getValue()));
					xw.writeEndElement();
				}
				xw.writeEndElement();
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		}

	}

}
