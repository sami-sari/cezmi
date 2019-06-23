package net.n3.nanoxml;

public class XMLParseException
  extends XMLException
{
  public XMLParseException(String paramString)
  {
    super(paramString);
  }
  
  public XMLParseException(String paramString1, int paramInt, String paramString2)
  {
    super(paramString1, paramInt, null, paramString2, true);
  }
}


/* Location:              C:\Users\sam\Downloads\nanoxml-2.2.3.jar!\net\n3\nanoxml\XMLParseException.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */