package com.mobon.billing.dump.service;

import java.io.File;
import java.util.Map;

import com.mobon.billing.dump.domainmodel.point.PointDataStats;
import com.mobon.billing.dump.file.point.PointSummary;

import net.sf.json.JSONArray;

public interface PointFileService {
	
	/**
	 * @Method Name : loadFile
	 * @Date : 2020. 10. 16.
	 * @Author : dhlim
	 * @Comment : 한시간 전 point파일 을 읽어서 List에 저장 
	 * @param fileName, PointSummary 
	 */
	Map<String, PointDataStats> loadFile(File file, PointSummary stats);

	/**
	 * @Method Name : makeRetryFile
	 * @Date : 2020. 10. 19.
	 * @Author : dhlim
	 * @Comment : DB insert 에러시 해당 파일을 Retry 파일로 떨궈서 처리 될수 있도로고 하는 로직 
	 * @param retryFile
	 */
	void makeRetryFile(File retryFile);
	
	/**
	 * @Method Name : makeDiffResultFile
	 * @Date : 2020. 10. 21.
	 * @Author : dhlim
	 * @Comment : 서로 다른 point Data siteCode를 파일로 떨구는 로직  
	 * @param diffSiteCodeList
	 */
	void makeDiffResultFile(JSONArray diffSiteCodeList);
	
	/**
	 * @Method Name : RetryLoadFile
	 * @Date : 2020. 10. 27.
	 * @Author : dhlim
	 * @Comment : 재처리 로직  
	 * @param files, stats
	 */
	Map<String, PointDataStats> RetryLoadFile(File[] files, PointSummary stats);
	
}
