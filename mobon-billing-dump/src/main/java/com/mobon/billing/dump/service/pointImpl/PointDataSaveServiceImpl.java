package com.mobon.billing.dump.service.pointImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobon.billing.dump.domainmodel.point.PointDataStats;
import com.mobon.billing.dump.repository.PointStatsRepository;
import com.mobon.billing.dump.service.PointSaveService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PointDataSaveServiceImpl implements PointSaveService{
	
	@Autowired
	PointStatsRepository pointStatsRepository;
	
	@Override
	public void SaveRealTimePointData(Map<String, PointDataStats> resultData) {
		log.info("#### PointData DB insert ####");

		log.info("#### PointData Log Original ####"+ resultData.size());
				
		List <PointDataStats> pointList = new ArrayList<PointDataStats>();
		resultData.forEach((pointDataKey, pointData) ->{
			PointDataStats originData = resultData.get(pointDataKey);
			Optional<PointDataStats> findDbData = pointStatsRepository.findById(originData.getId());
			if (findDbData.isPresent()) {
				BigDecimal dbPoint = findDbData.get().getPoint();
				pointData.addPoint(dbPoint);
			}
			
			pointList.add(pointData);
		});
		pointStatsRepository.saveAll(pointList);
		
		log.info("#### PointData DB insert Complete ####");
		
	}

	@Override
	public void RetrySavePointData(Map<String, PointDataStats> resultData) {
		log.info("#### Retry PointData DB insert ####");

		log.info("#### Retry PointData Log Original ####"+ resultData.size());
		List <PointDataStats> pointList = new ArrayList<PointDataStats>();
		resultData.forEach((pointDataKey, pointData) ->{
			PointDataStats originData = resultData.get(pointDataKey);
			Optional<PointDataStats> findDbData = pointStatsRepository.findById(originData.getId());
			if (findDbData.isPresent()) {
				BigDecimal dbPoint = findDbData.get().getPoint();
				pointData.addPoint(dbPoint);
			}
			pointList.add(pointData);
		});
		pointStatsRepository.saveAll(pointList);
		
		log.info("#### Retry PointData DB insert Complete ####");		
	}

}
