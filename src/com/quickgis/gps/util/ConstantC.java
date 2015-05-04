package com.quickgis.gps.util;

 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstantC {
	public static final int CH=0;
	public static final int EN=1;
	
	 
	public static int Lang=0;
	
 
	public static SimpleDateFormat dataFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	public static SimpleDateFormat dataFormat2=new SimpleDateFormat("yyyy-MM-dd"); 
	public static SimpleDateFormat dataFormat3=new SimpleDateFormat("HHmmss"); 
	
   
   
   
	private static int MAXCOUNT=10000;
	private static int MAXCOUNT2=100;
	private static int SEQ=1;
	
	public static String longToTime(long l){
		Date d=new Date(l);
		return dataFormat.format(d);
	}
	public static String longToDate(long l){
		Date d=new Date(l);
		return dataFormat2.format(d);
	}
	
	public static String getTimeStr(){
		Date d=new Date();
		return dataFormat3.format(d);
		
	}
	public static long timeToLong(String t){
		try {
			return dataFormat.parse(t).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return new Date().getTime();
	}
	
	public static long getTodayTime(){
		Date d=new Date();
		//去掉时分秒
		d.setHours(0);
		d.setMinutes(0);
		d.setSeconds(0);
		return d.getTime()/1000*1000;	//去掉毫秒数
	}
	
	public static String getUId(){
		String id=System.currentTimeMillis()+""+SEQ++;
		if(SEQ>MAXCOUNT){
			SEQ=1;
		}
		return id;
	}
	
	
	public static long getUIdL(){
		long id=System.currentTimeMillis()*MAXCOUNT2+SEQ++;
		if(SEQ>=MAXCOUNT2){
			SEQ=1;
		}
		return id;
	}
	 
    public static void main(String[] args){
    	long s=getTodayTime();
    	System.out.println(s);
    }
   
   
}
