package com.quickgis.gps.decoder;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.quickgis.gps.bean.Asset;
import com.quickgis.gps.bean.GPSLocation;
 
import com.quickgis.gps.util.ByteUtils;
import com.quickgis.gps.util.Constant;

public class QuickMapAssDecoder implements Decoder {
	static Logger logger=Logger.getLogger(QuickMapAssDecoder.class);
	
	@Override
	public Map parse(byte[] b, int from,String imei1) {
		// TODO Auto-generated method stub
	/*	if(b[1]=='G'){
			return parseLocation(b, from);
		}
		return null;*/
		
		// TODO Auto-generated method stub
		String s=new String(b);
		System.out.println("==========QuickMapAssDecoder : "+s);  //------------------------
		Map result=new HashMap();
		try{
		String[] gps=StringUtils.split(s,"#");
		for(int i=0;i<gps.length;i++){
			String[] onegps=StringUtils.split(gps[i],",");
			if(onegps.length>10){
			String zhi=onegps[0];
			String imei=onegps[1];
			
			String cmd=onegps[2];
			int start=3;
			if(!cmd.equals(Constant.V1)){
				start=5;
			}
			long gpstime=ByteUtils.stringToTime(onegps[start],onegps[start+8]);
			String loc=onegps[start+1];
			boolean isLoc=true;
			if(loc.equals("V")){
				isLoc=false;
			}
			 
			double lat=ByteUtils.stringToLat(onegps[start+2], onegps[start+3]);
			double lon=ByteUtils.stringToLon(onegps[start+4], onegps[start+5]);
			int speed=ByteUtils.stringToSpeed(onegps[start+6]);
			int rad=Integer.parseInt(onegps[start+7]);
			String[] status=ByteUtils.stringToStatus(onegps[start+9]);
			Long assetIdL=Constant.imeiAssetMap.get(imei);
			if(assetIdL!=null){
				long assetid=assetIdL;
				Asset ass=Constant.assetMap.get(assetid); 
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
				//gpsLoc.setName(ass.getName()+"--ass");
				gpsLoc.setName(ass.getName());
				Constant.gpsList.add(gpsLoc);
				result.put(Constant.logInKey, imei);
				logger.debug(gpsLoc.toString());
			}
		 }
		}
		}catch(Exception ex){
			logger.error("error parse quickmapascii data:"+s );
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
