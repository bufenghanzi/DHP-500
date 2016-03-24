package com.mingseal.ui;

import com.mingseal.ui.SwipeLayout.Status;


public interface SwipeLayoutInterface {

	Status getCurrentStatus();
	
	void close();
	
	void open();
}
