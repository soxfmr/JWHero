package com.qqpractice.user;

public class PracticeInfo {
	public static final int PRACTICE_STATE_IDLE = 0x31;
	public static final int PRACTICE_STATE_PRACTICING = 0x32;
	public static final int PRACTICE_STATE_HURT = 0x33;
	
	public static final int PRACTICE_POWER_LIMIT_HOUSE = 20;
	public static final int PRACTICE_POWER_LIMIT_PALACE = 15;
	
	public static final int PRACTICE_RANK_LIMIT_PALACE = 20;
	
	public static final int PRACTICE_PERIOD_HOUSE_NORMAL = 4;
	public static final int PRACTICE_PERIOD_PALACE_NORMAL = 2;
	
	public static final int PRACTICE_PLACE_SMART = 0x50;
	public static final int PRACTICE_PLACE_HOUSE = 0x51;
	public static final int PRACTICE_PLACE_PALACE = 0x52;
	
	public static final int PRACTICE_EXPERIENCE_CURRENT = 0;
	public static final int PRACTICE_EXPERIENCE_TOTAL = 1;
	
	public static final int PRACTICE_REMAIN_TIME_HOUR = 0;
	public static final int PRACTICE_REMAIN_TIME_MINUTE = 1;
	
	private String nickname;
	
	private int rank;
	private int power;
	private int expCurrent;
	private int expTotal;
	private int remainHour;
	private int remainMin;
	
	private int place;
	private int state;
	
	
	public int getRemainHour() {
		return remainHour;
	}
	public void setRemainHour(int remainHour) {
		this.remainHour = remainHour;
	}
	public int getRemainMin() {
		return remainMin;
	}
	public void setRemainMin(int remainMin) {
		this.remainMin = remainMin;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}
	public int getExpCurrent() {
		return expCurrent;
	}
	public void setExpCurrent(int expCurrent) {
		this.expCurrent = expCurrent;
	}
	public int getExpTotal() {
		return expTotal;
	}
	public void setExpTotal(int expTotal) {
		this.expTotal = expTotal;
	}
	public int getPlace() {
		return place;
	}
	public void setPlace(int place) {
		this.place = place;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	
}
