package com.quickgis.gps.decoder;

import java.util.Map;

public interface Decoder {
	
	//解析协议
	public Map parse(byte[] b,int from,String imei);
    
	//解析登录协议
	public Map parseLogin(byte[] b,int from);
	
	//解析定位协议
	public Map parseLocation(byte[] b,int from );
	
	//解析握手协议
	public Map parseStatus(byte[] b,int from);
	
	//解析消息协议
	public Map parseMessage(byte[] b,int from);
	
	//解析警情协议
	public Map parseAlarm(byte[] b,int from);
	
	//解析下载数据
	public Map parseQuestion(byte[] b,int from);
	
	
	 
}
