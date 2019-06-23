package com.samisari.cezmi.animator.gui;

import org.apache.log4j.Logger;

public class TimeLineEvent {
	private static final Logger	logger					= Logger.getLogger(TimeLineEvent.class);
	public static final int		ACTION_PLAY				= 0;
	public static final int		ACTION_PAUSE			= 1;
	public static final int		BACK_PLAY				= 2;
	public static final int		FAST_FORWARD			= 3;
	public static final int		FAST_BACK				= 4;
	public static final int		FRAME_CHANGED			= 5;
	public static final int		SELECTION_CHANGED		= 6;
	public static final int		ACTION_COPY				= 7;
	public static final int		ACTION_PASTE			= 8;
	public static final int		ACTION_EXTRAPOLATE		= 9;
	public static final int		ACTION_SAVE				= 10;
	public static final int		ACTION_LOAD				= 11;
	public static final int		ACTION_DELETE			= 12;
	public static final int		ACTION_SHOW_PROPERTIES	= 13;
	public static final int		ACTION_EDIT				= 14;
	public static final int		ACTION_ADD_SOUND		= 15;
	public static final int		ACTION_MAKEMOVIE		= 16;
	public static final int		ACTION_SHOWSCRIPT		= 17;

	private int					eventType;
	private int[]				arguments;

	public TimeLineEvent(int eventType, int[] arguments) {
		logger.debug("Firing Timeline event " + eventType + " args[0]=" + ((arguments != null && arguments.length > 0) ? arguments[0] : "") + " args[1]="
				+ ((arguments != null && arguments.length > 1) ? arguments[1] : ""));
		setEventType(eventType);
		setArguments(arguments);
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public int[] getArguments() {
		return arguments;
	}

	public void setArguments(int[] arguments) {
		this.arguments = arguments;
	}

}
