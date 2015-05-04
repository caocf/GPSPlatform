package com.quickgis.gps.encoder;

import com.quickgis.gps.bean.SendCmd;

public interface Encoder {
	
   //返回登录协议	
   public byte[] resLogin();
   
   //返回登录协议
   public byte[] resStatus();
   
   //发送定位指令
   public byte[] sendCmd(int i);
   
   
   //发送断邮电
   public byte[] sendDaunYouDian(int i);
   
   
   //发送恢复邮电
   public byte[] sendHuiFuYouDian(int i);
   
   
   public byte[] buildCmd(SendCmd cmd);
}
