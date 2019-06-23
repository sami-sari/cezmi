package com.samisari.cezmi.animator.commands;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

public class CmdAnimationWizard extends JDialog {
	private static final long		serialVersionUID		= -470764899939799346L;
	private static final Logger		logger					= Logger.getLogger(CmdAnimationWizard.class);
	private static final Object[][]	OBJECTS					= { { 0, 0, (String) null } };
	public static final Object[]	PARTS_COLUMN_NAMES		= new Object[] { "StartFrame", "EndFrame", "FileName" };
	public static final Class<?>[]	PARTS_COLUMN_CLASSES	= new Class<?>[] { Integer.class, Integer.class, String.class };

	public final JTable				partsTable				= new JTable(OBJECTS, PARTS_COLUMN_NAMES) {

																private static final long serialVersionUID = 1L;

																public java.lang.Class<?> getColumnClass(int column) {
																	return PARTS_COLUMN_CLASSES[column];
																};
															};

	public final JTable				soundsTable				= new JTable();

	private CmdAnimation			command;
	private JLabel					nameLbl					= new JLabel("Name");
	private JTextField				nameTxt;
	private final JTextField		superClassTxt			= new JTextField();
	private final JTextField		interfacesTxt			= new JTextField();
	private final JButton			addFieldBtn				= new JButton("+");
	private final JButton			addMethodBtn			= new JButton("+");
	private final JScrollPane		fieldsScroll			= new JScrollPane(partsTable);
	private final JScrollPane		methodsScroll			= new JScrollPane(soundsTable);
	private final JButton			removePartBtn			= new JButton("-");
	private final JButton			removeSoundBtn			= new JButton("-");
	private final JButton			saveBtn					= new JButton("OK");

	public CmdAnimationWizard(CmdAnimation cmd) {
		super();
		this.command = cmd;

	}
}
