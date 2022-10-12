package com.mobon.billing.model.v15;

import java.util.Map;

import com.adgather.constants.G;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.model.ActionData;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ActionLogData extends ActionData{
	
	public int vcnt=0;
	public int vcnt2=0;
	public int ccnt=0;

	// 전환시 사용
	public boolean bHandlingStatsMobon = true;
	public boolean bHandlingStatsPointMobon = true;



	public static ActionLogData fromHashMap(Map from) {
		ActionLogData result = new ActionLogData();
		result.yyyymmdd = StringUtils.trimToNull2(from.get("yyyymmdd"));
		result.keyIp = StringUtils.trimToNull2(from.get("keyIp"),"");
		result.setAu_id( StringUtils.trimToNull2(from.get("au_id"),"") );
		result.pCode = StringUtils.trimToNull2(from.get("pCode"),"");
		result.shoplogNo = Long.parseLong(StringUtils.trimToNull2(from.get("shoplogNo"),"0"));
		result.siteCode = StringUtils.trimToNull2(from.get("siteCode"));
		result.advertiserId = StringUtils.trimToNull2(from.get("advertiserId"));
		result.scriptNo = StringUtils.trimToNull2(from.get("scriptNo"));
		result.scriptUserId = StringUtils.trimToNull2(from.get("scriptUserId"));
		result.vcnt = Integer.parseInt(StringUtils.trimToNull2(from.get("vcnt"),"0"));
		result.vcnt2 = Integer.parseInt(StringUtils.trimToNull2(from.get("vcnt2"),"0"));
		result.ccnt = Integer.parseInt(StringUtils.trimToNull2(from.get("ccnt"),"0"));
		result.point = Float.parseFloat(StringUtils.trimToNull2(from.get("point"),"0"));
		result.actGubun = StringUtils.trimToNull2(from.get("actGubun"));
		result.adGubun = StringUtils.trimToNull2(from.get("adGubun"));
		result.setRecomTpCode(StringUtils.trimToNull2(from.get("recomTpCode"),"01"));
		result.setSubadgubun(StringUtils.trimToNull2(from.get("subadgubun"),""));
		result.product = StringUtils.trimToNull2(from.get("product"));
		result.mcgb = StringUtils.trimToNull2(from.get("mcgb"));
		result.sendDate = StringUtils.trimToNull2(from.get("sendDate"));
		result.bHandlingStatsMobon = Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsMobon")));
		result.bHandlingStatsPointMobon = Boolean.parseBoolean(StringUtils.trimToNull2(from.get("bHandlingStatsPointMobon")));
		result.kno = Long.parseLong(StringUtils.trimToNull2(from.get("kno")));
		result.setSvcTpCode(StringUtils.trimToNull2(from.get("svcTpCode")));
		result.setChrgTpCode(StringUtils.trimToNull2(from.get("chrgTpCode"),"01"));
		result.setAdvrtsTpCode(StringUtils.trimToNull2(from.get("advrtsTpCode"),"01"));
		result.setIntgLogCnt(Integer.parseInt(StringUtils.trimToNull2(from.get("intgLogCnt"),"1")));

		result.setIntgYn(StringUtils.trimToNull2(from.get("intgYn"),"N"));
		result.setCrossbrYn(StringUtils.trimToNull2(from.get("crossbrYn"),"N"));
		result.setKwrdSeq(StringUtils.trimToNull2(from.get("kwrdSeq"),"0"));
		result.setAdcSeq(StringUtils.trimToNull2(from.get("adcSeq"),"0"));
		result.setFromApp(StringUtils.trimToNull2(from.get("fromApp"),"N"));
		result.settTime(Integer.parseInt(StringUtils.trimToNull2(from.get("tTime"),"0")));
		result.setCtgrNo(StringUtils.trimToNull2(from.get("ctgrNo"),"0"));
		result.setCtgrYn(StringUtils.trimToNull2(from.get("ctgrYn"),"N"));
		result.setErgabt(StringUtils.trimToNull2(from.get("ergabt"),""));
		result.setErgdetail(StringUtils.trimToNull2(from.get("ergdetail"),""));
		result.setAiCateNo( Integer.parseInt(StringUtils.trimToNull2(from.get("aiCateNo"),"0")) );
		result.setNoExposureYN(Boolean.parseBoolean(StringUtils.trimToNull2(from.get("noExposureYN"),"false")));
		result.setRecomAlgoCode(StringUtils.trimToNull2(from.get("recomAlgoCode"), ""));
		result.setAbTests(StringUtils.trimToNull2(StringUtils.trimToNull2(from.get("abTests")), ""));
		result.setAbTestTy(StringUtils.trimToNull2(StringUtils.trimToNull2(from.get("abTestTy"), "")));
		result.setMobAdGrp((JSONArray) from.get("mobAdGrpData"));
		result.setAdvrtsStleTpCode(StringUtils.trimToNull2(from.get("advrtsStleTpCode"),"99"));
		result.setPrdtTpCode(StringUtils.trimToNull2(from.get("prdtTpCode"), G.convertPRDT_CODE((String) from.get("product"))));
		result.setHh(StringUtils.trimToNull2(from.get("hh"),"99"));

		return result;
	}
	public String getAdGubunCode() {
		try {
			return G.convertTP_CODE(this.getAdGubun());
		}catch(Exception e) {
			return "";
		}
	}
	public String getSubAdGubunCode() {
		try {
			return G.convertSUBADGUBUN_CODE(this.getSubadgubun());
		}catch(Exception e) {
			return "";
		}
	}

	public int getVcnt() {
		return vcnt;
	}

	public void setVcnt(int vcnt) {
		this.vcnt = vcnt;
	}

	public int getVcnt2() {
		return vcnt2;
	}

	public void setVcnt2(int vcnt2) {
		this.vcnt2 = vcnt2;
	}

	public int getCcnt() {
		return ccnt;
	}

	public void setCcnt(int ccnt) {
		this.ccnt = ccnt;
	}

	@Override
	public String generateKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sumGethering(ActionData from) {
		// TODO Auto-generated method stub
		return null;
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

}
