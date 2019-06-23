package net.n3.nanoxml;

import java.io.IOException;
import java.io.Reader;
import java.util.Stack;
import java.util.Vector;

public class StdXMLBuilder
  implements IXMLBuilder
{
  private Stack stack = null;
  private IXMLElement root = null;
  private IXMLElement prototype;
  
  public StdXMLBuilder()
  {
    this(new XMLElement());
  }
  
  public StdXMLBuilder(IXMLElement paramIXMLElement)
  {
    this.prototype = paramIXMLElement;
  }
  
  protected void finalize()
    throws Throwable
  {
    this.prototype = null;
    this.root = null;
    this.stack.clear();
    this.stack = null;
    super.finalize();
  }
  
  public void startBuilding(String paramString, int paramInt)
  {
    this.stack = new Stack();
    this.root = null;
  }
  
  public void newProcessingInstruction(String paramString, Reader paramReader) {}
  
  public void startElement(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt)
  {
    String str = paramString1;
    if (paramString2 != null) {
      str = paramString2 + ':' + paramString1;
    }
    IXMLElement localIXMLElement1 = this.prototype.createElement(str, paramString3, paramString4, paramInt);
    if (this.stack.empty())
    {
      this.root = localIXMLElement1;
    }
    else
    {
      IXMLElement localIXMLElement2 = (IXMLElement)this.stack.peek();
      localIXMLElement2.addChild(localIXMLElement1);
    }
    this.stack.push(localIXMLElement1);
  }
  
  public void elementAttributesProcessed(String paramString1, String paramString2, String paramString3) {}
  
  public void endElement(String paramString1, String paramString2, String paramString3)
  {
    IXMLElement localIXMLElement1 = (IXMLElement)this.stack.pop();
    if (localIXMLElement1.getChildrenCount() == 1)
    {
      IXMLElement localIXMLElement2 = localIXMLElement1.getChildAtIndex(0);
      if (localIXMLElement2.getName() == null)
      {
        localIXMLElement1.setContent(localIXMLElement2.getContent());
        localIXMLElement1.removeChildAtIndex(0);
      }
    }
  }
  
  public void addAttribute(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws Exception
  {
    String str = paramString1;
    if (paramString2 != null) {
      str = paramString2 + ':' + paramString1;
    }
    IXMLElement localIXMLElement = (IXMLElement)this.stack.peek();
    if (localIXMLElement.hasAttribute(str)) {
      throw new XMLParseException(localIXMLElement.getSystemID(), localIXMLElement.getLineNr(), "Duplicate attribute: " + paramString1);
    }
    if (paramString2 != null) {
      localIXMLElement.setAttribute(str, paramString3, paramString4);
    } else {
      localIXMLElement.setAttribute(str, paramString4);
    }
  }
  
  public void addPCData(Reader paramReader, String paramString, int paramInt)
  {
    int i = 2048;
    int j = 0;
    StringBuffer localStringBuffer = new StringBuffer(i);
    char[] arrayOfChar = new char[i];
    for (;;)
    {
      if (j >= i)
      {
        i *= 2;
        localStringBuffer.ensureCapacity(i);
      }
      int k;
      try
      {
        k = paramReader.read(arrayOfChar);
      }
      catch (IOException localIOException)
      {
        break;
      }
      if (k < 0) {
        break;
      }
      localStringBuffer.append(arrayOfChar, 0, k);
      j += k;
    }
    IXMLElement localIXMLElement1 = this.prototype.createElement(null, paramString, paramInt);
    localIXMLElement1.setContent(localStringBuffer.toString());
    if (!this.stack.empty())
    {
      IXMLElement localIXMLElement2 = (IXMLElement)this.stack.peek();
      localIXMLElement2.addChild(localIXMLElement1);
    }
  }
  
  public Object getResult()
  {
    return this.root;
  }
}


/* Location:              C:\Users\sam\Downloads\nanoxml-2.2.3.jar!\net\n3\nanoxml\StdXMLBuilder.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */