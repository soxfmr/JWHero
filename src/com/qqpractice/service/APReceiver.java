package com.qqpractice.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class APReceiver extends BroadcastReceiver {
	public static final String ACTION_AUTO_PRACTICE_ALARM = "com.qqpractice.bg.ACTION_AUTO_PRACTICE_ALARM";

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent intentAPService = new Intent(context, APService.class);
		context.startService(intentAPService);

	}

}
