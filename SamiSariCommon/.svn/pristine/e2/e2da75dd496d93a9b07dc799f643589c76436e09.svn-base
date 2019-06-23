package net.n3.nanoxml;

import java.io.Reader;
import java.io.StringReader;
import java.util.Hashtable;

public class XMLEntityResolver
  implements IXMLEntityResolver
{
  private Hashtable entities = new Hashtable();
  
  public XMLEntityResolver()
  {
    this.entities.put("amp", "&#38;");
    this.entities.put("quot", "&#34;");
    this.entities.put("apos", "&#39;");
    this.entities.put("lt", "&#60;");
    this.entities.put("gt", "&#62;");
  }
  
  protected void finalize()
    throws Throwable
  {
    this.entities.clear();
    this.entities = null;
    super.finalize();
  }
  
  public void addInternalEntity(String paramString1, String paramString2)
  {
    if (!this.entities.containsKey(paramString1)) {
      this.entities.put(paramString1, paramString2);
    }
  }
  
  public void addExternalEntity(String paramString1, String paramString2, String paramString3)
  {
    if (!this.entities.containsKey(paramString1)) {
      this.entities.put(paramString1, new String[] { paramString2, paramString3 });
    }
  }
  
  public Reader getEntity(IXMLReader paramIXMLReader, String paramString)
    throws XMLParseException
  {
    Object localObject = this.entities.get(paramString);
    if (localObject == null) {
      return null;
    }
    if ((localObject instanceof String)) {
      return new StringReader((String)localObject);
    }
    String[] arrayOfString = (String[])localObject;
    return openExternalEntity(paramIXMLReader, arrayOfString[0], arrayOfString[1]);
  }
  
  public boolean isExternalEntity(String paramString)
  {
    Object localObject = this.entities.get(paramString);
    return !(localObject instanceof String);
  }
  
  protected Reader openExternalEntity(IXMLReader paramIXMLReader, String paramString1, String paramString2)
    throws XMLParseException
  {
    String str = paramIXMLReader.getSystemID();
    try
    {
      return paramIXMLReader.openStream(paramString1, paramString2);
    }
    catch (Exception localException)
    {
      throw new XMLParseException(str, paramIXMLReader.getLineNr(), "Could not open external entity at system ID: " + paramString2);
    }
  }
}


/* Location:              C:\Users\sam\Downloads\nanoxml-2.2.3.jar!\net\n3\nanoxml\XMLEntityResolver.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */