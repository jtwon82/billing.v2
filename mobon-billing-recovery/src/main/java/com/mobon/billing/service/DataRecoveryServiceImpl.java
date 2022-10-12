package com.mobon.billing.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobon.billing.dao.DataRecoveryDao;
import com.mobon.billing.vo.DataBaseVo;

import ch.qos.logback.classic.Logger;

@Service
public class DataRecoveryServiceImpl implements DataRecoveryService{
	

	private static final org.slf4j.Logger	logger	= LoggerFactory.getLogger(DataRecoveryServiceImpl.class);

	
	@Autowired
	private DataRecoveryDao dataRecoveryDao;
	
	
	@Override
	public Boolean groupingData(Map<String, DataBaseVo> result, String fileName) {
		int groupingSize = 1000;
		Boolean checkResult = false;
		
		ArrayList<DataBaseVo> dataList = new ArrayList<DataBaseVo>();
		
		for (int i = 0; i < result.size(); i++) {
			DataBaseVo vo = result.get(String.valueOf(i));
			dataList.add(vo);
		}
		List<List<DataBaseVo>> ret = this.split(dataList, groupingSize);
		String dbTableName = this.checkQueryName(fileName);
		
		if ("".equals(dbTableName)) {
			logger.info("#### DB Talbe Name is Empty ####");
			return checkResult;
		}
		
		try {
			logger.info("#### DB INSERT START ####");
			for (int i = 0 ; i < ret.size() ; i++) {
				List<DataBaseVo> list = ret.get(i);
				
				checkResult = dataRecoveryDao.setData(list,dbTableName);			
			}
			logger.info("#### DB INSERT END ####");
		} catch (Exception e) {
			return false;
		}
		return checkResult;
	}
	
	
	private String checkQueryName(String fileName) {
		 if ("FRME_CYCLE_LOG.json".equals(fileName)) {
			return "insertFrmeCycleLog";
		} else {
			return  "insertFrameAdverDayStats";		
		}
	}


	public static <T> List<List<T>> split(List<T> resList, int count) {
		if (resList == null || count <1)
			return null;
		List<List<T>> ret = new ArrayList<List<T>>();
		int size = resList.size();
		if (size <= count) {
			// 데이터 부족 count 지정 크기
			ret.add(resList);
		} else {
			int pre = size / count;
			int last = size % count;
			// 앞 pre 개 집합, 모든 크기 다 count 가지 요소
			for (int i = 0; i <pre; i++) {
				List<T> itemList = new ArrayList<T>();
				for (int j = 0; j <count; j++) {
					itemList.add(resList.get(i * count + j));
				}
				ret.add(itemList);
			}
			// last 진행이 처리
			if (last > 0) {
				List<T> itemList = new ArrayList<T>();
				for (int i = 0; i <last; i++) {
					itemList.add(resList.get(pre * count + i));
				}
				ret.add(itemList);
			}
		}
		return ret;
	}
}
