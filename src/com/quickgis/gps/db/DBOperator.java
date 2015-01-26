package com.quickgis.gps.db;


 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
 
import java.util.ArrayList;
import java.util.List;
 
import org.apache.log4j.Logger;

 

import com.quickgis.gps.bean.AlarmParam;
import com.quickgis.gps.bean.Asset;
 
import com.quickgis.gps.bean.OverSpeedSet;
 
import com.quickgis.gps.util.Constant;

 

public class DBOperator {
 	static Logger logger=Logger.getLogger(DBOperator.class);
	
	private static  ConnMan conM=ConnMan.getInstance(); 
	 
	private static long maxAssetId=0;
	private static long maxOverSpeedId=0;
	
	private static long maxAlarmId=0;
	private static long maxAlarmSetId=0;
	public void updateAssetInfo(){
		logger.info("更新资产信息:"+maxAssetId);
		String sel="SELECT id,name,callnum,loctype ,updatetime,imei FROM t_asset where updatetime>?";
		Connection con =conM.getConnection();
		PreparedStatement  st=null; 
		ResultSet rs=null; 
		try{ 
		  st=con.prepareStatement(sel);
		  st.setLong(1,maxAssetId);
		  rs=st.executeQuery();
		  while(rs.next()){
			  long id=rs.getLong(1);
			  String name=rs.getString(2);
			  String call=rs.getString(3);
			  int type=rs.getInt(4);
			  long t=rs.getLong(5);
			  String imei=rs.getString(6);
			  if(t>maxAssetId){
				  maxAssetId=t;
			  }
			  Constant.assetMap.put(id, new Asset(id,name,call,type,imei));
			  Constant.imeiAssetMap.put(imei,id);
			  logger.debug("更新Asset："+id+","+name+","+call+","+type);
		  }
		  
		}catch(Exception ex){  
			ex.printStackTrace();
		}finally{
			conM.ClosePstam(rs, st, con);
		}  
		
	}
	
	public void updateOverSpeedSet(){
		logger.info("更新超速信息:"+maxOverSpeedId);
		String sel="SELECT assetid,overspeed1,overspeed2,updatetime FROM t_asset_overspeed_set where  updatetime >?";
		Connection con =conM.getConnection();
		PreparedStatement  st=null; 
		ResultSet rs=null; 
		try{ 
		  st=con.prepareStatement(sel);
		  st.setLong(1,maxOverSpeedId);
		  rs=st.executeQuery();
		  while(rs.next()){
			  long id=rs.getLong(1);
			  int speed1=rs.getInt(2);
			  int speed2=rs.getInt(3);
			  long t=rs.getLong(4);
			  if(t>maxOverSpeedId){
				  maxOverSpeedId=t;
			  }
			  Constant.overSpeedMap.put(id, new OverSpeedSet(id,speed1,speed2));
			  logger.debug("更新OverSpeed："+id+","+speed1+","+speed2 );
		  } 
		}catch(Exception ex){  
			ex.printStackTrace();
		}finally{
			conM.ClosePstam(rs, st, con);
		}  
	}
	
	public void updateAlarmSet(){
		logger.info("更新警情信息:"+maxAlarmSetId+"  "+maxAlarmId);
		String sel="select assetid,alarmtype,al.updatetime,name,points,geotype,l.updatetime,alarmid from  t_asset_alarm_label al, t_alarm_label l where al.alarmid=l.id and al.updatetime>? and l.updatetime>? ";
		Connection con =conM.getConnection();
		PreparedStatement  st=null; 
		ResultSet rs=null; 
		try{ 
		  st=con.prepareStatement(sel);
		  st.setLong(1,maxAlarmSetId);
		  st.setLong(2,maxAlarmId);
		  rs=st.executeQuery();
		  while(rs.next()){
			  long id=rs.getLong(1);
			  int alarmtype=rs.getInt(2); 
			  long alarmsettime=rs.getLong(3);
			  String name=rs.getString(4);
			  String points=rs.getString(5);
			  int geotype=rs.getInt(6);
			  long t=rs.getLong(7);
			  long alarmid=rs.getLong(8);
			  if(t>maxAlarmId){
				  maxAlarmId=t;
			  }
			  
			  if(alarmsettime>maxAlarmSetId){
				  maxAlarmSetId=alarmsettime;
			  }
			  AlarmParam alarm=new AlarmParam(id,alarmid,alarmtype,name,points,geotype);
			  List<AlarmParam> listAlarm=Constant.alarmParamMap.get(id);
			  if(listAlarm==null){
				  listAlarm=new ArrayList<AlarmParam>();
				  Constant.alarmParamMap.put(id, listAlarm);
			  }
			  listAlarm.add(alarm);
			   
			  logger.debug("更新Alarm："+id+","+alarmtype+","+name+","+points+","+geotype );
		  } 
		}catch(Exception ex){  
			ex.printStackTrace();
		}finally{
			conM.ClosePstam(rs, st, con);
		}  
	} 
	
	public void updateAssetStatus(){
		logger.info("更新在线信息:"+Constant.offlineUnit.size());
		while(Constant.offlineUnit.size()>0){
			Asset asset=Constant.offlineUnit.remove(0);
			String status="update t_asset set islogin=? where id=? ";
			Connection con =conM.getConnection();
			PreparedStatement  st=null; 
			ResultSet rs=null; 
			try{ 
			  st=con.prepareStatement(status);
			  st.setInt(1,asset.getStatus());
			  st.setLong(2,asset.getId());
			  st.executeUpdate(); 
			  logger.debug("更新Asset："+asset.getId()+","+asset.getStatus() ); 
			}catch(Exception ex){  
				ex.printStackTrace();
			}finally{
				conM.ClosePstam(rs, st, con);
			}  
		}
	}
	
	public static void main(String[] args){
		DBOperator db=new DBOperator();
	//	db.getCmdToSend();
	//	String result=db.getLabels("1358839582663,1358839582666,1358839582667"); 
		try { 
			 
		 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	 
	
}
