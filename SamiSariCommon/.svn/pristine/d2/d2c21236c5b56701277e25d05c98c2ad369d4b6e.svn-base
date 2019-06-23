package net.n3.nanoxml;

import java.util.Properties;

public class ValidatorPlugin
  implements IXMLValidator
{
  private IXMLValidator delegate = null;
  
  protected void finalize()
    throws Throwable
  {
    this.delegate = null;
    super.finalize();
  }
  
  public IXMLValidator getDelegate()
  {
    return this.delegate;
  }
  
  public void setDelegate(IXMLValidator paramIXMLValidator)
  {
    this.delegate = paramIXMLValidator;
  }
  
  public void setParameterEntityResolver(IXMLEntityResolver paramIXMLEntityResolver)
  {
    this.delegate.setParameterEntityResolver(paramIXMLEntityResolver);
  }
  
  public IXMLEntityResolver getParameterEntityResolver()
  {
    return this.delegate.getParameterEntityResolver();
  }
  
  public void parseDTD(String paramString, IXMLReader paramIXMLReader, IXMLEntityResolver paramIXMLEntityResolver, boolean paramBoolean)
    throws Exception
  {
    this.delegate.parseDTD(paramString, paramIXMLReader, paramIXMLEntityResolver, paramBoolean);
  }
  
  public void elementStarted(String paramString1, String paramString2, int paramInt)
    throws Exception
  {
    this.delegate.elementStarted(paramString1, paramString2, paramInt);
  }
  
  public void elementEnded(String paramString1, String paramString2, int paramInt)
    throws Exception
  {
    this.delegate.elementEnded(paramString1, paramString2, paramInt);
  }
  
  public void elementAttributesProcessed(String paramString1, Properties paramProperties, String paramString2, int paramInt)
    throws Exception
  {
    this.delegate.elementAttributesProcessed(paramString1, paramProperties, paramString2, paramInt);
  }
  
  public void attributeAdded(String paramString1, String paramString2, String paramString3, int paramInt)
    throws Exception
  {
    this.delegate.attributeAdded(paramString1, paramString2, paramString3, paramInt);
  }
  
  public void PCDataAdded(String paramString, int paramInt)
    throws Exception
  {
    this.delegate.PCDataAdded(paramString, paramInt);
  }
  
  public void missingElement(String paramString1, int paramInt, String paramString2, String paramString3)
    throws XMLValidationException
  {
    XMLUtil.errorMissingElement(paramString1, paramInt, paramString2, paramString3);
  }
  
  public void unexpectedElement(String paramString1, int paramInt, String paramString2, String paramString3)
    throws XMLValidationException
  {
    XMLUtil.errorUnexpectedElement(paramString1, paramInt, paramString2, paramString3);
  }
  
  public void missingAttribute(String paramString1, int paramInt, String paramString2, String paramString3)
    throws XMLValidationException
  {
    XMLUtil.errorMissingAttribute(paramString1, paramInt, paramString2, paramString3);
  }
  
  public void unexpectedAttribute(String paramString1, int paramInt, String paramString2, String paramString3)
    throws XMLValidationException
  {
    XMLUtil.errorUnexpectedAttribute(paramString1, paramInt, paramString2, paramString3);
  }
  
  public void invalidAttributeValue(String paramString1, int paramInt, String paramString2, String paramString3, String paramString4)
    throws XMLValidationException
  {
    XMLUtil.errorInvalidAttributeValue(paramString1, paramInt, paramString2, paramString3, paramString4);
  }
  
  public void missingPCData(String paramString1, int paramInt, String paramString2)
    throws XMLValidationException
  {
    XMLUtil.errorMissingPCData(paramString1, paramInt, paramString2);
  }
  
  public void unexpectedPCData(String paramString1, int paramInt, String paramString2)
    throws XMLValidationException
  {
    XMLUtil.errorUnexpectedPCData(paramString1, paramInt, paramString2);
  }
  
  public void validationError(String paramString1, int paramInt, String paramString2, String paramString3, String paramString4, String paramString5)
    throws XMLValidationException
  {
    XMLUtil.validationError(paramString1, paramInt, paramString2, paramString3, paramString4, paramString5);
  }
}


/* Location:              C:\Users\sam\Downloads\nanoxml-2.2.3.jar!\net\n3\nanoxml\ValidatorPlugin.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */