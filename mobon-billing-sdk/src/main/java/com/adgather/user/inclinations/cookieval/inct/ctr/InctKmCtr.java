package com.adgather.user.inclinations.cookieval.inct.ctr;

import org.apache.commons.lang3.StringUtils;

import com.adgather.user.inclinations.cookieval.inct.InctKm;
import com.adgather.util.HangulCharsetDetector;

public class InctKmCtr extends CommonCtr {
	public static InctKm createInctKm(String keyword) {
		if(StringUtils.isEmpty(keyword))		return null;
		
		if(HangulCharsetDetector.isBrokenString(keyword))	return null;	// 깨진것 이면 수집 하지 않는다.
		
		InctKm obj = new InctKm();
		obj.setKeyword(keyword);
		obj.setKeywordCnt(1);
		obj.setUpdDate(getUpdDate());
		
		return obj;
	}
}
