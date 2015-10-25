package com.qqpractice.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qqpractice.activity.R;

public class LayoutButton extends LinearLayout {
	private ImageView imgMore = null;
	
	public LayoutButton(Context context) {
		super(context);
		init(context);
	}
	
	public LayoutButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context) {
		setClickable(true);
		setBackgroundResource(R.drawable.btn_default_hightlight);
	}
	
	public void setCorner(boolean show) {
		int visibility = show ? View.VISIBLE : View.GONE;
		try{
			if(imgMore == null) {
				FrameLayout lyContainer = (FrameLayout) getChildAt(0);
				imgMore = (ImageView) lyContainer.getChildAt(1);
			}
			imgMore.setVisibility(visibility);
		}catch(NullPointerException ex) {ex.printStackTrace();}
		
		setEnabled(show);
	}
	
}
