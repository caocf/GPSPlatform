package com.quickgis.gps.bean;

import java.util.HashMap;
import java.util.Map;

 

public class GPSLocation {
	
	private long id;
	private long assetid;
	private String call;
	private String name;
	
	private long gpstime;
	private double lon;
	private double lat;
	private int speed;
	private int rad; 
	
	private int wendu;
	private String status;
	private int alarmType;  
	//里程 单位米
	 private int dis;
	 
	 private double oil; 
	 private String loc;

     private boolean locStatus;
	 private String imei;
	 
	 private long startTime;
	 private long endTime;
	 
	 private Map<String, Long> alarmId;  //解决同一gps数据包含多个警情时引发的错误
	 
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public long getAssetid() {
		return assetid;
	}
	public void setAssetid(long assetid) {
		this.assetid = assetid;
	}
	public String getCall() {
		return call;
	}
	public void setCall(String call) {
		this.call = call;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getGpstime() {
		return gpstime;
	}
	public void setGpstime(long gpstime) {
		this.gpstime = gpstime;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public double getOil() {
		return oil;
	}
	public void setOil(double oil) {
		this.oil = oil;
	}
 
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getRad() {
		return rad;
	}
	public void setRad(int rad) {
		this.rad = rad;
	}
	 
	 
	public boolean isLocStatus() {
		return locStatus;
	}
	public void setLocStatus(boolean locStatus) {
		this.locStatus = locStatus;
	}
	public int getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(int alarmType) {
		this.alarmType = alarmType;
	}
	 
	public int getDis() {
		return dis;
	}
	public void setDis(int dis) {
		this.dis = dis;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getWendu() {
		return wendu;
	}
	public void setWendu(int wendu) {
		this.wendu = wendu;
	}
	public long getAlarmId(String alarmType) {
		if(this.alarmId!=null){
			long id = alarmId.get(alarmType)==null?0l:alarmId.get(alarmType);
				return id;
		}
		return 0l;
	}
	public void setAlarmId(String alarmType, long alarmId) {
		if(this.alarmId == null){
			this.alarmId = new HashMap<String, Long>();
		}
		this.alarmId.put(alarmType, alarmId);
	}
		
	public GPSLocation copy(){
		GPSLocation gps=new GPSLocation();
		gps.setAssetid(this.assetid);
		gps.setCall(this.call);
		gps.setDis(this.dis);
		gps.setGpstime(gpstime);
		gps.setImei(imei);
		gps.setLat(lat);
		gps.setLon(lon);
		gps.setLoc(loc);
		gps.setLocStatus(locStatus);
		gps.setName(name);
		gps.setOil(oil);
		gps.setRad(rad);
		gps.setSpeed(speed);
		gps.setStatus(status);
		return gps;
	}
}
