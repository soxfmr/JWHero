package com.qqpractice.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qqpractice.activity.R;

public class ExImageButton extends LinearLayout {
	private ImageView mIcon = null;
	private TextView mText  = null;

	public ExImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		Context c = getContext();
		LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.ex_image_button, this);
		mIcon = (ImageView) findViewById(R.id.eximagebutton_icon);
		mText = (TextView) findViewById(R.id.eximagebutton_text);
		
		TypedArray a = null;
		try{
			a = c.obtainStyledAttributes(attrs, R.styleable.ExImageButton);
			mIcon.setBackgroundResource(a.getResourceId(R.styleable.ExImageButton_eib_icon, 0));
			mText.setText(a.getString(R.styleable.ExImageButton_eib_text));
		}catch(Exception e){
			e.printStackTrace();
			
		}finally { 
			if( a != null ) {
				a.recycle();
			}
		}
		this.setClickable(true);
		this.setBackgroundResource(R.drawable.btn_default_hightlight);
		
	}

}
