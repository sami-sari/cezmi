package net.n3.nanoxml;

import java.io.IOException;
import java.io.Reader;

class PIReader
  extends Reader
{
  private IXMLReader reader;
  private boolean atEndOfData;
  
  PIReader(IXMLReader paramIXMLReader)
  {
    this.reader = paramIXMLReader;
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
    if (this.atEndOfData) {
      return -1;
    }
    int i = 0;
    if (paramInt1 + paramInt2 > paramArrayOfChar.length) {
      paramInt2 = paramArrayOfChar.length - paramInt1;
    }
    while (i < paramInt2)
    {
      int j = this.reader.read();
      if (j == 63)
      {
        char c = this.reader.read();
        if (c == '>')
        {
          this.atEndOfData = true;
          break;
        }
        this.reader.unread(c);
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
      int i = this.reader.read();
      if (i == 63)
      {
        int j = this.reader.read();
        if (j == 62) {
          this.atEndOfData = true;
        }
      }
    }
  }
}


/* Location:              C:\Users\sam\Downloads\nanoxml-2.2.3.jar!\net\n3\nanoxml\PIReader.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */