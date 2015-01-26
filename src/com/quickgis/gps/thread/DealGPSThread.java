package com.quickgis.gps.thread;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;

import com.quickgis.gps.bean.AlarmParam;
import com.quickgis.gps.bean.Asset;
import com.quickgis.gps.bean.ChannelBean;
import com.quickgis.gps.bean.GPSLocation;
import com.quickgis.gps.bean.OnLine;
 
import com.quickgis.gps.bean.OverSpeedSet;
import com.quickgis.gps.mongodb.MongoDBOpe;
import com.quickgis.gps.tcp.NettyServerHandler;
import com.quickgis.gps.util.ChangeLonLat;
import com.quickgis.gps.util.Constant;
import com.quickgis.gps.util.ConstantC;
import com.vividsolutions.jts.geom.Geometry;

 
public class DealGPSThread  extends Thread {
	static Logger logger=Logger.getLogger(DealGPSThread.class);
	
	public void run(){
		
	  while(true){	 
		  try
	      { 
			  
			  Map<Channel,ChannelBean>  channel_Map=NettyServerHandler.getChannel_Map();
			  Map<String,Channel>  call_Map=NettyServerHandler.getCallLetter_Map();
			  Iterator<Channel> ite=channel_Map.keySet().iterator();
			  while(ite.hasNext()){
				  Channel ch=ite.next();
				  ChannelBean channelBean=channel_Map.get(ch);
				  long val=channelBean.getLastTime();
				  if(System.currentTimeMillis()-val>Constant.MAXUNACTIVE){
					  ite.remove();
					  String imei=channelBean.getKey(); 
				    	if(imei!=null){
				    	  String key=imei ;
				    	  Long assetid=Constant.imeiAssetMap.get(key);
				    	  if(assetid!=null){ 
				    		  Asset a=Constant.assetMap.get(assetid);
				    		 long time=ConstantC.getTodayTime();
				    		  OnLine on=Constant.onLineMap.get(assetid+"_"+time);
				    		  if(on!=null){
					    		  long lt=on.getLt();
					    		  if(lt>0){
					    			  on.setT((System.currentTimeMillis()-lt)/Constant.MILSECONEHOUR);
					    		  } 
					    		  on.setLt(0);
				    		  }
				    		  Asset asset=Constant.assetMap.get(assetid);
					    	  asset.setStatus(0);
					    	  Constant.offlineUnit.add(asset);
				    	  } 
				    	} 
			    		logger.info("car no active and log out :"+channelBean.getKey());
				  } 
			  }
			  logger.info("更新GPS数据:"+Constant.gpsList.size());
			while(Constant.gpsList.size()>0){ 
				GPSLocation location=Constant.gpsList.remove(0);
				boolean isGX=false;
				//对于googleMap和quickMap获取地理位置的方法
				//quickMap只能获取国内位置数据，可判断高速公路
				//googleMap能获取全球位置数据，但无法判断高速公路
				 /*String loc=ChangeLonLat.getQuickMapLocation(location.getLon(), location.getLat());
				 String[] locs=loc.split(",");
				 if(locs.length>1){ 
					 isGX=true;
				 }
				 location.setLoc(locs[0]); 
				 */
				String loc=ChangeLonLat.getGoogleLocation(location.getLon(), location.getLat());
				 location.setLoc(loc); 
				 
				 long assetid=location.getAssetid();
				 
				 long time=ConstantC.getTodayTime();
	    		  OnLine on=Constant.onLineMap.get(assetid+"_"+time);
	    		  if(on!=null){
	    			  long lt=on.getLt();
		    		  if(lt>0){
		    			  on.setT((System.currentTimeMillis()-lt)/Constant.MILSECONEHOUR);
		    		  } 
		    		  on.setLt(System.currentTimeMillis());
		    		  on.setLoc(location.getLoc());
		    		  on.setLon(location.getLon());
		    		  on.setLat(location.getLat());
	    		  }
	    		  
	    		  
				 OverSpeedSet overSpeed=Constant.overSpeedMap.get(assetid);
				 if(overSpeed!=null){
					 boolean isChaosu=false;
					 if(isGX){
						 isChaosu=location.getSpeed()>overSpeed.getSpeed2();
					 }else{
						 isChaosu=location.getSpeed()>overSpeed.getSpeed1();
					 }
					 
					 if(isChaosu){
							if(!Constant.overSpeed.containsKey(location.getAssetid())){ 
								Long l=Constant.removeOverSpeedAlarm.get(location.getAssetid());
								if(l==null){
									l=0L;
								}
								long curL=location.getGpstime();
								if(curL-l>Constant.MAXALARMLEN){  //距离上次报价10分钟 
							      location.setStartTime(location.getGpstime()); 
							      Constant.overSpeed.put(location.getAssetid(), location);
								}
							}else{
								GPSLocation alarmLocation=Constant.overSpeed.get(location.getAssetid());
								if(location.getGpstime()-alarmLocation.getGpstime()>Constant.OverSpeedAlarm&&alarmLocation.getAlarmId(Constant.OVERSPEEDALARM+"")<1){
								   long id=ConstantC.getUIdL();
								   MongoDBOpe db=new MongoDBOpe();
								   Map param=new HashMap();
								   param.put("sp1",overSpeed.getSpeed1());
								   param.put("sp2",overSpeed.getSpeed2());
								   db.writeAlarm(alarmLocation,location,Constant.OVERSPEEDALARM, id,param);
								   alarmLocation.setAlarmId(Constant.OVERSPEEDALARM+"", id);
								}
							}
						}else{
							if(Constant.overSpeed.containsKey(location.getAssetid())){
								GPSLocation alarmLocation=Constant.overSpeed.remove(location.getAssetid());
								 
								if(location.getGpstime()-alarmLocation.getGpstime()>Constant.OverSpeedAlarm){  //持续超速5分钟,设置为报警
									if(alarmLocation.getAlarmId(Constant.OVERSPEEDALARM+"")>0){
										 MongoDBOpe db=new MongoDBOpe(); 
										   db.updateAlarm(location, alarmLocation.getAlarmId(Constant.OVERSPEEDALARM+""));
									}else{
										  long id=ConstantC.getUIdL();
										   MongoDBOpe db=new MongoDBOpe();
										   Map param=new HashMap();
										   param.put("sp1",overSpeed.getSpeed1());
										   param.put("sp2",overSpeed.getSpeed2());
										   db.writeAlarm(alarmLocation,location,Constant.OVERSPEEDALARM, id,param);
									}
								   
								//	db.addOverSpeedAlarm(alarmLocation,location);
								   Constant.removeOverSpeedAlarm.put(alarmLocation.getAssetid(),location.getGpstime());
								}
							}
						}
				 }
				 List<AlarmParam> alarmParams=Constant.alarmParamMap.get(location.getAssetid());
				 if(alarmParams != null){
					 for(int i=0;i<alarmParams.size();i++){
						 AlarmParam alarm=alarmParams.get(i);
						 String key=location.getAssetid()+"_"+alarm.getAlarmtype()+"_"+alarm.getAlarmid();
						 switch(alarm.getAlarmtype()){
						 case Constant.KEYPOINTALARM:
							 Geometry curGeo=alarm.getGeoPoint(location.getLon(), location.getLat());
							 Geometry keyPoint=alarm.getGeo();
							 if(curGeo!=null&&keyPoint!=null){
								 double dis=curGeo.distance(keyPoint);
								 if(dis<Constant.MINPOINTDIS){  //进入关键点
									 if(!Constant.alarm.containsKey(key)){ 
										 Long l=Constant.removeAlarm.get(key);
										 if(l==null){
											 l=0L;
										 }
										 long curL=location.getGpstime();
										 if(curL-l>Constant.MAXALARMLEN){  //距离上次报价10分钟 
											 location.setStartTime(location.getGpstime()); 
											 Constant.alarm.put(key, location);
										 }
									 }else{
										 GPSLocation alarmLocation=Constant.alarm.get(key); 
										 if(location.getGpstime()-alarmLocation.getGpstime()>Constant.GeoFenceAlarm && alarmLocation.getAlarmId(alarm.getAlarmtype()+"")<1){
											 long id=ConstantC.getUIdL();
											 MongoDBOpe db=new MongoDBOpe();
											 Map param=new HashMap();
											 param.put("nam",alarm.getAlarmname());
											 param.put("pts",alarm.getAlarmpoints());
											 db.writeAlarm(alarmLocation,location,Constant.KEYPOINTALARM, id,param);
											 alarmLocation.setAlarmId(alarm.getAlarmtype()+"",id);
										 }
									 }  
								 }else{   //驶出关键点
									 if(Constant.alarm.containsKey(key)){
										 GPSLocation alarmLocation=Constant.alarm.remove(key);  
										 if(location.getGpstime()-alarmLocation.getGpstime()>Constant.GeoFenceAlarm ){
											 if(alarmLocation.getAlarmId(alarm.getAlarmtype()+"")<1){
												 long id=ConstantC.getUIdL();
												 MongoDBOpe db=new MongoDBOpe();
												 Map param=new HashMap();
												 param.put("nam",alarm.getAlarmname());
												 param.put("pts",alarm.getAlarmpoints());
												 db.writeAlarm(alarmLocation,location,Constant.KEYPOINTALARM, id,param);
												 alarmLocation.setAlarmId(alarm.getAlarmtype()+"",id);
											 }else{
												 MongoDBOpe db=new MongoDBOpe(); 
												 db.updateAlarm(location, alarmLocation.getAlarmId(alarm.getAlarmtype()+""));
											 }
											 Constant.removeAlarm.put(key,location.getGpstime());
										 }
									 }
								 }
							 }
							 break;
						 case Constant.PATHALARM:
							 curGeo=alarm.getGeoPoint(location.getLon(), location.getLat());
							 keyPoint=alarm.getGeo();
							 if(curGeo!=null&&keyPoint!=null){
								 double dis=curGeo.distance(keyPoint);
								 if(dis>Constant.MINDIS){  //偏离路线
									 if(!Constant.alarm.containsKey(key)){ 
										 Long l=Constant.removeAlarm.get(key);
										 if(l==null){
											 l=0L;
										 }
										 long curL=location.getGpstime();
										 if(curL-l>Constant.MAXALARMLEN){  //距离上次报价10分钟 
											 location.setStartTime(location.getGpstime()); 
											 Constant.alarm.put(key, location);
										 }
									 }else{
										 GPSLocation alarmLocation=Constant.alarm.get(key); 
										 if(location.getGpstime()-alarmLocation.getGpstime()>Constant.GeoFenceAlarm && alarmLocation.getAlarmId(alarm.getAlarmtype()+"")<1){
											 long id=ConstantC.getUIdL();
											 MongoDBOpe db=new MongoDBOpe();
											 Map param=new HashMap();
											 param.put("nam",alarm.getAlarmname());
											 param.put("pts",alarm.getAlarmpoints());
											 db.writeAlarm(alarmLocation,location,Constant.PATHALARM, id,param);
											 alarmLocation.setAlarmId(alarm.getAlarmtype()+"",id);
										 }
										 
									 }   
								 }else{   //返回线路
									 if(Constant.alarm.containsKey(key)){
										 GPSLocation alarmLocation=Constant.alarm.remove(key);  
										 if(location.getGpstime()-alarmLocation.getGpstime()>Constant.GeoFenceAlarm ){
											 if(alarmLocation.getAlarmId(alarm.getAlarmtype()+"")<1){
												 long id=ConstantC.getUIdL();
												 MongoDBOpe db=new MongoDBOpe();
												 Map param=new HashMap();
												 param.put("nam",alarm.getAlarmname());
												 param.put("pts",alarm.getAlarmpoints());
												 db.writeAlarm(alarmLocation,location,Constant.PATHALARM, id,param);
												 alarmLocation.setAlarmId(alarm.getAlarmtype()+"",id);
											 }else{
												 MongoDBOpe db=new MongoDBOpe(); 
												 db.updateAlarm(location, alarmLocation.getAlarmId(alarm.getAlarmtype()+""));
											 }
											 Constant.removeAlarm.put(key,location.getGpstime());
										 }
									 }
								 }
							 }
							 break;
						 case Constant.INALARM:
						 case Constant.OUTALARM:
						 case Constant.INOUTALARM:
							 curGeo=alarm.getGeoPoint(location.getLon(), location.getLat());
							 keyPoint=alarm.getGeo();
							 if(curGeo!=null&&keyPoint!=null){
								 boolean isIn=false;
								 if(alarm.getGeotype()==Constant.MAPPOLYGON||alarm.getGeotype()==Constant.MAPRECT){
									 isIn=keyPoint.contains(curGeo);
								 }else if(alarm.getGeotype()==Constant.MAPCIRCLE){
									 isIn=keyPoint.distance(curGeo)<alarm.getRadius();
								 }
								 
								 if(isIn ){  //在geofence
									 if(alarm.getAlarmtype()==Constant.INALARM||alarm.getAlarmtype()==Constant.INOUTALARM){ //驶入报警开始
										 if(!Constant.alarm.containsKey(key)){ 
											 Long l=Constant.removeAlarm.get(key);
											 if(l==null){
												 l=0L;
											 }
											 long curL=location.getGpstime();
											 if(curL-l>Constant.MAXALARMLEN){  //距离上次报价10分钟 
												 location.setStartTime(location.getGpstime()); 
												 Constant.alarm.put(key, location);
											 }
										 }else{
											 GPSLocation alarmLocation=Constant.alarm.get(key); 
											 if(location.getGpstime()-alarmLocation.getGpstime()>Constant.GeoFenceAlarm && alarmLocation.getAlarmId(alarm.getAlarmtype()+"")<1){
												 long id=ConstantC.getUIdL();
												 MongoDBOpe db=new MongoDBOpe();
												 Map param=new HashMap();
												 param.put("nam",alarm.getAlarmname());
												 param.put("pts",alarm.getAlarmpoints());
												 
												 db.writeAlarm(alarmLocation,location,alarm.getAlarmtype(), id,param);
												 alarmLocation.setAlarmId(alarm.getAlarmtype()+"",id);
											 }
										 }
									 }
									 if(alarm.getAlarmtype()==Constant.OUTALARM||alarm.getAlarmtype()==Constant.INOUTALARM){   //驶出报警结束
										 if(Constant.outAlarm.containsKey(key)){
											 GPSLocation alarmLocation=Constant.outAlarm.remove(key);  
											 if(location.getGpstime()-alarmLocation.getGpstime()>Constant.GeoFenceAlarm ){
												 if(alarmLocation.getAlarmId(alarm.getAlarmtype()+"")<1){
													 long id=ConstantC.getUIdL();
													 MongoDBOpe db=new MongoDBOpe();
													 Map param=new HashMap();
													 param.put("nam",alarm.getAlarmname());
													 param.put("pts",alarm.getAlarmpoints());
													 db.writeAlarm(alarmLocation,location,alarm.getAlarmtype(), id,param);
													 alarmLocation.setAlarmId(alarm.getAlarmtype()+"",id);
												 }else{
													 MongoDBOpe db=new MongoDBOpe(); 
													 db.updateAlarm(location, alarmLocation.getAlarmId(alarm.getAlarmtype()+""));
												 }
												 Constant.removeOutAlarm.put(key,location.getGpstime());
											 }
										 }
									 }
								 }else{   //不在geofence
									 if(alarm.getAlarmtype()==Constant.OUTALARM||alarm.getAlarmtype()==Constant.INOUTALARM){ //驶出报警开始
										 if(!Constant.outAlarm.containsKey(key)){ 
											 Long l=Constant.removeOutAlarm.get(key);
											 if(l==null){
												 l=0L;
											 }
											 long curL=location.getGpstime();
											 if(curL-l>Constant.MAXALARMLEN){  //距离上次报价10分钟 
												 location.setStartTime(location.getGpstime()); 
												 Constant.outAlarm.put(key, location);
											 }
										 }else{
											 GPSLocation alarmLocation=Constant.outAlarm.get(key); 
											 if(location.getGpstime()-alarmLocation.getGpstime()>Constant.GeoFenceAlarm && alarmLocation.getAlarmId(alarm.getAlarmtype()+"")<1){
												 long id=ConstantC.getUIdL();
												 MongoDBOpe db=new MongoDBOpe();
												 Map param=new HashMap();
												 param.put("nam",alarm.getAlarmname());
												 param.put("pts",alarm.getAlarmpoints());
												 
												 db.writeAlarm(alarmLocation,location,alarm.getAlarmtype(), id,param);
												 alarmLocation.setAlarmId(alarm.getAlarmtype()+"",id);
											 }
										 }
									 }
									 if(alarm.getAlarmtype()==Constant.INALARM||alarm.getAlarmtype()==Constant.INOUTALARM){  //驶入报警结束
										 if(Constant.alarm.containsKey(key)){
											 GPSLocation alarmLocation=Constant.alarm.remove(key);  
											 if(location.getGpstime()-alarmLocation.getGpstime()>Constant.GeoFenceAlarm ){
												 if(alarmLocation.getAlarmId(alarm.getAlarmtype()+"")<1){
													 long id=ConstantC.getUIdL();
													 MongoDBOpe db=new MongoDBOpe();
													 Map param=new HashMap();
													 param.put("nam",alarm.getAlarmname());
													 param.put("pts",alarm.getAlarmpoints());
													 db.writeAlarm(alarmLocation,location,alarm.getAlarmtype(), id,param);
													 alarmLocation.setAlarmId(alarm.getAlarmtype()+"",id);
												 }else{
													 MongoDBOpe db=new MongoDBOpe(); 
													 db.updateAlarm(location, alarmLocation.getAlarmId(alarm.getAlarmtype()+""));
												 }
												 Constant.removeAlarm.put(key,location.getGpstime());
											 }
										 }
									 } 
								 }
							 }
							 break;
						 }
					 }
				 }
				Constant.gpsList2.add(location);
			} 
		  sleep(1000L);
	     }
	      catch (Exception e) {
	        e.printStackTrace();
	      }
		
	  }
	}
}
