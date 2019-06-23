package net.n3.nanoxml;

public class XMLParserFactory
{
  public static final String DEFAULT_CLASS = "net.n3.nanoxml.StdXMLParser";
  public static final String CLASS_KEY = "net.n3.nanoxml.XMLParser";
  
  public static IXMLParser createDefaultXMLParser()
    throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    String str = System.getProperty("net.n3.nanoxml.XMLParser", "net.n3.nanoxml.StdXMLParser");
    return createXMLParser(str, new StdXMLBuilder());
  }
  
  public static IXMLParser createDefaultXMLParser(IXMLBuilder paramIXMLBuilder)
    throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    String str = System.getProperty("net.n3.nanoxml.XMLParser", "net.n3.nanoxml.StdXMLParser");
    return createXMLParser(str, paramIXMLBuilder);
  }
  
  public static IXMLParser createXMLParser(String paramString, IXMLBuilder paramIXMLBuilder)
    throws ClassNotFoundException, InstantiationException, IllegalAccessException
  {
    Class localClass = Class.forName(paramString);
    IXMLParser localIXMLParser = (IXMLParser)localClass.newInstance();
    localIXMLParser.setBuilder(paramIXMLBuilder);
    localIXMLParser.setValidator(new NonValidator());
    return localIXMLParser;
  }
}


/* Location:              C:\Users\sam\Downloads\nanoxml-2.2.3.jar!\net\n3\nanoxml\XMLParserFactory.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */