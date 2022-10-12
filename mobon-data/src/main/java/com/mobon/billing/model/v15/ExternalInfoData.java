package com.mobon.billing.model.v15;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.adgather.constants.G;
import com.adgather.constants.old.GlobalConstants;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.model.ClickViewData;

public class ExternalInfoData extends ClickViewData implements Serializable {

	public int mediaSite; // 매체 key
	public String zoneId; // 외부연동 key zoneId
	public String adType; // 광고 크키 ex) i250_250 adType
	public String transmit; // S: 송출 , R: 수신 transmit
	public int viewCntMobon=0; // 외부연동 노출 viewCntMobon
	public int clickCntMobon=0; // 인라이플 클릭 clickCntMobon
	public int passbackCnt=0; // passbackCnt
	public int totalCall=0; // 총 요청 CALL totalCall
	public String imgName; // imgName
	public String imgType; // imgType
	public String siteName; // siteName
	public int imv; // CTR * 노출 imv
	public String externalId; // externalId
	public String externalName; // externalName
	public String regDate; // 등록날짜 regDate
	public String etc1; // etc1
	public String etc2; // etc2
	public String etc3; // etc3
	public String etc4; // etc4
	String product="01";
	
	public String interlock="99";	// 01: 모비온, 02: 데이지, 03: 카카오, 99:외부연동
	
	private int mediaSeq=0;	// ssp mediaSeq

	private boolean bHandlingStatsMobon = true;
	private boolean bHandlingStatsPointMobon = true;

	private boolean targetYn = false;
	
	public static ExternalInfoData fromHashMap(Map from) {
		ExternalInfoData result = new ExternalInfoData();
		result.mediaSite	 = Integer.parseInt(StringUtils.trimToNull2(from.get("mediaSite"),"0"));
		result.setExl_seq(StringUtils.trimToNull2(from.get("exl_seq"),"0"));
		result.setSend_tp_code(StringUtils.trimToNull2(from.get("send_tp_code"),""));
		result.zoneId	 = StringUtils.trimToNull2(from.get("zoneId"));
		result.clickCntMobon	 = Integer.parseInt(StringUtils.trimToNull2(from.get("clickCntMobon"),"0"));
		result.adType	 = StringUtils.trimToNull2(from.get("adType"));
		result.transmit	 = StringUtils.trimToNull2(from.get("transmit"));
		result.viewCntMobon	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewCntMobon"),"0"));
		result.passbackCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("passbackCnt"),"0"));
		result.totalCall	 = Integer.parseInt(StringUtils.trimToNull2(from.get("totalCall"),"0"));
		result.imgName	 = StringUtils.trimToNull2(from.get("imgName"));
		result.imgType	 = StringUtils.trimToNull2(from.get("imgType"));
		result.siteName	 = StringUtils.trimToNull2(from.get("siteName"));
		result.imv	 = Integer.parseInt(StringUtils.trimToNull2(from.get("imv"),"0"));
		result.externalId	 = StringUtils.trimToNull2(from.get("externalId"));
		result.externalName	 = StringUtils.trimToNull2(from.get("externalName"));
		result.regDate	 = StringUtils.trimToNull2(from.get("regDate"));
		result.etc1	 = StringUtils.trimToNull2(from.get("etc1"));
		result.etc2	 = StringUtils.trimToNull2(from.get("etc2"));
		result.etc3	 = StringUtils.trimToNull2(from.get("etc3"));
		result.etc4	 = StringUtils.trimToNull2(from.get("etc4"));
		
		result.hh	 = StringUtils.trimToNull2(from.get("hh"),DateUtils.getDate("HH"));
		result.no	 = Long.parseLong(StringUtils.trimToNull2(from.get("no"),"0"));
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
		result.mpoint	 = Float.parseFloat(StringUtils.trimToNull2(from.get("mpoint"),"0"));
		result.key	 = StringUtils.trimToNull2(from.get("key"));
		result.product	 = StringUtils.trimToNull2(from.get("product"),"01");
		result.siteCode	 = StringUtils.trimToNull2(from.get("siteCode"));
		result.mediasiteNo	 = Integer.parseInt(StringUtils.trimToNull2(from.get("mediasiteNo"),"0"));
		result.offset	 = Long.parseLong(StringUtils.trimToNull2(from.get("offset"),"0"));
		result.adGubun	 = StringUtils.trimToNull2(from.get("adGubun"));
		result.dumpType	= StringUtils.trimToNull2(from.get("dumpType"));
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

		// 전환시 사용.
		result.setbHandlingStatsMobon(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsMobon"),"false")));
		result.setbHandlingStatsPointMobon(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsPointMobon"),"false")));
		
		return result;
	}
	
	public ArrayList toList() {
		Map map = new HashMap();
		map.put("yyyymmdd", this.getYyyymmdd());
		map.put("exl_seq", this.getExl_seq());
		map.put("send_tp_code", this.getSend_tp_code());
		map.put("zoneId", this.getPlatform());
		map.put("advertiserId", this.getAdvertiserId());
		map.put("scriptUserId", this.getScriptUserId());
		map.put("mediaSite", this.getMediaSite());
		map.put("scriptNo", this.getScriptNo());
		map.put("adType", this.getAdType());
		map.put("adGubun", this.getAdGubun());
		map.put("transmit", this.getTransmit());
		map.put("siteCode", this.getSiteCode());
		map.put("type", this.getType());
		map.put("dumpType", this.getDumpType());
		map.put("interlock", this.getInterlock());
		map.put("viewCnt", this.getViewCnt());
		map.put("viewCntMobon", this.getViewCntMobon());
		map.put("clickCnt", this.getClickCnt());
		map.put("clickCntMobon", this.getClickCntMobon());
		map.put("passbackCnt", this.getPassbackCnt());
		map.put("totalCnt", this.getTotalCall());
		map.put("imv", this.getImv());
		map.put("point", this.getPoint());
		map.put("mpoint", this.getMpoint());
		ArrayList list = new ArrayList();
		return list;
	}
	
	public BaseCVData toBaseCVData() {
		BaseCVData result = new BaseCVData();
		result.setYyyymmdd( this.getYyyymmdd() ); //.put("yyyymmdd", yyyymmdd);
		result.setExl_seq( this.getExl_seq() ); //
		result.setSend_tp_code(this.getSend_tp_code());
		result.setPlatform( this.getPlatform() ); //.put("platform", platform);
		result.setProduct( this.getProduct() ); //.put("product", product);
		result.setAdGubun(this.getAdGubun() ); //.put("adGubun", adGubun);
		result.setType( this.getType() ); //.put("type", type);
		result.setSiteCode( this.getSiteCode() ); //.put("siteCode", siteCode);
		result.setScriptNo( this.getScriptNo() ); //.put("scriptNo", scriptNo);
		result.setAdvertiserId( this.getAdvertiserId() ); //.put("advertiserId", advertiserId);
		result.setScriptUserId( this.getScriptUserId() ); //.put("scriptUserId", scriptUserId);
		result.setInterlock( this.getInterlock() ); //.put("interlock", interlock);
		result.setKey( this.getKno() ); //.put("kno", kno);
		result.setViewCnt( this.getViewCnt() ); //.put("viewCnt", viewCnt);
		result.setViewCnt2( this.getViewCnt2() ); //.put("viewCnt2", viewCnt2);
		result.setViewCnt3( this.getViewCnt3() ); //.put("viewCnt3", viewCnt3);
		result.setClickCnt( this.getClickCnt() ); //.put("clickCnt", clickCnt);
		result.setPoint( this.getPoint() ); //.put("point", point);
		result.setMpoint( this.getMpoint() ); //.put("mpoint", mpoint);
		return result;
	}

	public ExternalInfoData() {
	}

	public int getMediaSite() {
		return mediaSite;
	}

	public void setMediaSite(int mediaSite) {
		this.mediaSite = mediaSite;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public int getClickCntMobon() {
		return clickCntMobon;
	}

	public void setClickCntMobon(int clickCntMobon) {
		this.clickCntMobon = clickCntMobon;
	}

	public String getAdType() {
		return adType;
	}

	public void setAdType(String adType) {
		this.adType = adType;
	}

	public String getTransmit() {
		return transmit;
	}

	public void setTransmit(String transmit) {
		this.transmit = transmit;
	}

	public int getViewCntMobon() {
		return viewCntMobon;
	}

	public void setViewCntMobon(int viewCntMobon) {
		this.viewCntMobon = viewCntMobon;
	}

	public int getPassbackCnt() {
		return passbackCnt;
	}

	public void setPassbackCnt(int passbackCnt) {
		this.passbackCnt = passbackCnt;
	}

	public int getTotalCall() {
		return totalCall;
	}

	public void setTotalCall(int totalCall) {
		this.totalCall = totalCall;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public String getImgType() {
		return imgType;
	}

	public void setImgType(String imgType) {
		this.imgType = imgType;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public int getImv() {
		return imv;
	}

	public void setImv(int imv) {
		this.imv = imv;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getExternalName() {
		return externalName;
	}

	public void setExternalName(String externalName) {
		this.externalName = externalName;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getEtc1() {
		return etc1;
	}

	public void setEtc1(String etc1) {
		this.etc1 = etc1;
	}

	public String getEtc2() {
		return etc2;
	}

	public void setEtc2(String etc2) {
		this.etc2 = etc2;
	}

	public String getEtc3() {
		return etc3;
	}

	public void setEtc3(String etc3) {
		this.etc3 = etc3;
	}

	public String getEtc4() {
		return etc4;
	}

	public void setEtc4(String etc4) {
		this.etc4 = etc4;
	}

	@Override
	public String generateKey() {
		keyCode = String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s", this.getAdGubun(), this.getDumpType()
				, this.getYyyymmdd(), this.getAdvertiserId(), this.getScriptNo(), this.getZoneId(), this.getType(), this.getTransmit(), this.getMediaSite());
		
		setKeyCodeExternal( String.format("%s_%s_%s_%s_%s_%s_%s_%s", this.getAdGubun(), this.getDumpType()
				, this.getYyyymmdd(), this.getSiteCode(), this.getScriptNo(), this.getType(), this.getExl_seq(), this.getSend_tp_code() ));
		
		grouping = String.format("[%s, %s]", this.getAdGubun(), this.getDumpType());
		setGroupingExternal(String.format("[%s]", this.getScriptNo()%20 ));
		
		return keyCode;
	}

	@Override
	public void sumGethering(Object _from) {
		
	}
	public void sumGethering(ExternalInfoData from) {
		
		if( G.EXTERNALCHARGE.equals(from.getDumpType()) ) {
			
//			if ("PV".equals(from.getType())) {
//				this.setPassbackCnt( this.getPassbackCnt() + from.getPassbackCnt() );
//				this.setViewCntMobon( this.getViewCntMobon() + from.getViewCntMobon() );
//				
//			} else if("C".equals(from.getType())){
//				this.setClickCntMobon( this.getClickCntMobon() + from.getClickCntMobon() );
//				
//			} else {
//				this.setViewCntMobon( this.getViewCntMobon() + from.getViewCntMobon() );
//			}

			this.setViewCntMobon( this.getViewCntMobon() + from.getViewCntMobon() );
			this.setClickCntMobon( this.getClickCntMobon() + from.getClickCntMobon() );
			this.setPassbackCnt( this.getPassbackCnt() + from.getPassbackCnt() );
			this.setViewCnt( this.getViewCnt() + from.getViewCnt() );
			this.setClickCnt( this.getClickCnt() + from.getClickCnt() );
			
		} else {
			if(from.getViewCnt()>0)		this.setViewCnt( this.getViewCnt() + from.getViewCnt() );
			if(from.getViewCnt2()>0)	this.setViewCnt2( this.getViewCnt2() + from.getViewCnt2() );
			if(from.getViewCnt3()>0)	this.setViewCnt3( this.getViewCnt3() + from.getViewCnt3() );
			if( from.getClickCnt()>0 )	this.setClickCnt( this.getClickCnt() + from.getClickCnt() );
		}

		this.setTotalCall( this.getTotalCall() + from.getTotalCall() );
		this.setPoint( this.getPoint() + from.getPoint() );
		this.setMpoint( this.getMpoint() + from.getMpoint() );
		this.setPointUSD( this.getPointUSD() + from.getPointUSD());
	}

	public String getInterlock() {
		return interlock;
	}

	public void setInterlock(String interlock) {
		this.interlock = interlock;
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

	public int getMediaSeq() {
		return mediaSeq;
	}

	public void setMediaSeq(int mediaSeq) {
		this.mediaSeq = mediaSeq;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public boolean isTargetYn() {
		return targetYn;
	}

	public void setTargetYn(boolean targetYn) {
		this.targetYn = targetYn;
	}
}
