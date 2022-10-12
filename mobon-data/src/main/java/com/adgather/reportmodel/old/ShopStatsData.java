package com.adgather.reportmodel.old;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.adgather.lang.old.ObjectToString;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.model.v15.ShopStatsInfoData;

public class ShopStatsData extends ObjectToString implements Serializable{
	private static final long serialVersionUID = 1053041137078361135L;
	private String sDate;
	private String userId;
	private String pCode;
	private int viewCnt=0;
	private int adViewCnt=0;
	private int adClickCnt=0;
	private int adConvCnt=0;
	private int adConvPrice=0;
	private int clickCnt=0;
	private String pc_mobile_gubun;
	
	private String cate1;
	
	private int partition=0;
	private Long offset=0L;
	private String key="";
	private String appName = "";

	private boolean bHandlingStatsMobon = true;
	private boolean bHandlingStatsPointMobon = true;
	
	public ShopStatsData(){
		SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
		java.util.Date date=new java.util.Date();
		sDate=yyyymmdd.format(date);
		viewCnt=0;
		adViewCnt=0;
		adClickCnt=0;
		adConvCnt=0;
		adConvPrice=0;
		clickCnt=0;
	}
	public static ShopStatsData fromHashMap(Map from) {
		ShopStatsData result = new ShopStatsData();
		result.adClickCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("adClickCnt"),"0"));
		result.adConvCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("adConvCnt"),"0"));
		result.adConvPrice	 = Integer.parseInt(StringUtils.trimToNull2(from.get("adConvPrice"),"0"));
		result.adViewCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("adViewCnt"),"0"));
		result.cate1	 = StringUtils.trimToNull2(from.get("cate1"),"");
		result.className	 = StringUtils.trimToNull2(from.get("className"),"");
		result.clickCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("clickCnt"),"0"));
		//result.empty	 = StringUtils.trimToNull2(from.get("empty"));
		result.endDate	 = StringUtils.trimToNull2(from.get("endDate"),"");
		result.hh	 = StringUtils.trimToNull2(from.get("hh"),DateUtils.getDate("HH"));
		//result.limit	 = StringUtils.trimToNull2(from.get("limit"));
		result.orderBy	 = StringUtils.trimToNull2(from.get("orderBy"),"");
		result.pCode	 = StringUtils.trimToNull2(from.get("pCode"),"");
		result.pc_mobile_gubun	 = StringUtils.trimToNull2(from.get("pc_mobile_gubun"),"");
		result.sDate	 = StringUtils.trimToNull2(from.get("sDate"),"");
		result.sendDate	 = StringUtils.trimToNull2(from.get("sendDate"),"");
		result.startDate	 = StringUtils.trimToNull2(from.get("startDate"),"");
		result.targetDate	 = StringUtils.trimToNull2(from.get("targetDate"),"");
		result.userId	 = StringUtils.trimToNull2(from.get("userId"),"");
		result.viewCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewCnt"),"0"));
		result.appName	= (StringUtils.trimToNull2(from.get("appName"),""));
		
		// 전환시 사용.
		result.setbHandlingStatsMobon(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsMobon"),"false")));
		result.setbHandlingStatsPointMobon(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsPointMobon"),"false")));
		
		return result;
	}
	public String getPc_mobile_gubun() {
		return pc_mobile_gubun;
	}
	public void setPc_mobile_gubun(String pc_mobile_gubun) {
		this.pc_mobile_gubun = pc_mobile_gubun;
	}
	public String getsDate() {
		return sDate;
	}
	public void setsDate(String sDate) {
		this.sDate = sDate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getpCode() {
		return pCode;
	}
	public void setpCode(String pCode) {
		this.pCode = pCode;
	}
	public int getViewCnt() {
		return viewCnt;
	}
	public void setViewCnt(int viewCnt) {
		this.viewCnt = viewCnt;
	}
	public int getAdViewCnt() {
		return adViewCnt;
	}
	public void setAdViewCnt(int adViewCnt) {
		this.adViewCnt = adViewCnt;
	}
	public int getAdClickCnt() {
		return adClickCnt;
	}
	public void setAdClickCnt(int adClickCnt) {
		this.adClickCnt = adClickCnt;
	}
	public int getAdConvCnt() {
		return adConvCnt;
	}
	public void setAdConvCnt(int adConvCnt) {
		this.adConvCnt = adConvCnt;
	}
	public int getAdConvPrice() {
		return adConvPrice;
	}
	public void setAdConvPrice(int adConvPrice) {
		this.adConvPrice = adConvPrice;
	}
	public String getCate1() {
		return cate1;
	}
	public void setCate1(String cate1) {
		this.cate1 = cate1;
	}
	public int getClickCnt() {
		return clickCnt;
	}
	public void setClickCnt(int clickCnt) {
		this.clickCnt = clickCnt;
	}
	
	public ShopStatsInfoData toShopStatsInfoData(){
		ShopStatsInfoData result = new ShopStatsInfoData();
		result.setHh(this.getHh());
		result.setSendDate(this.getSendDate());
		result.setYyyymmdd(this.getsDate());
		result.setAdvertiserId(this.getUserId());
		result.setCate(this.getCate1());
		if(this.getPc_mobile_gubun()!=null)result.setPlatform(this.getPc_mobile_gubun().substring(0,1).toUpperCase());
		result.setpCode(this.getpCode());
		result.setViewCnt(this.getViewCnt());
		result.setAdViewCnt(this.getAdViewCnt());
		result.setAdClickCnt(this.getAdClickCnt());
		result.setAdConvCnt(this.getAdConvCnt());
		result.setAdConvPrice(this.getAdConvPrice());
		result.setClickCnt(this.getClickCnt());

		result.setPartition(this.getPartition());
		result.setOffset(this.getOffset());
		result.setKey(this.getKey());
		
		result.setbHandlingStatsMobon(this.isbHandlingStatsMobon());
		result.setbHandlingStatsPointMobon(this.isbHandlingStatsPointMobon());
		result.setAppName(this.getAppName());

//		ShopStatsInfoData result = new ShopStatsInfoData();
//		result.setYyyymmdd(this.getRDATE());
//		result.setAdvertiserId(this.getUserId());
//		result.setpCode(this.getPCODE());
//		result.setCate(this.getSC_TXT());
//		result.setPlatform( this.isWebInsert()?"W":"M" );
//		result.setViewCnt(0);
//		result.setAdViewCnt(0);
//		result.setAdClickCnt(1);
//		result.setAdConvCnt(0);
//		result.setAdConvPrice(0);
//		result.setClickCnt(1);
//
		
		return result;
	}
	public int getPartition() {
		return partition;
	}
	public void setPartition(int partition) {
		this.partition = partition;
	}
	public Long getOffset() {
		return offset;
	}
	public void setOffset(Long offset) {
		this.offset = offset;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public boolean isbHandlingStatsMobon() {
		return bHandlingStatsMobon;
	}
	public void setbHandlingStatsMobon(boolean bHandlingStatsMobon) {
		this.bHandlingStatsMobon = bHandlingStatsMobon;
	}
	public boolean isbHandlingStatsPointMobon() {
		return bHandlingStatsPointMobon;
	}
	public void setbHandlingStatsPointMobon(boolean bHandlingStatsPointMobon) {
		this.bHandlingStatsPointMobon = bHandlingStatsPointMobon;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
}