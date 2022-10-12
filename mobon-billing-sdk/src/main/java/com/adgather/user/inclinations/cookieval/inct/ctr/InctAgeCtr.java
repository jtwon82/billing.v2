package com.adgather.user.inclinations.cookieval.inct.ctr;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.adgather.user.inclinations.ConsumerInclinations;
import com.adgather.user.inclinations.cookiedef.CookieDefRepository;

public class InctAgeCtr {
	private static char YOUTH_AGE = 'a'; // 10대미만(a), 10대(b), 20대(c), 30대(d), 40대(e), 50대(f), 60대이상(g)
	
	public static int[] toArray(String dispoAge) {
		if(StringUtils.isEmpty(dispoAge)) {
			return null;
		}
		int[] ages = {0,0,0,0,0,0,0};
		char ageChar = YOUTH_AGE;
		for(int idx = 0; idx < ages.length; idx++, ageChar++) {
			if(StringUtils.contains(dispoAge, ageChar)) {
				ages[idx] = 1;
			}
		}
		return ages;
	}
	
	/** 덤프로직에 적용하기 위함 **/
	public static String toArrayString(ConsumerInclinations inclinations) {
		if(inclinations == null) {
			return "";
		}
		List<Integer> list = inclinations.getCookie(CookieDefRepository.INCT_AGE);
		return toArrayString(list);
	}
	
	public static String toArrayString(List<Integer> ageList) {
		if(CollectionUtils.isEmpty(ageList)) {
			return "";
		}
		StringBuffer buf = new StringBuffer();
		for (int idx = 0; idx < ageList.size(); idx++) {
			if(idx != 0) {
				buf.append("|");
			}
			buf.append(ageList.get(idx));
		}
		return buf.toString();
	}
}
