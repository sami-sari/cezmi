package com.samisari.debugtools.performance;

import java.util.HashMap;

public class PerformanceInspector {
	private static HashMap<String, PerformanceInspector>	inspectorMap;
	private long											start;
	private long											end;

	public static PerformanceInspector get(String name) {
		if (inspectorMap == null) {
			inspectorMap = new HashMap<>();
		}
		if (inspectorMap.containsKey(name)) {
			return inspectorMap.get(name);
		}
		PerformanceInspector inspector = new PerformanceInspector();
		inspectorMap.put(name, inspector);
		return inspector;
	}

	public void classStaticInitEnd() {
		start = System.currentTimeMillis();
	}

	public void classStaticInitStart() {
		end = System.currentTimeMillis();
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}
}
