package com.quickgis.gps.decoder;

import java.util.Map;

public interface Decoder {
	
	//����Э��
	public Map parse(byte[] b,int from,String imei);
    
	//������¼Э��
	public Map parseLogin(byte[] b,int from);
	
	//������λЭ��
	public Map parseLocation(byte[] b,int from );
	
	//��������Э��
	public Map parseStatus(byte[] b,int from);
	
	//������ϢЭ��
	public Map parseMessage(byte[] b,int from);
	
	//��������Э��
	public Map parseAlarm(byte[] b,int from);
	
	//������������
	public Map parseQuestion(byte[] b,int from);
	
	
	 
}
