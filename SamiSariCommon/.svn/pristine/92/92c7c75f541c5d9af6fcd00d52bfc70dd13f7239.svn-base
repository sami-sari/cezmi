package net.n3.nanoxml;

import java.io.IOException;
import java.io.Reader;

class CDATAReader
  extends Reader
{
  private IXMLReader reader;
  private char savedChar;
  private boolean atEndOfData;
  
  CDATAReader(IXMLReader paramIXMLReader)
  {
    this.reader = paramIXMLReader;
    this.savedChar = '\000';
    this.atEndOfData = false;
  }
  
  protected void finalize()
    throws Throwable
  {
    this.reader = null;
    super.finalize();
  }
  
  public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = 0;
    if (this.atEndOfData) {
      return -1;
    }
    if (paramInt1 + paramInt2 > paramArrayOfChar.length) {
      paramInt2 = paramArrayOfChar.length - paramInt1;
    }
    while (i < paramInt2)
    {
      int j = this.savedChar;
      if (j == 0) {
        j = this.reader.read();
      } else {
        this.savedChar = '\000';
      }
      if (j == 93)
      {
        char c1 = this.reader.read();
        if (c1 == ']')
        {
          char c2 = this.reader.read();
          if (c2 == '>')
          {
            this.atEndOfData = true;
            break;
          }
          this.savedChar = c1;
          this.reader.unread(c2);
        }
        else
        {
          this.reader.unread(c1);
        }
      }
      paramArrayOfChar[i] = (char)j;
      i++;
    }
    if (i == 0) {
      i = -1;
    }
    return i;
  }
  
  public void close()
    throws IOException
  {
    while (!this.atEndOfData)
    {
      int i = this.savedChar;
      if (i == 0) {
        i = this.reader.read();
      } else {
        this.savedChar = '\000';
      }
      if (i == 93)
      {
        char c1 = this.reader.read();
        if (c1 == ']')
        {
          char c2 = this.reader.read();
          if (c2 == '>') {
            break;
          }
          this.savedChar = c1;
          this.reader.unread(c2);
        }
        else
        {
          this.reader.unread(c1);
        }
      }
    }
    this.atEndOfData = true;
  }
}


/* Location:              C:\Users\sam\Downloads\nanoxml-2.2.3.jar!\net\n3\nanoxml\CDATAReader.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */