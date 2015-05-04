package com.quickgis.gps.bean;

import com.quickgis.gps.util.Constant;

public class OnLine {
	
	 private long aid;
	 /*
	  * 登录时间
	  */
	 private long lt;
	 /*
	  * 在线时长
	  */
	 private double t;
	 /*
	  * 统计日期
	  */
	 private long dt;
	 private double lon;
	 private double lat;
	 private String loc;
	 
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
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public OnLine(long aid, long lt, double t, long dt) {
		//super();
		this.aid = aid;
		this.lt = lt;
		this.t = t;
		this.dt = dt;
	}
	public long getAid() {
		return aid;
	}
	public void setAid(long aid) {
		this.aid = aid;
	}
	public long getLt() {
		return lt;
	}
	public void setLt(long lt) {
		this.lt = lt;
	}
	public double getT() {
		return t;
	}
	public void setT(double t) {
		this.t = t;
	}
	public long getDt() {
		return dt;
	}
	public void setDt(long dt) {
		this.dt = dt;
	}
	 
	 
	 public void addTime(){
		 long time=System.currentTimeMillis()-this.lt;
		 if(time>0&&time<Constant.MAXONLINETIME){
			 this.t+=time/Constant.MILSECONEHOUR; 
		 }
		 this.lt=System.currentTimeMillis();
	 }

}
