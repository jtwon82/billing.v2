package com.adgather.util.old;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DateUtils{
	public static final String YYYYMMDD = "yyyyMMdd";
	
	public static String getDate(String strtype){
		java.util.Date date = new java.util.Date();
		java.text.DateFormat df = new java.text.SimpleDateFormat(strtype);	///"yyyy-MM-dd"
		return df.format(date).toString();
	}
	public static String getDate(String strtype, Date date){
		java.text.DateFormat df = new java.text.SimpleDateFormat(strtype);	///"yyyy-MM-dd"
		return df.format(date).toString();
	}
	public static Date getDate(String strtype, String date) throws ParseException{
		java.text.DateFormat df = new java.text.SimpleDateFormat(strtype);	///"yyyy-MM-dd"
		return df.parse(date);
	}
	public static String getDate2(String strtype, String date) {
		java.text.DateFormat df = new java.text.SimpleDateFormat(strtype);	///"yyyy-MM-dd"
		try {
			return df.parse(date).toString();
		} catch (ParseException e) {
			return DateUtils.getDate("yyyyMMdd");
		}
	}
	public static String getDate(String strtype, int addDate){

		Date dt = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt); 
		c.add(Calendar.DATE, addDate);
		dt = c.getTime();
		
		java.text.DateFormat df = new java.text.SimpleDateFormat(strtype);	///"yyyy-MM-dd"
		return df.format(dt).toString();
	}
	public static Date getStrToDate(String strtype, String date) {
		java.text.DateFormat df = new java.text.SimpleDateFormat(strtype);	///"yyyy-MM-dd"
		try {
			return df.parse(date);
		} catch (ParseException e) {
			return new Date();
		}
	}
//	public static void main(String[] args) {
//	}

	/**
	 * 분석필요!!
	 * @param usemoney
	 * @param usedmoney
	 * @param ad_rhour
	 * @return
	 * 0:개제중지, 1:개제중
	 */
	public static int getAdTimeZone(int usemoney, int usedmoney, String ad_rhour){
		if( StringUtils.isEmpty(ad_rhour) ){
			return 0;
		}
		int hour= Integer.parseInt(DateUtils.getDate("HH"));hour= (hour==0?24:hour);
		int ad_rhoura= ad_rhour.indexOf("a")>-1?6:0;
		int ad_rhourb= ad_rhour.indexOf("b")>-1?3:0;
		int ad_rhourc= ad_rhour.indexOf("c")>-1?3:0;
		int ad_rhourd= ad_rhour.indexOf("d")>-1?3:0;
		int ad_rhoure= ad_rhour.indexOf("e")>-1?3:0;
		int ad_rhourf= ad_rhour.indexOf("f")>-1?3:0;
		int ad_rhourg= ad_rhour.indexOf("g")>-1?3:0;

		/**
		 * 현재 시간에 라이브 중인지 체크
		 * 1 : 라이브
		 * 0 : 중지
		 */
		int adlive = ( hour >= 0 && hour <= 6 && ad_rhoura > 0 ? 1 : 0 )	// 1시부터 6시59분 까지, a값이 있으면 = 1
				+ ( hour >= 7 && hour <= 9 && ad_rhourb > 0 ? 1 : 0 )		// 7시부터 9시59분 까지, b값이 있으면 = 1
				+ ( hour >= 10 && hour <= 12 && ad_rhourc > 0 ? 1 : 0 )	// 10시부터 12시59분 까지, c값이 있으면 = 1
				+ ( hour >= 13 && hour <= 15 && ad_rhourd > 0 ? 1 : 0 )	// 13시부터 15시59분 까지, d값이 있으면 = 1
				+ ( hour >= 16 && hour <= 18 && ad_rhoure > 0 ? 1 : 0 )	// 16시부터 18시59분 까지, e값이 있으면 = 1
				+ ( hour >= 19 && hour <= 21 && ad_rhourf > 0 ? 1 : 0 )		// 19시부터 21시59분 까지, f값이 있으면 = 1
				+ ( hour >= 22 && hour <= 24 && ad_rhourg > 0 ? 1 : 0 );	// 10시부터 24시59분 까지, g값이 있으면 = 1

		 int point_hour=0;
		 try{
			 // 하루 총 사용금액에서 / 전체 개제 시간을 나눈다.
			 // 시간별 사용가능 금액 구하기 하루 총 사용 가능 금액이 13000 / 게재 시간이 a,b,c,d,e,f,g이면 13000/24 = 541이다.
			 // logger.debug("point_hour = "+usemoney + " / " + (ad_rhoura + ad_rhourb + ad_rhourc + ad_rhourd + ad_rhoure + ad_rhourf + ad_rhourg  ));
			 point_hour = (usemoney / (ad_rhoura + ad_rhourb + ad_rhourc + ad_rhourd + ad_rhoure + ad_rhourf + ad_rhourg  ) );
		 }catch(Exception e){
			 return 0;
		 }

		// 지나온 시간 구함. 현제 시간이 11시면 0~9 총 6+3+3 = 12 이다.
		 int during_hour = ( hour > 0 && ad_rhoura > 0 ? 6 : 0 )
			 + ( hour > 6 && ad_rhourb > 0 ? 3 : 0 )
			 + ( hour > 9 && ad_rhourc > 0 ? 3 : 0 )
			 + ( hour > 12 && ad_rhourd > 0 ? 3 : 0 )
			 + ( hour > 15 && ad_rhoure > 0 ? 3 : 0 )
			 + ( hour > 18 && ad_rhourf > 0 ? 3 : 0 )
			 + ( hour > 21 && ad_rhourg > 0 ? 3 : 0 );

		 int return_value=0;
		 try{
			 // usemoney == 0 이면 무제한, 시간별 사용금액 * 지나온시간에서 현제 사용금액보다 높으면 개제 중지가 된다.
			 return_value= (usemoney==0 ? (adlive>0?1:0) : ( adlive > 0 && usedmoney < ( point_hour * during_hour ) ? 1 : 0 ) );
		 }catch(Exception e){
			 return 0;
		 }
		 return return_value;
	}

	public String getTime(){
		Calendar now=Calendar.getInstance();
		String year=Integer.toString(now.get(Calendar.YEAR)),
		   month=((now.get(Calendar.MONTH)+1)<10)  ? "0"+(now.get(Calendar.MONTH)+1) : ""+(now.get(Calendar.MONTH)+1),
		   date=(now.get(Calendar.DATE)<10) ? "0"+now.get(Calendar.DATE) : ""+now.get(Calendar.DATE),
		   hourd=(now.get(Calendar.HOUR_OF_DAY)<10) ? "0"+now.get(Calendar.HOUR_OF_DAY) : ""+now.get(Calendar.HOUR_OF_DAY),
		   minute=(now.get(Calendar.MINUTE)<10) ? "0"+now.get(Calendar.MINUTE) : ""+now.get(Calendar.MINUTE),
		   second=(now.get(Calendar.SECOND)<10) ? "0"+now.get(Calendar.SECOND) : ""+now.get(Calendar.SECOND),
		   millisecond = "";
		if( (now.get(Calendar.MILLISECOND )+"").length() == 3){
			millisecond = ""+(now.get(Calendar.MILLISECOND ));
		}else if( (now.get(Calendar.MILLISECOND )+"").length() == 2){
			millisecond = "0"+(now.get(Calendar.MILLISECOND ));
		}else if( (now.get(Calendar.MILLISECOND )+"").length() == 1){
			millisecond = "00"+(now.get(Calendar.MILLISECOND ));
		}
		return  year+"-"+month+"-"+date+" "+hourd+":"+minute+":"+second+"."+millisecond;
	}

	public String getSQLTime(long time){
		Calendar now=Calendar.getInstance();
		now.setTimeInMillis(time);
		String year=Integer.toString(now.get(Calendar.YEAR)),
		   month=((now.get(Calendar.MONTH)+1)<10)  ? "0"+(now.get(Calendar.MONTH)+1) : ""+(now.get(Calendar.MONTH)+1),
		   date=(now.get(Calendar.DATE)<10) ? "0"+now.get(Calendar.DATE) : ""+now.get(Calendar.DATE),
		   hourd=(now.get(Calendar.HOUR_OF_DAY)<10) ? "0"+now.get(Calendar.HOUR_OF_DAY) : ""+now.get(Calendar.HOUR_OF_DAY),
		   minute=(now.get(Calendar.MINUTE)<10) ? "0"+now.get(Calendar.MINUTE) : ""+now.get(Calendar.MINUTE),
		   second=(now.get(Calendar.SECOND)<10) ? "0"+now.get(Calendar.SECOND) : ""+now.get(Calendar.SECOND),
		   millisecond = "";
		if( (now.get(Calendar.MILLISECOND )+"").length() == 3){
			millisecond = ""+(now.get(Calendar.MILLISECOND ));
		}else if( (now.get(Calendar.MILLISECOND )+"").length() == 2){
			millisecond = "0"+(now.get(Calendar.MILLISECOND ));
		}else if( (now.get(Calendar.MILLISECOND )+"").length() == 1){
			millisecond = "00"+(now.get(Calendar.MILLISECOND ));
		}
		return  year+"-"+month+"-"+date+" "+hourd+":"+minute+":"+second+"."+millisecond;
	}

	public String getYYYYMMDDHH(long time,String sep){
		Calendar now=Calendar.getInstance();
		now.setTimeInMillis(time);
		String year=Integer.toString(now.get(Calendar.YEAR)),
		   month=((now.get(Calendar.MONTH)+1)<10)  ? "0"+(now.get(Calendar.MONTH)+1) : ""+(now.get(Calendar.MONTH)+1),
		   date=(now.get(Calendar.DATE)<10) ? "0"+now.get(Calendar.DATE) : ""+now.get(Calendar.DATE),
		   hourd=(now.get(Calendar.HOUR_OF_DAY)<10) ? "0"+now.get(Calendar.HOUR_OF_DAY) : ""+now.get(Calendar.HOUR_OF_DAY);
		return  year+sep+month+sep+date+sep+hourd;
	}

	/**
	 * SQL 요일
	 * @return
	 */
	public static String getWSql(){
		Calendar cal=Calendar.getInstance();
		int w=cal.get(Calendar.DAY_OF_WEEK);
		String wSql="";
		switch(w){
		case 1:
			wSql=" and sun = 'y' ";
			break;
		case 2:
			wSql=" and mon = 'y' ";
			break;
		case 3:
			wSql=" and tue = 'y' ";
			break;
		case 4:
			wSql=" and wed = 'y' ";
			break;
		case 5:
			wSql=" and thu = 'y' ";
			break;
		case 6:
			wSql=" and fri = 'y' ";
			break;
		case 7:
			wSql=" and sat = 'y' ";
			break;
		}
		return wSql;
	}

	/**
	 * 현재날짜 포멧
	 * @param format
	 * @return
	 */
	public static String getFormatDate(String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		java.util.Date date=new java.util.Date();
		String result = sdf.format(date);
		return result;
	}

	/**
	 * 원하는날짜가져오기
	 * yyyyMMdd
	 * @param format
	 * @return
	 */
	public static String getFormatDate(String format,int day){
		String result = "";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat fm = new SimpleDateFormat(format);
	    cal.add(Calendar.DATE, day);
	    result = fm.format(cal.getTime());
	    return result;
	}

	/**
	 * 5분 간격으로 시간 조절
	 */
	public static String getMinuteDistance(int minute){
		String result = "";
		if (minute >= 55) {
			result = "55";
		} else if (minute >= 50) {
			result = "50";
		} else if (minute >= 45) {
			result = "45";
		} else if (minute >= 40) {
			result = "40";
		} else if (minute >= 35) {
			result = "35";
		} else if (minute >= 30) {
			result = "30";
		} else if (minute >= 25) {
			result = "25";
		} else if (minute >= 20) {
			result = "20";
		} else if (minute >= 15) {
			result = "15";
		} else if (minute >= 10) {
			result = "10";
		} else if (minute >= 5) {
			result = "05";
		} else {
			result = "00";
		}
	    return result;
	}

//	public static String getToday(String format) {
//
//		if(format == null || format.length() == 0)
//			return "";
//
//		DateTime dt = new DateTime();
//		format = format.replace("Y", "y");
//		format = format.replace("D", "d");
//		DateTimeFormatter fmt = DateTimeFormat.forPattern(format);
//
//		return fmt.print(dt);
//
//	}

	/**
	 * 하루 전 날을 구한다.
	 */
	public static Date getPreviousDate() {
    	return getPreviousDate(1);
    }

	/**
	 * @param day
	 * @return
	 */
	public static Date getPreviousDate(int day) {
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DATE, -day);
    	return cal.getTime();
    }

	public static String getPreviousDate(int day, String format) {
		Date date = getPreviousDate(day);
		SimpleDateFormat fmt = new SimpleDateFormat(format);
		return fmt.format(date);
	}
		
	/**
	 * 일주일 전 날을 구한다.
	 */
	public static Date getPreviousWeekDate() {
		return getPreviousDate(7);
    }

	public static String toFormat(Date date, String format){
		SimpleDateFormat st = new SimpleDateFormat(format);
		return st.format(date);
	}

	/**
	 * 날짜값이 유효한지 체크한다.
	 * @param date 날짜
	 * @param format 인자값 날짜 포맷(ex:yyyyMMdd)
	 * @return
	 */
	public static boolean validateDateStr(String date, String format){
		SimpleDateFormat sf = new SimpleDateFormat(format, Locale.KOREA);
		// 엄밀하지 않게
		sf.setLenient(false);
		try{
			sf.parse(date);
			return true;
		}
		catch (Exception e){
		}
		return false;
	}
}