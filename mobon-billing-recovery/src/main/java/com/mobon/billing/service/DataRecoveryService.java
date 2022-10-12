package com.mobon.billing.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.mobon.billing.vo.DataBaseVo;


public interface DataRecoveryService {

	/**
	 * @MethodName : groupingData
	 * @Date : 2021. 2. 10.
	 * @작성자 :  dhlim
	 * @param result , fileName
	 * @return boolean
	 * @기능  : 해당 데이터의 크기를 일정하게 나눠서 그룹핑한다.
	 */
	public Boolean groupingData(Map<String, DataBaseVo> result, String fileName);

}
