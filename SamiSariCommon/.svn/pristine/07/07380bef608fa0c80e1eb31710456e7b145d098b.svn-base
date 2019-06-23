package net.n3.nanoxml;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public abstract interface IXMLElement
{
  public static final int NO_LINE = -1;
  
  public abstract IXMLElement createPCDataElement();
  
  public abstract IXMLElement createElement(String paramString);
  
  public abstract IXMLElement createElement(String paramString1, String paramString2, int paramInt);
  
  public abstract IXMLElement createElement(String paramString1, String paramString2);
  
  public abstract IXMLElement createElement(String paramString1, String paramString2, String paramString3, int paramInt);
  
  public abstract IXMLElement getParent();
  
  public abstract String getFullName();
  
  public abstract String getName();
  
  public abstract String getNamespace();
  
  public abstract void setName(String paramString);
  
  public abstract void setName(String paramString1, String paramString2);
  
  public abstract void addChild(IXMLElement paramIXMLElement);
  
  public abstract void removeChild(IXMLElement paramIXMLElement);
  
  public abstract void removeChildAtIndex(int paramInt);
  
  public abstract Enumeration enumerateChildren();
  
  public abstract boolean isLeaf();
  
  public abstract boolean hasChildren();
  
  public abstract int getChildrenCount();
  
  public abstract Vector getChildren();
  
  public abstract IXMLElement getChildAtIndex(int paramInt)
    throws ArrayIndexOutOfBoundsException;
  
  public abstract IXMLElement getFirstChildNamed(String paramString);
  
  public abstract IXMLElement getFirstChildNamed(String paramString1, String paramString2);
  
  public abstract Vector getChildrenNamed(String paramString);
  
  public abstract Vector getChildrenNamed(String paramString1, String paramString2);
  
  public abstract int getAttributeCount();
  
  /**
   * @deprecated
   */
  public abstract String getAttribute(String paramString);
  
  public abstract String getAttribute(String paramString1, String paramString2);
  
  public abstract String getAttribute(String paramString1, String paramString2, String paramString3);
  
  public abstract int getAttribute(String paramString, int paramInt);
  
  public abstract int getAttribute(String paramString1, String paramString2, int paramInt);
  
  public abstract String getAttributeType(String paramString);
  
  public abstract String getAttributeNamespace(String paramString);
  
  public abstract String getAttributeType(String paramString1, String paramString2);
  
  public abstract void setAttribute(String paramString1, String paramString2);
  
  public abstract void setAttribute(String paramString1, String paramString2, String paramString3);
  
  public abstract void removeAttribute(String paramString);
  
  public abstract void removeAttribute(String paramString1, String paramString2);
  
  public abstract Enumeration enumerateAttributeNames();
  
  public abstract boolean hasAttribute(String paramString);
  
  public abstract boolean hasAttribute(String paramString1, String paramString2);
  
  public abstract Properties getAttributes();
  
  public abstract Properties getAttributesInNamespace(String paramString);
  
  public abstract String getSystemID();
  
  public abstract int getLineNr();
  
  public abstract String getContent();
  
  public abstract void setContent(String paramString);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract boolean equalsXMLElement(IXMLElement paramIXMLElement);
}


/* Location:              C:\Users\sam\Downloads\nanoxml-2.2.3.jar!\net\n3\nanoxml\IXMLElement.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */