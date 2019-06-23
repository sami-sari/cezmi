package com.samisari.graphics.toolbox;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
import com.samisari.common.util.ImageTool;
import com.samisari.debugtools.performance.PerformanceInspector;
import com.samisari.graphics.commands.CmdButton;
import com.samisari.graphics.commands.CmdClearHistory;
import com.samisari.graphics.commands.CmdColor;
import com.samisari.graphics.commands.CmdCompactHistory;
import com.samisari.graphics.commands.CmdJTextBox;
import com.samisari.graphics.commands.CmdNewDocument;
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
import com.samisari.guidesigner.CmdWindow;

public class ToolBoxSwing extends JScrollPane {
	private static final org.apache.log4j.Logger	logger				= Logger.getLogger(ToolBoxSwing.class);
	private static PerformanceInspector				perfInspector		= PerformanceInspector.get(ToolBoxSwing.class.getName());
	private static final long						serialVersionUID	= -1567131106357475397L;
	private static final Properties					properties			= new Properties();
	private static final HashMap<String, Image>		imageMap			= new HashMap<>();
	private static final HashMap<String, JButton>	buttonMap			= new HashMap<>();
	private static final List<JButton>				buttonList			= new ArrayList<>();

	static {
		perfInspector.classStaticInitStart();
		boolean existsPropertyFile = true;
		try {
			properties.load(ClassLoader.getSystemResourceAsStream("Commands.properties"));
		} catch (Exception e) {
			logger.warn("Commmands.properties dosyası bulunamadı", e);
			existsPropertyFile = false;
		}
		if (existsPropertyFile) {
			String className = null;
			for (int i = 1; (className = properties.getProperty(Integer.toString(i))) != null; i++) {
				Class<?> clazz;
				try {
					clazz = Class.forName(className);
					imageMap.put(clazz.getSimpleName(), getButtonImage(clazz));
				} catch (ClassNotFoundException e) {
					logger.error(String.format("Class %1$s blunamadı", className), e);
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

		perfInspector.classStaticInitEnd();
	}

	private int		buttonCount	= 0;
	private JFrame	toolFrame;
	private JPanel	content=new JPanel();

	public void addCommand(final String name, final Class<?> clazz) {
		final JButton btn = new JButton(name == null ? clazz.getSimpleName() : name);
		Image img = getButtonImage(clazz);
		if (img != null)
			btn.setIcon(new ImageIcon(getButtonImage(clazz)));
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					CommandManager.getDeaultInstance().setCommand((AbstractCommand) clazz.newInstance());
				} catch (InstantiationException | IllegalAccessException exc) {
					logger.error("Can not add command!", exc);
				}
			}
		});
		buttonList.add(btn );
		buttonMap.put(clazz.getName(), btn);
		content.add(btn, new GridBagConstraints(0,buttonCount, 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 10, 10));
		buttonCount++;

	}

	private static Image getButtonImage(Class<?> clazz) {
		return ImageTool.getResourceAsImage(
				clazz.getName().substring(0, clazz.getName().lastIndexOf('.')).replaceAll("\\.", "/") + "/resources/" + clazz.getSimpleName().substring("Cmd".length()) + ".png");
	}

	public ToolBoxSwing() {
		super();
		content.setLayout(new GridBagLayout());
		setViewportView(content);
		// Draw Line Button
		addCommand("L", CmdLine.class);
		// Draw Poly Lines Button
		addCommand("PL", CmdPolyLines.class);
		// Draw Circle Button
		addCommand("C", CmdCircle.class);
		// Draw Rectangle button
		addCommand("R", CmdConnectableRectangle.class);
		// Add Text button
		addCommand("Tx", CmdText.class);
		// Add Multi Line Text button
		addCommand("MTx", CmdMultiText.class);
		// Add Color Button
		addCommand("Cl", CmdColor.class);
		// Add Save Button
		addCommand("S", CmdSave.class);
		// Add Select Object Button
		addCommand("SO", CmdSelectObject.class);
		// Add Show Properties Panel
		addCommand("P", CmdProperties.class);
		// Add Open File
		addCommand("Ld", CmdOpenFile.class);
		// New Document
		addCommand("New", CmdNewDocument.class);
		// Add Load Command Set
		// Add Remove Command Set
		// Add MultiSelect
		// Add zIndex
		// Add Form Objects
		// Add 3D Objects
		// Add Bevel
		// Duplicate Selected
		addCommand("Dup", CmdSODuplicate.class);
		// Delete Selected
		addCommand("Del", CmdSODelete.class);
		// Move Selected Up
		addCommand("Up", CmdSOUp.class);
		// Move Selected Down
		addCommand("Down", CmdSODown.class);
		// Add Align Objects
		addCommand("Alg", CmdSOAlign.class);
		// Toggle Grid
		addCommand("Grd", CmdShowGrid.class);
		// Show History
		addCommand("Hst", CmdShowHistory.class);
		// JTextBox
		addCommand("JTB", CmdJTextBox.class);
		// CmdWindow
		addCommand("Wnd", CmdWindow.class);
		// CmdButton
		addCommand("Btn", CmdButton.class);
		// CmdImage
		addCommand("Img", CmdImage.class);
		// CmdLayer
		addCommand("Lyr", CmdLayer.class);
		// CmdUnLayer
		addCommand("UnL", CmdUnLayer.class);
		// CmdUnLayer
		addCommand("Rot", CmdRotate.class);
		// CmdPan
		addCommand("Pan", CmdPan.class);
		// CmdCompactHistory
		addCommand("Cmp", CmdCompactHistory.class);
		// CmdClearHistory
		addCommand("Clr", CmdClearHistory.class);
		if (!properties.isEmpty()) {
			String className = "";
			Class<?> clazz = null;
			try {
				for (int i = 1; (className = properties.getProperty(Integer.toString(i))) != null; i++) {
					clazz = Class.forName(className);
					Method nameGetter = null;
					String buttonName = null;
					try {
						nameGetter = clazz.getDeclaredMethod("getButtonName");
					} catch (Exception t) {
						// ignore
					}
					if (nameGetter != null)
						try {
							buttonName = (String) nameGetter.invoke(null);
						} catch (Exception t) {
							// ignore
						}
					if (buttonName == null) {
						buttonName = clazz.getSimpleName().substring("Cmd".length());
					}
					addCommand(buttonName, clazz);
				}
			} catch (ClassNotFoundException e) {
				logger.warn("No " + className);
			}
		}
		setBackground(Color.black);
	}

	public ToolBoxSwing(JFrame frame) {
		super();

		if (frame == null) {
			toolFrame = new JFrame();
		}
		setBackground(Color.black);
		toolFrame.add(this);
		toolFrame.pack();
		toolFrame.setVisible(true);
	}

	public static void addCommand(int sequence, String className) {
		properties.setProperty(Integer.toString(sequence), className);
	}

	public int getButtonCount() {
		return buttonCount;
	}

	public void setButtonCount(int buttonCount) {
		this.buttonCount = buttonCount;
	}

}
