package net.n3.nanoxml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PushbackInputStream;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;
import java.util.Vector;

public class StdXMLReader implements IXMLReader {
	private Stack			readers;
	private StackedReader	currentReader;

	public static IXMLReader stringReader(String paramString) {
		return new StdXMLReader(new StringReader(paramString));
	}

	public static IXMLReader fileReader(String paramString) throws FileNotFoundException, IOException {
		StdXMLReader localStdXMLReader = new StdXMLReader(new FileInputStream(paramString));
		localStdXMLReader.setSystemID(paramString);
		for (int i = 0; i < localStdXMLReader.readers.size(); i++) {
			StackedReader localStackedReader = (StackedReader) localStdXMLReader.readers.elementAt(i);
			localStackedReader.systemId = localStdXMLReader.currentReader.systemId;
		}
		return localStdXMLReader;
	}

	public StdXMLReader(String paramString1, String paramString2) throws MalformedURLException, FileNotFoundException, IOException {
		URL localURL = null;
		try {
			localURL = new URL(paramString2);
		} catch (MalformedURLException localMalformedURLException1) {
			paramString2 = "file:" + paramString2;
			try {
				localURL = new URL(paramString2);
			} catch (MalformedURLException localMalformedURLException2) {
				throw localMalformedURLException1;
			}
		}
		this.currentReader = new StackedReader();
		this.readers = new Stack();
		Reader localReader = openStream(paramString1, localURL.toString());
		this.currentReader.lineReader = new LineNumberReader(localReader);
		this.currentReader.pbReader = new PushbackReader(this.currentReader.lineReader, 2);
	}

	public StdXMLReader(Reader paramReader) {
		this.currentReader = new StackedReader();
		this.readers = new Stack();
		this.currentReader.lineReader = new LineNumberReader(paramReader);
		this.currentReader.pbReader = new PushbackReader(this.currentReader.lineReader, 2);
		this.currentReader.publicId = "";
		try {
			this.currentReader.systemId = new URL("file:.");
		} catch (MalformedURLException localMalformedURLException) {
		}
	}

	protected void finalize() throws Throwable {
		this.currentReader.lineReader = null;
		this.currentReader.pbReader = null;
		this.currentReader.systemId = null;
		this.currentReader.publicId = null;
		this.currentReader = null;
		this.readers.clear();
		super.finalize();
	}

	protected String getEncoding(String paramString) {
		if (!paramString.startsWith("<?xml")) {
			return null;
		}
		int k;
		for (int i = 5; i < paramString.length(); i = k + 1) {
			StringBuffer localStringBuffer = new StringBuffer();
			for (;;) {
				i++;
				if (i < paramString.length()) {
					if (paramString.charAt(i) > ' ') {
						break;
					}
				}
			}
			do {
				localStringBuffer.append(paramString.charAt(i));
				i++;
				if ((i >= paramString.length()) || (paramString.charAt(i) < 'a')) {
					break;
				}
			} while (paramString.charAt(i) <= 'z');
			while ((i < paramString.length()) && (paramString.charAt(i) <= ' ')) {
				i++;
			}
			if (i >= paramString.length()) {
				break;
			}
			if (paramString.charAt(i) != '=') {
				break;
			}
			while ((i < paramString.length()) && (paramString.charAt(i) != '\'') && (paramString.charAt(i) != '"')) {
				i++;
			}
			if (i >= paramString.length()) {
				break;
			}
			int j = paramString.charAt(i);
			i++;
			k = paramString.indexOf(j, i);
			if (k < 0) {
				break;
			}
			if (localStringBuffer.toString().equals("encoding")) {
				return paramString.substring(i, k);
			}
		}
		return null;
	}

	protected Reader stream2reader(InputStream paramInputStream, StringBuffer paramStringBuffer) throws IOException {
		PushbackInputStream localPushbackInputStream = new PushbackInputStream(paramInputStream);
		int i = localPushbackInputStream.read();
		switch (i) {
		case 0:
		case 254:
		case 255:
			localPushbackInputStream.unread(i);
			return new InputStreamReader(localPushbackInputStream, "UTF-16");
		case 239:
			for (int j = 0; j < 2; j++) {
				localPushbackInputStream.read();
			}
			return new InputStreamReader(localPushbackInputStream, "UTF-8");
		case 60:
			i = localPushbackInputStream.read();
			paramStringBuffer.append('<');
			while ((i > 0) && (i != 62)) {
				paramStringBuffer.append((char) i);
				i = localPushbackInputStream.read();
			}
			if (i > 0) {
				paramStringBuffer.append((char) i);
			}
			String str = getEncoding(paramStringBuffer.toString());
			if (str == null) {
				return new InputStreamReader(localPushbackInputStream, "UTF-8");
			}
			paramStringBuffer.setLength(0);
			try {
				return new InputStreamReader(localPushbackInputStream, str);
			} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
				return new InputStreamReader(localPushbackInputStream, "UTF-8");
			}
		}
		paramStringBuffer.append((char) i);
		return new InputStreamReader(localPushbackInputStream, "UTF-8");
	}

	public StdXMLReader(InputStream paramInputStream) throws IOException {
		PushbackInputStream localPushbackInputStream = new PushbackInputStream(paramInputStream);
		StringBuffer localStringBuffer = new StringBuffer();
		Reader localReader = stream2reader(paramInputStream, localStringBuffer);
		this.currentReader = new StackedReader();
		this.readers = new Stack();
		this.currentReader.lineReader = new LineNumberReader(localReader);
		this.currentReader.pbReader = new PushbackReader(this.currentReader.lineReader, 2);
		this.currentReader.publicId = "";
		try {
			this.currentReader.systemId = new URL("file:.");
		} catch (MalformedURLException localMalformedURLException) {
		}
		startNewStream(new StringReader(localStringBuffer.toString()));
	}

	public char read() throws IOException {
		int i = this.currentReader.pbReader.read();
		for (; i < 0; i = this.currentReader.pbReader.read()) {
			if (this.readers.empty()) {
				throw new IOException("Unexpected EOF");
			}
			this.currentReader.pbReader.close();
			this.currentReader = ((StackedReader) this.readers.pop());
		}
		return (char) i;
	}

	public boolean atEOFOfCurrentStream() throws IOException {
		int i = this.currentReader.pbReader.read();
		if (i < 0) {
			return true;
		}
		this.currentReader.pbReader.unread(i);
		return false;
	}

	public boolean atEOF() throws IOException {
		int i = this.currentReader.pbReader.read();
		for (; i < 0; i = this.currentReader.pbReader.read()) {
			if (this.readers.empty()) {
				return true;
			}
			this.currentReader.pbReader.close();
			this.currentReader = ((StackedReader) this.readers.pop());
		}
		this.currentReader.pbReader.unread(i);
		return false;
	}

	public void unread(char paramChar) throws IOException {
		this.currentReader.pbReader.unread(paramChar);
	}

	public Reader openStream(String paramString1, String paramString2) throws MalformedURLException, FileNotFoundException, IOException {
		URL localURL = new URL(this.currentReader.systemId, paramString2);
		Object localObject;
		if (localURL.getRef() != null) {
			localObject = localURL.getRef();
			if (localURL.getFile().length() > 0) {
				localURL = new URL(localURL.getProtocol(), localURL.getHost(), localURL.getPort(), localURL.getFile());
				localURL = new URL("jar:" + localURL + '!' + (String) localObject);
			} else {
				localURL = StdXMLReader.class.getResource((String) localObject);
			}
		}
		this.currentReader.publicId = paramString1;
		this.currentReader.systemId = localURL;
		localObject = new StringBuffer();
		Reader localReader = stream2reader(localURL.openStream(), (StringBuffer) localObject);
		if (((StringBuffer) localObject).length() == 0) {
			return localReader;
		}
		String str = ((StringBuffer) localObject).toString();
		PushbackReader localPushbackReader = new PushbackReader(localReader, str.length());
		for (int i = str.length() - 1; i >= 0; i--) {
			localPushbackReader.unread(str.charAt(i));
		}
		return localPushbackReader;
	}

	public void startNewStream(Reader paramReader) {
		startNewStream(paramReader, false);
	}

	public void startNewStream(Reader paramReader, boolean paramBoolean) {
		StackedReader localStackedReader = this.currentReader;
		this.readers.push(this.currentReader);
		this.currentReader = new StackedReader();
		if (paramBoolean) {
			this.currentReader.lineReader = null;
			this.currentReader.pbReader = new PushbackReader(paramReader, 2);
		} else {
			this.currentReader.lineReader = new LineNumberReader(paramReader);
			this.currentReader.pbReader = new PushbackReader(this.currentReader.lineReader, 2);
		}
		this.currentReader.systemId = localStackedReader.systemId;
		this.currentReader.publicId = localStackedReader.publicId;
	}

	public int getStreamLevel() {
		return this.readers.size();
	}

	public int getLineNr() {
		if (this.currentReader.lineReader == null) {
			StackedReader localStackedReader = (StackedReader) this.readers.peek();
			if (localStackedReader.lineReader == null) {
				return 0;
			}
			return localStackedReader.lineReader.getLineNumber() + 1;
		}
		return this.currentReader.lineReader.getLineNumber() + 1;
	}

	public void setSystemID(String paramString) throws MalformedURLException {
		this.currentReader.systemId = new URL(this.currentReader.systemId, paramString);
	}

	public void setPublicID(String paramString) {
		this.currentReader.publicId = paramString;
	}

	public String getSystemID() {
		return this.currentReader.systemId.toString();
	}

	public String getPublicID() {
		return this.currentReader.publicId;
	}

	private class StackedReader {
		PushbackReader		pbReader;
		LineNumberReader	lineReader;
		URL					systemId;
		String				publicId;

		private StackedReader() {
		}

		StackedReader(StdXMLReader param1) {
			this();
		}
	}
}

/* Location:              C:\Users\sam\Downloads\nanoxml-2.2.3.jar!\net\n3\nanoxml\StdXMLReader.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */