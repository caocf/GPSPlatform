package com.quickgis.gps.mongodb;

import java.io.InputStream; 
import java.util.Properties;
 
import com.mongodb.DB;
import com.mongodb.Mongo; 
import com.quickgis.gps.util.ConstantC;

public class MongodbConn { 
	static private   Mongo mongo;
    static String dbName;
    static String dbUser;
    static char[] dbPass;
	static synchronized public Mongo getMongo() { 
		if (mongo == null) {
			 MongodbConn mo=new MongodbConn();
		    mo.init();
		}
		return mongo;
	} 
	
	private   void init() { 
		InputStream is=null;
		try {
			 is = getClass().getResourceAsStream(
					"/Connectionmongodb.properties");
			Properties dbProps = new Properties();
			dbProps.load(is);
			 String host=dbProps.getProperty("host");
			 int port=Integer.parseInt(dbProps.getProperty("port"));
			 dbName=dbProps.getProperty("dbname"); 
			 dbUser=dbProps.getProperty("dbuser");
			 dbPass=dbProps.getProperty("dbpass").toCharArray();
		 
			mongo = new Mongo(host,port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(is!=null){
				try{
				is.close();
				}catch(Exception ex){}
			}
		} 
	}
	static synchronized public DB auth(DB db){
		if(!db.isAuthenticated()){
			boolean auth = db.authenticate(dbUser,dbPass);
			if(!auth){ 
				throw new RuntimeException(); 
			}
		}
		return db;
	}
}
