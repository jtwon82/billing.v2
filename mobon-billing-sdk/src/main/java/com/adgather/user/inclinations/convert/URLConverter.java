package com.adgather.user.inclinations.convert;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;

import com.adgather.user.inclinations.convert.inter.CodeConverter;

/**
 * URLEncode 코드 변환기
 * 
 * @date 2017. 7. 4.
 * @param
 * @exception @see
 */
public class URLConverter implements CodeConverter {
	private static final String CS_UTF_8 = "UTF-8";

	private static URLConverter instance;

	public static URLConverter getInstance() {
		if (instance == null) {
			instance = new URLConverter();
		}
		return instance;
	}

	@Override
	public String encode(String sValue) {
		String enStr = null;
		try {
			enStr = URLEncoder.encode(sValue, CS_UTF_8);
		} catch (Exception e) {
		}
		return enStr;
	}

	@Override
	public String decode(String sValue) {
		String deStr = null;
		boolean bProc = false;
		try {
			deStr = URLDecoder.decode(sValue, CS_UTF_8);
			bProc = true;
		} catch (Exception e) {
		}
		if (bProc)
			return deStr;

		// 복원 재처러
		try {
			sValue = StringUtils.substringBeforeLast(sValue, "%");
			deStr = URLDecoder.decode(sValue, CS_UTF_8);
		} catch (Exception e) {
		}

		return deStr;
	}
}
