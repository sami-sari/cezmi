package com.samisari.common.util.bean;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.xml.stream.XMLOutputFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.samisari.common.util.ParserUtils;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import net.n3.nanoxml.IXMLParser;
import net.n3.nanoxml.StdXMLBuilder;
import net.n3.nanoxml.StdXMLReader;
import net.n3.nanoxml.XMLElement;
import net.n3.nanoxml.XMLParserFactory;
import net.n3.nanoxml.XMLWriter;

public class BeanUtils {
	private static final XMLOutputFactory	xmlOutputFactory	= XMLOutputFactory.newFactory();
	private static Log						logger				= LogFactory.getLog(BeanUtils.class);

	public static Class getPropertyType(Object bean, String propertyName) throws IntrospectionException {
		BeanInfo info = Introspector.getBeanInfo(bean.getClass());
		PropertyDescriptor[] propertyDescriptors = info.getPropertyDescriptors();
		for (PropertyDescriptor pd : propertyDescriptors) {
			if (pd.getName().equals(propertyName))
				return pd.getPropertyType();
		}
		return null;
	}

	private static String setter2FieldName(String name) {
		if (name.startsWith("set")) {
			return (name.substring(3, 4).toLowerCase(Locale.ENGLISH) + name.substring(4));
		}
		return null;
	}

	private static Field getField(Object bean, Method setterMethod) {
		Field f = null;
		String name = setter2FieldName(setterMethod.getName());
		Class<?> clazz = bean.getClass();
		try {
			f = clazz.getDeclaredField(name);
		} catch (NoSuchFieldException e) {

			while (clazz.getSuperclass() != null && f == null) {
				clazz = clazz.getSuperclass();
				try {
					f = clazz.getDeclaredField(name);
				} catch (Exception e1) {
					//e1.printStackTrace();
				}
			}
		} catch (SecurityException e) {
			//e.printStackTrace();
		}
		//f.setAccessible(true);
		return f;
	}

	private static boolean isTransient(Object bean, Method setterMethod) {
		Field field = getField(bean, setterMethod);
		if (field == null)
			return true;
		return Modifier.isTransient((field.getModifiers()));
	}

	public static void string2Value(final Object bean, final Class<?> type, final Method setterMethod, final String propertyValue) throws Exception {
		try {
			if (!isTransient(bean, setterMethod)) {
				if (type.equals(String.class)) {
					if (setterMethod != null)
						setterMethod.invoke(bean, propertyValue);
				} else if (propertyValue == null || propertyValue.isEmpty()) {
					if (setterMethod != null) {
						setterMethod.invoke(bean, (Object) null);
					}
				} else if (type.equals(Integer.class) || type.equals(int.class)) {
					setterMethod.invoke(bean, new Integer(new Integer(propertyValue)));
				} else if (type.equals(Float.class) || type.equals(float.class)) {
					setterMethod.invoke(bean, new Float(propertyValue));
				} else if (type.equals(Double.class) || type.equals(double.class)) {
					setterMethod.invoke(bean, new Double(propertyValue));
				} else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
					setterMethod.invoke(bean, new Boolean(propertyValue));
				} else if (type.equals(Font.class)) {
					setterMethod.invoke(bean, string2Font(propertyValue));
				} else if (type.equals(Color.class)) {
					setterMethod.invoke(bean, string2Color(propertyValue));
				} else if (type.equals(Rectangle.class)) {
					setterMethod.invoke(bean, string2Rectangle(propertyValue));
				} else if (type.equals(BufferedImage.class)) {
					setterMethod.invoke(bean, string2BufferedImage(propertyValue));
				} else if (type.equals(List.class)) {
					setterMethod.invoke(bean, string2List(propertyValue));
				} else if (type.equals(Point.class)) {
					setterMethod.invoke(bean, string2Point(propertyValue));
				} else if (type.equals(AffineTransform.class)) {
					setterMethod.invoke(bean, string2AffineTransform(propertyValue));
				} else {
					try {
						StringReader reader = new StringReader(propertyValue);
						Method deSerializeMethod = type.getMethod("deserialise", Reader.class);
						if (deSerializeMethod != null) {
							deSerializeMethod.invoke(bean, reader);
						}
					} catch (Exception e) {
						throw new Exception(type.getName() + " could not be deserialised " + bean.getClass().getName() + " " + setterMethod.getName() + " property.");
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public static void xml2Value(final Object bean, final Class<?> type, final Method setterMethod, final Element elm) throws Exception {
		try {
			if (setterMethod == null || !isTransient(bean, setterMethod)) {
				if (type.equals(List.class)) {
					setterMethod.invoke(bean, ListHandler.toObject(elm));
				} else {
					try {
						Method deSerializeMethod = type.getMethod("deserialise", Element.class);
						if (deSerializeMethod != null) {
							deSerializeMethod.invoke(bean, elm);
						}
					} catch (Exception e) {
						throw new Exception(type.getName() + " could not be deserialised " + bean.getClass().getName() + " " + setterMethod.getName() + " property.");
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public static List<Object> string2List(String propertyValue) throws Exception {
		List<Object> list = new ArrayList<>();
		try {
			tryParser(list, propertyValue);
			return list;
		} catch (Exception e) {

		}
		StringTokenizer st = new StringTokenizer(propertyValue, ";");
		while (st.hasMoreTokens()) {
			String tmp = st.nextToken();
			Object val = parse(tmp);
			list.add(val);
		}
		return list;
	}

	private static void tryParser(List<Object> list, String input) throws Exception {
		StringReader in = new StringReader(input);
		int c;
		String text = "";
		try {
			while ((c = in.read()) >= 0) {
				text = text + (char) c;
				if (text.endsWith(">")) {
					String className = text.substring(text.indexOf("<") + 1, text.indexOf(">"));
					Object command = null;
					try {
						command = Class.forName(className).newInstance();
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (command != null) {
						try {
							try {
								Method deSerializeMethod = command.getClass().getMethod("deserialise", Reader.class);
								if (deSerializeMethod != null) {
									deSerializeMethod.invoke(command, in);
								}
							} catch (Exception e) {
								throw new Exception(command.getClass().getName() + " could not be deserialised " + command.getClass().getName());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						list.add(command);
						text = "";
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private static final HashMap<String, Method>	parserMap		= new HashMap<String, Method>();
	private static final String						SIMPLE_TYPES	= "boolean,int,float,double,byte,Integer,Boolean,Long,Double,String,Point,Rectangle,Font,Color,BufferedImage,AffineTransform,Status";

	static {
		try {
			parserMap.put("java.awt.Point", BeanUtils.class.getMethod("string2Point", String.class));
			parserMap.put("java.awt.Color", BeanUtils.class.getMethod("string2Color", String.class));
			parserMap.put("java.awt.Font", BeanUtils.class.getMethod("string2Font", String.class));
			parserMap.put("java.util.List", BeanUtils.class.getMethod("string2List", String.class));
			parserMap.put("java.awt.Rectangle", BeanUtils.class.getMethod("string2Rectangle", String.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Object parse(String str) {
		int pos = -1;
		try {
			if ((pos = str.indexOf("[")) > -1) {
				return parserMap.get(str.substring(0, pos)).invoke(null, str);
			} else {
				return str;
			}
		} catch (Throwable t) {
			// ignore
		}
		return str;
	}

	public static String value2String(Object propertyValue) {
		long start = System.currentTimeMillis();
		try {
			if (propertyValue == null) {
				return "";
			}
			if (propertyValue instanceof String) {
				return (String) propertyValue;
			}
			if ((propertyValue instanceof Integer) || (propertyValue instanceof Float) || (propertyValue instanceof Double) || (propertyValue instanceof Boolean)) {
				return propertyValue.toString();
			}
			if ((propertyValue instanceof Font)) {
				return font2String((Font) propertyValue);
			}
			if ((propertyValue instanceof Color)) {
				return color2String((Color) propertyValue);
			}
			if ((propertyValue instanceof Rectangle)) {
				return rectangle2String((Rectangle) propertyValue);
			}
			if ((propertyValue instanceof BufferedImage)) {
				return bufferedImage2String((BufferedImage) propertyValue);
			}
			if (propertyValue instanceof List) {
				return list2String((List<?>) propertyValue);
			}
			if (propertyValue instanceof Point) {
				return point2String((Point) propertyValue);
			}
			if (propertyValue instanceof AffineTransform) {
				return affineTransform2String((AffineTransform) propertyValue);
			}
			if (propertyValue.getClass().isEnum()) {
				return propertyValue.toString();
			}

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			serialiseObject(propertyValue, out);
			return new String(out.toByteArray(), Charset.forName("UTF-8"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			long duration = System.currentTimeMillis() - start;
			logger.debug(propertyValue == null ? "NULL" : propertyValue.getClass().getName() + " " + (duration > 10 ? "LONG" : "") + " " + duration);
		}
	}

	private static String affineTransform2String(AffineTransform propertyValue) {
		StringBuffer sb = new StringBuffer();
		double[] matrix = new double[6];
		propertyValue.getMatrix(matrix);
		sb.append(propertyValue.getClass().getName());

		sb.append("[");
		for (int i = 0; i < 5; i++) {
			sb.append(matrix[i]);
			sb.append(";");
		}
		sb.append(matrix[5]);
		sb.append("]");
		return sb.toString();
	}

	private static AffineTransform string2AffineTransform(String propertyValue) {
		double[] matrix = new double[6];
		Pattern pattern = Pattern.compile("java\\.awt\\.geom\\.AffineTransform\\[([^\\]]*)\\]");
		Matcher matcher = pattern.matcher(propertyValue);
		if (matcher.matches()) {
			StringTokenizer st = new StringTokenizer(matcher.group(1), ";");
			int i = 0;
			while (st.hasMoreTokens()) {
				String tmp = st.nextToken();
				double val = Double.parseDouble(tmp);
				matrix[i++] = val;
			}
			return new AffineTransform(matrix);
		} else {
			pattern = Pattern.compile("R \\(([^,]*,[^,]*,[^\\)]*)\\)");
			matcher = pattern.matcher(propertyValue);
			if (matcher.matches()) {
				StringTokenizer st = new StringTokenizer(matcher.group(1), ",");
				int i = 0;
				double angle=0.0;
				int pivotX=0,pivotY=0;
				if (st.hasMoreTokens()) {
					String txt = st.nextToken();
					angle = Double.parseDouble(txt);
					if (st.hasMoreTokens()) {
						txt = st.nextToken();
						pivotX = Integer.parseInt(txt);
						if (st.hasMoreTokens()) {
							txt = st.nextToken();
							pivotY = Integer.parseInt(txt);
						}
					}
				}
				return AffineTransform.getRotateInstance(angle, (double) pivotX, (double) pivotY);
			}
		}

		return null;

	}

	private static String point2String(Point propertyValue) {
		StringBuffer sb = new StringBuffer();
		sb.append(propertyValue.getClass().getName());
		sb.append("[");
		sb.append(propertyValue.x);
		sb.append(",");
		sb.append(propertyValue.y);
		sb.append("]");

		return sb.toString();
	}

	public static Point string2Point(String value) {
		Pattern pattern = Pattern.compile("java\\.awt\\.Point\\[(\\d+),(\\d+)\\]");
		Matcher matcher = pattern.matcher(value);
		if (matcher.matches()) {
			return new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
		}

		return null;
	}

	public static String list2String(List<?> propertyValue) {
		return ListHandler.toString(propertyValue);
	}

	public static void serialiseObject(Object o, OutputStream out) throws Exception {
		net.n3.nanoxml.XMLWriter xmlWriter = new XMLWriter(out);
		BeanInfo classInfo = Introspector.getBeanInfo(o.getClass(), Object.class, Introspector.USE_ALL_BEANINFO);
		XMLElement root = new XMLElement(o.getClass().getName());
		PropertyDescriptor[] propertyInfoArr = classInfo.getPropertyDescriptors();
		for (PropertyDescriptor propertyInfo : propertyInfoArr) {
			String name = propertyInfo.getName();
			Object value = propertyInfo.getReadMethod().invoke(o);
			XMLElement child = new XMLElement(name);
			String valueStr = "";
			try {
				logger.debug(o.getClass().getSimpleName() + ":" + name);
				valueStr = BeanUtils.value2String(value);
				if (value != null) {
					if (SIMPLE_TYPES.indexOf(propertyInfo.getPropertyType().getSimpleName()) > -1) {
						child.setContent(valueStr);
					} else {
						IXMLParser parser = XMLParserFactory.createDefaultXMLParser();
						parser.setReader(StdXMLReader.stringReader(valueStr));
						parser.setBuilder(new StdXMLBuilder());
						Object result = parser.parse();
						logger.debug(valueStr);
						child.addChild((XMLElement) result);
					}
				}
			} catch (Throwable t) {
				try {
					Method serializeMethod = value.getClass().getMethod("serialise", new Class<?>[] { OutputStream.class });
					if ((serializeMethod != null)) {
						serializeMethod.invoke(value, out);
					}
				} catch (Throwable t1) {
					logger.warn("Error while serializing " + propertyInfo.getName() + " :" + valueStr + "\n" + t1.getMessage());
				}
			}
			root.addChild(child);
		}
		xmlWriter.write(root);
	}

	public static HashMap<String, Method> getClassMethods(Object o) {
		Method[] methodArr = o.getClass().getMethods();
		HashMap<String, Method> map = new HashMap<String, Method>();
		for (Method method : methodArr) {
			if (method.getName().startsWith("set")) {
				String propertyName = method.getName().substring(3, 4).toLowerCase(Locale.ENGLISH) + method.getName().substring(4);
				if (!map.containsKey(propertyName))
					map.put(propertyName, method);
				else {
					try {
						String getterName = "get" + propertyName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propertyName.substring(1);
						Method getter = o.getClass().getMethod(getterName, null);
						if (getter.getReturnType().equals(method.getParameters()[0].getType()))
							map.put(propertyName, method);

					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return map;
	}

	public static void deserialise(Object o, Element elm) {
		try {
			logger.debug("DESERAILISING " + o.getClass().getSimpleName());
			NodeList nodeList = elm.getChildNodes();
			HashMap<String, Method> setterMethods = getClassMethods(o);
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element propertyElm = (Element) node;
					String name = propertyElm.getNodeName();
					if (setterMethods.containsKey(name)) {
						Method setter = setterMethods.get(name);
						Class<?> propertyType = setter.getParameterTypes()[0];
						String value = "";
						if (propertyType.isAssignableFrom(java.util.List.class)) {
							logger.debug("LIST");
							List<Object> propertyValue = new ArrayList<Object>();
							xml2Value(o, propertyType, setter, propertyElm);

						} else if (propertyElm.hasChildNodes() && propertyElm.getFirstChild().hasChildNodes()) {
							String tag = propertyElm.getTagName();
							if (propertyType.equals(List.class))
								try {
									xml2Value(o, propertyType, setter, (Element) propertyElm);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						} else {
							value = propertyElm.getTextContent();
							try {
								string2Value(o, propertyType, setter, value);
							} catch (Exception e) {
								logger.error("ERROR setting property " + name, e);
							}
						}
					}
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public static String bufferedImage2String(BufferedImage image) {
		String result = "";
		ByteArrayOutputStream imageStream = new ByteArrayOutputStream(1024);
		try {
			ImageIO.write(image, "png", imageStream);
			return new String(Base64.encode(imageStream.toByteArray()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Object string2BufferedImage(String value) {
		byte[] imageBytes = null;
		try {
			imageBytes = Base64.decode(value);
		} catch (Base64DecodingException e1) {
			e1.printStackTrace();
		}
		ByteArrayInputStream imageStream = new ByteArrayInputStream(imageBytes);
		try {
			return ImageIO.read(imageStream);
		} catch (Exception e) {
			logger.error("Error", e);
		} finally {
			try {
				// imageStream.close();
			} catch (Exception e) {

			}
		}
		return null;
	}

	public static String rectangle2String(Rectangle value) {
		return "java.awt.Rectangle[x=" + value.x + ",y=" + value.y + ",width=" + value.width + ",height=" + value.height + "]";
	}

	public static Object string2Rectangle(String value) {
		Pattern pattern = Pattern.compile("java\\.awt\\.Rectangle\\[x=(\\d+),y=(\\d+),width=(\\d+),height=(\\d+)\\]");
		Matcher matcher = pattern.matcher(value);
		if (matcher.matches()) {
			return new Rectangle(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
		}

		return null;
	}

	public static String color2String(Color value) {
		return "java.awt.Color[r=" + value.getRed() + ",g=" + value.getGreen() + ",b=" + value.getBlue() + "]";
	}

	public static Color string2Color(String value) {
		Pattern pattern = Pattern.compile("java\\.awt\\.Color\\[r=(\\d+),g=(\\d+),b=(\\d+)\\]");
		Matcher matcher = pattern.matcher(value);
		if (matcher.matches()) {
			return new Color(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
		} else {

			if (value.startsWith("ColorRGB(")) {
				int[] start = { 0 };
				int r = Integer.parseInt(ParserUtils.getTextBetween(value, "(", ",", start));
				int g = Integer.parseInt(ParserUtils.getTextBetween(value, "", ",", start));
				int b = Integer.parseInt(ParserUtils.getTextBetween(value, "", ")", start));
				return new Color(r, g, b);
			} else if (value.startsWith("java.awt.Color[r=")) {
				int[] start = { 0 };
				int r = Integer.parseInt(ParserUtils.getTextBetween(value, "r=", ",", start));
				int g = Integer.parseInt(ParserUtils.getTextBetween(value, "g=", ",", start));
				int b = Integer.parseInt(ParserUtils.getTextBetween(value, "b=", "]", start));
				return new Color(r, g, b);
			} else {
				if (value.equalsIgnoreCase("red"))
						return Color.RED;
				else if (value.equalsIgnoreCase("green"))
					return Color.GREEN;
				else if (value.equalsIgnoreCase("blue"))
					return Color.BLUE;
				else if (value.equalsIgnoreCase("black"))
					return Color.BLACK;
				else if (value.equalsIgnoreCase("white"))
					return Color.WHITE;
				else if (value.equalsIgnoreCase("yellow"))
					return Color.YELLOW;
				else if (value.equalsIgnoreCase("orange"))
					return Color.ORANGE;
				else if (value.equalsIgnoreCase("cyan"))
					return Color.CYAN;
				else if (value.equalsIgnoreCase("gray"))
					return Color.GRAY;
				else if (value.equalsIgnoreCase("lightgray"))
					return Color.LIGHT_GRAY;
				else if (value.equalsIgnoreCase("darkgray"))
					return Color.DARK_GRAY;
				else if (value.equalsIgnoreCase("magenta"))
					return Color.MAGENTA;
				else if (value.equalsIgnoreCase("pink"))
					return Color.PINK;
			}
		}

		return null;
	}

	public static String font2String(Font value) {
		return "java.awt.Font[family=" + value.getFamily() + ",name=" + value.getName() + ",style="
				+ (value.getStyle() == Font.BOLD ? "bold"
						: (value.getStyle() == Font.PLAIN ? "plain"
								: (value.getStyle() == Font.ITALIC ? "italic" : (value.getStyle() == (Font.BOLD & Font.ITALIC) ? "bolditalic" : ""))))
				+ ",size=" + value.getSize() + "]";
	}

	public static Font string2Font(String value) {
		Pattern pattern = Pattern.compile("java\\.awt\\.Font\\[family=([^,]+),name=([^,]+),style=([^,]+),size=(\\d+)]");
		Matcher matcher = pattern.matcher(value);
		if (matcher.matches()) {
			return new Font(matcher.group(2),
					("bold".equals(matcher.group(3)) ? Font.BOLD
							: ("plain".equals(matcher.group(3)) ? Font.PLAIN
									: ("italic".equals(matcher.group(3)) ? Font.ITALIC : ("bolditalic".equals(matcher.group(3)) ? Font.BOLD & Font.ITALIC : null)))),
					Integer.parseInt(matcher.group(4)));
		} else {
			if (value.startsWith("Font(")) {
				int[] start = { 0 };
				String familly = ParserUtils.getTextBetween(value, "(", ",", start);
				int style = Integer.parseInt(ParserUtils.getTextBetween(value, "", ",", start));
				int size = Integer.parseInt(ParserUtils.getTextBetween(value, "", ")", start));
				return new Font(familly, style, size);
			}
		}

		return null;
	}

	public static Method getGetter(String propertyName, Class<?> bean) {
		StringBuilder methodName = new StringBuilder();
		String camelCase = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
		methodName.append("get");
		methodName.append(camelCase);
		Method method = null;
		try {
			method = bean.getMethod(methodName.toString(), new Class[0]);
		} catch (NoSuchMethodException | SecurityException e) {
			methodName.delete(0, 3);
			methodName.append("is", 0, 2);
			try {
				method = bean.getMethod(methodName.toString(), new Class[0]);
			} catch (NoSuchMethodException | SecurityException e1) {
			}
		}

		return method;
	}

}
