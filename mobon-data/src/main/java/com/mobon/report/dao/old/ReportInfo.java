package com.mobon.report.dao.old;

import java.io.Serializable;
import java.util.List;


/**
 * 통계 기본 정보
 * 
 * @author jordan
 */
public class ReportInfo extends com.adgather.lang.old.ObjectToString implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int scriptNo = 0;
	private String scriptUserId;
	private String siteCode;
	private String siteName;
	private String userId;
	private String adGubun;
	private String platformType;
	private String productType;
	private List<String> productTypes;

	private String kno; // um, kl 일때만 사용함. 상품 일땐 pcode

	/**
	 * TODO filter class 분리 하고 싶다...
	 */
	private String tableName;
	private String searchType; // 검색조건
	private String mediaType; // 미디어타입 조회구분(id : 매체아이디기준, code : 매체코드기준)

	public int getScriptNo() {
		return scriptNo;
	}

	public void setScriptNo(int scriptNo) {
		this.scriptNo = scriptNo;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAdGubun() {
		return adGubun;
	}

	public void setAdGubun(String adGubun) {
		this.adGubun = adGubun;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getScriptUserId() {
		return scriptUserId;
	}

	public void setScriptUserId(String scriptUserId) {
		this.scriptUserId = scriptUserId;
	}

	public String getKno() {
		return kno;
	}

	public void setKno(String kno) {
		this.kno = kno;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public List<String> getProductTypes() {
		return productTypes;
	}

	public void setProductTypes(List<String> productTypes) {
		this.productTypes = productTypes;
	}
}