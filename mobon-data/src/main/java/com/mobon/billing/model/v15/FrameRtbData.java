package com.mobon.billing.model.v15;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import com.adgather.constants.G;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.model.ClickViewData;

import net.sf.json.JSONArray;

public class FrameRtbData extends ClickViewData implements Serializable {

	private static final long serialVersionUID = 1L;

	private String frameId="";
//	private int frameCycleNum;
	private int frameCycleNum=0;
	private String mediaCode="";
	private String mediaScriptNo="";
	private String algmSeq="";
	private String frameSelector="";
	private String prdtTpCode="";
	private String statsDttm="";
	private String yyyymmdd="";
	private String advrtsTpCode="";
	private float price=0;
	private float point=0;
	private float mpoint=0;
	private String frameCombiKey = "";		// 프레임 조합형 코드 키
	private String frameType = "";		// 프레임 조합형 코드 키
	private String abTestTy = "";

	private String userId = "";
	private String ordCode="";
	private String bnrCode="";
	
	private String frameSize = "";
	private String cate1 = "";
	private String imgTpCode="";
	private String matrAlgmSeq = "0";
	private JSONArray adverProdData = null;
	
	private String keyCodeCycleLog="";
	private String keyCodeDayStats="";
	private String keyCodeTrnLog="";
	private String keyCodeCombiDatStats="";
	private String keyCodeAdverDayStats="";
	private String keyCodeAdverDayAbStats="";
	private String keyCodeMediaAdverStats="";
	private String keyFrmeCodeStats="";
	private String keyFrameSizeStats = "";
	private String keyFrameActionLog = "";
	private String keyFrameCtgrDayStats = "";
	private String keyFrameKaistCombiDayStats = "";
	private String keyFrameDayABStats = "";
	
	private int browserSessionOrderCnt = 0;
	private float browserSessionOrderAmt = 0;
	private int sessionOrderCnt = 0;
	private float sessionOrderAmt = 0;
	private int directOrderCnt = 0;
	private float directOrderAmt = 0;
	
	private String ip = "";
	private String cookieDirect =  "";
	
	private String frameSendTpCode = "";
	private String ctgrNo = "";
	
	private BigDecimal divisionViewCnt;
	
	private String frameKaistRstCode = "";
	private String frameRtbTypeCode = "";
	
	private String direct;
	private String inHour;
	
	@Override
	public String generateKey() {
		keyCode = String.format("%s_%s_%s_%s_%s", this.getAdGubun(), this.getScriptNo()
				, this.getYyyymmdd(), this.getFrameId(), this.getFrameCycleNum()
				);
		
		keyCodeCycleLog = String.format("%s_%s_%s_%s_%s", this.getMediaCode(), this.getFrameSelector(), this.getPrdtTpCode()
				, this.getFrameId(), this.getFrameCycleNum()
				);
		
		grouping = String.format("[%s, %s]", this.getAdGubun(), this.getScriptNo()%20 );
		return keyCode;
	}
	
	public String keyCycleLog () {
		
		keyCodeCycleLog = String.format("%s_%s_%s_%s_%s_%s", this.getMediaCode(), this.getFrameSelector(), this.getPrdtTpCode(), this.getType()
				, this.getFrameId(), this.getFrameCycleNum()
				);
		
		grouping = String.format("[%s, %s]", this.getAdGubun(), this.getScriptNo()%20 );
		return keyCodeCycleLog;
	}
	
	public String keyDayStats() {
		
		keyCodeDayStats = String.format("%s_%s_%s_%s_%s_%s_%s_%s", this.getYyyymmdd(), this.getMediaCode(), this.getFrameSelector(), this.getType()
				, this.getPrdtTpCode(), this.getAdvrtsTpCode(), this.getFrameId(), this.getFrameSendTpCode()
				);
		
		grouping = String.format("[%s, %s]", this.getAdGubun(), this.getScriptNo()%20 );
		return keyCodeDayStats;
	}
	
	public String keyTrnLog() {
		
		keyCodeTrnLog = String.format("%s_%s_%s_%s", this.getFrameId(), this.getFrameCycleNum(), this.getFrameSelector(), this.getType()
				);
		
		grouping = String.format("[%s, %s]", this.getAdGubun(), this.getScriptNo()%20 );
		return keyCodeTrnLog;
	}
	
	public String keyCombiDayStats() {
		
		keyCodeCombiDatStats = String.format("%s_%s_%s_%s_%s_%s", this.getYyyymmdd(), this.getMediaCode(), this.getAdvrtsTpCode(), this.getType()
				, this.getFrameCombiKey(), this.getUserId()
				);
		
		grouping = String.format("[%s, %s]", this.getAdGubun(), this.getScriptNo()%20 );
		return keyCodeCombiDatStats;
	}

	public String keyAdverDayStats() {

		keyCodeAdverDayStats = String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s", this.getYyyymmdd(), this.getUserId(), this.getFrameSelector(), this.getType()
				, this.getPrdtTpCode(), this.getAdvrtsTpCode(), this.getFrameId(), this.getBnrCode(), this.getFrameSendTpCode()
		);

		grouping = String.format("[%s, %s]", this.getAdGubun(), this.getScriptNo()%20 );
		return keyCodeAdverDayStats;
	}

	public String keyAdverDayAbStats() {

		keyCodeAdverDayAbStats = String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s", this.getYyyymmdd(), this.getUserId(), this.getFrameSelector(), this.getType()
				, this.getPrdtTpCode(), this.getAdvrtsTpCode(), this.getFrameId(), this.getBnrCode(), this.getFrameSendTpCode(), this.getAbTestTy()
		);

		grouping = String.format("[%s, %s]", this.getAdGubun(), this.getScriptNo()%20 );
		return keyCodeAdverDayAbStats;
	}

	
	public String keyMediaAdverStats() {

		keyCodeMediaAdverStats = String.format("%s_%s_%s_%s_%s", this.getYyyymmdd(), this.getAdvertiserId(), this.getType(), this.getFrameId(), this.getMediaCode()
		);

		grouping = String.format("[%s, %s]", this.getAdGubun(), this.getScriptNo()%20 );
		return keyCodeMediaAdverStats;
	}

	public String keyFrmeCodeStats() {

		keyFrmeCodeStats = String.format("%s_%s_%s_%s_%s_%s_%s_%s", this.getYyyymmdd(), this.getPlatform(), this.getAdvrtsTpCode(), this.getType(), this.getFrameId(), this.getPrdtTpCode()
		, this.getDirect(), this.getInHour());

		grouping = String.format("[%s, %s]", this.getAdGubun(), this.getScriptNo()%20 );
		return keyFrmeCodeStats;
	}

	public String keyFrameSizeStats() {
		this.keyFrameSizeStats = String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s", this.getYyyymmdd(), this.getAdvertiserId(), this.getMatrAlgmSeq(), this.getPlatform(), 
					this.getPrdtTpCode(), this.getAdGubun(), this.getInterlock(),
				this.getBnrCode(), this.getFrameSize(), this.getImgTpCode(), this.getCate1(),this.getpCode() ,this.getType());
		
		grouping = String.format("[%s, %s]", this.getAdGubun(), this.getScriptNo()%20 );
		return keyFrameSizeStats;
	}
	
	public String keyActionLog() {
		this.keyFrameActionLog = String.format("%s_%s_%s_%s_%s_%s_%s",this.getYyyymmdd(),this.getAdvertiserId(),this.getIp(),this.getAlgmSeq(),
				this.getFrameSize(), this.getImgTpCode(), this.getCate1() );
		
		grouping = String.format("[%s, %s]", this.getAdGubun(), this.getScriptNo()%20 );
		return keyFrameActionLog;
	}
	
	public String keyAdverCtgrStats() {
		keyFrameCtgrDayStats = String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s", this.getYyyymmdd(), this.getAdvertiserId(), this.getFrameSelector(), this.getType()
				, this.getPrdtTpCode(), this.getAdvrtsTpCode(), this.getFrameId(), this.getBnrCode(), this.getCtgrNo()
		);

		grouping = String.format("[%s, %s]", this.getAdGubun(), this.getScriptNo()%20 );
		
		return keyFrameCtgrDayStats;
		
	}
	
	public String keyFrameKaistCombiDayStats() {
		keyFrameKaistCombiDayStats = String.format("%s_%s_%s_%s_%s_%s_%s_%s", this.getYyyymmdd(), this.getMediaCode(), this.getAdvrtsTpCode(), this.getType()
				, this.getFrameCombiKey(), this.getUserId(), this.getAlgmSeq(), this.getFrameKaistRstCode()
				);
		return null;
	}
	
	public String keyFrameCtgrDayStats() {
		keyFrameCtgrDayStats = String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s", this.getYyyymmdd(), this.getMediaCode(), this.getFrameSelector(), this.getType()
				, this.getPrdtTpCode(), this.getAdvrtsTpCode(), this.getFrameId(), this.getFrameSendTpCode(), this.getAdvertiserId()
				);
		return keyFrameCtgrDayStats;
	}
	
	public String keyFrameDayABStats() {
		keyFrameDayABStats = String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s", this.getYyyymmdd(), this.getMediaCode(), this.getFrameSelector(), this.getType()
				, this.getPrdtTpCode(), this.getAdvrtsTpCode(), this.getFrameId(), this.getFrameSendTpCode() , this.getAbTestTy()
				);
		
		grouping = String.format("[%s, %s]", this.getAdGubun(), this.getScriptNo()%20 );
		return keyFrameDayABStats;
	}
	
	@Override
	public void sumGethering(Object _from) {
		FrameRtbData from = (FrameRtbData)_from;
		
		if ( G.VIEW.equals( from.getType() ) ) {
			if(from.getViewCnt()>0)		this.setViewCnt( this.getViewCnt() + from.getViewCnt() );
			if(from.getViewCnt2()>0)	this.setViewCnt2( this.getViewCnt2() + from.getViewCnt2() );
			if(from.getViewCnt3()>0)	this.setViewCnt3( this.getViewCnt3() + from.getViewCnt3() );
			//FrameRtb 소재 이미지 viewcnt 분할 
			if(from.getAdverProdData().size() > 0) this.setDivisionViewCnt(this.getDivisionViewCnt().add(from.getDivisionViewCnt()));
		}
		else if ( G.CLICK.equals( from.getType() ) ) {
			if( from.getClickCnt()>0 )	this.setClickCnt( this.getClickCnt() + from.getClickCnt() );
			else this.setClickCnt( this.getClickCnt() + 1 );
		} // 컨버젼 추가
		
		else if( G.CONVERSION.equals(from.getType()) || G.CONVERSIONCHARGE.equals(from.getDumpType())) {
			if( from.getOrderCnt()>0 )	this.setOrderCnt( this.getOrderCnt() + from.getOrderCnt() );
			else this.setOrderCnt( this.getOrderCnt() + 1 );
			
			if (from.getBrowserSessionOrderCnt() > 0) this.setBrowserSessionOrderCnt(this.getBrowserSessionOrderCnt() + from.getBrowserSessionOrderCnt());
			if (from.getSessionOrderCnt() > 0) this.setSessionOrderCnt(this.getSessionOrderCnt() + from.getSessionOrderCnt());
			if (from.getDirectOrderCnt() > 0) this.setDirectOrderCnt( this.getDirectOrderCnt() + from.getDirectOrderCnt());
			
//			this.setPrice( this.getPrice() + from.getPrice() );
			if( from.getPrice()>=0 )	this.setPrice( this.getPrice() + from.getPrice() );
			if( from.getSessionOrderAmt() > 0) this.setSessionOrderAmt(this.getSessionOrderAmt() + from.getSessionOrderAmt());
			if( from.getBrowserSessionOrderAmt() > 0) this.setBrowserSessionOrderAmt(this.getBrowserSessionOrderAmt() + from.getBrowserSessionOrderAmt());
			if(from.getDirectOrderAmt() > 0) this.setDirectOrderAmt(this.getDirectOrderAmt() + from.getDirectOrderAmt());
		}
		
		this.setPoint( this.getPoint() + from.getPoint() );
		this.setMpoint( this.getMpoint() + from.getMpoint() );
		
	}

	
	public static FrameRtbData fromHashMap(Map from) {
		FrameRtbData result = new FrameRtbData();
		
		result.key = (String) from.get("key");
		result.yyyymmdd = (String) from.get("yyyymmdd");
		result.scriptNo = (Integer.parseInt( StringUtils.trimToNull2(from.get("scriptNo"),"0") ));
		result.advertiserId = (String) from.get("advertiserId");
		result.scriptUserId = (String) from.get("scriptUserId");
		result.viewCnt = (Integer.parseInt( StringUtils.trimToNull2(from.get("viewCnt1"),"0") ));
		result.viewCnt2 = (Integer.parseInt( StringUtils.trimToNull2(from.get("viewCnt2"),"0") ));
		result.viewCnt3 = (Integer.parseInt( StringUtils.trimToNull2(from.get("viewCnt3"),"0") ));
		result.clickCnt = (Integer.parseInt( StringUtils.trimToNull2(from.get("clickCnt"),"0") ));
		result.orderCnt = (Integer.parseInt( StringUtils.trimToNull2(from.get("orderCnt"),"0") ));
		result.point = (Float.parseFloat( StringUtils.trimToNull2(from.get("point"),"0") ));
		result.mpoint = (Float.parseFloat( StringUtils.trimToNull2(from.get("mpoint"),"0") ));
		result.frameId = (String) from.get("frameId");
		result.frameCycleNum = (Integer.parseInt( StringUtils.trimToNull2(from.get("frameCycleNum"),"0") ));
		result.mediaCode = (String) from.get("mediaCode");
		result.frameSelector = ( StringUtils.trimToNull2(from.get("frameSelector"),"0") );
		result.prdtTpCode = (String) from.get("prdtTpCode");
		result.type = (String) from.get("type");
		result.advrtsTpCode = (String) from.get("advrtsTpCode");
		result.price = (Integer.parseInt( StringUtils.trimToNull2(String.valueOf(from.get("price")),"0") ));
		result.userId = (String) from.get("userId");
		result.frameCombiKey = (String) from.get("bnrCode");
		result.frameSize = (String) from.get("frameSize");
		result.cate1 = (String) from.get("cate1");
		result.imgTpCode = (String) from.get("imgTpCode");
		
		result.frameSendTpCode = (String) from.get("frameSendTpCode");
		result.ctgrNo = StringUtils.trimToNull2((String) from.get("ctgrNo"), "");
				
//		result.setKey(from.getKey());
//		result.setSendDate(from.getSendDate());
		
//		result.setYyyymmdd(this.getYyyymmdd());
//		result.setPlatform(this.getPlatform());
//		result.setProduct(this.getProduct());
//		result.setAdGubun(this.getAdGubun());
//		result.setSiteCode(this.getSiteCode());
		
		return result;
	}
	
	
	
	public String getFrameId() {
		return frameId;
	}

	public void setFrameId(String frameId) {
		this.frameId = frameId;
	}

	public int getFrameCycleNum() {
		return frameCycleNum;
	}

	public void setFrameCycleNum(int frameCycleNum) {
		this.frameCycleNum = frameCycleNum;
	}

	public String getMediaScriptNo() {
		return mediaScriptNo;
	}

	public void setMediaScriptNo(String mediaScriptNo) {
		this.mediaScriptNo = mediaScriptNo;
	}

	public String getAlgmSeq() {
		return algmSeq;
	}

	public void setAlgmSeq(String algmSeq) {
		this.algmSeq = algmSeq;
	}

	public String getPrdtTpCode() {
		return prdtTpCode;
	}

	public void setPrdtTpCode(String prdtTpCode) {
		this.prdtTpCode = prdtTpCode;
	}

	public String getStatsDttm() {
		return statsDttm;
	}

	public void setStatsDttm(String statsDttm) {
		this.statsDttm = statsDttm;
	}

	public String getAdvrtsTpCode() {
		return advrtsTpCode;
	}

	public void setAdvrtsTpCode(String advrtsTpCode) {
		this.advrtsTpCode = advrtsTpCode;
	}

	public String getKeyCodeCycleLog() {
		return keyCodeCycleLog;
	}

	public void setKeyCodeCycleLog(String keyCodeCycleLog) {
		this.keyCodeCycleLog = keyCodeCycleLog;
	}

	public String getKeyCodeDayStats() {
		return keyCodeDayStats;
	}

	public void setKeyCodeDayStats(String keyCodeDayStats) {
		this.keyCodeDayStats = keyCodeDayStats;
	}

	public String getKeyCodeTrnLog() {
		return keyCodeTrnLog;
	}

	public void setKeyCodeTrnLog(String keyCodeTrnLog) {
		this.keyCodeTrnLog = keyCodeTrnLog;
	}

	public String getMediaCode() {
		return mediaCode;
	}

	public void setMediaCode(String mediaCode) {
		this.mediaCode = mediaCode;
	}

	public String getFrameSelector() {
		return frameSelector;
	}

	public void setFrameSelector(String frameSelector) {
		this.frameSelector = frameSelector;
	}

	public String getYyyymmdd() {
		return yyyymmdd;
	}

	public void setYyyymmdd(String yyyymmdd) {
		this.yyyymmdd = yyyymmdd;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getPoint() {
		return point;
	}

	public void setPoint(float point) {
		this.point = point;
	}

	public float getMpoint() {
		return mpoint;
	}

	public void setMpoint(float mpoint) {
		this.mpoint = mpoint;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFrameCombiKey() {
		return frameCombiKey;
	}

	public void setFrameCombiKey(String frameCombiKey) {
		this.frameCombiKey = frameCombiKey;
	}
	public String getFrameType() {
		return frameType;
	}

	public void setFrameType(String frameType) {
		this.frameType = frameType;
	}
	public String getOrdCode() {
		return ordCode;
	}

	public void setOrdCode(String ordCode) {
		this.ordCode = ordCode;
	}

	public String getKeyCodeCombiDatStats() {
		return keyCodeCombiDatStats;
	}

	public void setKeyCodeCombiDatStats(String keyCodeCombiDatStats) {
		this.keyCodeCombiDatStats = keyCodeCombiDatStats;
	}

	public String getKeyCodeAdverDayStats() {
		return keyCodeAdverDayStats;
	}

	public void setKeyCodeAdverDayStats(String keyCodeAdverDayStats) {
		this.keyCodeAdverDayStats = keyCodeAdverDayStats;
	}

	public String getKeyCodeMediaAdverStats() {
		return keyCodeMediaAdverStats;
	}

	public void setKeyCodeMediaAdverStats(String keyCodeMediaAdverStats) {
		this.keyCodeMediaAdverStats = keyCodeMediaAdverStats;
	}

	public String getBnrCode() {
		return bnrCode;
	}

	public void setBnrCode(String bnrCode) {
		this.bnrCode = bnrCode;
	}

	public String getKeyFrameSizeStats() {
		return keyFrameSizeStats;
	}

	public void setKeyFrameSizeStats(String keyFrameSizeStats) {
		this.keyFrameSizeStats = keyFrameSizeStats;
	}

	public String getFrameSize() {
		return frameSize;
	}

	public void setFrameSize(String frameSize) {
		this.frameSize = frameSize;
	}

	public String getCate1() {
		return cate1;
	}

	public void setCate1(String cate1) {
		this.cate1 = cate1;
	}

	public String getImgTpCode() {
		return imgTpCode;
	}

	public void setImgTpCode(String imgTpCode) {
		this.imgTpCode = imgTpCode;
	}

	public JSONArray getAdverProdData() {
		return adverProdData;
	}

	public void setAdverProdData(JSONArray adverProdData) {
		this.adverProdData = adverProdData;
	}

	public int getSessionOrderCnt() {
		return sessionOrderCnt;
	}

	public void setSessionOrderCnt(int sessionOrderCnt) {
		this.sessionOrderCnt = sessionOrderCnt;
	}

	public float getSessionOrderAmt() {
		return sessionOrderAmt;
	}

	public void setSessionOrderAmt(float sessionOrderAmt) {
		this.sessionOrderAmt = sessionOrderAmt;
	}

	public int getDirectOrderCnt() {
		return directOrderCnt;
	}

	public void setDirectOrderCnt(int directOrderCnt) {
		this.directOrderCnt = directOrderCnt;
	}

	public float getDirectOrderAmt() {
		return directOrderAmt;
	}

	public void setDirectOrderAmt(float directOrderAmt) {
		this.directOrderAmt = directOrderAmt;
	}

	public String getKeyFrameActionLog() {
		return keyFrameActionLog;
	}

	public void setKeyFrameActionLog(String keyFrameActionLog) {
		this.keyFrameActionLog = keyFrameActionLog;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getBrowserSessionOrderCnt() {
		return browserSessionOrderCnt;
	}

	public void setBrowserSessionOrderCnt(int browserSessionOrderCnt) {
		this.browserSessionOrderCnt = browserSessionOrderCnt;
	}

	public float getBrowserSessionOrderAmt() {
		return browserSessionOrderAmt;
	}

	public void setBrowserSessionOrderAmt(float browserSessionOrderAmt) {
		this.browserSessionOrderAmt = browserSessionOrderAmt;
	}

	public String getCookieDirect() {
		return cookieDirect;
	}

	public void setCookieDirect(String cookieDirect) {
		this.cookieDirect = cookieDirect;
	}

	public String getFrameSendTpCode() {
		return frameSendTpCode;
	}

	public void setFrameSendTpCode(String frameSendTpCode) {
		this.frameSendTpCode = frameSendTpCode;
	}

	public String getCtgrNo() {
		return ctgrNo;
	}

	public void setCtgrNo(String ctgrNo) {
		this.ctgrNo = ctgrNo;
	}

	public String getKeyFrameCtgrDayStats() {
		return keyFrameCtgrDayStats;
	}

	public void setKeyFrameCtgrDayStats(String keyFrameCtgrDayStats) {
		this.keyFrameCtgrDayStats = keyFrameCtgrDayStats;
	}

	public String getMatrAlgmSeq() {
		return matrAlgmSeq;
	}

	public void setMatrAlgmSeq(String matrAlgmSeq) {
		this.matrAlgmSeq = matrAlgmSeq;
	}

	public BigDecimal getDivisionViewCnt() {
		return divisionViewCnt;
	}

	public void setDivisionViewCnt(BigDecimal divisionViewCnt) {
		this.divisionViewCnt = divisionViewCnt;
	}

	public String getKeyFrameKaistCombiDayStats() {
		return keyFrameKaistCombiDayStats;
	}

	public void setKeyFrameKaistCombiDayStats(String keyFrameKaistCombiDayStats) {
		this.keyFrameKaistCombiDayStats = keyFrameKaistCombiDayStats;
	}

	public String getFrameKaistRstCode() {
		return frameKaistRstCode;
	}

	public void setFrameKaistRstCode(String frameKaistRstCode) {
		this.frameKaistRstCode = frameKaistRstCode;
	}

	public String getFrameRtbTypeCode() {
		return frameRtbTypeCode;
	}

	public void setFrameRtbTypeCode(String frameRtbTypeCode) {
		this.frameRtbTypeCode = frameRtbTypeCode;
	}

	public String getDirect() {
		return direct;
	}

	public void setDirect(String direct) {
		this.direct = direct;
	}

	public String getInHour() {
		return inHour;
	}

	public void setInHour(String inHour) {
		this.inHour = inHour;
	}

	public String getAbTestTy() {
		return abTestTy;
	}

	public void setAbTestTy(String abTestTy) {
		this.abTestTy = abTestTy;
	}

	public String getKeyCodeAdverDayAbStats() {
		return keyCodeAdverDayAbStats;
	}

	public void setKeyCodeAdverDayAbStats(String keyCodeAdverDayAbStats) {
		this.keyCodeAdverDayAbStats = keyCodeAdverDayAbStats;
	}

	public String getKeyFrameDayABStats() {
		return keyFrameDayABStats;
	}

	public void setKeyFrameDayABStats(String keyFrameDayABStats) {
		this.keyFrameDayABStats = keyFrameDayABStats;
	}

	
	
	
}
