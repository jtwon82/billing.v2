package com.adgather.reportmodel.old;

import java.io.Serializable;
import java.util.Map;

import com.adgather.lang.old.ObjectToString;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.model.v15.ExternalInfoData;

public class ExternalLinkageData extends ObjectToString implements Serializable{
	private static final long serialVersionUID = 1L;
	String sdate = "";		// 날짜 : yyyymmdd
	int media_site = 0;	// 매체 key
	int media_code = 0;	// 매체 스크립트 key
	private String exl_seq= "0"; //  송출순서
	private String send_tp_code= ""; //전송구분코드
	String transmit = "R"; //  S: 송출 , R: 수신
	String zoneid = "1555";		// 와이더플래닛 key
	String external_id;
	String external_name;
	String userid = "widerplanet";		// 와이더플래닛 id
	String site_code = "8bf3b57466094cead02d2ac0226f0c3f";	// 와이더플래닛의 사이트코드
	String ad_type = "";	// 광고 크키 ex) i250_250
	int viewcnt = 0;	// 와이더플래닛 노출
	int viewcnt_mobon = 0;	// 인라이플 노출
	int clickcnt = 0;	// 와이더플래닛 클릭
	int clickcnt_mobon = 0;	// 인라이플 클릭
	int imv = 0;		// CTR * 노출
	int passback_cnt = 0;
	float point = 0;		// 매체비
	float ppoint = 0;		// 
	float pointUSD = 0;		// 외화
	String regdate = "";	// 등록날짜
	String gubun = "";		// 광고 구분자
	int totalcall = 0;	// 총 요청 CALL
	String media_id = "";		// 매체ID
	String type = "";		// TYPE : 'P' 패스백
	String etc1;
	String etc2;
	String etc3;
	String etc4;
	String product="01";

	String imgname;
	String imgtype;
	String site_name;
	
	private String dumpType; // dumpObject type
	private int partition=0;
	private Long offset=0L;
	private String key="";
	float mpoint=0;
	
	private int mediaSeq =0;	// ssp mediaSeq

	private boolean bHandlingStatsMobon = true;
	private boolean bHandlingStatsPointMobon = true;
	
	public static ExternalLinkageData fromHashMap(Map from) {
		ExternalLinkageData result = new ExternalLinkageData();
		result.exl_seq	= StringUtils.trimToNull2(from.get("exl_seq"),"0");
		result.send_tp_code	= StringUtils.trimToNull2(from.get("send_tp_code"),"");
		result.ad_type	 = StringUtils.trimToNull2(from.get("ad_type"),"");
		result.className	 = StringUtils.trimToNull2(from.get("className"),"");
		result.clickcnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("clickcnt"),"0"));
		result.clickcnt_mobon	 = Integer.parseInt(StringUtils.trimToNull2(from.get("clickcnt_mobon"),"0"));
		result.dumpType	 = StringUtils.trimToNull2(from.get("dumpType"),"");
		//result.empty	 = StringUtils.trimToNull2(from.get("empty"));
		//result.endDate	 = StringUtils.trimToNull2(from.get("endDate"));
		result.etc1	 = StringUtils.trimToNull2(from.get("etc1"),"");
		result.etc2	 = StringUtils.trimToNull2(from.get("etc2"),"");
		result.etc3	 = StringUtils.trimToNull2(from.get("etc3"),"");
		result.etc4	 = StringUtils.trimToNull2(from.get("etc4"),"");
		result.external_id	 = StringUtils.trimToNull2(from.get("external_id"),"");
		result.external_name	 = StringUtils.trimToNull2(from.get("external_name"),"");
		result.gubun	 = StringUtils.trimToNull2(from.get("gubun"),"");
		result.hh	 = StringUtils.trimToNull2(from.get("hh"), DateUtils.getDate("HH"));
		result.imgname	 = StringUtils.trimToNull2(from.get("imgname"),"");
		result.imgtype	 = StringUtils.trimToNull2(from.get("imgtype"),"");
		result.imv	 = Integer.parseInt(StringUtils.trimToNull2(from.get("imv"),"0"));
		result.key	 = StringUtils.trimToNull2(from.get("key"),"");
		result.offset	 = Long.parseLong(StringUtils.trimToNull2(from.get("offset"),"0"));
		result.partition	 = Integer.parseInt(StringUtils.trimToNull2(from.get("partition"),"0"));
		//result.limit	 = Integer.parseInt(StringUtils.trimToNull2(from.get("limit")));
		result.media_code	 = Integer.parseInt(StringUtils.trimToNull2(from.get("media_code"),"0"));
		result.media_id	 = StringUtils.trimToNull2(from.get("media_id"),"");
		result.media_site	 = Integer.parseInt(StringUtils.trimToNull2(from.get("media_site"),"0"));
		//result.orderBy	 = StringUtils.trimToNull2(from.get("orderBy"));
		result.passback_cnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("passback_cnt"), "0"));
		result.point	 = Float.parseFloat(StringUtils.trimToNull2(from.get("point"),"0"));
		result.regdate	 = StringUtils.trimToNull2(from.get("regdate"),"");
		result.sdate	 = StringUtils.trimToNull2(from.get("sdate"),"");
		result.sendDate	 = StringUtils.trimToNull2(from.get("sendDate"),"");
		result.site_code	 = StringUtils.trimToNull2(from.get("site_code"),"");
		result.site_name	 = StringUtils.trimToNull2(from.get("site_name"),"");
		//result.startDate	 = StringUtils.trimToNull2(from.get("startDate"));
		//result.targetDate	 = StringUtils.trimToNull2(from.get("targetDate"));
		result.totalcall	 = Integer.parseInt(StringUtils.trimToNull2(from.get("totalcall"),"0"));
		result.transmit	 = StringUtils.trimToNull2(from.get("transmit"),"");
		result.type	 = StringUtils.trimToNull2(from.get("type"),"");
		result.userid	 = StringUtils.trimToNull2(from.get("userid"),"");
		result.viewcnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewcnt"),"0"));
		result.viewcnt_mobon	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewcnt_mobon"),"0"));
		result.zoneid	 = StringUtils.trimToNull2(from.get("zoneid"),"");
		result.product	= StringUtils.trimToNull2(from.get("product"),"01");
		result.mpoint	 = Float.parseFloat(StringUtils.trimToNull2(from.get("mpoint"),"0"));
		result.ppoint	= Float.parseFloat(StringUtils.trimToNull2(from.get("ppoint"),"0"));
		result.pointUSD	= Float.parseFloat(StringUtils.trimToNull2(from.get("pointUSD"),"0"));
		
		result.setMediaSeq( Integer.parseInt(StringUtils.trimToNull2(from.get("mediaSeq"),"0")) );

		// 전환시 사용.
		result.setbHandlingStatsMobon(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsMobon"),"false")));
		result.setbHandlingStatsPointMobon(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsPointMobon"),"false")));
		
		return result;
	}

	public ExternalInfoData toExternalInfoData(){
		ExternalInfoData result = new ExternalInfoData();
		result.setExl_seq(this.getExl_seq());
		result.setSend_tp_code(this.getSend_tp_code());
		result.setHh(this.getHh());
		result.setSendDate(this.getSendDate());
		result.setYyyymmdd(this.getSdate());
		result.setScriptUserId(this.getMedia_id());
		result.setMediaSite(this.getMedia_site());
		result.setScriptNo(this.getMedia_code());
		result.setZoneId(this.getZoneid());
		result.setAdvertiserId(this.getUserid());
		result.setSiteCode(this.getSite_code());
		result.setClickCntMobon(this.getClickcnt_mobon());
		result.setAdType(this.getAd_type());
		result.setType(this.getType());
		result.setPoint(this.getPoint());
		result.setPpoint(this.getPpoint());
		result.setPointUSD(this.getPointUSD());
		result.setMpoint(this.getMpoint());
		result.setAdGubun(this.getGubun());
		result.setTransmit(this.getTransmit());
		result.setViewCntMobon(this.getViewcnt_mobon());
		result.setPassbackCnt(this.getPassback_cnt());
		result.setViewCnt(this.getViewcnt());
		result.setClickCnt(this.getClickcnt());
		result.setTotalCall(this.getTotalcall());
		result.setImgName(this.getImgname());
		result.setImgType(this.getImgtype());
		result.setSiteName(this.getSite_name());
		result.setImv(this.getImv());
		result.setExternalId(this.getExternal_id());
		result.setExternalName(this.getExternal_name());
		result.setRegDate(this.getRegdate());
		result.setEtc1(this.getEtc1());
		result.setEtc2(this.getEtc2());
		result.setEtc3(this.getEtc3());
		result.setEtc4(this.getEtc4());
		result.setDumpType(this.getDumpType());
		result.setKey(this.getKey());
		result.setOffset(this.getOffset());
		result.setPartition(this.getPartition());
		result.setExl_seq(this.getExl_seq());
		result.setProduct(this.getProduct());
		
		result.setMediaSeq(this.getMediaSeq());
		
		result.setbHandlingStatsMobon(this.isbHandlingStatsMobon());
		result.setbHandlingStatsPointMobon(this.isbHandlingStatsPointMobon());

		result.generateKey();
		
		return result;
	}
	
	public String getSite_name() {
		return site_name;
	}
	public void setSite_name(String site_name) {
		this.site_name = site_name;
	}
	public String getImgtype() {
		return imgtype;
	}
	public void setImgtype(String imgtype) {
		this.imgtype = imgtype;
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
	public String getImgname() {
		return imgname;
	}
	public void setImgname(String imgname) {
		this.imgname = imgname;
	}
	public int getPassback_cnt() {
		return passback_cnt;
	}
	public void setPassback_cnt(int passback_cnt) {
		this.passback_cnt = passback_cnt;
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
	public String getZoneid() {
		return zoneid;
	}
	public void setZoneid(String zoneid) {
		this.zoneid = zoneid;
	}
	public String getExternal_id() {
		return external_id;
	}
	public void setExternal_id(String external_id) {
		this.external_id = external_id;
	}
	public String getExternal_name() {
		return external_name;
	}
	public void setExternal_name(String external_name) {
		this.external_name = external_name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMedia_id() {
		return media_id;
	}
	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}
	public String getSdate() {
		return sdate;
	}
	public void setSdate(String sdate) {
		this.sdate = sdate;
	}
	public int getMedia_site() {
		return media_site;
	}
	public void setMedia_site(int media_site) {
		this.media_site = media_site;
	}
	public int getMedia_code() {
		return media_code;
	}
	public void setMedia_code(int media_code) {
		this.media_code = media_code;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getSite_code() {
		return site_code;
	}
	public void setSite_code(String site_code) {
		this.site_code = site_code;
	}
	public String getAd_type() {
		return ad_type;
	}
	public void setAd_type(String ad_type) {
		this.ad_type = ad_type;
	}
	public int getViewcnt() {
		return viewcnt;
	}
	public void setViewcnt(int viewcnt) {
		this.viewcnt = viewcnt;
	}
	public int getViewcnt_mobon() {
		return viewcnt_mobon;
	}
	public void setViewcnt_mobon(int viewcnt_mobon) {
		this.viewcnt_mobon = viewcnt_mobon;
	}
	public int getClickcnt() {
		return clickcnt;
	}
	public void setClickcnt(int clickcnt) {
		this.clickcnt = clickcnt;
	}
	public int getClickcnt_mobon() {
		return clickcnt_mobon;
	}
	public void setClickcnt_mobon(int clickcnt_mobon) {
		this.clickcnt_mobon = clickcnt_mobon;
	}
	public int getImv() {
		return imv;
	}
	public void setImv(int imv) {
		this.imv = imv;
	}
	public float getPoint() {
		return point;
	}
	public void setPoint(float point) {
		this.point = point;
	}
	public String getRegdate() {
		return regdate;
	}
	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	public String getGubun() {
		return gubun;
	}
	public void setGubun(String gubun) {
		this.gubun = gubun;
	}
	public int getTotalcall() {
		return totalcall;
	}
	public void setTotalcall(int totalcall) {
		this.totalcall = totalcall;
	}

	public String getInfo(String s){
		try{
			return s +toString();
		}catch(Exception e){
			return "getInfo:"+e;
		}
	}
	
	public String getTransmit() {
		return transmit;
	}
	public void setTransmit(String transmit) {
		this.transmit = transmit;
	}
	public void init(){}

	public String getDumpType() {
		return dumpType;
	}
	public void setDumpType(String dumpType) {
		this.dumpType = dumpType;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public int getPartition() {
		return partition;
	}

	public void setPartition(int partition) {
		this.partition = partition;
	}
	public float getPpoint() {
		return ppoint;
	}

	public void setPpoint(float ppoint) {
		this.ppoint = ppoint;
	}

	public float getMpoint() {
		return mpoint;
	}

	public void setMpoint(float mpoint) {
		this.mpoint = mpoint;
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

	public String getExl_seq() {
		return exl_seq;
	}

	public void setExl_seq(String exl_seq) {
		this.exl_seq = exl_seq;
	}

	public String getSend_tp_code() {
		return send_tp_code;
	}

	public void setSend_tp_code(String send_tp_code) {
		this.send_tp_code = send_tp_code;
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

	public float getPointUSD() {
		return pointUSD;
	}

	public void setPointUSD(float pointUSD) {
		this.pointUSD = pointUSD;
	}
}