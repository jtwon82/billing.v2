package com.mobon.billing.sample.model;

public class UseridVo {
	private String yyyymmdd="";
	private String hh="";
	private String advertiserId="";
	private String siteCode="";
	private String scriptUserId="";
	private String scriptNo="";
	private float point=0;

	public String getKey() {
		return String.format("%s_%s_%s_%s", this.yyyymmdd, this.hh, this.siteCode, this.scriptUserId);
	}

	public void sumGethering(Object _from) {
		UseridVo from = (UseridVo)_from;
		this.setPoint( this.getPoint() + from.getPoint() );
	}
	public String getGrouping() {
		return String.format("[%s]", Math.abs(this.getSiteCode().hashCode() % 20)+"");
	}
	public static void main(String []ar) {
		UseridVo vo = new UseridVo();
		for(int i=0; i<10000; i++) {
			vo.setAdvertiserId(i+"");
			System.out.println( vo.getGrouping() );
		}
	}
	
	public float getPoint() {
		return point;
	}
	public void setPoint(float point) {
		this.point = point;
	}
	public String getScriptUserId() {
		return scriptUserId;
	}
	public void setScriptUserId(String scriptUserId) {
		this.scriptUserId = scriptUserId;
	}
	public String getAdvertiserId() {
		return advertiserId;
	}
	public void setAdvertiserId(String advertiserId) {
		this.advertiserId = advertiserId;
	}
	public String getHh() {
		return hh;
	}
	public void setHh(String hh) {
		this.hh = hh;
	}
	public String getYyyymmdd() {
		return yyyymmdd;
	}
	public void setYyyymmdd(String yyyymmdd) {
		this.yyyymmdd = yyyymmdd;
	}
	public String getScriptNo() {
		return scriptNo;
	}
	public void setScriptNo(String scriptNo) {
		this.scriptNo = scriptNo;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	
}
