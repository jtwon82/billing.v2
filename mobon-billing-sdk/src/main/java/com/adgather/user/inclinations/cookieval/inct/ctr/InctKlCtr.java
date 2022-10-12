package com.adgather.user.inclinations.cookieval.inct.ctr;

import org.apache.commons.lang3.StringUtils;

import com.adgather.user.inclinations.cookieval.inct.InctKl;
import com.adgather.util.HangulCharsetDetector;

public class InctKlCtr extends CommonCtr {
	public static InctKl createInctKl(String keyword) {
		if(StringUtils.isEmpty(keyword))		return null;
		
		if(HangulCharsetDetector.isBrokenString(keyword))	return null;	// 깨진것 이면 수집 하지 않는다.
		
		InctKl obj = new InctKl();
		obj.setKeyword(keyword);
		obj.setUpdDate(getUpdDate());
		
		return obj;
	}
}
