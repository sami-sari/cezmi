package net.n3.nanoxml;

import java.io.IOException;
import java.io.Reader;

class XMLUtil
{
  static void skipComment(IXMLReader paramIXMLReader)
    throws IOException, XMLParseException
  {
    if (paramIXMLReader.read() != '-') {
      errorExpectedInput(paramIXMLReader.getSystemID(), paramIXMLReader.getLineNr(), "<!--");
    }
    int i = 0;
    for (;;)
    {
      int j = paramIXMLReader.read();
      switch (j)
      {
      case 45: 
        i++;
        break;
      case 62: 
        if (i == 2) {
          return;
        }
      default: 
        i = 0;
      }
    }
  }
  
  static void skipTag(IXMLReader paramIXMLReader)
    throws IOException, XMLParseException
  {
    int i = 1;
    while (i > 0)
    {
      int j = paramIXMLReader.read();
      switch (j)
      {
      case 60: 
        i++;
        break;
      case 62: 
        i--;
      }
    }
  }
  
  static String scanPublicID(StringBuffer paramStringBuffer, IXMLReader paramIXMLReader)
    throws IOException, XMLParseException
  {
    if (!checkLiteral(paramIXMLReader, "UBLIC")) {
      return null;
    }
    skipWhitespace(paramIXMLReader, null);
    paramStringBuffer.append(scanString(paramIXMLReader, '\000', null));
    skipWhitespace(paramIXMLReader, null);
    return scanString(paramIXMLReader, '\000', null);
  }
  
  static String scanSystemID(IXMLReader paramIXMLReader)
    throws IOException, XMLParseException
  {
    if (!checkLiteral(paramIXMLReader, "YSTEM")) {
      return null;
    }
    skipWhitespace(paramIXMLReader, null);
    return scanString(paramIXMLReader, '\000', null);
  }
  
  static String scanIdentifier(IXMLReader paramIXMLReader)
    throws IOException, XMLParseException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    char c;
    for (;;)
    {
      c = paramIXMLReader.read();
      if ((c != '_') && (c != ':') && (c != '-') && (c != '.') && ((c < 'a') || (c > 'z')) && ((c < 'A') || (c > 'Z')) && ((c < '0') || (c > '9')) && (c <= '~')) {
        break;
      }
      localStringBuffer.append(c);
    }
    paramIXMLReader.unread(c);
    return localStringBuffer.toString();
  }
  
  static String scanString(IXMLReader paramIXMLReader, char paramChar, IXMLEntityResolver paramIXMLEntityResolver)
    throws IOException, XMLParseException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = paramIXMLReader.getStreamLevel();
    char c1 = paramIXMLReader.read();
    if ((c1 != '\'') && (c1 != '"')) {
      errorExpectedInput(paramIXMLReader.getSystemID(), paramIXMLReader.getLineNr(), "delimited string");
    }
    for (;;)
    {
      String str = read(paramIXMLReader, paramChar);
      char c2 = str.charAt(0);
      if (c2 == paramChar)
      {
        if (str.charAt(1) == '#') {
          localStringBuffer.append(processCharLiteral(str));
        } else {
          processEntity(str, paramIXMLReader, paramIXMLEntityResolver);
        }
      }
      else if (c2 == '&')
      {
        paramIXMLReader.unread(c2);
        str = read(paramIXMLReader, '&');
        if (str.charAt(1) == '#') {
          localStringBuffer.append(processCharLiteral(str));
        } else {
          localStringBuffer.append(str);
        }
      }
      else if (paramIXMLReader.getStreamLevel() == i)
      {
        if (c2 == c1) {
          break;
        }
        if ((c2 == '\t') || (c2 == '\n') || (c2 == '\r')) {
          localStringBuffer.append(' ');
        } else {
          localStringBuffer.append(c2);
        }
      }
      else
      {
        localStringBuffer.append(c2);
      }
    }
    return localStringBuffer.toString();
  }
  
  static void processEntity(String paramString, IXMLReader paramIXMLReader, IXMLEntityResolver paramIXMLEntityResolver)
    throws IOException, XMLParseException
  {
    paramString = paramString.substring(1, paramString.length() - 1);
    Reader localReader = paramIXMLEntityResolver.getEntity(paramIXMLReader, paramString);
    if (localReader == null) {
      errorInvalidEntity(paramIXMLReader.getSystemID(), paramIXMLReader.getLineNr(), paramString);
    }
    boolean bool = paramIXMLEntityResolver.isExternalEntity(paramString);
    paramIXMLReader.startNewStream(localReader, !bool);
  }
  
  static char processCharLiteral(String paramString)
    throws IOException, XMLParseException
  {
    if (paramString.charAt(2) == 'x')
    {
      paramString = paramString.substring(3, paramString.length() - 1);
      return (char)Integer.parseInt(paramString, 16);
    }
    paramString = paramString.substring(2, paramString.length() - 1);
    return (char)Integer.parseInt(paramString, 10);
  }
  
  static void skipWhitespace(IXMLReader paramIXMLReader, StringBuffer paramStringBuffer)
    throws IOException
  {
    char c;
    if (paramStringBuffer == null) {
      do
      {
        c = paramIXMLReader.read();
      } while ((c == ' ') || (c == '\t') || (c == '\n'));
    } else {
      for (;;)
      {
        c = paramIXMLReader.read();
        if ((c != ' ') && (c != '\t') && (c != '\n')) {
          break;
        }
        if (c == '\n') {
          paramStringBuffer.append('\n');
        } else {
          paramStringBuffer.append(' ');
        }
      }
    }
    paramIXMLReader.unread(c);
  }
  
  static String read(IXMLReader paramIXMLReader, char paramChar)
    throws IOException, XMLParseException
  {
    char c = paramIXMLReader.read();
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(c);
    if (c == paramChar) {
      while (c != ';')
      {
        c = paramIXMLReader.read();
        localStringBuffer.append(c);
      }
    }
    return localStringBuffer.toString();
  }
  
  static char readChar(IXMLReader paramIXMLReader, char paramChar)
    throws IOException, XMLParseException
  {
    String str = read(paramIXMLReader, paramChar);
    char c = str.charAt(0);
    if (c == paramChar) {
      errorUnexpectedEntity(paramIXMLReader.getSystemID(), paramIXMLReader.getLineNr(), str);
    }
    return c;
  }
  
  static boolean checkLiteral(IXMLReader paramIXMLReader, String paramString)
    throws IOException, XMLParseException
  {
    for (int i = 0; i < paramString.length(); i++) {
      if (paramIXMLReader.read() != paramString.charAt(i)) {
        return false;
      }
    }
    return true;
  }
  
  static void errorExpectedInput(String paramString1, int paramInt, String paramString2)
    throws XMLParseException
  {
    throw new XMLParseException(paramString1, paramInt, "Expected: " + paramString2);
  }
  
  static void errorInvalidEntity(String paramString1, int paramInt, String paramString2)
    throws XMLParseException
  {
    throw new XMLParseException(paramString1, paramInt, "Invalid entity: `&" + paramString2 + ";'");
  }
  
  static void errorUnexpectedEntity(String paramString1, int paramInt, String paramString2)
    throws XMLParseException
  {
    throw new XMLParseException(paramString1, paramInt, "No entity reference is expected here (" + paramString2 + ")");
  }
  
  static void errorUnexpectedCDATA(String paramString, int paramInt)
    throws XMLParseException
  {
    throw new XMLParseException(paramString, paramInt, "No CDATA section is expected here");
  }
  
  static void errorInvalidInput(String paramString1, int paramInt, String paramString2)
    throws XMLParseException
  {
    throw new XMLParseException(paramString1, paramInt, "Invalid input: " + paramString2);
  }
  
  static void errorWrongClosingTag(String paramString1, int paramInt, String paramString2, String paramString3)
    throws XMLParseException
  {
    throw new XMLParseException(paramString1, paramInt, "Closing tag does not match opening tag: `" + paramString3 + "' != `" + paramString2 + "'");
  }
  
  static void errorClosingTagNotEmpty(String paramString, int paramInt)
    throws XMLParseException
  {
    throw new XMLParseException(paramString, paramInt, "Closing tag must be empty");
  }
  
  static void errorMissingElement(String paramString1, int paramInt, String paramString2, String paramString3)
    throws XMLValidationException
  {
    throw new XMLValidationException(1, paramString1, paramInt, paramString3, null, null, "Element " + paramString2 + " expects to have a " + paramString3);
  }
  
  static void errorUnexpectedElement(String paramString1, int paramInt, String paramString2, String paramString3)
    throws XMLValidationException
  {
    throw new XMLValidationException(2, paramString1, paramInt, paramString3, null, null, "Unexpected " + paramString3 + " in a " + paramString2);
  }
  
  static void errorMissingAttribute(String paramString1, int paramInt, String paramString2, String paramString3)
    throws XMLValidationException
  {
    throw new XMLValidationException(3, paramString1, paramInt, paramString2, paramString3, null, "Element " + paramString2 + " expects an attribute named " + paramString3);
  }
  
  static void errorUnexpectedAttribute(String paramString1, int paramInt, String paramString2, String paramString3)
    throws XMLValidationException
  {
    throw new XMLValidationException(4, paramString1, paramInt, paramString2, paramString3, null, "Element " + paramString2 + " did not expect an attribute " + "named " + paramString3);
  }
  
  static void errorInvalidAttributeValue(String paramString1, int paramInt, String paramString2, String paramString3, String paramString4)
    throws XMLValidationException
  {
    throw new XMLValidationException(5, paramString1, paramInt, paramString2, paramString3, paramString4, "Invalid value for attribute " + paramString3);
  }
  
  static void errorMissingPCData(String paramString1, int paramInt, String paramString2)
    throws XMLValidationException
  {
    throw new XMLValidationException(6, paramString1, paramInt, null, null, null, "Missing #PCDATA in element " + paramString2);
  }
  
  static void errorUnexpectedPCData(String paramString1, int paramInt, String paramString2)
    throws XMLValidationException
  {
    throw new XMLValidationException(7, paramString1, paramInt, null, null, null, "Unexpected #PCDATA in element " + paramString2);
  }
  
  static void validationError(String paramString1, int paramInt, String paramString2, String paramString3, String paramString4, String paramString5)
    throws XMLValidationException
  {
    throw new XMLValidationException(0, paramString1, paramInt, paramString3, paramString4, paramString5, paramString2);
  }
}


/* Location:              C:\Users\sam\Downloads\nanoxml-2.2.3.jar!\net\n3\nanoxml\XMLUtil.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */