package com.mobon.billing.dump.service.pointImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobon.billing.dump.domainmodel.point.MobCampMediaStandardStats;
import com.mobon.billing.dump.domainmodel.point.MobCampMediaStats;
import com.mobon.billing.dump.domainmodel.point.PointDataStats;
import com.mobon.billing.dump.repository.MobCampMediaStandardStatsRepository;
import com.mobon.billing.dump.repository.MobCampStatsRepository;
import com.mobon.billing.dump.repository.PointStatsRepository;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
@Service
@Slf4j
public class PointDataSelectServiceImpl {
	
	@Autowired
	PointStatsRepository pointStatsRepository;

	
	@Autowired
	MobCampStatsRepository mobCampStatsRepository;
	
	@Autowired
	MobCampMediaStandardStatsRepository mobCampMediaStandardStatsRepository;
	
	
	public List<PointDataStats> selectPointLogDataList(int yesterDayToInt) {
		List<PointDataStats> resultData = new ArrayList<PointDataStats>();
		
		resultData = pointStatsRepository.selectList(yesterDayToInt);

		return resultData;
	}

	public List<MobCampMediaStats> selectCampStatsDataList(int yesterDayToInt) {
		
		List<MobCampMediaStats> resultData = new ArrayList<MobCampMediaStats>();
		
		resultData = mobCampStatsRepository.selectList(yesterDayToInt);
		
		return resultData;
	}

	public List<MobCampMediaStandardStats> getDiffData(JSONObject obj) {
		List<MobCampMediaStandardStats> resultData =  new ArrayList<MobCampMediaStandardStats>();
		int statsDttm = (int) obj.get("STATS_DTTM");
		String siteCode = (String) obj.get("SITE_CODE");
		String mediaId = (String) obj.get("MEDIA_ID");
		BigDecimal minusDiffPoint = (BigDecimal) obj.get("DIFF_POINT");
		BigDecimal diffPoint = minusDiffPoint.multiply(new BigDecimal(-1));
		
		resultData = mobCampMediaStandardStatsRepository.selectDiffData(statsDttm, siteCode, mediaId, diffPoint );
		
		return resultData;
	}

	public List<MobCampMediaStandardStats> notEqDiffData(JSONObject obj) {
		List<MobCampMediaStandardStats> resultData =  new ArrayList<MobCampMediaStandardStats>();
		int statsDttm = (int) obj.get("STATS_DTTM");
		String siteCode = (String) obj.get("SITE_CODE");
		String mediaId = (String) obj.get("MEDIA_ID");	
		
		resultData = mobCampMediaStandardStatsRepository.selectNotEqDiffData(statsDttm, siteCode, mediaId);
		
		return resultData;
	}

	public boolean getAdverIdRegDate(String statsDttm, String adverId) {
		List<MobCampMediaStats> resultData = new ArrayList<MobCampMediaStats>();
		resultData = mobCampStatsRepository.selectAdverIdData(statsDttm , adverId);
		if (resultData.size() == 0) {
			return false;
		}
		return true;
	}

	

}
