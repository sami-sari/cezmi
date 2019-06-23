package com.samisari.cezmi.animator.sound;

import com.samisari.cezmi.animator.gui.TimeLineEvent;
import com.samisari.cezmi.animator.gui.TimeLineListener;

public class SoundClipBoundary implements TimeLineListener {
	private CmdSound	sound;
	private int			startPlayingAt;
	private int			stopPlayingAt;

	@Override
	public void onTimelineEvent(TimeLineEvent event) {
		if (TimeLineEvent.FRAME_CHANGED == event.getEventType()) {
			play(event.getArguments()[0]);
		}

	}

	public CmdSound getSound() {
		return sound;
	}

	public void setSound(CmdSound sound) {
		this.sound = sound;
	}

	public int getStartPlayingAt() {
		return startPlayingAt;
	}

	public void setStartPlayingAt(int startPlayingAt) {
		this.startPlayingAt = startPlayingAt;
	}

	public int getStopPlayingAt() {
		return stopPlayingAt;
	}

	public void setStopPlayingAt(int stopPlayingAt) {
		this.stopPlayingAt = stopPlayingAt;
	}

	public void play(int currentFrameNumber) {
		if (currentFrameNumber >= startPlayingAt && currentFrameNumber < stopPlayingAt) {
			sound.player.play();
		}
		if (currentFrameNumber < startPlayingAt || currentFrameNumber > stopPlayingAt) {
			sound.player.finish();
		}
	}
}
