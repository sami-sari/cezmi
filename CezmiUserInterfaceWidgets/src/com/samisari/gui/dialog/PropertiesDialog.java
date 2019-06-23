package com.samisari.gui.dialog;

import java.awt.FlowLayout;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.log4j.Logger;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandProperty;
import com.samisari.cezmi.core.ConsolePropertyManager;

public class PropertiesDialog extends JDialog {
	private static final long serialVersionUID = 1441451505783587266L;
	private static final Logger	logger	= Logger.getLogger(PropertiesDialog.class);
	JTable						propertiesTable;
	JScrollPane					tableScroll;
	AbstractCommand				command;

	public PropertiesDialog(AbstractCommand command) {
		super(ConsolePropertyManager.getDefaultInstance().getApplicationFrame(), "Properties");
		this.command = command;
		List<CommandProperty> properties = command.listProperties(null);
		Object[][] model = new Object[properties.size()][2];
		for (int i = 0; i < properties.size(); i++) {
			model[i][0] = properties.get(i).getName();
			model[i][1] = properties.get(i).getValue();
		}
		propertiesTable = new JTable(model, new String[] { "Name", "Value" });
		propertiesTable.getModel().addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent e) {
				String newValue = (String) propertiesTable.getValueAt(e.getFirstRow(), 1);
				String propertyName = (String) propertiesTable.getValueAt(e.getFirstRow(), 0);
				try {
					String setterName = "set" + propertyName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propertyName.substring(1);
					Method propertySetter = PropertiesDialog.this.command.getClass().getDeclaredMethod(setterName, String.class);
					propertySetter.invoke(PropertiesDialog.this.command, newValue);
				} catch (Exception e1) {
					logger.error("Error", e1);
				}
				ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
			}
		});
		tableScroll = new JScrollPane(propertiesTable);
		tableScroll.setBounds(10, 10, 200, 400);
		add(tableScroll);
		setSize(220, 480);
		setLayout(new FlowLayout());

	}

}
