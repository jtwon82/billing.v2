package com.adgather.reportmodel.old;

import java.io.Serializable;

import com.adgather.constants.old.GlobalConstants;

public class MediaScriptData  implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String Y = "Y";

	private String userId; //사용자 아이디
	private String AD_TYPE;//광고타입
	private String no;
	private String H_TYPE; //하우스배너타입
	private String H_BANNER; //하우스배너
	private String mediasite_no; 
	private String accept_sr; //본상품 승인여부
	private String accept_rc; //리사이클 승인여부
	private String accept_um; //성향광고승인여부
	private String accept_kl; //키워드광고승인여부
	private String accept_ad; //기본광고승인여부
	private String accept_st; //투데이베스트 승인여부
	private String accept_sp; //추천상품승인여부
	private String accept_rm; //리턴매칭 승인여부
	private String accept_cw; //장바구니 승인여부
	private String accept_pb; //퍼포먼스배너(퀴즈형) 승인여부
	private String accept_hu; //헤비유저 승인여부
	private String accept_pm; //프리미엄광고 승인여부
	private String accept_sj; //쇼핑입점 승인여부

	private String siteurl;
	private String limit_domains;
	private int weight_pct;
	private int bid_price;
	private String price_type;
	private String limit_pop;
	private String weight_type;
	private String imgcode = "01";
	private String imgcodeTemp;
	private String w_type;
	private String actype;
	private float acprice;
	private int acper;
	private String deduct;
	private String frameRtb_yn;
	private String mobonlinkcate = "none";
	private String r_gubun;
	private String icover_useyn;
	private float icover_time;
	private boolean isEmpty = false;
	private int pb_weight; // 퀴즈형 배너 비율
	private String two_yn; // 엠커버 투뎁스 사용여부
	private String mcover_type;
	private String psb_url;
	private String corpname;
	private String sitename;
	private String scate;
	
	public String getScate() {
		return scate;
	}
	public void setScate(String scate) {
		this.scate = scate;
	}
	public String getSitename() {
		return sitename;
	}
	public void setSitename(String sitename) {
		this.sitename = sitename;
	}
	public String getCorpname() {
		return corpname;
	}
	public void setCorpname(String corpname) {
		this.corpname = corpname;
	}
	public String getAccept_sj() {
		return accept_sj;
	}
	public void setAccept_sj(String accept_sj) {
		this.accept_sj = accept_sj;
	}
	public String getAccept_pm() {
		return accept_pm;
	}
	public void setAccept_pm(String accept_pm) {
		this.accept_pm = accept_pm;
	}
	
	public int getPb_weight() {
		return pb_weight;
	}

	public void setPb_weight(int pb_weight) {
		this.pb_weight = pb_weight;
	}

	public String getR_gubun() {
		return r_gubun;
	}

	public void setR_gubun(String r_gubun) {
		this.r_gubun = r_gubun;
	}

	public String getMobonlinkcate() {
		return mobonlinkcate;
	}

	public void setMobonlinkcate(String mobonlinkcate) {
		this.mobonlinkcate = mobonlinkcate;
	}

	public String getActype() {
		return actype;
	}

	public void setActype(String actype) {
		this.actype = actype;
	}

	public float getAcprice() {
		return acprice;
	}

	public void setAcprice(float acprice) {
		this.acprice = acprice;
	}

	public int getAcper() {
		return acper;
	}

	public void setAcper(int acper) {
		this.acper = acper;
	}

	public String getDeduct() {
		return deduct;
	}

	public void setDeduct(String deduct) {
		this.deduct = deduct;
	}

	public MediaScriptData() {
	}

	public MediaScriptData(String no) {
		this.no = no;
	}

	public MediaScriptData(String no, String imgcode) {
		this.no = no;
		this.imgcode = imgcode;
	}

	public MediaScriptData(String no, String imgcode, String type) {
		this.no = no;
		this.imgcode = imgcode;
		if ("".equals(type)) this.w_type = "";
	}

	public String getInfo(String a){
		try{
			return a + toString();
		}catch(Exception e){
			return "";
		}
	}

	public String getAccept_rc() {
		return accept_rc;
	}
	public void setAccept_rc(String accept_rc) {
		this.accept_rc = accept_rc;
	}

	public String getAccept_cw() {
		return accept_cw;
	}
	public void setAccept_cw(String accept_cw) {
		this.accept_cw = accept_cw;
	}

	public String getAccept_hu() {
		return accept_hu;
	}
	public void setAccept_hu(String accept_hu) {
		this.accept_hu = accept_hu;
	}
	
	/** 해당 광고 허용유무
	 * example) <br/> 
	 * MediaScriptData data = new MediaScriptData();<br/>
	 * boolean isAcceptSR = data.isAccept(GlobalConstants.SR);<br/>
	 * @param adGubun SR,KL,UM,KL,AD,ST,SP,RC,CW
	 * @return 
	 */
	public boolean isAccept(String adgubun){
		if (GlobalConstants.SR.equals(adgubun)) {
			return Y.equals(accept_sr);
		} else if (GlobalConstants.RC.equals(adgubun)) {
			return Y.equals(accept_rc);
		} else if (GlobalConstants.RM.equals(adgubun) || GlobalConstants.RR.equals(adgubun)) {
			return Y.equals(accept_rm);
		} else if (GlobalConstants.UM.equals(adgubun)){
			return Y.equals(accept_um);
		} else if (GlobalConstants.KL.equals(adgubun)) {
			return Y.equals(accept_kl);
		} else if (GlobalConstants.AD.equals(adgubun)) {
			return Y.equals(accept_ad);
		} else if (GlobalConstants.ST.equals(adgubun)) {
			return Y.equals(accept_st);
		} else if (GlobalConstants.SP.equals(adgubun)) {
			return Y.equals(accept_sp);
		} else if (GlobalConstants.CW.equals(adgubun)) {
			return Y.equals(accept_cw);
		} else if (GlobalConstants.HU.equals(adgubun)) {
			return Y.equals(accept_hu);
		} else {
			return false;
		} // if
	}

	public boolean isAcceptSr() { return accept_sr != null && Y.equals(accept_sr); }
	public boolean isAcceptRc() { return accept_rc != null && Y.equals(accept_rc); }
	public boolean isAcceptRm() { return accept_rm != null && Y.equals(accept_rm); }
	public boolean isAcceptUm() { return accept_um != null && Y.equals(accept_um); }
	public boolean isAcceptKl() { return accept_kl != null && Y.equals(accept_kl); }
	public boolean isAcceptAd() { return accept_ad != null && Y.equals(accept_ad); }
	public boolean isAcceptSt() { return accept_st != null && Y.equals(accept_st); }
	public boolean isAcceptSp() { return accept_sp != null && Y.equals(accept_sp); }
	public boolean isAcceptCw() { return accept_cw != null && Y.equals(accept_cw); }
	public boolean isAcceptHu() { return accept_hu != null && Y.equals(accept_hu); }

	public String getW_type() {
		return w_type;
	}
	public void setW_type(String w_type) {
		this.w_type = w_type;
	}
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAD_TYPE() {
		return AD_TYPE;
	}
	public void setAD_TYPE(String aDTYPE) {
		AD_TYPE = aDTYPE;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getH_BANNER() {
		return H_BANNER;
	}
	public void setH_BANNER(String hBANNER) {
		H_BANNER = hBANNER;
	}
	public String getMediasite_no() {
		return mediasite_no;
	}
	public void setMediasite_no(String mediasite_no) {
		this.mediasite_no = mediasite_no;
	}

	public String getAccept_sr() {
		return accept_sr;
	}

	public void setAccept_sr(String accept_sr) {
		this.accept_sr = accept_sr;
	}

	public String getAccept_um() {
		return accept_um;
	}

	public void setAccept_um(String accept_um) {
		this.accept_um = accept_um;
	}

	public String getAccept_kl() {
		return accept_kl;
	}

	public void setAccept_kl(String accept_kl) {
		this.accept_kl = accept_kl;
	}

	public String getAccept_ad() {
		return accept_ad;
	}

	public void setAccept_ad(String accept_ad) {
		this.accept_ad = accept_ad;
	}

	public String getAccept_pb() {
		return accept_pb;
	}

	public void setAccept_pb(String accept_pb) {
		this.accept_pb = accept_pb;
	}

	public String getLimit_domains() {
		return limit_domains;
	}

	public void setLimit_domains(String limit_domains) {
		this.limit_domains = limit_domains;
	}

	public String getH_TYPE() {
		return H_TYPE;
	}

	public void setH_TYPE(String h_TYPE) {
		H_TYPE = h_TYPE;
	}

	public int getWeight_pct() {
		return weight_pct;
	}

	public void setWeight_pct(int weight_pct) {
		this.weight_pct = weight_pct;
	}
	public String getAccept_st() {
		return accept_st;
	}

	public void setAccept_st(String accept_st) {
		this.accept_st = accept_st;
	}

	public String getAccept_sp() {
		return accept_sp;
	}

	public void setAccept_sp(String accept_sp) {
		this.accept_sp = accept_sp;
	}

	public int getBid_price() {
		return bid_price;
	}

	public void setBid_price(int bid_price) {
		this.bid_price = bid_price;
	}

	public String getPrice_type() {
		return price_type;
	}

	public void setPrice_type(String price_type) {
		this.price_type = price_type;
	}

	public String getLimit_pop() {
		return limit_pop;
	}

	public void setLimit_pop(String limit_pop) {
		this.limit_pop = limit_pop;
	}

	public String getWeight_type() {
		return weight_type;
	}

	public void setWeight_type(String weight_type) {
		this.weight_type = weight_type;
	}

	public String getImgcode() {
		return imgcode;
	}

	public void setImgcode(String imgcode) {
		this.imgcode = imgcode;
	}

	public String getFrameRtb_yn() {
		return frameRtb_yn;
	}

	public void setFrameRtb_yn(String frameRtb_yn) {
		this.frameRtb_yn = frameRtb_yn;
	}

	public String getAccept_rm() {
		return accept_rm;
	}

	public void setAccept_rm(String accept_rm) {
		this.accept_rm = accept_rm;
	}

	public String getSiteurl() {
		return siteurl;
	}

	public void setSiteurl(String siteurl) {
		this.siteurl = siteurl;
	}

	public String getIcover_useyn() {
		return icover_useyn;
	}

	public void setIcover_useyn(String icover_useyn) {
		this.icover_useyn = icover_useyn;
	}

	public float getIcover_time() {
		return icover_time;
	}

	public void setIcover_time(float icover_time) {
		this.icover_time = icover_time;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public String getTwo_yn() {
		return two_yn;
	}

	public void setTwo_yn(String two_yn) {
		this.two_yn = two_yn;
	}

	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	public String getMcover_type() {
		return mcover_type;
	}

	public void setMcover_type(String mcover_type) {
		this.mcover_type = mcover_type;
	}

	public String getPsb_url() {
		return psb_url;
	}

	public void setPsb_url(String psb_url) {
		this.psb_url = psb_url;
	}
  public String getImgcodeTemp() {
    return imgcodeTemp;
  }
  public void setImgcodeTemp(String imgcodeTemp) {
    this.imgcodeTemp = imgcodeTemp;
  }
  public String getPlatformType(){
    return w_type.equals("w") ? GlobalConstants.WEB : GlobalConstants.MOBILE;
  }
	
}
