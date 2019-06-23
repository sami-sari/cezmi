package com.samisari.graphics.toolbox;

public class Area implements Comparable<Area>{
	int x1;
	int y1;
	int x2;
	int y2;
	
	public Area(int x, int y, int length, int height) {
		if (length < 0) {
			x = x - length;
			length = length * -1;
		}
		if (height < 0) {
			y = y - height;
			height = height * -1;
		}

		x1 = x;
		y1 = y;
		x2 = x + length;
		y2 = y + height;
	}

	@Override
	public int compareTo(Area a) {
		int x = a.x1;
		int y = a.y1;
		if (y1 > y)
			return 1;
		else if (y2 < y)
			return -1;
		else if (x2 < x)
			return -1;
		else if (x1 > x)
			return 1;
		return 0;
	}
	
	@Override
	public boolean equals(Object a) {
		if (x1==((Area)a).x1 && y1==((Area)a).y1 && x2==((Area)a).x2 && y2==((Area)a).y2)
			return true;
		return false;
	}

}
