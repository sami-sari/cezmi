package com.samisari;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;

import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamViewer;

public class WebCamTest extends WebcamViewer {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	BufferedImage				currentImage;
	JButton						snap;

	public WebCamTest() {
		super();
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		snap = new JButton("Snap");
		snap.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				snapshot();
			}
		});
		add(snap, BorderLayout.SOUTH);
		pack();
	}
	@Override
	public void webcamImageObtained(WebcamEvent we) {
		super.webcamImageObtained(we);
		currentImage = we.getImage();
	}

	public void snapshot() {
		FileOutputStream os = null;
		try {
			os = new FileOutputStream("/home/sam/Desktop/test.jpg");
			ImageIO.write(currentImage, "JPEG", os);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
			}
		}
	}

	public static void main(String[] args) {
		WebCamTest viewer = new WebCamTest();
		viewer.run();

	}
}
