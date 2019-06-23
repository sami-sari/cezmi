package com.samisari.gui.table.propertiestable;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.gui.table.propertiestable.propertyeditor.PropertyValueEditor;
import com.samisari.gui.table.propertiestable.propertyeditor.PropertyValueRenderer;
import com.samisari.gui.table.propertiestable.propertyeditor.TextEditor;

public class PropertiesTable extends JTable{
	private static final long serialVersionUID = 5706530102358606408L;
	private PropertyTableModel model;
	
	public PropertiesTable(AbstractCommand command, String filter) {
		super();
		model = new PropertyTableModel(command,filter);
		setModel(model);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setCellSelectionEnabled(true);
		setEnabled(true);
		PropertyValueRenderer propertyValueRenderer = new PropertyValueRenderer();
		PropertyValueEditor propertyValueEditor=new PropertyValueEditor(this);
		getColumnModel().getColumn(0).setCellRenderer(new TextEditor());
		getColumnModel().getColumn(1).setCellRenderer(propertyValueRenderer);
		getColumnModel().getColumn(1).setCellEditor(propertyValueEditor);
		setDefaultRenderer(Object.class, propertyValueRenderer);
        setDefaultEditor(Object.class, propertyValueEditor);
        getColumnModel().getColumn(0).setHeaderValue("Özellik");
        getColumnModel().getColumn(1).setHeaderValue("Değer");
		
	} 
	
	@Override
	public void editingStopped(ChangeEvent ce) {
		System.out.println("Editing Stopped:" + ce.getSource());
		AbstractCommand cmd = ((PropertyTableModel) getModel()).getCommand();
		String propertyName = (String) getModel().getValueAt(getSelectedRow(), 0);
		cmd.setPropertyValue(propertyName, ce.getSource());
		ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
		super.editingStopped(ce);
		//getModel().setValueAt(ce.getSource(), getSelectedRow(), 1);
		//this.repaint();
	}

}
