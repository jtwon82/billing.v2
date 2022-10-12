package com.adgather.user.inclinations.etc;

import org.apache.commons.lang3.StringUtils;

public class AdGubun {
	private String platform;
	private String product;
	private String adGubun;
	
	public AdGubun() {}

    public AdGubun(String platform, String product, String adGubun) {
		this.platform = platform;
		this.product = product;
		this.adGubun = adGubun;
	}
	
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getAdGubun() {
		return adGubun;
	}
	public void setAdGubun(String adGubun) {
		this.adGubun = adGubun;
	}
	
	public boolean equals(AdGubun obj) {
		if(obj == null)		return false;
		
		return equalPlatform(obj.getPlatform()) && equalProduct(obj.getProduct()) && equalAdGubun(obj.getAdGubun());
	} 
	
	public boolean equalPlatform(String platform) {
		if(StringUtils.isEmpty(this.platform))	return false;
		
		return this.platform.equals(platform);
	}
	
	public boolean equalProduct(String product) {
		if(StringUtils.isEmpty(product))		return false;
		
		return this.product.equals(product);
	}
	
	public boolean equalAdGubun(String adGubun) {
		if(StringUtils.isEmpty(adGubun))		return false;
		
		return this.adGubun.equals(adGubun);
	}
	
	public static boolean isNotValidate(AdGubun obj) {
		return !isValidate(obj);
	}
	public static boolean isValidate(AdGubun obj) {
		if(obj == null)	return false;
		if(StringUtils.isEmpty(obj.platform))	return false;
		if(StringUtils.isEmpty(obj.product))	return false;
		if(StringUtils.isEmpty(obj.adGubun))	return false;

		return true;
	}
	
	
}
