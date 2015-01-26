package com.quickgis.gps.bean;

 
import java.util.Map;

import com.quickgis.gps.encoder.QuickMapEncoder;
import com.quickgis.gps.util.Constant;

 
public class SendCmd {
	
   private long  id;
   private long  assetid;
   private String name;
   private String call;
   private int loctype;
   private int  cmdtype;
   private Map param;
    
   
   private long lastSend;
  
	   
	   
	   
		public SendCmd(long id, long assetid, String name, String call, int loctype,
		int cmdtype, Map param) {
			super();
			this.id = id;
			this.assetid = assetid;
			this.name = name;
			this.call = call;
			this.loctype = loctype;
			this.cmdtype = cmdtype;
			this.param = param;
		}




		public long getId() {
		return id;
	}
	
	
	
	
	public void setId(long id) {
		this.id = id;
	}
	
	
	
	
	public long getAssetid() {
		return assetid;
	}
	
	
	
	
	public void setAssetid(long assetid) {
		this.assetid = assetid;
	}
	
	
	
	
	public String getName() {
		return name;
	}
	
	
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
	public String getCall() {
		return call;
	}
	
	
	
	
	public void setCall(String call) {
		this.call = call;
	}
	
	
	
	
	public int getLoctype() {
		return loctype;
	}
	
	
	
	
	public void setLoctype(int loctype) {
		this.loctype = loctype;
	}
	
	
	
	
	public int getCmdtype() {
		return cmdtype;
	}
	
	
	
	
	public void setCmdtype(int cmdtype) {
		this.cmdtype = cmdtype;
	}
	
	
	
	
	public Map getParam() {
		return param;
	}
	
	
	
	
	public void setParam(Map param) {
		this.param = param;
	}
	
	
	
	
	public long getLastSend() {
		return lastSend;
	}
	
	
	
	
	public void setLastSend(long lastSend) {
		this.lastSend = lastSend;
	}
	
	


	public byte[] buildCmd(){
		 StringBuffer sb=new StringBuffer();
		 switch(loctype){
			 case Constant.QUICKMAPLOC:
				 QuickMapEncoder encoder=new QuickMapEncoder(); 
				 byte[] cmd=encoder.buildCmd(this);
				 sb.append(new String(cmd));
				 break;
		 }
		 
		try {
			String s = new String(sb.toString().getBytes(),"US-ASCII");
			byte[] b = s.getBytes("US-ASCII");
			return b;
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString().getBytes();
	}
	
	 
   
}

