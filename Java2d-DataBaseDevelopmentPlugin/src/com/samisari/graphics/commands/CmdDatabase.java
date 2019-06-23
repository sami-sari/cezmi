package com.samisari.graphics.commands;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.samisari.common.util.ImageTool;
import com.samisari.guidesigner.CmdWindow;

public class CmdDatabase extends CmdWindow {
	private static final long serialVersionUID = 1L;

	public static class CmdDatabaseBeanInfo {
		private CmdDatabaseBeanInfo() {
			
		}
		private static final HashMap<String, FieldInfo> fields = new HashMap<>();
		static {
			fields.put("dataSourceName", new FieldInfo("dataSourceName", String.class, "getDataSourceName", "setDataSourceName", null));
			fields.put("jndiName", new FieldInfo("jndiName", String.class, "getJndiName", "setJndiName", null));
			fields.put("url", new FieldInfo("url", String.class, "getUrl", "setUrl", null));
			fields.put("driver", new FieldInfo("driver", String.class, "getDriver", "setDriver", null));
			fields.put("username", new FieldInfo("username", String.class, "getUsername", "setUsername", null));
			fields.put("password", new FieldInfo("password", String.class, "getPassword", "setPassword", null));
			fields.put("catalog", new FieldInfo("catalog", String.class, "getCatalog", "setCatalog", null));
			fields.put("schema", new FieldInfo("schema", String.class, "getSchema", "setSchema", null));
		}

		public static List<String> getFields() {
			ArrayList<String> list = new ArrayList<>();
			Iterator<String> iterator = fields.keySet().iterator();
			while (iterator.hasNext()) {
				list.add(iterator.next());
			}
			Collections.sort(list);
			return list;
		}

		public static String getGetter(String name) {
			return fields.get(name).getGetterMethodName();
		}

		public static String getSetter(String name) {
			return fields.get(name).getSetterMethodName();
		}

		public static Class<?> getType(String name) {
			return fields.get(name).getType();
		}
	}

	private String	dataSourceName;
	private String	jndiName;
	private String	url;
	private String	driver;
	private String	username;
	private String	password;
	private String	catalog;
	private String	schema;

	public CmdDatabase() {
		super();
		setTitleColor(Color.BLACK);
	}

	public String getCommandButtonText() {
		return "Database";
	}

	public Image getCommandButtonIcon() {
		return ImageTool.getResourceAsImage("com/samisari/graphics/commands/resources/CmdDatabse.png");
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public String getJndiName() {
		return jndiName;
	}

	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

	@Override
	public String toString() {
		return getDataSourceName() + "(" + getUrl() + ")";
	}

}
