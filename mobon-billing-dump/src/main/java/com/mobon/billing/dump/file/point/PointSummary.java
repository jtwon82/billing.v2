package com.mobon.billing.dump.file.point;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobon.billing.dump.domainmodel.point.MobCampMediaStats;
import com.mobon.billing.dump.domainmodel.point.PointDataStats;
import com.mobon.billing.dump.domainmodel.point.key.PointDataStatsKey;
import com.mobon.billing.dump.file.point.data.PointData;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class PointSummary {

	
	/** 
	 * @Method Name :  PointDataListToMap
	 * @Date : 2020.10.16
	 * @Author : dhlim
	 * @Comment : 재처리 파일전체를  라인별로  Summary 한다. 
	 */	
	public Map<String, PointDataStats> PointDataListToMap(List<PointData> pointVoList) {
		
		Map<String,PointDataStats> resultData = new HashMap<String,PointDataStats>();

		for (PointData pointData : pointVoList) {
			PointDataStats pointDataStats = null;
			if (resultData.containsKey(pointData.getPointDataStatskey())) {
				pointDataStats = resultData.get(pointData.getPointDataStatskey());
				pointData.addPoint(pointDataStats.getPoint());
			} 				
			pointDataStats = PointDataStats.builder()
					.id(PointDataStatsKey.builder()
							.statsDttm(pointData.statsDttm)
							.mediaId(pointData.mediaId)
							.siteCode(pointData.siteCode)
							.build())
					.point(pointData.point)				
					.build();

			resultData.put(pointData.getPointDataStatskey(), pointDataStats);
		}
		
		log.info("#### LogData Size ####" + resultData.size());

		return	resultData;
	}
	
	/** 
	 * @Method Name :  PointDataSet
	 * @Date : 2020.10.29
	 * @Author : dhlim
	 * @Comment : 한시간전 로그 파일를  라인별로  Summary 한다. 
	 */
	public Map<String, PointDataStats> PointDataSet(Map<String, PointDataStats> resultData, PointData pointDataVo) {
		PointDataStats pointDataStats = null;
		
		if (resultData.containsKey(pointDataVo.getPointDataStatskey())) {
			pointDataStats = resultData.get(pointDataVo.getPointDataStatskey());
			pointDataStats.addPoint(pointDataVo.point);
		} 
		
		 pointDataStats = resultData.getOrDefault(pointDataVo.getPointDataStatskey(), 
				PointDataStats.builder()
				.id(PointDataStatsKey.builder()
						.statsDttm(pointDataVo.statsDttm)
						.mediaId(pointDataVo.mediaId)
						.siteCode(pointDataVo.siteCode)
						.build())
				.point(pointDataVo.point)				
				.build()
				);
				
		resultData.put(pointDataVo.getPointDataStatskey(), pointDataStats);
		
		return resultData;
	}
	
	/** 
	 * @Method Name :  TransPointDataDiffListToMap
	 * @Date : 2020.10.30
	 * @Author : dhlim
	 * @Comment : LogPointData 전체를 차이가 나는 MediaId 까지의 기준으로  List 형태를 Map 형태로 변환  한다. 
	 */	
	public Map<String, PointDataStats> TransPointDataDiffListToMap(List<PointDataStats> pointDataList) {
		Map<String, PointDataStats> resultData = new HashMap<String, PointDataStats>();
		
		for (PointDataStats pointDataStats : pointDataList) {
			String key = pointDataStats.getMediaKey();
			resultData.put(key, pointDataStats);
		}
		
		return resultData;
	}
	/** 
	 * @Method Name :  TransCampMediaDatatDiffListToMap
	 * @Date : 2020.10.30
	 * @Author : dhlim
	 * @Comment : CampData 전체를 차이가 나는 MediaId 까지의 기준으로  List 형태를 Map 형태로 변환  한다. 
	 */	
	public Map<String, MobCampMediaStats> TransCampMediaDatatDiffListToMap(List<MobCampMediaStats> campDataList) {
		Map<String, MobCampMediaStats> resultData = new HashMap<String, MobCampMediaStats>();
		
		for (MobCampMediaStats mobCampMediaStats : campDataList) {
			String key = mobCampMediaStats.getMediaKey();
			
			resultData.put(key, mobCampMediaStats);
		}
		
		return resultData;
	}


}
