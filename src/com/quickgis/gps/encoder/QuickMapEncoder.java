package com.quickgis.gps.encoder;

import java.util.Map;

import com.quickgis.gps.bean.SendCmd;
import com.quickgis.gps.util.Constant;
import com.quickgis.gps.util.ConstantC;

 
public class QuickMapEncoder implements Encoder{
    short seq=0;
	@Override
	public byte[] resLogin() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] resStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public  byte[] sendCmd(int i) {
		// TODO Auto-generated method stub
		 
		return null;
	}

	@Override
	public byte[] sendDaunYouDian(int i) {
		// TODO Auto-generated method stub
		 
		return null;
		 
	}

	@Override
	public byte[] sendHuiFuYouDian(int i) {
		// TODO Auto-generated method stub
		 
		return null;
	}

	@Override
	public byte[] buildCmd(SendCmd cmd) {
		// TODO Auto-generated method stub
		switch (cmd.getCmdtype()){
		case Constant.LOC:
		 StringBuffer sb=new StringBuffer();
		 sb.append("*HQ,").append("").append(",D1,").append(ConstantC.getTimeStr()).append(",10,0#");
		 return sb.toString().getBytes();
		 
		case Constant.STOPOIL:
			  sb=new StringBuffer();
			 sb.append("*HQ,000,S20,").append(ConstantC.getTimeStr()).append(",0,10,20#");
			 return sb.toString().getBytes();
			 
		case Constant.TRACE:
			Map map=cmd.getParam();
			int batch=Integer.parseInt(map.get("batch").toString());
			int interval=Integer.parseInt(map.get("interval").toString());
			sb=new StringBuffer();
			 sb.append("*HQ,000,D1,").append(ConstantC.getTimeStr()).append(",").append(interval).append(",").append(batch).append("#");
			 return sb.toString().getBytes();
		}
		return null;
	}

}
