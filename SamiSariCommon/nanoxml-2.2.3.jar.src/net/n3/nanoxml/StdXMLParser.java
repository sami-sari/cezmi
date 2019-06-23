package net.n3.nanoxml;

import java.io.Reader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

public class StdXMLParser
  implements IXMLParser
{
  private IXMLBuilder builder = null;
  private IXMLReader reader = null;
  private IXMLEntityResolver entityResolver = new XMLEntityResolver();
  private IXMLValidator validator = null;
  
  protected void finalize()
    throws Throwable
  {
    this.builder = null;
    this.reader = null;
    this.entityResolver = null;
    this.validator = null;
    super.finalize();
  }
  
  public void setBuilder(IXMLBuilder paramIXMLBuilder)
  {
    this.builder = paramIXMLBuilder;
  }
  
  public IXMLBuilder getBuilder()
  {
    return this.builder;
  }
  
  public void setValidator(IXMLValidator paramIXMLValidator)
  {
    this.validator = paramIXMLValidator;
  }
  
  public IXMLValidator getValidator()
  {
    return this.validator;
  }
  
  public void setResolver(IXMLEntityResolver paramIXMLEntityResolver)
  {
    this.entityResolver = paramIXMLEntityResolver;
  }
  
  public IXMLEntityResolver getResolver()
  {
    return this.entityResolver;
  }
  
  public void setReader(IXMLReader paramIXMLReader)
  {
    this.reader = paramIXMLReader;
  }
  
  public IXMLReader getReader()
  {
    return this.reader;
  }
  
  public Object parse()
    throws XMLException
  {
    try
    {
      this.builder.startBuilding(this.reader.getSystemID(), this.reader.getLineNr());
      scanData();
      return this.builder.getResult();
    }
    catch (XMLException localXMLException)
    {
      throw localXMLException;
    }
    catch (Exception localException)
    {
      throw new XMLException(localException);
    }
  }
  
  protected void scanData()
    throws Exception
  {
    while ((!this.reader.atEOF()) && (this.builder.getResult() == null))
    {
      String str = XMLUtil.read(this.reader, '&');
      char c = str.charAt(0);
      if (c == '&') {
        XMLUtil.processEntity(str, this.reader, this.entityResolver);
      } else {
        switch (c)
        {
        case '<': 
          scanSomeTag(false, null, new Properties());
          break;
        case '\t': 
        case '\n': 
        case '\r': 
        case ' ': 
          break;
        default: 
          XMLUtil.errorInvalidInput(this.reader.getSystemID(), this.reader.getLineNr(), "`" + c + "' (0x" + Integer.toHexString(c) + ')');
        }
      }
    }
  }
  
  protected void scanSomeTag(boolean paramBoolean, String paramString, Properties paramProperties)
    throws Exception
  {
    String str = XMLUtil.read(this.reader, '&');
    char c = str.charAt(0);
    if (c == '&') {
      XMLUtil.errorUnexpectedEntity(this.reader.getSystemID(), this.reader.getLineNr(), str);
    }
    switch (c)
    {
    case '?': 
      processPI();
      break;
    case '!': 
      processSpecialTag(paramBoolean);
      break;
    default: 
      this.reader.unread(c);
      processElement(paramString, paramProperties);
    }
  }
  
  protected void processPI()
    throws Exception
  {
    XMLUtil.skipWhitespace(this.reader, null);
    String str = XMLUtil.scanIdentifier(this.reader);
    XMLUtil.skipWhitespace(this.reader, null);
    PIReader localPIReader = new PIReader(this.reader);
    if (!str.equalsIgnoreCase("xml")) {
      this.builder.newProcessingInstruction(str, localPIReader);
    }
    localPIReader.close();
  }
  
  protected void processSpecialTag(boolean paramBoolean)
    throws Exception
  {
    String str = XMLUtil.read(this.reader, '&');
    int i = str.charAt(0);
    if (i == 38) {
      XMLUtil.errorUnexpectedEntity(this.reader.getSystemID(), this.reader.getLineNr(), str);
    }
    switch (i)
    {
    case 91: 
      if (paramBoolean) {
        processCDATA();
      } else {
        XMLUtil.errorUnexpectedCDATA(this.reader.getSystemID(), this.reader.getLineNr());
      }
      return;
    case 68: 
      processDocType();
      return;
    case 45: 
      XMLUtil.skipComment(this.reader);
      return;
    }
  }
  
  protected void processCDATA()
    throws Exception
  {
    if (!XMLUtil.checkLiteral(this.reader, "CDATA[")) {
      XMLUtil.errorExpectedInput(this.reader.getSystemID(), this.reader.getLineNr(), "<![[CDATA[");
    }
    this.validator.PCDataAdded(this.reader.getSystemID(), this.reader.getLineNr());
    CDATAReader localCDATAReader = new CDATAReader(this.reader);
    this.builder.addPCData(localCDATAReader, this.reader.getSystemID(), this.reader.getLineNr());
    localCDATAReader.close();
  }
  
  protected void processDocType()
    throws Exception
  {
    if (!XMLUtil.checkLiteral(this.reader, "OCTYPE"))
    {
      XMLUtil.errorExpectedInput(this.reader.getSystemID(), this.reader.getLineNr(), "<!DOCTYPE");
      return;
    }
    XMLUtil.skipWhitespace(this.reader, null);
    String str1 = null;
    StringBuffer localStringBuffer = new StringBuffer();
    String str2 = XMLUtil.scanIdentifier(this.reader);
    XMLUtil.skipWhitespace(this.reader, null);
    int i = this.reader.read();
    if (i == 80)
    {
      str1 = XMLUtil.scanPublicID(localStringBuffer, this.reader);
      XMLUtil.skipWhitespace(this.reader, null);
      i = this.reader.read();
    }
    else if (i == 83)
    {
      str1 = XMLUtil.scanSystemID(this.reader);
      XMLUtil.skipWhitespace(this.reader, null);
      i = this.reader.read();
    }
    if (i == 91)
    {
      this.validator.parseDTD(localStringBuffer.toString(), this.reader, this.entityResolver, false);
      XMLUtil.skipWhitespace(this.reader, null);
      i = this.reader.read();
    }
    if (i != 62) {
      XMLUtil.errorExpectedInput(this.reader.getSystemID(), this.reader.getLineNr(), "`>'");
    }
    if (str1 != null)
    {
      Reader localReader = this.reader.openStream(localStringBuffer.toString(), str1);
      this.reader.startNewStream(localReader);
      this.reader.setSystemID(str1);
      this.reader.setPublicID(localStringBuffer.toString());
      this.validator.parseDTD(localStringBuffer.toString(), this.reader, this.entityResolver, true);
    }
  }
  
  protected void processElement(String paramString, Properties paramProperties)
    throws Exception
  {
    String str1 = XMLUtil.scanIdentifier(this.reader);
    String str2 = str1;
    XMLUtil.skipWhitespace(this.reader, null);
    String str3 = null;
    int i = str2.indexOf(':');
    if (i > 0)
    {
      str3 = str2.substring(0, i);
      str2 = str2.substring(i + 1);
    }
    Vector localVector1 = new Vector();
    Vector localVector2 = new Vector();
    Vector localVector3 = new Vector();
    this.validator.elementStarted(str1, this.reader.getSystemID(), this.reader.getLineNr());
    char c;
    for (;;)
    {
      c = this.reader.read();
      if ((c == '/') || (c == '>')) {
        break;
      }
      this.reader.unread(c);
      processAttribute(localVector1, localVector2, localVector3);
      XMLUtil.skipWhitespace(this.reader, null);
    }
    Properties localProperties = new Properties();
    this.validator.elementAttributesProcessed(str1, localProperties, this.reader.getSystemID(), this.reader.getLineNr());
    Enumeration localEnumeration = localProperties.keys();
    String str5;
    while (localEnumeration.hasMoreElements())
    {
      String str4 = (String)localEnumeration.nextElement();
      str5 = localProperties.getProperty(str4);
      localVector1.addElement(str4);
      localVector2.addElement(str5);
      localVector3.addElement("CDATA");
    }
    String str6;
    Object localObject1;
    for (int j = 0; j < localVector1.size(); j++)
    {
      str5 = (String)localVector1.elementAt(j);
      localObject1 = (String)localVector2.elementAt(j);
      str6 = (String)localVector3.elementAt(j);
      if (str5.equals("xmlns")) {
        paramString = (String)localObject1;
      } else if (str5.startsWith("xmlns:")) {
        paramProperties.put(str5.substring(6), localObject1);
      }
    }
    if (str3 == null) {
      this.builder.startElement(str2, str3, paramString, this.reader.getSystemID(), this.reader.getLineNr());
    } else {
      this.builder.startElement(str2, str3, paramProperties.getProperty(str3), this.reader.getSystemID(), this.reader.getLineNr());
    }
    Object localObject2;
    for (int k = 0; k < localVector1.size(); k++)
    {
      localObject1 = (String)localVector1.elementAt(k);
      if (!((String)localObject1).startsWith("xmlns"))
      {
        str6 = (String)localVector2.elementAt(k);
        localObject2 = (String)localVector3.elementAt(k);
        i = ((String)localObject1).indexOf(':');
        if (i > 0)
        {
          String str7 = ((String)localObject1).substring(0, i);
          localObject1 = ((String)localObject1).substring(i + 1);
          this.builder.addAttribute((String)localObject1, str7, paramProperties.getProperty(str7), str6, (String)localObject2);
        }
        else
        {
          this.builder.addAttribute((String)localObject1, null, null, str6, (String)localObject2);
        }
      }
    }
    if (str3 == null) {
      this.builder.elementAttributesProcessed(str2, str3, paramString);
    } else {
      this.builder.elementAttributesProcessed(str2, str3, paramProperties.getProperty(str3));
    }
    if (c == '/')
    {
      if (this.reader.read() != '>') {
        XMLUtil.errorExpectedInput(this.reader.getSystemID(), this.reader.getLineNr(), "`>'");
      }
      this.validator.elementEnded(str2, this.reader.getSystemID(), this.reader.getLineNr());
      if (str3 == null) {
        this.builder.endElement(str2, str3, paramString);
      } else {
        this.builder.endElement(str2, str3, paramProperties.getProperty(str3));
      }
      return;
    }
    localObject1 = new StringBuffer(16);
    for (;;)
    {
      ((StringBuffer)localObject1).setLength(0);
      for (;;)
      {
        XMLUtil.skipWhitespace(this.reader, (StringBuffer)localObject1);
        str6 = XMLUtil.read(this.reader, '&');
        if ((str6.charAt(0) != '&') || (str6.charAt(1) == '#')) {
          break;
        }
        XMLUtil.processEntity(str6, this.reader, this.entityResolver);
      }
      if (str6.charAt(0) == '<')
      {
        str6 = XMLUtil.read(this.reader, '\000');
        if (str6.charAt(0) == '/')
        {
          XMLUtil.skipWhitespace(this.reader, null);
          str6 = XMLUtil.scanIdentifier(this.reader);
          if (!str6.equals(str1)) {
            XMLUtil.errorWrongClosingTag(this.reader.getSystemID(), this.reader.getLineNr(), str2, str6);
          }
          XMLUtil.skipWhitespace(this.reader, null);
          if (this.reader.read() != '>') {
            XMLUtil.errorClosingTagNotEmpty(this.reader.getSystemID(), this.reader.getLineNr());
          }
          this.validator.elementEnded(str1, this.reader.getSystemID(), this.reader.getLineNr());
          if (str3 == null)
          {
            this.builder.endElement(str2, str3, paramString);
            break;
          }
          this.builder.endElement(str2, str3, paramProperties.getProperty(str3));
          break;
        }
        this.reader.unread(str6.charAt(0));
        scanSomeTag(true, paramString, (Properties)paramProperties.clone());
      }
      else
      {
        if (str6.charAt(0) == '&')
        {
          c = XMLUtil.processCharLiteral(str6);
          ((StringBuffer)localObject1).append(c);
        }
        else
        {
          this.reader.unread(str6.charAt(0));
        }
        this.validator.PCDataAdded(this.reader.getSystemID(), this.reader.getLineNr());
        localObject2 = new ContentReader(this.reader, this.entityResolver, ((StringBuffer)localObject1).toString());
        this.builder.addPCData((Reader)localObject2, this.reader.getSystemID(), this.reader.getLineNr());
        ((Reader)localObject2).close();
      }
    }
  }
  
  protected void processAttribute(Vector paramVector1, Vector paramVector2, Vector paramVector3)
    throws Exception
  {
    String str1 = XMLUtil.scanIdentifier(this.reader);
    XMLUtil.skipWhitespace(this.reader, null);
    if (!XMLUtil.read(this.reader, '&').equals("=")) {
      XMLUtil.errorExpectedInput(this.reader.getSystemID(), this.reader.getLineNr(), "`='");
    }
    XMLUtil.skipWhitespace(this.reader, null);
    String str2 = XMLUtil.scanString(this.reader, '&', this.entityResolver);
    paramVector1.addElement(str1);
    paramVector2.addElement(str2);
    paramVector3.addElement("CDATA");
    this.validator.attributeAdded(str1, str2, this.reader.getSystemID(), this.reader.getLineNr());
  }
}


/* Location:              C:\Users\sam\Downloads\nanoxml-2.2.3.jar!\net\n3\nanoxml\StdXMLParser.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */