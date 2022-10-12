package com.adgather.reportmodel.old;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adgather.lang.old.ObjectToString;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.model.v15.ShopInfoData;
import com.mobon.billing.model.v15.ShopStatsInfoData;

import net.sf.json.JSONArray;

public class ShopData extends ObjectToString implements Serializable, Cloneable {
	private static final Logger	logger	= LoggerFactory.getLogger(ShopData.class);
	private static final long serialVersionUID = 1L;

	private long NO=0L;
	private String PARTID;
	private String TARGETGUBUN;
	private String site_code;
	private String site_code_s;
	private String ip;
	private String svc_type;
	private String userId;
	private String PCODE; //상품코드
	private String GB; //구분자
	private String MCGB;
	private String CWGB;
	private String SC_TXT;
	private String PNM; //상품명
	private String PURL; //상품 URL
	private String MOBILE_PURL; //상품 URL
	private String RDATE;
	private String RTIME;
	private String IMGPATH; //이미지 경로
	private String URL;
	private String RF;
	private String PRICE; //가격
	private String MOBILE_PRICE; //가격
	private String SITE_URL;
	private String CATE1; //카테고리1
	private String CATE2; //카테고리2
	private String site_etc;
	private String etc_type;
	private String mallnm;
	private String pcMobileGubun;
	//private String targetDate; //타겟팅 된 날짜
	private String status;
	private String croChk = "N";

	private Timestamp REGDATE;
	private Timestamp LASTUPDATE;
	private int width=0; //넓이
	private int height=0; //높이
	private String k1;
	private String STATE;
	private String kakao_status; //kakao 노출 상태 'Y' or 'N'

	private String dumpType; // dumpObject type

	// TODO : dump > was 이동
	private boolean mobileInsert = true;
	private boolean webInsert = true;
	private boolean INSERT_BOTH = false; // INSERT_SHOPDATA_BOTH_WEB_MOBILE 존재유무
	private boolean blockUserid = false; // 차단한 광고주여부
	private boolean checkMobileLink = false; // 모바일링크여부
	
  private String au_id;
  private boolean isDelete;

	private int partition=0;
	private Long offset=0L;
	private String key="";

	private boolean bHandlingStatsMobon = true;
	private boolean bHandlingStatsPointMobon = true;

	// 상품 할인정보(SHOP_DATA -> PRDT_PRMCT)
	private String prdtPrmct = "-1";
	private String liveChk = "Y";
	private String soldOut = "0";
	private List<String> mdPcode;
	private String appName="";

	//CAID3 추가 적재
	private String CAID3 = "";

	//CAID4 추가 적재
	private String CAID4 = "";

	public ShopData() {}
	public ShopData(ShopData data) {
		this.NO = data.NO;
		this.PARTID = data.PARTID;
		this.TARGETGUBUN = data.TARGETGUBUN;
		this.site_code = data.site_code;
		this.site_code_s = data.site_code_s;
		this.ip = data.ip;
		this.svc_type = data.svc_type;
		this.userId = data.userId;
/** [DEL SRC] yhlim 20170710 **/
/* 사용하지 않는 변수로 삭제
		this.flag = data.flag;
*/
		this.PCODE = data.PCODE;
		this.GB = data.GB;
		this.MCGB = data.MCGB;
		this.CWGB = data.CWGB;
		this.SC_TXT = data.SC_TXT;
		this.PNM = data.PNM;
		this.PURL = data.PURL;
		this.RDATE = data.RDATE;
		this.RTIME = data.RTIME;
		this.IMGPATH = data.IMGPATH;
		this.URL = data.URL;
		this.RF = data.RF;
		this.PRICE = data.PRICE;
		this.SITE_URL = data.SITE_URL;
		this.CATE1 = data.CATE1;
		this.CATE2 = data.CATE2;
		this.site_etc = data.site_etc;
		this.etc_type = data.etc_type;
		this.mallnm = data.mallnm;
		this.pcMobileGubun = data.pcMobileGubun;
		//this.targetDate = data.targetDate;
		this.kakao_status = data.kakao_status;
    this.status = data.status;
	this.CAID3 = data.CAID3;
	this.CAID4 = data.CAID4;
	}

	public static ShopData fromHashMap(Map from) {
		ShopData result = new ShopData();
		Map <String,Object> convertTreeMap = new TreeMap<String,Object>(String.CASE_INSENSITIVE_ORDER);
		
		convertTreeMap.putAll(from);
		
		result.CATE1	 = StringUtils.trimToNull2(convertTreeMap.get("CATE1"),"");
		result.CATE2	 = StringUtils.trimToNull2(convertTreeMap.get("CATE2"),"");
		result.CWGB	 = StringUtils.trimToNull2(convertTreeMap.get("CWGB"),"");
		result.GB	 = StringUtils.trimToNull2(convertTreeMap.get("GB"),"");
		result.IMGPATH	 = StringUtils.trimToNull2(convertTreeMap.get("IMGPATH"),"");
		result.INSERT_BOTH	 = Boolean.parseBoolean(StringUtils.trimToNull2(convertTreeMap.get("INSERT_BOTH")));
		//result.LASTUPDATE	 = StringUtils.trimToNull2(convertTreeMap.get("LASTUPDATE"));
		result.MCGB	 = StringUtils.trimToNull2(convertTreeMap.get("MCGB"),"");
		result.MOBILE_PRICE	 = StringUtils.trimToNull2(convertTreeMap.get("MOBILE_PRICE"),"");
		result.MOBILE_PURL	 = StringUtils.trimToNull2(convertTreeMap.get("MOBILE_PURL"),"");
		result.NO	 = Long.parseLong(StringUtils.trimToNull2(convertTreeMap.get("NO"),"0"));
		result.PARTID	 = StringUtils.trimToNull2(convertTreeMap.get("PARTID"),"");
		result.PCODE	 = StringUtils.trimToNull2(convertTreeMap.get("PCODE"),"");
		result.PNM	 = StringUtils.trimToNull2(convertTreeMap.get("PNM"),"");
		result.PRICE	 = StringUtils.trimToNull2(convertTreeMap.get("PRICE"),"").replace(",", "");
		result.PURL	 = StringUtils.trimToNull2(convertTreeMap.get("PURL"),"");
		result.RDATE	 = StringUtils.trimToNull2(convertTreeMap.get("RDATE"),"");
		//result.REGDATE	 = StringUtils.trimToNull2(convertTreeMap.get("REGDATE"));
		result.RF	 = StringUtils.trimToNull2(convertTreeMap.get("RF"),"");
		result.RTIME	 = StringUtils.trimToNull2(convertTreeMap.get("RTIME"),"");
		result.SC_TXT	 = StringUtils.trimToNull2(convertTreeMap.get("SC_TXT"),"");
		result.SITE_URL	 = StringUtils.trimToNull2(convertTreeMap.get("SITE_URL"),"");
		result.STATE	 = StringUtils.trimToNull2(convertTreeMap.get("STATE"),"");
		result.TARGETGUBUN	 = StringUtils.trimToNull2(convertTreeMap.get("TARGETGUBUN"),"");
		result.URL	 = StringUtils.trimToNull2(convertTreeMap.get("URL"),"");
		result.au_id	 = StringUtils.trimToNull2(convertTreeMap.get("au_id"),"");
		result.blockUserid	 = Boolean.parseBoolean(StringUtils.trimToNull2(convertTreeMap.get("blockUserid")));
		result.checkMobileLink	 = Boolean.parseBoolean(StringUtils.trimToNull2(convertTreeMap.get("checkMobileLink")));
		result.className	 = StringUtils.trimToNull2(convertTreeMap.get("className"),"");
		result.croChk	 = StringUtils.trimToNull2(convertTreeMap.get("croChk"),"");
		result.isDelete	 = Boolean.parseBoolean(StringUtils.trimToNull2(convertTreeMap.get("delete")));
		result.dumpType	 = StringUtils.trimToNull2(convertTreeMap.get("dumpType"),"");
		//result.empty	 = StringUtils.trimToNull2(convertTreeMap.get("empty"));
		result.endDate	 = StringUtils.trimToNull2(convertTreeMap.get("endDate"),"");
		result.etc_type	 = StringUtils.trimToNull2(convertTreeMap.get("etc_type"),"");
		result.height	 = Integer.parseInt(StringUtils.trimToNull2(convertTreeMap.get("height"),"0"));
		result.hh	 = StringUtils.trimToNull2(convertTreeMap.get("hh"),DateUtils.getDate("HH"));
		result.ip	 = StringUtils.trimToNull2(convertTreeMap.get("ip"),"");
		result.k1	 = StringUtils.trimToNull2(convertTreeMap.get("k1"),"");
		result.kakao_status	 = StringUtils.trimToNull2(convertTreeMap.get("kakao_status"),"");
		result.key	 = StringUtils.trimToNull2(convertTreeMap.get("key"),"");
		//result.limit	 = StringUtils.trimToNull2(convertTreeMap.get("limit"));
		result.mallnm	 = StringUtils.trimToNull2(convertTreeMap.get("mallnm"),"");
		result.mobileInsert	 = Boolean.parseBoolean(StringUtils.trimToNull2(convertTreeMap.get("mobileInsert")));
		result.orderBy	 = StringUtils.trimToNull2(convertTreeMap.get("orderBy"),"");
		result.pcMobileGubun	 = StringUtils.trimToNull2(convertTreeMap.get("pcMobileGubun"),"");
		result.sendDate	 = StringUtils.trimToNull2(convertTreeMap.get("sendDate"),"");
		result.site_code	 = StringUtils.trimToNull2(convertTreeMap.get("site_code"),"");
		result.site_code_s	 = StringUtils.trimToNull2(convertTreeMap.get("site_code_s"),"");
		result.site_etc	 = StringUtils.trimToNull2(convertTreeMap.get("site_etc"),"");
		result.startDate	 = StringUtils.trimToNull2(convertTreeMap.get("startDate"),"");
		result.status	 = StringUtils.trimToNull2(convertTreeMap.get("status"),"");
		result.svc_type	 = StringUtils.trimToNull2(convertTreeMap.get("svc_type"),"");
		result.targetDate	 = StringUtils.trimToNull2(convertTreeMap.get("targetDate"),"");
		result.userId	 = StringUtils.trimToNull2(convertTreeMap.get("userId"),"");
		result.webInsert	 = Boolean.parseBoolean(StringUtils.trimToNull2(convertTreeMap.get("webInsert")));
		result.width	 = Integer.parseInt(StringUtils.trimToNull2(convertTreeMap.get("width"),"0"));
		result.appName	= (StringUtils.trimToNull2(convertTreeMap.get("appName"),""));
		result.CAID3 = StringUtils.trimToNull2(from.get("CAID3"), "");
		result.CAID4 = StringUtils.trimToNull2(from.get("CAID4"), "");

		// 전환시 사용.
		result.setbHandlingStatsMobon(Boolean.parseBoolean(StringUtils.trimToNull2(convertTreeMap.get("bHandlingStatsMobon"),"false")));
		result.setbHandlingStatsPointMobon(Boolean.parseBoolean(StringUtils.trimToNull2(convertTreeMap.get("bHandlingStatsPointMobon"),"false")));
		
		String replaceCommaPrdtPrmct = convertTreeMap.get("prdtPrmct").toString().replace(",", "");
		if( StringUtils.isEmpty(replaceCommaPrdtPrmct) || !StringUtils.isNumeric(replaceCommaPrdtPrmct) ) {
			replaceCommaPrdtPrmct = "-1";
		}
		result.prdtPrmct	= replaceCommaPrdtPrmct;
		result.soldOut		= StringUtils.trimToNull2(convertTreeMap.get("soldOut"),"0");
		JSONArray list= (JSONArray) convertTreeMap.get("mdPcode");
		result.setMdPcode(list);
		
		return result;
	}
	
	public String getTargetDate() {
		return targetDate;
	}
	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}
	public String getInfo(String st){
		String rt="";
		try{
			rt= st + toString();
		}catch(Exception e){
		}
		return rt;
	}
	public void debug(String st){
	}

	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTARGETGUBUN() {
		if(TARGETGUBUN==null) TARGETGUBUN="";
		return TARGETGUBUN;
	}

	public void setTARGETGUBUN(String tARGETGUBUN) {
		TARGETGUBUN = tARGETGUBUN;
	}
	public String getMallnm() {
		return mallnm;
	}

	public void setMallnm(String mallnm) {
		this.mallnm = mallnm;
	}

	public String getCATE1() {
		return CATE1;
	}
	public void setCATE1(String cATE1) {
		CATE1 = cATE1;
	}
	
	public String getCATE2() {
		return CATE2;
	}
	public void setCATE2(String cATE2) {
		CATE2 = cATE2;
	}
	
	public String getPARTID() {
		return PARTID;
	}
	public void setPARTID(String pARTID) {
		PARTID = pARTID;
	}
	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}
	public String getPcMobileGubun() {
		return pcMobileGubun;
	}

	public void setPcMobileGubun(String pcMobileGubun) {
		this.pcMobileGubun = pcMobileGubun;
	}
	public String getRF() {
		return RF;
	}
	public void setRF(String rF) {
		RF = rF;
	}
	public String getSC_TXT() {
		return SC_TXT;
	}
	public void setSC_TXT(String sCTXT) {
		SC_TXT = sCTXT;
	}
	public String getMCGB() {
		if(MCGB==null) MCGB="";
		return MCGB;
	}
	public void setMCGB(String mCGB) {
		MCGB = mCGB;
	}
	public String getCWGB() {
		if(CWGB==null) CWGB="";
		return CWGB;
	}
	public void setCWGB(String cwGB) {
		CWGB = cwGB;
	}
	public String getRDATE() {
		return RDATE;
	}
	public void setRDATE(String rDATE) {
		RDATE = rDATE;
	}
	public String getRTIME() {
		return RTIME;
	}
	public void setRTIME(String rTIME) {
		RTIME = rTIME;
	}
	public String getPNM() {
		if(PNM!=null && PNM.length()>=50){
			PNM=PNM.substring(0,50);
		}
		if (PNM != null) PNM = PNM.replaceAll("\'", "").replaceAll("\"", "").replaceAll("\\\\", "");
		else PNM = "";
		return PNM;
	}
	public void setPNM(String pNM) {
		PNM = pNM;
	}
	public String getPCODE() {
		if(PCODE==null) PCODE="";
		return PCODE;
	}
	public void setPCODE(String pCODE) {
		PCODE = pCODE;
	}
	public String getPURL() {
		if(PURL==null) PURL="";
		return PURL;
	}
	public void setPURL(String pURL) {
		PURL = pURL;
	}
	public String getGB() {
		if(GB==null) GB="";
		return GB;
	}
	public void setGB(String gB) {
		GB = gB;
	}

	public String getIMGPATH() {
		return IMGPATH;
	}
	public void setIMGPATH(String iMGPATH) {
		IMGPATH = iMGPATH;
	}
	public String getPRICE() {
		return PRICE;
	}
	public void setPRICE(String pRICE) {
		PRICE = pRICE;
	}
	public long getNO() {
		return NO;
	}
	public void setNO(long nO) {
		NO = nO;
	}

	public String getSite_code() {
		return site_code;
	}
	public void setSite_code(String site_code) {
		this.site_code = site_code;
	}

	public String getSITE_URL() {
		return SITE_URL;
	}
	public void setSITE_URL(String sITEURL) {
		SITE_URL = sITEURL;
	}

	public String getSvc_type() {
		if(svc_type==null){
			return "";
		}else{
			return svc_type;
		}
	}

	public void setSvc_type(String svc_type) {
		this.svc_type = svc_type;
	}
	public String getEtc_type() {
		return etc_type;
	}
	public void setEtc_type(String etc_type) {
		this.etc_type = etc_type;
	}
	public String getSite_etc() {
		return site_etc;
	}
	public void setSite_etc(String site_etc) {
		this.site_etc = site_etc;
	}
	public Timestamp getLASTUPDATE() {
		return LASTUPDATE;
	}
	public void setLASTUPDATE(Timestamp lASTUPDATE) {
		LASTUPDATE = lASTUPDATE;
	}
	public Timestamp getREGDATE() {
		return REGDATE;
	}
	public void setREGDATE(Timestamp rEGDATE) {
		REGDATE = rEGDATE;
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

	public String getSTATE() {
		return STATE;
	}

	public void setSTATE(String sTATE) {
		STATE = sTATE;
	}

	public String getSite_code_s() {
		return site_code_s;
	}
	public void setSite_code_s(String site_code_s) {
		this.site_code_s = site_code_s;
	}
	public void encodedPnm(){
		if(StringUtils.isNotEmpty(this.PNM)){
			this.PNM = Base64.encodeBase64String(this.PNM.getBytes());
		}
	}
	public void decodePnm(){
		if(StringUtils.isNotEmpty(this.PNM)){
			if(Base64.isBase64(this.PNM))
				this.PNM = new String(Base64.decodeBase64(this.PNM));
		}
	}

	public void escapePnm(){
		if(StringUtils.isNotEmpty(this.PNM)){
			this.PNM = this.PNM.replaceAll("([\\\\\\[\\{\\(\\*\\?\\^\\$\\|])", "");
		}
	}
	public void escapeCate1(){
		if(StringUtils.isNotEmpty(this.CATE1)){
			this.CATE1 = this.CATE1.replaceAll("([\\\\\\[\\{\\(\\*\\?\\^\\$\\|])", "");
		}
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

	public void escapeCate2(){
		if(StringUtils.isNotEmpty(this.CATE2)){
			this.CATE2 = this.CATE2.replaceAll("([\\\\\\[\\{\\(\\*\\?\\^\\$\\|])", "");
		}
	}
	
	
	public String getKakao_status() {
		return kakao_status;
	}
	public void setKakao_status(String kakao_status) {
		this.kakao_status = kakao_status;
	}
	@Override
	public ShopData clone() {
		try {
			return (ShopData) super.clone();
		} catch (CloneNotSupportedException e) {
			return null; // 이 클래스가 Cloneable 하므로 clone() 메소드에서 절대 CloneNotSupportedException 발생 안하며, 실제 null 리턴은 없음.
		} // try
	}
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  /**
   * @return the au_id
   */
  public String getAu_id() {
    return au_id;
  }
  /**
   * @param au_id the au_id to set
   */
  public void setAu_id(String au_id) {
    this.au_id = au_id;
  }	

	public ShopInfoData toShopInfoData() {
		ShopInfoData result = new ShopInfoData();
		result.setKeyIp(this.getIp());
		result.setCwgb(this.getGB());
		result.setNo(this.getNO());
		result.setProduct(this.getSvc_type());
		result.setSiteCode(this.getSite_code());
		result.setpCode(this.getPCODE());
		result.setPARTID(this.getPARTID());
		result.setTargetDate(this.getTargetDate());
		result.setSiteCode(this.getSite_code_s());
		result.setAdvertiserId(this.getUserId());
		//result.setFlag(this.getFlag());
		result.setMcgb(this.getMCGB());
//		result.setCwgb(this.getGB());
		result.setScTxt(this.getSC_TXT());
		result.setPnm(this.getPNM());
		result.setpUrl(this.getPURL());
		result.setUrl(this.getURL());
		result.setrDate(this.getRDATE());
		result.setrTime(this.getRTIME());
		result.setImgPath(this.getIMGPATH());
		result.setRf(this.getRF());
		result.setPrice( Math.round(Float.parseFloat(this.getPRICE())) );
		result.setCate( this.getCATE1() );
		result.setCate2( this.getCATE2() );
		result.setMailnm(this.getMallnm());
		result.setKakaoStatus(this.getKakao_status());
		result.setSiteUrl(this.getSITE_URL());
		result.setSiteEtc(this.getSite_etc());
		result.setEtcType(this.getSite_etc());
		result.setPlatform(this.getPcMobileGubun());
		result.setRegDate(this.getREGDATE());
		result.setLastUpdate(this.getLASTUPDATE());
		result.setWidth(this.getWidth());
		result.setHeight(this.getHeight());
		result.setK1(this.getK1());
		//result.setState(this.getStatus());
		result.setStatus(this.getStatus());
		
		result.setMobileInsert(this.isMobileInsert());
		result.setWebInsert(this.isWebInsert());
		result.setINSERT_BOTH(this.isINSERT_BOTH());
		result.setBlockUserid(this.isBlockUserid());
		result.setCheckMobileLink(this.isCheckMobileLink());
		
		result.setPartition(this.getPartition());
		result.setOffset(this.getOffset());
		result.setKey(this.getKey());
		
		result.setHh(this.getHh());
		result.setSendDate(this.getSendDate());
		
		result.setbHandlingStatsMobon(this.isbHandlingStatsMobon());
		result.setbHandlingStatsPointMobon(this.isbHandlingStatsPointMobon());
		
		result.setPrdtPrmct(this.getPrdtPrmct());
		result.setSoldOut(this.getSoldOut());
		result.setMdPcode(this.getMdPcode());
		result.setAppName(this.getAppName());
		result.setCAID3(this.getCAID3());
		result.setCAID4(this.getCAID4());
		return result;
	}
	public ShopStatsInfoData toShopStatsInfoData() {
		ShopStatsInfoData result = new ShopStatsInfoData();
		result.setYyyymmdd(this.getRDATE());
		result.setAdvertiserId(this.getUserId());
		result.setpCode(this.getPCODE());
		result.setCate(this.getSC_TXT());
		result.setPlatform( this.isWebInsert()?"W":"M" );
		result.setViewCnt(0);
		result.setAdViewCnt(0);
		result.setAdClickCnt(1);
		result.setAdConvCnt(0);
		result.setAdConvPrice(0);
		result.setClickCnt(1);

		result.setPartition(this.getPartition());
		result.setOffset(this.getOffset());
		result.setKey(this.getKey());
		
		return result;
	}
	public String getDumpType() {
		return dumpType;
	}
	public void setDumpType(String dumpType) {
		this.dumpType = dumpType;
	}
	public String getMOBILE_PURL() {
		return MOBILE_PURL;
	}
	public void setMOBILE_PURL(String mOBILE_PURL) {
		MOBILE_PURL = mOBILE_PURL;
	}
	public String getMOBILE_PRICE() {
		return MOBILE_PRICE;
	}
	public void setMOBILE_PRICE(String mOBILE_PRICE) {
		MOBILE_PRICE = mOBILE_PRICE;
	}
	public String getCroChk() {
		return croChk;
	}
	public void setCroChk(String croChk) {
		this.croChk = croChk;
	}
	public boolean isMobileInsert() {
		return mobileInsert;
	}
	public void setMobileInsert(boolean mobileInsert) {
		this.mobileInsert = mobileInsert;
	}
	public boolean isWebInsert() {
		return webInsert;
	}
	public void setWebInsert(boolean webInsert) {
		this.webInsert = webInsert;
	}
	public boolean isDelete() {
		return isDelete;
	}
	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
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
	public String getPrdtPrmct() {
		return prdtPrmct;
	}
	public void setPrdtPrmct(String prdtPrmct) {
		this.prdtPrmct = prdtPrmct;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
}