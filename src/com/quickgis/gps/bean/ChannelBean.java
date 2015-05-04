package com.quickgis.gps.bean;

public class ChannelBean {
	  private String key;
	  private long lastTime;
	  
      private byte[] lastByte;
  
	public ChannelBean(String key, long lastTime) {
		super();
		this.key = key;
		this.lastTime = lastTime;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public long getLastTime() {
		return lastTime;
	}
	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}
	public byte[] getLastByte() {
		return lastByte;
	}
	public void setLastByte(byte[] lastByte) {
		this.lastByte = lastByte;
	}
	
	
  
}
