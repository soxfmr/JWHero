package com.qqpractice.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class ExpProgressBar extends ProgressBar {
	
	public ExpProgressBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ExpProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ExpProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public void setTotalExp(int total) {
		setMax(total);
	}
	
	public void setCurrentExp(int current) {
		setProgress(current);
	}
	
	public synchronized int getTotalExp() {
		return getMax();
	}
	
	public synchronized int getCurrentExp() {
		return getProgress();
	}
	
	
}
