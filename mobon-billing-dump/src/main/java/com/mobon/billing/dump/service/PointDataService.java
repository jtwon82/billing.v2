package com.mobon.billing.dump.service;

import java.util.List;

import com.mobon.billing.dump.domainmodel.point.MobCampMediaStats;
import com.mobon.billing.dump.domainmodel.point.PointDataStats;

public interface PointDataService {
	
	/**
	 * @Method Name : resultDiffPointData
	 * @Date : 2020. 10. 30
	 * @Author : dhlim
	 * @Comment : 최종적으로 나오는 누락 포인데이터 
	 * @param pointDataList, campDataList, diffSiteCodeList
	 */	
	void resultDiffPointData(List<PointDataStats> pointDataList, List<MobCampMediaStats> campDataList);

}
