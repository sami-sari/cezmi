package com.samisari.cezmi.animator.sound;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JOptionPane;

public class SoundRecorder {

	TargetDataLine	line;
	File			file;

	public SoundRecorder(File file) {
		this.file = file;
	}

	public AudioFormat getAudioFormat() {
		//javax.sound.sampled.AudioFileFormat.Type[] audioFileTypes = AudioSystem.getAudioFileTypes();
		
		float sampleRate = 16000;
		int sampleSizeInBits = 16;
		int channels = 2;
		boolean signed = true;
		boolean bigEndian = true;
		
//		float sampleRate = 44100;
//		int sampleSizeInBits = 32;
//		int channels = 2;
//		boolean signed = false;
//		boolean bigEndian = true;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

	public void record() {
		AudioFormat format = getAudioFormat();
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format); // format
																				// is
																				// an
																				// AudioFormat
																				// object
		if (!AudioSystem.isLineSupported(info)) {
			JOptionPane.showMessageDialog(null, "Format Not Supported");// Handle
																		// the
																		// error
																		// ...
																		//System.exit(0);
		}
		// Obtain and open the line.
		try {
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
			line.start();
			AudioInputStream ais = new AudioInputStream(line);
			AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
		} catch (LineUnavailableException ex) {
			// Handle the error ...
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void finish() {
		if (line != null) {
			line.stop();
			line.close();
		}
	}

	public static void main(String[] args) throws IOException {
		/*
		File file = null;
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("/home/sam/Cezmi"));
		
		if (JFileChooser.APPROVE_OPTION == fc.showSaveDialog(null)) {
			file = fc.getSelectedFile();
		}
		final SoundRecorder r = new SoundRecorder(file);
		javax.swing.Timer timer = new javax.swing.Timer(60000, new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				r.finish();
		
			}
		});
		timer.start();
		r.record();
		*/
		AudioFileFormat.Type[] audioFileFormats = AudioSystem.getAudioFileTypes();
		for (AudioFileFormat.Type audioFileFormat:audioFileFormats) {
			System.out.println(audioFileFormat.getExtension());
		}
		System.out.println();
		System.out.println();
		System.out.println();
		
		if (AudioSystem.isLineSupported(Port.Info.MICROPHONE)) {
			try {
				Line line = AudioSystem.getLine(Port.Info.MICROPHONE);
				System.out.println(line.getLineInfo());
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		}
		System.out.println();
		System.out.println();
		System.out.println();
		
		
		Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
		for (Mixer.Info mixerInfo : mixerInfos) {
			System.out.println(mixerInfo);
			Mixer mixer = AudioSystem.getMixer(mixerInfo);
			Line.Info[] lineInfos = mixer.getSourceLineInfo();
			for (Line.Info lineInfo : lineInfos) {
				System.out.println(String.format("      %1$s", lineInfo.toString()));
				try {
					Line line = AudioSystem.getLine(lineInfo);
					
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				}
			}
			mixer.close();
		}

	}

}
