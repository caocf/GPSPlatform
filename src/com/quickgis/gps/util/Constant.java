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
	
	public final double JIETOKM=1.852 ; //��תǧ��/Сʱ
	
	 /*
	  * geotype 
	  */
	 public static final int MAPPOINT=1;
	 public  static final int MAPPATH=2;
	 public  static final int MAPPOLYGON=3;
	 public  static final int MAPCIRCLE=4;
	 public  static final int MAPRECT=5;
	 
	public static final int MAXSENDTIME=300000;   //������5���飬���û���յ��ն�ȷ�ϣ����·���
	
	public static final int MAXUNACTIVE=600000;  //
	
    public static final  String logInKey="logInKey";
    public static final  String resBytes="resBytes";
    public static final  String response="Response";
    public static final  String location="Location";
    
    public static final  String locationCMD="DWXX,000000#"; 
    public static final  String duanyouCMD="DYD,000000#";
    public static final  String huifuCMD="HFYD,000000#";
    
    public static final  String sendLocCmd="��λ";
    public static final  String sendDuanYouCMd="���͵�";
    public static final  String sendHuiFuYouCMd="�ָ��͵�";
    
    public static final  String sheFang="���";
    public static final  String cheFang="����";
    
    public static final  String accDi="ACC��";
    public static final  String accGao="ACC��";
    
    public static final  String dianYuan0="δ�ӵ�Դ���";
    public static final  String dianYuan1="�ѽӵ�Դ���";
    
    public static final  String STATUS1="����";
    public static final  String STATUS2="�𶯱���";
    public static final  String STATUS3="�ϵ籨��";
    public static final  String STATUS4="�͵籨��";
    public static final  String STATUS5="SOS���";
    
    public static final  String LocStatus1="GPS��λ";
    public static final  String LocStatus0="GPS����λ";
    
    
    public static final  String YouDianStatus1="�͵��ͨ";
    public static final  String YouDianStatus0="�͵�Ͽ�";
    
    public static final  String DIANYA5="ǿ�йػ�����";
    public static final  String DIANYA0="�͵�ػ�";
    public static final  String DIANYA1="��������";
    public static final  String DIANYA2="�͵籨��";
    public static final  String DIANYA3="�͵磬��������ʹ��";
    public static final  String DIANYA4="����";
    
    public static final  String GSM0="���ź�";
    public static final  String GSM1="�źż���";
    public static final  String GSM2="�źŽ���";
    public static final  String GSM3="�ź�����";
    public static final  String GSM4="�ź�ǿ";
    
    public static final int SLEEPTIME=1000;
    //����������µ��������
    public static final int ALARMPARAM=30;
    
    public  static List<SendCmd> sendCmds=new ArrayList<SendCmd>();
    
    public  static List<GPSLocation> gpsList=new ArrayList<GPSLocation>();
    public  static List<GPSLocation> gpsList2=new ArrayList<GPSLocation>(); 
    public  static List<Response> responseList=new ArrayList<Response>(); 
    
    public static final int OverSpeedAlarm=300000;  //���ٳ���5����Ϊ����
    public static final int GeoFenceAlarm=60000;  //��ԽΧ������1����Ϊ����
    public static final int MAXALARMLEN=600000;  //ÿ��������10����
    
    
    public static final int MAXONLINETIME=6000000;  //����ʱ��
    public double MILSECONEHOUR=3600000.0;  //΢�뵽Сʱ
    //��¼���飬�;���ʱ�䣬��֤һ�����鲻�ظ���
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
    
    public static final double MINPOINTDIS=0.0005;  //50����
    public static final double MINDIS=0.0009;  //100����
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
	     * ����ָ��ID
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
		 
		 public int MESS=16; //���Ͷ���
}
