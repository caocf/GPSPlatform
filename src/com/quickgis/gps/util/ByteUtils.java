package com.quickgis.gps.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ByteUtils {
    private static final int MAXBYTE=256; 
	 
	public static String byteToString(byte[] b,int from ,int len){
		StringBuffer sb=new StringBuffer();
		for(int i=from ;i<len+from;i++){
			int t=b[i];
			if(t<0){
				t=MAXBYTE+t;
			}
			String tmp=Integer.toHexString(t);
			if(tmp.length()==1&&i>from){
			  sb.append("0").append(tmp);
		   }else{
			   sb.append(tmp);
		   }
		}
		return sb.toString();
		
	}
	
	public static int hexToInt(int i){ 
		if(i<0){
			i=MAXBYTE+i;
		}
		String tmp=Integer.toHexString(i);
		return Integer.parseInt(tmp);
	}
	
	public static String byteToString2(byte[] b ){
		StringBuffer sb=new StringBuffer();
		for(int i=0 ;i<b.length;i++){			 
			   sb.append(b[i]);		    
		}
		return sb.toString();
		
	}
	
	public static String byteToString3(byte[] b ){
		StringBuffer sb=new StringBuffer();
		for(int i=0 ;i<b.length;i++){	
				int t=b[i];
				if(t<0){
					t=MAXBYTE+t;
				}
				String s=Integer.toString(t, 16);
				if(s.length()<2){
					 sb.append("0").append(s).append(" ");		
				}else{
					 sb.append(s).append(" ");		
				}     
		}
		return sb.toString();
		
	}
	public static long byteToTime(byte[] b,int from ,int len){ 
			int h=hexToInt(b[from]);
			 
			//h+=8;   时间不转为北京时间
			
			int m=hexToInt(b[from+1]);
			 
			int s=hexToInt(b[from+2]);
			 
		 
			int d=hexToInt(b[from+3]);
			 
			
			int mo=hexToInt(b[from+4]);
			 
			
			int y=hexToInt(b[from+5]);
			 
			y+=2000;
			
			//转为标准时间
			Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+0000"));
			c1.set(y, mo-1, d, h, m, s); 
		
			return c1.getTimeInMillis(); 
	}
	
	public static String jutf82Cutf8(String s){
		byte[] b;
		try {
			b = s.getBytes("UTF-8");
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<b.length;i++){
				sb.append(b[i]|256);
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	
	public static int byte2Int(byte b[],int from) {
		int s = 0;
		s = ((((b[from] & 0xff) << 8 | (b[from+1] & 0xff)) << 8) | (b[from+2] & 0xff)) << 8
				| (b[from+3] & 0xff);
		return s;
	}
	
	/*
	 * 将15位imei号，转成byte，
	 */
	public static byte[] stringToByte(String imei){
		imei="0"+imei;
		int len=imei.length()/2;
		byte [] b=new byte[len];
		for(int i=0;i<len;i++){
			int a=Integer.parseInt(imei.substring(i*2,i*2+2),16);
			 
			b[i]= (byte)(a);
		}
		return b;
	}
	
	
	public static void int22bytes(int n,byte[] b,int from) { 
		b[from] = (byte) ((n & 0xFF00) >> 8);
		b[from+1] = (byte) (n & 0xFF); 
	}
	
	
	public static void int2bytes(int n,byte[] b,int from) {
		 
		b[from] = (byte) (0xff & n);
		b[from+1] = (byte) ((0xff00 & n) >> 8);
		b[from+2] = (byte) ((0xff0000 & n) >> 16);
		b[from+3] = (byte) ((0xff000000 & n) >> 24);
		
	}
	
	public static String byteToTime(byte[] b,int from){
		int year=b[from]+2000;
		int month=b[from+1];
		int day=b[from+2];
		int hour=b[from+3];
		int minute=b[from+4];
		int second=b[from+5];
		StringBuffer sb=new StringBuffer();
		sb.append(year).append("-").append(month).append("-").append(day).append(" ").append(hour).append(":").append(minute).append(":").append(second);
		return sb.toString();
	}
	
	public static long stringToTime(String time, String date){
		
		int year=Integer.parseInt(date.substring(4))+2000;
		int month=Integer.parseInt(date.substring(2,4));
		int day=Integer.parseInt(date.substring(0,2));
		int hour=Integer.parseInt(time.substring(0,2));
		int minute=Integer.parseInt(time.substring(2,4));
		int second=Integer.parseInt(time.substring(4));
		
		   Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+0000"));
		   c1.set(year, month-1, day, hour, minute, second); 
	
		  return c1.getTimeInMillis();
	}
	
	public static long stringToTime(String datatime){
		
		int year=Integer.parseInt(datatime.substring(0,2))+100;
		int month=Integer.parseInt(datatime.substring(2,4))-1;
		int day=Integer.parseInt(datatime.substring(4,6));
		int hour=Integer.parseInt(datatime.substring(6,8));
		int minute=Integer.parseInt(datatime.substring(8,10));
		int second=Integer.parseInt(datatime.substring(10));
		Date d=new Date(year,month,day,hour,minute,second);
	
		return d.getTime();
	}
	
     public static double stringToLat(String latS, String ns){
		
	   int latI=Integer.parseInt(latS.substring(0,2));
	   double latF=Double.parseDouble(latS.substring(2));
	   double lat=latI+latF/60;
	   if(ns.equals("S")){
		   lat=-lat;
	   }
		return lat;
	}
     public static double stringToLon(String lonS, String ew){
 		
  	   int lonI=Integer.parseInt(lonS.substring(0,3));
  	   double lonF=Double.parseDouble(lonS.substring(3));
  	   double lon=lonI+lonF/60;
  	   if(ew.equals("W")){
  		   lon=-lon;
  	   }
  		return lon;
  	}
     
     public static double stringToLat2(String latS, String ns){
 		
  	   int latI=Integer.parseInt(latS.substring(0,2));
  	   double latF=Double.parseDouble(latS.substring(2));
  	   double lat=latI+latF/600000;
  	   if(ns.equals("S")){
  		   lat=-lat;
  	   }
  		return lat;
  	}
       public static double stringToLon2(String lonS, String ew){
   		
    	   int lonI=Integer.parseInt(lonS.substring(0,3));
    	   double lonF=Double.parseDouble(lonS.substring(3));
    	   double lon=lonI+lonF/600000;
    	   if(ew.equals("W")){
    		   lon=-lon;
    	   }
    		return lon;
    	}
	
     public static int stringToSpeed(String speed){
 	   if(speed==null||speed.equals("")){
 		   return 0;
 	   }
 	   double d=Double.parseDouble(speed)*Constant.JIETOKM;
 	    
  		return (int)d;
  	}
     
     public static String[] stringToStatus(String sta){
   	   StringBuffer sb=new StringBuffer();
   	  
   	   BitSet bs=byte2BitSet(Integer.parseInt(sta.substring(0,2),16));
   	   
   	   List<Integer> list=new ArrayList<Integer>();
   	   int alarmtype=0;
   	   for(int i=0;i<8;i++){
   		   if(!bs.get(i)){
   			   list.add(4+i);
   		   }
   	   }
   	   bs=byte2BitSet(Integer.parseInt(sta.substring(2,4),16));
   	   int add=12;
	   	for(int i=0;i<8;i++){
	   		  if(i==2){
	   			add=11;
	   			  continue;	   			  
	   		  }
			   if(!bs.get(i)){
				    
				   list.add(add+i);
			   }
		   }
	   	
	    bs=byte2BitSet(Integer.parseInt(sta.substring(4,6),16));
	   	     add=19;
		   	for(int i=0;i<8;i++){
		   		  if(i==3||i==4){
		   			add=17;
		   			  continue;	   			  
		   		  }
				   if(!bs.get(i)){ 
					   list.add(add+i);
				   }
			   }
		   	
		    bs=byte2BitSet(Integer.parseInt(sta.substring(6),16));
	   	    
		   	for(int i=0;i<8;i++){
		   		 
				   if(!bs.get(i)){ 
					   alarmtype=25+i;
					   list.add(25+i);
				   }
			   }
		   	for(int i=0;i<list.size();i++){
		   		sb.append(list.get(i)).append(",");
		   	}
		   	if(sb.length()>1){
		   		sb.deleteCharAt(sb.length()-1);
		   	}
		   	String[] str=new String[2];
		   	str[0]=sb.toString();
		   	str[1]=alarmtype+"";
   	   return str;
    }
	public static int[] byte2Bit(byte b){
		 BitSet bits = new BitSet(8);
		    for (int i = 0; i < 8; i++)
		    {
		        bits.set(7-i, (b & 1) == 1); 
		        b >>= 1;
		    }
		    int i1=0;
		   for(int i=0;i<4;i++){
			   if(bits.get(i)){
				   i1=i1*2+1;
			   }else{
				   i1=i1*2;
			   }
		   } 
		   int i2=0;
		   for(int i=4;i<8;i++){
			   if(bits.get(i)){
				   i2=i2*2+1;
			   }else{
				   i2=i2*2;
			   }
		   } 
		 int[] res=new int[2];
		 res[0]=i1;
		 res[1]=i2;
		 return res;
	}
	
	public static BitSet byte2BitSet(int b){
	    BitSet bits = new BitSet(8);
	    for (int i = 0; i < 8; i++)
	    {
	        bits.set(i, (b & 1) == 1); 
	        
	        b >>= 1;
	       
	    }
	    return bits;
	}
	
	public static String byte2Mes(byte b){
		    BitSet bits = new BitSet(8);
		    for (int i = 0; i < 8; i++)
		    {
		        bits.set(7-i, (b & 1) == 1); 
		       // System.out.println((b & 1) == 1);
		        b >>= 1;
		       
		    }
		   StringBuffer sb=new StringBuffer();
		   if(bits.get(0)){
			  sb.append(Constant.cheFang).append(",");   
		   }else{
			   sb.append(Constant.sheFang).append(",");  
		   }
		   
		   if(bits.get(1)){ 
			   sb.append(Constant.accGao).append(",");   
		   }else{
			   sb.append(Constant.accDi).append(",");  
		   }
		   
		   if(bits.get(2)){ 
			   sb.append(Constant.dianYuan1).append(",");   
		   }else{
			   sb.append(Constant.dianYuan0).append(",");  
		   }
		   
		   int res=0;
		   for(int i=3;i<5;i++){
			   if(bits.get(i)){
				   res=res*2+1;
			   }else{
				   res=res*2;
			   }
		   }
		   
		   if(res==0){
			   sb.append(Constant.STATUS1).append(",");   
		   }else if(res==1){
			   sb.append(Constant.STATUS2).append(",");   
		   }else if(res==2){
			   sb.append(Constant.STATUS3).append(",");   
		   }else if(res==3){
			   sb.append(Constant.STATUS4).append(",");   
		   }else if(res==4){
			   sb.append(Constant.STATUS5).append(",");   
		   } 
		   
		   
		   if(bits.get(6)){ 
			   sb.append(Constant.LocStatus1).append(",");   
		   }else{
			   sb.append(Constant.LocStatus0).append(",");  
		   }
		   
		   
		   if(bits.get(7)){ 
			   sb.append(Constant.YouDianStatus1).append(",");   
		   }else{
			   sb.append(Constant.YouDianStatus0).append(",");  
		   }
		 return sb.toString();
	}
	
	
	public static int byte2AlarmType(byte b){
	    BitSet bits = new BitSet(8);
	    for (int i = 0; i < 8; i++)
	    {
	        bits.set(7-i, (b & 1) == 1); 
	        System.out.println((b & 1) == 1);
	        b >>= 1;
	       
	    } 
	   int res=0;
	   for(int i=3;i<5;i++){
		   if(bits.get(i)){
			   res=res*2+1;
		   }else{
			   res=res*2;
		   }
	   } 
	   
	 return res;
   }
	
	
	public static short byte2Short(byte[] b,int from){
		short n = (short) (((b[from] < 0 ? b[from] + 256 : b[from]) << 8) + (b[from+1] < 0 ? b[from+1] + 256
				: b[from+1]));
		return n;
	}
	
	public static int[] byte2Ints(byte[] bb,int from){
		 BitSet bits = new BitSet(16);
		   byte b=bb[from];
		   
		    for (int i = 0; i < 8; i++)
		    {
		        bits.set(7-i, (b & 1) == 1); 
		       
		        b >>= 1;
		    }
		    b=bb[from+1];
		    for (int i = 0; i < 8; i++)
		    {
		    	 
		        bits.set(15-i, (b & 1) == 1); 
		        b >>= 1;
		    }
		    
		    int [] res=new int[5];
		    res[0]=bits.get(2)?1:0;
		    res[1]=bits.get(3)?1:0;
		    res[2]=bits.get(4)?1:0;
		    res[3]=bits.get(5)?1:0;
		    for(int i=6;i<16;i++){
		    	if(bits.get(i)){
		    		res[4]=res[4]*2+1;
		    	}else{
		    		res[4]=res[4]*2;
		    	}
		    }
		    
		  //  System.out.println(res[0]+"  "+res[1]+"  "+res[2]+"  "+res[3]+"  "+res[4]);
		    return res;
	}
	
	 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
       String imei="123456789012345";
       byte[] b=ByteUtils.stringToByte(imei);
       for(int i=0;i<b.length;i++){
    	   System.out.print(b[i]+" ");
       }
       String s=ByteUtils.byteToString(b, 0, 8);
       System.out.println(s);
       
       int[] ii=ByteUtils.byte2Bit((byte)156);
       System.out.println(ii[0]+"  "+ii[1]);
       
       byte[] bb=new byte[2];
       bb[0]=0x05;
       bb[1]=0x4c;
       ByteUtils.byte2Ints(bb, 0);
       String res=ByteUtils.byte2Mes((byte)0x4B);
       System.out.println(res);
       
       
       BitSet bits=ByteUtils.byte2BitSet((byte)7);
       for(int i=0;i<8;i++){
    	   System.out.print((bits.get(i)?1:0)+"  ");
       }
       
       long time = stringToTime("140206141730");
       System.out.println(time);
       System.out.println(ConstantC.dataFormat.format(new Date(time)));
       
       System.out.println(stringToStatus("1"));
	}

}
