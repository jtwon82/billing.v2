package com.adgather.user.inclinations.cookieval.inct.ctr;

import org.apache.commons.lang3.StringUtils;

import com.adgather.user.inclinations.cookieval.inct.InctMrf;

public class InctMrfCtr extends CommonCtr {
	public static InctMrf createInctMrf(String objKey) {
		if(StringUtils.isEmpty(objKey))		return null;
		
		InctMrf obj = new InctMrf();
		obj.setObjKey(objKey);
		obj.setAdCnt(1);
		obj.setUpdDate(getUpdDate());
		
		return obj;
	}
}
