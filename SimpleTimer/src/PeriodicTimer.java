import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class PeriodicTimer extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private Timer timer;
	private int count;
	private long time;
	private int period = 15000;
	private JLabel partNo = new JLabel("0");
	private JLabel chrono = new JLabel("0");
	private JButton resetBtn = new JButton("Start / Restart");
	private JButton stopBtn = new JButton("Stop");
	private JTextField partLength = new JTextField(period + "");
	private Properties tasks=new Properties();

	public PeriodicTimer() {
		super();
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		setPreferredSize(new Dimension(400, 300));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setAlwaysOnTop(true);

		partLength.setPreferredSize(new Dimension(100, 25));
		partLength.setMaximumSize(new Dimension(100, 25));
		add(partLength);

		partNo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		partNo.setPreferredSize(new Dimension(100, 50));
		add(partNo);

		chrono.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		chrono.setPreferredSize(new Dimension(50, 20));
		add(chrono);

		resetBtn.setPreferredSize(new Dimension(70, 20));
		resetBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				count = 0;
				time = 0;
				partNo.setText(count + "");
				chrono.setText((new Double(time).doubleValue() / 1000.00) + "");
				period = Integer.parseInt(partLength.getText());
				timer.stop();
				timer.restart();

			}
		});
		add(resetBtn);

		stopBtn.setPreferredSize(new Dimension(70, 20));
		stopBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				count = 0;
				time = 0;
				partNo.setText(count + "");
				chrono.setText((new Double(time).doubleValue() / 1000.00) + "");
				period = Integer.parseInt(partLength.getText());
				timer.stop();
				
			}
		});
		add(stopBtn);

		setVisible(true);
		pack();
	}

	public static void main(String[] args) {
		PeriodicTimer periodic = new PeriodicTimer();
		periodic.timer = new Timer(100, periodic);
		periodic.timer.setRepeats(true);
		periodic.timer.start();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		time += timer.getDelay();
		if (time >= period) {
			time = 0;
			count++;
			partNo.setText(count + "");
			try {
				Robot robot = new Robot();
				BufferedImage image = robot.createScreenCapture(
						getGraphicsConfiguration().getDevice().getDefaultConfiguration().getBounds());
				try {
					ImageIO.write(image, "PNG", new File(System.getProperty("user.home") + File.separator + "ImageData"
							+ File.separator + System.currentTimeMillis() + ".png"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (AWTException e1) {
				e1.printStackTrace();
			}

		}
		chrono.setText((new Double(time).doubleValue() / 1000.0) + "");
		repaint();
	}

	class ColorChanger implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			partNo.setForeground(Color.red);

		}

	}
}
