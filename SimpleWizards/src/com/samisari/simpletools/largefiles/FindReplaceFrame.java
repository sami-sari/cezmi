package com.samisari.simpletools.largefiles;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class FindReplaceFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final String DOSYA = "Dosya";

	private static final String DOSYA_AC_TOOLTIP = "Dosya seç";

	private static final String ARANACAK_METIN = "Aranacak Metin";

	private static final String YENI_METIN = "Değiştirilece Metin";

	private JTextField fileName;
	private JButton activateFileChooserBtn;
	private JFileChooser fileChooser;
	private JTextField originalText;
	private JTextField newText;
	private JTable replacementTable;

	private FindReplaceFrame() {
		super("Find and Replace Large Files");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new GridBagLayout());
		setPreferredSize(new Dimension(800, 600));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;

		JLabel l1 = new JLabel(DOSYA);
		add(l1, gbc);

		gbc.gridx++;
		gbc.weightx = 1.0;
		fileName = new JTextField();
		fileName.setPreferredSize(new Dimension(200, 25));
		add(fileName, gbc);

		gbc.gridx++;
		gbc.weightx = 0.0;
		activateFileChooserBtn = new JButton("?");
		activateFileChooserBtn.setPreferredSize(new Dimension(25, 25));
		activateFileChooserBtn.setToolTipText(DOSYA_AC_TOOLTIP);
		activateFileChooserBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser = new JFileChooser();
				int result = fileChooser.showOpenDialog(FindReplaceFrame.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					fileName.setText(selectedFile.getAbsolutePath());
				}
			}
		});
		add(activateFileChooserBtn, gbc);

		gbc.weightx = 0.0;
		gbc.gridy++;
		gbc.gridx = 0;
		JLabel l2 = new JLabel(ARANACAK_METIN);
		add(l2, gbc);

		gbc.gridx++;
		gbc.weightx = 1.0;
		originalText = new JTextField();
		originalText.setPreferredSize(new Dimension(25, 400));
		add(originalText, gbc);

		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 0.0;
		JLabel l3 = new JLabel(YENI_METIN);
		add(l3, gbc);

		gbc.gridx++;
		gbc.weightx = 1.0;
		newText = new JTextField();
		newText.setPreferredSize(new Dimension(25, 400));
		add(newText, gbc);

		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 4;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		replacementTable = new JTable();
		JScrollPane scrollPane = new JScrollPane(replacementTable);
		add(scrollPane, gbc);

		setVisible(true);
		pack();
	}

	public static void main(String[] args) {
		new FindReplaceFrame();
	}
}
