package com.mobon.billing.model.v15;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.model.ClickViewData;

import net.sf.json.JSONArray;

public class ShopInfoData extends ClickViewData implements Serializable {
	private static final Logger	logger	= LoggerFactory.getLogger(ShopInfoData.class);

	private String PARTID; // 파티션아이디 PARTID
	private String targetGubun; // 타게팅여부 구분 targetGubun
	private String siteCodeS; // 배너의 캠페인코드 siteCodeS
	private String flag; // 임시변수 flag
	private String cwgb; // 장바구니 타겟구분 B,I,S cwgb
	private String scTxt; // 카테고리 scTxt
	private String pnm; // 상품명 pnm
	private String pUrl; // 상품 URL purl
	private String url; // purl 과 혼용한다 url
	private String rDate; // 날짜 rDate
	private String rTime; // 시간 rTime
	private String imgPath; // 이미지 경로 imgPath
	private String rf; // purl의 도메인 rf
	private int price; // 가격 price
	private String cate; // 카테고리 cate1
	private String cate2; // 카테고리 cate2
	private String mailnm; // 플렛폼 (cafe24, makeshop) mailnm
	private String targetDate; // 타게팅된 날짜 targetDate
	private String kakaoStatus; // kakao 노출 상태 'Y' or 'N' kakaoStatus
	private String siteUrl; // 사이트주소 siteUrl
	private String siteEtc; // 인식코드 siteEtc
	private String etcType; // etcType
	private Timestamp regDate; // 레디스-날짜 regDate
	private Timestamp lastUpdate; // 레디스-날짜 lastUpdate
	private int width; // 넓이 width
	private int height; // 높이 height
	private String k1; // 레디스-키워드모바일 k1
	private String status; // state

	private boolean mobileInsert = true;
	private boolean webInsert = true;
	private boolean INSERT_BOTH = false; // INSERT_SHOPDATA_BOTH_WEB_MOBILE 존재유무
	private boolean blockUserid = false; // 차단한 광고주여부
	private boolean checkMobileLink = false; // 모바일링크여부

	private boolean bHandlingStatsMobon = true;
	private boolean bHandlingStatsPointMobon = true;
	//상품 할인정보(SHOP_DATA -> PRDT_PRMCT)
	private String prdtPrmct="-1";
	private String liveChk="";
	private String soldOut="0";
	private List<String> mdPcode;
	private String rcmdPrdtCode="";
	private String appName="";

	//CAID3 추가 적재
	private String CAID3 = "";

	//CAID4 추가 적재
	private String CAID4 = "";
	
	public ShopInfoData() {
	}
	public ShopInfoData(String advertiserId, String pCode, String cate, String platform, String url, String pnm, int price, String imgPath, String kakaoStatus) {
		this.setAdvertiserId(advertiserId);
		this.setpCode(pCode);
		this.setCate(cate);
		this.setPlatform(platform);
		this.setUrl(url);
		this.setPnm(pnm);
		this.setPrice(price);
		this.setImgPath(imgPath);
		this.setKakaoStatus(kakaoStatus);
		
	}

	public static ShopInfoData fromHashMap(Map from) {
		ShopInfoData result = new ShopInfoData();
		result.blockUserid	 = Boolean.getBoolean(StringUtils.trimToNull2(from.get("blockUserid")));
		result.hh	 = StringUtils.trimToNull2(from.get("hh"),DateUtils.getDate("HH"));
		result.scTxt	 = StringUtils.trimToNull2(from.get("scTxt"),"");
		result.siteUrl	 = StringUtils.trimToNull2(from.get("siteUrl"),"");
		result.sendDate	 = StringUtils.trimToNull2(from.get("sendDate"),"");
		result.etcType	 = StringUtils.trimToNull2(from.get("etcType"),"");
		result.type	 = StringUtils.trimToNull2(from.get("type"),"");
		result.viewCnt3	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewCnt3"),"0"));
		result.viewCnt2	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewCnt2"),"0"));
		result.price	 = Integer.parseInt(StringUtils.trimToNull2(from.get("price"),"0"));
		result.viewCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewCnt"),"0"));
		result.scriptUserId	 = StringUtils.trimToNull2(from.get("scriptUserId"),"");
		result.mpoint	 = Float.parseFloat(StringUtils.trimToNull2(from.get("mpoint"),"0"));
		result.height	 = Integer.parseInt(StringUtils.trimToNull2(from.get("height"),"0"));
		result.mediasiteNo	 = Integer.parseInt(StringUtils.trimToNull2(from.get("mediasiteNo"),"0"));
		result.offset	 = Long.parseLong(StringUtils.trimToNull2(from.get("offset"),"0"));
		result.k1	 = StringUtils.trimToNull2(from.get("k1"),"");
		result.rDate	 = StringUtils.trimToNull2(from.get("rDate"),"");
		result.targetDate	 = StringUtils.trimToNull2(from.get("targetDate"),"");
		result.freqLog	 = Integer.parseInt(StringUtils.trimToNull2(from.get("freqLog"),"0"));
		result.grouping	 = StringUtils.trimToNull2(from.get("grouping"),"");
		result.pnm	 = StringUtils.trimToNull2(from.get("pnm"),"");
		result.INSERT_BOTH	 = Boolean.parseBoolean(StringUtils.trimToNull2(from.get("INSERT_BOTH")));
		result.keyCode	 = StringUtils.trimToNull2(from.get("keyCode"),"");
		result.rTime	 = StringUtils.trimToNull2(from.get("rTime"),"");
		result.webInsert	 = Boolean.parseBoolean(StringUtils.trimToNull2(from.get("webInsert")));
		result.rf	 = StringUtils.trimToNull2(from.get("rf"),"");
		result.checkMobileLink	 = Boolean.parseBoolean(StringUtils.trimToNull2(from.get("checkMobileLink")));
		result.imgPath	 = StringUtils.trimToNull2(from.get("imgPath"),"");
		//result.lastUpdate	 = StringUtils.trimToNull2(from.get("lastUpdate"));
		result.pCode	 = StringUtils.trimToNull2(from.get("pCode"),"");
		result.mailnm	 = StringUtils.trimToNull2(from.get("mailnm"),"");
		result.status	 = StringUtils.trimToNull2(from.get("status"),"");
		result.no	 = Long.parseLong(StringUtils.trimToNull2(from.get("no"),"0"));
		result.mcgb	 = StringUtils.trimToNull2(from.get("mcgb"),"");
		result.flag	 = StringUtils.trimToNull2(from.get("flag"),"");
		result.clickCnt	 = Integer.parseInt(StringUtils.trimToNull2(from.get("clickCnt"),"0"));
		result.kakaoStatus	 = StringUtils.trimToNull2(from.get("kakaoStatus"),"");
		//result.regDate	 = StringUtils.trimToNull2(from.get("regDate"));
		result.serverName	 = StringUtils.trimToNull2(from.get("serverName"),"");
		result.className	 = StringUtils.trimToNull2(from.get("className"),"");
		result.platform	 = StringUtils.trimToNull2(from.get("platform"),"");
		result.point	 = Float.parseFloat(StringUtils.trimToNull2(from.get("point"),"0"));
		result.targetGubun	 = StringUtils.trimToNull2(from.get("targetGubun"),"");
		result.partition	 = Integer.parseInt(StringUtils.trimToNull2(from.get("partition"),"0"));
		result.siteCodeS	 = StringUtils.trimToNull2(from.get("siteCodeS"),"0");
		result.scriptNo	 = Integer.parseInt(StringUtils.trimToNull2(from.get("scriptNo"),"0"));
		result.key	 = StringUtils.trimToNull2(from.get("key"),"");
		result.mobileInsert	 = Boolean.parseBoolean(StringUtils.trimToNull2(from.get("mobileInsert")));
		result.cate	 = StringUtils.trimToNull2(from.get("cate"),"");
		result.cate2	 = StringUtils.trimToNull2(from.get("cate2"),"");
		result.cwgb	 = StringUtils.trimToNull2(from.get("cwgb"),"");
		result.product	 = StringUtils.trimToNull2(from.get("product"),"");
		result.siteCode	 = StringUtils.trimToNull2(from.get("siteCode"),"");
		result.siteEtc	 = StringUtils.trimToNull2(from.get("siteEtc"),"");
		result.adGubun	 = StringUtils.trimToNull2(from.get("adGubun"),"");
		result.pUrl	 = StringUtils.trimToNull2(from.get("pUrl"),"");
		result.kno	 = StringUtils.trimToNull2(from.get("kno"),"");
		result.url	 = StringUtils.trimToNull2(from.get("url"),"");
		result.advertiserId	 = StringUtils.trimToNull2(from.get("advertiserId"),"");
		result.pbGubun	 = StringUtils.trimToNull2(from.get("pbGubun"),"");
		result.PARTID	 = StringUtils.trimToNull2(from.get("PARTID"),"");
		result.keyIp	 = StringUtils.trimToNull2(from.get("keyIp"),"");
		result.mobonLinkCate	 = StringUtils.trimToNull2(from.get("mobonLinkCate"),"");
		result.yyyymmdd	 = StringUtils.trimToNull2(from.get("yyyymmdd"),"");
		result.clickChk	 = Boolean.parseBoolean(StringUtils.trimToNull2(from.get("clickChk"),""));
		result.width	 = Integer.parseInt(StringUtils.trimToNull2(from.get("width"),"0"));
		result.interlock	 = StringUtils.trimToNull2(from.get("interlock"),"");
		result.appName	= StringUtils.trimToNull2(from.get("appName"),"");
		result.CAID3 = StringUtils.trimToNull2(from.get("CAID3"), "");
		result.CAID4 = StringUtils.trimToNull2(from.get("CAID4"), "");

		// 전환시 사용.
		result.setbHandlingStatsMobon(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsMobon"),"false")));
		result.setbHandlingStatsPointMobon(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsPointMobon"),"false")));
		
		// 할인금액
		result.prdtPrmct	= StringUtils.trimToNull2(from.get("prdtPrmct"),"-1");
		result.soldOut		= StringUtils.trimToNull2(from.get("soldOut"),"");
		JSONArray list= (JSONArray) from.get("mdPcode");
		result.setMdPcode(list);
		
		return result;
	}
	
	public String getPARTID() {
		return PARTID;
	}

	public void setPARTID(String pARTID) {
		PARTID = pARTID;
	}

	public String getTargetGubun() {
		return targetGubun;
	}

	public void setTargetGubun(String targetGubun) {
		this.targetGubun = targetGubun;
	}

	public String getSiteCodeS() {
		return siteCodeS;
	}

	public void setSiteCodeS(String siteCodeS) {
		this.siteCodeS = siteCodeS;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getCwgb() {
		return cwgb;
	}

	public void setCwgb(String cwgb) {
		this.cwgb = cwgb;
	}

	public String getScTxt() {
		return scTxt;
	}

	public void setScTxt(String scTxt) {
		this.scTxt = scTxt;
	}

	public String getPnm() {
		return pnm;
	}

	public void setPnm(String pnm) {
		this.pnm = pnm;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getrDate() {
		return rDate;
	}

	public void setrDate(String rDate) {
		this.rDate = rDate;
	}

	public String getrTime() {
		return rTime;
	}

	public void setrTime(String rTime) {
		this.rTime = rTime;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getRf() {
		return rf;
	}

	public void setRf(String rf) {
		this.rf = rf;
	}

	public String getCate() {
		return cate;
	}

	public void setCate(String cate) {
		this.cate = cate;
	}
	public String getCate2() {
		return cate2;
	}

	public void setCate2(String cate2) {
		this.cate2 = cate2;
	}
	public String getMailnm() {
		return mailnm;
	}

	public void setMailnm(String mailnm) {
		this.mailnm = mailnm;
	}

	public String getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}

	public String getKakaoStatus() {
		return kakaoStatus;
	}

	public void setKakaoStatus(String kakaoStatus) {
		this.kakaoStatus = kakaoStatus;
	}

	public String getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	public String getSiteEtc() {
		return siteEtc;
	}

	public void setSiteEtc(String siteEtc) {
		this.siteEtc = siteEtc;
	}

	public String getEtcType() {
		return etcType;
	}

	public void setEtcType(String etcType) {
		this.etcType = etcType;
	}

	public Timestamp getRegDate() {
		return regDate;
	}

	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
	}

	public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getK1() {
		return k1;
	}

	public void setK1(String k1) {
		this.k1 = k1;
	}

	public String getpUrl() {
		return pUrl;
	}

	public void setpUrl(String pUrl) {
		this.pUrl = pUrl;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getCAID3() {
		return CAID3;
	}

	public void setCAID3(String CAID3) {
		this.CAID3 = CAID3;
	}

	public String getCAID4() {
		return CAID4;
	}

	public void setCAID4(String CAID4) {
		this.CAID4 = CAID4;
	}

	@Override
	public String generateKey() {
		keyCode = String.format("%s_%s_%s_%s_%s", this.getAdvertiserId(), this.getpCode(), this.getPlatform(), this.isbHandlingStatsMobon(), this.isbHandlingStatsPointMobon());
		setKeyCodeMdPcode(String.format("%s_%s_%s_%s_%s_%s", this.getAdvertiserId(), this.getpCode(), this.getPlatform(), this.getRcmdPrdtCode(), this.isbHandlingStatsMobon(), this.isbHandlingStatsPointMobon()));
		grouping = String.format("[%s]", Math.abs(this.getAdvertiserId().hashCode())%20 );
//		grouping = String.format("[%s]", this.getPlatform() );
		return keyCode;
	}

	@Override
	public void sumGethering(Object _from) {
		ShopInfoData from = (ShopInfoData)_from;
		
		this.setViewCnt( this.getViewCnt() + from.getViewCnt() );
		
	}
	
	public boolean isWebInsert() {
		return webInsert;
	}
	public void setWebInsert(boolean webInsert) {
		this.webInsert = webInsert;
	}
	public boolean isMobileInsert() {
		return mobileInsert;
	}
	public void setMobileInsert(boolean mobileInsert) {
		this.mobileInsert = mobileInsert;
	}
	public boolean isINSERT_BOTH() {
		return INSERT_BOTH;
	}
	public void setINSERT_BOTH(boolean iNSERT_BOTH) {
		INSERT_BOTH = iNSERT_BOTH;
	}
	public boolean isBlockUserid() {
		return blockUserid;
	}
	public void setBlockUserid(boolean blockUserid) {
		this.blockUserid = blockUserid;
	}
	public boolean isCheckMobileLink() {
		return checkMobileLink;
	}
	public void setCheckMobileLink(boolean checkMobileLink) {
		this.checkMobileLink = checkMobileLink;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getPrdtPrmct() {
		return prdtPrmct;
	}
	public void setPrdtPrmct(String prdtPrmct) {
		this.prdtPrmct = prdtPrmct;
	}
	public String getLiveChk() {
		return liveChk;
	}
	public void setLiveChk(String liveChk) {
		this.liveChk = liveChk;
	}
	public String getSoldOut() {
		return soldOut;
	}
	public void setSoldOut(String soldOut) {
		this.soldOut = soldOut;
	}
	public List<String> getMdPcode() {
		return mdPcode;
	}
	public void setMdPcode(List<String> mdPcode) {
		this.mdPcode = mdPcode;
	}
	public String getRcmdPrdtCode() {
		return rcmdPrdtCode;
	}
	public void setRcmdPrdtCode(String rcmdPrdtCode) {
		this.rcmdPrdtCode = rcmdPrdtCode;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
}
