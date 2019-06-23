package com.samisari.gui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.CommandSelectionChangeListener;
import com.samisari.cezmi.core.ConsolePropertyManager;
import com.samisari.gui.table.propertiestable.PropertiesTable;
import com.samisari.gui.table.propertiestable.PropertyTableModel;

public class PropertiesPanel extends JPanel implements CommandSelectionChangeListener {
	private static final long	serialVersionUID	= 346703268231541145L;
	private PropertiesTable		propertiesTable;
	private JScrollPane			tableScroll;
	private JTextField			filterTxt			= new JTextField();
	private AbstractCommand		command;

	public PropertiesPanel() {
		super();
		this.setLayout(new BorderLayout());
		this.command = CommandManager.getDeaultInstance().getCommand();
		CommandManager.getDeaultInstance().addSelectionChangeListener(this);
		propertiesTable = new PropertiesTable(command, filterTxt.getText());

		propertiesTable.getModel().addTableModelListener(e -> {
				String newValue = (String) propertiesTable.getValueAt(e.getFirstRow(), 1);
				String propertyName = (String) propertiesTable.getValueAt(e.getFirstRow(), 0);
				try {
					String setterName = "set" + propertyName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propertyName.substring(1);
					Method propertySetter = PropertiesPanel.this.command.getClass().getDeclaredMethod(setterName, String.class);
					propertySetter.invoke(PropertiesPanel.this.command, newValue);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
				ConsolePropertyManager.getDefaultInstance().getConsolePanel().repaint();
			}
		);

		DefaultTableColumnModel columnModel = new DefaultTableColumnModel();
		TableColumn nameColumn = new TableColumn(0, 100);
		TableColumn valueColumn = new TableColumn(1, 200);
		nameColumn.setHeaderValue("Özellik");
		valueColumn.setHeaderValue("Değer");
		columnModel.addColumn(nameColumn);
		columnModel.addColumn(valueColumn);
		tableScroll = new JScrollPane(propertiesTable);
		this.add(filterTxt, BorderLayout.NORTH);
		this.add(tableScroll);
		setBackground(Color.RED);
	}

	public AbstractCommand getCommand() {
		return command;
	}

	public void setCommand(AbstractCommand command) {
		this.command = command;
		Runnable propertyFillerThread =  () -> {
			int c0w = PropertiesPanel.this.propertiesTable.getColumnModel().getColumn(0).getWidth();
			int c1w = PropertiesPanel.this.propertiesTable.getColumnModel().getColumn(1).getWidth();
			PropertiesPanel.this.propertiesTable.setModel(new PropertyTableModel(command, filterTxt.getText()));
			PropertiesPanel.this.propertiesTable.getColumnModel().getColumn(0).setHeaderValue("Özellik");
			PropertiesPanel.this.propertiesTable.getColumnModel().getColumn(0).setPreferredWidth(c0w);
			PropertiesPanel.this.propertiesTable.getColumnModel().getColumn(1).setHeaderValue("Değer");
			PropertiesPanel.this.propertiesTable.getColumnModel().getColumn(1).setPreferredWidth(c1w);
			
			repaint();

		};
		SwingUtilities.invokeLater(propertyFillerThread);
	}

	@Override
	public void handleCommandSelectionChange(AbstractCommand cmd) {
		if (cmd.isSelected())
			setCommand(cmd);
		else {
			List<AbstractCommand> commandList = CommandManager.getDeaultInstance().getSelectedCommands();
			if (!commandList.isEmpty()) {
				setCommand(commandList.get(0));
			}
		}
	}

}
