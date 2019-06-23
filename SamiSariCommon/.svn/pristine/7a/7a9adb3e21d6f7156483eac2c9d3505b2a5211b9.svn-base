package net.n3.nanoxml;

public class XMLValidationException
  extends XMLException
{
  public static final int MISSING_ELEMENT = 1;
  public static final int UNEXPECTED_ELEMENT = 2;
  public static final int MISSING_ATTRIBUTE = 3;
  public static final int UNEXPECTED_ATTRIBUTE = 4;
  public static final int ATTRIBUTE_WITH_INVALID_VALUE = 5;
  public static final int MISSING_PCDATA = 6;
  public static final int UNEXPECTED_PCDATA = 7;
  public static final int MISC_ERROR = 0;
  private int errorType;
  private String elementName;
  private String attributeName;
  private String attributeValue;
  
  public XMLValidationException(int paramInt1, String paramString1, int paramInt2, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    super(paramString1, paramInt2, null, paramString5 + (paramString2 == null ? "" : new StringBuffer().append(", element=").append(paramString2).toString()) + (paramString3 == null ? "" : new StringBuffer().append(", attribute=").append(paramString3).toString()) + (paramString4 == null ? "" : new StringBuffer().append(", value='").append(paramString4).append("'").toString()), false);
    this.elementName = paramString2;
    this.attributeName = paramString3;
    this.attributeValue = paramString4;
  }
  
  protected void finalize()
    throws Throwable
  {
    this.elementName = null;
    this.attributeName = null;
    this.attributeValue = null;
    super.finalize();
  }
  
  public String getElementName()
  {
    return this.elementName;
  }
  
  public String getAttributeName()
  {
    return this.attributeName;
  }
  
  public String getAttributeValue()
  {
    return this.attributeValue;
  }
}


/* Location:              C:\Users\sam\Downloads\nanoxml-2.2.3.jar!\net\n3\nanoxml\XMLValidationException.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */