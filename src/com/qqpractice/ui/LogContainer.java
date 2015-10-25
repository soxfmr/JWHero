package com.qqpractice.ui;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

public class LogContainer extends LinearLayout {
	//Log item color
	private static final int MODE_DEFAULT = 1;
	private static final int MODE_HIGHLIGHT = 2;
	
	//to limit the log item count
	private static final int MAX_COUNT = 10;
	
	private Context context;
	private int mCount = 0;
	
	private LayoutParams param = null;
	private AlphaAnimation alphaAnimation = null;
	
	/** true is open the animation effects. */
	private boolean bAnimMode = false;
	
	/** Reserve */
	public int getmCount() {
		return mCount;
	}

	private void incCount() {
		mCount++;
	}
	
	private void resetCount() {
		mCount = 0;
	}
	
	private boolean isbAnimMode() {
		return bAnimMode;
	}

	/** true is open the animation effects. */
	private void setbAnimMode(boolean bAnimMode) {
		this.bAnimMode = bAnimMode;
		//set current item animation
		alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		alphaAnimation.setDuration(800);
	}
	
	@SuppressLint("NewApi")
	public LogContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			final LayoutTransition transition = new LayoutTransition();
			this.setLayoutTransition(transition);
	    }else {
	    	setbAnimMode(true);
	    }
		
		//初始化日志间距 margin bottom = 10
		param =  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		param.setMargins(0, 0, 0, 10);
	}

	public void addLog(String log) {
		add(log, MODE_DEFAULT);
	}
	
	public void addErrLog(String log) {
		add(log, MODE_HIGHLIGHT);
	}
	
	public void clear() {
		resetCount();
		removeAllViews();
	}
	
	private void add(String log, int mode) {
		log += ".";
		LogItem item = new LogItem(context);
		//超出日志条数最大值则清空
		if(getmCount() >= MAX_COUNT) clear();
		//日志类型
		if(mode == MODE_DEFAULT) {
			item.setNormalText(log);
		}else if(mode == MODE_HIGHLIGHT) {
			item.setErrText(log);
		}
		//Android3.0 以下则刷新视图
		if( isbAnimMode() ) refreshView(item);
		//添加日志
		addView(item, 0, param);
		incCount();
		//System.gc();
	}
	
	private void refreshView(LogItem item) {
		item.startAnimation(alphaAnimation);
	}

}
