package com.quickgis.gps.util;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader; 
 
import java.net.URL;
import java.net.URLConnection; 

import org.apache.log4j.Logger;

 

public class ChangeLonLat  { 
	
 
	 static String googleGecoder="http://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&sensor=false&language=%s";
     static String quickMap="http://122.115.47.103:82/location?op=1&points=%s,%s&isG=true";
    static Logger logger=Logger.getLogger(ChangeLonLat.class);
	
	public ChangeLonLat() { 
	}
	 
	   
	
	public static String getGoogleLocation( double lon,double lat){
		String result=null;
		try{ 
		 URL url = new URL( String.format(googleGecoder,lat, lon,"en"));
		 System.out.println(url);
		 URLConnection connection = url.openConnection(); 
		 
		 String sCurrentLine="";
		  
		 InputStream l_urlStream;
		 l_urlStream = connection.getInputStream();
		 BufferedReader l_reader = new BufferedReader(new InputStreamReader(
		         l_urlStream,"UTF-8"));
		// logger.info("get google location :"+  lon+"  "+lat);
		 while ((sCurrentLine = l_reader.readLine()) != null) {
			 //logger.info("get google location :"+sCurrentLine);
			 if(sCurrentLine.indexOf("formatted_address")>0){
				 int ind=sCurrentLine.indexOf(":");
				 int end=sCurrentLine.lastIndexOf("\"");
				 
				 if(ind>0){
					 result=sCurrentLine.substring(ind+3,end);
				 }
				 break;
			 } 
		     
		 }  
		 
		 l_reader.close();
		 l_urlStream.close();
		 logger.info("get google location :"+lon+","+lat+","+result);
		}catch(Exception ex){
			ex.printStackTrace();
			 logger.error("get google location error :"+ex.getMessage()+","+lon+","+lat);
		}
		 return result;
     }
	public static String getQuickMapLocation( double lon,double lat){
		String result="";
		try{ 
		 URL url = new URL( String.format(quickMap,lon, lat));
		 URLConnection connection = url.openConnection(); 
		 
		 String sCurrentLine="";
		  
		 InputStream l_urlStream;
		 l_urlStream = connection.getInputStream();
		 BufferedReader l_reader = new BufferedReader(new InputStreamReader(
		         l_urlStream,"UTF-8"));
	//	 logger.info("get quickmap location :"+  lon+"  "+lat);
		 sCurrentLine = l_reader.readLine() ;
	//		 logger.info("get quickmap location :"+sCurrentLine); 
			 String [] lines=sCurrentLine.split(",");
			 if(lines.length>2){
			   result=lines[2]; 
		    }  
		 
		 l_reader.close();
		 l_urlStream.close();
		 logger.info("get quickmap  location :"+lon+","+lat+","+result);
		}catch(Exception ex){
			ex.printStackTrace();
			 logger.error("get quickmap location error :"+ex.getMessage()+","+lon+","+lat);
		}
		 return result;
     }
	 
	 
	 
	 
	
	public static void main(String[] args){
		String str=getGoogleLocation( 114.04935058594,  22.563379720052);
		System.out.println("get google locatin:"+str);
		
		str=getQuickMapLocation( 114.04935058594,  22.563379720052);
		System.out.println("get quickmap locatin:"+str);
		/*
		double lng = -117.955278;
		double lat = 34.014738;
		String str=getGoogleLocation( lng, lat);
		System.out.println("get google locatin:"+str);
		
		str=getQuickMapLocation( lng, lat);
		System.out.println("get quickmap locatin:"+str);*/
  
	}

}
