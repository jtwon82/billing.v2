package com.adgather.user.inclinations.convert;

import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;

import com.adgather.user.inclinations.convert.inter.CodeConverter;

/**
 * Base64 코드 변환기
 * 
 * @date 2017. 7. 4.
 * @param
 * @exception @see
 */
public class Base64Converter implements CodeConverter {
	private static final Charset CS_ISO_8859_1 = Charset.forName("ISO-8859-1");
	private static final Charset CS_UTF_8 = Charset.forName("UTF-8");

	private static Base64Converter instance;

	public static Base64Converter getInstance() {
		if (instance == null) {
			instance = new Base64Converter();
		}
		return instance;
	}

	private final Base64 BASE64 = new Base64(0, null, true);

	@Override
	public String encode(String sValue) {
		String str = new String(BASE64.encode(sValue.getBytes(CS_UTF_8)), CS_ISO_8859_1);
		// System.out.println(sValue + " => " + str);
		return str;
	}

	@Override
	public String decode(String sValue) {
		String str = new String(BASE64.decode(sValue.getBytes(CS_ISO_8859_1)), CS_UTF_8);
		// System.out.println(sValue + " => " + str);
		return str;
	}
}
