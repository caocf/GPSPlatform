package com.quickgis.gps.encoder;

import com.quickgis.gps.bean.SendCmd;

public interface Encoder {
	
   //���ص�¼Э��	
   public byte[] resLogin();
   
   //���ص�¼Э��
   public byte[] resStatus();
   
   //���Ͷ�λָ��
   public byte[] sendCmd(int i);
   
   
   //���Ͷ��ʵ�
   public byte[] sendDaunYouDian(int i);
   
   
   //���ͻָ��ʵ�
   public byte[] sendHuiFuYouDian(int i);
   
   
   public byte[] buildCmd(SendCmd cmd);
}
