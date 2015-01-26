package com.quickgis.gps.test;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.quickgis.gps.util.Constant;

public class GpsDataAnalyze {
	//mongo---------------------------------------------------------
	static private Mongo  mongo ;
	static public DB db;
	static private DBCollection history;
	static private DBCollection lastHistory;
	static { 
		try {
			mongo = new Mongo("192.198.208.38",27017);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		db=mongo.getDB("gps");
		auth(db);
		history=db.getCollection(Constant.historyName);
    	lastHistory=db.getCollection(Constant.lasHistoryName); 
	} 
	static private void auth(DB db){
		if(!db.isAuthenticated()){
			char[] pwd_char = "gps".toCharArray(); 
			boolean auth = db.authenticate("gps",pwd_char);
			if(!auth){ 
				throw new RuntimeException(); 
			}
		}
	}
	//mongo---------------------------------------------------------
	
	public static void main(String[] args) {
		timeBetween2Gpsdata();
	}
	
	
	private static void timeBetween2Gpsdata(){
		
		
		 
		  DBObject qlh = new BasicDBObject();
		  qlh.put("aid","123");
		 // qlh.put("gt", new BasicDBObject("$gt", 1396339804016l).append("$lte", 1396350380926l));
		 // qlh.put("nam","添加测试--bin");
		  qlh.put("isl",true);
		  //qlh.put("t",new BasicDBObject("$gt", 0));
		  DBCursor cur = history.find(qlh).sort((DBObject)new BasicDBObject().put("gt", 1));
		  long lastGt = 0l;
		  long lastT = 0l;
		  int count = 0;
		  System.out.println("总计："+cur.length());
		  
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		  
		  Iterator<DBObject> it = cur.iterator();
		  while(it.hasNext()){
			  DBObject obj = it.next();
			  long gt = Long.parseLong(obj.get("gt").toString());
			  String date = df.format(new Date(gt));
			  
			  long t = Long.parseLong(obj.get("t").toString()); 
			  String date2 = df.format(new Date(t));
				
			  gt /= 1000;
			  t /= 1000;
			  if(gt == lastGt){
				  count++;
			  }else{
				  System.out.println("\tcount:" + count);
				  System.out.print("time:" + date);
				  System.out.print("\t" + gt);
				  System.out.print("\t" + (gt - lastGt));
				  System.out.print("\ttime:" + date2);
				  System.out.print("\t" + t);
				  System.out.print("\t" + (t - lastT));
				  System.out.print("");
				  count = 1;
				  lastGt = gt;
				  lastT = t;
			  }
		  }
		  System.out.println("\tcount:" + count);
	}
}
