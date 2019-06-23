package com.samisari.trash;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.samisari.graphics.util.FileChooserHistory;
import com.samisari.trash.RequestFileParser.FormField;

public class MD5HashTestFrame extends JFrame {
	private static final Logger logger = Logger
			.getLogger(FileChooserHistory.class);
	private static final String defaultKey = "6_!^7]A2_S]9&x6v@4=W";
	DefaultListModel model;
	JTextField txtKey;
	JTextField txtFieldValue;
	JTextField txtHash;
	JButton btnAddField;
	JButton btnRemoveField;
	JButton btnFieldUp;
	JButton btnFieldDown;
	JList lstFields;
	JMenuBar menuBar;
	JMenu mFile;
	JMenuItem miExit;
	JMenuItem miCalculate;
	JMenuItem miOpenFile;
	JFileChooser fcOpenRequestFile;
	File file;

	public MD5HashTestFrame() {
		super("MD5 Hash Tool");
		setLayout(null);
		setPreferredSize(new Dimension(500, 400));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		buildMenu();

		buildForm();

		setVisible(true);
		pack();

	}

	private void openFileChooser() {
		try {
			fcOpenRequestFile = new JFileChooser();
			if (JFileChooser.APPROVE_OPTION == fcOpenRequestFile
					.showOpenDialog(this))
				file = fcOpenRequestFile.getSelectedFile();
			else
				return;
			RequestFileParser rfp = new RequestFileParser(file);

			rfp.parse();
			model.clear();
			for (FormField ff : rfp.getFormFields()) {
				model.add(model.getSize(),
						ff.getValue().getBytes(Charset.forName("UTF8")).length
								+ ff.getValue());
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			logger.error("Exception", e);
		}
	}

	private void buildForm() {
		JLabel lblKey = new JLabel("Key");
		lblKey.setBounds(10, 10, 100, 25);
		getContentPane().add(lblKey);

		txtKey = new JTextField(defaultKey);
		txtKey.setBounds(115, 10, 100, 25);
		getContentPane().add(txtKey);

		JLabel lblField = new JLabel("Field value");
		lblField.setBounds(10, 40, 100, 25);
		getContentPane().add(lblField);

		txtFieldValue = new JTextField();
		txtFieldValue.setBounds(115, 40, 200, 25);
		getContentPane().add(txtFieldValue);

		btnAddField = new JButton("Add");
		btnAddField.setBounds(320, 40, 75, 25);
		btnAddField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addField();
			}
		});
		getContentPane().add(btnAddField);

		lstFields = new JList();
		lstFields.setBounds(10, 70, 400, 200);
		JScrollPane lstFieldsScroll = new JScrollPane(lstFields);
		lstFieldsScroll.setBounds(10, 70, 400, 200);
		getContentPane().add(lstFieldsScroll);

		model = new DefaultListModel<String>();
		lstFields.setModel(model);
		lstFields.getModel().addListDataListener(new ListDataListener() {

			@Override
			public void intervalRemoved(ListDataEvent arg0) {
				lstFields.repaint();

			}

			@Override
			public void intervalAdded(ListDataEvent arg0) {
				lstFields.repaint();

			}

			@Override
			public void contentsChanged(ListDataEvent arg0) {
				lstFields.repaint();

			}
		});

		btnRemoveField = new JButton("Del");
		btnRemoveField.setBounds(415, 70, 75, 25);
		btnRemoveField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeField();

			}
		});
		getContentPane().add(btnRemoveField);

		btnFieldUp = new JButton("Up");
		btnFieldUp.setBounds(415, 100, 75, 25);
		btnFieldUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				fieldUp();

			}
		});
		getContentPane().add(btnFieldUp);

		btnFieldDown = new JButton("Down");
		btnFieldDown.setBounds(415, 130, 75, 25);
		btnFieldDown.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				fieldDown();

			}
		});
		getContentPane().add(btnFieldDown);

		txtHash = new JTextField();
		txtHash.setBounds(10, 275, 400, 25);
		getContentPane().add(txtHash);

	}

	protected void fieldDown() {
		int index = lstFields.getSelectedIndex();
		model.add(index + 2, lstFields.getSelectedValue());
		model.remove(index);
		lstFields.repaint();

	}

	protected void fieldUp() {
		int index = lstFields.getSelectedIndex();
		model.add(index - 1, lstFields.getSelectedValue());
		model.remove(index + 1);
		lstFields.repaint();
	}

	protected void removeField() {
		model.remove(lstFields.getSelectedIndex());
		lstFields.repaint();
	}

	private void buildMenu() {
		menuBar = new JMenuBar();

		mFile = new JMenu("File");
		menuBar.add(mFile);

		miCalculate = new JMenuItem("Calculate Hash");
		miCalculate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				calculate();

			}

		});
		mFile.add(miCalculate);

		miExit = new JMenuItem("Exit");
		miExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mFile.add(miExit);

		miOpenFile = new JMenuItem("Open file");
		miOpenFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				openFileChooser();

			}
		});
		mFile.add(miOpenFile);

		setJMenuBar(menuBar);

	}

	protected void addField() {
		model.add(
				model.getSize(),
				txtFieldValue.getText().getBytes(Charset.forName("UTF8")).length
						+ txtFieldValue.getText());

	}

	private void calculate() {
		txtHash.setText(Encoder.hmacDigest(listToString(), txtKey.getText(),
				"HmacMD5"));

	}

	private String listToString() {
		StringBuilder sb = new StringBuilder();
		for (int index = 0; index < model.getSize(); index++)
			sb.append(model.getElementAt(index));
		return sb.toString();
	}

	public static void main(String[] args) {
		new MD5HashTestFrame();
	}
}
