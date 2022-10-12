package com.mobon.billing.sample.model;

import java.io.Serializable;
import java.util.Map;

import com.mobon.billing.sample.util.StringUtils;

public class SampleVo implements Serializable {
	private String dumpType; // dumpType	
	private String className; // 클래스이름
	private String sendDate;	// 전송날짜
	private String keyCode="";	// 고유키
	private String grouping=""; // 그룹핑

	private String advertiserId=""; // 광고주아이디 advertiserId
	private String scriptUserId=""; // 메체 userid
	private String type=""; // 노출, 클릭
	private String yyyymmdd=""; // 날짜
	private String hh=""; // 시간
	private String platform=""; // 웹 모바일 w, m
	private String adGubun=""; // 광고 구분
	private String product=""; // 일반베너/아이커버/브랜드링크  b, i, s
	private String siteCode=""; // 캠페인 코드
	private String kno="0"; // 비타겟일경우 adlink의 no값, 타겟의경우 0
	private int scriptNo=0; // 매체스크립트 코드
	private String media_code="0";
	private String interlock="01";	// 01: 모비온, 02: 데이지, 03: 카카오, 99:외부연동
	private String statYn="Y";
	private String seq="0";

	private int viewCnt=0; // 광고주 총노출
	private int viewCnt2=0; // 광고주 구좌노출
	private int viewCnt3=0; // 구좌노출 카운트
	private int clickCnt=0; // 클릭카운트
	private float point=0; // 포인트
	private float mpoint=0; // 메체 정산 포인트

	@Override
	public String toString() {
		return "SampleVo [dumpType=" + dumpType + ", className=" + className + ", sendDate=" + sendDate + ", keyCode="
				+ keyCode + ", grouping=" + grouping + ", advertiserId=" + advertiserId + ", scriptUserId="
				+ scriptUserId + ", type=" + type + ", yyyymmdd=" + yyyymmdd + ", hh=" + hh + ", platform=" + platform
				+ ", adGubun=" + adGubun + ", product=" + product + ", siteCode=" + siteCode + ", kno=" + kno
				+ ", scriptNo=" + scriptNo + ", media_code=" + media_code + ", interlock=" + interlock + ", statYn="
				+ statYn + ", seq=" + seq + ", viewCnt=" + viewCnt + ", viewCnt2=" + viewCnt2 + ", viewCnt3=" + viewCnt3
				+ ", clickCnt=" + clickCnt + ", point=" + point + ", mpoint=" + mpoint + "]";
	}
	
	public String generateKey() {
		keyCode = String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s", this.getAdGubun(), this.getPlatform()
				, this.getYyyymmdd(), this.getHh(), this.getProduct(), this.getSiteCode(), this.getScriptNo(), this.getType()
				, this.getKno(), this.getInterlock()
				, this.getAdvertiserId(), this.getScriptUserId(), this.getStatYn()
				);
//		setGrouping(String.format("[%s, %s]", this.getAdGubun(), this.getScriptNo()%10 ));
		setGrouping(String.format("[%s]", this.getScriptNo()%10 ));
		
		return keyCode;
	}
	
	public void sumGethering(Object _from) {
		SampleVo from = (SampleVo)_from;
		
		if ( "V".equals( from.getType() ) ) {
			if(from.getViewCnt()>0)		this.setViewCnt( this.getViewCnt() + from.getViewCnt() );
			if(from.getViewCnt2()>0)	this.setViewCnt2( this.getViewCnt2() + from.getViewCnt2() );
			if(from.getViewCnt3()>0)	this.setViewCnt3( this.getViewCnt3() + from.getViewCnt3() );
		}
		else if ( "C".equals( from.getType() ) ) {
			if( from.getClickCnt()>0 )	this.setClickCnt( this.getClickCnt() + from.getClickCnt() );
		}
		else if ( "CONV".equals( from.getType() ) ) {
			// convcnt ++
		}
		this.setPoint( this.getPoint() + from.getPoint() );
		this.setMpoint( this.getMpoint() + from.getMpoint() );
	}

	public static SampleVo fromHashMap(Map from) {
		SampleVo r = new SampleVo();

		java.util.Date date = new java.util.Date();
		java.text.DateFormat df = new java.text.SimpleDateFormat("HH");	///"yyyy-MM-dd"
		df.format(date).toString();
		
		r.setYyyymmdd( StringUtils.trimToNull2(from.get("yyyymmdd"),"") );
		r.setHh( StringUtils.trimToNull2(from.get("hh"), df.format(date).toString()) );
		r.setDumpType( StringUtils.trimToNull2(from.get("dumpType"),"") );
		r.setClassName( StringUtils.trimToNull2(from.get("className"),"") );
		r.setSendDate( StringUtils.trimToNull2(from.get("sendDate"),"") );
		r.setAdvertiserId( StringUtils.trimToNull2(from.get("userid"),"") );
		r.setScriptUserId( StringUtils.trimToNull2(from.get("scriptNo"),"") );
		r.setType( StringUtils.trimToNull2(from.get("type"),"V") );
		r.setPlatform( StringUtils.trimToNull2(from.get("platform"),"") );
		r.setProduct( StringUtils.trimToNull2(from.get("product"),"") );
		r.setAdGubun( StringUtils.trimToNull2(from.get("adGubun"),"") );
		r.setSiteCode( StringUtils.trimToNull2(from.get("site_code"),"") );
		r.setScriptNo( Integer.parseInt(StringUtils.trimToNull2(from.get("media_code"),"")) );
		r.setKno( StringUtils.trimToNull2(from.get("kno"),"0") );
		r.setStatYn( StringUtils.trimToNull2(from.get("statsYn"),"Y"));
		
		r.setViewCnt( Integer.parseInt(StringUtils.trimToNull2(from.get("viewcnt1"),"0")) );
		r.setViewCnt2( Integer.parseInt(StringUtils.trimToNull2(from.get("viewcnt2"),"0")) );
		r.setViewCnt3( Integer.parseInt(StringUtils.trimToNull2(from.get("viewcnt3"),"0")) );
		r.setClickCnt( Integer.parseInt(StringUtils.trimToNull2(from.get("clickcnt"),"0")) );
		r.setPoint( Float.parseFloat(StringUtils.trimToNull2(from.get("point"),"0")) );
		r.setMpoint( Float.parseFloat(StringUtils.trimToNull2(from.get("mpoint"),"0")) );
		
		return r;
	}

	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getDumpType() {
		return dumpType;
	}
	public void setDumpType(String dumpType) {
		this.dumpType = dumpType;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getKeyCode() {
		return keyCode;
	}
	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}
	public String getGrouping() {
		return grouping;
	}
	public void setGrouping(String grouping) {
		this.grouping = grouping;
	}
	public String getAdvertiserId() {
		return advertiserId;
	}
	public void setAdvertiserId(String advertiserId) {
		this.advertiserId = advertiserId;
	}
	public String getScriptUserId() {
		return scriptUserId;
	}
	public void setScriptUserId(String scriptUserId) {
		this.scriptUserId = scriptUserId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getYyyymmdd() {
		return yyyymmdd;
	}
	public void setYyyymmdd(String yyyymmdd) {
		this.yyyymmdd = yyyymmdd;
	}
	public String getHh() {
		return hh;
	}
	public void setHh(String hh) {
		this.hh = hh;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getAdGubun() {
		return adGubun;
	}
	public void setAdGubun(String adGubun) {
		this.adGubun = adGubun;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getSiteCode() {
		return siteCode;
	}
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	public String getKno() {
		return kno;
	}
	public void setKno(String kno) {
		this.kno = kno;
	}
	public int getScriptNo() {
		return scriptNo;
	}
	public void setScriptNo(int scriptNo) {
		this.scriptNo = scriptNo;
	}
	public String getInterlock() {
		return interlock;
	}
	public void setInterlock(String interlock) {
		this.interlock = interlock;
	}
	public String getStatYn() {
		return statYn;
	}
	public void setStatYn(String statYn) {
		this.statYn = statYn;
	}
	public int getViewCnt() {
		return viewCnt;
	}
	public void setViewCnt(int viewCnt) {
		this.viewCnt = viewCnt;
	}
	public int getViewCnt2() {
		return viewCnt2;
	}
	public void setViewCnt2(int viewCnt2) {
		this.viewCnt2 = viewCnt2;
	}
	public int getViewCnt3() {
		return viewCnt3;
	}
	public void setViewCnt3(int viewCnt3) {
		this.viewCnt3 = viewCnt3;
	}
	public int getClickCnt() {
		return clickCnt;
	}
	public void setClickCnt(int clickCnt) {
		this.clickCnt = clickCnt;
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
	public String getMedia_code() {
		return media_code;
	}
	public void setMedia_code(String media_code) {
		this.media_code = media_code;
	}
}
