package com.samisari.graphics.toolbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.samisari.cezmi.component.CmdCircle;
import com.samisari.cezmi.component.CmdConnectableRectangle;
import com.samisari.cezmi.component.CmdImage;
import com.samisari.cezmi.component.CmdLayer;
import com.samisari.cezmi.component.CmdLine;
import com.samisari.cezmi.component.CmdMultiText;
import com.samisari.cezmi.component.CmdPan;
import com.samisari.cezmi.component.CmdPolyLines;
import com.samisari.cezmi.component.CmdText;
import com.samisari.cezmi.component.CmdUnLayer;
import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.Status;
import com.samisari.common.util.ImageTool;
import com.samisari.debugtools.performance.PerformanceInspector;
import com.samisari.graphics.commands.CmdButton;
import com.samisari.graphics.commands.CmdClearHistory;
import com.samisari.graphics.commands.CmdColor;
import com.samisari.graphics.commands.CmdCompactHistory;
import com.samisari.graphics.commands.CmdJTextBox;
import com.samisari.graphics.commands.CmdOpenFile;
import com.samisari.graphics.commands.CmdProperties;
import com.samisari.graphics.commands.CmdRotate;
import com.samisari.graphics.commands.CmdSOAlign;
import com.samisari.graphics.commands.CmdSODelete;
import com.samisari.graphics.commands.CmdSODown;
import com.samisari.graphics.commands.CmdSODuplicate;
import com.samisari.graphics.commands.CmdSOUp;
import com.samisari.graphics.commands.CmdSave;
import com.samisari.graphics.commands.CmdSelectObject;
import com.samisari.graphics.commands.CmdShowGrid;
import com.samisari.graphics.commands.CmdShowHistory;
import com.samisari.graphics.commands.CmdNewDocument;
import com.samisari.guidesigner.CmdWindow;


public class ToolBox extends JPanel {
	private static final org.apache.log4j.Logger logger = Logger
			.getLogger(ToolBox.class);

	private static PerformanceInspector perfInspector = PerformanceInspector
			.get(ToolBox.class.getName());

	private static final int MARGIN = 10;
	private static final int SPACING = 10;
	private static final int PANEL_WIDTH = 210;
	private static final int PANEL_HEIGHT = 600;
	private static final int BUTTON_SIZE = 40;
	private static final int COLUMNS = (PANEL_WIDTH - 2 * MARGIN + SPACING)
			/ (SPACING + BUTTON_SIZE);
	private static final Properties properties = new Properties();
	private static final HashMap<String, Image> imageMap = new HashMap<String, Image>();
	/**
	 * The map that keeps command button areas
	 */
	private static AreaMap<java.lang.Class<?>> commandMap;
	private int col = 0, row = 0;
	private int buttonCount = 0;

	/**
	 * Add a new command setting command button area and the handler class
	 * 
	 * @param x1
	 * @param y1
	 * @param length
	 * @param height
	 * @param clazz
	 */
	public void addCommand(int x1, int y1, int length, int height,
			Class<?> clazz) {
		commandMap.add(new Area(x1, y1, length, height), clazz);
	}

	/**
	 * Given the clicked location calculates which command button area has been
	 * clicked and returns associated command
	 * 
	 * @param x
	 * @param y
	 * @return new instance of matching command or null if none is matched
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */

	public AbstractCommand getCommand(int x, int y)
			throws InstantiationException, IllegalAccessException {
		AbstractCommand command = CommandManager.getDeaultInstance().getCommand();
		if (command == null
				|| command.getCurrentStatus() == Status.START
				|| command.getCurrentStatus() == Status.END) {

			command = commandMap.get(x, y) != null ? (AbstractCommand) commandMap
					.get(x, y).newInstance() : null;
		}
		CommandManager.getDeaultInstance().setCommand(command);
		return command;
	}

	static {
		perfInspector.classStaticInitStart();
		boolean existsPropertyFile = true;
		try {
			properties.load(ClassLoader
					.getSystemResourceAsStream("Commands.properties"));
		} catch (Exception e) {
			logger.warn("Commmands.properties dosyası bulunamadı",e);
			existsPropertyFile = false;
		}
		if (existsPropertyFile) {
			String className = null;
			for (int i = 1; (className = properties.getProperty(Integer
					.toString(i))) != null; i++) {
				Class<?> clazz;
				try {
					clazz = Class.forName(className);
					imageMap.put(clazz.getSimpleName(), getButtonImage(clazz));
				} catch (ClassNotFoundException e) {
					logger.error(String.format("Class %1$s blunamadı", className) , e);
				}
			}
		}
		imageMap.put("CmdLine", getButtonImage(CmdLine.class));
		imageMap.put("CmdPolyLines", getButtonImage(CmdPolyLines.class));
		imageMap.put("CmdCircle", getButtonImage(CmdCircle.class));
		imageMap.put("CmdConnectableRectangle", getButtonImage(CmdConnectableRectangle.class));
		imageMap.put("CmdText", getButtonImage(CmdText.class));
		imageMap.put("CmdColor", getButtonImage(CmdColor.class));
		imageMap.put("CmdSave", getButtonImage(CmdSave.class));
		imageMap.put("CmdSelectObject", getButtonImage(CmdSelectObject.class));
		imageMap.put("CmdProperties", getButtonImage(CmdProperties.class));
		imageMap.put("CmdOpenFile", getButtonImage(CmdOpenFile.class));
		imageMap.put("CmdNewDocument", getButtonImage(CmdNewDocument.class));
		imageMap.put("CmdSODuplicate", getButtonImage(CmdSODuplicate.class));
		imageMap.put("CmdSODelete", getButtonImage(CmdSODelete.class));
		imageMap.put("CmdSOAlign", getButtonImage(CmdSOAlign.class));
		imageMap.put("CmdShowGrid", getButtonImage(CmdShowGrid.class));
		imageMap.put("CmdShowHistory", getButtonImage(CmdShowHistory.class));
		imageMap.put("CmdJTextBox", getButtonImage(CmdJTextBox.class));
		imageMap.put("CmdWindow", getButtonImage(CmdWindow.class));
		imageMap.put("CmdButton", getButtonImage(CmdButton.class));
		imageMap.put("CmdImage", getButtonImage(CmdImage.class));
		imageMap.put("CmdLayer", getButtonImage(CmdLayer.class));
		imageMap.put("CmdUnLayer", getButtonImage(CmdUnLayer.class));
		imageMap.put("CmdPan", getButtonImage(CmdPan.class));
		imageMap.put("CmdCompactHistory", getButtonImage(CmdCompactHistory.class));
		imageMap.put("CmdClearHistory", getButtonImage(CmdClearHistory.class));

		commandMap = new AreaMap<>();

		perfInspector.classStaticInitEnd();

	}

	private static Image getButtonImage(Class<?> clazz) {
		return ImageTool.getResourceAsImage(clazz.getName()
				.substring(0, clazz.getName().lastIndexOf('.'))
				.replaceAll("\\.", "/")
				+ "/resources/"
				+ clazz.getSimpleName().substring("Cmd".length()) + ".png");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1567131106357475397L;

	private JFrame toolFrame;

	public ToolBox() {
		super();

		this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setBackground(Color.black);
		this.addMouseListener(new ToolboxMouseListener());
	}

	public ToolBox(JFrame frame) {
		super();

		if (frame == null) {
			toolFrame = new JFrame();
		}
		this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setBackground(Color.black);
		toolFrame.add(this);
		toolFrame.pack();
		toolFrame.setVisible(true);
		this.addMouseListener(new ToolboxMouseListener());
	}

	public Point buttonXY(int row, int column) {
		Point topLeft = new Point();
		topLeft.x = MARGIN + column * (BUTTON_SIZE + SPACING);
		topLeft.y = MARGIN + row * (BUTTON_SIZE + SPACING);
		return topLeft;
	}

	public void paintComponent(Graphics g) {
		int col = 0, row = 0;
		int buttonCount = 0;
		super.paintComponent(g); // call superclass's paintComponent
		g.setPaintMode();

		// Draw Line Button
		makeCommandButton(g, row, col, "L", CmdLine.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;
		// Draw Poly Lines Button
		
		makeCommandButton(g, row, col, "PL", CmdPolyLines.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;
		
		// Draw Circle Button
		makeCommandButton(g, row, col, "C", CmdCircle.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;

		// Draw Rectangle button
		makeCommandButton(g, row, col, "R", CmdConnectableRectangle.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;

		// Add Text button
		makeCommandButton(g, row, col, "Tx", CmdText.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;

		// Add Multi Line Text button
		makeCommandButton(g, row, col, "MTx", CmdMultiText.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;
		
		// Add Color Button
		makeCommandButton(g, row, col, "Cl", CmdColor.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;

		// Add Save Button
		makeCommandButton(g, row, col, "S", CmdSave.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;

		// Add Select Object Button
		makeCommandButton(g, row, col, "SO", CmdSelectObject.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;

		// Add Show Properties Panel
		makeCommandButton(g, row, col, "P", CmdProperties.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;

		// Add Open File
		makeCommandButton(g, row, col, "Ld", CmdOpenFile.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;
		
		// New Document
		makeCommandButton(g, row, col, "New", CmdNewDocument.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;

		// Add Load Command Set
		// Add Remove Command Set
		// Add MultiSelect
		// Add zIndex
		// Add Form Objects
		// Add 3D Objects
		// Add Bevel

		
		// Duplicate Selected
		makeCommandButton(g, row, col, "Dup", CmdSODuplicate.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;
		// Delete Selected
		makeCommandButton(g, row, col, "Del", CmdSODelete.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;
		// Move Selected Up
		makeCommandButton(g, row, col, "Up", CmdSOUp.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;
		// Move Selected Down
		makeCommandButton(g, row, col, "Down", CmdSODown.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;
		// Add Align Objects
		makeCommandButton(g, row, col, "Alg", CmdSOAlign.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;
		// Toggle Grid
		makeCommandButton(g, row, col, "Grd", CmdShowGrid.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;
		// Show History
		makeCommandButton(g, row, col, "Hst", CmdShowHistory.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;

		// JTextBox
		makeCommandButton(g, row, col, "JTB", CmdJTextBox.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;

		// CmdWindow
		makeCommandButton(g, row, col, "Wnd", CmdWindow.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;

		// CmdButton
		makeCommandButton(g, row, col, "Btn", CmdButton.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;

		// CmdImage
		makeCommandButton(g, row, col, "Img", CmdImage.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;

		// CmdLayer
		makeCommandButton(g, row, col, "Lyr", CmdLayer.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;

		// CmdUnLayer
		makeCommandButton(g, row, col, "UnL", CmdUnLayer.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;
		
		// CmdUnLayer
		makeCommandButton(g, row, col, "Rot", CmdRotate.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;
		
		// CmdPan
		makeCommandButton(g, row, col, "Pan", CmdPan.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;

		// CmdCompactHistory
		makeCommandButton(g, row, col, "Cmp", CmdCompactHistory.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;

		// CmdClearHistory
		makeCommandButton(g, row, col, "Clr", CmdClearHistory.class);
		row = ++buttonCount / COLUMNS;
		col = buttonCount % COLUMNS;

		if (!properties.isEmpty()) {
			String className = "";
			Class<?> clazz = null;
			try {
				for (int i = 1; (className = (String) properties
						.getProperty(Integer.toString(i))) != null; i++) {
					clazz = Class.forName(className);
					Method nameGetter = null;
					String buttonName = null;
					try {
						nameGetter = clazz.getDeclaredMethod("getButtonName");
					} catch (Throwable t) {
					}
					if (nameGetter != null)
						try {
							buttonName = (String) nameGetter.invoke(null);
						} catch (Throwable t) {
						}
					if (buttonName == null) {
						buttonName = clazz.getSimpleName().substring(
								"Cmd".length());
					}
					makeCommandButton(g, row, col, buttonName, clazz);
					row = ++buttonCount / COLUMNS;
					col = buttonCount % COLUMNS;

				}
			} catch (ClassNotFoundException e) {
				logger.warn("No " + className);
			}
		}
		setCol(col);
		setRow(row);
		setButtonCount(buttonCount);
	}

	private void makeCommandButton(Graphics g, int row, int column,
			String text, Class<?> clazz) {
		g.setColor(Color.black);
		int x = buttonXY(row, column).x;
		int y = buttonXY(row, column).y;
		Image icon = imageMap.get(clazz.getSimpleName());
		g.draw3DRect(x, y, BUTTON_SIZE, BUTTON_SIZE, true);
		g.setColor(Color.white);
		g.fill3DRect(x, y, BUTTON_SIZE, BUTTON_SIZE, true);
		g.setColor(Color.black);
		g.drawString(text, x + 15, y + 25);
		if (icon != null)
			g.drawImage(icon, x, y, null);

		addCommand(x, y, BUTTON_SIZE, BUTTON_SIZE, clazz);

	}

	public static void addCommand(int sequence, String className) {
		properties.setProperty(Integer.toString(sequence), className);
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getButtonCount() {
		return buttonCount;
	}

	public void setButtonCount(int buttonCount) {
		this.buttonCount = buttonCount;
	}

	public Dimension getDimensions() {
		int width = MARGIN + COLUMNS * (BUTTON_SIZE + SPACING);
		int height = MARGIN + (row + 1) * (BUTTON_SIZE + SPACING);
		return new Dimension(width,height);
	}

}
