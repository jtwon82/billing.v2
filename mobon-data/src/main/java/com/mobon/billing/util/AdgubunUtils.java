package com.mobon.billing.util;

import com.adgather.constants.G;

public class AdgubunUtils {

	public static boolean isPrdtTrgtCode(String adgubun) {
		if (G.adgubunFilter.contains(adgubun) || G.adgubunCodeFilter.contains(adgubun)) {
			return true;
		}
		else {
			return false;
		}
	}
}
