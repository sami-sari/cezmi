package com.samisari.gui.dialog;

import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.samisari.cezmi.core.ConsolePropertyManager;

public class RectangleDialog extends JDialog {
	private static final long		serialVersionUID	= 4499646382164589003L;
	private static final int		DIALOG_WIDTH		= 400;
	private static final int		DIALOG_HEIGHT		= 300;

	private Rectangle				rectangle;
	private PropertyChangeSupport	changeSupport;

	public RectangleDialog(Rectangle rectangle) {
		super();
		setLayout(null);
		changeSupport = new PropertyChangeSupport(this);
		if (rectangle == null) {
			rectangle = new Rectangle(0, 0, 0, 0);
		}
		final JFrame frame = ConsolePropertyManager.getDefaultInstance().getApplicationFrame();
		final Rectangle parentBounds = (frame == null
				? new Rectangle(0, 0, (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight())
				: frame.getBounds());
		final JButton btnOk = new JButton("Kaydet");
		final JTextField txtLeft = new JTextField(rectangle.x);
		final JTextField txtTop = new JTextField(rectangle.y);
		final JTextField txtWidth = new JTextField(rectangle.width);
		final JTextField txtHeight = new JTextField(rectangle.height);
		final JLabel lblLeft = new JLabel("Sol");
		final JLabel lblTop = new JLabel("Üst");
		final JLabel lblWidth = new JLabel("Genişlik");
		final JLabel lblHeight = new JLabel("Yükseklik");
		final int centerX = parentBounds.x + parentBounds.width / 2;
		final int centerY = parentBounds.y + parentBounds.height / 2;
		this.rectangle = rectangle;
		setBounds(centerX - DIALOG_WIDTH / 2, centerY - DIALOG_HEIGHT / 2, DIALOG_WIDTH, DIALOG_HEIGHT);

		lblLeft.setBounds(10, 35, 100, 25);
		add(lblLeft);
		txtLeft.setBounds(150, 35, 100, 25);
		add(txtLeft);
		lblTop.setBounds(10, 65, 100, 25);
		add(lblTop);
		txtTop.setBounds(150, 65, 100, 25);
		add(txtTop);
		lblWidth.setBounds(10, 95, 100, 25);
		add(lblWidth);
		txtWidth.setBounds(150, 95, 100, 25);
		add(txtWidth);
		lblHeight.setBounds(10, 125, 100, 25);
		add(lblHeight);
		txtHeight.setBounds(150, 125, 100, 25);
		add(txtHeight);
		btnOk.setBounds(DIALOG_WIDTH - 150, DIALOG_HEIGHT - 35, 100, 25);
		add(btnOk);

		btnOk.addActionListener(e -> {
			firePropertyChange("rectangle", RectangleDialog.this.rectangle, new Rectangle(Integer.parseInt(txtLeft.getText()), Integer.parseInt(txtTop.getText()),
					Integer.parseInt(txtWidth.getText()), Integer.parseInt(txtHeight.getText())));
			RectangleDialog.this.setVisible(false);
		});
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	@Override
	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		if (changeSupport != null)
			changeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

}
