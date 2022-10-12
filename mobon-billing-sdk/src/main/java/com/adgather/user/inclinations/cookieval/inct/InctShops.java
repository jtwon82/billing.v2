package com.adgather.user.inclinations.cookieval.inct;

import com.adgather.user.inclinations.cookieval.inter.CookieVal;
import com.adgather.user.inclinations.cookieval.inter.Waitable;
import com.mobon.service.ADService.frequency.base.FTargetData;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import java.util.Date;

/**
 * 샵로그(성향) 로우 데이터, 데이터 서버스 대기기능 가능
 * @author yhlim
 *
 */
public class InctShops extends Waitable implements CookieVal, FTargetData{
	/** 쿠키 역확 *************************************************
	 * 1. 소비자 관심 상품 정보 수집
	 * 2. 광고송출 가능 여부(프리퀀시 가능상태/종료상태)
	 * ********************************************************/
	/** 코드정보 *************************************************
	 * 
	 * 광고구분 초기 설정  (#yhlim 코드 처리 방법 변경 필요)
	 * 						cwgb		mcgb		td				적용구분자	  banner	   sky	   ico   pl
	 * 			CW		"GHIP"		""			""								    G			H		I    P
	 * 			SR			""			""			""								A			C		B    P
	 * 			RC			""			"DEFP"		""								D			E		F    P
	 * 			SP			""			"ACB"		""								A			C		B
	 * * mcgb는 SR 이외 추가 시에 구분자 1개만 추가된다.
	 * * 프리퀀시 종료시 해당 구분자가  td에 적용된다.
	 * ********************************************************/

	public static final String KEY_CYCLE_END_BANNER	= "cycleEndB";
	public static final String KEY_CYCLE_END_ICO		= "cycleEndI";
	public static final String KEY_CYCLE_END_SKY		= "cycleEndS";
	
	/** values ****************************************/
	private String siteCode;			// 캠패인 코드 (캠페인 : 광고주의 모비온 광고 상품)
	private String pCode;				// 상품코드
	private String tg;					// 
	private String mcgb;				// 
	private String cwgb;				// 장바구니 (모비온 상품) 타겟팅여부(모비온 상품; 일반베너, 브랜드 링크, 아이커버)
	private String td;					// 수집일(변경필요 -> 수집시간 추가)
	
	private String fromApp;		// 앱 모수성과 체크용
	
	//hiden info (몽고에만 적용되는 정보)
	private int visitCnt;				// 방문횟수 (hiden info)
	private boolean visitCounging;		// 카운팅 여부(상태 요청에만 적용)


	private boolean cycleEndBanner;			// 일반배너 순환 종료
	private boolean cycleEndIco;			// 아이커버 순환 종료
	private boolean cycleEndSky;			// 브랜드 순환 종료

//	private Map<String, Boolean> modCycleEndList;
	
	/** create method **********************************/
	public InctShops() {}
	
	public InctShops(String cookieValue) throws Exception {
		setCookieValue(cookieValue);
	}
	
	public InctShops(Document mongoDoc) throws Exception {
		setMongoValue(mongoDoc);
	}
	
	public void setCycleEndBanner(boolean cycleEndBanner) {
		this.cycleEndBanner = cycleEndBanner;
	}

	public void setCycleEndIco(boolean cycleEndIco) {
		this.cycleEndIco = cycleEndIco;
	}

	public void setCycleEndSky(boolean cycleEndSky) {
		this.cycleEndSky = cycleEndSky;
	}

	public InctShops(InctShops obj) {
		this.siteCode = obj.siteCode;
		this.pCode = obj.pCode;
		this.tg = obj.tg;
		this.mcgb = obj.mcgb;
		this.cwgb = obj.cwgb;
		this.td = obj.td;
		this.visitCnt = obj.visitCnt;
		this.cycleEndBanner = obj.cycleEndBanner;
		this.cycleEndIco = obj.cycleEndIco;
		this.cycleEndSky = obj.cycleEndSky;
		
		if(obj.fromApp != null)
			this.fromApp = obj.fromApp;
	}
	
	/** value get/set method **********************************/
	public String getSiteCode() {
		return siteCode;
	}
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	public String getpCode() {
		return pCode;
	}
	public void setpCode(String pCode) {
		this.pCode = pCode;
	}
	public String getTg() {
		return tg;
	}
	public void setTg(String tg) {
		this.tg = tg;
	}
	public String getMcgb() {
		return mcgb;
	}
	public void setMcgb(String mcgb) {
		this.mcgb = mcgb;
	}
	public String getCwgb() {
		return cwgb;
	}
	public void setCwgb(String cwgb) {
		this.cwgb = cwgb;
	}
	public String getTd() {
		return td;
	}
	public void setTd(String td) {
		this.td = td;
	}
	public int getVisitCnt() {
		return visitCnt;
	}
	public void setVisitCnt(int visitCnt) {
		this.visitCnt = visitCnt;
	}
	public boolean isVisitCounging() {
		return visitCounging;
	}
	public void setVisitCounging(boolean visitCounging) {
		this.visitCounging = visitCounging;
	}
	public boolean isCycleEndBanner() {
		return cycleEndBanner;
	}
	public boolean isCycleEndIco() {
		return cycleEndIco;
	}
	public boolean isCycleEndSky() {
		return cycleEndSky;
	}


	public String getFromApp() {
		return fromApp;
	}
	public void setFromApp(String fromApp) {
		this.fromApp = fromApp;
	}
//	public Map<String, Boolean> getModCycleEndList() {
//		return modCycleEndList;
//	}
//	public void setModCycleEndList(Map<String, Boolean> modCycleEndList) {
//		this.modCycleEndList = modCycleEndList;
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
//		if(strs == null || (strs.length >= 6 && strs.length <= 7))		throw new Exception("Cookie Value Is Not Validate.");
		if(strs == null || (strs.length <  6))		throw new Exception("Cookie Value Is Not Validate.");
		
		this.siteCode = strs[0];
		this.pCode = strs[1];
		this.tg = strs[2];
		this.mcgb = strs[3];
		this.cwgb = strs[4];
		this.td = strs[5];
		this.visitCnt = 1;		// 쿠키의 방문 횟수는 1로 설정한다.
		
		if(strs.length == 7)
			this.fromApp = strs[6];
		
	}
	
	@Override
	public Object getCookieValue() {
		// ex) 2f7f159a8b8fcf23d9fa43ebbc73b57a^45666^I^^GHI
		if(fromApp != null && !fromApp.equals(""))
			return String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s"
						, StringUtils.defaultString(siteCode), DELIMETER
						, StringUtils.defaultString(pCode), DELIMETER
						, StringUtils.defaultString(tg), DELIMETER
						, StringUtils.defaultString(mcgb), DELIMETER
						, StringUtils.defaultString(cwgb), DELIMETER
						, StringUtils.defaultString(td), DELIMETER
						, StringUtils.defaultString(fromApp));
		else
			return String.format("%s%s%s%s%s%s%s%s%s%s%s"
					, StringUtils.defaultString(siteCode), DELIMETER
					, StringUtils.defaultString(pCode), DELIMETER
					, StringUtils.defaultString(tg), DELIMETER
					, StringUtils.defaultString(mcgb), DELIMETER
					, StringUtils.defaultString(cwgb), DELIMETER
					, StringUtils.defaultString(td));
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
		this.siteCode = mongoDoc.getString("siteCode");
		this.pCode = mongoDoc.getString("pCode");
		if(mongoDoc.containsKey("tg"))		this.tg = mongoDoc.getString("tg");
		if(mongoDoc.containsKey("mcgb"))	this.mcgb = mongoDoc.getString("mcgb");
		if(mongoDoc.containsKey("cwgb"))	this.cwgb = mongoDoc.getString("cwgb");
		if(mongoDoc.containsKey("td"))		this.td = mongoDoc.getString("td");
		
		if(mongoDoc.containsKey("visitCnt")) {
			if(StringUtils.isNotEmpty(this.td) && "20180124170000".compareTo(this.td) > 0) {		// 1월 24일 오후 2시 이전 것 CLEAR처리
				setVisitCnt(1);
			} else {
				setVisitCnt(mongoDoc.getInteger("visitCnt", 1));
			}
		} else {
			setVisitCnt(1);
		}

		Boolean endB = mongoDoc.getBoolean("cycleEndB");		// banner
		this.cycleEndBanner = endB != null ? endB : false;

		Boolean endI = mongoDoc.getBoolean("cycleEndI");		// ico
		this.cycleEndIco = endI != null ? endI : false;

		Boolean endS = mongoDoc.getBoolean("cycleEndS");		// sky
		this.cycleEndSky = endS != null ? endS : false;
		
		if(mongoDoc.containsKey("waitTime")) {
			setWaiteTime(mongoDoc.getDate("waitTime").getTime());
		}
		
		if(mongoDoc.containsKey("fromApp")) this.fromApp = mongoDoc.getString("fromApp"); // 앱 모수성과 체크
	}
	
	@Override
	public Object getMongoValue() {
		Document doc = new Document();
		doc.put("siteCode", this.siteCode);
		doc.put("pCode", this.pCode);
		doc.put("tg", this.tg);
		doc.put("mcgb", this.mcgb);
		doc.put("cwgb", this.cwgb);
		doc.put("td", this.td);
		doc.put("visitCnt", this.visitCnt);
		
		doc.put("cycleEndB", this.cycleEndBanner);
		doc.put("cycleEndI", this.cycleEndIco);
		doc.put("cycleEndS", this.cycleEndSky);
		
		if(getWaitTime() > 0) {
			doc.put("waitTime", new Date(getWaitTime()));
		}
		
		if(this.fromApp != null)
			doc.put("fromApp",this.fromApp);
		
		return doc;
	}
	
	@Override
	public InctShops clone() throws CloneNotSupportedException {
		InctShops newObj = new InctShops();
		newObj.siteCode = this.siteCode;
		newObj.pCode = this.pCode;
		newObj.tg = this.tg;
		newObj.mcgb = this.mcgb;
		newObj.cwgb = this.cwgb;
		newObj.td = this.td;
		newObj.visitCnt = this.visitCnt;
		newObj.cycleEndBanner = this.cycleEndBanner;
		newObj.cycleEndIco = this.cycleEndIco;
		newObj.cycleEndSky = this.cycleEndSky;
		
		newObj.setWaiteTime(getWaitTime());
		
		newObj.setFromApp(this.fromApp);
		return newObj;
	}
	
	@Override
	public String getKey() {
		// ex) 2f7f159a8b8fcf23d9fa43ebbc73b57a^45666^GHI
		return String.format("%s%s%s%s%s", StringUtils.defaultString(siteCode), DELIMETER
														   , StringUtils.defaultString(pCode), DELIMETER, StringUtils.defaultString(cwgb));
	}
	
	@Override
	public void modValue(Object element, boolean bAppendValue) {
		if(!(element instanceof InctShops))		return;
		
		InctShops obj = (InctShops)element;
		if (bAppendValue) {
			this.tg = unionAlpha(this.tg, obj.tg);
			this.mcgb = unionAlpha(this.mcgb, obj.mcgb);
			if(obj.visitCounging) {
				this.visitCnt += obj.visitCnt;
			}
		} else {
			this.tg = obj.tg;
			this.mcgb = obj.mcgb;
			this.visitCnt = obj.visitCnt;
		}
		if(StringUtils.isNotEmpty(obj.td)) {
			this.td = obj.td;
		}
		if(obj.getWaitTime() > 0) {
			this.setWaiteTime(obj.getWaitTime());
		}
		
//		if(obj.modCycleEndList != null) {
//			for(Map.Entry<String, Boolean> entry : obj.modCycleEndList.entrySet()) {
//				switch (entry.getKey()) {
//				case KEY_CYCLE_END_BANNER:	this.cycleEndBanner = entry.getValue(); break;
//				case KEY_CYCLE_END_ICO:		this.cycleEndIco = entry.getValue(); break;
//				case KEY_CYCLE_END_SKY:		this.cycleEndSky = entry.getValue(); break;
//				default:				break;
//				}				
//			}
//		}
		
		this.cycleEndBanner = obj.cycleEndBanner;
		this.cycleEndIco = obj.cycleEndIco;
		this.cycleEndSky = obj.cycleEndSky;
		
		this.fromApp = obj.fromApp;
	}


	/** String Utils(MStringUtils 구성시에 분리)  **/
	private String unionAlpha(String str1, String str2) {
		if(str1 == null && str2 == null)		return null;
		if(str1 == null)							return str2;
		if(str2 == null)							return str1;
		StringBuffer buf = new StringBuffer(str1);
		char[] addChars = str2.toCharArray();
		for (char ch : addChars) {
			if(str1.contains(String.valueOf(ch)))		continue;
	
			buf.append(ch);
		}
		char[] outChars = buf.toString().toCharArray();
		return String.copyValueOf(outChars);
	}
}
