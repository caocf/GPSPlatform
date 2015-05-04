package com.quickgis.gps.decoder;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.quickgis.gps.bean.GPSLocation;
import com.quickgis.gps.bean.Response;
import com.quickgis.gps.db.DBOperator;
import com.quickgis.gps.tcp.NettyServer;
import com.quickgis.gps.util.ByteUtils;
import com.quickgis.gps.util.CRC16Util;
import com.quickgis.gps.util.Constant;

public class TextDecoder implements Decoder {
	static Logger logger=Logger.getLogger(TextDecoder.class);
	
	  

	@Override
	public Map parseLogin(byte[] b,int from) {
		// TODO Auto-generated method stub
		//°ü³¤¶È 
		return null;
	}
	
	public static boolean isNotNull(String s){
		return s!=null&&!"".equals(s);
	}
	
	
	@Override
	public Map parseLocation(byte[] b,int from) {
		// TODO Auto-generated method stub
		String s=new String(b);
		String[] ss=s.split(",");
		  Map<String,Object> map=new HashMap<String,Object>(); 
		 if(ss.length>13){
		    GPSLocation  gps=new GPSLocation();
		    String imei=ss[1];
		    gps.setImei(ss[1]);
		    double lon=Double.parseDouble(ss[2]);
		    double lat=Double.parseDouble(ss[3]);
		    
		  //  logger.info("get location:"+lon+" "+lat+" after:"+aa.a+"  "+aa.b);
		    gps.setLon(lon);
		    gps.setLat(lat);
		  
		    gps.setSpeed(Integer.parseInt(ss[4]));
		    gps.setRad(Integer.parseInt(ss[5]));
		  
		    int alarm=Integer.parseInt(ss[7]);
		    
		    gps.setAlarmType(alarm);
		    if(isNotNull(ss[8])){
		       gps.setDis(Integer.parseInt(ss[8]));
		    }
		    
		    if(isNotNull(ss[12])){
		       gps.setOil(Double.parseDouble(ss[12]));
		    }
		    map.put(Constant.location, gps);
		    map.put(Constant.logInKey, ss[1]);
		 }
		return map;
	}

	@Override
	public Map parseAlarm(byte[] b,int from) {
		// TODO Auto-generated method stub
		
	 
		return null;
	}
	
	@Override
	public Map parseMessage(byte[] b, int from) {
		// TODO Auto-generated method stub
		 
		return null;
	}
	
	@Override
	public Map parseStatus(byte[] b, int from) {
		// TODO Auto-generated method stub
		 
		return null;
	}

	DBOperator db=new DBOperator();
	@Override
	public Map parseQuestion(byte[] b, int from) {
		// TODO Auto-generated method stub
		String s=new String(b);
		s=s.substring(3,s.indexOf(",#"));
		 String result=null;
		  Map<String,Object> map=new HashMap<String,Object>(); 
		  try {
			  map.put(Constant.resBytes, result.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return map;
	}
	
	
	public static void main(String[] args){
		 
		
	}

	@Override
	public Map parse(byte[] b, int from,String imei) {
		// TODO Auto-generated method stub
		return null;
	}


	
}
