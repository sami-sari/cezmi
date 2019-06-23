package com.samisari.graphics.toolbox;

import java.util.ArrayList;
import java.util.Iterator;

public class AreaMap<T> {
	ArrayList<Area> indices;
	ArrayList<T> values;

	public AreaMap() {
		indices = new ArrayList<Area>();
		values = new ArrayList<T>();
	}

	public synchronized void add(Area a, T value) {
		int i = 0;
		while (i < indices.size() && a.compareTo(indices.get(i)) > 0) {
			i++;
		}
		// if the same command is added more than once
		if (i < indices.size() && indices.get(i).equals(a)) {
			indices.remove(i);
		}
		indices.add(i, a);
		values.add(i, value);
	}

	public T get(int x, int y) {
		Area a = new Area(x, y, 0, 0);
		int i = 0;
		while (i < indices.size() && indices.get(i).compareTo(a) < 0 ) {
			i++;
		}
		if (indices.size()>i && indices.get(i).compareTo(a) == 0)
			return values.get(i);
		return null;
	}

	public void setOffset(int offsetX, int offsetY) {
		Iterator<Area> iter = indices.iterator();
		while (iter.hasNext()) {
			Area a = iter.next();
			a.x1 += offsetX;
			a.x2 += offsetX;
			a.y1 += offsetY;
			a.y2 += offsetY;
		}
	}

}
