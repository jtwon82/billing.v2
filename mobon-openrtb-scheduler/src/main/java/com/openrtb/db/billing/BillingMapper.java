package com.openrtb.db.billing;

import java.util.List;
import java.util.Map;

public interface BillingMapper {
	public String getBillingTest() throws Exception;

	// 시간별 비교
	public List<Map<String, Object>> getBillingTimeDataList(Map<String, String> param) throws Exception;

	public List<Map<String, Object>> getOepnRtbTimeDataList(Map<String, String> param) throws Exception;

	// 일자별 비교
	public Map<String, Object> getBillingDayDataList(Map<String, String> param) throws Exception;

	public Map<String, Object> getOepnRtbDayDataList(Map<String, String> param) throws Exception;

	// 데이터 보정
	// 시간대별
	public int del_MOB_CAMP_HH_STATS(Map<String, String> param) throws Exception;

	public int insert_MOB_CAMP_HH_STATS(Map<String, String> param) throws Exception;

	public int del_MOB_ADVER_HH_STATS(Map<String, String> param) throws Exception;

	public int insert_MOB_ADVER_HH_STATS(Map<String, String> param) throws Exception;

	public int del_MOB_MEDIA_SCRIPT_HH_STATS(Map<String, String> param) throws Exception;

	public int insert_MOB_MEDIA_SCRIPT_HH_STATS(Map<String, String> param) throws Exception;

	public int del_MOB_MEDIA_HH_STATS(Map<String, String> param) throws Exception;

	public int insert_MOB_MEDIA_HH_STATS(Map<String, String> param) throws Exception;

	public int del_MOB_COM_HH_STATS_INFO(Map<String, String> param) throws Exception;

	public int insert_MOB_COM_HH_STATS_INFO(Map<String, String> param) throws Exception;

	// 일자별
	public int del_MOB_CAMP_MEDIA_STATS(Map<String, String> param) throws Exception;

	public int insert_MOB_CAMP_MEDIA_STATS(Map<String, String> param) throws Exception;

	public int del_MOB_CAMP_STATS(Map<String, String> param) throws Exception;

	public int insert_MOB_CAMP_STATS(Map<String, String> param) throws Exception;

	public int del_MOB_ADVER_MEDIA_STATS(Map<String, String> param) throws Exception;

	public int insert_MOB_ADVER_MEDIA_STATS(Map<String, String> param) throws Exception;

	public int del_MOB_ADVER_STATS(Map<String, String> param) throws Exception;

	public int insert_MOB_ADVER_STATS(Map<String, String> param) throws Exception;

	public int del_MOB_MEDIA_SCRIPT_STATS(Map<String, String> param) throws Exception;

	public int insert_MOB_MEDIA_SCRIPT_STATS(Map<String, String> param) throws Exception;

	public int del_MOB_MEDIA_STATS(Map<String, String> param) throws Exception;

	public int insert_MOB_MEDIA_STATS(Map<String, String> param) throws Exception;

	public int del_MOB_COM_STATS_INFO(Map<String, String> param) throws Exception;

	public int insert_MOB_COM_STATS_INFO(Map<String, String> param) throws Exception;

	public int del_MOB_MEDIA_SCRIPT_CHRG_STATS(Map<String, String> param) throws Exception;

	public int insert_MOB_MEDIA_SCRIPT_CHRG_STATS(Map<String, String> param) throws Exception;

	public int del_MOB_MEDIA_CHRG_STATS(Map<String, String> param) throws Exception;

	public int insert_MOB_MEDIA_CHRG_STATS(Map<String, String> param) throws Exception;

}
