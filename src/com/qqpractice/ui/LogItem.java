package com.qqpractice.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qqpractice.activity.R;

public class LogItem extends LinearLayout {
	private TextView mTime;
	private TextView mContent;
	private ImageView imgStatus = null;
	
	public LogItem(Context context) {
		super(context);		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.log_item, this);
		
		mTime = (TextView) findViewById(R.id.log_item_time);
		mContent = (TextView) findViewById(R.id.log_item_content);
		imgStatus = (ImageView) findViewById(R.id.log_item_status);
	}
	
	public void setNormalText(String content) {
		//show time
		mTime.setText(getTime());
		//show the content of log
		mContent.setText(content);
	}
	
	public void setErrText(String content) {
		Drawable errIcon = getResources().getDrawable(R.drawable.log_status_error);
		imgStatus.setImageDrawable(errIcon);
		setNormalText(content);
	}
	
	private String getTime() {
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
		return sdf.format(new Date());
	}

}
