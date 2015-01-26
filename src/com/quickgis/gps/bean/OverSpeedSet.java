package com.quickgis.gps.bean;

public class OverSpeedSet {
    private long assetid;
    private int speed1;
    private int speed2;
    
    
    public OverSpeedSet(){
    	
    }
    
    public OverSpeedSet(long id ,int speed1,int speed2){
    	this.assetid=id;
    	this.speed1=speed1;
    	this.speed2=speed2;
    }
    
	public long getAssetid() {
		return assetid;
	}
	public void setAssetid(long assetid) {
		this.assetid = assetid;
	}
	public int getSpeed1() {
		return speed1;
	}
	public void setSpeed1(int speed1) {
		this.speed1 = speed1;
	}
	public int getSpeed2() {
		return speed2;
	}
	public void setSpeed2(int speed2) {
		this.speed2 = speed2;
	}
    
    
}
