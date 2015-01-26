package com.quickgis.gps.mongodb;
 
 
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection; 
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.quickgis.gps.bean.Asset;
import com.quickgis.gps.bean.GPSLocation;
import com.quickgis.gps.bean.OnLine;
import com.quickgis.gps.bean.SendCmd;
import com.quickgis.gps.db.DBOperator;
import com.quickgis.gps.util.Constant;
import com.quickgis.gps.util.ConstantC;
 
 
 
public class MongoDBOpe {
	static Logger logger=Logger.getLogger(MongoDBOpe.class);
	
	public Mongo  mongo;
 
	String dbName;
	
	public long maxCmdId;
	
	 public MongoDBOpe(){  
  	       mongo=MongodbConn.getMongo();
  	       dbName=MongodbConn.dbName;
    }
	 
	 public void getCmdToSend(){
		 logger.info("查询发送的命令");
		DB dbase=mongo.getDB(dbName);
		dbase = MongodbConn.auth(dbase);
		DBCollection command=dbase.getCollection(Constant.cmd);  
        DBCursor dbCursor = command.find(); 
       while(dbCursor.hasNext()){
        	DBObject db=dbCursor.next();
        	long id=Long.parseLong(db.get("id").toString());
        	long aid=Long.parseLong(db.get("aid").toString());
        	String call=db.get("cal").toString();
        	String name=db.get("nam").toString();
        	int loctype=Integer.parseInt(db.get("lty").toString());
        	int cmdtype=Integer.parseInt(db.get("cty").toString());
        	Map param=(Map)db.get("par");
        	
        	SendCmd cmd=new SendCmd(id,aid,call,name,loctype,cmdtype,param);
        	logger.debug("发送指令："+id+","+aid+","+call+","+name+","+loctype+","+cmdtype+","+param);
        	Constant.sendCmds.add(cmd);
        	command.remove(db);
            //dbCursor.remove();
       }
	 }
	 
	 public void writeHistory(){
		 logger.info("写入历史数据:"+Constant.gpsList2.size());
		 DB dbase=mongo.getDB(dbName);
		 dbase = MongodbConn.auth(dbase);
			DBCollection history=dbase.getCollection(Constant.historyName);  
			DBCollection lastHis=dbase.getCollection(Constant.lasHistoryName);  
		 while(Constant.gpsList2.size()>0){
			 GPSLocation loc=Constant.gpsList2.remove(0);
			 
			 
			 BasicDBObject data = new BasicDBObject(); 
	    	 
	    	  data.put("aid", loc.getAssetid()+"");
	    	  data.put("gt",loc.getGpstime());
	    	  data.put("t", System.currentTimeMillis());
	    	  data.put("lon", loc.getLon());
	    	  data.put("lat", loc.getLat());
	    	  data.put("spe", loc.getSpeed());
	    	  data.put("rad", loc.getRad());
	    	  data.put("cal",loc.getCall());
	    	  data.put("nam", loc.getName());
	    	  data.put("loc", loc.getLoc());
	    	  data.put("dis", loc.getDis());
	    	  data.put("wen", loc.getWendu());
	    	  data.put("isl",loc.isLocStatus());
	    	  data.put("isa", loc.getAlarmType()>0?true:false);
	    	  data.put("alt", loc.getAlarmType());
	    	  data.put("sta", loc.getStatus());
	    	  history.insert(data);
	    	  //最后位置仅保存有效位置数据
	    	  if(loc.isLocStatus()){
	    		  DBObject queryCondition = new BasicDBObject();   
	    		  queryCondition.put("aid",  loc.getAssetid()+""); 
	    		  
	    		  if(data.containsField("_id")){  //_id无法修改会导致写数据报错
	    			  data.remove("_id");
	    		  }
	    		  //当前gps时间大于最后位置gps时间时才更新记录
	    		  DBObject qlh = new BasicDBObject();
	    		  qlh.put("aid",loc.getAssetid()+"");
	    		  qlh.put("gt", new BasicDBObject("$gte", loc.getGpstime()));
	    		  DBCursor cur = lastHis.find(qlh);
	    		  if(!cur.hasNext()){
	    			  DBObject db=lastHis.findAndModify(queryCondition, data);
	    			  if(db==null){
	    				  lastHis.insert(data);
	    			  }
	    		  }
	    	  }
	    		logger.debug("写入历史数据："+loc.getAssetid()+","+loc.getGpstime()+","+loc.getLon()+","+ loc.getLat()+","+loc.getSpeed()
	    				+","+loc.getRad()+","+loc.getCall()+","+loc.getName()+","+loc.getLoc()+","+loc.getDis()+","+loc.getAlarmType()
	    				+","+loc.getStatus());
		 }
	 }
	 
	 public void writeMess(){
		 
	 }
	 
	 public void writeCmdBack(SendCmd cmd,boolean suc){
		    DB dbase=mongo.getDB(dbName);
		    dbase = MongodbConn.auth(dbase);
			DBCollection cmdBack=dbase.getCollection(Constant.cmdback);   
			DBCollection cmdCol=dbase.getCollection(Constant.cmd);   
			
			 BasicDBObject data = new BasicDBObject();   
	    	  data.put("sta", suc); 
	    	  DBObject queryCondition = new BasicDBObject();   
	          queryCondition.put("id", cmd.getId()); 
	           
	     	 DBObject updatedValue = new BasicDBObject(); 
	    	  updatedValue.put("$set", data);   
	    	 
	    	  cmdCol.update(queryCondition, updatedValue);
	    	  
	    	  data = new BasicDBObject();   
	    	  data.put("sta", suc);   
	    	  data.put("cid", cmd.getId()+"");
	    	  data.put("nam", cmd.getName());
	    	  data.put("cal", cmd.getCall());
	    	  data.put("aid", cmd.getAssetid());
	    	  data.put("cty", cmd.getCmdtype());
	    	  data.put("t", System.currentTimeMillis());

	    	  cmdBack.insert(data);
	    	  
	 }
	 
	 public void writeAlarm(GPSLocation loc,GPSLocation loc2,int alty,long id,Map param){
		 logger.info("写入警情数据");
		  DB dbase=mongo.getDB(dbName);
		  dbase = MongodbConn.auth(dbase);
			DBCollection alarm=dbase.getCollection(Constant.alarmCol);   
			 BasicDBObject data = new BasicDBObject(); 
			 data.put("id",id);
	    	  data.put("aid", loc.getAssetid()+"");
	    	  data.put("gt",loc.getGpstime());
	    	  data.put("t", System.currentTimeMillis());
	    	  data.put("lon", loc.getLon());
	    	  data.put("lat", loc.getLat());
	    	  data.put("spe", loc.getSpeed());
	    	  data.put("rad", loc.getRad());
	    	  data.put("cal",loc.getCall());
	    	  data.put("nam", loc.getName());
	    	  data.put("all", loc.getLoc()); 
	    	  //data.put("alt", loc.getAlarmType()); 
	    	  data.put("alt", alty); 
	    	  
	    	  data.put("gt2",loc2.getGpstime()); 
	    	  data.put("lon2", loc2.getLon());
	    	  data.put("lat2", loc2.getLat());
	    	  data.put("spe2", loc2.getSpeed());
	    	  data.put("rad2", loc2.getRad()); 
	    	  data.put("all2", loc2.getLoc()); 
	    	  
	    	  data.put("par", param); 
	    	  alarm.insert(data);
	    		logger.debug("写入警情："+loc.getAssetid()+","+loc.getGpstime()+","+loc.getLon()+","+ loc.getLat()+","+loc.getSpeed()
	    				+","+loc.getRad()+","+loc.getCall()+","+loc.getName()+","+loc.getLoc()+","+loc.getDis()+","+loc.getAlarmType()
	    				+","+loc.getStatus());
	 }
	 
	 public void updateAlarm(GPSLocation loc,long id){
		 logger.info("更新警情数据");
		    DB dbase=mongo.getDB(dbName);
		    dbase = MongodbConn.auth(dbase);
			DBCollection alarm=dbase.getCollection(Constant.alarmCol);   
			 
			 BasicDBObject data = new BasicDBObject();  
	    	  
	    	  data.put("gt2",loc.getGpstime());  
	    	  data.put("lon2", loc.getLon());
	    	  data.put("lat2", loc.getLat());
	    	  data.put("spe2", loc.getSpeed());
	    	  data.put("rad2", loc.getRad()); 
	    	  data.put("all2", loc.getLoc()); 
	    	  DBObject queryCondition = new BasicDBObject();   
	          queryCondition.put("id", id); 
	          
	          
	     	 DBObject updatedValue = new BasicDBObject(); 
	    	  updatedValue.put("$set", data);   
	    	 
	    	  alarm.update(queryCondition, updatedValue);
	    	  logger.debug("更新警情："+loc.getAssetid()+","+loc.getGpstime()+","+loc.getLon()+","+ loc.getLat()+","+loc.getSpeed()
	    				+","+loc.getRad()+","+loc.getCall()+","+loc.getName()+","+loc.getLoc()+","+loc.getDis()+","+loc.getAlarmType()
	    				+","+loc.getStatus());
	 }
	 
	 
	 public void getOnLineData(){
		 logger.info("查询车辆在线数据");
		 DB dbase=mongo.getDB(dbName);
		 dbase = MongodbConn.auth(dbase);
		 DBCollection online=dbase.getCollection(Constant.online);  
		  DBObject queryCondition = new BasicDBObject();  
		  long t=ConstantC.getTodayTime();
          queryCondition.put("t",t); 
          
	        DBCursor dbCursor = online.find(); 
	       while(dbCursor.hasNext()){
	        	DBObject db=dbCursor.next();
	        	 
	        	long aid=Long.parseLong(db.get("aid").toString());
	            long lt=Long.parseLong(db.get("lt").toString());
	            double ont=Double.parseDouble(db.get("ont").toString());
	        	OnLine on=new OnLine(aid,lt,ont,t);
	        	Constant.onLineMap.put(aid+"_"+t, on);
	        	 logger.debug("查询车辆在线数据："+aid+","+t+","+ont);
	       }
	 }
	 
	 public void updateOnLineData(){
		 logger.info("更新车辆在线数据");
		 DB dbase=mongo.getDB(dbName);
		 dbase = MongodbConn.auth(dbase);
		 DBCollection online=dbase.getCollection(Constant.online);  
		 Iterator<String> ite=Constant.onLineMap.keySet().iterator();
		 long curDate=ConstantC.getTodayTime();
		 while(ite.hasNext()){
			 String key=ite.next();
			 OnLine on=Constant.onLineMap.get(key);
			 Asset asset=Constant.assetMap.get(on.getAid());
			 if(!key.equals(on.getAid()+"_"+curDate)){
				 ite.remove();
			 }
			 
			 BasicDBObject data = new BasicDBObject(); 
			 
	    	  data.put("aid", asset.getId()+"");
	    	 
	    	  data.put("cal",asset.getCallnum());
	    	  data.put("nam", asset.getName());
	    	  data.put("lon",on.getLon() );
	    	  data.put("lat",on.getLat() );
	    	  data.put("loc",on.getLoc() );
	    	  data.put("lt", on.getLt());
	    	  data.put("ont", on.getT());
	    	  data.put("t", on.getDt());
	    	  
	     	DBObject updatedValue = new BasicDBObject();  
	    	 updatedValue.put("$set", data); 
	    	 
	    	 DBObject queryCondition = new BasicDBObject();   
	         queryCondition.put("aid", on.getAid()+"" ); 
	         queryCondition.put("t", on.getDt()); 
	   	     
	         DBObject db=online.findAndModify(queryCondition, updatedValue);
	         if(db==null){
	        	 online.insert(data);
	         }
	         logger.debug("更新车辆在线数据："+on.getAid()+","+on.getT()+","+on.getT());
		 }
	 }
	 
	 public static void main(String[] args){
		 MongoDBOpe mogodb=new MongoDBOpe();
		 DB dbase=mogodb.mongo.getDB(mogodb.dbName); 
		 dbase = MongodbConn.auth(dbase);
		 BasicDBObject data = new BasicDBObject(); 
    	 GPSLocation loc=new GPSLocation();
    	 loc.setAssetid(2);
    	 loc.setGpstime(System.currentTimeMillis());
    	 
    	 DBObject updatedValue = new BasicDBObject();

         

         
    //	  data.put("lat",23.1);
   	     data.put("lon", 115.1);
   	    // data.put("aid", 2);
	     
   	     updatedValue.put("$set", data);   
   	 
		 DBCollection lastHis=dbase.getCollection(Constant.lasHistoryName); 
		 
		 DBObject queryCondition = new BasicDBObject();   
         queryCondition.put("aid",  loc.getAssetid() ); 
   	     
         lastHis.findAndModify(queryCondition, updatedValue);
		 
	 }
}
