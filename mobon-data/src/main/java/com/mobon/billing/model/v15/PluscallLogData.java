package com.mobon.billing.model.v15;

import java.io.Serializable;
import java.util.Map;

import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.model.ClickViewData;

/**
 * PluscallLogData
 * 플러스콜 유효콜 DTO
 * 
 * @author  : sjpark3
 * @since   : 2022-01-04
 */
public class PluscallLogData extends ClickViewData implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int avalCallTime = 0;				// 유효콜 콜타임
	private int avalCallCnt = 0;				// 유효콜 카운트
	private int dbCnvrsCnt = 0;					// DB 전환수
	
	/**
	 * fromHashMap
	 * JSON 데이터 매핑 메소드
	 * 
	 * @author  : sjpark3
	 * @since   : 2022-01-04
	 */
	public static PluscallLogData fromHashMap(Map from) {
		PluscallLogData result = new PluscallLogData();
		
		result.yyyymmdd = StringUtils.trimToNull2(from.get("yyyymmdd"));
		result.hh = StringUtils.trimToNull2(from.get("hh"),DateUtils.getDate("HH"));
		result.platform = StringUtils.trimToNull2(from.get("platform"));
		result.product = StringUtils.trimToNull2(from.get("product"));
		result.adGubun = StringUtils.trimToNull2(from.get("adGubun"));
		result.siteCode = StringUtils.trimToNull2(from.get("siteCode"));
		result.scriptNo = Integer.parseInt(StringUtils.trimToNull2(from.get("scriptNo"),"0"));
		result.interlock = StringUtils.trimToNull2(from.get("interlock"));
		result.advertiserId = StringUtils.trimToNull2(from.get("advertiserId"));
		result.scriptUserId = StringUtils.trimToNull2(from.get("scriptUserId"));
		result.viewCnt = Integer.parseInt(StringUtils.trimToNull2(from.get("viewCnt"),"0"));
		result.viewCnt3 = Integer.parseInt(StringUtils.trimToNull2(from.get("viewCnt3"),"0"));
		result.clickCnt = Integer.parseInt(StringUtils.trimToNull2(from.get("clickCnt"),"0"));
		result.point = Float.parseFloat(StringUtils.trimToNull2(from.get("point"),"0"));
		result.avalCallTime = Integer.parseInt(StringUtils.trimToNull2(from.get("avalCallTime"),"0"));
		result.avalCallCnt = Integer.parseInt(StringUtils.trimToNull2(from.get("avalCallCnt"),"0"));
		result.dbCnvrsCnt = Integer.parseInt(StringUtils.trimToNull2(from.get("dbCnvrsCnt"),"0"));
		
		return result;
	}
	
	public int getAvalCallTime() {
		return avalCallTime;
	}

	public void setAvalCallTime(int avalCallTime) {
		this.avalCallTime = avalCallTime;
	}

	public int getAvalCallCnt() {
		return avalCallCnt;
	}

	public void setAvalCallCnt(int avalCallCnt) {
		this.avalCallCnt = avalCallCnt;
	}

	public int getDbCnvrsCnt() {
		return dbCnvrsCnt;
	}

	public void setDbCnvrsCnt(int dbCnvrsCnt) {
		this.dbCnvrsCnt = dbCnvrsCnt;
	}

	@Override
	public String generateKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sumGethering(Object from) {
		// TODO Auto-generated method stub
		
	}

}
