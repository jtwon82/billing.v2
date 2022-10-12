package com.mobon.billing.dump.service;

import java.util.Map;
import java.util.concurrent.Future;

public interface DumpSaveService {
	
	/**
	 * @Method Name : SaveDumpData
	 * @Date : 2020. 08. 19.
	 * @Author : dkchoi
	 * @Comment : Summary된 데이터를 대상 테이블에 저장하는 서비스.
	 * @param totResultData
	 */
	
	public void SaveDumpData(Map<String, Object> totResultData);
	
	
	/**
	 * @Method Name : SaveDumpData
	 * @Date : 2020. 08. 19.
	 * @Author : dkchoi
	 * @Comment : Summary된 데이터를 대상 테이블에 저장하는 서비스. (병렬처리)
	 * @param totResultData, DataKey
	 */
	
	public Future<Object> SaveDumpData(Map<String, Object> totResultData , String DataKey);

}
