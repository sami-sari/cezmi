package com.samisari.cezmi.animator.sound;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFileChooser;

public class SoundPlayer {

	AudioInputStream			in;

	AudioFormat					decodedFormat;

	AudioInputStream			din;

	AudioFormat					baseFormat;

	SourceDataLine				line;

	private boolean				loop;

	private BufferedInputStream	stream;
	private byte[]				buffer;

	// private ByteArrayInputStream stream;

	/**
	 * recreate the stream
	 * 
	 */
	public void reset() {
		try {
			stream.close();
			stream = new BufferedInputStream(new ByteArrayInputStream(buffer));
			in = AudioSystem.getAudioInputStream(stream);
			din = AudioSystem.getAudioInputStream(decodedFormat, in);
			line = getLine(decodedFormat);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			line.close();
			din.close();
			in.close();
		} catch (IOException e) {
		}
	}

	SoundPlayer(String filename, boolean loop) {
		this(filename);
		this.loop = loop;
	}

	SoundPlayer(String filename) {
		this.loop = false;
		try {
			int size = (int) new File(filename).length();
			InputStream raw = new FileInputStream(filename);
			int ib = raw.read();
			buffer = new byte[size];
			for (int i = 0; ib != -1; i++) {
				buffer[i] = (byte) ib;
				ib = raw.read();
			}
			raw.close();
			ByteArrayInputStream bais = new ByteArrayInputStream(buffer);

			stream = new BufferedInputStream(bais);

			in = AudioSystem.getAudioInputStream(stream);
			din = null;

			if (in != null) {
				baseFormat = in.getFormat();

				decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
						baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);

				din = AudioSystem.getAudioInputStream(decodedFormat, in);
				line = getLine(decodedFormat);
			}
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException {
		SourceDataLine res = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		res = (SourceDataLine) AudioSystem.getLine(info);
		res.open(audioFormat);
		return res;
	}

	public void play() {

		try {
			boolean firstTime = true;
			while (firstTime || loop) {

				firstTime = false;
				byte[] data = new byte[4096];

				if (line != null) {

					line.start();
					int nBytesRead = 0;

					while (nBytesRead != -1) {
						nBytesRead = din.read(data, 0, data.length);
						if (nBytesRead != -1)
							line.write(data, 0, nBytesRead);
					}

					line.drain();
					line.stop();
					line.close();

					reset();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    public void finish() {
        line.stop();
        line.close();
    }

	public static void main(String[] args) {
		String filename = null;
		JFileChooser fc = new JFileChooser();
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			filename = fc.getSelectedFile().getAbsolutePath();
		}
		if (filename != null) {
			SoundPlayer sp = new SoundPlayer(filename);
			//sp.loop = true;
			sp.play();
		}
		// sp.play();
	}
}
