package com.adgather.user.inclinations;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adgather.beans.ManagementCookie;
import com.adgather.constants.GlobalConstants;
import com.adgather.user.TestTimeChecker;
import com.adgather.user.auth.UserAuthIDResult;
import com.adgather.user.data.mongodb.ConsumerInclinationsMDao;
import com.adgather.user.inclinations.cookiedef.CookieDefRepository;
import com.adgather.user.inclinations.cookiedef.inter.CookieDef;
import com.adgather.user.inclinations.cookiedef.inter.RefactCookieDef;
import com.adgather.user.inclinations.cookielist.SyncList;
import com.adgather.user.inclinations.cookielist.factory.SyncListFactory;
import com.adgather.user.inclinations.cookielist.merge.SyncListMerger;
import com.adgather.user.inclinations.cookieval.inct.InctShops;
import com.adgather.user.inclinations.cookieval.inct.ctr.InctShopsCtr;
import com.adgather.user.inclinations.memory.MemoryObj;
import com.adgather.user.inclinations.memory.SimulMemoryObj;
import com.adgather.user.inclinations.status.RefactStatus;
import com.adgather.user.inclinations.status.SampleAGroupStatus;
import com.adgather.user.inclinations.status.SampleBGroupStatus;
import com.adgather.user.inclinations.status.SampleCGroupStatus;
import com.adgather.user.inclinations.status.SampleDGroupStatus;
//import com.adgather.util.CommonUtil;
import com.adgather.util.ErrorLog;
import com.adgather.util.PropertyHandler;
import com.mongodb.MongoExecutionTimeoutException;
import com.mongodb.MongoSocketReadTimeoutException;
import com.mongodb.MongoWriteException;


public class ConsumerInclinations extends CookieDefRepository {
	protected final static Logger logger = LoggerFactory.getLogger(ConsumerInclinations.class);
	
	private static final String[] DOC_KEY_DELs = {"shoplog_cnt"};	// 몽고디비 저장 정보 삭제 처리(설정후 35일후 완전 삭제됨)
	
	private static final String DOC_KEY_SAVE_CNT = "saveCnt";		// 몽고 저장 개수 수치화 키
	
	/** 소비자 정보  *******************************************************************/
	private RefactStatus refactStatus = new RefactStatus();
	private SampleAGroupStatus sampleAGroupStatus = new SampleAGroupStatus();
	private SampleBGroupStatus sampleBGroupStatus = new SampleBGroupStatus();
	private SampleCGroupStatus sampleCGroupStatus = new SampleCGroupStatus();
	private SampleDGroupStatus sampleDGroupStatus = new SampleDGroupStatus();

	// 현 요청의 기준 시간(동기화시간, waitTime시간 처리에 이용)
	private final long curTime = new Date().getTime();
	
	/** 소비자 고유키 관리 **/
	private String consumerKey;
	private String delConsumerKey;
	
	/** 소비자 성향/프리퀀시 정보 (데이터 변경상항 적용지; cookie와 mongo데이터 merge된 데이터, merge된 데이터는  개수 제한 없음) **/
	@SuppressWarnings("rawtypes")
	private Map<String, MemoryObj> inclinationMemorys = new HashMap<>();
	private Set<String> mongoDelKeys = new HashSet<>();
	
	/** 몽고DB 접속 실패(한번 실패이면 다른 것도 시도하지 않는다.; 응답시간 짧게 처리) **/
	private boolean bFailMongo;
	
	private boolean bAddUpdateOfDomain = CIFunctionController.isAddUpdDateOfDomain();
	
	/** get/set method  *******************************************************************/
	public String getConsumerKey() {
		return consumerKey;
	}
	
	public long getCurTime() {
		return curTime;
	}
	
	public String getTestType(String testCase) {
		
		String sampleAgrouptype = sampleAGroupStatus.getTestType(testCase);
		if(StringUtils.isNotEmpty(sampleAgrouptype)) {
			return sampleAgrouptype;
		}
		
		String sampleBgrouptype = sampleBGroupStatus.getTestType(testCase);
		if(StringUtils.isNotEmpty(sampleBgrouptype)) {
			return sampleBgrouptype;
		}
		
		String sampleCgrouptype = sampleCGroupStatus.getTestType(testCase);
		if(StringUtils.isNotEmpty(sampleCgrouptype)) {
			return sampleCgrouptype;
		}
		
		String sampleDgrouptype = sampleDGroupStatus.getTestType(testCase);
		if(StringUtils.isNotEmpty(sampleDgrouptype)) {
			return sampleDgrouptype;
		}
		
		return refactStatus.getTestType(testCase);
	}
	
	public void setDevice(boolean isMobile, HttpServletRequest req) {
		refactStatus.setDevice(isMobile ? "M" : "P");
		if(req == null)	return;
		
		boolean bChangedDomain = StringUtils.contains(req.getRequestURL(), GlobalConstants.MEDIACATEGORY_DOMAIN);
		refactStatus.setbChangedDomain(bChangedDomain ? "Y" : null);
	}
	
	public <E> List<E> getCookie(String cookieName) {
		if(cookieName == null)		return null;
		
		@SuppressWarnings("unchecked")
		MemoryObj<E> memoryObj = inclinationMemorys.get(cookieName);
		if(memoryObj == null)		return null;
		
		return memoryObj.getServiceData();
	}
	
	public <E> List<E> getSaveCookie(String cookieName, int maxCnt) {
		if(cookieName == null)		return null;

		@SuppressWarnings("unchecked")
		MemoryObj<E> memoryObj = inclinationMemorys.get(cookieName);
		if(memoryObj == null)		return null;
		
		return memoryObj.getSaveSyncList(maxCnt);
	}
	
	public <E> long getLastUpdTime(String cookieName) {
		if(cookieName == null)		return 0;

		@SuppressWarnings("unchecked")
		MemoryObj<E> memoryObj = inclinationMemorys.get(cookieName);
		if(memoryObj == null)		return 0;
		
		return memoryObj.getLastUpdTime();
	}
	
	public <E> List<E> getSaveCookie(String cookieName) {
		if(cookieName == null)		return null;
		
		return getSimulSaveCookie(cookieName, 20);
//		@SuppressWarnings("unchecked")
//		MemoryObj<E> memoryObj = inclinationMemorys.get(cookieName);
//		if(memoryObj == null)		return null;
//		
//		return memoryObj.getSaveSyncList(memoryObj.getCookieMaxCnt());
	}
	
	private <E> List<E> getSimulSaveCookie(String cookieName, int maxCnt) {
		if(cookieName == null)		return null;
		
		@SuppressWarnings("unchecked")
		MemoryObj<E> memoryObj = inclinationMemorys.get(cookieName);
		if(memoryObj == null)		return null;
		
		if(memoryObj instanceof SimulMemoryObj) {
			return ((SimulMemoryObj<E>)memoryObj).getSaveCookieListData(maxCnt);
		} else {
			return memoryObj.getSaveSyncList(maxCnt);
		}
	}
	
	/** 값의 추가(기존에 있을 경우 기존것 변경, 변경시 키가 아닌값 append 여부)**/
	@SuppressWarnings("unchecked")
	public <E> void addCookie(String cookieName, E obj, boolean bAppendValue) {
		if(cookieName == null)	return;
		if(obj == null)				return;
		
		MemoryObj<E> memoryObj = inclinationMemorys.get(cookieName);
		if(memoryObj == null) {		// 초기 로드되지 않은 대상
//			String loadCookieName = getLoadCookieName(cookieName); // 변환전 쿠키 샵로그 확인(변환 후는 변환된것으로 반환)
			String loadCookieName = getLoadCookieNameNAbTest(cookieName, refactStatus); // 변환전 쿠키 샵로그 확인(변환 후는 변환된것으로 반환)
			_loadEmptyData(loadCookieName);
			
			memoryObj = inclinationMemorys.get(cookieName);
		}
		if(memoryObj == null) 	return;
		
		memoryObj.applyAdd(obj, bAppendValue);
	}
	
	/** 값의 변경(기존에 없을 경우 추가하지 않음, 변경시 키가 아닌값 append 여부)**/
	public <E> void modCookie(String cookieName, E obj, boolean bAppendValue) {
		if(cookieName == null)	return;
		if(obj == null)				return;
		
		@SuppressWarnings("unchecked")
		MemoryObj<E> memoryObj = inclinationMemorys.get(cookieName);
		if(memoryObj == null)		return;
		
		memoryObj.applyMod(obj, bAppendValue);
	}
	
	public <E> void delCookie(String cookieName, E obj) {
		if(cookieName == null)	return;
		if(obj == null)				return;
		
		@SuppressWarnings("unchecked")
		MemoryObj<E> memoryObj = inclinationMemorys.get(cookieName);
		if(memoryObj == null)		return;
		
		memoryObj.applyDel(obj);
	}

	/** 데이터 로딩 관련메소드  ***************************************************************/
	/** 전체 쿠키 로딩(설정된 대상의 전체) **/
	public boolean load(HttpServletRequest req, String keyIp, UserAuthIDResult userAuthIDResult)  {
		return load(req, keyIp, userAuthIDResult, -1);
	}

	public boolean load(HttpServletRequest req, String keyIp, UserAuthIDResult userAuthIDResult, String mediaScriptNo)  {
		int iMediaScriptNo = NumberUtils.toInt(mediaScriptNo, -1);

		return load(req, keyIp, userAuthIDResult, iMediaScriptNo); 
	}
	public boolean load(HttpServletRequest req, String keyIp, UserAuthIDResult userAuthIDResult, int mediaScriptNo)  {
		if(req == null)		return false;
		TestTimeChecker timeChecker = new TestTimeChecker();
		timeChecker.testStart();
		boolean bRes = false;

		try {
			setDevice(true, req);
			
			if(PropertyHandler.isTrue("AUID_USE")) {
				if(userAuthIDResult != null) {
					refactStatus.setRefacting(userAuthIDResult.getNewAuId());		// 변환 상태 설정(AB테스트 포함)
					bRes ^= _load(req, userAuthIDResult.getNowAuId(), userAuthIDResult.getNewAuId(), mediaScriptNo);
				} else {
					bRes ^= false;		//auid사용 인데 auid가 없는 경우 몽고조회하지 않음.
				}
			} else {
				refactStatus.setRefacting(keyIp);		// 변환 상태 설정(AB테스트 포함)
				bRes ^= _load(req, keyIp, mediaScriptNo);
			}	
		} catch (Exception e) {
			logger.error(ErrorLog.getStack(e));
		}

		timeChecker.testEnd("LOAD A");
		return bRes;
	}
	
	public boolean loadOnlyParam(Map<String, String> paramMap) {
		if(paramMap == null)		return false;
		
		boolean bRes = false;
		bRes ^= _loadParameter(paramMap);
		return bRes;
	}
	
	private boolean _load(HttpServletRequest req, String consumerKey, int mediaScriptNo) {
		this.consumerKey = consumerKey;
		
		boolean bRes = _loadCookie(req);
		bRes ^= _loadMongo(mediaScriptNo);
		return bRes;
	}
	
	private boolean _load(HttpServletRequest req, String nowConsumerKey, String newConsumerKey, int mediaScriptNo) {
		TestTimeChecker timeChecker = new TestTimeChecker();
		timeChecker.testStart();
		
		this.consumerKey = newConsumerKey;
		boolean bRes = _loadCookie(req);
		timeChecker.testEnd("LOAD B1");
		
		if (StringUtils.isEmpty(nowConsumerKey) && StringUtils.isEmpty(newConsumerKey)) {
			return false;
		}
		
		if (StringUtils.isNotEmpty(nowConsumerKey) && StringUtils.isNotEmpty(newConsumerKey) 
				&& !StringUtils.equals(nowConsumerKey, newConsumerKey)) {
			// 기존/신규키가 다른경우
			this.delConsumerKey = nowConsumerKey;		
			bRes ^= _loadMongo(nowConsumerKey, newConsumerKey, mediaScriptNo);
		} else {
			// 둘중 하나
			bRes ^= _loadMongo(StringUtils.defaultIfEmpty(newConsumerKey, nowConsumerKey), mediaScriptNo);
		}

		timeChecker.testEnd("LOAD B2");
		return bRes;
	}
	
	
	private boolean _loadEmptyData(String cookieName) {
		boolean res = false;
		CookieDef cookieDef = null;
		
		if(defAbtestType.getCookieKey().equals(cookieName)) {
			cookieDef = defAbtestType;
		} else {
			cookieDef = getCookieDef(cookieName, bAddUpdateOfDomain);
		}
		
		if (cookieDef != null) {			
			@SuppressWarnings("rawtypes")
			Map<String, MemoryObj> map =_loadEmptyData(cookieName, cookieDef);
			inclinationMemorys.putAll(map);
			res ^= true;
		}
		return res;
	}

	@SuppressWarnings({ "rawtypes"})
	private <E> Map<String, MemoryObj> _loadEmptyData(String cookieName, CookieDef cookieDef) {
		if(cookieDef == null)			return null;
		
		Map<String, MemoryObj> memoryObjList = new HashMap<String, MemoryObj>();
		try {
			SyncList<E> cookieData = SyncListFactory.create(cookieDef, this.curTime); 

			cookieData.setNeedUpdateAll(true);
			
			MemoryObj<E> memoryObj = new MemoryObj<E>(cookieDef);
			memoryObj.setCookieData(cookieData);
			memoryObjList.put(cookieName, memoryObj);
			
			loadSimul(cookieDef, memoryObj);

		} catch (Exception e) {
			logger.error(ErrorLog.getStack(e));
		}

		return memoryObjList;
	}
	
	
	private boolean _loadCookie(HttpServletRequest req) {
		if(req == null)			return false;
		
		Cookie[] cookies = req.getCookies();
		if(cookies == null)	return false;
		
		_loadCookieAbtestType(cookies);
				
		//general cookie
		boolean res = false;
		for (Cookie cookie : cookies) {
			CookieDef cookieDef = getCookieDef(cookie.getName(), bAddUpdateOfDomain);
			if (cookieDef != null) {			
				@SuppressWarnings("rawtypes")
				Map<String, MemoryObj> map =_loadCookie(cookie.getName(), cookie.getValue(), cookieDef);
				if(map == null) continue;
					
				inclinationMemorys.putAll(map);
				res ^= true;
			}
		}
		return res;
	}
	
	private void _loadCookieAbtestType(Cookie[] cookies) {
		//precheck cookie
		for (Cookie cookie : cookies) {
			if(defAbtestType == null)	break;
			if(!defAbtestType.getCookieKey().equals(cookie.getName()))	continue;
			
			
			CookieDef cookieDef = defAbtestType;
			if (cookieDef != null) {			
				@SuppressWarnings("rawtypes")
				Map<String, MemoryObj> map =_loadCookie(cookie.getName(), cookie.getValue(), cookieDef);
				inclinationMemorys.putAll(map);
				break;
			}
			if("clientAbTestType".equals(cookie.getName())) {
				/** 클라이언트 AB테스트  쿠키가 들어올 경우 처리**/
			}
		}
		this.sampleAGroupStatus.setSampleAGroupABTest(this);
		this.sampleBGroupStatus.setSampleBGroupABTest(this);
		this.sampleCGroupStatus.setSampleCGroupABTest(this);
		this.sampleDGroupStatus.setSampleDGroupABTest(this);
	}
	
	private boolean _loadParameter(Map<String, String> paramMap) {
		if(paramMap == null)		return false;
		
		boolean res = false;
		for(Map.Entry<String, String> entry : paramMap.entrySet()) {
			// auid에 의한 키값 변경이 필요하다.
			// ab테스트에 따라 변경이 필요하다.
			
			
			CookieDef cookieDef = getCookieDef(entry.getKey(), bAddUpdateOfDomain);
			if (cookieDef != null) {			
				@SuppressWarnings("rawtypes")
				Map<String, MemoryObj> map =_loadCookie(entry.getKey(), entry.getValue(), cookieDef);
				if(map == null) continue;
					
				inclinationMemorys.putAll(map);
				res ^= true;
			}
		} 
		return res;
	}

	/** 개별 쿠키값 로딩 **/
	@SuppressWarnings({"rawtypes" })
	private <E> Map<String, MemoryObj> _loadCookie(String cookieName, String cookieValue, CookieDef cookieDef) {
		if(StringUtils.isEmpty(cookieValue))		return null;
		if(cookieDef == null)			return null;
		
		Map<String, MemoryObj> memoryObjList = new HashMap<String, MemoryObj>();
		try {
			SyncList<E> cookieData = SyncListFactory.create(cookieDef, this.curTime);

			cookieData.setLoadCookie();	//몽고 데이터가 로딩 되지 않았기 때문에 몽고저장필요 사항으로 설정(몽고 데이터가 없는 경우를 위함)
			cookieData.setCookieValue(cookieValue, cookieDef);
			
			MemoryObj<E> memoryObj = new MemoryObj<E>(cookieDef);
			memoryObj.setCookieData(cookieData);
			memoryObjList.put(cookieName, memoryObj);
			
			loadSimul(cookieDef, memoryObj);
			
		} catch (Exception e) {
			// 개별 쿠키의 형식이 맞지 않는 경우 로그처리 하지 않음. 안정화 시기에는 유지.
			// logger.error(ErrorLog.getStack(e));
		}

		return memoryObjList;
	}
	
	/** 전체 몽고 데이터 로딩(쿠키 선택된 데이터) **/

	private <E> boolean _loadMongo(int mediaScriptNo) {
		return _loadMongo(this.consumerKey, mediaScriptNo);
	}
	private <E> boolean _loadMongo(String nowConsumerKey, int mediaScriptNo) {
		TestTimeChecker timeChecker = new TestTimeChecker();
		timeChecker.testStart();
		
		// 소비자 성향 데이터 몽고 사용 여부
		if(CIFunctionController.isNotUseMongoStorage(refactStatus.getDevice())) {
			return false;
		}
		
		if(consumerKey == null)		return false;
		Document doc = null;
		try {
			if(!bFailMongo) {
				doc = ConsumerInclinationsMDao.get(nowConsumerKey);
			}
		} catch (MongoSocketReadTimeoutException e) {
			bFailMongo = true;
		} catch (MongoExecutionTimeoutException e) {
			bFailMongo = true;
		} catch (Exception e) {
			bFailMongo = true;
			logger.error(ErrorLog.getStack(e));
		}
		timeChecker.testEnd("LOAD C1");
		
		boolean b = loadMergeMongo(doc, mediaScriptNo);
		timeChecker.testEnd("LOAD C2");
		return b;
	}
	
	public <E> boolean _loadMongo(String nowConsumerKey, String newConsumerKey, int mediaScriptNo) {
		// 소비자 성향 데이터 몽고 사용 여부
		if(CIFunctionController.isNotUseMongoStorage(refactStatus.getDevice())) {
			return false;
		}
		Map<String, Document> docMap = null;
		try {
			if(!bFailMongo) {
				docMap = ConsumerInclinationsMDao.gets(new String[]{nowConsumerKey, newConsumerKey});	
			}
		} catch (MongoSocketReadTimeoutException e) {
			bFailMongo = true;
		} catch (MongoExecutionTimeoutException e) {
			bFailMongo = true;
		} catch (Exception e) {
			bFailMongo = true;
			logger.error(ErrorLog.getStack(e));
		}
		
		if(MapUtils.isEmpty(docMap)) {
			return true;
		}
		boolean bRes = false;
		bRes ^= loadMergeMongo(docMap.get(nowConsumerKey), mediaScriptNo);
		
		Document newDoc = docMap.get(newConsumerKey);
		bRes ^= loadAppendMongo(newDoc, mediaScriptNo);

		return bRes;
	}
	
	/** 몽고데이터를 기존 것의 차이를 반영해서 로딩(현재것에는 있고 예전것에는 없으면 추가, 현재것은 없고 예전것은 있으면 삭제, 현재것과 예전것이 같이 있으면 현재것으로 덮어씌우기) **/
	public <E> boolean loadMergeMongo(Document doc, int mediaScriptNo) {
		if(doc == null)		return false;

		_loadMongoAbtestType(doc, mediaScriptNo);

		boolean res = false;
		// 신규 정의된 경우
		for (Map.Entry<String, CookieDef> entry : getCookieDefMap(bAddUpdateOfDomain).entrySet()) {
			// 몽고 사용하지 않는 값을 merge처리 제외
			if(!entry.getValue().isUseMongo(mediaScriptNo)) {
				continue;
			}
			
			@SuppressWarnings("unchecked")
			MemoryObj<E> memoryObj = inclinationMemorys.get(entry.getKey());
			SyncList<E> cookieObj = memoryObj == null ? null : memoryObj.getCookieData();
			SyncList<E> mergedObj = _loadMergeMongoData(entry.getKey(), doc, entry.getValue(), cookieObj);
			
			// simul(대행자) 이면 제외
			if(memoryObj != null && memoryObj instanceof SimulMemoryObj) {
				continue;				// simulated memory object는 제외
			}
			
			if (mergedObj == null && memoryObj != null) {
				inclinationMemorys.remove(entry.getKey());
				res ^= false;
				
			} else if(mergedObj != null && memoryObj == null) {
				memoryObj = new MemoryObj<E>(entry.getValue());
				memoryObj.setMongoData(mergedObj);
				inclinationMemorys.put(entry.getKey(), memoryObj);
				res ^= true;
				
				loadSimul(entry.getValue(), memoryObj);
				
			} else if (mergedObj != null && memoryObj != null) {
				memoryObj.setMongoData(mergedObj);
				res ^= true;
				
			} else {
				res ^= false;
			}
		}
		
		// 삭제해야할 몽고키 
		_loadMongoDelKeys(doc);
		
		return res;
	}
	
	/** 몽고데이터를 기존 것과 합치 형태로 로딩 **/
	public <E> boolean loadAppendMongo(Document doc, int mediaScriptNo) {
		if(doc == null)		return false;

		_loadMongoAbtestType(doc, mediaScriptNo);
		
		boolean res = false;
		// 신규 정의된 경우
		for (Map.Entry<String, CookieDef> entry : getCookieDefMap(bAddUpdateOfDomain).entrySet()) {
			// 몽고 사용하지 않는 값을 merge처리 제외
			if(!entry.getValue().isUseMongo(mediaScriptNo)) {
				continue;
			}
			
			@SuppressWarnings("unchecked")
			MemoryObj<E> memoryObj = inclinationMemorys.get(entry.getKey());
			SyncList<E> cookieObj = memoryObj == null ? null : memoryObj.getCookieData();
			SyncList<E> appendObj = _loadAppendMongoData(entry.getKey(), doc, entry.getValue(), cookieObj);
			
			if(memoryObj != null && memoryObj instanceof SimulMemoryObj) {
				continue;				// simulated memory object는 제외
			}
			
			if (appendObj == null && memoryObj != null) {
				inclinationMemorys.remove(entry.getKey());
				res ^= false;
				
			} else if(appendObj != null && memoryObj == null) {
				memoryObj = new MemoryObj<E>(entry.getValue());
				memoryObj.setMongoData(appendObj);
				inclinationMemorys.put(entry.getKey(), memoryObj);
				res ^= true;
				
				loadSimul(entry.getValue(), memoryObj);
				
			} else if (appendObj != null && memoryObj != null) {
				memoryObj.setMongoData(appendObj);
				res ^= true;
				
			} else {
				res ^= false;
			}
		}

		// 삭제해야할 몽고키 
		_loadMongoDelKeys(doc);
		
		return res;
	}
	
	private <E> SyncList<E> _loadMergeMongoData(String cookieName, Document mongoDoc, CookieDef cookieDef, SyncList<E> cookieObj) {
		SyncList<E> mergedObj = null;
		try {
			SyncList<E> mongoObj = null;
			String mongoKey = cookieDef.getMongoKey();
			if (mongoDoc.containsKey(mongoKey)) {
				mongoObj = SyncListFactory.create(cookieDef, this.curTime);
				mongoObj.setLoadMongo();			// 쿠키와 합치기 임으로 쿠키저장필요 사항으로 설정
				try {
					mongoObj.setMongoValue(mongoDoc.get(mongoKey), cookieDef);
				} catch(Exception se) {
					mongoObj = null;		//지원되지 않는 형태나 null인경우 몽고 데이터는 없는 것으로 처리
				}
			}
			mergedObj =  SyncListMerger.merge(cookieDef, cookieObj, mongoObj, this.curTime);
		} catch (Exception e) {
			logger.error(ErrorLog.getStack(e));	/** [ETC SRC] yhlim 20170823 **/	//주석 잠깜 품 yhlim 20170823 
		}
		return mergedObj;
	}
	
	private <E> SyncList<E> _loadAppendMongoData(String cookieName, Document mongoDoc, CookieDef cookieDef, SyncList<E> cookieObj) {
		SyncList<E> mergedObj = null;
		try {
			SyncList<E> mongoObj = null;
			String mongoKey = cookieDef.getMongoKey();
			if (mongoDoc.containsKey(mongoKey)) {
				mongoObj = SyncListFactory.create(cookieDef, this.curTime);						// syncTime 설정하지 않음.
				mongoObj.setLoadMongo();			// 쿠키와 합치기 임으로 쿠키저장필요 사항으로 설정
				try {
					mongoObj.setMongoValue(mongoDoc.get(mongoKey), cookieDef);
				} catch(Exception se) {
					mongoObj = null;		//지원되지 않는 형태나 null인경우 몽고 데이터는 없는 것으로 처리
				}
			}
			mergedObj =  SyncListMerger.append(cookieDef, cookieObj, mongoObj, this.curTime);
		} catch (Exception e) {
			logger.error(ErrorLog.getStack(e)); 	/** [ETC SRC] yhlim 20170823 **/	//주석 잠깜 품 yhlim 20170823 
		}
		return mergedObj;
	}
	
	private <E> void _loadMongoAbtestType(Document doc, int mediaScriptNo) {
		// abtestType 로드
		if(defAbtestType != null && defAbtestType.isUseMongo(mediaScriptNo)) {
			@SuppressWarnings("unchecked")
			MemoryObj<E> memoryObj = inclinationMemorys.get(defAbtestType.getCookieKey());
			SyncList<E> cookieObj = memoryObj == null ? null : memoryObj.getCookieData();
			SyncList<E> mongoObj = null;
			
			if (doc != null && doc.containsKey(defAbtestType.getMongoKey())) {
				try {
					mongoObj = SyncListFactory.create(defAbtestType, this.curTime);
					mongoObj.setLoadMongo();			// 쿠키와 합치기 임으로 쿠키저장필요 사항으로 설정
					mongoObj.setMongoValue(doc.get(defAbtestType.getMongoKey()), defAbtestType);
				} catch(Exception se) {
					mongoObj = null;		//지원되지 않는 형태나 null인경우 몽고 데이터는 없는 것으로 처리
				}
			}
			
			SyncList<E> selectObj = null;
			if (cookieObj != null && mongoObj != null) {
				if(cookieObj.getSyncTime() < mongoObj.getSyncTime()) {		// ab테스트는 이전 상태를 찾는다.
					selectObj = cookieObj;
				} else {													
					selectObj = mongoObj;						
				}
			} else if (cookieObj != null && mongoObj == null) {
				selectObj = cookieObj;
			} else if (cookieObj == null && mongoObj != null) {
				selectObj = mongoObj;
			}
			
			if(selectObj != null) {
				memoryObj = new MemoryObj<E>(defAbtestType);
				memoryObj.setMongoData(selectObj);
				inclinationMemorys.put(defAbtestType.getCookieKey(), memoryObj);
			} 
			this.sampleAGroupStatus.setSampleAGroupABTest(this);
			this.sampleBGroupStatus.setSampleBGroupABTest(this);
			this.sampleCGroupStatus.setSampleCGroupABTest(this);
			this.sampleDGroupStatus.setSampleDGroupABTest(this);
		}
				
	}

	/** 데이터 저장 관련메소드  ***************************************************************/
	/** 쿠키에 저장(변경된 대상만) **/
	public boolean save(HttpServletResponse res) {
		return save(res, -1);
	}
	public boolean save(HttpServletResponse res, String mediaScriptNo) {
		int iMediaScriptNo = NumberUtils.toInt(mediaScriptNo, -1);
		
		return save(res, iMediaScriptNo);
	}
	public boolean save(HttpServletResponse res, int mediaScriptNo) {
		TestTimeChecker timeChecker = new TestTimeChecker();
		timeChecker.testStart();
		boolean bRes = false;
		try {
			bRes ^= saveCookie(res);		// 항상 cookie  이용하고  mongo 이용 예외 없음.
			bRes ^= saveMongo(mediaScriptNo);
		} catch (Exception e) {
			logger.error(ErrorLog.getStack(e));
		}
		timeChecker.testEnd("SAVE A");
		return bRes;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean saveCookie(HttpServletResponse res) {
		boolean bForceSave = StringUtils.isNotEmpty(this.delConsumerKey);
		
		for (Map.Entry<String, MemoryObj> entry : inclinationMemorys.entrySet()) {
			MemoryObj memory = entry.getValue();
			if(bForceSave) {
				entry.getValue().setNeedUpdateAll();
			}
			
			if(memory != null && memory instanceof SimulMemoryObj) {
				continue;				// simulated memory object는 제외
			}
//			System.out.println("memory.isModifedCookie()="+memory.isModifedCookie()+", memory.isModifedMongo()="+memory.isModifedMongo());
			if(memory.isModifedCookie() && memory.isModifedMongo()) {
				memory.setSyncTime(this.curTime);						// 동기화 시간 설정
			}
			_saveCookie(res, entry.getKey(), memory);
		}
		return true;
	}
	private <E> void _saveCookie(HttpServletResponse res, String cookieName, MemoryObj<E> memoryObj) {
		if(cookieName == null)		return;
		if(memoryObj == null)		return;
		
		if(logger.isDebugEnabled()) logger.debug(String.format("_saveCookie["+cookieName+"] isModifedCookie[" + memoryObj.isModifedCookie() + "] is ModifiedMongo[" + memoryObj.isModifedMongo() + "]"));

		CookieDef cookieDef = memoryObj.getCookieDef();
		if(!cookieDef.isUseCookie())	return;
		
		int expire = cookieDef.getExpire();
		// 변환 진행
//원본	if(cookieDef instanceof RefactCookieDef && ((RefactCookieDef)cookieDef).isRefacting()) {	// 일반 refact 객체의 설정값이용
		if(cookieDef instanceof RefactCookieDef && refactStatus.isRefacting(cookieName)) {			// ab테스트 설정이 포함된 설정값 이용(임시사용)
			Map<String, String> cookieValueMap = memoryObj.getSaveRefactCookieData();
			if(cookieValueMap == null)		return;
			
			for (Map.Entry<String, String> entry : cookieValueMap.entrySet()) {
				ManagementCookie.makeCookie(entry.getKey(), entry.getValue(), expire, res);
			}
			ManagementCookie.deleteCookie(cookieName, res);
		// 쿠키 데이터가 변경 되었을 경우
		} else if (memoryObj.isModifedCookie()) {
			String cookieVal = memoryObj.getSaveCookieData();
			if(StringUtils.isEmpty(cookieVal)) {
				ManagementCookie.deleteCookie(cookieName, res);
			} else {
				ManagementCookie.makeCookie(cookieName, cookieVal, expire, res);
			}
		}
	}
	
	/** 몽고에 저장(변경된 대상만) **/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean saveMongo(int mediaScriptNo) {
		TestTimeChecker timeChecker = new TestTimeChecker();
		timeChecker.testStart();
		// 소비자 성향 데이터 몽고 사용 여부
		if(CIFunctionController.isNotUseMongoStorage(refactStatus.getDevice())) {
			return false;
		}
		
		if(CIFunctionController.isNotWriteMongo()) {
			return false;
		}
		
		Document setDoc = new Document();
		Document unsetDoc = new Document();

		for (Map.Entry<String, MemoryObj> entry : inclinationMemorys.entrySet()) {
			if(!entry.getValue().getCookieDef().isUseMongo(mediaScriptNo)) {
				continue;
			}
			if(entry.getValue() != null && entry.getValue() instanceof SimulMemoryObj) {
				// simulated memory object는 제외
				// 변경 사항 Mongo Doc에 반영하지 않음.
				
				// 저장되 개수 카운트
				((SimulMemoryObj)entry.getValue()).setSavedMongoCnt();
				//map에 카운트 정보 저장(저장여부와 관계없이 수집)
				setMongoSavedCnt(entry.getKey(), entry.getValue().getSavedMongoCnt());
				
			} else {
				// 변경 사항 Mongo Doc에 반영
				_setMongoDoc(setDoc, unsetDoc, entry.getKey(), entry.getValue());
			}

			timeChecker.testEnd("SAVE B 1 [" + entry.getValue() + "]" );
		}
		
		saveMongoCnt(setDoc); //몽고 개수 정보 저장
		
		_saveMongoDelKeys(unsetDoc);

		try {
			if(!bFailMongo) {
				ConsumerInclinationsMDao.set(consumerKey, setDoc, unsetDoc);
				if(StringUtils.isNotEmpty(this.delConsumerKey)) {
					ConsumerInclinationsMDao.del(this.delConsumerKey);
				}
			}
		} catch (MongoSocketReadTimeoutException e) {
			
			bFailMongo = true;
			e.printStackTrace();
		} catch (MongoExecutionTimeoutException e) {
			bFailMongo = true;
			e.printStackTrace();
		} catch (MongoWriteException e) {
			bFailMongo = true;
			e.printStackTrace();
//			if(StringUtils.contains(e.getMessage(), "E11000 duplicate key error collection")) {
//			} else {
//				logger.error(ErrorLog.getStack(e));
//			}
		} catch (Exception e) {
			bFailMongo = true;
//			logger.error(ErrorLog.getStack(e));
			e.printStackTrace();
		}
		
		timeChecker.testEnd("SAVE B");
		return true;
	}
	private <E> void _setMongoDoc(Document setDoc, Document unsetDoc, String cookieName, MemoryObj<E> memoryObj) {
		if(setDoc == null)			return;
		if(unsetDoc == null)			return;
		if(cookieName == null)		return;
		if(memoryObj == null)		return;
		
		CookieDef cookieDef = memoryObj.getCookieDef();
		// 변환 요청시
//원본	if(cookieDef instanceof RefactCookieDef && ((RefactCookieDef)cookieDef).isRefacting()) {	// 일반 refact 객체의 설정값이용
		if(cookieDef instanceof RefactCookieDef && refactStatus.isRefacting(cookieName)) {			// ab테스트 설정이 포함된 설정값 이용(임시사용)
			Map<String, Object> mongoValueMap = memoryObj.getSaveRefactMongoData();
			if(mongoValueMap == null)		return; 
			
			setDoc.putAll(mongoValueMap);
			unsetDoc.append(cookieDef.getMongoKey(), "");
			
			for (Map.Entry<String, Object> entry : mongoValueMap.entrySet()) {
				if(!(entry.getValue() instanceof List))	continue;
				
				setMongoSavedCnt(entry.getKey(), ((List)entry.getValue()).size());
			}
			
		// 몽고적용 데이터가 변경 되었을 경우
		} else if (memoryObj.isModifedMongo()) {
			//setDoc.put(cookieName, memoryObj.getSaveMongoData());
			
			String mongoKey = memoryObj.getCookieDef().getMongoKey();
			if(StringUtils.isEmpty(mongoKey)) {
				return;
			}
			
			setDoc.put(mongoKey, memoryObj.getSaveMongoData());

			//map에 카운트 정보 저장(저장여부와 관계없이 수집)
			setMongoSavedCnt(cookieName, memoryObj.getSavedMongoCnt());
		}
	}
	
	private void loadSimul(CookieDef cookieDef, MemoryObj<?> memoryObj) {
		if(!(cookieDef instanceof RefactCookieDef))		return;

		RefactCookieDef refactCookieDef = (RefactCookieDef)cookieDef;
		Map<String, SimulMemoryObj> simulMap = refactCookieDef.createSimulMemoryObj(memoryObj);
		if(simulMap != null) {
			inclinationMemorys.putAll(simulMap);
		}
	}

	/** 삭제할키   ***************************************************************/
	
	private void _loadMongoDelKeys(Document doc) {
		if(DOC_KEY_DELs == null)	return;
		
		// 삭제해야할 몽고키 
		for(String key : DOC_KEY_DELs) {
			if(doc.containsKey(key)) {
				mongoDelKeys.add(key);
			}
		}
	}
	
	private void _saveMongoDelKeys(Document unsetDoc) {
		if(mongoDelKeys == null || mongoDelKeys.size() == 0)	return;
		
		for(String key : mongoDelKeys) {
			unsetDoc.append(key, "");
		}
	}
	
	

	/** 기타 기능(중요도 낮음)  ***************************************************************/
	@SuppressWarnings("rawtypes")
	public int getShopLogCnt() {
		int cnt = 0;
		List cwShopLog = getCookie(INCT_CW);
		if(cwShopLog != null) 	cnt += cwShopLog.size();
		
		List srShopLog = getCookie(INCT_SR);
		if(srShopLog != null) 	cnt += srShopLog.size();
		
		List rcShopLog = getCookie(INCT_RC);
		if(rcShopLog != null) 	cnt += rcShopLog.size();
		
		List spShopLog = getCookie(INCT_SP);
		if(spShopLog != null) 	cnt += spShopLog.size();

		return cnt;
	}
	
	private final String INST_SHOPS_CNT_SUFFIX = "Cnt";
	private Map<String, Integer> mongoSavedCntMap = new HashMap<String, Integer>();
	private void setMongoSavedCnt(String cookieName, int cnt) {
		if(StringUtils.isEmpty(cookieName)) return;
		if(cnt < 0)		return;			// 저장되지 않은 값을 설정하지 않음.
		
		switch(cookieName) {
		case INCT_CW:
		case INCT_SR:
		case INCT_RC:
		case INCT_SP:
		case INCT_HU:
		case INCT_HU_CONV:
		case INCT_HU_SHORTCUT:
		case INCT_UM:
		case INCT_KL:
			mongoSavedCntMap.put(cookieName + INST_SHOPS_CNT_SUFFIX, cnt);
			break;
		}
	}
	
	@SuppressWarnings("unused")
	private void saveMongoCnt(Document setDoc) {
		// 변경사항이 없으면 개수 정보를 저장하지 않는다.
		if(setDoc == null || setDoc.size() == 0)	return;
		
		Document subDoc = new Document();
		if(mongoSavedCntMap != null) {
			for (Map.Entry<String, Integer> entry : mongoSavedCntMap.entrySet()) {
				setDoc.put(DOC_KEY_SAVE_CNT+ "." + entry.getKey(), entry.getValue());		// 저장된 값만 부분적용처리
			}
		}
		// setDoc.append(DOC_KEY_SAVE_CNT + "." + "type", refactStatus.isRefacting(SHOP_LOG) ? "NEW" : "OLD");
		setDoc.append(DOC_KEY_SAVE_CNT + "." + "device", refactStatus.getDevice());
		if(StringUtils.isNotEmpty(refactStatus.getbChangedDomain())) {
			setDoc.append(DOC_KEY_SAVE_CNT + "." + "changedDomain", refactStatus.getbChangedDomain());
		}
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getKakaoSaveShops() {
		final int maxCnt = CIFunctionController.getKakaoShopsCnt();
		final float perCw =  CIFunctionController.getKakaoPerCw();
		List resList = new ArrayList();
//		List afterList = new ArrayList();
		
		List cwShopLog = getSimulSaveCookie(INCT_CW, maxCnt);
		List srShopLog = getSimulSaveCookie(INCT_SR, maxCnt);
		List rcShopLog = getSimulSaveCookie(INCT_RC, maxCnt);
		List spShopLog = getSimulSaveCookie(INCT_SP, maxCnt);
		
		int cwCnt = cwShopLog == null ? 0 : cwShopLog.size();
		int srCnt = srShopLog == null ? 0 : srShopLog.size();
		int rcCnt = rcShopLog == null ? 0 : rcShopLog.size();
		int spCnt = spShopLog == null ? 0 : spShopLog.size();
		
		int cwMax = (int)(maxCnt * perCw);
		final int srMax = maxCnt;
		int rcMax = 5;
		int spMax = 5;
		
		int cwIdx = 0;
		int srIdx = 0;
		int rcIdx = 0;
		int spIdx = 0;

//		String typeAB = getTestType(FreqBannerABTest.TEST_NAME);
		
		for(; resList.size() < maxCnt && cwIdx < cwMax && cwIdx < cwCnt; cwIdx++) {
			if (InctShopsCtr.isEndFreqTg((InctShops)cwShopLog.get(cwIdx), GlobalConstants.CW, GlobalConstants.BANNER)) {
				continue;
//			} else if ((FreqABTest.TYPE_B.equals(typeAB) || FreqABTest.TYPE_C.equals(typeAB)) && InctShopsCtr.removeMaxUnlimitedFreqShopLog(this,GlobalConstants.CW,(InctShops)cwShopLog.get(cwIdx))) {
//				afterList.add(cwShopLog.get(cwIdx));
//				continue;
			} else {
				resList.add(cwShopLog.get(cwIdx));
			}
		}
		
		for(; resList.size() < maxCnt && srIdx < srMax && srIdx < srCnt; srIdx++) {
			if (InctShopsCtr.isEndFreqTg((InctShops)srShopLog.get(srIdx), GlobalConstants.SR, GlobalConstants.BANNER)) {
				continue;
//			} else if ((FreqABTest.TYPE_B.equals(typeAB) || FreqABTest.TYPE_C.equals(typeAB)) && InctShopsCtr.removeMaxUnlimitedFreqShopLog(this,GlobalConstants.SR,(InctShops)srShopLog.get(srIdx))) {
//				afterList.add(srShopLog.get(srIdx));
//				continue;
			} else {
				resList.add(srShopLog.get(srIdx));
			}
		}
		
		for(; resList.size() < maxCnt && rcIdx < rcMax && rcIdx < rcCnt; rcIdx++) {
 			if (InctShopsCtr.isEndFreqTg((InctShops)rcShopLog.get(rcIdx), GlobalConstants.RC, GlobalConstants.BANNER)) {
				continue;
			} else {
				resList.add(rcShopLog.get(rcIdx));
			}
		}
		
		for(; resList.size() < maxCnt && spIdx < spMax && spIdx < spCnt; spIdx++) {
			if (InctShopsCtr.isEndFreqTg((InctShops)spShopLog.get(spIdx), GlobalConstants.SP, GlobalConstants.BANNER)) {
				continue;
			} else {
				resList.add(spShopLog.get(spIdx));
			}
		}
		
//		if(afterList.size() > 0) {
//			resList.addAll(afterList);
//		}
		
		if(resList.size() >= maxCnt) {	// 중간 갯수 확인
			return resList;
		}
		
		cwMax = maxCnt;	// 모자를 경우 cw 15개 더 추가
		for(; resList.size() < maxCnt && cwIdx < cwMax && cwIdx < cwCnt; cwIdx++) {
			resList.add(cwShopLog.get(cwIdx));
		}
		
		rcMax = maxCnt;	// 모자를 경우 rc 15개 더 추가
		for(; resList.size() < maxCnt && rcIdx < rcMax && rcIdx < rcCnt; rcIdx++) {
			resList.add(rcShopLog.get(rcIdx));
		}

		spMax = maxCnt;	// 모자를 경우 sp 15개 더 추가
		for(; resList.size() < maxCnt && spIdx < spMax && spIdx < spCnt; spIdx++) {
			resList.add(spShopLog.get(spIdx));
		}
		return resList;
	}
}
