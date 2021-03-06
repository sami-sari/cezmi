package net.n3.nanoxml;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

public class XMLElement
  implements IXMLElement, Serializable
{
  static final long serialVersionUID = -2383376380548624920L;
  public static final int NO_LINE = -1;
  private IXMLElement parent;
  private Vector attributes = new Vector();
  private Vector children = new Vector(8);
  private String name;
  private String fullName;
  private String namespace;
  private String content;
  private String systemID;
  private int lineNr;
  
  public XMLElement()
  {
    this(null, null, null, -1);
  }
  
  public XMLElement(String fullname)
  {
    this(fullname, null, null, -1);
  }
  
  public XMLElement(String fullname, String namespace, int lineNr)
  {
    this(fullname, null, namespace, lineNr);
  }
  
  public XMLElement(String fullname, String namespace)
  {
    this(fullname, namespace, null, -1);
  }
  
  public XMLElement(String fullname, String namespace, String systemID, int lineNr)
  {
    this.fullName = fullname;
    if (namespace == null)
    {
      this.name = fullname;
    }
    else
    {
      int i = fullname.indexOf(':');
      if (i >= 0) {
        this.name = fullname.substring(i + 1);
      } else {
        this.name = fullname;
      }
    }
    this.namespace = namespace;
    this.content = null;
    this.lineNr = lineNr;
    this.systemID = systemID;
    this.parent = null;
  }
  
  public IXMLElement createPCDataElement()
  {
    return new XMLElement();
  }
  
  public IXMLElement createElement(String fullname)
  {
    return new XMLElement(fullname);
  }
  
  public IXMLElement createElement(String fullname, String namespace, int lineNr)
  {
    return new XMLElement(fullname, namespace, lineNr);
  }
  
  public IXMLElement createElement(String paramString1, String paramString2)
  {
    return new XMLElement(paramString1, paramString2);
  }
  
  public IXMLElement createElement(String paramString1, String paramString2, String paramString3, int paramInt)
  {
    return new XMLElement(paramString1, paramString2, paramString3, paramInt);
  }
  
  protected void finalize()
    throws Throwable
  {
    this.attributes.clear();
    this.attributes = null;
    this.children = null;
    this.fullName = null;
    this.name = null;
    this.namespace = null;
    this.content = null;
    this.systemID = null;
    this.parent = null;
    super.finalize();
  }
  
  public IXMLElement getParent()
  {
    return this.parent;
  }
  
  public String getFullName()
  {
    return this.fullName;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getNamespace()
  {
    return this.namespace;
  }
  
  public void setName(String paramString)
  {
    this.name = paramString;
    this.fullName = paramString;
    this.namespace = null;
  }
  
  public void setName(String paramString1, String paramString2)
  {
    int i = paramString1.indexOf(':');
    if ((paramString2 == null) || (i < 0)) {
      this.name = paramString1;
    } else {
      this.name = paramString1.substring(i + 1);
    }
    this.fullName = paramString1;
    this.namespace = paramString2;
  }
  
  public void addChild(IXMLElement paramIXMLElement)
  {
    if (paramIXMLElement == null) {
      throw new IllegalArgumentException("child must not be null");
    }
    if ((paramIXMLElement.getName() == null) && (!this.children.isEmpty()))
    {
      IXMLElement localIXMLElement = (IXMLElement)this.children.lastElement();
      if (localIXMLElement.getName() == null)
      {
        localIXMLElement.setContent(localIXMLElement.getContent() + paramIXMLElement.getContent());
        return;
      }
    }
    ((XMLElement)paramIXMLElement).parent = this;
    this.children.addElement(paramIXMLElement);
  }
  
  public void insertChild(IXMLElement paramIXMLElement, int paramInt)
  {
    if (paramIXMLElement == null) {
      throw new IllegalArgumentException("child must not be null");
    }
    if ((paramIXMLElement.getName() == null) && (!this.children.isEmpty()))
    {
      IXMLElement localIXMLElement = (IXMLElement)this.children.lastElement();
      if (localIXMLElement.getName() == null)
      {
        localIXMLElement.setContent(localIXMLElement.getContent() + paramIXMLElement.getContent());
        return;
      }
    }
    ((XMLElement)paramIXMLElement).parent = this;
    this.children.insertElementAt(paramIXMLElement, paramInt);
  }
  
  public void removeChild(IXMLElement paramIXMLElement)
  {
    if (paramIXMLElement == null) {
      throw new IllegalArgumentException("child must not be null");
    }
    this.children.removeElement(paramIXMLElement);
  }
  
  public void removeChildAtIndex(int paramInt)
  {
    this.children.removeElementAt(paramInt);
  }
  
  public Enumeration enumerateChildren()
  {
    return this.children.elements();
  }
  
  public boolean isLeaf()
  {
    return this.children.isEmpty();
  }
  
  public boolean hasChildren()
  {
    return !this.children.isEmpty();
  }
  
  public int getChildrenCount()
  {
    return this.children.size();
  }
  
  public Vector getChildren()
  {
    return this.children;
  }
  
  public IXMLElement getChildAtIndex(int paramInt)
    throws ArrayIndexOutOfBoundsException
  {
    return (IXMLElement)this.children.elementAt(paramInt);
  }
  
  public IXMLElement getFirstChildNamed(String paramString)
  {
    Enumeration localEnumeration = this.children.elements();
    while (localEnumeration.hasMoreElements())
    {
      IXMLElement localIXMLElement = (IXMLElement)localEnumeration.nextElement();
      String str = localIXMLElement.getFullName();
      if ((str != null) && (str.equals(paramString))) {
        return localIXMLElement;
      }
    }
    return null;
  }
  
  public IXMLElement getFirstChildNamed(String paramString1, String paramString2)
  {
    Enumeration localEnumeration = this.children.elements();
    while (localEnumeration.hasMoreElements())
    {
      IXMLElement localIXMLElement = (IXMLElement)localEnumeration.nextElement();
      String str = localIXMLElement.getName();
      boolean bool = (str != null) && (str.equals(paramString1));
      str = localIXMLElement.getNamespace();
      if (str == null) {
        bool &= paramString1 == null;
      } else {
        bool &= str.equals(paramString2);
      }
      if (bool) {
        return localIXMLElement;
      }
    }
    return null;
  }
  
  public Vector getChildrenNamed(String paramString)
  {
    Vector localVector = new Vector(this.children.size());
    Enumeration localEnumeration = this.children.elements();
    while (localEnumeration.hasMoreElements())
    {
      IXMLElement localIXMLElement = (IXMLElement)localEnumeration.nextElement();
      String str = localIXMLElement.getFullName();
      if ((str != null) && (str.equals(paramString))) {
        localVector.addElement(localIXMLElement);
      }
    }
    return localVector;
  }
  
  public Vector getChildrenNamed(String paramString1, String paramString2)
  {
    Vector localVector = new Vector(this.children.size());
    Enumeration localEnumeration = this.children.elements();
    while (localEnumeration.hasMoreElements())
    {
      IXMLElement localIXMLElement = (IXMLElement)localEnumeration.nextElement();
      String str = localIXMLElement.getName();
      boolean bool = (str != null) && (str.equals(paramString1));
      str = localIXMLElement.getNamespace();
      if (str == null) {
        bool &= paramString1 == null;
      } else {
        bool &= str.equals(paramString2);
      }
      if (bool) {
        localVector.addElement(localIXMLElement);
      }
    }
    return localVector;
  }
  
  private XMLAttribute findAttribute(String paramString)
  {
    Enumeration localEnumeration = this.attributes.elements();
    while (localEnumeration.hasMoreElements())
    {
      XMLAttribute localXMLAttribute = (XMLAttribute)localEnumeration.nextElement();
      if (localXMLAttribute.getFullName().equals(paramString)) {
        return localXMLAttribute;
      }
    }
    return null;
  }
  
  private XMLAttribute findAttribute(String paramString1, String paramString2)
  {
    Enumeration localEnumeration = this.attributes.elements();
    while (localEnumeration.hasMoreElements())
    {
      XMLAttribute localXMLAttribute = (XMLAttribute)localEnumeration.nextElement();
      boolean bool = localXMLAttribute.getName().equals(paramString1);
      if (paramString2 == null) {
        bool &= localXMLAttribute.getNamespace() == null;
      } else {
        bool &= paramString2.equals(localXMLAttribute.getNamespace());
      }
      if (bool) {
        return localXMLAttribute;
      }
    }
    return null;
  }
  
  public int getAttributeCount()
  {
    return this.attributes.size();
  }
  
  /**
   * @deprecated
   */
  public String getAttribute(String paramString)
  {
    return getAttribute(paramString, null);
  }
  
  public String getAttribute(String paramString1, String paramString2)
  {
    XMLAttribute localXMLAttribute = findAttribute(paramString1);
    if (localXMLAttribute == null) {
      return paramString2;
    }
    return localXMLAttribute.getValue();
  }
  
  public String getAttribute(String paramString1, String paramString2, String paramString3)
  {
    XMLAttribute localXMLAttribute = findAttribute(paramString1, paramString2);
    if (localXMLAttribute == null) {
      return paramString3;
    }
    return localXMLAttribute.getValue();
  }
  
  public int getAttribute(String paramString, int paramInt)
  {
    String str = getAttribute(paramString, Integer.toString(paramInt));
    return Integer.parseInt(str);
  }
  
  public int getAttribute(String paramString1, String paramString2, int paramInt)
  {
    String str = getAttribute(paramString1, paramString2, Integer.toString(paramInt));
    return Integer.parseInt(str);
  }
  
  public String getAttributeType(String paramString)
  {
    XMLAttribute localXMLAttribute = findAttribute(paramString);
    if (localXMLAttribute == null) {
      return null;
    }
    return localXMLAttribute.getType();
  }
  
  public String getAttributeNamespace(String paramString)
  {
    XMLAttribute localXMLAttribute = findAttribute(paramString);
    if (localXMLAttribute == null) {
      return null;
    }
    return localXMLAttribute.getNamespace();
  }
  
  public String getAttributeType(String paramString1, String paramString2)
  {
    XMLAttribute localXMLAttribute = findAttribute(paramString1, paramString2);
    if (localXMLAttribute == null) {
      return null;
    }
    return localXMLAttribute.getType();
  }
  
  public void setAttribute(String paramString1, String paramString2)
  {
    XMLAttribute localXMLAttribute = findAttribute(paramString1);
    if (localXMLAttribute == null)
    {
      localXMLAttribute = new XMLAttribute(paramString1, paramString1, null, paramString2, "CDATA");
      this.attributes.addElement(localXMLAttribute);
    }
    else
    {
      localXMLAttribute.setValue(paramString2);
    }
  }
  
  public void setAttribute(String paramString1, String paramString2, String paramString3)
  {
    int i = paramString1.indexOf(':');
    String str = paramString1.substring(i + 1);
    XMLAttribute localXMLAttribute = findAttribute(str, paramString2);
    if (localXMLAttribute == null)
    {
      localXMLAttribute = new XMLAttribute(paramString1, str, paramString2, paramString3, "CDATA");
      this.attributes.addElement(localXMLAttribute);
    }
    else
    {
      localXMLAttribute.setValue(paramString3);
    }
  }
  
  public void removeAttribute(String paramString)
  {
    for (int i = 0; i < this.attributes.size(); i++)
    {
      XMLAttribute localXMLAttribute = (XMLAttribute)this.attributes.elementAt(i);
      if (localXMLAttribute.getFullName().equals(paramString))
      {
        this.attributes.removeElementAt(i);
        return;
      }
    }
  }
  
  public void removeAttribute(String paramString1, String paramString2)
  {
    for (int i = 0; i < this.attributes.size(); i++)
    {
      XMLAttribute localXMLAttribute = (XMLAttribute)this.attributes.elementAt(i);
      boolean bool = localXMLAttribute.getName().equals(paramString1);
      if (paramString2 == null) {
        bool &= localXMLAttribute.getNamespace() == null;
      } else {
        bool &= localXMLAttribute.getNamespace().equals(paramString2);
      }
      if (bool)
      {
        this.attributes.removeElementAt(i);
        return;
      }
    }
  }
  
  public Enumeration enumerateAttributeNames()
  {
    Vector localVector = new Vector();
    Enumeration localEnumeration = this.attributes.elements();
    while (localEnumeration.hasMoreElements())
    {
      XMLAttribute localXMLAttribute = (XMLAttribute)localEnumeration.nextElement();
      localVector.addElement(localXMLAttribute.getFullName());
    }
    return localVector.elements();
  }
  
  public boolean hasAttribute(String paramString)
  {
    return findAttribute(paramString) != null;
  }
  
  public boolean hasAttribute(String paramString1, String paramString2)
  {
    return findAttribute(paramString1, paramString2) != null;
  }
  
  public Properties getAttributes()
  {
    Properties localProperties = new Properties();
    Enumeration localEnumeration = this.attributes.elements();
    while (localEnumeration.hasMoreElements())
    {
      XMLAttribute localXMLAttribute = (XMLAttribute)localEnumeration.nextElement();
      localProperties.put(localXMLAttribute.getFullName(), localXMLAttribute.getValue());
    }
    return localProperties;
  }
  
  public Properties getAttributesInNamespace(String paramString)
  {
    Properties localProperties = new Properties();
    Enumeration localEnumeration = this.attributes.elements();
    while (localEnumeration.hasMoreElements())
    {
      XMLAttribute localXMLAttribute = (XMLAttribute)localEnumeration.nextElement();
      if (paramString == null)
      {
        if (localXMLAttribute.getNamespace() == null) {
          localProperties.put(localXMLAttribute.getName(), localXMLAttribute.getValue());
        }
      }
      else if (paramString.equals(localXMLAttribute.getNamespace())) {
        localProperties.put(localXMLAttribute.getName(), localXMLAttribute.getValue());
      }
    }
    return localProperties;
  }
  
  public String getSystemID()
  {
    return this.systemID;
  }
  
  public int getLineNr()
  {
    return this.lineNr;
  }
  
  public String getContent()
  {
    return this.content;
  }
  
  public void setContent(String paramString)
  {
    this.content = paramString;
  }
  
  public boolean equals(Object paramObject)
  {
    try
    {
      return equalsXMLElement((IXMLElement)paramObject);
    }
    catch (ClassCastException localClassCastException) {}
    return false;
  }
  
  public boolean equalsXMLElement(IXMLElement paramIXMLElement)
  {
    if (!this.name.equals(paramIXMLElement.getName())) {
      return false;
    }
    if (this.attributes.size() != paramIXMLElement.getAttributeCount()) {
      return false;
    }
    Enumeration localEnumeration = this.attributes.elements();
    Object localObject1;
    Object localObject2;
    while (localEnumeration.hasMoreElements())
    {
      XMLAttribute localXMLAttribute = (XMLAttribute)localEnumeration.nextElement();
      if (!paramIXMLElement.hasAttribute(localXMLAttribute.getName(), localXMLAttribute.getNamespace())) {
        return false;
      }
      localObject1 = paramIXMLElement.getAttribute(localXMLAttribute.getName(), localXMLAttribute.getNamespace(), null);
      if (!localXMLAttribute.getValue().equals(localObject1)) {
        return false;
      }
      localObject2 = paramIXMLElement.getAttributeType(localXMLAttribute.getName(), localXMLAttribute.getNamespace());
      if (!localXMLAttribute.getType().equals(localObject2)) {
        return false;
      }
    }
    if (this.children.size() != paramIXMLElement.getChildrenCount()) {
      return false;
    }
    for (int i = 0; i < this.children.size(); i++)
    {
      localObject1 = getChildAtIndex(i);
      localObject2 = paramIXMLElement.getChildAtIndex(i);
      if (!((IXMLElement)localObject1).equalsXMLElement((IXMLElement)localObject2)) {
        return false;
      }
    }
    return true;
  }
}


/* Location:              C:\Users\sam\Downloads\nanoxml-2.2.3.jar!\net\n3\nanoxml\XMLElement.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */