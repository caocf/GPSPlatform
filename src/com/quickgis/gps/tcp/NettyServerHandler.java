package com.quickgis.gps.tcp;

 
 
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

 
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer; 
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler; 

 
import com.quickgis.gps.bean.Asset;
import com.quickgis.gps.bean.ChannelBean;
import com.quickgis.gps.bean.OnLine;
import com.quickgis.gps.decoder.QuickMapAssDecoder;
import com.quickgis.gps.decoder.QuickMapBinDecoder;
import com.quickgis.gps.util.ByteUtils;
import com.quickgis.gps.util.Constant;
import com.quickgis.gps.util.ConstantC;
 
 
public class NettyServerHandler extends SimpleChannelUpstreamHandler { 
   	static Logger logger=Logger.getLogger(NettyServerHandler.class);
	   
     private static Map<String,Channel> callLetter_Map=new ConcurrentHashMap<String,Channel>();
     private static Map<Channel,ChannelBean> channel_Map=new ConcurrentHashMap<Channel,ChannelBean>();
      
    
     
     ExecutorService executorService = Executors.newCachedThreadPool();
     public NettyServerHandler(){ 
		   
	  } 
     
    @Override
    public void messageReceived(
            ChannelHandlerContext ctx, MessageEvent e) {
        // Send back the received message to the remote peer. 
          Channel channel=e.getChannel();
         byte [] b=new byte[((ChannelBuffer) e.getMessage()).readableBytes()]; 
         Map result=null;
         ChannelBean  channelBean=channel_Map.get(channel);
         String imei=null;
         if(channelBean!=null){
        	 imei=channelBean.getKey();
         }
         ((ChannelBuffer) e.getMessage()).readBytes(b);
         logger.debug("receive data:"+ByteUtils.byteToString2(b));
          if(b[0]==Constant.THH||b[0]==Constant.THH2){  //二进制数据
        	  logger.debug("接收二进制数据："+b.length);
        	  QuickMapBinDecoder decoder=new QuickMapBinDecoder();
        	  result= decoder.parse(b, 0,imei);
          }else if(b[0]==Constant.THS){  //assii数据
        	  if(b[b.length-1]==Constant.THE){  //完整数据
        		  QuickMapAssDecoder decoder=new QuickMapAssDecoder();
        		  result=  decoder.parse(b, 0,imei);
        		  logger.debug("接收完整的assii数据："+new String(b));  
        	  }else{  //分段数据
        		  logger.debug("接收部分的assii数据："+b.length);  
        		  channelBean.setLastByte(b);
        		  
        	  }
          }else{  //断头数据
        	  if(channelBean!=null){
            	  byte[] oldbyte=channelBean.getLastByte();
            	  if(oldbyte!=null){
            		  byte [] newbyte=new byte[oldbyte.length+b.length];
            		  System.arraycopy(oldbyte, 0, newbyte, 0, oldbyte.length);
            		  System.arraycopy(b, 0, newbyte, oldbyte.length, b.length);
            		  
            		  if(newbyte[newbyte.length-1]!=Constant.THE){
            			  logger.debug("接收部分的assii数据："+b.length);  
            			  channelBean.setLastByte(newbyte);
            		  }else{
            			  channelBean.setLastByte(null);
            			  QuickMapAssDecoder decoder=new QuickMapAssDecoder();
            			  result=  decoder.parse(newbyte, 0,imei);
            			  logger.debug("接收组合的assii数据："+new String(b));   
            		  }
            	  }else{
            		  logger.info("不能识别的数据："+ByteUtils.byteToString2(b));  
            	  }
        	  }else{
        		  logger.info("不能识别的数据："+ByteUtils.byteToString2(b));
        	  }
        	  
          }
          if(result!=null){
        	  String key=(String)result.get(Constant.logInKey);
        	  if(key!=null){
        		  Channel oldChannel=callLetter_Map.get(key);
        		  if(oldChannel!=null && oldChannel!=channel){
	        		  oldChannel.close();
	        		  channel_Map.remove(oldChannel); 
        		  }
        		  if(channelBean==null){
        			  channelBean=new ChannelBean(key,System.currentTimeMillis());
        		  }else{
	        		  channelBean.setLastTime(System.currentTimeMillis());
	        		  channelBean.setKey(key);
        		  }
        		  callLetter_Map.put(key,channel);
        		  channel_Map.put(channel, channelBean);
        		  
        		  long time=ConstantC.getTodayTime();
        		  long assetid=Constant.imeiAssetMap.get(key);
        		  OnLine on=Constant.onLineMap.get(assetid+"_"+time);
     			 if(on==null){
     				 on=new OnLine(assetid,System.currentTimeMillis(),0,time);
     				 Constant.onLineMap.put(assetid+"_"+time,on); 
     			 } 
     			  Asset asset=Constant.assetMap.get(assetid);
	   	    	  asset.setStatus(1);
	   	    	  Constant.offlineUnit.add(asset);
        	  } 
          }
	    } 
        
	   
	    public static boolean sendToClient(String call_letter,byte[] data){ 
	    	  Channel channel=callLetter_Map.get(call_letter); 
	    	  if(channel==null){
	    		  return false;
	    	  }
	    		ChannelBuffer firstMessage = ChannelBuffers.buffer(data.length*2); 
	            firstMessage.writeBytes(data);
	    		channel.write(firstMessage); 
	    		logger.info("send data:"+call_letter);
	    		logger.info("send data content:"+new String (data));
	    		return true;
	    }
	    @Override
	    public void exceptionCaught(
	            ChannelHandlerContext ctx, ExceptionEvent e) {
	        // Close the connection when an exception is raised.
	    	
	    	logger.error("client error:"+e.getChannel()+"  ");
	    	ChannelBean imei=channel_Map.remove(e.getChannel());
	    	if(imei!=null){
	    	  String key=imei.getKey();
	    	  Long assetid=Constant.imeiAssetMap.get(key);
	    	  if(assetid!=null){
	    		  Asset a=Constant.assetMap.get(assetid);
	    		  long time=ConstantC.getTodayTime();
	    		  OnLine on=Constant.onLineMap.get(assetid+"_"+time);
	    		  
	    		  long lt=on.getLt();
	    		  if(lt>0){
	    			  on.setT((System.currentTimeMillis()-lt)/Constant.MILSECONEHOUR);
	    		  } 
	    		  on.setLt(0);
	    	  }
	    	  Asset asset=Constant.assetMap.get(assetid);
	    	  asset.setStatus(0);
	    	  Constant.offlineUnit.add(asset);
	    	  if(callLetter_Map.size()>0){
	    		  callLetter_Map.remove(imei);
	    	  }
	    	}
	        e.getChannel().close();
	    }
	    
	    public static Map<String, Channel> getCallLetter_Map() {
			return callLetter_Map;
		}

		public static void setCallLetter_Map(Map<String, Channel> callLetter_Map) {
			NettyServerHandler.callLetter_Map = callLetter_Map;
		}

		public static Map<Channel, ChannelBean> getChannel_Map() {
			return channel_Map;
		}

		public static void setChannel_Map(Map<Channel, ChannelBean> channel_Map) {
			NettyServerHandler.channel_Map = channel_Map;
		}

		 
	   
}
