import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class MouseListenerMandelbrotRunner extends JFrame implements KeyListener {
	public static JFrame		mainFrame;
	private static JLabel		headerLabel;
	public static JLabel		statusLabel;
	private static JPanel		controlPanel;
	private static final int	WIDTH		= 500;
	private static final int	HEIGHT		= 500;
	private static int			ZOOM		= 350;
	public static int			zoomBase	= 5;
	public static int			zoomEx		= 2;

	public static Mandelbrot	m			= new Mandelbrot(ZOOM, WIDTH + 300, HEIGHT - 40, 10);

	public MouseListenerMandelbrotRunner() {
		prepareGUI();
		mainFrame.addKeyListener(this);
	}

	public static void main(String[] args) {
		MouseListenerMandelbrotRunner mlmr = new MouseListenerMandelbrotRunner();
		MouseListenerMandelbrotRunner.showMouseListenerDemo();
		m.addMouseListener(new CustomMouseListener());
		m.requestFocus();

		mainFrame.getContentPane().add(m);

		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void prepareGUI() {
		mainFrame = new JFrame("Mandelbrot Zoom");
		mainFrame.setSize(1600, 990);
		//mainFrame.setLayout(new GridLayout(1, 0));

		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());

		//mainFrame.add(controlPanel);
		mainFrame.setVisible(true);
	}

	private static void showMouseListenerDemo() {

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		panel.setLayout(new FlowLayout());

		JLabel msglabel = new JLabel("", JLabel.CENTER);
		panel.add(msglabel);

		msglabel.addMouseListener(new CustomMouseListener());
		panel.add(msglabel);

		controlPanel.add(panel);
		mainFrame.setVisible(true);
	}

	public static Mandelbrot getFractal() {
		return m;
	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_A) {
			MouseListenerMandelbrotRunner.getFractal().shift(-300, 0);
		}
		if (key == KeyEvent.VK_D) {
			MouseListenerMandelbrotRunner.getFractal().shift(300, 0);
		}
		if (key == KeyEvent.VK_W) {
			MouseListenerMandelbrotRunner.getFractal().shift(0, 300);
		}
		if (key == KeyEvent.VK_S) {
			MouseListenerMandelbrotRunner.getFractal().shift(0, -300);
		}
		MouseListenerMandelbrotRunner.mainFrame.requestFocus();
	}

	public void keyReleased(KeyEvent e) {

	}
}

class CustomMouseListener implements MouseListener {
	public void mouseClicked(MouseEvent e) {
		MouseListenerMandelbrotRunner.getFractal().mouseX = e.getX();
		MouseListenerMandelbrotRunner.getFractal().mouseY = e.getY();
		MouseListenerMandelbrotRunner.getFractal().zoom();
		MouseListenerMandelbrotRunner.mainFrame.getContentPane().add(MouseListenerMandelbrotRunner.getFractal());
		MouseListenerMandelbrotRunner.mainFrame.requestFocus();
	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}
}