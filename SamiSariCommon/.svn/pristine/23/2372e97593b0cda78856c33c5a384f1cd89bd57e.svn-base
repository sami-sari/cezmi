package net.n3.nanoxml;

import java.io.Reader;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Stack;
import java.util.Vector;

public class NonValidator
  implements IXMLValidator
{
  protected IXMLEntityResolver parameterEntityResolver = new XMLEntityResolver();
  protected Hashtable attributeDefaultValues = new Hashtable();
  protected Stack currentElements = new Stack();
  
  protected void finalize()
    throws Throwable
  {
    this.parameterEntityResolver = null;
    this.attributeDefaultValues.clear();
    this.attributeDefaultValues = null;
    this.currentElements.clear();
    this.currentElements = null;
    super.finalize();
  }
  
  public void setParameterEntityResolver(IXMLEntityResolver paramIXMLEntityResolver)
  {
    this.parameterEntityResolver = paramIXMLEntityResolver;
  }
  
  public IXMLEntityResolver getParameterEntityResolver()
  {
    return this.parameterEntityResolver;
  }
  
  public void parseDTD(String paramString, IXMLReader paramIXMLReader, IXMLEntityResolver paramIXMLEntityResolver, boolean paramBoolean)
    throws Exception
  {
    XMLUtil.skipWhitespace(paramIXMLReader, null);
    int i = paramIXMLReader.getStreamLevel();
    for (;;)
    {
      String str = XMLUtil.read(paramIXMLReader, '%');
      char c = str.charAt(0);
      if (c == '%')
      {
        XMLUtil.processEntity(str, paramIXMLReader, this.parameterEntityResolver);
      }
      else
      {
        if (c == '<')
        {
          processElement(paramIXMLReader, paramIXMLEntityResolver);
        }
        else
        {
          if (c == ']') {
            return;
          }
          XMLUtil.errorInvalidInput(paramIXMLReader.getSystemID(), paramIXMLReader.getLineNr(), str);
        }
        do
        {
          c = paramIXMLReader.read();
          if ((paramBoolean) && (paramIXMLReader.getStreamLevel() < i))
          {
            paramIXMLReader.unread(c);
            return;
          }
        } while ((c == ' ') || (c == '\t') || (c == '\n') || (c == '\r'));
        paramIXMLReader.unread(c);
      }
    }
  }
  
  protected void processElement(IXMLReader paramIXMLReader, IXMLEntityResolver paramIXMLEntityResolver)
    throws Exception
  {
    String str = XMLUtil.read(paramIXMLReader, '%');
    int i = str.charAt(0);
    if (i != 33)
    {
      XMLUtil.skipTag(paramIXMLReader);
      return;
    }
    str = XMLUtil.read(paramIXMLReader, '%');
    i = str.charAt(0);
    switch (i)
    {
    case 45: 
      XMLUtil.skipComment(paramIXMLReader);
      break;
    case 91: 
      processConditionalSection(paramIXMLReader, paramIXMLEntityResolver);
      break;
    case 69: 
      processEntity(paramIXMLReader, paramIXMLEntityResolver);
      break;
    case 65: 
      processAttList(paramIXMLReader, paramIXMLEntityResolver);
      break;
    default: 
      XMLUtil.skipTag(paramIXMLReader);
    }
  }
  
  protected void processConditionalSection(IXMLReader paramIXMLReader, IXMLEntityResolver paramIXMLEntityResolver)
    throws Exception
  {
    XMLUtil.skipWhitespace(paramIXMLReader, null);
    String str = XMLUtil.read(paramIXMLReader, '%');
    int i = str.charAt(0);
    if (i != 73)
    {
      XMLUtil.skipTag(paramIXMLReader);
      return;
    }
    str = XMLUtil.read(paramIXMLReader, '%');
    i = str.charAt(0);
    switch (i)
    {
    case 71: 
      processIgnoreSection(paramIXMLReader, paramIXMLEntityResolver);
      return;
    case 78: 
      break;
    default: 
      XMLUtil.skipTag(paramIXMLReader);
      return;
    }
    if (!XMLUtil.checkLiteral(paramIXMLReader, "CLUDE"))
    {
      XMLUtil.skipTag(paramIXMLReader);
      return;
    }
    XMLUtil.skipWhitespace(paramIXMLReader, null);
    str = XMLUtil.read(paramIXMLReader, '%');
    i = str.charAt(0);
    if (i != 91)
    {
      XMLUtil.skipTag(paramIXMLReader);
      return;
    }
    CDATAReader localCDATAReader = new CDATAReader(paramIXMLReader);
    StringBuffer localStringBuffer = new StringBuffer(1024);
    for (;;)
    {
      int j = localCDATAReader.read();
      if (j < 0) {
        break;
      }
      localStringBuffer.append((char)j);
    }
    localCDATAReader.close();
    paramIXMLReader.startNewStream(new StringReader(localStringBuffer.toString()));
  }
  
  protected void processIgnoreSection(IXMLReader paramIXMLReader, IXMLEntityResolver paramIXMLEntityResolver)
    throws Exception
  {
    if (!XMLUtil.checkLiteral(paramIXMLReader, "NORE"))
    {
      XMLUtil.skipTag(paramIXMLReader);
      return;
    }
    XMLUtil.skipWhitespace(paramIXMLReader, null);
    String str = XMLUtil.read(paramIXMLReader, '%');
    int i = str.charAt(0);
    if (i != 91)
    {
      XMLUtil.skipTag(paramIXMLReader);
      return;
    }
    CDATAReader localCDATAReader = new CDATAReader(paramIXMLReader);
    localCDATAReader.close();
  }
  
  protected void processAttList(IXMLReader paramIXMLReader, IXMLEntityResolver paramIXMLEntityResolver)
    throws Exception
  {
    if (!XMLUtil.checkLiteral(paramIXMLReader, "TTLIST"))
    {
      XMLUtil.skipTag(paramIXMLReader);
      return;
    }
    XMLUtil.skipWhitespace(paramIXMLReader, null);
    String str1 = XMLUtil.read(paramIXMLReader, '%');
    char c = str1.charAt(0);
    for (; c == '%'; c = str1.charAt(0))
    {
      XMLUtil.processEntity(str1, paramIXMLReader, this.parameterEntityResolver);
      str1 = XMLUtil.read(paramIXMLReader, '%');
    }
    paramIXMLReader.unread(c);
    String str2 = XMLUtil.scanIdentifier(paramIXMLReader);
    XMLUtil.skipWhitespace(paramIXMLReader, null);
    str1 = XMLUtil.read(paramIXMLReader, '%');
    for (c = str1.charAt(0); c == '%'; c = str1.charAt(0))
    {
      XMLUtil.processEntity(str1, paramIXMLReader, this.parameterEntityResolver);
      str1 = XMLUtil.read(paramIXMLReader, '%');
    }
    Properties localProperties = new Properties();
    while (c != '>')
    {
      paramIXMLReader.unread(c);
      String str3 = XMLUtil.scanIdentifier(paramIXMLReader);
      XMLUtil.skipWhitespace(paramIXMLReader, null);
      str1 = XMLUtil.read(paramIXMLReader, '%');
      for (c = str1.charAt(0); c == '%'; c = str1.charAt(0))
      {
        XMLUtil.processEntity(str1, paramIXMLReader, this.parameterEntityResolver);
        str1 = XMLUtil.read(paramIXMLReader, '%');
      }
      if (c == '(')
      {
        while (c != ')')
        {
          str1 = XMLUtil.read(paramIXMLReader, '%');
          for (c = str1.charAt(0); c == '%'; c = str1.charAt(0))
          {
            XMLUtil.processEntity(str1, paramIXMLReader, this.parameterEntityResolver);
            str1 = XMLUtil.read(paramIXMLReader, '%');
          }
        }
      }
      else
      {
        paramIXMLReader.unread(c);
        XMLUtil.scanIdentifier(paramIXMLReader);
      }
      XMLUtil.skipWhitespace(paramIXMLReader, null);
      str1 = XMLUtil.read(paramIXMLReader, '%');
      for (c = str1.charAt(0); c == '%'; c = str1.charAt(0))
      {
        XMLUtil.processEntity(str1, paramIXMLReader, this.parameterEntityResolver);
        str1 = XMLUtil.read(paramIXMLReader, '%');
      }
      if (c == '#')
      {
        str1 = XMLUtil.scanIdentifier(paramIXMLReader);
        XMLUtil.skipWhitespace(paramIXMLReader, null);
        if (!str1.equals("FIXED"))
        {
          XMLUtil.skipWhitespace(paramIXMLReader, null);
          str1 = XMLUtil.read(paramIXMLReader, '%');
          for (c = str1.charAt(0); c == '%'; c = str1.charAt(0))
          {
            XMLUtil.processEntity(str1, paramIXMLReader, this.parameterEntityResolver);
            str1 = XMLUtil.read(paramIXMLReader, '%');
          }
          continue;
        }
      }
      else
      {
        paramIXMLReader.unread(c);
      }
      String str4 = XMLUtil.scanString(paramIXMLReader, '%', this.parameterEntityResolver);
      localProperties.put(str3, str4);
      XMLUtil.skipWhitespace(paramIXMLReader, null);
      str1 = XMLUtil.read(paramIXMLReader, '%');
      for (c = str1.charAt(0); c == '%'; c = str1.charAt(0))
      {
        XMLUtil.processEntity(str1, paramIXMLReader, this.parameterEntityResolver);
        str1 = XMLUtil.read(paramIXMLReader, '%');
      }
    }
    if (!localProperties.isEmpty()) {
      this.attributeDefaultValues.put(str2, localProperties);
    }
  }
  
  protected void processEntity(IXMLReader paramIXMLReader, IXMLEntityResolver paramIXMLEntityResolver)
    throws Exception
  {
    if (!XMLUtil.checkLiteral(paramIXMLReader, "NTITY"))
    {
      XMLUtil.skipTag(paramIXMLReader);
      return;
    }
    XMLUtil.skipWhitespace(paramIXMLReader, null);
    char c = XMLUtil.readChar(paramIXMLReader, '\000');
    if (c == '%')
    {
      XMLUtil.skipWhitespace(paramIXMLReader, null);
      paramIXMLEntityResolver = this.parameterEntityResolver;
    }
    else
    {
      paramIXMLReader.unread(c);
    }
    String str1 = XMLUtil.scanIdentifier(paramIXMLReader);
    XMLUtil.skipWhitespace(paramIXMLReader, null);
    c = XMLUtil.readChar(paramIXMLReader, '%');
    String str2 = null;
    String str3 = null;
    switch (c)
    {
    case 'P': 
      if (!XMLUtil.checkLiteral(paramIXMLReader, "UBLIC"))
      {
        XMLUtil.skipTag(paramIXMLReader);
        return;
      }
      XMLUtil.skipWhitespace(paramIXMLReader, null);
      str3 = XMLUtil.scanString(paramIXMLReader, '%', this.parameterEntityResolver);
      XMLUtil.skipWhitespace(paramIXMLReader, null);
      str2 = XMLUtil.scanString(paramIXMLReader, '%', this.parameterEntityResolver);
      XMLUtil.skipWhitespace(paramIXMLReader, null);
      XMLUtil.readChar(paramIXMLReader, '%');
      break;
    case 'S': 
      if (!XMLUtil.checkLiteral(paramIXMLReader, "YSTEM"))
      {
        XMLUtil.skipTag(paramIXMLReader);
        return;
      }
      XMLUtil.skipWhitespace(paramIXMLReader, null);
      str2 = XMLUtil.scanString(paramIXMLReader, '%', this.parameterEntityResolver);
      XMLUtil.skipWhitespace(paramIXMLReader, null);
      XMLUtil.readChar(paramIXMLReader, '%');
      break;
    case '"': 
    case '\'': 
      paramIXMLReader.unread(c);
      String str4 = XMLUtil.scanString(paramIXMLReader, '%', this.parameterEntityResolver);
      paramIXMLEntityResolver.addInternalEntity(str1, str4);
      XMLUtil.skipWhitespace(paramIXMLReader, null);
      XMLUtil.readChar(paramIXMLReader, '%');
      break;
    default: 
      XMLUtil.skipTag(paramIXMLReader);
    }
    if (str2 != null) {
      paramIXMLEntityResolver.addExternalEntity(str1, str3, str2);
    }
  }
  
  public void elementStarted(String paramString1, String paramString2, int paramInt)
  {
    Properties localProperties = (Properties)this.attributeDefaultValues.get(paramString1);
    if (localProperties == null) {
      localProperties = new Properties();
    } else {
      localProperties = (Properties)localProperties.clone();
    }
    this.currentElements.push(localProperties);
  }
  
  public void elementEnded(String paramString1, String paramString2, int paramInt) {}
  
  public void elementAttributesProcessed(String paramString1, Properties paramProperties, String paramString2, int paramInt)
  {
    Properties localProperties = (Properties)this.currentElements.pop();
    Enumeration localEnumeration = localProperties.keys();
    while (localEnumeration.hasMoreElements())
    {
      String str = (String)localEnumeration.nextElement();
      paramProperties.put(str, localProperties.get(str));
    }
  }
  
  public void attributeAdded(String paramString1, String paramString2, String paramString3, int paramInt)
  {
    Properties localProperties = (Properties)this.currentElements.peek();
    if (localProperties.containsKey(paramString1)) {
      localProperties.remove(paramString1);
    }
  }
  
  public void PCDataAdded(String paramString, int paramInt) {}
}


/* Location:              C:\Users\sam\Downloads\nanoxml-2.2.3.jar!\net\n3\nanoxml\NonValidator.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */