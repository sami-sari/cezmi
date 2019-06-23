package net.n3.nanoxml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;

public class XMLWriter
{
  private PrintWriter writer;
  
  public XMLWriter(Writer paramWriter)
  {
    if ((paramWriter instanceof PrintWriter)) {
      this.writer = ((PrintWriter)paramWriter);
    } else {
      this.writer = new PrintWriter(paramWriter);
    }
  }
  
  public XMLWriter(OutputStream paramOutputStream)
  {
    this.writer = new PrintWriter(paramOutputStream);
  }
  
  protected void finalize()
    throws Throwable
  {
    this.writer = null;
    super.finalize();
  }
  
  public void write(IXMLElement paramIXMLElement)
    throws IOException
  {
    write(paramIXMLElement, false, 0, true);
  }
  
  public void write(IXMLElement paramIXMLElement, boolean paramBoolean)
    throws IOException
  {
    write(paramIXMLElement, paramBoolean, 0, true);
  }
  
  public void write(IXMLElement paramIXMLElement, boolean paramBoolean, int paramInt)
    throws IOException
  {
    write(paramIXMLElement, paramBoolean, paramInt, true);
  }
  
  public void write(IXMLElement paramIXMLElement, boolean paramBoolean1, int paramInt, boolean paramBoolean2)
    throws IOException
  {
    if (paramBoolean1) {
      for (int i = 0; i < paramInt; i++) {
        this.writer.print(' ');
      }
    }
    if (paramIXMLElement.getName() == null)
    {
      if (paramIXMLElement.getContent() != null) {
        if (paramBoolean1)
        {
          writeEncoded(paramIXMLElement.getContent().trim());
          this.writer.println();
        }
        else
        {
          writeEncoded(paramIXMLElement.getContent());
        }
      }
    }
    else
    {
      this.writer.print('<');
      this.writer.print(paramIXMLElement.getFullName());
      Vector localVector = new Vector();
      Object localObject1;
      if (paramIXMLElement.getNamespace() != null) {
        if (paramIXMLElement.getName().equals(paramIXMLElement.getFullName()))
        {
          this.writer.print(" xmlns=\"" + paramIXMLElement.getNamespace() + '"');
        }
        else
        {
          localObject1 = paramIXMLElement.getFullName();
          localObject1 = ((String)localObject1).substring(0, ((String)localObject1).indexOf(':'));
          localVector.addElement(localObject1);
          this.writer.print(" xmlns:" + (String)localObject1);
          this.writer.print("=\"" + paramIXMLElement.getNamespace() + "\"");
        }
      }
      localObject1 = paramIXMLElement.enumerateAttributeNames();
      Object localObject2;
      while (((Enumeration)localObject1).hasMoreElements())
      {
        localObject2 = (String)((Enumeration)localObject1).nextElement();
        int k = ((String)localObject2).indexOf(':');
        if (k >= 0)
        {
          String str2 = paramIXMLElement.getAttributeNamespace((String)localObject2);
          if (str2 != null)
          {
            String str3 = ((String)localObject2).substring(0, k);
            if (!localVector.contains(str3))
            {
              this.writer.print(" xmlns:" + str3);
              this.writer.print("=\"" + str2 + '"');
              localVector.addElement(str3);
            }
          }
        }
      }
      localObject1 = paramIXMLElement.enumerateAttributeNames();
      while (((Enumeration)localObject1).hasMoreElements())
      {
        localObject2 = (String)((Enumeration)localObject1).nextElement();
        String str1 = paramIXMLElement.getAttribute((String)localObject2, null);
        this.writer.print(" " + (String)localObject2 + "=\"");
        writeEncoded(str1);
        this.writer.print('"');
      }
      if ((paramIXMLElement.getContent() != null) && (paramIXMLElement.getContent().length() > 0))
      {
        this.writer.print('>');
        writeEncoded(paramIXMLElement.getContent());
        this.writer.print("</" + paramIXMLElement.getFullName() + '>');
        if (paramBoolean1) {
          this.writer.println();
        }
      }
      else if ((paramIXMLElement.hasChildren()) || (!paramBoolean2))
      {
        this.writer.print('>');
        if (paramBoolean1) {
          this.writer.println();
        }
        localObject1 = paramIXMLElement.enumerateChildren();
        while (((Enumeration)localObject1).hasMoreElements())
        {
          localObject2 = (IXMLElement)((Enumeration)localObject1).nextElement();
          write((IXMLElement)localObject2, paramBoolean1, paramInt + 4, paramBoolean2);
        }
        if (paramBoolean1) {
          for (int j = 0; j < paramInt; j++) {
            this.writer.print(' ');
          }
        }
        this.writer.print("</" + paramIXMLElement.getFullName() + ">");
        if (paramBoolean1) {
          this.writer.println();
        }
      }
      else
      {
        this.writer.print("/>");
        if (paramBoolean1) {
          this.writer.println();
        }
      }
    }
    this.writer.flush();
  }
  
  private void writeEncoded(String paramString)
  {
    for (int i = 0; i < paramString.length(); i++)
    {
      char c = paramString.charAt(i);
      switch (c)
      {
      case '\n': 
        this.writer.print(c);
        break;
      case '<': 
        this.writer.print("&lt;");
        break;
      case '>': 
        this.writer.print("&gt;");
        break;
      case '&': 
        this.writer.print("&amp;");
        break;
      case '\'': 
        this.writer.print("&apos;");
        break;
      case '"': 
        this.writer.print("&quot;");
        break;
      default: 
        if ((c < ' ') || (c > '~'))
        {
          this.writer.print("&#x");
          this.writer.print(Integer.toString(c, 16));
          this.writer.print(';');
        }
        else
        {
          this.writer.print(c);
        }
        break;
      }
    }
  }
}


/* Location:              C:\Users\sam\Downloads\nanoxml-2.2.3.jar!\net\n3\nanoxml\XMLWriter.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */