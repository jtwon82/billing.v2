package com.adgather.beans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adgather.constants.GlobalConstants;
import com.adgather.util.PropertyHandler;
import com.adgather.util.cookie.CookieIdGenerator;

import net.sf.json.JSONObject;

public class ManagementCookie {

	private static final Logger logger = LoggerFactory.getLogger(ManagementCookie.class);

	private static final DateFormat _format = new SimpleDateFormat("yyyyMMddHH");
	private static final Date _date = new Date();

	private static final String _getDateString() {
		synchronized (_format) {
			_date.setTime(System.currentTimeMillis());
			return _format.format(_date);
		} // synchronized
	} // _getDateString()

	public static String makeKeyCookie(HttpServletRequest request, HttpServletResponse response) {
		final String cookieName = "IP_info";
		String sIpInfo = getCookieInfo(request, cookieName);
		if (sIpInfo.isEmpty()) {
			sIpInfo = CookieIdGenerator.next(request.getRemoteAddr());
			makeCookie("Start_Time", _getDateString(), 60 * 60 * 24 * 730, response); // 최초 경우, Start_Time 쿠키 추가.
		} // if
		makeCookie(cookieName, sIpInfo, 60 * 60 * 24 * 730, response); // 2년동안 cookie 보관.
		return sIpInfo;
	} // makeKeyCookie()

	/** 삭제할 쿠키 객체는 캐싱해서 재활용하겠음 (by yseun) */
	private static final Map<String, Cookie> _mapNameToCookie = new ConcurrentHashMap<String, Cookie>(32);

	/** 삭제용 쿠키 반환 (by yseun) */
	private static Cookie _getCookieForDelete(String sName) {
		Cookie cookie = _mapNameToCookie.get(sName);
		if (cookie == null) {
			cookie = new Cookie(sName, "");
			cookie.setDomain(".dreamsearch.or.kr"); // 서브 도메인에만 접근 가능..
			cookie.setPath("/");
			cookie.setMaxAge(0);
			_mapNameToCookie.put(sName, cookie);
		} // if

		logger.debug("[delete cooke] " + sName);
		return cookie;
	} // _getCookieForDelete()

	/**
	 * Cookie 삭제
	 * 
	 * @param sName
	 *            쿠키명
	 * @param response
	 */
	public static void deleteCookie(String sName, HttpServletResponse response) {
		if (response == null)
			return;

		response.addCookie(_getCookieForDelete(sName));
	} // deleteCookie()

	public static void makeCookie(String sName, String sValue, int iAgeSeconds, HttpServletResponse response) {
		try {
			if (response == null)
				return;
			logger.debug("[make cooke] " + sName + " = " + sValue + " ; " + iAgeSeconds);
			Cookie cookie = new Cookie(sName, sValue);
			cookie.setDomain(".dreamsearch.or.kr"); // 서브 도메인에만 접근 가능..
			cookie.setPath("/");
			cookie.setMaxAge(iAgeSeconds);
			response.addCookie(cookie);
		} catch (Exception e) {
			if (PropertyHandler.getBoolean("PRINT_COOKIE_ERROR")) {
				logger.error(String.format("error param %s, %s, %s", sName, sValue, iAgeSeconds));
			}
		}
	} // makeCookie(String, String, int, HttpServletResponse)

	public static void makeCookie(JSONObject obj, int age, HttpServletResponse response) {
		@SuppressWarnings("unchecked")
		final Set<Map.Entry<String, Object>> entries = obj.entrySet();
		for (Map.Entry<String, Object> entry : entries) {
			makeCookie(entry.getKey(), entry.getValue().toString(), age, response);
		} // for
	} // makeCookie()

	public static String getCookieInfo(HttpServletRequest request, String cookieName) {
		final Cookie[] cookies = request.getCookies();
		for (int i = 0; cookies != null && i < cookies.length; i++) {
			if (cookies[i].getName().equals(cookieName)) {
				return cookies[i].getValue();
			} // if
		} // for
		return "";
	} // getCookieInfo()	
	
	/**
	 * drc(클릭) 발생시 컨버젼 용 쿠키 수집을 위해 별도 구현,
	 * 입력되는 배열을 _를 붙여 하나의 key로 만듬 2018
	 * @param list
	 * @return
	 */
	public static String makeConvCookie(String... list) {

		StringBuffer conv = new StringBuffer();
		for (String key : list) {
			conv.append(key).append(GlobalConstants.GUBUN);
		}
		
		return conv.toString();
	}

} // ManagementCookie
