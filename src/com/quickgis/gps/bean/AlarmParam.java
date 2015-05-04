package com.quickgis.gps.bean;

import org.apache.log4j.Logger;

 
import com.quickgis.gps.util.Constant;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class AlarmParam {
     private long assetid;
     private long alarmid;
     private int alarmtype;
     private String alarmname;
     private String alarmpoints;
     private int geotype;
     
     private double radius;
     private Geometry geo;
     static Logger logger=Logger.getLogger(AlarmParam.class);
 	
     private static WKTReader reader=new WKTReader();
     public AlarmParam(){
    	 
     }
     
	public AlarmParam(long assetid, long alarmid,  int alarmtype,
			String alarmname, String alarmpoints, int geotype) {
		 
		this.assetid = assetid;
		this.alarmid=alarmid;
		this.alarmtype = alarmtype;
		this.alarmname = alarmname;
		this.alarmpoints = alarmpoints;
		this.geotype = geotype;
		if(geotype==Constant.MAPPOINT){
			String points=alarmpoints.replace(",", " ");
			try {
				this.geo=reader.read("POINT("+points+")");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.error("parse geo error:"+assetid+","+alarmname+","+alarmpoints+","+geotype);
				e.printStackTrace();
			}
		}else if(geotype==Constant.MAPPATH){
			String points=alarmpoints.replaceAll(",", " ").replaceAll(";", ",");
			try {
				this.geo=reader.read("LINESTRING("+points+")");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.error("parse geo error:"+assetid+","+alarmname+","+alarmpoints+","+geotype);
				e.printStackTrace();
			}
		}else if(geotype==Constant.MAPPOLYGON){
			String points=alarmpoints.replaceAll(",", " ").replaceAll(";", ",");
			try {
				this.geo=reader.read("POLYGON (("+points+"))");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("parse geo error:"+assetid+","+alarmname+","+alarmpoints+","+geotype);
			}
		}else if(geotype==Constant.MAPCIRCLE){
			String[] p=this.alarmpoints.split(",");
			try {
				this.geo=reader.read("POINT("+p[0]+" "+p[1]+")");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("parse geo error:"+assetid+","+alarmname+","+alarmpoints+","+geotype);
			}
			this.radius=Double.parseDouble(p[2]);
			
		}else if(geotype==Constant.MAPRECT){
			String[] p=alarmpoints.split(",");
			StringBuffer sb=new StringBuffer();
			sb.append("POLYGON((");
			sb.append(p[0]).append(" ").append(p[1]).append(",");
			sb.append(p[0]).append(" ").append(p[3]).append(",");
			sb.append(p[2]).append(" ").append(p[3]).append(",");
			sb.append(p[2]).append(" ").append(p[1]).append(",");
			sb.append(p[0]).append(" ").append(p[1]).append("))");
			try {
				this.geo=reader.read(sb.toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("parse geo error:"+assetid+","+alarmname+","+alarmpoints+","+geotype);
			}
		}
	}
	public long getAssetid() {
		return assetid;
	}
	public void setAssetid(long assetid) {
		this.assetid = assetid;
	}
	 
	public int getAlarmtype() {
		return alarmtype;
	}
	public void setAlarmtype(int alarmtype) {
		this.alarmtype = alarmtype;
	}
	public String getAlarmname() {
		return alarmname;
	}
	public void setAlarmname(String alarmname) {
		this.alarmname = alarmname;
	}
	public String getAlarmpoints() {
		return alarmpoints;
	}
	public void setAlarmpoints(String alarmpoints) {
		this.alarmpoints = alarmpoints;
	}
	public int getGeotype() {
		return geotype;
	}
	public void setGeotype(int geotype) {
		this.geotype = geotype;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public Geometry getGeo() {
		return geo;
	}

	public void setGeo(Geometry geo) {
		this.geo = geo;
	}
	
	
	public static Geometry getGeoPoint(double lon,double lat){
		try {
			return reader.read("POINT("+lon+" "+lat+")");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("parse geo point error: POINT("+lon+" "+lat+")");
		}
		return null;
	}

	public long getAlarmid() {
		return alarmid;
	}

	public void setAlarmid(long alarmid) {
		this.alarmid = alarmid;
	}
     
     
     
}
