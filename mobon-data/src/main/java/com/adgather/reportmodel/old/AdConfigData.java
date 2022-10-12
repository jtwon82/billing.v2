package com.adgather.reportmodel.old;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class AdConfigData  implements Serializable,Cloneable{
	private static final long serialVersionUID = 6912183909776607533L;

	@Override
	public AdConfigData clone(){
		Object obj = null;
		try {
			obj = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		AdConfigData copy=(AdConfigData)obj;
		return copy;
	}
	private String site_code = "";
	private String site_code_s = "";
	private String site_code_tmp = "";
	private String site_url = "";
	private String userid = "";
	private String site_etc = "";
	private String media_code = "";
	private String url_style = "";
	private String pop = "";
	private String kno = "0";
	private String scriptno = "";
	private String mediano = "";
	private String adtxt = "";
	private String no = "0";
	private String svc_type = "";
	private String recom_ad = "";
	private String dispo_sex = "";
	private String dispo_age = "";
	private String shop_tag = "";
	private String sales_url = "";

	private String site_title = "";
	private String site_name = "";
	private String adyn = "";
	private String site_desc = ""; // 25글자
	private String site_desc1 = ""; // 40글자 (1)
	private String site_desc2 = ""; // 40글자 (2)
	private String site_desc3 = ""; // 7글자
	private String site_desc4 = ""; // 15글자
	private String purl = "";
	private String turl = "";

	private String tgubun = "";
	private String corpname = "";
	// 일
	private String sun;
	// 월
	private String mon;
	// 화
	private String tue;
	// 수
	private String wed;
	// 목
	private String thu;
	// 금
	private String fri;
	// 토
	private String sat;
	private String imageInfo;
  private String imageBkg;
  private String imageState;
  
	public String getImageState() {
    return imageState;
  }

  public void setImageState(String imageState) {
    this.imageState = imageState;
  }

  public String getImageBkg() {
    return imageBkg;
  }

  public void setImageBkg(String imageBkg) {
    this.imageBkg = imageBkg;
  }

  public String getImageInfo() {
    return imageInfo;
  }

  public void setImageInfo(String imageInfo) {
    this.imageInfo = imageInfo;
  }

  public String getSun() {
    return sun;
  }

  public void setSun(String sun) {
    this.sun = sun;
  }

  public String getMon() {
    return mon;
  }

  public void setMon(String mon) {
    this.mon = mon;
  }

  public String getTue() {
    return tue;
  }

  public void setTue(String tue) {
    this.tue = tue;
  }

  public String getWed() {
    return wed;
  }

  public void setWed(String wed) {
    this.wed = wed;
  }

  public String getThu() {
    return thu;
  }

  public void setThu(String thu) {
    this.thu = thu;
  }

  public String getFri() {
    return fri;
  }

  public void setFri(String fri) {
    this.fri = fri;
  }

  public String getSat() {
    return sat;
  }

  public void setSat(String sat) {
    this.sat = sat;
  }
  // 프리퀀시 종료 처리 후 TG값 전달
	private String tg = "";
	/**
	 * banner_path1 : 360 * 50
	 * imgname : 250 * 250
	 * imgname2 : 120 * 600
	 * imgname3 : 728 * 90
	 * imgname4 : 300 * 180
	 * imgname5 : 800 * 1500
	 * imgname6 : 엣지
	 * imgname7 : 160 * 300
	 * imgname8 : 300 * 65
	 * imgname9 : 850 * 800
	 * imgname10 : 960 * 100
	 * imgname11 : 720 * 1230
	 * imgname12 : 160 * 600
	 * imgname13 : 640 * 350
	 * imgname14 : 250 * 250(라이플전용)
	 */
	private String imgname = "";
	private String imgname2 = "";
	private String imgname3 = "";
	private String imgname4 = "";
	private String imgname5 = "";
	private String imgname6 = "";
	private String imgname7 = "";
	private String imgname8 = "";
	private String imgname9 = "";
	private String imgname10 = "";
	private String imgname11 = "";
	private String imgname12 = "";
	private String imgname13 = "";
	private String imgname14 = "";
	private String imgname15 = "";
	private String imgname16 = "";
	private String imgname17 = "";
	private String imgname18 = "";
	private String imgname19 = ""; // 누끼1
	private String imgname20 = ""; // 누끼2

	private String imgname_logo = "";
	private String imgname_icon = "";
	private String skyProductImg = ""; // 브랜드링크 상품 띠배너 이미지
	private String imgpath = "";
	private String pnm = "";
	private String price = "";
	
	private String premium_yn = "";
	private String premium_html = "";
	
	public String getSkyProductImg() {
    return skyProductImg;
  }

  public void setSkyProductImg(String skyProductImg) {
    this.skyProductImg = skyProductImg;
  }

  public String getPremium_yn() {
		return premium_yn;
	}

	public void setPremium_yn(String premium_yn) {
		this.premium_yn = premium_yn;
	}

	public String getPremium_html() {
		return premium_html;
	}

	public void setPremium_html(String premium_html) {
		this.premium_html = premium_html;
	}
	/**
	 * 캠페인별 단가
	 */
	private String c_price = "";

	public String getTgubun() {
		return tgubun;
	}

	public void setTgubun(String tgubun) {
		this.tgubun = tgubun;
	}
	private String mallnm = "";
	private String pcode = "";
	private String gubun = "";

	private String banner_path1 = "";
	private String banner_path2 = "";
	private String site_descm = "";
	private String site_urlm = "";
	private String state = "";
	private String stateM = "";
	private String stateW = "";
	private String site_etcm = "";
	private String homepi = "";

	private String mcgb = "";
	private String cwgb = "";
	private String cate1 = "";
	private String cate = "";
	private String user_cate = "";
	private String cate_nm = "";
	private String user_cate_nm = "";

	private String gb = "";

	private String ismain = "";
	private String flag = "";

	private boolean adnew=false;
	private String adnew_html = "";
	private String external_html = ""; // 외부연동 html (2017.02.28 Mcover에서만 사용)

	private String schonlogo = "";

	private int width = 0;
	private int height = 0;
	private String rectType = "";

	private String media_no = "";
	private String script_no = "";
	private String ad_dcodeno = "";
	private String usemoney = ""; // 일 예산
	private String usedmoney = ""; // 일 캠페인 소진 금액
	private String ad_rhour = "";
	private String adweight = "";
	private String weight_type = "";
	/**
	 * 외부연동이미지
	 */
	private String externalImg = "";

	/**
	 * 퀴즈형 배너 모델 추가
	 * 작성자: 조규홍
	 * 작성일자: 2016-03-21
	 */
//	private QAInfo qaInfo = new QAInfo();
	private String pb_gubun = "";
	private String coup_no = "";


//	private RMInfo rmInfo;
//	private List<UMUrlMatchInfo> umInfoList;


	private String shortUrl = "";
	private String sex_yn = "";
	private String age_yn = "";
	private String cate_yn = "";
	private String ms_userid = "";
	private String ad_cate = "";
	private String camp_info = "";
	private String conv_con = "";
	private String r_gubun = "";
	private String app_url = "";
	private String app_name = "";
	private String comp_name = "";

	private Set<String> abtests;

	/**
	 * 모바일 배너 이미지
	 * 스타일썸에서 사용중.
	 */
	private String banner_path1_mobile = "";


	//CPI 노출제어를 위한 변수
	private String view_ratio = "";
	//실행률
	private String action_ctr = "";
	//ctr
	private String ctr = "";
	//광고주 ctr
	private String point_ctr = "";
	//eCPM
	private String eCPM = "";
	//CPI reg_date
	private String reg_date = "";
	//CPI 그룹코드
	private String group_no = "";
	//CPI ios url
	private String ios_url = "";
	//CPI 아이폰 패키지명
	private String bundle_id = "";
	//CPI 동영상 URL
	private String video_url = "";
	//CPI 연령제한
	private String age_limit = "";
	//CPI 개발사
	private String company_name = "";

	public String getBundle_id() {
		return bundle_id;
	}

	public void setBundle_id(String bundle_id) {
		this.bundle_id = bundle_id;
	}

	public String getVideo_url() {
		return video_url;
	}

	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}

	public String getAge_limit() {
		return age_limit;
	}

	public void setAge_limit(String age_limit) {
		this.age_limit = age_limit;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getIos_url() {
		return ios_url;
	}

	public void setIos_url(String ios_url) {
		this.ios_url = ios_url;
	}

	public String getGroup_no() {
		return group_no;
	}

	public void setGroup_no(String group_no) {
		this.group_no = group_no;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	public String geteCPM() {
		return eCPM;
	}

	public void seteCPM(String eCPM) {
		this.eCPM = eCPM;
	}

	public String getPoint_ctr() {
		return point_ctr;
	}

	public void setPoint_ctr(String point_ctr) {
		this.point_ctr = point_ctr;
	}

	public String getAction_ctr() {
		return action_ctr;
	}

	public void setAction_ctr(String action_ctr) {
		this.action_ctr = action_ctr;
	}

	public String getCtr() {
		return ctr;
	}

	public void setCtr(String ctr) {
		this.ctr = ctr;
	}

	public String getView_ratio() {
		return view_ratio;
	}

	public void setView_ratio(String view_ratio) {
		this.view_ratio = view_ratio;
	}
	private boolean isEmpty = false;

	public String getComp_name() {
		return comp_name;
	}

	public void setComp_name(String comp_name) {
		this.comp_name = comp_name;
	}

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public String getApp_url() {
		return app_url;
	}

	public void setApp_url(String app_url) {
		this.app_url = app_url;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public String getSex_yn() {
		return sex_yn;
	}

	public void setSex_yn(String sex_yn) {
		this.sex_yn = sex_yn;
	}

	public String getAge_yn() {
		return age_yn;
	}

	public void setAge_yn(String age_yn) {
		this.age_yn = age_yn;
	}

	public String getCate_yn() {
		return cate_yn;
	}

	public void setCate_yn(String cate_yn) {
		this.cate_yn = cate_yn;
	}

	public String getMs_userid() {
		return ms_userid;
	}

	public void setMs_userid(String ms_userid) {
		this.ms_userid = ms_userid;
	}

	public String getAd_cate() {
		return ad_cate;
	}

	public void setAd_cate(String ad_cate) {
		this.ad_cate = ad_cate;
	}

	public String getCamp_info() {
		return camp_info;
	}

	public void setCamp_info(String camp_info) {
		this.camp_info = camp_info;
	}

	public String getConv_con() {
		return conv_con;
	}

	public void setConv_con(String conv_con) {
		this.conv_con = conv_con;
	}

	public String getR_gubun() {
		return r_gubun;
	}

	public void setR_gubun(String r_gubun) {
		this.r_gubun = r_gubun;
	}

	public String getCoup_no() {
		return coup_no;
	}

	public void setCoup_no(String coup_no) {
		this.coup_no = coup_no;
	}
	private List<ShopData> shopDataList;

	private String shop_ad_yn = "N";

	public String getScript_no() {
		return script_no;
	}

	public void setScript_no(String script_no) {
		this.script_no = script_no;
	}

	public String getInfo(String s){
		try{
			return s +toString();
		}catch(Exception e){
			return "getInfo:"+e;
		}
	}
	public String getAdyn() {
		return adyn;
	}
	public void setAdyn(String adyn) {
		this.adyn = adyn;
	}
	public String getSite_etcm() {
		return site_etcm;
	}
	public void setSite_etcm(String site_etcm) {
		this.site_etcm = site_etcm;
	}
	public String getSvc_type() {
		return svc_type;
	}
	public void setSvc_type(String svc_type) {
		this.svc_type = svc_type;
	}
	public String getSite_code() {
		return site_code;
	}
	public void setSite_code(String siteCode) {
		site_code = siteCode;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getSite_etc() {
		if( site_etc==null)
			return "";
		else
			return site_etc;
	}
	public void setSite_etc(String siteEtc) {
		site_etc = siteEtc;
	}
	public String getMedia_code() {
		return media_code;
	}
	public void setMedia_code(String mediaCode) {
		media_code = mediaCode;
	}
	public String getUrl_style() {
		return url_style;
	}
	public void setUrl_style(String urlStyle) {
		url_style = urlStyle;
	}
	public String getPop() {
		return pop;
	}
	public void setPop(String pop) {
		this.pop = pop;
	}
	public String getSite_url() {
		return site_url;
	}
	public void setSite_url(String siteUrl) {
		site_url = siteUrl;
	}
	public String getKno() {
		return kno;
	}
	public void setKno(String kno) {
		this.kno = kno;
	}
	public String getScriptno() {
		return scriptno;
	}
	public void setScriptno(String scriptno) {
		this.scriptno = scriptno;
	}
	public String getAdtxt() {
		return adtxt;
	}
	public void setAdtxt(String adtxt) {
		this.adtxt = adtxt;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getSite_title() {
		return site_title;
	}
	public void setSite_title(String siteTitle) {
		site_title = siteTitle;
	}
	public String getSite_desc() {
		if (site_desc != null) site_desc = site_desc.replaceAll("\'", "").replaceAll("\"", "").replaceAll("\\\\", "");
		else site_desc = "";
		return site_desc;
	}
	public void setSite_desc(String siteDesc) {
		site_desc = siteDesc;
	}
	public String getImgname() {
		return imgname;
	}
	public void setImgname(String imgname) {
		this.imgname = imgname;
	}

	public String getImgname2() {
		return imgname2;
	}
	public void setImgname2(String imgname2) {
		this.imgname2 = imgname2;
	}
	public String getImgname3() {
		return imgname3;
	}
	public void setImgname3(String imgname3) {
		this.imgname3 = imgname3;
	}
	public String getImgname4() {
		return imgname4;
	}
	public void setImgname4(String imgname4) {
		this.imgname4 = imgname4;
	}
	public String getImgname5() {
		return imgname5;
	}
	public void setImgname5(String imgname5) {
		this.imgname5 = imgname5;
	}
	public String getPurl() {
		return purl;
	}
	public void setPurl(String purl) {
		this.purl = purl;
	}
	public String getImgpath() {
		return imgpath;
	}
	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}
	public String getPnm() {
		if (pnm != null ) pnm = pnm.replaceAll("\'", "").replaceAll("\"", "").replaceAll("\\\\", "");
		else pnm = "";
		return pnm;
	}
	public void setPnm(String pnm) {
		this.pnm = pnm;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getMallnm() {
		return mallnm;
	}
	public void setMallnm(String mallnm) {
		this.mallnm = mallnm;
	}
	public String getMediano() {
		return mediano;
	}
	public void setMediano(String mediano) {
		this.mediano = mediano;
	}
	public String getPcode() {
		return pcode;
	}
	public void setPcode(String pcode) {
		this.pcode = pcode;
	}
	public String getRecom_ad() {
		return recom_ad;
	}
	public void setRecom_ad(String recom_ad) {
		this.recom_ad = recom_ad;
	}
	public String getMcgb() {
		return mcgb;
	}
	public void setMcgb(String mcgb) {
		this.mcgb = mcgb;
	}
	public String getCwgb() {
		return cwgb;
	}
	public void setCwgb(String cwgb) {
		this.cwgb = cwgb;
	}
	public String getIsmain() {
		return ismain;
	}
	public void setIsmain(String ismain) {
		this.ismain = ismain;
	}
	public String getCate1() {
		return cate1;
	}
	public void setCate1(String cate1) {
		this.cate1 = cate1;
	}
	public String getCate() {
		return cate;
	}
	public void setCate(String cate) {
		this.cate = cate;
	}
	public String getGb() {
		return gb;
	}
	public void setGb(String gb) {
		this.gb = gb;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getDispo_sex() {
		return dispo_sex;
	}
	public void setDispo_sex(String dispo_sex) {
		this.dispo_sex = dispo_sex;
	}
	public String getDispo_age() {
		return dispo_age;
	}
	public void setDispo_age(String dispo_age) {
		this.dispo_age = dispo_age;
	}
	public String getShop_tag() {
		return shop_tag;
	}
	public void setShop_tag(String shop_tag) {
		this.shop_tag = shop_tag;
	}

	public String getImgname_logo() {
		return imgname_logo;
	}
	public void setImgname_logo(String imgname_logo) {
		this.imgname_logo = imgname_logo;
	}

	public String getSite_name() {
		if (site_name != null) site_name = site_name.replaceAll("\'", "").replaceAll("\"", "").replaceAll("\\\\", "");
		else site_name = "";
		return site_name;
	}
	public void setSite_name(String site_name) {
		this.site_name = site_name;
	}

	public String getGubun() {
		return gubun;
	}

	public void setGubun(String gubun) {
		this.gubun = gubun;
	}
	public String getBanner_path1() {
		return banner_path1;
	}
	public void setBanner_path1(String banner_path1) {
		this.banner_path1 = banner_path1;
	}
	public String getBanner_path2() {
		return banner_path2;
	}
	public void setBanner_path2(String banner_path2) {
		this.banner_path2 = banner_path2;
	}
	public String getSite_descm() {
		if (site_descm != null) site_descm = site_descm.replaceAll("\'", "").replaceAll("\"", "").replaceAll("\\\\", "");
		else site_descm = "";
		return site_descm;
	}
	public void setSite_descm(String site_descm) {
		this.site_descm = site_descm;
	}
	public String getSite_urlm() {
		return site_urlm;
	}
	public void setSite_urlm(String site_urlm) {
		this.site_urlm = site_urlm;
	}
	public String getImgname6() {
		return imgname6;
	}
	public void setImgname6(String imgname6) {
		this.imgname6 = imgname6;
	}
	public String getImgname7() {
		return imgname7;
	}
	public void setImgname7(String imgname7) {
		this.imgname7 = imgname7;
	}
	public String getImgname8() {
		return imgname8;
	}
	public void setImgname8(String imgname8) {
		this.imgname8 = imgname8;
	}
	public String getAdnew_html() {
		return adnew_html;
	}
	public void setAdnew_html(String adnew_html) {
		this.adnew_html = adnew_html;
	}
	public String getExternal_html() {
    return external_html;
  }

  public void setExternal_html(String external_html) {
    this.external_html = external_html;
  }
  public boolean isAdnew() {
		return adnew;
	}
	public void setAdnew(boolean adnew) {
		this.adnew = adnew;
	}

	public String getStateM() {
		return stateM;
	}

	public void setStateM(String stateM) {
		this.stateM = stateM;
	}

	public String getStateW() {
		return stateW;
	}

	public void setStateW(String stateW) {
		this.stateW = stateW;
	}

	public String getHomepi() {
		return homepi;
	}

	public void setHomepi(String homepi) {
		this.homepi = homepi;
	}

	public String getSite_code_tmp() {
		return site_code_tmp;
	}

	public void setSite_code_tmp(String site_code_tmp) {
		this.site_code_tmp = site_code_tmp;
	}

	public String getSchonlogo() {
		return schonlogo;
	}

	public void setSchonlogo(String schonlogo) {
		this.schonlogo = schonlogo;
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

	public String getRectType() {
		int width,height = 0;
		try {
			width = getWidth();
		}catch (Exception e) {
			width = 0;
		}
		try{
			height = getHeight();
		}catch (Exception e) {
			height = 0;
		}
		//정사각형
		if(checkRect(width,height)){
			rectType = "1";
		}
		//가로형직사각형
		else if(width > height){
			rectType = "2";
		}
		//세로형직사각형
		else if(height > width){
			rectType = "3";
		}
		return rectType;
	}

	public void setRectType(String rectType) {
		this.rectType = rectType;
	}

	//정사각형체크
	private static boolean checkRect(int width,int height){
		boolean check = false;
		int resultValue = 0;
		resultValue = width - height;
		if(width == height){
			check = true;
		}
		if(resultValue > -10 && resultValue < 10){
			check = true;
		}
		return check;
	}

	public String getMedia_no() {
		return media_no;
	}

	public void setMedia_no(String media_no) {
		this.media_no = media_no;
	}

	public String getAd_dcodeno() {
		return ad_dcodeno;
	}

	public void setAd_dcodeno(String ad_dcodeno) {
		this.ad_dcodeno = ad_dcodeno;
	}


	public String getAd_rhour() {
		return ad_rhour;
	}

	public void setAd_rhour(String ad_rhour) {
		this.ad_rhour = ad_rhour;
	}

	public String getUsemoney() {
		return usemoney;
	}

	public void setUsemoney(String usemoney) {
		this.usemoney = usemoney;
	}

	public String getUsedmoney() {
		return usedmoney;
	}

	public void setUsedmoney(String usedmoney) {
		this.usedmoney = usedmoney;
	}

	public String getAdweight() {
		return adweight;
	}

	public void setAdweight(String adweight) {
		this.adweight = adweight;
	}

	public String getWeight_type() {
		return weight_type;
	}

	public void setWeight_type(String weight_type) {
		this.weight_type = weight_type;
	}

	public String getImgname_icon() {
		return imgname_icon;
	}

	public void setImgname_icon(String imgname_icon) {
		this.imgname_icon = imgname_icon;
	}

	public String getImgname9() {
		return imgname9;
	}

	public void setImgname9(String imgname9) {
		this.imgname9 = imgname9;
	}

	public String getImgname10() {
		return imgname10;
	}

	public void setImgname10(String imgname10) {
		this.imgname10 = imgname10;
	}

	public String getImgname11() {
		return imgname11;
	}

	public void setImgname11(String imgname11) {
		this.imgname11 = imgname11;
	}

	public String getImgname12() {
		return imgname12;
	}

	public void setImgname12(String imgname12) {
		this.imgname12 = imgname12;
	}

	public String getImgname13() {
		return imgname13;
	}

	public void setImgname13(String imgname13) {
		this.imgname13 = imgname13;
	}

	public String getExternalImg() {
		return externalImg;
	}

	public void setExternalImg(String externalImg) {
		this.externalImg = externalImg;
	}

	public String getImgname14() {
		return imgname14;
	}

	public void setImgname14(String imgname14) {
		this.imgname14 = imgname14;
	}

	public String getImgname15() {
		return imgname15;
	}

	public void setImgname15(String imgname15) {
		this.imgname15 = imgname15;
	}

	public String getImgname16() {
		return imgname16;
	}

	public void setImgname16(String imgname16) {
		this.imgname16 = imgname16;
	}

	public String getImgname17() {
		return imgname17;
	}

	public void setImgname17(String imgname17) {
		this.imgname17 = imgname17;
	}

	public String getImgname18() {
		return imgname18;
	}

	public void setImgname18(String imgname18) {
		this.imgname18 = imgname18;
	}

	public String getImgname19() {
		return imgname19;
	}

	public void setImgname19(String imgname19) {
		this.imgname19 = imgname19;
	}

	public String getImgname20() {
		return imgname20;
	}

	public void setImgname20(String imgname20) {
		this.imgname20 = imgname20;
	}

	public String getSite_code_s() {
		return site_code_s;
	}
	public void setSite_code_s(String site_code_s) {
		this.site_code_s = site_code_s;
	}
		public String getSite_desc1() {
		return site_desc1;
	}

	public void setSite_desc1(String site_desc1) {
		this.site_desc1 = site_desc1;
	}

	public String getSite_desc2() {
		return site_desc2;
	}

	public void setSite_desc2(String site_desc2) {
		this.site_desc2 = site_desc2;
	}

	public String getC_price() {
		return c_price;
	}

	public void setC_price(String c_price) {
		this.c_price = c_price;
	}

	/**
	 * 상품타게팅된 여부를 체크함.
	 * pcode 데이터가 있으면 상품타게팅이 되었다고 판단함.
	 * @return
	 */
	public boolean isTargetProduct(){
		return StringUtils.isNotEmpty(this.pcode);
	}

	public String getPb_gubun() {
		return pb_gubun;
	}

	public void setPb_gubun(String pb_gubun) {
		this.pb_gubun = pb_gubun;
	}

	public String getSite_desc3() {
		return site_desc3;
	}

	public void setSite_desc3(String site_desc3) {
		this.site_desc3 = site_desc3;
	}

	public String getSite_desc4() {
		return site_desc4;
	}

	public void setSite_desc4(String site_desc4) {
		this.site_desc4 = site_desc4;
	}

	public List<ShopData> getShopDataList() {
		return shopDataList;
	}

	public void setShopDataList(List<ShopData> shopDataList) {
		this.shopDataList = shopDataList;
	}

	public void addShopDataList(ShopData shopData){
		if(CollectionUtils.isEmpty(shopDataList)) this.shopDataList = new ArrayList<ShopData>();
		this.shopDataList.add(shopData);
	}

	public String getShop_ad_yn() {
		return shop_ad_yn;
	}

	public void setShop_ad_yn(String shop_ad_yn){
		this.shop_ad_yn = shop_ad_yn;
	}

	public void setTg(String tg) {
		this.tg = tg;
	}

	public String getTg() {
		return tg;
	}

	public Set<String> getAbtests() {
		return abtests;
	}

	public void setAbtest(String abtestType) {
		if(this.abtests == null) {
			this.abtests = new HashSet<String>();
		} else if(this.abtests.size() > 0) {
			this.abtests.clear();
		}
		this.abtests.add(abtestType);
	}

	public String getTurl() {
		return turl;
	}

	public void setTurl(String turl) {
		this.turl = turl;
	}


	public String getUser_cate() {
		return user_cate;
	}

	public void setUser_cate(String user_cate) {
		this.user_cate = user_cate;
	}

	public String getUser_cate_nm() {
		return user_cate_nm;
	}

	public void setUser_cate_nm(String user_cate_nm) {
		this.user_cate_nm = user_cate_nm;
	}

	public String getCate_nm() {
		return cate_nm;
	}

	public void setCate_nm(String cate_nm) {
		this.cate_nm = cate_nm;
	}

	public String getBanner_path1_mobile() {
		return banner_path1_mobile;
	}

	public void setBanner_path1_mobile(String banner_path1_mobile) {
		this.banner_path1_mobile = banner_path1_mobile;
	}

	public String getCorpname() {
		return corpname;
	}

	public void setCorpname(String corpname) {
		this.corpname = corpname;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSales_url() {
		return sales_url;
	}

	public void setSales_url(String sales_url) {
		this.sales_url = sales_url;
	}
}