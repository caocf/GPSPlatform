package com.quickgis.gps.thread;

 

import org.apache.log4j.Logger;


import com.quickgis.gps.bean.Asset;
import com.quickgis.gps.bean.SendCmd;
import com.quickgis.gps.db.DBOperator;
import com.quickgis.gps.mongodb.MongoDBOpe;
import com.quickgis.gps.tcp.NettyServerHandler;
import com.quickgis.gps.util.Constant;

public class UpdateDataThread extends Thread {
    private static boolean isRun=true;
    static Logger logger=Logger.getLogger(UpdateDataThread.class);
	
	public void run(){
		DBOperator db=new DBOperator();
		MongoDBOpe  mongoDB=new MongoDBOpe();
		int i=0;
		logger.info("�����߳�����");
		db.updateAssetInfo();
		db.updateOverSpeedSet();
		db.updateAlarmSet();
		db.updateAssetStatus();
		mongoDB.getOnLineData(); 
		while(isRun){
			logger.debug("�������µ���Ϣ");
			//�����ݿ��ȡ
			try{
			mongoDB.getCmdToSend();
			mongoDB.writeHistory();  
			mongoDB.writeMess();
		
			//����ָ��
			 
			while(Constant.sendCmds.size()>0){
				SendCmd cmd=Constant.sendCmds.remove(0);
				byte[] b=cmd.buildCmd();
				if(b!=null){
				   Asset asset=	Constant.assetMap.get(cmd.getAssetid());
				   if(asset!=null){
					   boolean send=NettyServerHandler.sendToClient(asset.getImei(),b);
					   if(send){ 
						   mongoDB.writeCmdBack(cmd, true);
					   }else{
						   mongoDB.writeCmdBack(cmd, false);
					   }
				   }else{
					   logger.error("����ָ��ʶ��,�Ҳ���Ԫ����:"+cmd.getAssetid()+","+cmd.getCmdtype()+","+cmd.getId()+","+cmd.getParam());
				   }
				}
			} 
			
			if(i==Constant.ALARMPARAM){
				i=0;
				logger.debug("���¾��������Ϣ");
				//ÿ�����Ӹ���һ�¾�������
				db.updateAssetInfo();
				db.updateOverSpeedSet();
				db.updateAlarmSet();
				db.updateAssetStatus();
				mongoDB.updateOnLineData();
			}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			try {
				sleep(Constant.SLEEPTIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			i++;
		}
	}
	
	public void stopUpdata(){
		isRun=false;
	}
}
