import java.awt.*;

public class Mandelbrot extends Canvas {

	private int		x;
	private int		y;
	//X and Y are the JFrame coordinates and aren't involved in the fractal drawing
	private double	xMax;
	private double	xMin;
	private double	yMax;
	private double	yMin;
	//These four are used in the zoom calculation that I found online but don't quite understand
	public double	ZOOM;
	public double	zoomIncrease;
	public int		mouseX;
	public int		mouseY;
	private int		shiftX;
	private int		shiftY;
	public long		colorOffset;

	public Mandelbrot(int z, int a, int b, long c) {
		ZOOM = z;
		zoomIncrease = 50;
		shiftX = 0;
		shiftY = 0;
		colorOffset = c;

		xMax = 1600;
		xMin = 0;
		yMax = 900;
		yMin = 0;
		//This is window size
		setBackground(Color.WHITE);
	}

	public void paint(Graphics window) {
		x = 0;
		y = -1;
		for (int i = 0; i < 990; i++) {
			y++;
			x = -1;
			for (int j = 0; j < 1600; j++) {
				x++;
				double cR = ((x - shiftX - (xMax / 2.0)) / ZOOM);
				double cX = ((y - shiftY - (yMax / 2.0)) / ZOOM);
				//These are an edited part of the mandelbrot equation, and as far as I can tell deals with transformation of the fractal
				double zR = 0;
				double zX = 0;
				int r = 0;
				while (r <= 50 && zR <= 2) {
					r++;
					double ozR = zR;
					zR = (cR + ((zR * zR) - (zX * zX)));
					zX = (2 * (ozR * zX)) + cX;
					//All of this is calculations for the pixels
				}
				if (zR < 2) {
					window.setColor(Color.BLACK);
				} else {
					window.setColor(new Color((int) (r * colorOffset)));
				}
				window.drawRect(x, y, 1, 1);
			}
		}
	}

	public void zoom() {
		ZOOM += zoomIncrease;
		zoomIncrease *= 1.5;
		//shiftX = mouseX - (int)(750.0 * ZOOM / 450.0);
		//shiftY = mouseY - (int)(375.0 * ZOOM / 450.0);
		//repaint();

		double xDist = xMax + xMin;
		double yDist = yMax + yMin;
		double xTr = xDist / 900.0 * ((double) mouseX);
		double yTr = yDist / 1600.0 * ((double) mouseY);

		xMax = xTr + xDist / 2.0;
		xMin = xTr - xDist / 2.0;
		yMax = yTr + yDist / 2.0;
		yMin = yTr - yDist / 2.0;

		//The above is what I found online for a zoom function, but it doesn't work any better than mine (the commented lines above)
		repaint();
	}

	public void shift(int a, int b) {
		/*
		 * Negative X = left
		 * Positive X = right
		 * Negative Y = down
		 * Positive Y = up
		 */
		shiftX += a;
		shiftY -= b;
		repaint();
	}
}
