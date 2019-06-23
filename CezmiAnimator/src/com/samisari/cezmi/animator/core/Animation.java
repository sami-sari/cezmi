package com.samisari.cezmi.animator.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.samisari.cezmi.animator.animationcommands.CezmiAbstractTransformation;
import com.samisari.cezmi.animator.animationcommands.CezmiPropertyTransformation;
import com.samisari.cezmi.animator.animationcommands.CezmiRotation;
import com.samisari.cezmi.animator.animationcommands.CezmiScaling;
import com.samisari.cezmi.animator.animationcommands.CezmiTranslation;
import com.samisari.cezmi.animator.sound.SoundClipBoundary;
import com.samisari.cezmi.core.AbstractCommand;
import com.samisari.cezmi.core.CommandManager;
import com.samisari.cezmi.core.CommandProperty;
import com.samisari.cezmi.core.History;

public class Animation {
	private static final Logger											logger				= Logger.getLogger(Animation.class);
	private List<History>												views;
	private List<HashMap<String, List<CommandProperty>>>				changes;
	// Assets that form the basic scene
	private History														assets;
	// Transformations distributed among frames
	private HashMap<AbstractCommand, List<CezmiAbstractTransformation>>	transformations;

	// Key frames are snapshots of the scene at points of property changes
	private HashMap<String, List<KeyFrame>>								keyframes			= new HashMap<>();

	// storing the current view for every frame is very expensive in terms of memory 
	// so transformations will be extrapolated for every frame before starting to play the animation
	private History														currentView;
	private int															currentFrameNumber;
	private int															numberOfFrames;
	private int															framesPerSecond		= 60;
	private List<AnimationListener>										animationListeners	= new ArrayList<>();
	private int															selectionStart		= -1;
	private int															selectionEnd		= -1;
	private byte[]														animation;
	private List<SoundClipBoundary>										clips;
	private HashMap<Integer, List<String>>								frameScripts		= new HashMap<>();

	public Animation(int numberOfFrames) {
		views = new ArrayList<>();
		views.add((History) CommandManager.getDeaultInstance().getHistory().clone());
		setAssets((History) CommandManager.getDeaultInstance().getHistory().clone());
		setCurrentFrameNumber(0);
		setCurrentView(views.get(currentFrameNumber));
		setTransformations(new HashMap<AbstractCommand, List<CezmiAbstractTransformation>>());
		setNumberOfFrames(numberOfFrames);
	}

	public History getAssets() {
		return assets;
	}

	public void setAssets(History assets) {
		this.assets = assets;
	}

	public HashMap<AbstractCommand, List<CezmiAbstractTransformation>> getTransformations() {
		return transformations;
	}

	public void setTransformations(HashMap<AbstractCommand, List<CezmiAbstractTransformation>> transformations) {
		this.transformations = transformations;
	}

	public History getCurrentView() {
		int lastNonEmptyView = getCurrentFrameNumber();
		while (currentView == null) {
			currentView = getViews().get(lastNonEmptyView--);
		}
		return currentView;
	}

	public void setCurrentView(History currentView) {
		this.currentView = currentView;
		for (AnimationListener l : animationListeners) {
			l.onFrameChange();
		}
	}

	public int getCurrentFrameNumber() {
		return currentFrameNumber;
	}

	public void makeLinearTransformationList() {
		List<List<CezmiAbstractTransformation>> transformationList = new ArrayList<>();
		Set<Entry<AbstractCommand, List<CezmiAbstractTransformation>>> transformationSet = getTransformations().entrySet();
		Iterator<Entry<AbstractCommand, List<CezmiAbstractTransformation>>> iterator = transformationSet.iterator();
		while (iterator.hasNext()) {
			Entry<AbstractCommand, List<CezmiAbstractTransformation>> entry = iterator.next();
			for (CezmiAbstractTransformation transformation : entry.getValue()) {
				for (int i = transformation.getStartFrame(); i <= transformation.getEndFrame(); i++) {
					int duration = transformation.getEndFrame() - transformation.getStartFrame();
					if (transformation instanceof CezmiRotation) {
						double angle = ((CezmiRotation) transformation).getAngle() / (double) duration;
						transformationList.get(i).add(new CezmiRotation(transformation.getObject(), angle, ((CezmiRotation) transformation).getPivotPoint(), i, i));
					} else if (transformation instanceof CezmiTranslation) {
						double dx = ((CezmiTranslation) transformation).getDx() / (double) duration;
						double dy = ((CezmiTranslation) transformation).getDy() / (double) duration;
						transformationList.get(i).add(new CezmiTranslation(transformation.getObject(), dx, dy, i, i));
					} else if (transformation instanceof CezmiScaling) {
						double sx = ((CezmiScaling) transformation).getSx() / (double) duration;
						double sy = ((CezmiScaling) transformation).getSy() / (double) duration;
						transformationList.get(i).add(new CezmiScaling(transformation.getObject(), sx, sy, i, i));
					} else if (transformation instanceof CezmiPropertyTransformation) {
						if (i == transformation.getStartFrame()) {
							String name = ((CezmiPropertyTransformation) transformation).getPropertyName();
							Object newVal = ((CezmiPropertyTransformation) transformation).getNewValue();
							Object oldVal = ((CezmiPropertyTransformation) transformation).getOldValue();
							transformationList.get(i).add(new CezmiPropertyTransformation(transformation.getObject(), name, oldVal, newVal, i, i));
						}
					}
				}
			}
		}
	}

	//	public History makeFrame () {
	//		History view = new History();
	//		view = (History) getViews().get(0).clone();
	//		for (int i=0;i<=currentFrameNumber;i++) {
	//			getTransformations().get(i).
	//		}
	//		return view;
	//	}

	public void setCurrentFrameNumber(int currentFrameNumber) {
		if (currentFrameNumber < 0) {
			setCurrentFrameNumber(0);
			return;
		}
		if (views.size() <= currentFrameNumber || views.get(currentFrameNumber) == null) {
			while (views.size() <= currentFrameNumber) {
				views.add(assets);
			}
			if (views.get(currentFrameNumber) == null) {
				views.set(currentFrameNumber, assets);
			}
		}
		if (currentFrameNumber >= 0) {
			this.currentFrameNumber = currentFrameNumber;
			setCurrentView(views.get(currentFrameNumber));
		}

		// for (int i = 0; i < currentView.size(); i++) {
		// AbstractCommand cmd = currentView.get(i);
		// List<CezmiAbstractTransformation> tList = transformations.get(cmd);
		// if (tList != null)
		// for (int j = 0; j < tList.size(); j++) {
		// CezmiAbstractTransformation t = tList.get(j);
		// if (t.getStartFrame() <= getCurrentFrameNumber() && t.getEndFrame()
		// >= getCurrentFrameNumber()) {
		// t.transform();
		// }
		// }
		// }
	}

	public int getNumberOfFrames() {
		return numberOfFrames;
	}

	public void setNumberOfFrames(int numberOfFrames) {
		this.numberOfFrames = numberOfFrames;
	}

	public int getFramesPerSecond() {
		return framesPerSecond;
	}

	public void setFramesPerSecond(int framesPerSecond) {
		this.framesPerSecond = framesPerSecond;
	}

	public void addListener(AnimationListener listener) {
		animationListeners.add(listener);
	}

	public int getSelectionStart() {
		if (selectionStart < 0)
			return currentFrameNumber;
		return selectionStart;
	}

	public void setSelectionStart(int selectionStart) {
		this.selectionStart = selectionStart;
	}

	public int getSelectionEnd() {
		if (selectionEnd < 0)
			return currentFrameNumber;
		return selectionEnd;
	}

	public void setSelectionEnd(int selectionEnd) {
		this.selectionEnd = selectionEnd;
	}

	public List<History> getViews() {
		return views;
	}

	public void setViews(List<History> views) {
		this.views = views;
	}

	public void load() {
		byte[] startFrameBytes = "<frame>".getBytes();
		byte[] endFrameBytes = "</frame>".getBytes();
		int pos = 0;
		int endPos = 0;
		List<Integer> startPositions = new ArrayList<Integer>();
		List<Integer> endPositions = new ArrayList<Integer>();

		for (int i = 0; i < animation.length; i++) {
			if (endPositions.size() < startPositions.size()) {
				if (animation[i] == endFrameBytes[endPos]) {
					if (endPos == endFrameBytes.length - 1) {
						endPositions.add(i - endFrameBytes.length);
						endPos = 0;
					} else
						endPos++;
				} else
					endPos = 0;
			} else {
				if (animation[i] == startFrameBytes[pos]) {
					if (pos == startFrameBytes.length - 1) {
						startPositions.add(i + 1);
						pos = 0;
					} else
						pos++;
				} else
					pos = 0;
			}
		}

		views.clear();
		for (int i = 0; i < startPositions.size(); i++) {
			ByteArrayInputStream is = new ByteArrayInputStream(Arrays.copyOfRange(animation, startPositions.get(i), endPositions.get(i)));
			InputStreamReader in = new InputStreamReader(is);
			History history = new History();
			int c;
			String text = "";
			try {
				while ((c = in.read()) >= 0) {
					text = text + (char) c;
					logger.debug(text);
					if (text.endsWith(">")) {
						String className = text.substring(text.indexOf("<") + 1, text.indexOf(">"));
						logger.debug("PARSING\n\tClass " + className);
						AbstractCommand command = null;
						try {
							command = (AbstractCommand) Class.forName(className).newInstance();
						} catch (Exception e) {
							logger.error("Komut Sınıfı bilnmiyor!" + className);
						}
						if (command != null) {
							try {
								command.deserialise(in);
							} catch (Exception e) {
								logger.error("gele", e);
							}
							history.add(command);
							text = "";
						}
					}
				}
			} catch (Exception e) {
				logger.error("Dosya okunamadı!", e);
			}
			views.add((History) history);
		}
		logger.warn("Done loading");
	}

	public void save() {
		//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//		for (History v : getViews()) {
		//			v.serialise(baos);
		//		}
		//		animation = baos.toByteArray();
		try {
			_save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void _saveKeyFrames() {

	}

	public AbstractCommand interpolateFrame(int frameNum, KeyFrame start, KeyFrame end) {
		return null;

	}

	public void applyPath() {

	}

	public void _save() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final History assets = getAssets();
		baos.write("<assets>".getBytes());
		assets.serialise(baos);
		baos.write("</assets>".getBytes());
		baos.write("<frames>".getBytes());
		for (int i = 0; i < getViews().size(); i++) {
			History v = getViews().get(i);
			baos.write(("<frame><num>" + i + "</num>").getBytes());
			for (AbstractCommand cmd : v) {
				AbstractCommand original = assets.getCommand(cmd.getId());
				if (!cmd.getBounds().equals(original.getBounds())) {
					baos.write(("<cmd><id>" + cmd.getId() + "</id><dbounds><x>" + (cmd.getBounds().x - original.getBounds().x) + "</x>").getBytes());
					baos.write(("<y>" + (cmd.getBounds().y - original.getBounds().y) + "</y>").getBytes());
					baos.write(("<width>" + (cmd.getBounds().width - original.getBounds().width) + "</width>").getBytes());
					baos.write(("<height>" + (cmd.getBounds().height - original.getBounds().height) + "</height>").getBytes());
					baos.write("</dbounds></cmd>".getBytes());
				}
			}
			baos.write("</frame>".getBytes());
		}
		baos.write("</frames>".getBytes());
		animation = baos.toByteArray();
	}

	public void setAnimation(byte[] animation) {
		this.animation = animation;
	}

	public byte[] getAnimation() {
		return animation;
	}

	private void save(final OutputStream os, final List<History> frames) throws IOException {
		for (History v : frames) {
			os.write("<frame>".getBytes(Charset.forName("UTF8")));
			v.serialise(os);
			os.write("</frame>".getBytes(Charset.forName("UTF8")));
		}
	}

	private void saveDelta(final OutputStream os, final List<History> frames) throws IOException {
		History v = getViews().get(0);
		os.write("<frame>".getBytes(Charset.forName("UTF8")));
		v.serialise(os);
		os.write("</frame>".getBytes(Charset.forName("UTF8")));
		for (int i = 1; i < getViews().size(); i++) {
			FrameDiff delta = new FrameDiff(getViews().get(i - 1), getViews().get(i));
			delta.save(os);
		}

	}

	private void getTagBoundaries(String tag, List<Integer> startPositions, List<Integer> endPositions) {
		byte[] startBytes = ("<" + tag + ">").getBytes(Charset.forName("UTF8"));
		byte[] endBytes = ("</" + tag + ">").getBytes();
		int endPos = 0, pos = 0;

		for (int i = 0; i < animation.length; i++) {
			if (endPositions.size() < startPositions.size()) {
				if (animation[i] == endBytes[endPos]) {
					if (endPos == endBytes.length - 1) {
						endPositions.add(i - endPos);
						endPos = 0;
					} else
						endPos++;
				} else
					endPos = 0;
			} else {
				if (animation[i] == startBytes[pos]) {
					if (pos == startBytes.length - 1) {
						startPositions.add(i + 1);
						pos = 0;
					} else
						pos++;
				} else
					pos = 0;
			}
		}
	}

	private String getFirstTagContent(String tag, int startPos) {
		byte[] startBytes = ("<" + tag + ">").getBytes(Charset.forName("UTF8"));
		byte[] endBytes = ("</" + tag + ">").getBytes(Charset.forName("UTF8"));
		int endPos = 0, pos = 0, start = 0, end = 0;
		for (int i = startPos; i < animation.length && end == 0; i++) {
			if (start > 0) {
				if (animation[i] == endBytes[endPos]) {
					if (endPos == endBytes.length - 1) {
						end = i - endBytes.length;
						endPos = 0;
					} else
						endPos++;
				} else
					endPos = 0;
			} else {
				if (animation[i] == startBytes[pos]) {
					if (pos == startBytes.length - 1) {
						start = i + 1;
						pos = 0;
					} else
						pos++;
				} else
					pos = 0;
			}
		}
		byte[] content = Arrays.copyOfRange(animation, start, end + 1);
		return new String(content, Charset.forName("UTF8"));
	}

	public void loadDelta() {
		List<Integer> startPositions = new ArrayList<Integer>();
		List<Integer> endPositions = new ArrayList<Integer>();
		List<Integer> startDiffPositions = new ArrayList<Integer>();
		List<Integer> endDiffPositions = new ArrayList<Integer>();
		getTagBoundaries("frame", startPositions, endPositions);
		getTagBoundaries("frameDiff", startDiffPositions, endDiffPositions);

		views.clear();
		int diffPos = 0;
		int framePos = 0;

		while (framePos < startPositions.size() || diffPos < startDiffPositions.size()) {
			ByteArrayInputStream is;
			History history = new History();
			boolean frame = false;
			if (startDiffPositions.size() <= diffPos || (startPositions.size() > framePos && startPositions.get(framePos) < startDiffPositions.get(diffPos))) {
				is = new ByteArrayInputStream(Arrays.copyOfRange(animation, startPositions.get(framePos), endPositions.get(framePos)));
				frame = true;
				framePos++;
			} else {
				is = new ByteArrayInputStream(Arrays.copyOfRange(animation, startDiffPositions.get(diffPos), endDiffPositions.get(diffPos)));
				diffPos++;
			}
			InputStreamReader in = new InputStreamReader(is);
			int c;
			String text = "";
			try {
				if (!frame)
					history = (History) getViews().get(getViews().size() - 1).clone();
				while ((c = in.read()) >= 0) {
					text = text + (char) c;
					logger.debug(text);
					if (text.endsWith(">")) {
						String className = text.substring(text.indexOf("<") + 1, text.indexOf(">"));
						logger.debug("PARSING\n\tClass " + className);
						AbstractCommand command = null;
						try {
							if (frame) {
								command = (AbstractCommand) Class.forName(className).newInstance();
							} else {
								String id = getCommandId(startDiffPositions.get(diffPos - 1));
								command = history.getCommand(id);
							}
						} catch (Exception e) {
							logger.error("Komut Sınıfı bilnmiyor!" + className);
						}
						if (command != null) {
							try {
								if (frame)
									command.deserialise(in);
								else
									command.deserialise(in);
							} catch (Exception e) {
								logger.error("gele", e);
							}
							if (frame)
								history.add(command);
							text = "";
						}
					}
				}
			} catch (Exception e) {
				logger.error("Dosya okunamadı!", e);
			}
			views.add((History) history);
		}
		logger.warn("Done loading");
	}

	private String getCommandId(int pos) {
		return getFirstTagContent("id", pos);
	}

	public void save(String fileName) throws IOException {
		FileOutputStream fos = new FileOutputStream(fileName);
		saveDelta(fos, getViews());
		fos.flush();
		fos.close();
	}

	public void saveSelected(String fileName) throws IOException {
		FileOutputStream fos = new FileOutputStream(fileName);
		save(fos, getSelectedViews());
		fos.flush();
		fos.close();
	}

	private List<History> getSelectedViews() {
		return getViews().subList(getSelectionStart(), getSelectionEnd());
	}

	public void load(String fileName) throws IOException {
		FileInputStream fis = new FileInputStream(fileName);
		ByteBuffer buffer = ByteBuffer.allocate((int) new File(fileName).length());
		byte b = (byte) fis.read();
		while (b > -1) {
			buffer.put(b);
			b = (byte) fis.read();
		}
		fis.close();
		setAnimation(buffer.array());
	}

	public void delete() {
		for (int i = getSelectionStart(); i <= getSelectionEnd();) {
			getViews().remove(i);
			if (getSelectionStart() <= getSelectionEnd())
				setSelectionEnd(getSelectionEnd() - 1);
			else {
				i--;
				setSelectionEnd(getSelectionEnd() - 1);
			}
		}

	}

	public void applyCurrentChanges(AbstractCommand cmd) {
		List<CommandProperty> properties = changes.get(currentFrameNumber).get(cmd.getId());
		for (CommandProperty property : properties) {
			cmd.setPropertyValue(property.getName(), property.getValue());
		}
	}

	public boolean hasCanges(AbstractCommand cmd) {
		try {
			return changes.get(currentFrameNumber).get(cmd.getId()) != null;
		} catch (Exception e) {
			return false;
		}
	}

	public HashMap<Integer, List<String>> getFrameScripts() {
		return frameScripts;
	}

	public void setFrameScripts(HashMap<Integer, List<String>> frameScripts) {
		this.frameScripts = frameScripts;
	}

}
