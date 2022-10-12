/*
 * StringUtil.java
 *
 * Created on 2001년 11월 30일 금, 오전 11:19
 */

package com.mobon.billing.framertb.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Administrator
 * @version
 */
@Slf4j
public class StringUtil {
	public static final int		PROPERTY_DELIM		= '"';

	public static final char	CHAR_OF_BLANK		= ' ';

	public static final char	CHAR_OF_LF			= '\n';

	public static final char	CHAR_OF_CR			= '\r';

	private static Class		STRING_UTIL_CLASS	= null;

	static {
		try {
			STRING_UTIL_CLASS = StringUtil.class;
		}
		catch(Exception e) {
		}
	}

	/** Creates new StringUtil */
	public StringUtil() {
	}

	/**
	 * StringConvertUtil.ConvertString() 사용
	 * 
	 * @param source
	 *            원본
	 * @param infos
	 * @return 전환된 String
	 * @deprecated 스트링버퍼를 호출하는 쪽에서 넘겨받아 처리하는 로직으로 변경
	 */
	public static final String ConvertString(String source, List infos) {
		for (Iterator iter = infos.iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			String value = (String) iter.next();

			source = ConvertString(source, key, value);
		}
		return source;
	}

	/**
	 * 원본의 특정 스트링을 치환한다. target -> dest
	 * 
	 * @param source
	 *            원본스트링
	 * @param target
	 *            치환할 스트링
	 * @param dest
	 *            치환될 스트링
	 * @return
	 * @deprecated
	 */
	public static final String ConvertString(String source, String target, String dest) {
		if( source == null )
			return null;

		StringBuffer myBuffer = new StringBuffer(source.length());
		int idx1 = 0;
		int idx2 = 0;

		while (true) {
			idx1 = source.indexOf(target, idx2);

			if( idx1 < 0 )
				break;

			myBuffer.append(source.substring(idx2, idx1));
			myBuffer.append(dest);

			idx2 = idx1 + target.length();
		}

		myBuffer.append(source.substring(idx2));

		return myBuffer.toString();
	}

	/**
	 * 웹에디터에서 & 를 &amp; 로 치환한 것을 원복시킨다.
	 * 
	 * @param html
	 *            원본 스트링
	 * @return 치환결과 스트링
	 */
	public static final String removeSpecial(String html) {
		return removeSpecial(new StringBuffer(html));
	}

	/**
	 * 웹에디터에서 & 를 &amp; 로 치환한 것을 원복시킨다.
	 * 
	 * @param html
	 *            원본
	 * @return 치환결과 스트링
	 */
	public static final String removeSpecial(StringBuffer html) {
		for (int i = 0; i < html.length() - 6; i++) {
			if( (html.substring(i, i + 5)).equals("&amp;") ) {
				html.replace(i, i + 5, "&");
			}
		}

		return html.toString();
	}

	/**
	 * Splits string with specified "delimit"
	 * 
	 * @param str
	 * @param delimit
	 * @return
	 */
	public static String[] split(String str, String delimit) {

		String[] ret = new String[1];
		if( delimit == null || delimit.equals("") ) {
			ret[0] = str;
			return ret;
		}
		int len = delimit.length();
		int idx = 0;
		int count = 1;

		while ((idx = str.indexOf(delimit, idx)) != -1) {
			idx = idx + len;
			count++;
		}

		ret = new String[count];
		count = 0;
		idx = 0;
		int tmpIdx;

		while ((tmpIdx = str.indexOf(delimit, idx)) != -1) {
			ret[count] = str.substring(idx, tmpIdx);
			idx = tmpIdx + len;
			count++;
		}
		ret[count] = str.substring(idx);
		return ret;
	}
	
// as-is 방식 : 채널별로 리스트테이블 관리...하였지	
	public static final String getListTable_asis(String post_id, String channel_type){
		if( "EM".equals(channel_type) )      { return getEmailTable(post_id);
		}else if( "PU".equals(channel_type) ){ return getPushTable(post_id);
		}else if( "SS".equals(channel_type) ){ return getSmsTable(post_id);
		}else if( "SM".equals(channel_type) ){ return getSmsTable(post_id);
		}else{ return ""; }
	}
// to-be 방식 : 채널 구분 없이 통합.	
	public static final String getListTable(String post_id, String channel_type){
		if( post_id == null )
			return " ";
		else if( post_id.length() < 6 )
			return " ";

		return "TMS_CAMP_SEND_LIST_" + post_id.substring(4, 6);
	}
	

	/**
	 * 해당 POST_ID에 해당하는 테이블 이름을 반환한다.
	 * 
	 * @param post_id
	 *            대상 POST_ID
	 * @return 테이블 이름
	 */
	public static final String getEmailTable(String post_id) {
		if( post_id == null )
			return " ";
		else if( post_id.length() < 6 )
			return " ";

		return "TMS_CAMP_SEND_LIST_" + post_id.substring(4, 6);
	}
	public static final String getPushTable(String post_id) {
		if( post_id == null )
			return " ";
		else if( post_id.length() < 6 )
			return " ";
		
		return "TMS_CAMP_SEND_LIST_" + post_id.substring(4, 6);
	}
	public static final String getSmsTable(String post_id) {
		if( post_id == null )
			return " ";
		else if( post_id.length() < 6 )
			return " ";
		
		return "TMS_CAMP_SEND_LIST_" + post_id.substring(4, 6);
	}

	/**
	 * 원본 스트링에서 구분자로 구분한 순번중 해당 index의 스트링을 반환한다.
	 * 
	 * @param source
	 *            원본스트링
	 * @param delim
	 *            구분자
	 * @param idx
	 *            원하는 순번
	 * @return 해당 순번의 스트링
	 */
	public static final String FindIndexOfDelimString(String source, String delim, int idx) {
		int idx1 = 0;
		int idx2 = 0;

		int count = 1;

		String returnValue = "";

		while (true) {
			idx1 = source.indexOf(delim, idx2);

			if( idx1 < 0 )
				break;

			// 주어진 인덱스와 순번이 맞으면
			if( count == idx ) {
				return source.substring(idx2, idx1);
			}
			count++;
			idx2 = idx1 + delim.length();
		}

		if( count == idx ) {
			returnValue = source.substring(idx2);
		}

		return returnValue;
	}

	/**
	 * 이메일 주소에서 도메인을 구한다.
	 * 
	 * @param str
	 *            원본 이메일
	 * @return 이메일의 도메인
	 */
	public static final String getDomain(String str) {
		if( str == null )
			return "";
		int index = str.indexOf('@');
		if( index < -1 )
			return str;
		return str.substring(index + 1);
	}

	/**
	 * 전화번호에서 통신사 번호를 뽑는다. author 김정섭 ^^
	 * 
	 * @param str
	 *            원본 전화번호
	 * @return 전화번호의 통신사번호
	 */
	public static final String getTeleCom(String str) {
		if( str == null || str.length()<10)
			return "";
		return str.substring(0, 3);
	}

	/**
	 * 이메일 주소의 정합성을 검사한다.
	 * 
	 * @param inemail
	 *            검사할 이메일
	 * @return true : 정상 / false : 오류이메일
	 */
	public static final boolean isError(String inemail) {
		String email = inemail.trim();

		if( email.indexOf('@') < 0 )
			return true;

		if( !isValidHost(getDomain(email)) )
			return true;

		return false;
	}

	/**
	 * SMS의 정합성을 검사한다.
	 * 
	 * @param inSms : 검사할 이메일
	 * @return true : 비정상 / false : 정상
	 */
	public static final boolean isSmsError(String inSms) {
		String SMS = inSms.trim();
		if( SMS == null )
			return true;
		
		return !Pattern.matches("^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$", SMS);
//TODO (pioneer) 2014.08.04 SMS를 정규식으로 변경 
//		if( SMS.length() > 8 && SMS.length() < 14 )
//			return false;
//		return true;
	}

	/**
	 * 도메인 스트링의 정합성을 검증한다.
	 * 
	 * @param host
	 *            도메인 스트링
	 * @return true : 유효도메인 / false : 무효도메인
	 */
	public static final boolean isValidHost(String host) {
		if( host.indexOf("@") > 0 || host.indexOf(".") < 0 || host.indexOf(" ") > 0 || host.startsWith(".") || host.endsWith(".")
				|| host.indexOf("..") > 0 || host.startsWith("-") ) {
			return false;
		}

		// 유효문자 점검
		for (int i = 0; i < host.length(); i++) {
			int ch = host.charAt(i);
			if( (ch >= 48 && ch <= 57) || (ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122) || (ch == 45) || (ch == 46) || (ch == 95) ) {
			}
			else {
				return false;
			}
		}

		return true;
	}

	/**
	 * 널일 경우에 공백으로 치환한다.
	 * 
	 * @param src
	 *            입력 스트링
	 * @return 변환 스트링
	 */
	public static final String trimNull(Object src) {
		if( src == null )
			return "";

		return src.toString().trim();
	}

	/**
	 * 스트링중 목적 스트링의 포함수를 반환한다.
	 * 
	 * @param src
	 *            원본스트링
	 * @param target
	 *            카운트할 목적 스트링
	 * @return 목적스트링의 출현횟수
	 */
	public static final int CountString(String src, String target) {
		if( src == null )
			return -1;

		int idx1 = 0;
		int idx2 = 0;

		int returnValue = 0;

		while (true) {
			idx1 = src.indexOf(target, idx2);

			if( idx1 < 0 )
				break;

			returnValue++;

			idx2 = idx1 + target.length();
		}

		return returnValue;
	}

	public static String getPercent(String src, int leng) {
		if( src == null )
			src = "";
		else
			src = src.trim();

		if( src.length() <= leng ) {
			String returnValue = "0.";

			for (int i = 0; i < leng - src.length(); i++) {
				returnValue = returnValue + "0";
			}

			returnValue = returnValue + src;

			return returnValue;
		}

		return src.substring(0, src.length() - leng) + "." + src.substring(src.length() - leng);
	}

	private static DecimalFormat	number_formatter	= new DecimalFormat();

	public static boolean isPositive(String a) {
		if( a == null )
			return true;
		try {
			double src = Double.parseDouble(a);

			if( src < 0 ) {
				return false;
			}

			return true;
		}
		catch(Exception e) {
			return true;
		}
	}

	public static boolean isEqual(String src, String dest) {
		if( src == null && dest == null )
			return true;

		if( src == null || dest == null )
			return false;

		return src.equals(dest);
	}

	public static boolean isEqualIgnoreCase(String src, String dest) {
		if( src == null && dest == null )
			return true;

		if( src == null || dest == null )
			return false;

		return src.equalsIgnoreCase(dest);
	}

	public static String ABS(String a) {
		if( a == null )
			return "";
		try {
			return String.valueOf(Math.abs(Double.parseDouble(a)));
		}
		catch(Exception e) {
			return a;
		}
	}

	public static String ABSi(String a) {
		if( a == null )
			return "";
		try {
			return String.valueOf(Math.abs(Integer.parseInt(a)));
		}
		catch(Exception e) {
			return a;
		}
	}

	public static String toFormatedNumber(String a, String pattern) {
		if( a == null )
			return " ";

		double src = 0;
		try {
			src = Double.parseDouble(a);
		}
		catch(Exception e) {
			return a;
		}

		String returnValue = null;

		synchronized (number_formatter) {
			try {
				number_formatter.applyPattern(pattern);
				returnValue = number_formatter.format(src);
			}
			catch(Exception e) {
				return a;
			}
		}

		return returnValue == null ? " " : returnValue;
	}

	public static String getSimpleMoneyValue(String a) {
		if( a == null || a.trim().length() == 0 )
			return " ";
		return toFormatedNumber(a, "#,##0");
	}

	public static String getSimpleMoneyValue(Object a) {
		if( a == null )
			return " ";
		return getSimpleMoneyValue(a.toString());
	}

	public static String getMoneyValue(Object a) {
		if( a == null )
			return " ";
		return getMoneyValue(a.toString());
	}

	public static String getMoneyValue(String a) {
		if( a == null || a.trim().length() == 0 )
			return "0.00";
		return toFormatedNumber(a, "#,##0.00#");
	}

	public static String getStringOfStream(InputStream in, String char_set) throws Exception {
		byte[] buffer = new byte[1024];

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		while (true) {
			int bytes = in.read(buffer);
			if( bytes < 0 )
				break;

			out.write(buffer, 0, bytes);
		}

		in.close();

		out.flush();

		String returnValue = null;

		if( char_set == null ) {
			returnValue = out.toString();
		}
		else {
			returnValue = out.toString(char_set);
		}

		out.reset();
		out.close();

		return returnValue;
	}

	public static String getStringOfReader(Reader in) throws Exception {
		char[] buffer = new char[1024];

		CharArrayWriter out = new CharArrayWriter();

		while (true) {
			int bytes = in.read(buffer);
			if( bytes < 0 )
				break;

			out.write(buffer, 0, bytes);
		}

		in.close();

		out.flush();

		String returnValue = out.toString();

		out.reset();
		out.close();

		return returnValue;
	}

	public static String substring(String src, String start, String end) {
		if( src == null )
			return "";
		try {
			int idx1 = Integer.parseInt(start);
			int idx2 = Integer.parseInt(end);
			return src.substring(idx1, idx2);
		}
		catch(Exception e) {
			return src;
		}
	}

	/**
	 * 라인이 "." 으로만 되어 있으면 통신상에서 컨텐츠 마지막을 가리키기 때문에 ".." 이걸로 치환한다.
	 */
	public static String GeneralizeMailContents(String src) {
		if( src == null || src.trim().length() < 1 )
			return "";

		StringBuffer tmpBuffer = new StringBuffer();

		BufferedReader b = new BufferedReader(new StringReader(src));

		String tmp = null;

		try {
			while (b.ready()) {
				tmp = b.readLine();

				if( tmp == null )
					break;

				tmp = tmp.trim();

				if( tmp.startsWith(".") ) {
					tmpBuffer.append("..");
				}

				tmpBuffer.append(tmp);

				tmpBuffer.append("\r\n");
			}

			return tmpBuffer.toString();
		}
		catch(Exception e) {
			return src;
		}

	}

	public static String GeneralizeContents(String src, String start, String end, String head) {
		if( src == null )
			return "";

		int mapping_idx = 1;

		StringBuffer ReturnValue = new StringBuffer();

		int idx1 = 0;
		int idx2 = 0;
		int idx3 = 0;

		//		String key = null;
		//		Object value = null;

		while (true) {
			idx1 = src.indexOf(start, idx3);
			if( idx1 < 0 )
				break;

			idx2 = src.indexOf(end, idx1 + start.length());
			if( idx2 < 0 )
				break;

			ReturnValue.append(src.substring(idx3, idx1));

			ReturnValue.append(start);
			ReturnValue.append(head);
			ReturnValue.append(String.valueOf(mapping_idx++));
			ReturnValue.append(end);

			idx3 = idx2 + end.length();
		}

		ReturnValue.append(src.substring(idx3));

		return ReturnValue.toString();
	}

	public static boolean isBetween(String s, String f, String t) {
		if( s == null || s.trim().length() < 1 )
			return false;

		if( (f == null || f.trim().length() < 1) && (t == null || t.trim().length() < 1) )
			return false;

		double src = 0;

		try {
			src = Double.parseDouble(s);
		}
		catch(Exception e) {
			return false;
		}

		try {
			if( f != null && f.trim().length() > 0 && src < Double.parseDouble(f) )
				return false;
		}
		catch(Exception e) {
			return false;
		}

		try {
			if( t != null && t.trim().length() > 0 && src >= Double.parseDouble(t) )
				return false;
		}
		catch(Exception e) {
			return false;
		}

		return true;
	}

	public static String getDayFormat(String source) {
		if( source == null )
			return "";

		if( source.length() == 4 ) {
			if( Integer.parseInt(source) > 1231 ) {
				return source + "년";
			}

			return source.substring(0, 2) + "월 " + source.substring(2) + "일";
		}

		if( source.length() == 6 ) {
			return source.substring(0, 4) + "년 " + source.substring(4) + "월";
		}

		if( source.length() == 8 ) {
			return source.substring(0, 4) + "년 " + source.substring(4, 6) + "월 " + source.substring(6) + "일 ";
		}

		return source;
	}

	public static final String nl2blank(String src) {
		if( src == null )
			return "";

		return src.replace(CHAR_OF_CR, CHAR_OF_BLANK).replace(CHAR_OF_LF, CHAR_OF_BLANK);
	}

	/**
	 * Tag의 Property를 정리함과 동시에 중간에 삽입된 개행문자를 제거한다.
	 */
	public static String TagFilter(String src) {
		/* close Tag일경우에는 설정이 없기 때문에 그냥 소문자 변환하여 반환 */
		if( src.startsWith("</") )
			return src.toLowerCase();

		int idx1 = src.indexOf(CHAR_OF_BLANK);

		if( idx1 < 0 ) {
			return src;
		}

		return src.substring(0, idx1).toLowerCase();
	}

	public static String getAttribute(String __SRC__, String __ATT_NAME__) {
		String __LOWER_SRC__ = __SRC__.toLowerCase().replace('\'', '"');
		String __LOWER_ATT_NAME__ = __ATT_NAME__.toLowerCase();

		int __VALUE_QUALIFIER__ = 0;

		boolean next = true;

		int idx_param_start = __LOWER_SRC__.indexOf(__LOWER_ATT_NAME__);

		// 해당 파라미터가 없다면 null을 반환한다.
		if( idx_param_start < 0 )
			return null;

		int idx_param_end = __LOWER_SRC__.indexOf("=", idx_param_start);

		// 해당 파라미터가 없다면 null을 반환한다.
		if( idx_param_end < 0 )
			return null;

		int __VALUE_QUALIFIER_START_INDEX__ = idx_param_end;
		int __VALUE_QUALIFIER_END_INDEX__ = 0;

		next = true;

		while (next) {
			__VALUE_QUALIFIER_START_INDEX__++;
			__VALUE_QUALIFIER__ = __LOWER_SRC__.charAt(__VALUE_QUALIFIER_START_INDEX__);

			switch (__VALUE_QUALIFIER__) {
				case '\r':
				case '\n':
				case '\t':
				case ' ': {//공백이나 탭들은 건너 뛰어야한다.
					break;
				}

				default: { // 위에 정의된 char 이외의 문자는 구분자로 본다.
					next = false;
					break;
				}
			}
		}

		if( PROPERTY_DELIM == __VALUE_QUALIFIER__ ) { // 한정자가 " 로 시작한다면
			__VALUE_QUALIFIER_END_INDEX__ = __LOWER_SRC__.indexOf("\"", __VALUE_QUALIFIER_START_INDEX__ + 1);
			/**
			 * Property한정자가 " 로 시작하고 " 로 끝나지 않는다면 null을 반환한다.
			 */
			if( __VALUE_QUALIFIER_END_INDEX__ < 0 )
				return null;

			return __SRC__.substring(__VALUE_QUALIFIER_START_INDEX__ + 1, __VALUE_QUALIFIER_END_INDEX__).trim();
		}
		// 한정자가 없을 경우에는 공백이나 > 를 찾는다.
		__VALUE_QUALIFIER_END_INDEX__ = __LOWER_SRC__.indexOf(" ", __VALUE_QUALIFIER_START_INDEX__);
		/**
		 * Property한정자가 공백으로 끝나지 않는다면 > 를 찾고 아니면 null을 반환한다.
		 */
		if( __VALUE_QUALIFIER_END_INDEX__ < 0 ) {
			__VALUE_QUALIFIER_END_INDEX__ = __LOWER_SRC__.indexOf(">", __VALUE_QUALIFIER_START_INDEX__);

			if( __VALUE_QUALIFIER_END_INDEX__ < 0 )
				return null;
		}

		if( __VALUE_QUALIFIER_END_INDEX__ == __VALUE_QUALIFIER_START_INDEX__ ) {
			return "";
		}

		return __SRC__.substring(__VALUE_QUALIFIER_START_INDEX__, __VALUE_QUALIFIER_END_INDEX__).trim();
	}

	public static String switchAttribute(String __SRC__, String __ATT_NAME__, String __CHANGE_ATT_VALUE__) {
		String __LOWER_SRC__ = __SRC__.toLowerCase().replace('\'', '"');
		String __LOWER_ATT_NAME__ = __ATT_NAME__.toLowerCase();

		int __VALUE_QUALIFIER__ = 0;

		boolean next = true;

		int idx_param_start = __LOWER_SRC__.indexOf(__LOWER_ATT_NAME__);

		// 해당 파라미터가 없다면 null을 반환한다.
		if( idx_param_start < 0 )
			return __SRC__;

		int idx_param_end = __LOWER_SRC__.indexOf("=", idx_param_start);

		// 해당 파라미터가 없다면 null을 반환한다.
		if( idx_param_end < 0 )
			return __SRC__;

		int __VALUE_QUALIFIER_START_INDEX__ = idx_param_end;
		int __VALUE_QUALIFIER_END_INDEX__ = 0;

		next = true;

		while (next) {
			__VALUE_QUALIFIER_START_INDEX__++;
			__VALUE_QUALIFIER__ = __LOWER_SRC__.charAt(__VALUE_QUALIFIER_START_INDEX__);

			switch (__VALUE_QUALIFIER__) {
				case '\r':
				case '\n':
				case '\t':
				case ' ': {//공백이나 탭들은 건너 뛰어야한다.
					break;
				}

				default: { // 위에 정의된 char 이외의 문자는 구분자로 본다.
					next = false;
					break;
				}
			}
		}

		if( PROPERTY_DELIM == __VALUE_QUALIFIER__ ) { // 한정자가 " 로 시작한다면
			__VALUE_QUALIFIER_END_INDEX__ = __LOWER_SRC__.indexOf("\"", __VALUE_QUALIFIER_START_INDEX__ + 1);
			/**
			 * Property한정자가 " 로 시작하고 " 로 끝나지 않는다면 null을 반환한다.
			 */
			if( __VALUE_QUALIFIER_END_INDEX__ < 0 )
				return __SRC__;

			return new StringBuffer(__SRC__).replace(__VALUE_QUALIFIER_START_INDEX__ + 1, __VALUE_QUALIFIER_END_INDEX__, __CHANGE_ATT_VALUE__)
					.toString();
			//return __SRC__.substring( __VALUE_QUALIFIER_START_INDEX__ + 1 ,
			// __VALUE_QUALIFIER_END_INDEX__ ).trim();
		}
		// 한정자가 없을 경우에는 공백이나 > 를 찾는다.
		__VALUE_QUALIFIER_END_INDEX__ = __LOWER_SRC__.indexOf(" ", __VALUE_QUALIFIER_START_INDEX__);
		/**
		 * Property한정자가 공백으로 끝나지 않는다면 > 를 찾고 아니면 null을 반환한다.
		 */
		if( __VALUE_QUALIFIER_END_INDEX__ < 0 ) {
			__VALUE_QUALIFIER_END_INDEX__ = __LOWER_SRC__.indexOf(">", __VALUE_QUALIFIER_START_INDEX__);

			if( __VALUE_QUALIFIER_END_INDEX__ < 0 )
				return __SRC__;
		}

		if( __VALUE_QUALIFIER_END_INDEX__ == __VALUE_QUALIFIER_START_INDEX__ ) {
			return "";
		}

		return new StringBuffer(__SRC__).replace(__VALUE_QUALIFIER_START_INDEX__, __VALUE_QUALIFIER_END_INDEX__, __CHANGE_ATT_VALUE__).toString();
	}

	public static String switchAttribute2(String src, String pname, String value) {
		String source = src.toLowerCase().replace('\'', '"');
		String param = pname.toLowerCase();

		int idx1 = source.indexOf(param + "=");

		// 해당 파라미터가 없다면 null을 반환한다.
		if( idx1 < 0 )
			return null;

		int delim = source.charAt(idx1 + param.length() + 1);

		if( PROPERTY_DELIM == delim ) // 한정자가 " 로 시작한다면
		{
			int idx2 = source.indexOf("\"", idx1 + param.length() + 2);
			/**
			 * Property한정자가 " 로 시작하고 " 로 끝나지 않는다면 null을 반환한다.
			 */
			if( idx2 < 0 )
				return null;

			return new StringBuffer(src).replace(idx1 + param.length() + 2, idx2, value).toString();
		}
		// 한정자가 없을 경우에는 공백이나 > 를 찾는다.
		int idx_s = source.indexOf(">", idx1 + param.length() + 1);
		int idx_b = source.indexOf(" ", idx1 + param.length() + 1);

		if( idx_s < 0 && idx_b < 0 )
			return null;

		/**
		 * Property한정자가 공백이나 '>' 중에서 가까운 것으로 짤라야 한다.
		 */
		if( idx_s < idx_b ) {
			return new StringBuffer(src).replace(idx1 + param.length() + 1, idx_s, value).toString();
		}

		return new StringBuffer(src).replace(idx1 + param.length() + 1, idx_b, value).toString();
	}

	public static final int findIndex(byte[] src, byte[] target) {
		return findIndex(src, target, 0);
	}

	public static final int findIndex(byte[] src, byte[] target, int offset) {
		if( offset > src.length )
			return -1;

		int length = target.length;
		int range = src.length - length;

		for (int idx = offset; idx <= range; idx++) {
			if( src[idx] != target[0] )
				continue;

			boolean match = true;

			for (int i = 1; i < length; i++) {
				if( src[idx + i] != target[i] ) {
					match = false;
					break;
				}
			}

			if( match )
				return idx;
		}

		return -1;
	}

	public static final String attachLeftPadding(int source, int limit, String AppendDigit) {
		String returnValue = String.valueOf(source);

		while (returnValue.length() < limit) {
			returnValue = AppendDigit.concat(returnValue);
		}

		return returnValue;
	}

	public static final int getLength(String src) {
		if( src == null )
			return 0;
		return src.trim().length();
	}

	public static String removeFileSpecialString(String src) {
		if( src == null )
			return null;

		return src.replace('\\', '_').replace('/', '_').replace(':', '_').replace('"', '_').replace('<', '_').replace('>', '_').replace('|', '_');
	}

	public static final boolean isPureDigit(String src) {
		if( src == null )
			return false;
		return isPureDigit(src, 0, src.length());
	}

	public static final boolean isPureDigit(String src, int Start, int End) {
		if( src == null )
			return false;
		End = src.length() < End ? src.length() : End;
		for (int i = Start; i < End; i++) {
			if( !Character.isDigit(src.charAt(i)) )
				return false;
		}
		return true;
	}

	public static final String nl2br(String src) {
		StringBuffer TARGET = new StringBuffer(src);

		//log.debug( TARGET.toString() );

		for (int i = 0; i < TARGET.length(); i++) {
			char target = TARGET.charAt(i);
			if( target == CHAR_OF_LF ) {
				TARGET.insert(i, "<br>");
				i = i + 4;
				continue;
			}

			if( target == CHAR_OF_CR ) {
				TARGET.deleteCharAt(i);
				i--;
				continue;
			}
		}

		String returnValue = TARGET.toString();

		return returnValue;
	}
	
	public static String getAutoMemberID(Object mid) {
		if( mid == null ) {
			return "";
		}

		String M_ID = mid.toString();

		int idx = M_ID.lastIndexOf("_");

		if( idx < 0 ) {
			return M_ID;
		}

		return M_ID.substring(0, idx);
	}

	public static final boolean isNull(String src) {
		if( src == null || src.trim().length() < 1 ){
			return true;
		}

		return false;
	}

	public static final int len(Object src) {
		if( src == null )
			return 0;
		return src.toString().length();
	}

	public static final String addStrTONum(String str1, String str2) {

		if( str1 == null )
			str1 = "0";
		if( str2 == null )
			str2 = "0";

		try {
			return String.valueOf(Integer.parseInt(str1) + Integer.parseInt(str2));
		}
		catch(Exception ignore) {
			return "";
		}
	}

	/**
	 * Returns a new string resulting from replacing all occurrences of 'from'
	 * string in this string with 'to' string.
	 * 
	 * @param target
	 *            the java.lang.String object which will be changed
	 * @param from
	 * @param to
	 * @return changed string
	 */
	public static String replace(String target, String from, String to) {
		if( target == null || from == null || to == null )
			return null;

		int idx1 = 0;
		int idx2;

		while ((idx2 = target.indexOf(from, idx1)) != -1) {
			target = target.substring(0, idx2) + to + target.substring(idx2 + from.length());
			idx1 = idx2 + to.length();
		}
		return target;
	}

	/**
	 * 삼성테스코에서 하며 추가된 메소드
	 */

	/*******************************************************************************
	 * 현재일자를 지정한 형식으로 반환한다.
	 * @return 현재일자
	 * @throws Exception
	*******************************************************************************/
	public static String getToday(String pFormat) {
		return getDate(new Date(), pFormat);
	}

	/*******************************************************************************
	 *  Date 타입의 날짜를  지정한 형식의 문자형 날짜로 반환한다.
	 * @param pDate Date 객체
	 * @param pFormat SimpleDateFormat에 정의된 날짜형식
	 * @return 변경된 날짜
	*******************************************************************************/
	public static String getDate(Date pDate, String pFormat) {

        if(pDate == null)
            return "";

		StringBuffer ret = new StringBuffer();
		new SimpleDateFormat(pFormat).format(pDate, ret, new FieldPosition(0));
		return ret.toString();
	}

	
	/**
	 * 바이트수를 알아내서 길이만큼 자르고 글씨들을 더하는 기능
	 * @param str2 : 원문 String
	 * @param len : 자르고 싶은 길이
	 * @param tail : 원문에서 len만큼byte수만큼 자른후 뒷부분에 붙여질 글
	 * @return
	 */
	public static String getSubStr(String str2, int len, String tail) throws Exception{

		String str = "";
		int byteCnt = 0;
	
		try{
			//str = new String(str2.getBytes(), "8859_1");
			str = str2;
		}catch(Exception e){
//			log.debug(e.toString());
			str = str2;
	    }
	
		if(str.getBytes().length <= len)
			return str2;
	
		StringCharacterIterator sci = new StringCharacterIterator(str);
		StringBuffer sb = new StringBuffer();
		sb.append(sci.first());
		//log.debug("sb==================" + sb);
		for(int i = 1; byteCnt < len; i++)
		{
			char c = sci.next();
			sb.append(c);
			//log.debug("==========================]sci = " + sci.current());
			if (c > 127)
				byteCnt += 2;
			else
				byteCnt++;
		}
	
		sb.append(tail);
		//return toKor("" + sb.toString());
		return sb.toString();
	}

    /**
     * 틸드문자를 HTML로 변환
     * @param str
     * @return
     */
    public static String toHLML(String str){
		str = str.replaceAll("&amp;","&");
		str = str.replaceAll("&lt;", "<");
		str = str.replaceAll("&gt;", ">");
		str = str.replaceAll("&quot;","'");
        return str;
	}
    
    /**
     * SMS 숫자 체크 
     * @param str
     * @author KHT(droplet25)
     * @return
     */
    public static boolean isNumeric(String str) {   
    	
    	for (int i = 0; i < str.length(); i++) {
    		if (!Character.isDigit(str.charAt(i)))
    			return false;
	    }
    	return true;
    }  

    
    
    
    
    
    /**
     * ##############[samsunglife_method]############## 
     */
    
    /**
     * String 을 구분자로 나눠서 head와 숫자의 증가를 넣어 key를 만들어 properties에 저장한다.
     * @param str 리스트 스트링
     * @param dm 데이터 구분자
     * @param head 컬럼 KEY HEAD
     * @return Properties
     */
    public static Properties getProp_sslife(String str, String head) {
    	String dm = String.valueOf('\u007f');
    	
    	String[] str_extr    = split(str, dm);
    	Properties prop      = new Properties();
    	StringBuffer sBuffer = new StringBuffer();
    	String val_str = "";
    		
		for (int j = 0; j < str_extr.length; j++) {
    		if(isNull(str_extr[j])){
    			val_str = "&nbsp;";
    		}else{
    			val_str = str_extr[j].trim();
    		}
			sBuffer.setLength(0);
			sBuffer.append(head).append("_").append(j);
			prop.setProperty(sBuffer.toString(), val_str);
		}
		
		return prop;
    }
    
    
    /**
     * 삼성생명의 실시간 메일의 keyfiller에서 고객Id와 보안 여부를 가져오는 부분
     * @param key_filler
     * @return
     */
    public static String getKeyFillerValue(String key_filler){
    	String dm = String.valueOf('\u007f');
    	
    	if(StringUtil.isNull(key_filler)){
    		return " |Y";
    	}
    	
    	String[] str_a = split(key_filler, dm);
    	
    	if(str_a.length >= 5){
    		StringBuffer sBuffer = new StringBuffer();
    		
    		sBuffer.append(str_a[0].trim());
    		sBuffer.append("|");
			sBuffer.append(str_a[4].trim());
    		
    		return sBuffer.toString();
    		
    	}else{
    		return " |Y";
    	}
    }
    
    /**
     * String 을 구분자로 나눠서 head와 숫자의 증가를 넣어 key를 만들어 properties에 저장한다.
     * @param str 리스트 스트링
     * @param dm 데이터 구분자
     * @param head 컬럼 KEY HEAD
     * @return Properties
     */
    public static Properties getProp_sslife_token(String str, String head) {
    	String dm = String.valueOf('\u007f');
    	return getProp_sslife_token(str, dm, head);
    }
    
    public static Properties getProp_sslife_token(String str, char c_dm, String head) {
    	String dm = String.valueOf(c_dm);
    	return getProp_sslife_token(str, dm, head);
    }
    
    public static Properties getProp_sslife_token(String str, String dm, String head) {
    	Properties prop = new Properties();    	
    	StringTokenizer token_col = new StringTokenizer(str,dm);
    	
    	if(token_col.countTokens() == 0){
    		return prop;
    	}
    	
    	String col_str = "";
    	StringBuffer sBuffer = new StringBuffer();
    	String val_str = "";
    	
    	for(int col_cnt=0; token_col.hasMoreElements(); col_cnt++){
    		col_str = token_col.nextToken();
    		
    		if(isNull(col_str)){
    			val_str = "&nbsp;";
    		}else{
    			val_str = col_str.trim();
    		}
    		
    		sBuffer.setLength(0);
			sBuffer.append(head).append("_").append(col_cnt);
			prop.setProperty(sBuffer.toString(), val_str);
    	}
		
		return prop;
    }
    
    /**
     * 라인구분자와 컬럼 구분자를 이용하여 list형으로 구성해서 전달한다. key는 head와 숫자의 증가로 구성한다.
     * @param str 리스트 스트링
     * @param dm1 라인 구분자
     * @param dm2 컬럼 구분자
     * @return LinkedList
     * 
     * EX) ^ASDFAS|DFASD|FASDF|ASDFASDF^ASDFAS|DFASD|FASDF|ASDFASDF^ASDFAS|DFASD|FASDF|ASDFASDF^
     */
    public static LinkedList getList_sslife_token(String str,String head) {
    	String dm1 = String.valueOf('\u0014');
    	String dm2 = String.valueOf('\u007f');

    	return getList_sslife_token(str, dm1, dm2, head);
    }  
    
    public static LinkedList getList_sslife_token(String str, char c_dm1, char c_dm2, String head) {
    	String dm1 = String.valueOf(c_dm1);
    	String dm2 = String.valueOf(c_dm2);

    	return getList_sslife_token(str, dm1, dm2, head);
    }  
    
    public static LinkedList getList_sslife_token(String str, String dm1, String dm2, String head) {
    	LinkedList TMP_LIST_MAPPING_LIST = new LinkedList();
    	Properties prop = null;
    	
    	StringTokenizer token_line = new StringTokenizer(str,dm1);
    	StringTokenizer token_col = null;
    	
    	//log.debug("[token_line_cnt]"+token_line.countTokens());
    	if(token_line.countTokens() == 0){
    		//log.debug("no");
    		prop = new Properties();
    		TMP_LIST_MAPPING_LIST.addLast(prop);
    		
    		return TMP_LIST_MAPPING_LIST;
    	}
    	
    	String line_str = "";
    	String col_str  = "";
    	StringBuffer sBuffer = new StringBuffer();
    	
        while(token_line.hasMoreElements()){
        	prop = new Properties();
        	line_str = token_line.nextToken();
        	token_col = new StringTokenizer(line_str,dm2);
        	
        	if(token_col.countTokens() == 0){
        		TMP_LIST_MAPPING_LIST.addLast(prop);
        		continue;
        	}
        	String val_str = "";
        	
        	for(int col_cnt=0; token_col.hasMoreElements(); col_cnt++){
        		col_str = token_col.nextToken();
        		if(isNull(col_str)){
        			val_str = "&nbsp;";
        		}else{
        			val_str = col_str.trim();
        		}
        		sBuffer.setLength(0);
    			sBuffer.append(head).append("_").append(col_cnt);
    			prop.setProperty(sBuffer.toString(), val_str);
        	}
        	
        	TMP_LIST_MAPPING_LIST.addLast(prop);
        }
    	
    	return TMP_LIST_MAPPING_LIST;
    }  
    
    
    /**
     * @param value1 나눠질값, value2 나눌값
     * @return int 결과의 올림값
     */
    public static int divideCeil(int a1, int b1) {
		int returnStr = 0;
		try {
			float leaves = a1%b1;
			
			int temp = 0;
			if (leaves!= 0)
				temp = (a1/b1) + 1;
			else
				temp = a1/b1;
			
			returnStr = temp;
			return returnStr;
		} catch(Exception e) {
			return returnStr;
		}
    }    

    public static String getToday_sslife(String fmt) {
		java.util.Date currentdate = new java.util.Date();
		java.text.SimpleDateFormat timestring = new java.text.SimpleDateFormat(fmt);
		return timestring.format(currentdate);
    }
    
	public static String nullCheck(String s){
		String value = s.trim();		
		if(value.length() == 0 || s.equals("null")) {
			value="&nbsp;"; 
		}
		return value;
	}
		
	public static String delSpace(String data) {
		StringBuffer ts = new StringBuffer(replace(data, "　", " "));
		int size = data.length();
		for (int i = size-1 ;  i > 0 ; i--) {
			if (ts.charAt(i) == ' ') {
				ts.deleteCharAt(i);
			}
			else {
				return ts.toString();
			}
		}
		return ts.toString();
	}	
	
	public static String alterSign(String s){
		String value = s.trim();
		if(value.length() == 13){
			value = "*********"+value.substring(9,13);	
		}else{
			value="*"; 
		}
		return value;
	}	
    
	
    public static String trimZero(String message) {
        return String.valueOf(Long.parseLong(message));
    }	
    public static String encodeSeq( String jumin )
	{
		DecimalFormat dformat  = new DecimalFormat("000");
		String        en_jumin = "";

		boolean flag = false;

		if( jumin.length() != 13 ) return "___" + jumin + "___";

		for(int i=0; i<jumin.length(); i++ ) {
			String num = jumin.substring(i, i+1);

			if( num.equals("0") || num.equals("1") || num.equals("2") || num.equals("3") || num.equals("4") || num.equals("5") || num.equals("6") || num.equals("7") || num.equals("8") || num.equals("9") ) {
				flag = false;
			} else {
				flag = true;
				break;
			}
		}

		if( flag == true ) return "___" + jumin + "___";

		int chk_digit = Integer.parseInt(jumin.substring(12,13));

		if( jumin.length() == 13 ) {
			if( chk_digit  ==  0 ) {
				en_jumin =    dformat.format(Integer.parseInt(jumin.substring( 8,10)) * 9)
				            + dformat.format(Integer.parseInt(jumin.substring( 4, 6)) * 9)
				            + dformat.format(Integer.parseInt(jumin.substring(10,12)) * 9)
				            + dformat.format(Integer.parseInt(jumin.substring( 2, 4)) * 9) + "29"
				            + dformat.format(Integer.parseInt(jumin.substring( 6, 8)) * 9)
				            + dformat.format(Integer.parseInt(jumin.substring( 0, 2)) * 9);
			} else {
				en_jumin =    dformat.format(Integer.parseInt(jumin.substring( 8,10)) * chk_digit)
				            + dformat.format(Integer.parseInt(jumin.substring( 4, 6)) * chk_digit)
				            + dformat.format(Integer.parseInt(jumin.substring(10,12)) * chk_digit)
				            + dformat.format(Integer.parseInt(jumin.substring( 2, 4)) * chk_digit) + chk_digit + "2"
				            + dformat.format(Integer.parseInt(jumin.substring( 6, 8)) * chk_digit)
				            + dformat.format(Integer.parseInt(jumin.substring( 0, 2)) * chk_digit);
			}
		}
		else {
			en_jumin = "___" + jumin + "___";
		}

		return en_jumin;
	}    
    
    /**
     * 문자열 내의 캐릭터를 바꾼다.
     * @param origin 원래 문자열
     * @param ch 바꿀 캐릭터
     * @param startIndex 바꾸기 시작할 위치
     * @param endIndex 끝날위치
     * @return
     */
    public static String changeCharacter(String origin, String ch, int startIndex, int endIndex) {
    	String temp = "";
    	for (int i=0; i<endIndex-startIndex; i++) {
    		temp += ch;
    	}    	
    	return origin.substring(0, startIndex-1) + temp;
    }
    /**
     * 주민번호를 보기쉬운 형식으로 변경
     * @param jumin
     * @return
     */
	public static String getJuminFormat(String jumin) {
		return jumin.substring(0, 6) + " - " + jumin.substring(6, 13);
	}
    /**
     * 날짜포맷을 원하는 형태로 변환
     * @param from 날짜 데이터 (원본)
     * @param oFormat 원래의 포맷
     * @param rFormat 변경할 포맷
     * @return
     */
	public static String getFormattedDate(String from, String oFormat, String rFormat) {
		SimpleDateFormat oriForm = new SimpleDateFormat(oFormat);
		SimpleDateFormat resForm = new SimpleDateFormat(rFormat);
		Date oDate = null;
		long rDate = 0;
		try {
			oDate = oriForm.parse(from);
			rDate = oDate.getTime();
		} catch (ParseException e) {
			//e.printStackTrace();
//			log.error(e.getMessage());
		}
		return resForm.format(rDate);
	}
	 /**
     * 소수점 자리수 변환
     * @param data
     * @param digitCnt
     * @return
     */
    public static String changeDigit(String data, String digitCnt) {
    	if(isNull(data)) {
    		data = "0";
    	}
    	String retValue = "";
    	String temp = "";
    	String[] dataArr = split(data, ".");
    	String remainNum = "";
    	try {
    		// 0 padding
    		remainNum = dataArr[1];
    		if (remainNum.length() <= Integer.parseInt(digitCnt) ) {
	    		for (int i=0; i<Integer.parseInt(digitCnt)-dataArr[1].length()+1; i++)
	    			remainNum += "0";
	    	}
    		int length = Integer.parseInt(digitCnt);
	    	for (int i=0; i<length; i++) {
	    		if (i == length-1) {
	    			temp += ( remainNum.charAt(length) >= '0' && remainNum.charAt(length) < '5' ) ? 
	    					remainNum.charAt(i) : (char)(((int)remainNum.charAt(i)-'0') + 1 + '0');
	    		} else {
	    			temp += remainNum.charAt(i);
	    		}
	    	}
    	} catch (ArrayIndexOutOfBoundsException e) {
    		temp = "";
			for (int i=0; i<Integer.parseInt(digitCnt); i++) temp += "0";
		} finally {
			retValue = dataArr[0] + "." + temp;
		}
    	return retValue;
    }

    public static String escape(String src) {
        int i;
        char j;
       StringBuffer tmp = new StringBuffer();
       tmp.ensureCapacity(src.length() * 6);
        for (i = 0; i < src.length(); i++) {
             j = src.charAt(i);
              if (Character.isDigit(j) || Character. isLowerCase(j)
                         || Character. isUpperCase(j))
                   tmp.append(j);
              else if (j < 256) {
                   tmp.append( "%");
                    if (j < 16)
                         tmp.append( "0");
                   tmp.append(Integer. toString(j, 16));
             } else {
                   tmp.append( "%u");
                   tmp.append(Integer. toString(j, 16));
             }
       }
        return tmp.toString();
    }
    
    // 문자 바이트체크
 	public static int checkByte(String target){
 		int temp = target.length();
 		int count = 0;
 		char onechar;
 		for(int i=0;i<temp;i++){
 			onechar = target.charAt(i);
 			String kal =  String.valueOf(onechar);
 			if( StringUtil.escape(kal).length() > 4){
 				count += 2;
 			} else {
 				count +=1;
 			}
 		}
 		return count;
 	}
    
    
}
