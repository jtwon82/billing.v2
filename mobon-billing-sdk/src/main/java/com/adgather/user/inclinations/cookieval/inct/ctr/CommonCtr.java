package com.adgather.user.inclinations.cookieval.inct.ctr;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class CommonCtr {
	//신규 수집날짜 형식
	private static final DateFormat FD_UPDDATE = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final DateFormat FD_UPDDAY = new SimpleDateFormat("yyyyMMdd");
	public static String getUpdDate() {
		return FD_UPDDATE.format(new Date());
	}
	
	// 문자열날짜를 Date형으로 변환 (이용 할 때 null 처리 필요)
	public static Date toDate(String strDate) {
		return toDate(strDate, null);
	}

	// 문자열날짜를 Date형으로 변환 (default date 이용)
	public static Date toDate(String strDate, Date defaultDate) {
		if(StringUtils.isEmpty(strDate))		return defaultDate;
		Date date = null;
		try {
			date = FD_UPDDATE.parse(strDate);
		} catch(Exception e) {}
		return date != null ? date : defaultDate;
	}
}
