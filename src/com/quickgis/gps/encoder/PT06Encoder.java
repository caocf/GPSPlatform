package com.quickgis.gps.encoder;

import com.quickgis.gps.bean.SendCmd;
import com.quickgis.gps.util.ByteUtils;
import com.quickgis.gps.util.CRC16Util;
import com.quickgis.gps.util.Constant;
public class PT06Encoder implements Encoder{
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
		
		byte[] res=new byte[27];
		res[0]=0x78;
		res[1]=0x78;
		res[2]=23;
		res[3]=(byte)0x80;
		res[4]=16;
		ByteUtils.int2bytes(i, res, 5);
		System.arraycopy(Constant.locationCMD.getBytes(), 0, res, 9, 12);
		ByteUtils.int22bytes(seq++, res, 21);
		CRC16Util.getCrc(res, 2, 23);
		res[25]=(byte)0x0d;
		res[26]=0x0a;
		return res;
	}

	@Override
	public byte[] sendDaunYouDian(int i) {
		// TODO Auto-generated method stub
		byte[] res=new byte[26];
		res[0]=0x78;
		res[1]=0x78;
		res[2]=23;
		res[3]=(byte)0x80;
		res[4]=16;
		ByteUtils.int2bytes(i, res, 5);
		System.arraycopy(Constant.duanyouCMD.getBytes(), 0, res, 9, 11);
		ByteUtils.int22bytes(seq++, res, 20);
		CRC16Util.getCrc(res, 2, 21);
		res[24]=(byte)0x0d;
		res[25]=0x0a;
		return res;
		 
	}

	@Override
	public byte[] sendHuiFuYouDian(int i) {
		// TODO Auto-generated method stub
		byte[] res=new byte[27];
		res[0]=0x78;
		res[1]=0x78;
		res[2]=23;
		res[3]=(byte)0x80;
		res[4]=16;
		ByteUtils.int2bytes(i, res, 5);
		System.arraycopy(Constant.huifuCMD.getBytes(), 0, res, 9, 12);
		ByteUtils.int22bytes(seq++, res, 21);
		CRC16Util.getCrc(res, 2, 23);
		res[25]=(byte)0x0d;
		res[26]=0x0a;
		return res;
	}

	@Override
	public byte[] buildCmd(SendCmd cmd) {
		// TODO Auto-generated method stub
		return null;
	}

}
