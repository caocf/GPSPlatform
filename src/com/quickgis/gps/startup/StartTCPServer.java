package com.quickgis.gps.startup;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.quickgis.gps.tcp.NettyServer;
 
import com.quickgis.gps.thread.DealGPSThread;
import com.quickgis.gps.thread.UpdateDataThread;

public class StartTCPServer {
	static NettyServer  nettyServer; 
	 
	static Logger logger=Logger.getLogger(StartTCPServer.class);
	
	public void startup(int port ){
		nettyServer=NettyServer.getInstance(port);   
	}
	public void stop(){
		try {
			nettyServer.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
	}
	
	 
	
	 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		StartTCPServer startServer=new StartTCPServer();
		InputStream is=null;
		int port =8089;
		try {
			 is = startServer.getClass().getResourceAsStream(
					"/init.properties");
			Properties dbProps = new Properties();
			dbProps.load(is);
			  port =Integer.parseInt(dbProps.getProperty("tcpport"));
			  
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
		startServer.startup(port);
		UpdateDataThread updateThread=new UpdateDataThread();
		updateThread.start();
		
		DealGPSThread dealGPS=new DealGPSThread();
		dealGPS.start();
	}

}
