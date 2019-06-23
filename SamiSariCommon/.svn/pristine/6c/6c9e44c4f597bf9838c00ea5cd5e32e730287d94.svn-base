package net.n3.nanoxml;

import java.util.Properties;

public abstract interface IXMLValidator
{
  public abstract void setParameterEntityResolver(IXMLEntityResolver paramIXMLEntityResolver);
  
  public abstract IXMLEntityResolver getParameterEntityResolver();
  
  public abstract void parseDTD(String paramString, IXMLReader paramIXMLReader, IXMLEntityResolver paramIXMLEntityResolver, boolean paramBoolean)
    throws Exception;
  
  public abstract void elementStarted(String paramString1, String paramString2, int paramInt)
    throws Exception;
  
  public abstract void elementEnded(String paramString1, String paramString2, int paramInt)
    throws Exception;
  
  public abstract void attributeAdded(String paramString1, String paramString2, String paramString3, int paramInt)
    throws Exception;
  
  public abstract void elementAttributesProcessed(String paramString1, Properties paramProperties, String paramString2, int paramInt)
    throws Exception;
  
  public abstract void PCDataAdded(String paramString, int paramInt)
    throws Exception;
}


/* Location:              C:\Users\sam\Downloads\nanoxml-2.2.3.jar!\net\n3\nanoxml\IXMLValidator.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */