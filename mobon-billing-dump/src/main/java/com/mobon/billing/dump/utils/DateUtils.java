package com.mobon.billing.dump.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.saro.commons.DateFormat;

public abstract class DateUtils {
    
    private DateUtils() {
        throw new AssertionError();
    }


    public static String getAnHourAgo() {
		return getHour(-1);
	}
    public static String getAnHourAgo(int val) {
		return getHour(val);
	}
    
    private static String getHour(int hourCnt) {
		
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd_HH");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, hourCnt);
		return sdfDate.format(calendar.getTime());
		
	}

	public static Date getStrToDate(String strtype, String date) {
		java.text.DateFormat df = new java.text.SimpleDateFormat(strtype);	///"yyyy-MM-dd"
		try {
			return df.parse(date);
		} catch (ParseException e) {
			return new Date();
		}
	}
	public static String getDate(String strtype, Date date){
		java.text.DateFormat df = new java.text.SimpleDateFormat(strtype);	///"yyyy-MM-dd"
		return df.format(date).toString();
	}
	
    /**
     *  특정일 이전 날짜를 구하는 매소드
     * @param dateAgo 이전일
     * @return long 결과 일
     */
    public static long getDate(int dateAgo){
        return DateFormat.now().addDates(dateAgo).getTimeInMillis();
    }
}
