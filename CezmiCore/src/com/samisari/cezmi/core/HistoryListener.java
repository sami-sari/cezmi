package com.samisari.cezmi.core;

public interface HistoryListener {
	public void onRemoved(Object o) ;
	public void onInserted(Object o);
	public void onModelChanged();
}
