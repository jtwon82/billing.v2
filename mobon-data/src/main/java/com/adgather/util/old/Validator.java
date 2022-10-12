package com.adgather.util.old;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;



/** 값을 검사하는 클래스<p>
 * 여기서는 기본적인 "빈 값"(정의는 아래 참조)에 대한 검사 기능을 제공한다.<br>
 * 메소드가 많지만 대부분 overloading 메소드들이며, 아래 6가지 유형의 메소드만 존재한다.<br>
 * [주의] 공통적으로 <code>String</code> 자료형은 기본 trim 한 값을 대상으로 검사 또는 반환한다.<br>
 * <ul>
 * <li>제공 기능
 * <ul>
 * <li><code>isEmpty</code> : 빈 값인지 확인
 * <li><code>isNotEmpty</code> : 빈 값 아닌지 확인
 * <li><code>ensureNotEmpty</code> : 빈 값이 아닌 경우만 리턴한다 - 빈 값이 아니면 원래 값(<code>String</code> 은 trim 한 값) 리턴, 빈 값이면 {@link EmptyValueException} 예외 발생
 * <li><code>adjustEmpty</code> : 빈 값인 경우, 값을 조정한다 - 빈 값이 아니면 원래 값(<code>String</code> 은 trim 한 값) 리턴, 빈 값이면, 사용자가 전달한 default 값 리턴
 * <li><code>trim</code> : nullable 한 <code>String</code>을 trim 한다.
 * <li><code>toInt</code>, <code>toLong</code> : Exception 던지지 않고 숫자 파싱, Exception 발생시 기본값 전달.
 * </ul>
 * <li>여기서 검사하는 "빈 값"을 대상 유형별로 아래와 같이 정의한다.
 * <ul>
 * <li>Containter 객체 : null 이거나 크기가 0 인 경우 (예: 배열, <code>Collection</code>, <code>Map</code>, <code>Iterable</code> ..)
 * <li>문자열 객체 : null 이거나 whitespace 만 존재하는 경우 (예: <code>String</code>, <code>CharSequence</code> ..)
 * <li>그 외 객체 : null 인 경우
 * </ul>
 * </ul>
 *
 * @see EmptyValueException
 * @author yseun
 */
public class Validator
{
	private static final Pattern P_NON_WS = Pattern.compile("[^\\s]+"); // non white space pattern

	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	// 빈 값 확인
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	/** 빈 값인지 검사<p>
	 * @param s 검사할 값
	 * @return 빈 값이면 true
	 */
	public static boolean isEmpty(String s) { return trim(s, null) == null; }
	public static boolean isEmpty(CharSequence cs) { return cs == null || cs.length() == 0 || !P_NON_WS.matcher(cs).find(); }
	public static boolean isEmpty(Object o) { return o == null; }
	public static boolean isEmpty(Iterable<?> it) { return it == null || !it.iterator().hasNext(); }
	public static boolean isEmpty(Collection<?> col) { return col == null || col.isEmpty(); }
	public static boolean isEmpty(Map<?, ?> map) { return map == null || map.isEmpty(); }
	public static boolean isEmpty(Object[] ao) { return ao == null || ao.length == 0; }
	public static boolean isEmpty(byte[] ab) { return ab == null || ab.length == 0; }
	public static boolean isEmpty(short[] ash) { return ash == null || ash.length == 0; }
	public static boolean isEmpty(int[] ai) { return ai == null || ai.length == 0; }
	public static boolean isEmpty(long[] al) { return al == null || al.length == 0; }
	public static boolean isEmpty(float[] af) { return af == null || af.length == 0; }
	public static boolean isEmpty(double[] ad) { return ad == null || ad.length == 0; }

	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	// 빈 값이 아닌지 확인
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	/** 빈 값이 아닌지 검사<p>
	 * @param s 검사할 값
	 * @return 빈 값이 아니면 true
	 */
	public static boolean isNotEmpty(String s) { return trim(s, null) != null; }
	public static boolean isNotEmpty(CharSequence cs) { return !isEmpty(cs); }
	public static boolean isNotEmpty(Object o) { return !isEmpty(o); }
	public static boolean isNotEmpty(Iterable<?> it) { return !isEmpty(it); }
	public static boolean isNotEmpty(Collection<?> col) { return !isEmpty(col); }
	public static boolean isNotEmpty(Map<?, ?> map) { return !isEmpty(map); }
	public static boolean isNotEmpty(Object[] ao) { return !isEmpty(ao); }
	public static boolean isNotEmpty(byte[] ab) { return !isEmpty(ab); }
	public static boolean isNotEmpty(short[] ash) { return !isEmpty(ash); }
	public static boolean isNotEmpty(int[] ai) { return !isEmpty(ai); }
	public static boolean isNotEmpty(long[] al) { return !isEmpty(al); }
	public static boolean isNotEmpty(float[] af) { return !isEmpty(af); }
	public static boolean isNotEmpty(double[] ad) { return !isEmpty(ad); }

	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	// 빈 값 아닌 경우만 값 리턴, 빈값은 예외 발생 (1)
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	/** 문자열이  비어있지 않은지 확인하며, 빈 값이 아니면 원래 값을 trim 해서 리턴하고, 비어있다면 Exception 을 발생한다.<p>
	 * (의도) 코드를 간결하게 하기 위해 추가했으며, 아래 예제처럼 코드가 1줄로 줄일 수 있다..<br>
	 * <pre>
	 * [기존 스타일]
	 * String s = vo.getS();
	 * if (isEmpty(s)) {
	 * 	    return;
	 * }</pre>
	 * <pre>
	 * [새 스타일]
	 * String s = ensureNotEmpty(vo.getS());
	 * </pre>
	 * @param s 검사할 값
	 * @return 비어있지 않은 값
	 * @throws EmptyValueException 값이 비어있는 경우 발생하는 예외
	 */
	public static String ensureNotEmpty(String s) throws EmptyValueException { if ((s = trim(s, null)) == null) throw new EmptyValueException(); else return s; }
	/** (String, Collection, 배열을 제외한) 객체가 null 이 아닌지 확인하며, null 이 아니면 전달받은 값을 그대로 리턴하고, null 이면 Exception 을 발생시킨다.<p> */
	public static <T extends CharSequence> T ensureNotEmpty(T cs) throws EmptyValueException { if (isEmpty(cs)) throw new EmptyValueException(); else return cs; }
	public static <T> T ensureNotEmpty(T t) throws EmptyValueException { if (isEmpty(t)) throw new EmptyValueException(); else return t; }
	public static <T extends Iterable<?>> T ensureNotEmpty(T t) throws EmptyValueException { if (isEmpty(t)) throw new EmptyValueException(); else return t; }
	/** Collection (List, Set) 이 비어있지 않은지 확인하며, 비어있지 않으면 전달받은 값을 그대로 리턴하고, 비어있으면 Exception 을 발생시킨다.<p> */
	public static <T extends Collection<?>> T ensureNotEmpty(T t) throws EmptyValueException { if (isEmpty(t)) throw new EmptyValueException(); else return t; }
	/** Map 이 비어있지 않은지 확인하며, 비어있지 않으면 전달받은 값을 그대로 리턴하고, 비어있으면 Exception 을 발생시킨다.<p> */
	public static <T extends Map<?, ?>> T ensureNotEmpty(T t) throws EmptyValueException { if (isEmpty(t)) throw new EmptyValueException(); else return t; }
	public static <T> T[] ensureNotEmpty(T[] at) throws EmptyValueException { if (isEmpty(at)) throw new EmptyValueException(); else return at; }
	public static byte[] ensureNotEmpty(byte[] ab) throws EmptyValueException { if (isEmpty(ab)) throw new EmptyValueException(); else return ab; }
	public static short[] ensureNotEmpty(short[] ash) throws EmptyValueException { if (isEmpty(ash)) throw new EmptyValueException(); else return ash; }
	public static int[] ensureNotEmpty(int[] ai) throws EmptyValueException { if (isEmpty(ai)) throw new EmptyValueException(); else return ai; }
	public static long[] ensureNotEmpty(long[] al) throws EmptyValueException { if (isEmpty(al)) throw new EmptyValueException(); else return al; }
	public static float[] ensureNotEmpty(float[] af) throws EmptyValueException { if (isEmpty(af)) throw new EmptyValueException(); else return af; }
	public static double[] ensureNotEmpty(double[] ad) throws EmptyValueException { if (isEmpty(ad)) throw new EmptyValueException(); else return ad; }

	//////////////////////////////////////////////////////////////////////////////////////////
	// 빈 값 아닌 경우만 값 리턴, 빈값은 예외 발생 (1) - named value 검사용
	//////////////////////////////////////////////////////////////////////////////////////////
	/** 빈 값이 아님을 보장한다.<p>
	 * 리턴하는 값은 빈 값이 아니며, 빈 값은 예외가 발생한다.
	 * @param s 검사할 값
	 * @param sName 검사하는 값이 named value 라면, 어떤 값이 비어있어 예외가 발생했는지 추적하기 위해서, 값의 이름을 여기에 전달할 수 있다.
	 * @return 비어있지 않은 값
	 * @throws EmptyValueException 값이 비어있는 경우 발생하는 예외
	 */
	public static String ensureNotEmpty(String s, String sName) throws EmptyValueException { if ((s = trim(s, null)) == null) throw new EmptyValueException(sName); else return s; }
	public static <T extends CharSequence> T ensureNotEmpty(T cs, String sName) throws EmptyValueException { if (isEmpty(cs)) throw new EmptyValueException(sName); else return cs; }
	public static <T> T ensureNotEmpty(T t, String sName) throws EmptyValueException { if (isEmpty(t)) throw new EmptyValueException(sName); else return t; }
	public static <T extends Iterable<?>> T ensureNotEmpty(T t, String sName) throws EmptyValueException { if (isEmpty(t)) throw new EmptyValueException(sName); else return t; }
	public static <T extends Collection<?>> T ensureNotEmpty(T t, String sName) throws EmptyValueException { if (isEmpty(t)) throw new EmptyValueException(sName); else return t; }
	public static <T extends Map<?, ?>> T ensureNotEmpty(T t, String sName) throws EmptyValueException { if (isEmpty(t)) throw new EmptyValueException(sName); else return t; }
	public static <T> T[] ensureNotEmpty(T[] ab, String sName) throws EmptyValueException { if (isEmpty(ab)) throw new EmptyValueException(sName); else return ab; }
	public static byte[] ensureNotEmpty(byte[] ab, String sName) throws EmptyValueException { if (isEmpty(ab)) throw new EmptyValueException(sName); else return ab; }
	public static short[] ensureNotEmpty(short[] ash, String sName) throws EmptyValueException { if (isEmpty(ash)) throw new EmptyValueException(sName); else return ash; }
	public static int[] ensureNotEmpty(int[] ai, String sName) throws EmptyValueException { if (isEmpty(ai)) throw new EmptyValueException(sName); else return ai; }
	public static long[] ensureNotEmpty(long[] al, String sName) throws EmptyValueException { if (isEmpty(al)) throw new EmptyValueException(sName); else return al; }
	public static float[] ensureNotEmpty(float[] af, String sName) throws EmptyValueException { if (isEmpty(af)) throw new EmptyValueException(sName); else return af; }
	public static double[] ensureNotEmpty(double[] ad, String sName) throws EmptyValueException { if (isEmpty(ad)) throw new EmptyValueException(sName); else return ad; }

	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	// 빈 값을 조정해서 리턴
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	/** 빈 값을 조정한다.<p>
	 * 빈 값인 경우, 전달한 기본값으로 리턴한다.
	 * @param s 원래 값
	 * @param sDefault 기본 값
	 * @return 빈 값이 아니면 원래 값을, 빈 값이면 기본 값
	 */
	public static String adjustEmpty(String s, String sDefault) { return (s = trim(s, null)) == null ? sDefault : s; }
	public static <T> T adjustEmpty(T t, T tDefault) { return isEmpty(t) ? tDefault : t; }
	public static <T extends Iterable<?>> T adjustEmpty(T t, T tDefault) { return isEmpty(t) ? tDefault : t; }
	public static <T extends Collection<?>> T adjustEmpty(T t, T tDefault) { return isEmpty(t) ? tDefault : t; }
	public static <T extends Map<?, ?>> T adjustEmpty(T t, T tDefault) { return isEmpty(t) ? tDefault : t; }
	public static <T> T[] adjustEmpty(T[] ab, T[] abDefault) { return isEmpty(ab) ? abDefault : ab; }
	public static byte[] adjustEmpty(byte[] ab, byte[] abDefault) { return isEmpty(ab) ? abDefault : ab; }
	public static short[] adjustEmpty(short[] ash, short[] astDefault) { return isEmpty(ash) ? astDefault : ash; }
	public static int[] adjustEmpty(int[] ai, int[] aiDefault) { return isEmpty(ai) ? aiDefault : ai; }
	public static long[] adjustEmpty(long[] al, long[] alDefault) { return isEmpty(al) ? alDefault : al; }
	public static float[] adjustEmpty(float[] af, float[] afDefault) { return isEmpty(af) ? afDefault : af; }
	public static double[] adjustEmpty(double[] ad, double[] adDefault) { return isEmpty(ad) ? adDefault : ad; }

	/** 빈 값이면 빈 문자열("")을 리턴, 그 외는 trim() 한 문자열을 반환한다.<p>
	 * {@link StringUtils#nvl(String)} 을 대체하는 메소드
	 * @param s 원래 값
	 * @return s값이 비어있으면 "", 아니면 trim한 문자열.
	 */
	public static String adjustEmpty(String s) { return (s = trim(s, null)) == null ? "" : s; }
	public static String nvl(String s) { return adjustEmpty(s); }

	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	// Null 처리 가능한 trim
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @param s 원래 값
	 * @return 문자열이 null 이면 빈문자열, 빈 문자열이 아니면 trim 한 문자열을 리턴한다.
	 */
	public static String trim(String s) { return s == null ? "" : s.trim(); }
	public static String trim(String s, String sDefaultIfEmpty) { return s == null || (s = s.trim()).isEmpty() ? sDefaultIfEmpty : s; }

	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	// 예외 안던지는 숫자 파싱
	//////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////
	/** {@link Integer#parseInt(String)} 사용시 Exception 처리를 해줘야 한다. 이 메소드는 예외를 던지지 않으며, 발생시, <code>iDefault</code> 파라미터값을 리턴한다.
	 * @param s 파싱할 문자열
	 * @param iDefault 숫자로 파싱 실패시 리턴할 기본값
	 * @return 정수
	 */
	public static int toInt(String s, int iDefault) {
		if ((s = adjustEmpty(s, null)) != null) {
			try {
				return Integer.parseInt(s);
			} catch (Exception e) {
			} // try
		} // if
		return iDefault;
	} // toInt()

	/** 문자열을 Long 값으로 변환해서 리턴한다. 빈 값 또는 파싱오류가 발생하면, {@code lDefault}가 리턴된다.<p>
	 * @param s 파싱할 문자열
	 * @param lDefault 파싱 오류 발생시 리턴할 기본값
	 * @return 파싱한 long 값
	 */
	public static long toLong(String s, long lDefault) {
		if ((s = adjustEmpty(s, null)) != null) {
			try {
				return Long.parseLong(s);
			} catch (Exception e) {
			} // try
		} // if
		return lDefault;
	} // toLong()

	/* 테스트 코드 */
//	public static void main(String...args) throws Exception
//	{
//		System.out.println(Validator.<Integer>adjustEmpty(null, 0)); // -> invoke Validator.<T extends Object>adjustEmpty(T, T)
//		System.out.println(Validator.<java.util.ArrayList<Integer>>adjustEmpty(null, new java.util.ArrayList<Integer>(0))); // -> invoke Validator.<T extends Collection<?>>adjustEmpty(T, T)
////		Integer i = null;
////		System.out.format("%s\n", getDefaultIfEmpty(i, 9));
////		ensureNotEmpty("", "s");
////		String s = "";
////		StringBuilder s = new StringBuilder();
//		StringBuffer s = new StringBuffer();
////		s.append(" ");
////		s.append("\t");
//		s.append(".");
//		System.out.println(isEmpty(s)); // invoke Validator.<T
//	} // main()

} // Validator
