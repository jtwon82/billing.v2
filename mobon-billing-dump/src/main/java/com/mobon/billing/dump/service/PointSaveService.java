package com.mobon.billing.dump.service;

import java.util.Map;

import com.mobon.billing.dump.domainmodel.point.PointDataStats;

public interface PointSaveService {
	/**
	 * @Method Name : SaveRealTimePointData
	 * @Date : 2020. 10. 29.
	 * @Author : dhlim
	 * @Comment : 한시간 전 point Data 를 DB에 저장 
	 * @param resultData 
	 */
	void SaveRealTimePointData(Map<String, PointDataStats> resultData);

	/**
	 * @Method Name : RetrySavePointData
	 * @Date : 2020. 10. 29.
	 * @Author : dhlim
	 * @Comment : 재처리 point Data 를 DB에 저장 
	 * @param resultData 
	 */	
	void RetrySavePointData(Map<String, PointDataStats> resultData);
	
}
