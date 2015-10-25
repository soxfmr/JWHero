package com.qqpractice.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public abstract class BaseUiUpdateReceiver extends BroadcastReceiver {
	public static final String ACTION_TYPE = "act_type";

	public static final String KEY_GENERIC = "key_generic";
	
	public Bundle data;
	public int action;

	@Override
	public void onReceive(Context context, Intent intent) {
		data = intent.getExtras();
		action = data.getInt(ACTION_TYPE);
	}

	public int dispatchInt(String key) {
		return data.getInt(key);
	}
	
	public String dispatchChars(String key) {
		return data.getString(key);
	}
	
	public boolean dispatchBool(String key) {
		return data.getBoolean(key);
	}
	
}
