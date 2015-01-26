package com.quickgis.gps.bean;

public class Asset {
   private long id;
   private String name;
   private String callnum;
   private int loctype;
   
   private String imei;
   
   private int status;

    public Asset(){
	   
   }
   
   public Asset(long id,String name,String call,int type,String imei){
	   this.id=id;
	   this.name=name;
	   this.callnum=call;
	   this.loctype=type;
	   this.imei=imei;
   }
   
   
   
   public String getImei() {
	return imei;
}

public void setImei(String imei) {
	this.imei = imei;
}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	 
	public String getCallnum() {
		return callnum;
	}

	public void setCallnum(String callnum) {
		this.callnum = callnum;
	}

	public int getLoctype() {
		return loctype;
	}
	public void setLoctype(int loctype) {
		this.loctype = loctype;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
   
   
}
