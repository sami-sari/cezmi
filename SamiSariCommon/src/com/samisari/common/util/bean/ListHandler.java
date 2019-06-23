package com.samisari.common.util.bean;

import java.awt.Rectangle;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ListHandler {

	private static Log						logger				= LogFactory.getLog(ListHandler.class); 
	
	public static String toString(List<?> list) {
		try {
			StringWriter sw = new StringWriter();
			sw.write("<list>" );
			for (Object item:list) {
				sw.write("<item>" );
				sw.write(BeanUtils.value2String(item));
				sw.write("</item>" );
			}
			sw.write("</list>");
			return sw.toString();
		} catch (Exception e1) {
			logger.error("Unexpected Error", e1);
		}
		return null;
	}

	public static List<Object> toObject(String text) {
		List<Object> list = new ArrayList<Object>();
		text = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + text;
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.parse(new InputSource(new StringReader(text)));
			Element root = doc.getDocumentElement();
			NodeList items = root.getElementsByTagName("item");
			for (int i = 0; i < items.getLength(); i++) {
				Node item = items.item(i);
				list.add(BeanUtils.parse(item.getTextContent()));
			}
		} catch (Exception e) {
			logger.error("Unexpected Error", e);
		}
		return list;
	}
	public static List<Object> toObject(Element elm) {
		List<Object> list = new ArrayList<Object>();
		try {
			Element root = (Element) elm.getFirstChild();
			NodeList items = root.getElementsByTagName("item");
			for (int i = 0; i < items.getLength(); i++) {
				Node item = items.item(i).getFirstChild();
				String className = item.getNodeName();
				if (className.equals("#text")) {
					Object bean = BeanUtils.parse(item.getNodeValue());
					list.add(bean);
				} else {
					Class<?> type = Class.forName(className);
					Object bean = type.newInstance();
					BeanUtils.xml2Value(bean, type, null, (Element) item);
					list.add(bean);
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected Error", e);
		}
		return list;
	}

	public static void main(String[] args) {
		List<Object> list = new ArrayList<>();
		list.add(new Rectangle(0, 0, 10, 10));
		list.add(new Rectangle(50, 0, 10, 10));
		list.add(new Rectangle(0, 50, 10, 10));
		list.add(new Rectangle(50, 50, 10, 10));
		list.add("<blabla>");
		String text = toString(list);
		logger.info(text);
		List<Object> deserialisedList = toObject(text);
		for (Object o : deserialisedList) {
			logger.info(o.toString());
		}
	}
}
