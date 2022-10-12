package com.mobon.billing.model.v15;

import java.io.Serializable;
import java.util.Map;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.model.ClickViewData;

public class NearData extends ClickViewData implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ADSTRD_CODE; 		// 행정동코드|행정동의 고유값|RGN_ADSTRD_INFO.ADSTRD_CODE|
	private int NEW_IP_CNT=0;             // 신규IP건수|신규IP의 건수||
	private int RGN_IP_CNT=0;             // 지역IP건수|지역IP의 건수||

	private boolean targetYn = false; // 상품 타겟팅 여부
	
	public String getADSTRD_CODE() {
		return ADSTRD_CODE;
	}
	public void setADSTRD_CODE(String aDSTRD_CODE) {
		ADSTRD_CODE = aDSTRD_CODE;
	}
	public int getNEW_IP_CNT() {
		return NEW_IP_CNT;
	}
	public void setNEW_IP_CNT(int nEW_IP_CNT) {
		NEW_IP_CNT = nEW_IP_CNT;
	}
	public int getRGN_IP_CNT() {
		return RGN_IP_CNT;
	}
	public void setRGN_IP_CNT(int rGN_IP_CNT) {
		RGN_IP_CNT = rGN_IP_CNT;
	}
	
	@Override
	public String generateKey() {
		keyCode = String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s", this.getAdGubun(), this.getPlatform(), 
				this.getYyyymmdd(), this.getHh(), this.getProduct(), this.getSiteCode(), this.getScriptNo(), this.getType(), this.getADSTRD_CODE(), 
				this.getAdvertiserId(), this.getScriptUserId());
		grouping = String.format("[%s, %s]", this.getAdGubun(), this.getPlatform());
		return keyCode;
	}
	@Override
	public void sumGethering(Object _from) {
		NearData from = (NearData)_from;
		
		if ( G.VIEW.equals( from.getType() ) ) {
			if(from.getViewCnt()>0)		this.setViewCnt( this.getViewCnt() + from.getViewCnt() );
			if(from.getViewCnt2()>0)	this.setViewCnt2( this.getViewCnt2() + from.getViewCnt2() );
			if(from.getViewCnt3()>0)	this.setViewCnt3( this.getViewCnt3() + from.getViewCnt3() );
		}
		else if ( G.CLICK.equals( from.getType() ) ) {
			if( from.getClickCnt()>0 )	this.setClickCnt( this.getClickCnt() + from.getClickCnt() );
			else this.setClickCnt( this.getClickCnt() + 1 );
		}
		
		if(from.getNEW_IP_CNT()>0)	this.setNEW_IP_CNT( this.getNEW_IP_CNT() + from.getNEW_IP_CNT() );
		if(from.getRGN_IP_CNT()>0)	this.setRGN_IP_CNT( this.getRGN_IP_CNT() + from.getRGN_IP_CNT() );
		
		this.setPoint( this.getPoint() + from.getPoint() );
		this.setMpoint( this.getMpoint() + from.getMpoint() );
		
	}
	
	
	public static NearData fromHashMap(Map from) {
		NearData result = new NearData();
		result.yyyymmdd	 = StringUtils.trimToNull2(from.get("yyyymmdd"));
		result.platform	 = StringUtils.trimToNull2(from.get("platform"));
		result.product	 = StringUtils.trimToNull2(from.get("product"));
		result.adGubun	 = StringUtils.trimToNull2(from.get("adGubun"));
		result.siteCode	 = StringUtils.trimToNull2(from.get("siteCode"));
		result.scriptNo	 = Integer.parseInt(StringUtils.trimToNull2(from.get("scriptNo"),"0"));
		result.advertiserId	 = StringUtils.trimToNull2(from.get("advertiserId"));
		result.scriptUserId	 = StringUtils.trimToNull2(from.get("scriptUserId"));
		result.viewCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewCnt"),"0"));
		result.viewCnt2	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewCnt2"),"0"));
		result.viewCnt3	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewCnt3"),"0"));
		result.clickCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("clickCnt"),"0"));
		result.point	 = Float.parseFloat(StringUtils.trimToNull2(from.get("point"),"0"));
//		result.point	 = Float.parseFloat(StringUtils.trimToNull2(from.get("point"),"0"));
		result.mpoint	 = Float.parseFloat(StringUtils.trimToNull2(from.get("mpoint"),"0"));
		
		result.ADSTRD_CODE	= StringUtils.trimToNull2(from.get("nearCode"), "");
		result.NEW_IP_CNT	= Integer.parseInt(StringUtils.trimToNull2(from.get("NEW_IP_CNT"), "0"));
		result.RGN_IP_CNT	= Integer.parseInt(StringUtils.trimToNull2(from.get("RGN_IP_CNT"), "0"));

		return result;
	}

	public boolean isTargetYn() {
		return targetYn;
	}

	public void setTargetYn(boolean targetYn) {
		this.targetYn = targetYn;
	}
}
