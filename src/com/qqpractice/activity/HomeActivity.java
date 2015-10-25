package com.qqpractice.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.qqpractice.receiver.BaseUiUpdateReceiver;
import com.qqpractice.ui.bases.BaseActivity;
import com.qqpractice.user.Userinfo;

public class HomeActivity extends BaseActivity {
	public static final int FLAG = 3;
	
	private Userinfo mUserinfo = null;
	//UI部分
	private View viewMain = null;
	
	private RelativeLayout lyLoginTips = null;

	@SuppressLint("InflateParams") @Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		LayoutInflater inflater = getLayoutInflater();
		viewMain = inflater.inflate(R.layout.activity_home, null);
		setContentView(viewMain);
		
		initview();
		
		Bundle bundle = getIntent().getExtras();
	}
	
	private void initview() {
		
	}
	
	@Override
	public void finish() {
	}


	public void debug(String msg) {
		Log.i("HomeActivity", msg);
	}
	
	public class UIUpdateReceiver extends BaseUiUpdateReceiver {
		public static final int ACTION_UPDATE_NICKNAME = 0x1;
		public static final int ACTION_UPDATE_LOG =  0x2;
		
		public static final String KEY_LOG_TYPE_ERR = "key_log_type_err";
		
		public static final String ACTION_HOME_UI_UPDATE = "com.qqpractice.activity.ACTION_HOME_UI_UPDATE";

		private boolean bErrLog = false;
		
		@Override
		public void onReceive(Context context, Intent intent) {
			super.onReceive(context, intent);
			switch(action) {
			case ACTION_UPDATE_NICKNAME:
				debug("update nickname.");
				break;
			case ACTION_UPDATE_LOG:
				bErrLog = dispatchBool(KEY_LOG_TYPE_ERR);
				//add log
				break;
			default:;
			}
			
		}
		
	}
	
}
