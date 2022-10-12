package com.adgather.user.inclinations.cookieval.inct.ctr;

import org.apache.commons.lang3.StringUtils;

import com.adgather.user.inclinations.cookieval.inct.InctKc;
import com.adgather.util.HangulCharsetDetector;

public class InctKcCtr extends CommonCtr {
	public static InctKc createInctKc(String category) {
		if(StringUtils.isEmpty(category))		return null;
		
		if(HangulCharsetDetector.isBrokenString(category))	return null;	// 깨진것 이면 수집 하지 않는다.
		
		InctKc obj = new InctKc();
		obj.setCategory(category);
		obj.setCategoryCnt(1);
		obj.setUpdDate(getUpdDate());
		
		return obj;
	}
}
