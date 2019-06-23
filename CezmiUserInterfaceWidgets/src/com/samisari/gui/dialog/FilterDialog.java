package com.samisari.gui.dialog;

import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JTextField;

import com.samisari.cezmi.core.ConsolePropertyManager;

public class FilterDialog extends JDialog {
	private static final long serialVersionUID = 4281363222997387646L;
	private static final int	DIALOG_WIDTH	= 400;
	private static final int	DIALOG_HEIGHT	= 300;

	private JButton				btnOk;
	private JList<String>		lstFilters;
	private JTextField			txtFilterName;
	private JTextField			txtFilterExpression;

	public FilterDialog() {
		super();
		setLayout(null);
		Rectangle parentBounds = ConsolePropertyManager.getDefaultInstance().getApplicationFrame().getBounds();
		int centerX = parentBounds.x + parentBounds.width / 2;
		int centerY = parentBounds.y + parentBounds.height / 2;

		setBounds(centerX - DIALOG_WIDTH / 2, centerY - DIALOG_HEIGHT / 2, DIALOG_WIDTH, DIALOG_HEIGHT);
		
		lstFilters = new JList<>(new String[] {"name='FRAME-\\d+'"});
		lstFilters.setBounds(10,10,DIALOG_WIDTH - 220, DIALOG_HEIGHT - 70);
		add(lstFilters);

		txtFilterName = new JTextField("Filtre");
		txtFilterName.setBounds(DIALOG_WIDTH-230 , 10, DIALOG_WIDTH - 220, 25);
		add(txtFilterName);
		
		txtFilterExpression = new JTextField("Expression");
		txtFilterExpression.setBounds(DIALOG_WIDTH-230 , 45, DIALOG_WIDTH - 220, 25);
		add(txtFilterExpression);

		
		btnOk = new JButton("Kaydet");
		btnOk.setBounds(DIALOG_WIDTH-150,DIALOG_HEIGHT - 35,100, 25);
		add(btnOk);
		
		btnOk.addActionListener(e -> FilterDialog.this.setVisible(false));
	}
}
