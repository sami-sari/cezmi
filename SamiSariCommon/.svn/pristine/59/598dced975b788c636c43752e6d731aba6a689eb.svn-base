package net.n3.nanoxml;

import java.io.IOException;
import java.io.Reader;

class ContentReader
  extends Reader
{
  private IXMLReader reader;
  private String buffer;
  private int bufferIndex;
  private IXMLEntityResolver resolver;
  
  ContentReader(IXMLReader paramIXMLReader, IXMLEntityResolver paramIXMLEntityResolver, String paramString)
  {
    this.reader = paramIXMLReader;
    this.resolver = paramIXMLEntityResolver;
    this.buffer = paramString;
    this.bufferIndex = 0;
  }
  
  protected void finalize()
    throws Throwable
  {
    this.reader = null;
    this.resolver = null;
    this.buffer = null;
    super.finalize();
  }
  
  public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    try
    {
      int i = 0;
      int j = this.buffer.length();
      if (paramInt1 + paramInt2 > paramArrayOfChar.length) {
        paramInt2 = paramArrayOfChar.length - paramInt1;
      }
      while (i < paramInt2)
      {
        String str = "";
        char c;
        if (this.bufferIndex >= j)
        {
          str = XMLUtil.read(this.reader, '&');
          c = str.charAt(0);
        }
        else
        {
          c = this.buffer.charAt(this.bufferIndex);
          this.bufferIndex += 1;
          paramArrayOfChar[i] = c;
          i++;
          continue;
        }
        if (c == '<')
        {
          this.reader.unread(c);
          break;
        }
        if ((c == '&') && (str.length() > 1)) {
          if (str.charAt(1) == '#')
          {
            c = XMLUtil.processCharLiteral(str);
          }
          else
          {
            XMLUtil.processEntity(str, this.reader, this.resolver);
            continue;
          }
        }
        paramArrayOfChar[i] = c;
        i++;
      }
      if (i == 0) {
        i = -1;
      }
      return i;
    }
    catch (XMLParseException localXMLParseException)
    {
      throw new IOException(localXMLParseException.getMessage());
    }
  }
  
  public void close()
    throws IOException
  {
    try
    {
      int i = this.buffer.length();
      for (;;)
      {
        String str = "";
        char c;
        if (this.bufferIndex >= i)
        {
          str = XMLUtil.read(this.reader, '&');
          c = str.charAt(0);
        }
        else
        {
          c = this.buffer.charAt(this.bufferIndex);
          this.bufferIndex += 1;
          continue;
        }
        if (c == '<')
        {
          this.reader.unread(c);
          break;
        }
        if ((c == '&') && (str.length() > 1) && (str.charAt(1) != '#')) {
          XMLUtil.processEntity(str, this.reader, this.resolver);
        }
      }
    }
    catch (XMLParseException localXMLParseException)
    {
      throw new IOException(localXMLParseException.getMessage());
    }
  }
}


/* Location:              C:\Users\sam\Downloads\nanoxml-2.2.3.jar!\net\n3\nanoxml\ContentReader.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */