package com.quickgis.gps.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.quickgis.gps.bean.AlarmParam;
import com.quickgis.gps.bean.Asset;
import com.quickgis.gps.bean.GPSLocation;
import com.quickgis.gps.bean.OnLine;
import com.quickgis.gps.bean.OverSpeedSet;
import com.quickgis.gps.bean.Response;
import com.quickgis.gps.bean.SendCmd;

public interface Constant {
	
	
	public final static byte THH='$';
	public final static byte THH2='X';
	public final static byte THS='*';
	public final static byte THE='#';
	
	public final static String V1="V1";
	
	public final static int QUICKMAPLOC=3;
	
	public final double JIETOKM=1.852 ; //节转千米/小时
	
	 /*
	  * geotype 
	  */
	 public static final int MAPPOINT=1;
	 public  static final int MAPPATH=2;
	 public  static final int MAPPOLYGON=3;
	 public  static final int MAPCIRCLE=4;
	 public  static final int MAPRECT=5;
	 
	public static final int MAXSENDTIME=300000;   //任务发送5分组，如果没有收到终端确认，重新发送
	
	public static final int MAXUNACTIVE=600000;  //
	
    public static final  String logInKey="logInKey";
    public static final  String resBytes="resBytes";
    public static final  String response="Response";
    public static final  String location="Location";
    
    public static final  String locationCMD="DWXX,000000#"; 
    public static final  String duanyouCMD="DYD,000000#";
    public static final  String huifuCMD="HFYD,000000#";
    
    public static final  String sendLocCmd="定位";
    public static final  String sendDuanYouCMd="断油电";
    public static final  String sendHuiFuYouCMd="恢复油电";
    
    public static final  String sheFang="设防";
    public static final  String cheFang="撤防";
    
    public static final  String accDi="ACC低";
    public static final  String accGao="ACC高";
    
    public static final  String dianYuan0="未接电源充电";
    public static final  String dianYuan1="已接电源充电";
    
    public static final  String STATUS1="正常";
    public static final  String STATUS2="震动报警";
    public static final  String STATUS3="断电报警";
    public static final  String STATUS4="低电报警";
    public static final  String STATUS5="SOS求救";
    
    public static final  String LocStatus1="GPS定位";
    public static final  String LocStatus0="GPS不定位";
    
    
    public static final  String YouDianStatus1="油电接通";
    public static final  String YouDianStatus0="油电断开";
    
    public static final  String DIANYA5="强行关机报警";
    public static final  String DIANYA0="低电关机";
    public static final  String DIANYA1="电量不足";
    public static final  String DIANYA2="低电报警";
    public static final  String DIANYA3="低电，可以正常使用";
    public static final  String DIANYA4="正常";
    
    public static final  String GSM0="无信号";
    public static final  String GSM1="信号极弱";
    public static final  String GSM2="信号较弱";
    public static final  String GSM3="信号良好";
    public static final  String GSM4="信号强";
    
    public static final int SLEEPTIME=1000;
    //警情参数更新的秒数间隔
    public static final int ALARMPARAM=30;
    
    public  static List<SendCmd> sendCmds=new ArrayList<SendCmd>();
    
    public  static List<GPSLocation> gpsList=new ArrayList<GPSLocation>();
    public  static List<GPSLocation> gpsList2=new ArrayList<GPSLocation>(); 
    public  static List<Response> responseList=new ArrayList<Response>(); 
    
    public static final int OverSpeedAlarm=300000;  //超速持续5分钟为报警
    public static final int GeoFenceAlarm=60000;  //翻越围栏持续1分钟为报警
    public static final int MAXALARMLEN=600000;  //每个警情间隔10分钟
    
    
    public static final int MAXONLINETIME=6000000;  //在线时长
    public double MILSECONEHOUR=3600000.0;  //微秒到小时
    //记录警情，和警情时间，保证一个警情不重复报
    public  static Map<String,Long> alarmMap=new ConcurrentHashMap<String,Long>();
    
    public Map<Long,Asset> assetMap=new ConcurrentHashMap<Long,Asset>();
    
    public Map<String ,Long> imeiAssetMap=new ConcurrentHashMap<String,Long>();
    public Map<Long,OverSpeedSet> overSpeedMap=new ConcurrentHashMap<Long,OverSpeedSet>();
    public Map<Long,List<AlarmParam>> alarmParamMap=new ConcurrentHashMap<Long,List<AlarmParam>>();
    
    /*
     * mongodb collections
     */
    public String commandtmp="cmd_tmp";
	public String historyName="his";
	public String lasHistoryName="lasthis";
	
	public String cmd="cmd";
	public String cmdback="cmdback";
	public String message="mess";
	public String alarmCol="alarm";
	
	public   String online="online";
	
   
    public  static List<Asset> offlineUnit=new ArrayList<Asset>();
    
    public static final double MINPOINTDIS=0.0005;  //50米内
    public static final double MINDIS=0.0009;  //100米内
    public static Map<Long,GPSLocation> overSpeed=new HashMap<Long,GPSLocation>();
    public static Map<Long,Long> removeOverSpeedAlarm=new HashMap<Long,Long>();
	
    public static Map<String,GPSLocation> alarm=new HashMap<String,GPSLocation>(); 
    public static Map<String,GPSLocation> outAlarm=new HashMap<String,GPSLocation>();
    public static Map<String,Long> removeAlarm=new HashMap<String,Long>();
    public static Map<String,Long> removeOutAlarm=new HashMap<String,Long>();
	
    
    public static Map<String,OnLine> onLineMap=new HashMap<String,OnLine>(); 
    
	 
	 public int KEYPOINTALARM=1;
	 public int OVERSPEEDALARM=2;
	 public int PATHALARM=3;
	 public int INALARM=4;
	 public int OUTALARM=5;
	 public int INOUTALARM=6;
	 
	 
	  /*
	     * 发送指令ID
	     */
		 public int LOC=1;
		 public int TRACE=2;
		 public int TRACECANCEL=3;
		 public int OVERSPEED=4;
		 public int OVERSPEEDCANCEL=5;
		 
		 public int KEYPOINT=6;
		 public int KEYPOINTCANCEL=7;
		 public int PATHSET=8;
		 public int PATHSETCANCEL=9;
		 
		 public int INFENCE=10;
		 public int OUTFENCE=11;
		 public int INOUTFENCE=12;
		 public int FENCECANCAL=13;
		 
		 public int STOPOIL=14;
		 public int CONPOIL=15;
		 
		 public int MESS=16; //发送短信
}
