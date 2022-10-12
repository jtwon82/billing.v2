package com.mobon.billing.model.v15;

import java.io.Serializable;
import java.util.Map;

import com.adgather.constants.G;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.model.ClickViewData;

public class AppTargetData extends ClickViewData implements Serializable {

	private static final long serialVersionUID = 1L;


	private boolean targetYn = false;

	@Override
	public String generateKey() {
		keyCode = String.format("%s_%s_%s_%s_%s_%s", this.getAdGubun(), this.getPlatform(), 
				this.getYyyymmdd(), this.getProduct(), this.getScriptNo(), this.getAdvertiserId());
		
		grouping = String.format("[%s, %s]", this.getAdGubun(), this.getPlatform());
		return keyCode;
	}
	
	@Override
	public void sumGethering(Object _from) {
		AppTargetData from = (AppTargetData)_from;
		
		if ( G.VIEW.equals( from.getType() ) ) {
			if(from.getViewCnt()>0)		this.setViewCnt( this.getViewCnt() + from.getViewCnt() );
			if(from.getViewCnt2()>0)	this.setViewCnt2( this.getViewCnt2() + from.getViewCnt2() );
			if(from.getViewCnt3()>0)	this.setViewCnt3( this.getViewCnt3() + from.getViewCnt3() );
		}
		else if ( G.CLICK.equals( from.getType() ) ) {
			if( from.getClickCnt()>0 )	this.setClickCnt( this.getClickCnt() + from.getClickCnt() );
			else this.setClickCnt( this.getClickCnt() + 1 );
		}
		
		this.setPoint( this.getPoint() + from.getPoint() );
		this.setMpoint( this.getMpoint() + from.getMpoint() );
		
	}
	
	
	public static AppTargetData fromHashMap(Map from) {
		AppTargetData result = new AppTargetData();
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
		result.mpoint	 = Float.parseFloat(StringUtils.trimToNull2(from.get("mpoint"),"0"));
		
		return result;
	}

	public boolean isTargetYn() {
		return targetYn;
	}

	public void setTargetYn(boolean targetYn) {
		this.targetYn = targetYn;
	}
}
