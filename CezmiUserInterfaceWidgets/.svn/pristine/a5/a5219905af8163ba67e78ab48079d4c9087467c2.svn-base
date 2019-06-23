package com.samisari.gui.table.propertiestable.propertyeditor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class TextEditor implements TableCellEditor, TableCellRenderer{
	private String text;
	private ArrayList<CellEditorListener> editorListeners=new ArrayList<CellEditorListener>();
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		return new DefaultTableCellRenderer();
	}

	@Override
	public Object getCellEditorValue() {
		return text;
	}

	@Override
	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return false;
	}

	@Override
	public boolean stopCellEditing() {
		return false;
	}

	@Override
	public void cancelCellEditing() {
		
	}

	@Override
	public void addCellEditorListener(CellEditorListener l) {
		editorListeners.add(l);
		
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
		editorListeners.remove(l);
		
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		final JPanel panel = new JPanel();
		final JTextField textField = new JTextField(text);
		final JButton button = new JButton("...");
		panel.add(textField);
		panel.add(button);
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				final JDialog dialog = new JDialog();
				final JTextPane textEdit = new JTextPane();
				dialog.add(textEdit);
				dialog.setVisible(true);
			}
		});
		
		return panel;
	}

}
