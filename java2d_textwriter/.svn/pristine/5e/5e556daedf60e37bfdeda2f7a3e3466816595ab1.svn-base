package com.samisari.java2d.textwriter.commands.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STRawGroupDir;

import com.samisari.graphics.commands.ICmdJTextBox;
import com.samisari.graphics.commands.ICmdRectangle;
import com.samisari.graphics.commands.ICmdText;
import com.samisari.graphics.commands.ICmdWindow;
import com.samisari.java2d.textwriter.JavaCodeWriter;
import com.samisari.java2d.textwriter.commands.Indent;

public class Window implements JavaCodeWriter {
	private ICmdWindow cmd;
	String name = "window";
	STRawGroupDir stgd = new STRawGroupDir(
			"com/samisari/java2d/textwriter/commands/window", "UTF-8");

	public Window(ICmdWindow cmd, String name) {
		setCmd(cmd);
		setName(name);
	}

	public String getJavaImportDeclaration(Indent indent) throws Exception {
		ST st = stgd.getInstanceOf("importDecl");
		return st.render();
	}

	public String getJavaFieldDeclaration(Indent indent) throws Exception {
		ST st = stgd.getInstanceOf("fieldDecl");
		st.add("name", name);
		st.add("indent", indent);
		return st.render();
	}

	public class IntRenderer implements AttributeRenderer {
		@Override
		public String toString(Object arg0, String arg1, Locale arg2) {
			return ((Integer) arg0).toString();
		}
	}

	public String getJavaBuildMethodStatements(Indent indent) throws Exception {
		stgd.registerRenderer(Integer.class, new IntRenderer());
		ST st = stgd.getInstanceOf("initialization");
		st.add("name", name);
		st.add("window", cmd);
		st.add("indent", indent.toString());
		return st.render();
	}

	public String getWindowListenerStatements(Indent indent) throws Exception {
		if (cmd.getWindowListener() != null
				&& cmd.getWindowListener().length() > 0) {
			stgd.registerRenderer(Integer.class, new IntRenderer());
			ST st = stgd.getInstanceOf("actionListener");
			st.add("name", name);
			st.add("button", cmd);
			st.add("indent", indent);
			return st.render();
		}
		return "";
	}

	public ICmdWindow getCmd() {
		return cmd;
	}

	public void setCmd(ICmdWindow cmd) {
		this.cmd = cmd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return null;
	}

	public static void main(String[] args) {
		ICmdWindow wnd = new ICmdWindow() {

			@Override
			public int getX() {
				return 0;
			}

			@Override
			public int getY() {
				return 0;
			}

			@Override
			public int getWidth() {
				return 400;
			}

			@Override
			public int getHeight() {
				return 300;
			}

			@Override
			public Color getBorderColor() {
				return null;
			}

			@Override
			public Color getFillColor() {
				return Color.GRAY;
			}

			@Override
			public Rectangle getBounds() {
				return new Rectangle(0, 0, 300, 400);
			}

			@Override
			public Font getTitleFont() {
				return null;
			}

			@Override
			public String getTitleText() {
				return "Test Penceresi";
			}

			@Override
			public Color getTitleColor() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getWindowListener() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<ICmdRectangle> getComponents() {
				ICmdText label1 = new ICmdText() {

					@Override
					public Rectangle getBounds() {
						return new Rectangle(10, 10, 200, 20);
					}

					@Override
					public int getX() {
						return 10;
					}

					@Override
					public int getY() {
						return 210;
					}

					@Override
					public int getWidth() {
						return 10;
					}

					@Override
					public int getHeight() {
						return 30;
					}

					@Override
					public Color getFillColor() {
						return null;
					}

					@Override
					public Color getBorderColor() {
						return null;
					}

					@Override
					public String getText() {
						return "Kullanıcı Adı";
					}

					@Override
					public Font getFont() {
						return new Font("Tahoma", Font.BOLD, 14);
					}

					@Override
					public Color getColor() {
						return Color.BLACK;
					}
				};

				ICmdJTextBox textBox = new ICmdJTextBox() {

					@Override
					public Rectangle getBounds() {
						return new Rectangle(10, 10, 200, 20);
					}

					@Override
					public int getX() {
						return 10;
					}

					@Override
					public int getWidth() {
						return 200;
					}

					@Override
					public int getY() {
						return 10;
					}

					@Override
					public int getHeight() {
						return 20;
					}

					@Override
					public Color getFillColor() {
						return null;
					}

					@Override
					public Color getBorderColor() {
						return null;
					}

					@Override
					public String getName() {
						return "txtName";
					}

					@Override
					public String getMask() {
						return null;
					}

					@Override
					public int getMaxLength() {
						return 20;
					}

					@Override
					public String getChangeEventHandler() {
						return "";
					}
				};
				List<ICmdRectangle> list = new ArrayList<ICmdRectangle>();
				list.add(label1);
				list.add(textBox );
				return list;
			}

		};
		Indent indent = new Indent();
		try {
			Window window = new Window(wnd, "frame");
			System.out.println(window.getJavaImportDeclaration(indent));
			System.out.println(window.getJavaFieldDeclaration(indent));

//			List<JavaCodeWriter> codeWriterList = new ArrayList<JavaCodeWriter>();
//			for (ICmdRectangle comp : wnd.getComponents()) {
//				codeWriterList.add(CodeWriterFactory.getJavaCodeWriter(comp));
//			}
			System.out.println(window.getJavaBuildMethodStatements(indent));
			System.out.println(window.getCmd().getComponents());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
