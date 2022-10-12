package com.adgather.user.inclinations.cookieval.inct.ctr;

import org.apache.commons.lang3.StringUtils;

import com.adgather.user.inclinations.cookieval.inct.InctKcc;
import com.adgather.util.HangulCharsetDetector;

public class InctKccCtr extends CommonCtr {
	public static InctKcc createInctKcc(String cateFiveCntYn) {
		if(StringUtils.isEmpty(cateFiveCntYn))		return null;
		
		if(HangulCharsetDetector.isBrokenString(cateFiveCntYn))	return null;	// 깨진것 이면 수집 하지 않는다.
		
		InctKcc obj = new InctKcc();
		obj.setCateFiveCntYn(cateFiveCntYn);
		obj.setUpdDate(getUpdDate());
		
		return obj;
	}
}
