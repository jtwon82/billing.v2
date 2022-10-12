package com.mobon.billing.model.v15;

import java.io.Serializable;
import java.util.Map;

import com.adgather.util.old.StringUtils;
import com.mobon.billing.model.ActionData;

public class ChrgLogData extends ActionData implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String product = "";		// 광고상품
	private String etc = "";			// 기타
	
	public static ChrgLogData fromHashMap(Map from) {
		ChrgLogData result = new ChrgLogData();
		
		result.yyyymmdd = StringUtils.trimToNull2(from.get("yyyymmdd"));
		result.product = StringUtils.trimToNull2(from.get("product"), "");
		result.setChrgTpCode( StringUtils.trimToNull2(from.get("chrgTpCode"), "") );
		result.scriptNo = StringUtils.trimToNull2(from.get("scriptNo"), "0");
		result.siteCode = StringUtils.trimToNull2(from.get("siteCode"), "");
		result.pCode = StringUtils.trimToNull2(from.get("pCode"), "");
		result.keyIp = StringUtils.trimToNull2(from.get("keyIp"), "");
		result.point = Float.parseFloat(StringUtils.trimToNull2(from.get("point"), "0"));
		result.sendDate = StringUtils.trimToNull2(from.get("sendDate"), "");
		result.etc = StringUtils.trimToNull2(from.get("etc"), null);
		
		return result;
	}	
	
	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getEtc() {
		return etc;
	}

	public void setEtc(String etc) {
		this.etc = etc;
	}

	@Override
	public String generateKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sumGethering(ActionData from) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
