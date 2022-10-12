package com.adgather.user.inclinations.cookieval.inct;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import com.adgather.user.inclinations.cookieval.inct.ctr.InctHuCtr;
import com.adgather.user.inclinations.cookieval.inter.CookieVal;


/**
 * HU 방문횟수(성향) 로우 데이터
 * @author yhlim
 *
 */
public class InctHu implements CookieVal {
	public static final String KEY_END_BANNER = "endB";
	public static final String KEY_END_ICO = "endI";
	public static final String KEY_END_SKY = "endS";
	/** values ****************************************/
	private int visitCnt;				// 방문횟수
	private String domain;				// 도메인
	private String updDate;
	private boolean endBanner;
	private boolean endIco;
	private boolean endSky;
	
	private String fromApp; // 앱 모수성과 체크
	
	
//	private Map<String, Boolean> modEndList;

	/** create method **********************************/
	public InctHu() {}
	
	public InctHu(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}
	
	public InctHu(Document mongoDoc) throws Exception {
		setMongoValue(mongoDoc);
	}
	
	public InctHu(InctHu obj) {
		this.domain = obj.domain;
		this.visitCnt = obj.visitCnt;
		this.updDate = obj.updDate;
		this.endBanner = obj.endBanner;
		this.endIco = obj.endIco;
		this.endSky = obj.endSky;
		
		if(obj.fromApp != null)
			this.fromApp = obj.fromApp;//앱 모수성과 체크
	}
	
	/** value get/set method **********************************/
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public int getVisitCnt() {
		return visitCnt;
	}
	public void setVisitCnt(int visitCnt) {
		this.visitCnt = visitCnt;
	}
	public String getUpdDate() {
		return updDate;
	}
	public void setUpdDate(String updDate) {
		this.updDate = updDate;
	}
	public boolean isEndBanner() {
		return endBanner;
	}
	public void setEndBanner(boolean endBanner) {
		this.endBanner = endBanner;
	}
	public boolean isEndIco() {
		return endIco;
	}
	public void setEndIco(boolean endIco) {
		this.endIco = endIco;
	}
	public boolean isEndSky() {
		return endSky;
	}
	public void setEndSky(boolean endSky) {
		this.endSky = endSky;
	}
	
	public String getFromApp() {
		return fromApp;
	}
	
	public void setFromApp(String fromApp) {
		this.fromApp = fromApp;
	}
//	public Map<String, Boolean> getModEndList() {
//		return modEndList;
//	}
//	public void setModEndList(Map<String, Boolean> modEndList) {
//		this.modEndList = modEndList;
//	}

	/** Implements method  **********************************/
	@Override
	public void setCookieValue(Object cookieValue) throws Exception {
		if(cookieValue instanceof String) {
			setCookieValue((String)cookieValue);
		} else {
			throw new Exception("Unsupported Object.");
		}
	}
	public void setCookieValue(String cookieValue) throws Exception {
		if(StringUtils.isEmpty(cookieValue))		throw new Exception("Cookie Value Is Empty.");
		
		String[] strs = StringUtils.splitPreserveAllTokens(cookieValue, DELIMETER);
		if(strs == null || strs.length < 2)		throw new Exception("Cookie Value Is Not Validate.");
		
		this.visitCnt = NumberUtils.toInt(strs[0], 1);		// 도메인이 있으면 최소 방문 1회 설정
		this.domain = strs[1];
		this.updDate = strs.length >= 3 ? strs[2] : InctHuCtr.getUpdDate();		// yhlim 20171106 도메인 날짜 수집, (기존)없을 경우 현재 시간
		
		this.fromApp = strs.length >=4 ? strs[3] : null;		//앱 모수성과 체크
	}
	
	@Override
	public Object getCookieValue() {
		if(fromApp != null && !fromApp.equals(""))
			return String.format("%d%s%s%s%s%s%s"
						, visitCnt, DELIMETER
						, StringUtils.defaultString(domain), DELIMETER
						, StringUtils.defaultString(updDate), DELIMETER
						, StringUtils.defaultString(fromApp)); // 앱 모수성과 체크 
		else
			return String.format("%d%s%s%s%s"
					, visitCnt, DELIMETER
					, StringUtils.defaultString(domain), DELIMETER
					, StringUtils.defaultString(updDate));
	}
	
	@Override
	public void setMongoValue(Object mongoDoc) throws Exception {
		if(mongoDoc instanceof Document) {
			setMongoValue((Document)mongoDoc);
		} else {
			throw new Exception("Unsupported Object.");
		}
	}
	public void setMongoValue(Document mongoDoc) throws Exception {
		if(mongoDoc == null)				throw new Exception("Mongo Value Is Empty.");
		this.visitCnt = mongoDoc.getInteger("visitCnt", 0);
		this.domain = mongoDoc.getString("domain");
		
		String tempUpdDate = mongoDoc.getString("updDate");
		this.updDate = tempUpdDate != null ? tempUpdDate : InctHuCtr.getUpdDate();
		
		Boolean endB = mongoDoc.getBoolean("endB");		// banner
		this.endBanner = endB != null ? endB : false;
		
		Boolean endI = mongoDoc.getBoolean("endI");		// ico
		this.endIco = endI != null ? endI : false;
		
		Boolean endS = mongoDoc.getBoolean("endS");		// sky
		this.endSky = endS != null ? endS : false;
		
		if(mongoDoc.getString("fromApp") != null)
			this.fromApp = mongoDoc.getString("fromApp"); // 앱 모수성과 체크
	}
	
	@Override
	public Object getMongoValue() {
		Document doc = new Document();
		doc.put("visitCnt", this.visitCnt);
		doc.put("domain", this.domain);
		if(this.updDate != null)	doc.put("updDate", this.updDate);
		
		doc.put("endB", this.endBanner);
		doc.put("endI", this.endIco);
		doc.put("endS", this.endSky);
		
		if(this.fromApp != null)
			doc.put("fromApp", this.fromApp); //앱 모수성과 체크
		
		return doc;
	}
	
	@Override
	public InctHu clone() throws CloneNotSupportedException {
		return new InctHu(this);
	}
	
	@Override
	public String getKey() {
		return StringUtils.defaultString(domain);
	}
	
	@Override
	public void modValue(Object element, boolean bAppendValue) {
		if(!(element instanceof InctHu))		return;
		
		InctHu obj = (InctHu)element;
		if (bAppendValue) {
			this.visitCnt += obj.visitCnt;
		} else {
			this.visitCnt = obj.visitCnt;
		}
		this.updDate = InctHuCtr.getUpdDate();
		
//		if(obj.modEndList != null) {
//			for(Map.Entry<String, Boolean> entry : obj.modEndList.entrySet()) {
//				switch (entry.getKey()) {
//				case KEY_END_BANNER:	this.endBanner = entry.getValue(); break;
//				case KEY_END_ICO:		this.endIco = entry.getValue(); break;
//				case KEY_END_SKY:		this.endSky = entry.getValue(); break;
//				default:				break;
//				}				
//			}
//		}
		
		this.endBanner = obj.endBanner;
		this.endIco = obj.endIco;
		this.endSky = obj.endSky;
		
		if(obj.fromApp != null)
			this.fromApp = obj.fromApp; // 앱 모수성과 체크
	}
}
