package com.quickgis.gps.decoder;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

 

import com.quickgis.gps.bean.Asset;
import com.quickgis.gps.bean.GPSLocation;
 
import com.quickgis.gps.util.ByteUtils;
import com.quickgis.gps.util.Constant;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class QuickMapBinDecoder implements Decoder {
	static Logger logger=Logger.getLogger(QuickMapBinDecoder.class);
	
	@Override
	public Map parse(byte[] b, int from,String imei1) {
		// TODO Auto-generated method stub
		System.out.println("==============QuickMapBinDecoder : " + ByteUtils.byteToString(b, 0, b.length));
		Map result=new HashMap();
		try{
		while(from<b.length-1){
			if(b[from]==Constant.THH){  //GPS信息
				from+=1;
				String imei=ByteUtils.byteToString(b,from,5);
				from+=5;
				long gpstime=ByteUtils.byteToTime(b,from,6);
				from+=6;
				
				String latStr=ByteUtils.byteToString(b, from, 4);
				from+=5;
				
				String lonSStr=ByteUtils.byteToString(b, from, 5);
				from+=5;
				String lonStr=lonSStr.substring(0,lonSStr.length()-1);
				int st=Integer.parseInt(lonSStr.substring(lonSStr.length()-1), 16);
				BitSet bs=ByteUtils.byte2BitSet((byte)st);
				boolean isLoc=false;
				if(bs.get(1)){
					isLoc=true;
				}
				
				String ns="S";
				if(bs.get(2)){
					ns="N";
				}
				String ew="W";
				if(bs.get(3)){
					ew="E";
				}
				
				double lat=ByteUtils.stringToLat2(latStr, ns);
				double lon=ByteUtils.stringToLon2(lonStr, ew);
				
				
				String speedrad=ByteUtils.byteToString(b, from, 3);
				int speed=ByteUtils.stringToSpeed(speedrad.substring(0,3));
				int rad=Integer.parseInt(speedrad.substring(3));
				
				from+=3;
				String sta=ByteUtils.byteToString(b, from, 4);
				
				from+=7;
				Long assetIdL= Constant.imeiAssetMap.get(imei);  
				if(assetIdL!=null){
					long assetid=assetIdL;
					Asset ass=Constant.assetMap.get(assetid);
					String[] status=ByteUtils.stringToStatus(sta);
					GPSLocation gpsLoc=new GPSLocation();
					gpsLoc.setAssetid(assetid);
					gpsLoc.setLon(lon);
					gpsLoc.setLat(lat);
					gpsLoc.setSpeed(speed);
					gpsLoc.setRad(rad);
					gpsLoc.setGpstime(gpstime);
					gpsLoc.setImei(imei);
					gpsLoc.setLocStatus(isLoc);
					gpsLoc.setStatus(status[0]);
					gpsLoc.setAlarmType(Integer.parseInt(status[1]));
					gpsLoc.setCall(ass.getCallnum());
					gpsLoc.setName(ass.getName());
					//gpsLoc.setName(ass.getName()+"--bin");
					Constant.gpsList.add(gpsLoc);
					result.put(Constant.logInKey, imei);
					logger.debug(gpsLoc.toString());
				}else{
					logger.info("没有录入车辆信息,不能解析记录:"+imei);
				}
			}else if(b[from]==Constant.THH2){  //x信息
				if(imei1==null){
					logger.info("没有登录信息,不能解析X记录:"+b[from]);
					from+=32;
				}
				
				from+=1;
				String disStr=ByteUtils.byteToString(b,from,5);
				int dis=(int)(Integer.parseInt(disStr)*0.51444);
				from+=5;
				long gpstime=ByteUtils.byteToTime(b,from,6);
				from+=6;
				
				String latStr=ByteUtils.byteToString(b, from, 4);
				
				from+=4;
				int wendu=b[from];
				if(wendu<0){
					wendu=256+wendu;
				}
				wendu=wendu/2;
				if(wendu>125){
					wendu=0;
				}
				
				from+=1;
				
				String lonSStr=ByteUtils.byteToString(b, from, 5);
				from+=5;
				String lonStr=lonSStr.substring(0,lonSStr.length()-1);
				int st=Integer.parseInt(lonSStr.substring(lonSStr.length()-1), 16);
				BitSet bs=ByteUtils.byte2BitSet((byte)st);
				
				if(bs.get(0)){
					wendu=-wendu;
				}
				
				boolean isLoc=false;
				if(bs.get(1)){
					isLoc=true;
				}
				
				String ns="S";
				if(bs.get(2)){
					ns="N";
				}
				String ew="W";
				if(bs.get(3)){
					ew="E";
				}
				
				
				double lat=ByteUtils.stringToLat(latStr, ns);
				
				double lon=ByteUtils.stringToLon(lonStr, ew);
				
				
				String speedrad=ByteUtils.byteToString(b, from, 3);
				int speed=ByteUtils.stringToSpeed(speedrad.substring(0,3));
				int rad=Integer.parseInt(speedrad.substring(3));
				
				from+=3;
				String sta=ByteUtils.byteToString(b, from, 4);
				
				from+=7;
				Long assetIdL=Constant.imeiAssetMap.get(imei1);
				if(assetIdL!=null){
					long assetid=assetIdL;
					Asset ass=Constant.assetMap.get(assetid);
					String[] status=ByteUtils.stringToStatus(sta);
					GPSLocation gpsLoc=new GPSLocation();
					gpsLoc.setAssetid(assetid);
					gpsLoc.setLon(lon);
					gpsLoc.setLat(lat);
					gpsLoc.setSpeed(speed);
					gpsLoc.setRad(rad);
					gpsLoc.setGpstime(gpstime);
					gpsLoc.setImei(imei1);
					gpsLoc.setLocStatus(isLoc);
					gpsLoc.setStatus(status[0]);
					gpsLoc.setAlarmType(Integer.parseInt(status[1]));
					gpsLoc.setCall(ass.getCallnum());
					gpsLoc.setName(ass.getName());
					gpsLoc.setWendu(wendu);
					gpsLoc.setDis(dis);
					Constant.gpsList.add(gpsLoc);
				 
					logger.debug(gpsLoc.toString());
				}
			}
		}
		}catch(Exception ex){
			logger.error("error parse quickmapbin data:");
			logger.error(ex);
		}
		return result;
	}

	@Override
	public Map parseAlarm(byte[] b, int from) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map parseLocation(byte[] b, int from) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map parseLogin(byte[] b, int from) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map parseMessage(byte[] b, int from) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map parseQuestion(byte[] b, int from) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map parseStatus(byte[] b, int from) {
		// TODO Auto-generated method stub
		return null;
	}

}
