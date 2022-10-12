package com.mobon.billing.model.v15;

import java.io.Serializable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adgather.constants.G;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.model.ClickViewData;

public class NativeNonAdReportData extends ClickViewData implements Serializable {

	private static final Logger logger = LoggerFactory.getLogger(NativeNonAdReportData.class);
	
	private static final long serialVersionUID = 1L;

	@Override
	public String generateKey() {
		keyCode = String.format("%s_%s_%s_%s_%d_%s_%s", this.getAdGubun(), this.getPlatform(), 
				this.getYyyymmdd(), this.getProduct(), this.getScriptNo(), this.getType(), this.getInterlock());
		grouping = String.format("[%s, %s]", this.getAdGubun(), this.getPlatform());
		return keyCode;
	}
	
	@Override
	public void sumGethering(Object _from) {
		NativeNonAdReportData from = (NativeNonAdReportData)_from;
		if ( G.VIEW.equals( from.getType() ) ) {
			if(from.getViewCnt()>0) {
				this.setViewCnt( this.getViewCnt() + from.getViewCnt() );
			}
			// 연간기사
			if(from.getReNewsCnt() > 0) {
				this.setReNewsCnt(this.getReNewsCnt() + from.getReNewsCnt() );
			}
			// 오디언스기사
			if(from.getAuNewsCnt() > 0) {
				this.setAuNewsCnt(this.getAuNewsCnt() + from.getAuNewsCnt() );
			}
			// 인기기사
			if(from.getPoNewsCnt() > 0) {
				this.setPoNewsCnt(this.getPoNewsCnt() + from.getPoNewsCnt() );
			}
			// 최신기사
			if(from.getLaNewsCnt() > 0) {
				this.setLaNewsCnt(this.getLaNewsCnt() + from.getLaNewsCnt() );
			}
			// 카테고리기사
			if(from.getCmNewsCnt() > 0) {
				this.setCmNewsCnt(this.getCmNewsCnt() + from.getCmNewsCnt() );
			}
			// abTes
			if(from.getAbTestCnt() > 0) {
				this.setAbTestCnt(this.getAbTestCnt() + from.getAbTestCnt() );
			}
			// 리턴기사
			if(from.getRmNewsCnt() > 0) {
				this.setRmNewsCnt(this.getRmNewsCnt() + from.getRmNewsCnt() );
			}
			// 통계
			if(from.getTotalNewsCnt() > 0) {
				this.setTotalNewsCnt(this.getTotalNewsCnt() + from.getTotalNewsCnt() );
			}
			/*
			logger.debug("연간----------------->>>{}", this.getReNewsCnt());
			logger.debug("오디언스-------------->>>{}", this.getAuNewsCnt());
			logger.debug("인기----------------->>>{}", this.getPoNewsCnt());
			logger.debug("최신----------------->>>{}", this.getLaNewsCnt());
			logger.debug("카테고리-------------->>>{}", this.getCmNewsCnt());
			logger.debug("인기2---------------->>>{}", this.getAbTestCnt());
			logger.debug("리턴----------------->>>{}", this.getRmNewsCnt());
			logger.debug("total--------------->>>{}", this.getTotalNewsCnt());
			*/
		} else if ( G.CLICK.equals( from.getType() ) ) {
			if( from.getClickCnt() > 0 ) {
				// 클릭카운트
				this.setClickCnt( this.getClickCnt() + from.getClickCnt() );
				// 뉴스클릭카운트
				this.setNewsClickCnt( this.getNewsClickCnt() + from.getNewsClickCnt() );
			}
		} else if ( G.VALID_VIEW.equals( from.getType() ) ) {
			if( from.getAvalEprsCnt()>0 ) {
				this.setAvalEprsCnt( this.getAvalEprsCnt() + from.getAvalEprsCnt() );
			}
		} else if ( G.NATIVE_VIEW.equals( from.getType() ) ) {
			if( from.getNativeViewCnt()>0 ) {
				this.setNativeViewCnt( this.getNativeViewCnt() + from.getNativeViewCnt() );
			}
		}
	}
	
	/*
	 * param Setting 
	 * */
	public static NativeNonAdReportData fromHashMap(Map from) {
		NativeNonAdReportData result = new NativeNonAdReportData();
		result.type		 	 = StringUtils.trimToNull2(from.get("type"));
		result.yyyymmdd	 	 = StringUtils.trimToNull2(from.get("yyyymmdd"));
		result.platform	 	 = StringUtils.trimToNull2(from.get("platform"));
		result.product	 	 = StringUtils.trimToNull2(from.get("product"));
		result.adGubun	 	 = StringUtils.trimToNull2(from.get("adGubun"));
		result.newsType	 	 = StringUtils.trimToNull2(from.get("newsType"));
		result.interlock 	 = StringUtils.trimToNull2(from.get("chargeType"));
		result.scriptNo	 	 = Integer.parseInt(StringUtils.trimToNull2(from.get("scriptNo"),"0"));
		result.reNewsCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("reNewsCnt"),"0"));
		result.auNewsCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("auNewsCnt"),"0"));
		result.poNewsCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("poNewsCnt"),"0"));
		result.laNewsCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("laNewsCnt"),"0"));
		result.cmNewsCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("cmNewsCnt"),"0"));
		result.rmNewsCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("rmNewsCnt"),"0"));  
		result.totalNewsCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("totalNewsCnt"),"0"));
		result.abTestCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("abTestCnt"),"0"));
		if(G.VIEW.equals( result.type )) {
			result.viewCnt = 1;	
		}else if(G.CLICK.equals( result.type )) {
			result.clickCnt = 1;
			result.newsClickCnt=1;
		}else if(G.VALID_VIEW.equals( result.type )) {
			result.avalEprsCnt 	= 1;
		}else if(G.NATIVE_VIEW.equals( result.type )) {
			result.nativeViewCnt = 1;
		}
		return result;
	}
}
