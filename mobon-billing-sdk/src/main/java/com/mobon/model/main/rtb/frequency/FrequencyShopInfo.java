package com.mobon.model.main.rtb.frequency;

import com.adgather.user.inclinations.cookieval.inct.InctShops;


/**
 * Frequency SR,CW 정보를 저장한다.
 * @author by ijhwang
 *
 */
public class FrequencyShopInfo extends FrequencyInfo {
	public FrequencyShopInfo() {}
	/**
	 * 쿠키의 상품 정보
	 */
	private InctShops shopInfo;	

	public InctShops getShopInfo() {
		return shopInfo;
	}

	public void setShopInfo(InctShops shopInfo) {
		this.shopInfo = shopInfo;
	}	
	
}
