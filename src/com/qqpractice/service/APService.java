package com.qqpractice.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;

import com.qqpractice.activity.HomeActivity;
import com.qqpractice.activity.R;
import com.qqpractice.core.RestoreUtils;
import com.qqpractice.core.ToolsUtils;
import com.qqpractice.fragment.PracticeFragment;
import com.qqpractice.func.Practice;
import com.qqpractice.receiver.BaseUiUpdateReceiver;
import com.qqpractice.user.PracticeInfo;

public class APService extends Service {
	////////////////////////////////////////////
	//                Field
	////////////////////////////////////////////
	
	private List<ActivityManager.RunningAppProcessInfo> appProcesses = null;
	private ActivityManager activityMgr = null;
	
	private AlarmManager alarmMgr;
	private PendingIntent mPendingIntent;
	
	////////////////////////////////////////////
	//              Override
	////////////////////////////////////////////

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
		
		String ACTION_AUTO_PRACTICE = APReceiver.ACTION_AUTO_PRACTICE_ALARM;
		Intent intent = new Intent(ACTION_AUTO_PRACTICE);
		
		mPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 获取缓存数据
		RestoreUtils restore = new RestoreUtils(getApplicationContext());
		final String sid = restore.getSid();
		final int place = restore.getPlace();
		final int timeUpper = restore.getTimeUpper();
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				refreshPracticeState(sid, place, timeUpper);
			}
		});
		thread.start();
		
		return START_NOT_STICKY;
	}
	
	////////////////////////////////////////////
	//         Main interface method
	////////////////////////////////////////////
	
	public void refreshPracticeState(String sid, int place, int timeUpper) {
		Practice practice = new Practice(sid);
		// 请求首页数据获取用户信息
		PracticeInfo pi = practice.getPracticeInfo();
		if(pi == null) {
			addErrLog(R.string.practice_log_get_userinfo_failed); // 获取用户信息失败，请刷新重试
			return ;
		}else {
			updatePracticeInfo(pi);
		}
		// 判断当前状态
		int state = pi.getState();
		switch(state) {
		case PracticeInfo.PRACTICE_STATE_IDLE:
			// 没体力你说个吊
			int power = pi.getPower();
			if(practice.isLowMiniPower(power)) {
				addErrLog(R.string.practice_log_power_smaller_than_fifteen); // 体力小于15，请补充体力后刷新重试
				return;
			}
			// 修炼地点判断
			int rank = pi.getRank();
			if(place == PracticeInfo.PRACTICE_PLACE_SMART) {
				place = practice.isLowRank(rank) ? PracticeInfo.PRACTICE_PLACE_HOUSE : 
					PracticeInfo.PRACTICE_PLACE_PALACE;

			}else if(place == PracticeInfo.PRACTICE_PLACE_PALACE) {
				place = practice.isLowRank(rank) ? PracticeInfo.PRACTICE_PLACE_HOUSE : 
					PracticeInfo.PRACTICE_PLACE_PALACE;
				if(place == PracticeInfo.PRACTICE_PLACE_HOUSE) {
					addLog(R.string.practice_log_practice_auto_change_place); // 等级不足，自动转入练功房
				}

			}else {
				// 练功房
				place = PracticeInfo.PRACTICE_PLACE_HOUSE;
			}
			// 如果修炼地点在战神宫，没体力你说个吊!!
			if(place == PracticeInfo.PRACTICE_PLACE_PALACE && practice.isLowPower(power)) {
				addErrLog(R.string.practice_log_power_smaller_than_twenty); // 体力小于20，请补充体力后刷新重试
				return;
			}
			// 设置修炼地点
			practice.setPlace(place);
			// 开始修炼
			boolean bsuccess = practice.onStart();
			if (bsuccess) {
				addLog(R.string.practice_log_practice_success);
			}
			else {
				addErrLog(R.string.practice_log_practice_failed);
			}
			// 开始定时
			timeUpper = place == PracticeInfo.PRACTICE_PLACE_HOUSE ? 
					PracticeInfo.PRACTICE_PERIOD_HOUSE_NORMAL : timeUpper;
			schedule(timeUpper);
			break;
		case PracticeInfo.PRACTICE_STATE_PRACTICING:
			// 获取当前修炼地点，使用新变量防止与place混淆
			int where = pi.getPlace();
			// 获取修炼时间上限
			timeUpper = where == PracticeInfo.PRACTICE_PLACE_HOUSE ? 
					PracticeInfo.PRACTICE_PERIOD_HOUSE_NORMAL : timeUpper;
			// 判断修炼是否完成
			int remainHour = pi.getRemainHour();
			if(remainHour == timeUpper) {
				boolean bfinish = practice.onDone();
				if(bfinish) {
					addLog(R.string.practice_log_finish_success);
					// 进入下一轮修炼
					refreshPracticeState(sid, place, timeUpper);
				}else {
					addErrLog(R.string.practice_log_finish_error);
				}
			}else {
				// 计算修炼剩余总时长
				int remainMin = pi.getRemainMin();
				long haveBeenDone = ToolsUtils.HtoMS(remainHour) + ToolsUtils.MtoMS(remainMin);
				long remainAll = ToolsUtils.HtoMS(timeUpper) - haveBeenDone;
				schedule(remainAll);
			}
			break;
		case PracticeInfo.PRACTICE_STATE_HURT:
			boolean bcured = practice.onCure();
			if(bcured) {
				addLog(R.string.practice_log_cure_success);
				// 刷新修炼状态
				refreshPracticeState(sid, place, timeUpper);
			}else {
				addErrLog(R.string.practice_log_cure_failed);
			}
			break;
		default:;
		}
		
	}
	
	public void schedule(int hour) {
		long time = ToolsUtils.HtoMS(hour);
		schedule(time);
	}
	
	public void schedule(long time) {
		long triggerAtMillis = SystemClock.elapsedRealtime() + time;
		alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMillis, mPendingIntent);
	}
	
	
	////////////////////////////////////////////
	//              Update UI
	////////////////////////////////////////////
	
	public void updatePracticeInfo(PracticeInfo pi) {
		Intent updater = new Intent(PracticeFragment.UiUpdateReceiver.ACTION_PRACTICE_UI_UPDATE);
		String rank = pi.getRank() + "";
		sendUiUpdateMessage(PracticeFragment.UiUpdateReceiver.ACTION_UPDATE_RANK, rank, updater);
		String power = pi.getPower() + "";
		sendUiUpdateMessage(PracticeFragment.UiUpdateReceiver.ACTION_UPDATE_POWER, power, updater);
		String expCurrent = pi.getExpCurrent() + "";
		sendUiUpdateMessage(PracticeFragment.UiUpdateReceiver.ACTION_UPDATE_POWER, expCurrent, updater);
		// 更新经验值
		Bundle bundle = new Bundle();
		bundle.putInt(PracticeFragment.UiUpdateReceiver.KEY_EXP_CURRENT, pi.getExpCurrent());
		bundle.putInt(PracticeFragment.UiUpdateReceiver.KEY_EXP_TOTAL, pi.getExpTotal());
		sendUiUpdateMessage(PracticeFragment.UiUpdateReceiver.ACTION_UPDATE_EXPERIENCE, bundle, updater);
	}
	
	public void updateNickName(String nickname) {
		Intent updater = new Intent(HomeActivity.UIUpdateReceiver.ACTION_HOME_UI_UPDATE);
		sendUiUpdateMessage(HomeActivity.UIUpdateReceiver.ACTION_UPDATE_NICKNAME, nickname, updater);
	}
	
	public void addLog(int resId) {
		String msg = getString(resId);
		addLog(msg, false);
	}
	
	/*public void addLog(String msg) {
		addLog(msg, false);
	}*/
	
	public void addErrLog(int resId) {
		String msg = getString(resId);
		addLog(msg, true);
	}
	
	/*public void addErrLog(String msg) {
		addLog(msg, true);
	}*/
	
	private void addLog(String msg, boolean bErrLog) {
		Intent updater = new Intent(HomeActivity.UIUpdateReceiver.ACTION_HOME_UI_UPDATE);
		Bundle bundle = new Bundle();
		// 封装日志内容
		bundle.putString(BaseUiUpdateReceiver.KEY_GENERIC, msg);
		// 日志类型
		bundle.putBoolean(HomeActivity.UIUpdateReceiver.KEY_LOG_TYPE_ERR, bErrLog);
		sendUiUpdateMessage(HomeActivity.UIUpdateReceiver.ACTION_UPDATE_LOG, bundle, updater);
	}
	
	public void sendUiUpdateMessage(int action, String data, Intent intent) {
		Bundle bundle = new Bundle();
		bundle.putInt(BaseUiUpdateReceiver.ACTION_TYPE, action);
		bundle.putString(BaseUiUpdateReceiver.KEY_GENERIC, data);
		intent.putExtras(bundle);
		sendBroadcast(intent);
	}
	
	public void sendUiUpdateMessage(int action, Bundle bundle, Intent intent) {
		bundle.putInt(BaseUiUpdateReceiver.ACTION_TYPE, action);
		intent.putExtras(bundle);
		sendBroadcast(intent);
	}
	
	private boolean isAppOnForeBackground() {
		boolean bRet = false;
		appProcesses = activityMgr.getRunningAppProcesses();
		if(appProcesses != null) {
			ActivityManager.RunningAppProcessInfo info;
			for(int i = 0, size = appProcesses.size(); i < size; i++) {
				info = appProcesses.get(i);
				if(info.processName.equals(getPackageName()) && 
						info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					bRet = true;
					break;
				}
			}
		}
		appProcesses = null;
		activityMgr = null;
		System.gc();
		return bRet;
	}

}
