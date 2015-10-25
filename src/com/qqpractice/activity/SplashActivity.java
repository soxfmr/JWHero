package com.qqpractice.activity;

import java.util.List;

import com.qqpractice.databases.DataMan;
import com.qqpractice.databases.SQLiteHelper;
import com.qqpractice.ui.bases.BaseActivity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class SplashActivity extends BaseActivity {
	public static final int FLAG = 1;
	
	protected Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.activity_splash, null);
		setContentView(view);
		if(needStartApp()) {
			Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
			view.startAnimation(animation);
			animation.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {}
				@Override
				public void onAnimationRepeat(Animation animation) {}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					mHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							//goHome();
							openActivity(LoginActivity.class);
							finish();
						}
					}, 500);
				}
			});
			initAd();
		}else {
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
	
	private void goHome() {
		DataMan dm = new DataMan(mContext);
		String user = dm.getFinaluser();
		if(user.equals(DataMan.RECORD_NO_FOUND)) {
			openActivity(LoginActivity.class);
		}else {
			Bundle bundle = new Bundle();
			//登陆页面来源标识
			bundle.putInt(ACTIVITY_TAG, SplashActivity.FLAG);
			bundle.putString(SQLiteHelper.TB_USERINFO_USERNAME, user);
			openActivity(HomeActivity.class, bundle);
			bundle = null;
		}
		dm.close();
		dm = null;
		user = null;
		finish();
	}
	
	private void initAd() {
		//AdManager.getInstance(this).init("7775b7833bbbd7db", "45c0dd863a67ec3a", false);
	}
	
	private final boolean needStartApp() {
		final ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final List<RunningTaskInfo> tasks = manager.getRunningTasks(1024);
		if(!tasks.isEmpty()) {
			final String packageName = getPackageName();
			for(RunningTaskInfo taskInfo : tasks) {
				if(packageName.equals(taskInfo.baseActivity.getPackageName())) {
					return taskInfo.numActivities==1;
				}
			}
			
		}
		return true;
	}
	
}