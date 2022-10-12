package com.adgather.user.inclinations.cookieval.inct.ctr;

import org.apache.commons.lang3.StringUtils;

import com.adgather.user.inclinations.cookieval.inct.InctAdc;
import com.adgather.util.HangulCharsetDetector;

public class InctAdcCtr extends CommonCtr {
	
	public static InctAdc createInctAdc(String adcSeq) {
		
		if(StringUtils.isEmpty(adcSeq))		return null;
		
		if(HangulCharsetDetector.isBrokenString(adcSeq))	return null;	// 깨진것 이면 수집 하지 않는다.
		
		InctAdc obj = new InctAdc();
		obj.setAdcSeq(adcSeq);
		obj.setUpdDate(getUpdDate());
		
		return obj;
	}
}