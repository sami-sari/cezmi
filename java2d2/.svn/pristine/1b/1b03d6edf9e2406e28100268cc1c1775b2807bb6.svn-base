package com.samisari.trash;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RequestFileParser {
	public static class FormField {
		String	name;
		String	value;

		public FormField(String name, String value) {
			super();
			setName(name);
			setValue(value);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	File			f;
	List<FormField>	formFields;

	public RequestFileParser(File file) {
		f = file;

	}

	public void parse() throws ParserConfigurationException, SAXException, IOException {
		formFields = new ArrayList<FormField>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();

		Document doc = db.parse(f);
		Element root = doc.getDocumentElement();
		NodeList entryNodes = root.getElementsByTagName("entry");

		for (int i = 0; i < entryNodes.getLength(); i++) {
			Node entry = entryNodes.item(i);
			String key = entry.getAttributes().getNamedItem("key").getTextContent();
			String value = entry.getTextContent();
			formFields.add(new FormField(key, value));
		}
	}

	public List<FormField> getFormFields() {
		return formFields;
	}

}
