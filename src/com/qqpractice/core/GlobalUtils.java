package com.qqpractice.core;

public class GlobalUtils {
	public static final int NIGHT_SWITCH_CLOSE = 0;
	public static final int NIGHT_SWITCH_OPEN = 1;
	
	private int nightSwitch = NIGHT_SWITCH_CLOSE;
	private boolean autoUpdate = false;
	private boolean showBarIcon = false;
	
	public int getNightSwitch() {
		return nightSwitch;
	}
	public void setNightSwitch(int nightSwitch) {
		this.nightSwitch = nightSwitch;
	}
	public boolean isAutoUpdate() {
		return autoUpdate;
	}
	public void setAutoUpdate(boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
	}
	public boolean isShowBarIcon() {
		return showBarIcon;
	}
	public void setShowBarIcon(boolean showBarIcon) {
		this.showBarIcon = showBarIcon;
	}
	
	

}
