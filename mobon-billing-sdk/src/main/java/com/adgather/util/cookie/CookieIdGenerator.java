package com.adgather.util.cookie;

import java.util.Random;

/**
 * 사용자를 구분하는 ID를 IP를 이용해서 생성한다.
 * <p>
 * ManagementCookie.makeKeyCookie() 에서 사용하던 로직 그대로 클래스만 분리하고, 내부 버퍼를 재사용함.
 */
public class CookieIdGenerator {
	private static final Random random = new Random();
	private static final StringBuilder sb = new StringBuilder(32);

	public static synchronized String next(String sIp) {
		sb.setLength(0);
		sb.append(sIp).append('.');
		sb.append(random.nextInt(100000000)); // 최대 100000000 미만의 정수가 리턴됨.
		return sb.substring(0, sb.length() > 20 ? 20 : sb.length());
	} // next()

} // CookieIdGenerator
