package com.samisari.cezmi.animator.gui;

import java.util.Vector;

import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.format.VideoFormat;

public class VideoCapture {
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		//VideoFormat format = new VideoFormat(VideoFormat.YUV);
		//Vector list = CaptureDeviceManager.getDeviceList(null);
		CaptureDeviceInfo info = CaptureDeviceManager.getDevice("Integrated Webcam");
		System.out.println(info);
	}
}
