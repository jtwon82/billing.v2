package com.mobon.billing.report.disk.json;

/**
 * 2018/04/13
 * 요청은 받지만, 수집하지 않음.
 * 최초 기획 담당자 : 안병찬.
 * @author 
 *
 */
public class AppGPS {

	private String ip;
	private String connectiontype;
	private String geotype; // 1 : GPS/Location Services, 2 : IP Address
	private String lat; // 위도
	private String lon; // 경도
	
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getConnectiontype() {
		return connectiontype;
	}
	public void setConnectiontype(String connectiontype) {
		this.connectiontype = connectiontype;
	}
	public String getGeotype() {
		return geotype;
	}
	public void setGeotype(String geotype) {
		this.geotype = geotype;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	
}
