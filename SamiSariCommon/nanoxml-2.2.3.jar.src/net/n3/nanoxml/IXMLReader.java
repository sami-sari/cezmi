package net.n3.nanoxml;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;

public abstract interface IXMLReader
{
  public abstract char read()
    throws IOException;
  
  public abstract boolean atEOFOfCurrentStream()
    throws IOException;
  
  public abstract boolean atEOF()
    throws IOException;
  
  public abstract void unread(char paramChar)
    throws IOException;
  
  public abstract int getLineNr();
  
  public abstract Reader openStream(String paramString1, String paramString2)
    throws MalformedURLException, FileNotFoundException, IOException;
  
  public abstract void startNewStream(Reader paramReader);
  
  public abstract void startNewStream(Reader paramReader, boolean paramBoolean);
  
  public abstract int getStreamLevel();
  
  public abstract void setSystemID(String paramString)
    throws MalformedURLException;
  
  public abstract void setPublicID(String paramString);
  
  public abstract String getSystemID();
  
  public abstract String getPublicID();
}


/* Location:              C:\Users\sam\Downloads\nanoxml-2.2.3.jar!\net\n3\nanoxml\IXMLReader.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */