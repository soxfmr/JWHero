package com.qqpractice.ui.bases;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends Activity {
	public static final String ACTIVITY_TAG = "activity_tag";
	public static final String ACTIVITY_TIP = "activity_tip";
	
	public Handler mHandler = new Handler();
	protected Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
	}
	
	/* 切换activity函数重载 */
	public void openActivity(Class<?> cls) {
		openActivity(cls, null);
	}
	
	public void openActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent(this, cls);
		if(bundle != null) { intent.putExtras(bundle); }
		startActivity(intent);
	}
	
	/**
	 * Open activity waiting for it back
	 * */
	public void openActivityWait(Class<?> cls, int requestCode) {
		openActivityWait(cls, null, requestCode);
	}
	
	/**
	 * Open activity waiting for it back
	 * */
	public void openActivityWait(Class<?> cls, Bundle bundle, int requestCode) {
		Intent intent = new Intent(this, cls);
		if(bundle != null) { intent.putExtras(bundle); }
		startActivityForResult(intent, requestCode);
	}
	
	public void openActivityInThread(final Class<?> cls) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				openActivity(cls, null);
			}
		});
	}
	
	public void openActivityInThread(final Class<?> cls, final Bundle bundle) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				openActivity(cls, bundle);
			}
		});
	}
	
	/* 显示toast函数重载 */
	public void OnNotice(final int ResId) {
		Toast.makeText(this, ResId, Toast.LENGTH_SHORT).show();
	}
	
	protected void OnNoticeInThread(final int ResId) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				OnNotice(ResId);
			}
		});
	}
	
	public void loadTitle(int title, int titleId) {
		TextView tv = (TextView) findViewById(titleId);
		tv.setText(title);
	}
}
