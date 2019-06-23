package com.samisari.cezmi.networkcommands.actions;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class TextAreaInputStream  extends InputStream implements  DocumentListener {
	JTextArea textArea;
	Queue<Byte> inputQueue;

	public TextAreaInputStream(JTextArea textArea) {
		this.textArea = textArea;
		inputQueue = new LinkedBlockingQueue<Byte>();
		textArea.getDocument().addDocumentListener(this);
	}

	@Override
	public synchronized int read() throws IOException {
		try {
			if (inputQueue.isEmpty())
				this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Byte b = inputQueue.remove();
		System.out.println(b.byteValue());
		return (int) b.byteValue();
	}

	@Override
	public synchronized void changedUpdate(DocumentEvent e) {

	}

	@Override
	public synchronized void insertUpdate(DocumentEvent e) {
		int length = e.getLength();
		if (length == 1) {
			Document doc = e.getDocument();
			try {
				String change = doc.getText(doc.getLength() - length, length);
				byte[] bytes = change.getBytes("UTF-8");
				for (byte b : bytes) {
					if (b==10)
						inputQueue.add((byte)13);
					inputQueue.add(b);
				}
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.notify();
		}
	}

	@Override
	public synchronized void removeUpdate(DocumentEvent e) {

	}

}
