package com.samisari.graphics.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class SqlResultsTable extends JTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8493967668421947356L;
	private String sql;
	private Object[] parameters;
	private Connection connection;
	private ResultSet rs;
	private PreparedStatement pstm;
	List<String> columnTypes = new ArrayList<String>();
	private static HashMap<String, TableCellRenderer> columnRendererMap = new HashMap<String, TableCellRenderer>();

	static {
		columnRendererMap.put("java.lang.String", new DefaultTableCellRenderer());
		columnRendererMap.put("java.math.BigDecimal", new BigDecimalRendererComponent());
		columnRendererMap.put("default", new DefaultTableCellRenderer());
	}

	public SqlResultsTable(String sql, Object[] parameters, Connection connection) {
		super();
		setSql(sql);
		setConnection(connection);
		setParameters(parameters);
		if (sql != null) {
			setModel(buildModelByQuery());
			for (int i = 0; i < columnTypes.size(); i++) {
				TableCellRenderer renderer = columnRendererMap.get(columnTypes.get(i));
				if (renderer == null) {
					renderer = columnRendererMap.get(columnTypes.get(i));
				}
				getColumnModel().getColumn(i).setCellRenderer(renderer);
			}
		}
	}

	private TableModel buildModelByQuery() {
		DefaultTableModel model = new DefaultTableModel();
		try {
			pstm = connection.prepareStatement(sql);
			for (int i = 0; i < parameters.length; i++) {
				pstm.setObject(i, parameters[i]);
			}
			rs = pstm.executeQuery();
			for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
				model.addColumn(rs.getMetaData().getColumnName(i + 1));
				columnTypes.add(rs.getMetaData().getColumnClassName(i + 1));
			}
			while (rs.next()) {
				int columnCount = rs.getMetaData().getColumnCount();
				Object[] row = new Object[columnCount];
				for (int i = 0; i < columnCount; i++) {
					row[i] = rs.getObject(i + 1);
				}
				model.addRow(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				pstm.close();
			} catch (Exception e) {
			}
		}
		return model;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}
