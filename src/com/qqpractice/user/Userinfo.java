package com.qqpractice.user;

public class Userinfo {
	private String username = null;
	private String password = null;
	private String nickname = null;
	private int place = 0;
	private int perHouse = 0;
	private int perPalace = 0 ;
	private int countFight = 0;
	private int placeQuench = 0;
	private int countQuench = 0;
	
	private boolean memSwitch = false;
	private boolean memHeader = false;
	private boolean memFire = false;
	private boolean memTree = false;
	private boolean memWater = false;

	private boolean autoUpdRank = false;
	
	private String dateSpend = null;
	
	private String sid = null;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	public int getPerHouse() {
		return perHouse;
	}

	public void setPerHouse(int perHouse) {
		this.perHouse = perHouse;
	}

	public int getPerPalace() {
		return perPalace;
	}

	public void setPerPalace(int perPalace) {
		this.perPalace = perPalace;
	}

	public int getCountFight() {
		return countFight;
	}

	public void setCountFight(int countFight) {
		this.countFight = countFight;
	}

	public int getPlaceQuench() {
		return placeQuench;
	}

	public void setPlaceQuench(int placeQuench) {
		this.placeQuench = placeQuench;
	}

	public int getCountQuench() {
		return countQuench;
	}

	public void setCountQuench(int countQuench) {
		this.countQuench = countQuench;
	}

	public boolean isMemSwitch() {
		return memSwitch;
	}

	public void setMemSwitch(boolean memSwitch) {
		this.memSwitch = memSwitch;
	}

	public boolean isMemHeader() {
		return memHeader;
	}

	public void setMemHeader(boolean memHeader) {
		this.memHeader = memHeader;
	}

	public boolean isMemFire() {
		return memFire;
	}

	public void setMemFire(boolean memFire) {
		this.memFire = memFire;
	}

	public boolean isMemTree() {
		return memTree;
	}

	public void setMemTree(boolean memTree) {
		this.memTree = memTree;
	}

	public boolean isMemWater() {
		return memWater;
	}

	public void setMemWater(boolean memWater) {
		this.memWater = memWater;
	}

	public boolean isAutoUpdRank() {
		return autoUpdRank;
	}

	public void setAutoUpdRank(boolean autoUpdRank) {
		this.autoUpdRank = autoUpdRank;
	}

	public String getDateSpend() {
		return dateSpend;
	}

	public void setDateSpend(String dateSpend) {
		this.dateSpend = dateSpend;
	}
	
	public void init(String user, String pwd) {
		username = user;
		password = pwd;
		nickname = "精武英雄";
		
		place = 1;
		perHouse = 4;
		perPalace = 2;
		countFight = 1;
		placeQuench = 1;
		countQuench = 1;
		
		memSwitch = false;
		memHeader = false;
		memFire = false;
		memTree = false;
		memWater = false;
		
		autoUpdRank = false;
		
		dateSpend = "19700101";
	}
}
