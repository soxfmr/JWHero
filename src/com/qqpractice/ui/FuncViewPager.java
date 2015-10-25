package com.qqpractice.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

@SuppressLint("ClickableViewAccessibility") 
public class FuncViewPager extends ViewPager implements OnTouchListener {

	public FuncViewPager(Context context) {
		super(context);
		init();
	}

	public FuncViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public void init() {
		setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		return false;
	}
	
}
