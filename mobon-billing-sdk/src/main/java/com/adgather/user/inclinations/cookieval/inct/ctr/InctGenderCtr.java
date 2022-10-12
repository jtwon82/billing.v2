package com.adgather.user.inclinations.cookieval.inct.ctr;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.adgather.user.inclinations.ConsumerInclinations;
import com.adgather.user.inclinations.cookiedef.CookieDefRepository;

public class InctGenderCtr {
	private static char MAN = 'M';
	private static char WOMAN = 'W';
	
	public static int[] toArray(String dispoSex) {
		if(StringUtils.isEmpty(dispoSex)) {
			return null;
		}
		int[] genders = {0,0};
		if(StringUtils.contains(dispoSex, MAN)) {
			genders[0] = 1;
		}
		if(StringUtils.contains(dispoSex, WOMAN)) {
			genders[1] = 1;
		}
		return genders;
	}
	
	/** 덤프로직에 적용하기 위함 **/
	public static String toArrayString(ConsumerInclinations inclinations) {
		if(inclinations == null) {
			return "";
		}
		List<Integer> list = inclinations.getCookie(CookieDefRepository.INCT_GENDER);
		return toArrayString(list);
	}
	public static String toArrayString(List<Integer> genderList) {
		if(CollectionUtils.isEmpty(genderList)) {
			return "";
		}
		StringBuffer buf = new StringBuffer();
		for (int idx = 0; idx < genderList.size(); idx++) {
			if(idx != 0) {
				buf.append("|");
			}
			buf.append(genderList.get(idx));
		}
		return buf.toString();
	}	
}
