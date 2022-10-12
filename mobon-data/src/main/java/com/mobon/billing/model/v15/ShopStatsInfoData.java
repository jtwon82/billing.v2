package com.mobon.billing.model.v15;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.adgather.util.old.StringUtils;
import com.mobon.billing.model.ClickViewData;

public class ShopStatsInfoData extends ClickViewData {

	public String cate=""; // 카테고리1 cate
	public int adViewCnt=0; // 광고주 노출수 adViewCnt
	public int adClickCnt=0; // 광고주 클릭수
	public int adConvCnt=0; // 광고주 전환수
	public int adConvPrice=0; // 광고주 전환금액
	private String appName="";

	private boolean bHandlingStatsMobon = true;
	private boolean bHandlingStatsPointMobon = true;
	
	public String retryFlag="";
	public ShopStatsInfoData(){
	}
	public ShopStatsInfoData(String yyyymmdd, String platform, String advertiserId, String cate, String pcode, int adViewCnt, int adClickCnt, int adConvCnt,
			int adConvPrice) {
		this.setYyyymmdd(yyyymmdd);
		this.setPlatform(platform);
		this.setAdvertiserId(advertiserId);
		this.setpCode(pcode);
		
		this.cate = cate;
		this.adViewCnt = adViewCnt;
		this.adClickCnt = adClickCnt;
		this.adConvCnt = adConvCnt;
		this.adConvPrice = adConvPrice;
	}
	public ShopStatsInfoData(String advertiserId, String cate, int adViewCnt, int adClickCnt, int adConvCnt,
			int adConvPrice) {
		
		this.cate = cate;
		this.adViewCnt = adViewCnt;
		this.adClickCnt = adClickCnt;
		this.adConvCnt = adConvCnt;
		this.adConvPrice = adConvPrice;
	}

	public ArrayList toList() {
		Map map = new HashMap();
		map.put("yyyymmdd", yyyymmdd);
		map.put("advertiserId", advertiserId);
		map.put("pCode", pCode);
		map.put("cate", cate);
		map.put("platform", platform);
		map.put("viewCnt", viewCnt);
		map.put("clickCnt", clickCnt);
		map.put("adViewCnt", adViewCnt);
		map.put("adClickCnt", adClickCnt);
		map.put("adConvCnt", adConvCnt);
		map.put("adConvPrice", adConvPrice);
		ArrayList list = new ArrayList();
		return list;
	}

	public static ShopStatsInfoData fromHashMap(Map from) {
		ShopStatsInfoData result = new ShopStatsInfoData();
		result.cate	 = StringUtils.trimToNull2(from.get("cate"));
		result.adViewCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("adViewCnt"),"0"));
		result.adClickCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("adClickCnt"),"0"));
		result.adConvCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("adConvCnt"),"0"));
		result.adConvPrice	 = Integer.parseInt(StringUtils.trimToNull2(from.get("adConvPrice"),"0"));
		result.no	 = Long.parseLong(StringUtils.trimToNull2(from.get("no")));
		result.mcgb	 = StringUtils.trimToNull2(from.get("mcgb"));
		result.sendDate	 = StringUtils.trimToNull2(from.get("sendDate"));
		result.clickCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("clickCnt"),"0"));
		result.serverName	 = StringUtils.trimToNull2(from.get("serverName"));
		result.className	 = StringUtils.trimToNull2(from.get("className"));
		result.type	 = StringUtils.trimToNull2(from.get("type"));
		result.platform	 = StringUtils.trimToNull2(from.get("platform"));
		result.point	 = Float.parseFloat(StringUtils.trimToNull2(from.get("point"),"0"));
		result.viewCnt3	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewCnt3"),"0"));
		result.viewCnt2	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewCnt2"),"0"));
		result.partition	 = Integer.parseInt(StringUtils.trimToNull2(from.get("partition"),"0"));
		result.viewCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewCnt"),"0"));
		result.scriptNo	 = Integer.parseInt(StringUtils.trimToNull2(from.get("scriptNo"),"0"));
		result.scriptUserId	 = StringUtils.trimToNull2(from.get("scriptUserId"));
		result.mpoint	 = Float.parseFloat(StringUtils.trimToNull2(from.get("mpoint")));
		result.key	 = StringUtils.trimToNull2(from.get("key"));
		result.product	 = StringUtils.trimToNull2(from.get("product"));
		result.siteCode	 = StringUtils.trimToNull2(from.get("siteCode"));
		result.mediasiteNo	 = Integer.parseInt(StringUtils.trimToNull2(from.get("mediasiteNo"),"0"));
		result.offset	 = Long.parseLong(StringUtils.trimToNull2(from.get("offset"),"0"));
		result.adGubun	 = StringUtils.trimToNull2(from.get("adGubun"));
		result.freqLog	 = Integer.parseInt(StringUtils.trimToNull2(from.get("freqLog"),"0"));
		result.kno	 = StringUtils.trimToNull2(from.get("kno"));
		result.advertiserId	 = StringUtils.trimToNull2(from.get("advertiserId"));
		result.pbGubun	 = StringUtils.trimToNull2(from.get("pbGubun"));
		result.keyCode	 = StringUtils.trimToNull2(from.get("keyCode"));
		result.keyIp	 = StringUtils.trimToNull2(from.get("keyIp"));
		result.mobonLinkCate	 = StringUtils.trimToNull2(from.get("mobonLinkCate"));
		result.yyyymmdd	 = StringUtils.trimToNull2(from.get("yyyymmdd"));
		result.pCode	 = StringUtils.trimToNull2(from.get("pCode"));
		result.clickChk	 = Boolean.parseBoolean(StringUtils.trimToNull2(from.get("clickChk")));
		result.interlock	 = StringUtils.trimToNull2(from.get("interlock"));
		result.appName		= StringUtils.trimToNull2(from.get("appName"));

		// 전환시 사용.
		result.setbHandlingStatsMobon(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsMobon"),"false")));
		result.setbHandlingStatsPointMobon(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsPointMobon"),"false")));
		
		return result;
	}
	
	public String getCate() {
		return cate;
	}

	public void setCate(String cate) {
		this.cate = cate;
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

	public String getRetryFlag() {
		return retryFlag;
	}
	
	public void setRetryFlag(String retryFlag) {
		this.retryFlag = retryFlag;
	}
	

	@Override
	public String generateKey(){
		keyCode = String.format("%s_%s_%s_%s_%s_%s", this.getPlatform(), this.getAdvertiserId(), this.getpCode(), this.getCate()
				, this.isbHandlingStatsMobon(), this.isbHandlingStatsPointMobon());
		
		this.setKeyCodeAdverProductHH(String.format("%s_%s_%s_%s_%s_%s", this.getPlatform(), this.getYyyymmdd(), this.getHh(), this.getAdvertiserId(), this.getpCode(), this.getCate()));
		
		grouping = String.format("[%s]", Math.abs(this.getAdvertiserId().hashCode())%20 );
//		grouping = String.format("[%s]", this.getPlatform() );
		return keyCode;
	}

	@Override
	public void sumGethering(Object obj) {
		if(obj == null)
			return;
		ShopStatsInfoData from = (ShopStatsInfoData) obj;
		
		this.setViewCnt( this.getViewCnt() + from.getViewCnt() );
		this.setViewCnt2( this.getViewCnt2() + from.getViewCnt2() );
		this.setViewCnt3( this.getViewCnt3() + from.getViewCnt3() );
		this.setClickCnt( this.getClickCnt() + from.getClickCnt() );
		this.setPoint( this.getPoint() + from.getPoint() );
		this.setMpoint( this.getMpoint() + from.getMpoint() );
		
		this.setAdViewCnt( this.getAdViewCnt() + from.getAdViewCnt() );
		this.setAdClickCnt( this.getAdClickCnt() + from.getAdClickCnt() );
		this.setAdConvCnt( this.getAdConvCnt() + from.getAdConvCnt() );
		this.setAdConvPrice( this.getAdConvPrice() + from.getAdConvPrice() );
		
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
