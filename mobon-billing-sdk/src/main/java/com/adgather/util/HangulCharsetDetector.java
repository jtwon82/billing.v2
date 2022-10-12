package com.adgather.util;

import java.nio.charset.Charset;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.web.util.HtmlUtils;

/**
 * EUC-KR 또는 UTF-8 문자셋으로 작성된 바이트 배열을 패턴 분석해서 문자셋을 알아낸다.
 * 
 * @author yseun
 */
public class HangulCharsetDetector {
	static final Logger logger = LoggerFactory.getLogger(HangulCharsetDetector.class);

	static final Charset CS_US_ASCII = Charset.forName("US-ASCII");
	static final Charset CS_ISO_8859_1 = Charset.forName("ISO-8859-1");
	static final Charset CS_EUC_KR = Charset.forName("EUC-KR");
	static final Charset CS_UTF_8 = Charset.forName("UTF-8");

	private static final Utf8BitPatternSequence UTF8_PATTERN = new Utf8BitPatternSequence();
	private static final java.awt.Font FONT = new java.awt.Font("", java.awt.Font.PLAIN, 12);

	/**
	 * 바이트 배열을 한글 문자열로 변환
	 * 
	 * @param ab
	 *            바이트 배열
	 * @return 한글 문자열
	 */

	public static String toString(byte[] ab) {
		return toString(ab, 0, ab.length);
	} // toString()

	/**
	 * 지정한 범위의 바이트 배열을 한글 문자열로 변환
	 * 
	 * @param ab
	 *            바이트 배열
	 * @param iBegin
	 *            시작 인덱스
	 * @param iEnd
	 *            종료 인덱스
	 * @return 한글 문자열
	 */

	public static String toString(byte[] ab, int iBegin, int iEnd) {
		final Charset cs = _analyzeCharset(ab, iBegin, iEnd, false);
		if (cs != null) {
			return new String(ab, iBegin, iEnd, cs).trim();
		} else {
			// 2BYTE UTF-8 문자가 존재할 경우, UTF-8 인지 EUC-KR 인지 모호하게되는데,
			// UTF-8을 우선으로해서 화면 표시 가능하다면 해당 Charset으로 변환한다.
			final String s = new String(ab, iBegin, iEnd, CS_UTF_8);
			return (_isDisplayable(s) ? s : new String(ab, iBegin, iEnd, CS_EUC_KR)).trim();
		} // if
	} // toString()

	/**
	 * 바이트 문자열이 Html Escape 처리되어 있다면, Unescape 처리한 후, 한글 문자열로 변환한다.
	 * 
	 * @param sHtmlEscapedByteString
	 *            Html Escape 처리된 Byte String
	 * @return 한글 문자열
	 */

	public static String toStringAfterUnescape(String sHtmlEscapedByteString) {
		return toString(StringEscapeUtils.unescapeHtml4(sHtmlEscapedByteString).getBytes(CS_ISO_8859_1));
	} // toStringAfterHtmlUnescape()

	
	public static String toString(String sByteString, String sDefult) {
		if(StringUtils.isEmpty(sByteString)) 
			return sDefult;
		try {
			return toString(sByteString, 0, sByteString.length());
		} catch(Exception e) {}
		return sDefult;
	} // toString()
	
	/**
	 * 바이트 문자열을 한글 문자열로 변환, 바이트 문자열이 아니라면, {@code sByteString} 을 그대로 리턴함.
	 * 
	 * @param sByteString
	 *            byte 배열을 ISO_8859_1 문자셋 사용해서 만든 문자열
	 * @return 한글 문자열
	 */

	public static String toString(String sByteString) {
		return toString(sByteString, 0, sByteString.length());
	} // toString()

	/**
	 * 바이트 문자열을 한글 문자열로 변환, 바이트 문자열이 아니라면,
	 * {@code sByteString}.subsring({@code iBegin}, {@code iEnd}) 값을 리턴함.
	 * 
	 * @param sByteString
	 * @param iBegin
	 * @param iEnd
	 * @return 한글 문자열
	 */

	public static String toString(String sByteString, int iBegin, int iEnd) {
		final String sPart = sByteString.substring(iBegin, iEnd);
		for (int ch : sPart.toCharArray()) {
			if ((ch & 0xFF00) != 0) {
				logger.warn("Invalid Data : This is not byte string - {}", sPart);
				return sPart;
			} // if
		} // for
		final byte[] abPart = sPart.getBytes(CS_ISO_8859_1);
		final int iCharSize = _isUtf8(abPart, 0, abPart.length);
		if (iCharSize > 0) {
			return iCharSize == abPart.length ? sPart.trim() : new String(abPart, CS_UTF_8).trim();
		} else if (iCharSize == 0) {
			return new String(abPart, CS_EUC_KR).trim();
		} else {
			// 2BYTE UTF-8 문자가 존재할 경우, UTF-8 인지 EUC-KR 인지 모호하게되는데,
			// UTF-8을 우선으로해서 화면 표시 가능하다면 해당 Charset으로 변환한다.
			final String s = new String(abPart, CS_UTF_8);
			return (_isDisplayable(s) ? s : new String(abPart, CS_EUC_KR).trim());
		} // if
	} // toString()

	private static boolean _isDisplayable(String s) {
		int i = 0;
		int n = s.length();
		for (; i < n && FONT.canDisplay(s.charAt(i)); ++i)
			;
		return i == n;
	} // _isDisplayable()

	/**
	 * 지정한 byte 배열을 분석해서 {"US-ASCII", "UTF-8", "EUC-KR"} 문자셋 중 적합한 하나를 선택한다.
	 * 
	 * @param ab
	 *            바이트 배열
	 * @return "ASCII" 또는 "UTF-8" 또는 "EUC-KR" 값이 리턴된다.
	 */
	public static Charset analyzeCharset(byte[] ab) {
		return _analyzeCharset(ab, 0, ab.length, true);
	}

	/**
	 * 지정한 byte 배열을 분석해서 {"US-ASCII", "UTF-8", "EUC-KR"} 문자셋 중 적합한 하나를 선택한다.
	 * 
	 * @param ab
	 *            바이트 배열
	 * @param iBegin
	 *            시작 인덱스
	 * @param iEnd
	 *            종료 인덱스
	 * @return "ASCII" 또는 "UTF-8" 또는 "EUC-KR" 값이 리턴된다.
	 */
	public static Charset analyzeCharset(byte[] ab, int iBegin, int iEnd) {
		return _analyzeCharset(ab, iBegin, iEnd, true);
	}

	/**
	 * [내부 메소드] 지정한 byte 배열을 분석해서 {"US-ASCII", "UTF-8", "EUC-KR"} 문자셋 중 적합한 하나를
	 * 선택한다.
	 * 
	 * @param ab
	 *            바이트 배열
	 * @param iBegin
	 *            시작 인덱스
	 * @param iEnd
	 *            종료 인덱스
	 * @param bForceToDecide
	 *            모호한 경우, 가능성이 높은 Charset을 여기서 결정도록 함.
	 * @return null 은 UTF-8 인지 EUC-KR인지 모호한 경우, 그 외는 "ASCII" 또는 "UTF-8" 또는 "EUC-KR"
	 *         값이 리턴된다.
	 */
	private static Charset _analyzeCharset(byte[] ab, int iBegin, int iEnd, boolean bForceToDecide) {
		int iCharCount = 0;
		int iPos = iBegin;
		while (iPos < iEnd) {
			int iDelta = UTF8_PATTERN.isMatch(ab, iPos);
			if (iDelta == 0) {
				return CS_EUC_KR;
			} else if (iDelta == 2) { // 2-byte 짜리 UTF-8 문자가 존재한다면, UTF-8 형식도 맞지만, 다른 Charset 일수도 있다.
				if (bForceToDecide) {
					return _isDisplayable(new String(ab, iBegin, iEnd, CS_UTF_8)) ? CS_UTF_8 : CS_EUC_KR;
				} else {
					return null;
				} // if
			} // if
			++iCharCount;
			iPos += iDelta;
		} // for
		return iCharCount == iEnd - iBegin ? CS_US_ASCII : CS_UTF_8;
	} // _analyzeCharset()

	/**
	 * 바이트 배열이 UTF8 형식인가? (원래 문자가 ASCII 와 한글로만 구성됐을 때만 해당됨)
	 * 
	 * @param ab
	 *            바이트 배열
	 * @param iBegin
	 * @param iEnd
	 * @return 문자 갯수, 해당 byte 배열이 utf8 only면 양수값, utf8 형식이 아니면 0, uft8 형식에는 맞으나,
	 *         euc-kr형식일 수도 있으면 음수값.
	 */
	private static int _isUtf8(byte[] ab, int iBegin, int iEnd) {
		int iCharCount = 0;
		boolean bAmbiguous = false;
		for (int iPos = iBegin; iPos < iEnd;) {
			int iDelta = UTF8_PATTERN.isMatch(ab, iPos);
			if (iDelta == 0) {
				return 0;
			} else if (iDelta == 2) { // 2-byte 짜리 UTF-8 문자가 존재한다면, UTF-8 형식도 맞지만, 다른 Charset 일수도 있다.
				bAmbiguous = true;
			} // if
			++iCharCount;
			iPos += iDelta;
		} // for
		return bAmbiguous ? -iCharCount : iCharCount;
	} // _isUtf8()

	/** 수집중지 상품 명칭 여부 **/
    public static boolean isBrokenString(String str) {
    	if(StringUtils.isEmpty(str))
    		return false;
    	
    	if(PropertyHandler.isFalse("SHOP_DATA_BLOCK_MORE_PATTERN_ENABLE"))
    		return false;
    	
    	String[] brokenPatterns = PropertyHandler.getSplitString("SHOP_DATA_BLOCK_MORE_PATTERN", ",");
    	if(ArrayUtils.isEmpty(brokenPatterns))
    		return false;
    	
    	int pNameLen = str.length();
    	for(String brokenPattern : brokenPatterns) {
    		if(StringUtils.isEmpty(brokenPattern)) {
    			continue;
    		}
    		
        	String nPName = str.replace(brokenPattern, "");
        	if(pNameLen - nPName.length() >= 1) {
        		return true;
        	}
       	}
    	return false;
    }
	
	
	
	
	
	
	/**
	 * UTF8 문자에서 가능한 모든 비트 패턴시퀀스들
	 * <P>
	 * (아스키 + 한글) 문자만 대상으로 수정함
	 */
	private static final class Utf8BitPatternSequence {
		private final BitPatternSequence[] SEQUENCES;

		public Utf8BitPatternSequence() {
			SEQUENCES = new BitPatternSequence[] { // 효율을 위해, 2byte 짜리 UTF-8 패턴을 가장 마지막에 뒀다.
					new BitPatternSequence(new BitPattern(0x0, 1)) // java 6 이하에서 binary 상수표기 안된다. ex) 0b0
					, new BitPatternSequence(new BitPattern(0xE, 4), new BitPattern(0x2, 2), new BitPattern(0x2, 2)) // 0b1110, 0b10, 0b10
					, new BitPatternSequence(new BitPattern(0x6, 3), new BitPattern(0x2, 2)) // 0b110, 0b10
			};
		} // Utf8BitPatternSequence()

		/**
		 * 하나의 UTF-8 문자와 일치하는 byte 개수반환
		 * 
		 * @param ab
		 * @param iBegin
		 * @return
		 */
		public int isMatch(byte[] ab, int iBegin) {
			int iResult = 0;
			for (int i = 0; i < SEQUENCES.length && (iResult = SEQUENCES[i].isMatch(ab, iBegin)) == 0; ++i)
				;
			return iResult;
		} // isMatch()

		/**
		 * N 바이트에 대한 bit 패턴 유형
		 * <p>
		 * UTF8 문자는 바이트 크기가 다양하다. 바이트 크기에 따라 다른 bit 패턴 유형을 나타낸다.
		 */
		private static class BitPatternSequence {
			private final BitPattern[] BIT_PATTERNS;

			public BitPatternSequence(BitPattern... bitPatterns) {
				BIT_PATTERNS = bitPatterns;
			} // BitPatternSequence()

			public int isMatch(byte[] ab, int iBegin) {
				int i = 0;
				while (i < BIT_PATTERNS.length && (iBegin + i) < ab.length && BIT_PATTERNS[i].isMatch(ab[iBegin + i]))
					++i;
				return i == BIT_PATTERNS.length ? i : 0;
			} // isMatch()
		} // BitPatternSequence

		/**
		 * 1 Byte의 비트 패턴을 정의한다.
		 */
		private static class BitPattern {
			private final byte PATTERN;
			private final byte MASK;

			public BitPattern(int iPattern, int iSize) {
				PATTERN = (byte) (iPattern << (Byte.SIZE - iSize));
				MASK = (byte) (0xFF << (Byte.SIZE - iSize));
			} // BitPattern()

			public boolean isMatch(byte btData) {
				return (btData & MASK) == PATTERN;
			} // isMatch()
		} // BitPattern
	} // Utf8BitPatternSequence

	/** 테스트 코드 */
	public static void main(String... args) throws Exception {
//		final String[] asWord = { "유니코드", new String("★새해맞이! 1+1+1증정! 4Nº 고농축 앰플 20ml (비타C,아줄렌,히알루론,EGF) 택 1   ".getBytes(CS_UTF_8), CS_ISO_8859_1), // 특수문자 테스트
//				new String("★새해맞이! 1+1+1증정! 4Nº 고농축 앰플 20ml (비타C,아줄렌,히알루론,EGF) 택 1".getBytes(CS_EUC_KR), CS_ISO_8859_1), new String("º".getBytes(CS_UTF_8), CS_ISO_8859_1), // 특수문자 테스트
//				new String("º".getBytes(CS_EUC_KR), CS_ISO_8859_1), "ìì´ë°ì­ì¹¸", HtmlUtils.htmlEscape(new String("한".getBytes(CS_UTF_8), CS_ISO_8859_1)), HtmlUtils.htmlEscape(new String("한".getBytes(CS_EUC_KR), CS_ISO_8859_1)), };
//		System.out.println(asWord.length);
//		System.out.println("###########[toStringAfterUnescape]##############");
//		for (String sWord : asWord) {
//			System.out.format("[%s][%d] >>>>>> [%s]\n", sWord, sWord.length(), toStringAfterUnescape(sWord));
//		} // for
//		System.out.println("###########[toString(String)]##############");
//		for (String sWord : asWord) {
//			System.out.format("[%s][%d] >>>>>> [%s]\n", sWord, sWord.length(), toString(sWord));
//		} // for
//		System.out.println("###########[Analyze Charset]##############");
//		for (String sWord : asWord) {
//			byte[] abWord = sWord.getBytes(CS_ISO_8859_1);
//			Charset cs = analyzeCharset(abWord, 0, abWord.length);
//			System.out.format("[%s][%d] >>>>>> [%s][%s]\n", sWord, sWord.length(), cs, new String(abWord, cs));
//		}
	} // main()

} // HangulCharsetDetector
