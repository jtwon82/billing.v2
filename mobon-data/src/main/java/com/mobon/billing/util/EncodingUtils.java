package com.mobon.billing.util;

import org.mozilla.universalchardet.UniversalDetector;

public class EncodingUtils {

	public static String readEncoding(String str) {
		// encoding
		UniversalDetector detector = new UniversalDetector(null);
		try {
			int nread = str.length();
			detector.handleData(str.getBytes(), 0, nread);
			detector.dataEnd();
			
			String encoding = detector.getDetectedCharset();
			if(encoding!=null) {
				return encoding;
			} else {
				return null;
			}
		}catch(Exception e) {
			return null;
		}finally{
			detector.reset();
		}
	}
}
