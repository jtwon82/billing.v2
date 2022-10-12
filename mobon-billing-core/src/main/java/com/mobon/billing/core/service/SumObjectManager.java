package com.mobon.billing.core.service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.mobon.billing.model.ActionData;
import com.mobon.billing.model.v15.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.util.ConsumerFileUtils;

import net.sf.json.JSONObject;

@Service
public class SumObjectManager {

	private static final Logger logger = LoggerFactory.getLogger(SumObjectManager.class);

	private static SumObjectManager INSTANCE = null;

	@Value("${log.path}")
	private String	logPath;
	@Value("${summery.list.size}")
	private int	summeryListSize;

	
	public SumObjectManager() {
		logger.debug(">> SumObjectManager init ");
		init();
	}

	public static SumObjectManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SumObjectManager();
		}
		return INSTANCE;
	}

	public synchronized Map<String, ?> removeObjectMap(Object obj) {
		Map<String, ?> result = null;
		
//		if(obj instanceof BaseCVData) {
//			result = this.mapBaseCVData;
//			mapBaseCVData = (new ConcurrentHashMap<String, BaseCVData>());
//			
//		} else 
		if(obj instanceof ExternalInfoData) {
			result = this.mapExternalInfoData;
			mapExternalInfoData = (new ConcurrentHashMap<String, ExternalInfoData>());
			
		} else if(obj instanceof ShopStatsInfoData) {
			result = this.mapShopStatsInfoData;
			mapShopStatsInfoData = (new ConcurrentHashMap<String, ShopStatsInfoData>());
			
		} else if(obj instanceof ShopInfoData) {
			result = this.mapShopInfoData;
			mapShopInfoData = (new ConcurrentHashMap<String, ShopInfoData>());

		} else {
			logger.debug("else {}", obj);
		}
		return result;
	}
	
	public synchronized Map<String, ?> removeClickViewObjectMap(Object obj) {
		Map<String, ?> result = null;
		
		if(obj instanceof BaseCVData) {
			result = this.mapBaseCVData;
			mapBaseCVData = (new ConcurrentHashMap<String, BaseCVData>());
			
		} else {
			logger.debug("else {}", obj);
		}
		return result;
	}

	public synchronized Map<String, ?> removeADBlockObjectMap(Object obj) {
		Map<String, ?> result = null;

		if(obj instanceof BaseCVData) {
			result = this.mapADBlockData;
			mapADBlockData = (new ConcurrentHashMap<String, BaseCVData>());

		} else {
			logger.debug("else {}", obj);
		}
		return result;
	}

	public synchronized Map<String, ?> removeCrossAUIDObjectMap(Object obj) {
		Map<String, ?> result = null;

		if(obj instanceof BaseCVData) {
			result = this.mapCrossAUIDData;
			mapCrossAUIDData = (new ConcurrentHashMap<String, BaseCVData>());

		} else {
			logger.debug("else {}", obj);
		}
		return result;
	}

	public synchronized Map<String, ?> removeOpenrtbObjectMap() {
		Map<String, ?> result = null;
		
		result = this.mapOpenrtbData;
		mapOpenrtbData = (new ConcurrentHashMap<String, BaseCVData>());

		return result;
	}
	
	public synchronized Map<String, ?> removeClientEnvirmentObjectMap(Object obj) {
		Map<String, ?> result = null;
		
		if(obj instanceof BaseCVData) {
			result = this.mapClientEnvirmentData;
			mapClientEnvirmentData = (new ConcurrentHashMap<String, BaseCVData>());
			
		} else {
			logger.debug("else {}", obj);
		}
		return result;
	}
	public synchronized Map<String, ?> removeAdverClientEnvirmentObjectMap(Object obj) {
		Map<String, ?> result = null;
		
		if(obj instanceof BaseCVData) {
			result = this.mapAdverClientEnvirmentData;
			mapAdverClientEnvirmentData = (new ConcurrentHashMap<String, BaseCVData>());
			
		} else {
			logger.debug("else {}", obj);
		}
		return result;
	}
	
	public synchronized Map<String, ?> removeCampMediaRetrnAvalObjectMap(Object obj) {
		Map<String, ?> result = null;
		
		if(obj instanceof BaseCVData) {
			result = this.mapCampMediaRetrnAvalData;
			mapCampMediaRetrnAvalData = (new ConcurrentHashMap<String, BaseCVData>());
			
		} else {
			logger.debug("else {}", obj);
		}
		return result;
	}
	
	
	public synchronized Map<String, ?> removeClientAgeGenderObjectMap(Object obj) {
		Map<String, ?> result = null;
		
		if(obj instanceof BaseCVData) {
			result = this.mapClientAgeGenderData;
			mapClientAgeGenderData = (new ConcurrentHashMap<String, BaseCVData>());
			
		} else {
			logger.debug("else {}", obj);
		}
		return result;
	}
	
	public synchronized Map<String, ?> removeClickViewPcodeDataObjectMap() {
		Map<String, ?> result = null;

		result = this.mapClickViewPcodeData;
		this.mapClickViewPcodeData = (new ConcurrentHashMap<String, BaseCVData>());
		
		return result;
	}
	
	public synchronized Map<String, ?> removeViewPcodeDataObjectMap() {
		Map<String, ?> result = null;

		result = this.mapViewPcodeData;
		this.mapViewPcodeData = (new ConcurrentHashMap<String, BaseCVData>());
		
		return result;
	}
	
	public synchronized Map<String, ?> removePhoneData() {
		Map<String, ?> result = null;
			result = this.mapPhoneData;
			mapPhoneData = (new ConcurrentHashMap<String, BaseCVData>());
		return result;
	}
	
	public synchronized Map<String, ?> removePointObjectMap(Object obj) {
		Map<String, ?> result = null;
		
		if(obj instanceof BaseCVData) {
			result = this.mapBaseCVPointData;
			mapBaseCVPointData = (new ConcurrentHashMap<String, BaseCVData>());
			
		} else {
			logger.debug("else {}", obj);
		}
		return result;
	}

	public synchronized Map<String, ?> removePoint2ObjectMap(Object obj) {
		Map<String, ?> result = null;
		
		if(obj instanceof BaseCVData) {
			result = this.mapBaseCVPoint2Data;
			mapBaseCVPoint2Data = (new ConcurrentHashMap<String, BaseCVData>());
			
		} else {
			logger.debug("else {}", obj);
		}
		return result;
	}
	
	public synchronized Map<String, ?> removeShopInfoMdPcodeMap() {
		Map<String, ?> result = null;
		result = this.mapShopInfoMdPcodeData;
		mapShopInfoMdPcodeData = (new ConcurrentHashMap<String, ShopInfoData>());
		return result;
	}
	
	public synchronized Map<String, ?> removeMediaChrgMap() {
		Map<String, ?> result = null;
		result = this.mapMediaChrgData;
		mapMediaChrgData = (new ConcurrentHashMap<String, BaseCVData>());
		return result;
	}
	
	public synchronized Map<String, ?> removeParGatrMap() {
		Map<String, ?> result = null;
		result = this.mapParGatrData;
		mapParGatrData = (new ConcurrentHashMap<String, BaseCVData>());
		return result;
	}
	
	public synchronized Map<String, ?> removeIntgCntrMap() {
		Map<String, ?> result = null;
		result = this.mapIntgCntrData;
		mapIntgCntrData = (new ConcurrentHashMap<String, BaseCVData>());
		return result;
	}
	
	public synchronized Map<String, ?> removeIntgCntrKgrMap() {
		Map<String, ?> result = null;
		result = this.mapIntgCntrKgrData;
		mapIntgCntrKgrData = (new ConcurrentHashMap<String, BaseCVData>());
		return result;
	}
	
	public synchronized Map<String, ?> removeIntgCntrUMMap() {
		Map<String, ?> result = null;
		result = this.mapIntgCntrUMData;
		mapIntgCntrUMData = (new ConcurrentHashMap<String, BaseCVData>());
		return result;
	}
	
	public synchronized Map<String, ?> removeIntgCntrTtimeMap() {
		Map<String, ?> result = null;
		result = this.mapIntgCntrTtimeData;
		mapIntgCntrTtimeData = (new ConcurrentHashMap<String, BaseCVData>());
		return result;
	}
	
	public synchronized Map<String, ?> removeNearData() {
		Map<String, ?> result = null;
		result = this.mapNearData;
		mapNearData = (new ConcurrentHashMap<String, NearData>());
		return result;
	}
	
	public synchronized Map<String, ?> removeAppTargetData() {
		Map<String, ?> result = null;
		result = this.mapAppTargetData;
		mapAppTargetData = (new ConcurrentHashMap<String, AppTargetData>());
		return result;
	}
	
	public synchronized List<?> removeIntgCntrConvData() {
		List<?> result = null;
		result = this.listIntgCntrConvData;
		listIntgCntrConvData = Collections.synchronizedList(new ArrayList<ConvData>());
		return result;
	}
	
	public synchronized List<?> removeConvPcodeData() {
		List<?> result = null;
		result = this.listConvPcodeData;
		listConvPcodeData = Collections.synchronizedList(new ArrayList<ConvData>());
		return result;
	}
	
	public synchronized List<?> removeConvAllData() {
		List<?> result = null;
		result = this.listConvAllData;
		listConvAllData = Collections.synchronizedList(new ArrayList<ConvData>());
		return result;
	}
	
	public synchronized List<?> removeConvAbusingData() {
		List<?> result = null;
		result = this.listConvAbusingData;
		listConvAbusingData = Collections.synchronizedList(new ArrayList<ConvData>());
		return result;
	}
	
	public synchronized List<?> removeIntgCntrActionLogData() {
		List<?> result = null;
		result = this.listIntgCntrActionLogData;
		listIntgCntrActionLogData = Collections.synchronizedList(new ArrayList<ActionLogData>());
		return result;
	}
	
	public synchronized List<?> removeActionPcodeLogData() {
		List<?> result = null;
		result = this.listActionPcodeLogData;
		listActionPcodeLogData = Collections.synchronizedList(new ArrayList<ActionLogData>());
		return result;
	}
	
	public List<?> removeObjectList(Object obj) {
		List<?> result = null;
		
		if(obj instanceof ConvData) {
			synchronized(listConvData) {
				result = this.listConvData;
				listConvData = Collections.synchronizedList(new ArrayList<ConvData>());
			}
			
		} else if(obj instanceof ActionLogData) {
			synchronized(listActionLogData) {
				result = this.listActionLogData;
				listActionLogData = Collections.synchronizedList(new ArrayList<ActionLogData>());
			}
		} else if (obj instanceof ChrgLogData) {
			synchronized(listChrgLogData) {
				result = this.listChrgLogData;
				listChrgLogData = Collections.synchronizedList(new ArrayList<ChrgLogData>());
			}
		}else if (obj instanceof PluscallLogData) {
			synchronized(listPluscallLogData) {
				result = this.listPluscallLogData;
				listPluscallLogData = Collections.synchronizedList(new ArrayList<PluscallLogData>());
			}
		}
		return result;
	}
	
	//유니크 클릭 
	public Map<String, ?> removeClickUniqueMap(BaseCVData baseCVData) {
		Map<String, ?> result = null;
		result = this.mapClickUniqueData;
		mapClickUniqueData = (new ConcurrentHashMap<String, BaseCVData>());
		return result;
	}
	
	public synchronized List<?> removeContributeConvData() {
		List<?> result = null;
		result  = this.listContributeConvData;
		listContributeConvData = Collections.synchronizedList(new ArrayList<ConvData>());
		
		return result;
	}
	
	// Ai 캠페인 적재 로직
	public synchronized Map<String, ?> removeAiData() {
		Map<String, ?> result = null;
		result = this.mapAiData;
		mapAiData = (new ConcurrentHashMap<String, BaseCVData>());
		return result;
	}

	//추천 알고리즘 ABTest[노출 클릭]
	public synchronized Map<String ,?> removeABPcodeRecomData(){
		Map<String ,?> result = null;
		result = this.mapABPcodeRecomData;
		mapABPcodeRecomData = new ConcurrentHashMap<String , BaseCVData>();

		return result;
	}
	//추천 알고리즘 ABTest[전환]
	public synchronized List<?> removeListABPcodeConvData(){
		List<?> result = null;
		result = this.listABPcodeConvData;
		listABPcodeConvData = Collections.synchronizedList(new ArrayList<ConvData>());
		return result;
	}
	//추천 알고리즘 ABTest[Action_log]
	public synchronized  List<?> removeActionABPcodeData(){
		List <?> result = null;
		result = this.listActionABPcodeData;
		listActionABPcodeData = Collections.synchronizedList(new ArrayList<ActionLogData>());
		return result;
	}
 	//actionLog 데이터 통합
	public synchronized  List<?> removeActionRenewLogData(){
		List<?> result = null;
		result = this.listActionRenewLogData;
		listActionRenewLogData = Collections.synchronizedList(new ArrayList<ActionLogData>());
		return result;
	}

	//장바구니 이력 데이터
	public synchronized List<?> removeBasketData(BasketData basketData) {
		List<?> result = null;
		result = this.listBasketData;
		listBasketData = Collections.synchronizedList(new ArrayList<BasketData>());
		return result;
	}

	public SumObjectManager(SumObjectManager obj) {
		this.mapBaseCVData = obj.mapBaseCVData;
		this.mapADBlockData = obj.mapADBlockData;
		this.mapCrossAUIDData = obj.mapCrossAUIDData;
		this.mapOpenrtbData= obj.mapOpenrtbData;
		this.mapClickViewPcodeData = obj.mapClickViewPcodeData;
		this.mapViewPcodeData = obj.mapViewPcodeData;
		this.mapClientEnvirmentData = obj.mapClientEnvirmentData;
		this.mapAdverClientEnvirmentData = obj.mapAdverClientEnvirmentData;
		this.mapClientAgeGenderData = obj.mapClientAgeGenderData;
		this.mapPhoneData = obj.mapPhoneData;
		this.mapBaseCVPointData = obj.mapBaseCVPointData;
		this.mapMediaChrgData = obj.mapMediaChrgData;
		this.mapParGatrData = obj.mapParGatrData;
		this.mapIntgCntrData = obj.mapIntgCntrData;
		this.mapIntgCntrKgrData = obj.mapIntgCntrKgrData;
		this.mapIntgCntrUMData = obj.mapIntgCntrUMData;
		this.mapIntgCntrTtimeData = obj.mapIntgCntrTtimeData;
		this.mapNearData = obj.mapNearData;
		this.mapAppTargetData = obj.mapAppTargetData;
		this.mapExternalInfoData = obj.mapExternalInfoData;
		this.mapShopInfoData = obj.mapShopInfoData;
		this.mapShopInfoMdPcodeData = obj.mapShopInfoMdPcodeData;
		this.mapShopStatsInfoData = obj.mapShopStatsInfoData;
		this.listConvData = obj.listConvData;
		this.listConvAllData = obj.listConvAllData;
		this.listConvAbusingData = obj.listConvAbusingData;
		this.listIntgCntrConvData = obj.listIntgCntrConvData;
		this.listConvPcodeData = obj.listConvPcodeData;
		this.listActionLogData = obj.listActionLogData;
		this.listActionPcodeLogData = obj.listActionPcodeLogData;
		this.listIntgCntrActionLogData = obj.listIntgCntrActionLogData;
		
		//유니크 
		this.mapClickUniqueData = obj.mapClickUniqueData;
		this.listContributeConvData = obj.listContributeConvData;

		//추천 알고리즘 abtest[노출 , 클릭]
		this.mapABPcodeRecomData = obj.mapABPcodeRecomData;
		//추천 알고리즘 abtest[전환]
		this.listABPcodeConvData = obj.listABPcodeConvData;
		//추천 앍고리즘 abTest[ACTION_LOG]
		this.listActionABPcodeData = obj.listActionABPcodeData;

		//actionLog 통합
		this.listActionRenewLogData = obj.listActionRenewLogData;

		//장바구니 이력 데이터
		this.listBasketData = obj.listBasketData;
	}

	public void init() {
		mapBaseCVData = (new ConcurrentHashMap<String, BaseCVData>());
		mapADBlockData = (new ConcurrentHashMap<String, BaseCVData>());
		mapCrossAUIDData = (new ConcurrentHashMap<String, BaseCVData>());
		mapOpenrtbData= (new ConcurrentHashMap<String, BaseCVData>());
		mapClickViewPcodeData = (new ConcurrentHashMap<String, BaseCVData>());
		mapViewPcodeData = (new ConcurrentHashMap<String, BaseCVData>());
		mapClientEnvirmentData = (new ConcurrentHashMap<String, BaseCVData>());
		mapAdverClientEnvirmentData = (new ConcurrentHashMap<String, BaseCVData>());
		mapClientAgeGenderData = (new ConcurrentHashMap<String, BaseCVData>());
		mapPhoneData = (new ConcurrentHashMap<String, BaseCVData>());
		mapBaseCVPointData = (new ConcurrentHashMap<String, BaseCVData>());
		mapMediaChrgData = (new ConcurrentHashMap<String, BaseCVData>());
		mapParGatrData = (new ConcurrentHashMap<String, BaseCVData>());
		mapIntgCntrData = (new ConcurrentHashMap<String, BaseCVData>());
		mapIntgCntrKgrData = (new ConcurrentHashMap<String, BaseCVData>());
		mapIntgCntrUMData = (new ConcurrentHashMap<String, BaseCVData>());
		mapIntgCntrTtimeData = (new ConcurrentHashMap<String, BaseCVData>());
		mapNearData = (new ConcurrentHashMap<String, NearData>());
		mapAppTargetData = (new ConcurrentHashMap<String, AppTargetData>());
		mapExternalInfoData = (new ConcurrentHashMap<String, ExternalInfoData>());
		mapShopStatsInfoData = (new ConcurrentHashMap<String, ShopStatsInfoData>());
		mapShopInfoData = (new ConcurrentHashMap<String, ShopInfoData>());
		mapShopInfoMdPcodeData = (new ConcurrentHashMap<String, ShopInfoData>());
		listConvData = Collections.synchronizedList(new ArrayList<ConvData>());
		listConvAllData = Collections.synchronizedList(new ArrayList<ConvData>());
		listConvAbusingData = Collections.synchronizedList(new ArrayList<ConvData>());
		listIntgCntrConvData = Collections.synchronizedList(new ArrayList<ConvData>());
		listConvPcodeData = Collections.synchronizedList(new ArrayList<ConvData>());
		listActionLogData = Collections.synchronizedList(new ArrayList<ActionLogData>());
		listActionPcodeLogData = Collections.synchronizedList(new ArrayList<ActionLogData>());
		listIntgCntrActionLogData = Collections.synchronizedList(new ArrayList<ActionLogData>());
		
		//유니크 
		mapClickUniqueData = (new ConcurrentHashMap<String , BaseCVData>());
		listContributeConvData = Collections.synchronizedList(new ArrayList<ConvData>());

		//추천 알고리즘 abtest [노출,클릭]
		mapABPcodeRecomData = (new ConcurrentHashMap<String , BaseCVData>());
		//추천 알고리즘 abtest [전환]
		listABPcodeConvData = Collections.synchronizedList(new ArrayList<ConvData>());
		//추천 알고리즘 abtest [Action_log]
		listActionABPcodeData = Collections.synchronizedList(new ArrayList<ActionLogData>());

		//ACTION_log 통합
		listActionRenewLogData = Collections.synchronizedList(new ArrayList<ActionLogData>());
		//장바구니 이력 데이터
		listBasketData = Collections.synchronizedList(new ArrayList<BasketData>());
	}

	public void appendData(Object r, boolean bool) {
		if (r instanceof BaseCVData) {
			//appendData((BaseCVData) r);
			
		} else if (r instanceof ExternalInfoData) {
			appendData((ExternalInfoData) r);
			
//		} else if (r instanceof ShopInfoData) {
//			appendData((ShopInfoData) r);
//			
//		} else if (r instanceof ShopStatsInfoData) {
//			appendData((ShopStatsInfoData) r);
			
		} else if (r instanceof ActionLogData) {
			appendData((ActionLogData) r);
			
		} else if (r instanceof ConvData) {
			appendData((ConvData) r);
			
		} else {
			logger.debug("sumObjectManager else - {}", JSONObject.fromObject(r));
		}
	}
	
	public void appendPointData(Object r, boolean bool) {
		if (r instanceof BaseCVData) {
			appendPointData((BaseCVData) r);
		} else {
			logger.debug("sumObjectManager else - {}", JSONObject.fromObject(r));
		}
	}

	public void appendMdPcodeData(ShopInfoData record) {
		synchronized(mapShopInfoMdPcodeData) {
			if( mapShopInfoMdPcodeData.size()>summeryListSize ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				JSONObject jJSONObject = JSONObject.fromObject(record);
				
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.shopMdPcode_info, jJSONObject);
				}catch(Exception e) {				}
			} else {
				record.generateKey();
				ShopInfoData sum = mapShopInfoMdPcodeData.get(record.getKeyCodeMdPcode());
				if (sum == null) {
					mapShopInfoMdPcodeData.put(record.getKeyCodeMdPcode(), record);
				} else {
					sum.sumGethering(record);
				}
			}
		}
	}

	public synchronized void appendClickViewData(BaseCVData record) {
		if( mapBaseCVData.size()>summeryListSize ) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.click_view, jJSONObject);
			}catch(Exception e) {				}
		} else {

//			BaseCVData recordVo = SerializationUtils.clone(record);
//			// 키구조를 코드로 미리변환해서 처리한다.
//			if( !StringUtils.isNumeric(recordVo.getAdGubun()) ) recordVo.setAdGubun(G.convertTP_CODE(recordVo.getAdGubun()));
//			if( !StringUtils.isNumeric(recordVo.getPlatform()) ) recordVo.setPlatform(G.convertPLATFORM_CODE(recordVo.getPlatform()));
//			if( !StringUtils.isNumeric(recordVo.getProduct()) ) recordVo.setProduct(G.convertPRDT_CODE(recordVo.getProduct()));
//			BaseCVData sum = mapBaseCVData.get(recordVo.generateKey());
//			if (sum == null) {
//				mapBaseCVData.put(recordVo.generateKey(), recordVo);
//			} else {
//				sum.sumGethering(recordVo);
//			}

			BaseCVData sum = mapBaseCVData.get(record.getKeyCodeClickView());
			if (sum == null) {
				mapBaseCVData.put(record.getKeyCodeClickView(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}

	//유니크 클릭 데이터 
	public synchronized  void appendClickUniquekData(BaseCVData record) {
		if (mapClickUniqueData.size() > summeryListSize) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.Unique_Click, jJSONObject);
			}catch(Exception e) {				}
		} else {
			BaseCVData sum = mapClickUniqueData.get(record.getKeyCodeClickUnique());
			if (sum == null) {
				mapClickUniqueData.put(record.getKeyCodeClickUnique(), record);
			} else {
				sum.sumGethering(record);
			}
		}
		
	}

	public synchronized void appendADBlockData(BaseCVData record) {
		if( mapADBlockData.size()>summeryListSize ) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);

			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.click_view_adblock, jJSONObject);
			}catch(Exception e) {				}
		} else {

			BaseCVData sum = mapADBlockData.get(record.getKeyCodeADBlock());
			if (sum == null) {
				mapADBlockData.put(record.getKeyCodeADBlock(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}

	public synchronized void appendCrossAUIDData(BaseCVData record) {
		if( mapCrossAUIDData.size()>summeryListSize ) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);

			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.click_view_crossAUID, jJSONObject);
			}catch(Exception e) {				}
		} else {

			BaseCVData sum = mapCrossAUIDData.get(record.getKeyCodeCrossAUID());
			if (sum == null) {
				mapCrossAUIDData.put(record.getKeyCodeCrossAUID(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}

	public synchronized void appendOpenrtbData(BaseCVData record) {
		if( mapOpenrtbData.size()>summeryListSize ) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.click_view_openrtb, jJSONObject);
			}catch(Exception e) {				}
		} else {
			BaseCVData sum = mapOpenrtbData.get(record.getKeyCodeOpenrtb());
			if (sum == null) {
				mapOpenrtbData.put(record.getKeyCodeOpenrtb(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}
	
	public synchronized void appendClientEnvironmentData(BaseCVData record) {
		if( mapClientEnvirmentData.size()>summeryListSize ) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.client_environment, jJSONObject);
			}catch(Exception e) {				}
		} else {
			record.generateKey();
			BaseCVData sum = mapClientEnvirmentData.get(record.getKeyClientEnvironment());
			if (sum == null) {
				mapClientEnvirmentData.put(record.getKeyClientEnvironment(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}
	
	public synchronized void appendCampMediaRetrnAvalData(BaseCVData record) {
		if( mapCampMediaRetrnAvalData.size()>summeryListSize ) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.camp_media_retrn_aval, jJSONObject);
			}catch(Exception e) {				}
		} else {
			record.generateKey();
			BaseCVData sum = mapCampMediaRetrnAvalData.get(record.getKeyCampMediaRetrnStats());
			if (sum == null) {
				mapCampMediaRetrnAvalData.put(record.getKeyCampMediaRetrnStats(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}
	
	
	public synchronized void appendAdverClientEnvironmentData(BaseCVData record) {
		if( mapAdverClientEnvirmentData.size()>summeryListSize ) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.adver_client_environment, jJSONObject);
			}catch(Exception e) {				}
		} else {
			record.generateKey();
			BaseCVData sum = mapAdverClientEnvirmentData.get(record.getKeyAdverClientEnvironment());
			if (sum == null) {
				mapAdverClientEnvirmentData.put(record.getKeyAdverClientEnvironment(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}
	
	public synchronized void appendClientAgeGenderData(BaseCVData record) {
		if( mapClientAgeGenderData.size()>summeryListSize ) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.client_age_gender, jJSONObject);
			}catch(Exception e) {				}
		} else {
			record.generateKey();
			BaseCVData sum = mapClientAgeGenderData.get(record.getKeyClientAgeGender());
			if (sum == null) {
				mapClientAgeGenderData.put(record.getKeyClientAgeGender(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}
	
	public synchronized void appendClickViewPcodeData(BaseCVData record) {
		if( mapClickViewPcodeData.size()>summeryListSize ) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.click_view_pcode, jJSONObject);
			}catch(Exception e) {				}
		} else {
			record.generateKey();
			BaseCVData sum = mapClickViewPcodeData.get(record.getKeyCodeClickViewPcode());
			if (sum == null) {
				mapClickViewPcodeData.put(record.getKeyCodeClickViewPcode(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}
	
	public synchronized void appendViewPcodeData(BaseCVData record) {
		if( mapViewPcodeData.size()>summeryListSize ) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.view_pcode, jJSONObject);
			}catch(Exception e) {				}
		} else {
			record.generateKey();
			BaseCVData sum = mapViewPcodeData.get(record.getKeyCodeViewPcode());
			if (sum == null) {
				mapViewPcodeData.put(record.getKeyCodeViewPcode(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}
	
	public synchronized void appendPhoneData(BaseCVData record) {
		if( mapPhoneData.size()>summeryListSize ) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.client_environment, jJSONObject);
			}catch(Exception e) {				}
		} else {
			record.generateKey();
			BaseCVData sum = mapPhoneData.get(record.getKeyPhone());
			if (sum == null) {
				mapPhoneData.put(record.getKeyPhone(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}

	private synchronized void appendPointData(BaseCVData record) {
		if( mapBaseCVPointData.size()>summeryListSize ) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.click_view_point, jJSONObject);
			}catch(Exception e) {				}
		} else {

			BaseCVData sum = mapBaseCVPointData.get(record.getKeyCodeClickView());
			if (sum == null) {
				mapBaseCVPointData.put(record.getKeyCodeClickView(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}

	public synchronized void appendPoint2Data(BaseCVData record) {
		if( mapBaseCVPoint2Data.size()>summeryListSize ) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.click_view, jJSONObject);
			}catch(Exception e) {				}
		} else {
			record.generateKey();
			BaseCVData sum = mapBaseCVPoint2Data.get(record.getKeyCodePoint2());
			if (sum == null) {
				mapBaseCVPoint2Data.put(record.getKeyCodePoint2(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}
	
	
	
	public synchronized void appendMediaChrgData(BaseCVData record) {
		if( mapMediaChrgData.size()>summeryListSize ) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.MediaChrgData, jJSONObject);
			}catch(Exception e) {				}
		} else {
			record.generateKey();
			BaseCVData sum = mapMediaChrgData.get(record.getKeyCodeMediaCharge());
			if (sum == null) {
				mapMediaChrgData.put(record.getKeyCodeMediaCharge(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}
	
	public synchronized void appendParGatrData(BaseCVData record) {
		if( mapParGatrData.size()>summeryListSize ) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.ParGatrData, jJSONObject);
			}catch(Exception e) {				}
		} else {
			record.generateKey();
			BaseCVData sum = mapParGatrData.get(record.getKeyCodeMediaCharge());
			if (sum == null) {
				mapParGatrData.put(record.getKeyCodeMediaCharge(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}
	
	public synchronized void appendIntgCntrData(BaseCVData record) {
		if( mapIntgCntrData.size()>summeryListSize ) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.IntgCntrData, jJSONObject);
			}catch(Exception e) {				}
		} else {
			record.generateKey();
			BaseCVData sum = mapIntgCntrData.get(record.getKeyCodeIntgCntr());
			if (sum == null) {
				mapIntgCntrData.put(record.getKeyCodeIntgCntr(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}
	
	public synchronized void appendIntgCntrKgrData(BaseCVData record) {
		if( mapIntgCntrKgrData.size()>summeryListSize ) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.IntgCntrKgrData, jJSONObject);
			}catch(Exception e) {				}
		} else {
			record.generateKey();
			BaseCVData sum = mapIntgCntrKgrData.get(record.getKeyCodeIntgCntrKgr());
			if (sum == null) {
				mapIntgCntrKgrData.put(record.getKeyCodeIntgCntrKgr(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}
	
	public synchronized void appendIntgCntrUMData(BaseCVData record) {
		if( mapIntgCntrUMData.size()>summeryListSize ) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.IntgCntrUMData, jJSONObject);
			}catch(Exception e) {				}
		} else {
			record.generateKey();
			BaseCVData sum = mapIntgCntrUMData.get(record.getKeyCodeIntgCntrUM());
			if (sum == null) {
				mapIntgCntrUMData.put(record.getKeyCodeIntgCntrUM(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}
	
	public synchronized void appendIntgCntrTtimeData(BaseCVData record) {
		if( mapIntgCntrTtimeData.size()>summeryListSize ) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.IntgCntrTtimeData, jJSONObject);
			}catch(Exception e) {				}
		} else {
			record.generateKey();
			BaseCVData sum = mapIntgCntrTtimeData.get(record.getKeyCodeIntgCntrTtime());
			if (sum == null) {
				mapIntgCntrTtimeData.put(record.getKeyCodeIntgCntrTtime(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}
	
	public synchronized void appendNearData(NearData record) {
		NearData sum = mapNearData.get(record.generateKey());
		if (sum == null) {
			mapNearData.put(record.generateKey(), record);
		} else {
			sum.sumGethering(record);
		}
	}
	
	public synchronized void appendAppTargetData(AppTargetData record) {
		AppTargetData sum = mapAppTargetData.get(record.generateKey());
		if (sum == null) {
			mapAppTargetData.put(record.generateKey(), record);
		} else {
			sum.sumGethering(record);
		}
	}

	private void appendData(ExternalInfoData record) {
		synchronized(mapExternalInfoData) {
			if( mapExternalInfoData.size()>summeryListSize ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				JSONObject jJSONObject = JSONObject.fromObject(record);
				
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.external_info, jJSONObject);
				}catch(Exception e) {				}
			} else {
				record.generateKey();
				ExternalInfoData sum = mapExternalInfoData.get(record.getKeyCodeExternal());
				if (sum == null) {
					mapExternalInfoData.put(record.getKeyCodeExternal(), record);
				} else {
					sum.sumGethering(record);
				}
			}
		}
	}

	public void appendShopStatsData(ShopStatsInfoData record) {
		synchronized(mapShopStatsInfoData) {
			if( mapShopStatsInfoData.size()>summeryListSize ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				JSONObject jJSONObject = JSONObject.fromObject(record);
				
				try {
					ConsumerFileUtils.writeLine( logPath +"retryfail/", writeFileName, G.shop_stats, jJSONObject);
				}catch(Exception e) {				}
			} else {
				ShopStatsInfoData sum = mapShopStatsInfoData.get(record.generateKey());
				if (sum == null) {
					mapShopStatsInfoData.put(record.generateKey(), record);
				} else {
					sum.sumGethering(record);
				}
			}
		}
	}

	public void appendShopInfoData(ShopInfoData record) {
		synchronized(mapShopInfoData) {
			if( mapShopInfoData.size()>summeryListSize ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				JSONObject jJSONObject = JSONObject.fromObject(record);
				
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.shop_info, jJSONObject);
				}catch(Exception e) {				}
			} else {
				if (!"".equals(record.getPnm().trim())) {
					ShopInfoData sum = mapShopInfoData.get(record.generateKey());
					if (sum == null) {
						mapShopInfoData.put(record.generateKey(), record);
					} else {
						sum.sumGethering(record);
					}
				}
			}
		}
	}

	private void appendData(ConvData record) {
		synchronized(listConvData) {
			if( listConvData.size()>summeryListSize ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				JSONObject jJSONObject = JSONObject.fromObject(record);
				
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.conv_info, jJSONObject);
				}catch(Exception e) {				}
			} else {
				if(listConvData == null) {
					listConvData = Collections.synchronizedList(new ArrayList<ConvData>());
				}
				// 애드트래커 중복
				for( ConvData row : listConvData ) {
					if(!row.getTrkTpCode().equals("90") && row.getConvKey().equals(record.getConvKey())) {
						row.setOrdQty(  Integer.toString(Integer.parseInt(row.getOrdQty()) + Integer.parseInt(record.getOrdQty())) );
						row.setPrice(  Integer.toString(Integer.parseInt(row.getPrice()) + Integer.parseInt(record.getPrice()))  );
						return;
					}
				}
				listConvData.add(record);
			}
		}
	}
	
	public void appendConvAllData(ConvData record) {
		synchronized(listConvAllData) {
			if( listConvAllData.size()>summeryListSize ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				JSONObject jJSONObject = JSONObject.fromObject(record);
				
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.convAll_info, jJSONObject);
				}catch(Exception e) {				}
			} else {
				if(listConvAllData == null) {
					listConvAllData = Collections.synchronizedList(new ArrayList<ConvData>());
				}
				listConvAllData.add(record);
			}
		}
	}
	
	public void appendConvAbusingData(ConvData record, String abusingTpCode) {
		synchronized(listConvAbusingData) {
			if(abusingTpCode!=null) {
				record.setCnvrsAbusingTpCode(abusingTpCode);
			}
			
			if( listConvAbusingData.size()>summeryListSize ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				JSONObject jJSONObject = JSONObject.fromObject(record);
				
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.convAbusing_info, jJSONObject);
				}catch(Exception e) {				}
			} else {
				if(listConvAbusingData == null) {
					listConvAbusingData = Collections.synchronizedList(new ArrayList<ConvData>());
				}
				listConvAbusingData.add(record);
			}
		}
	}
	
	public void appendIntgCntrConvData(ConvData record) {
		synchronized(listIntgCntrConvData) {
			if( listIntgCntrConvData.size()>summeryListSize ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				JSONObject jJSONObject = JSONObject.fromObject(record);
				
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.IntgCntrConv_info, jJSONObject);
				}catch(Exception e) {				}
			} else {
				if(listIntgCntrConvData == null) {
					listIntgCntrConvData = Collections.synchronizedList(new ArrayList<ConvData>());
				}
				listIntgCntrConvData.add(record);
			}
		}
	}
	
	public void appendConvPcodeData(ConvData record) {
		synchronized(listConvPcodeData) {
			if( listConvPcodeData.size()>summeryListSize ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				JSONObject jJSONObject = JSONObject.fromObject(record);
				
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.ConvPcode_info, jJSONObject);
				}catch(Exception e) {				}
			} else {
				if(listConvPcodeData == null) {
					listConvPcodeData = Collections.synchronizedList(new ArrayList<ConvData>());
				}
				listConvPcodeData.add(record);
			}
		}
	}

	private void appendData(ActionLogData record) {
		synchronized(listActionLogData) {
			if( listActionLogData.size()>summeryListSize ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				JSONObject jJSONObject = JSONObject.fromObject(record);
				
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.action_data, jJSONObject);
				}catch(Exception e) {				}
			} else {
				if (listActionLogData == null) {
					listActionLogData = Collections.synchronizedList(new ArrayList<ActionLogData>());
				}
				listActionLogData.add(record);
			}
		}
	}

	public void appendActionPcodeData(ActionLogData record) {
		synchronized(listActionPcodeLogData) {
			if( listActionPcodeLogData.size()>summeryListSize ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				JSONObject jJSONObject = JSONObject.fromObject(record);
				
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.action_pcode_data, jJSONObject);
				}catch(Exception e) {				}
			} else {
				if (listActionPcodeLogData == null) {
					listActionPcodeLogData = Collections.synchronizedList(new ArrayList<ActionLogData>());
				}
				listActionPcodeLogData.add(record);
			}
		}
	}
	
	public void appendIntgCntrActionLogData(ActionLogData record) {
		synchronized(listIntgCntrActionLogData) {
			if( listIntgCntrActionLogData.size()>summeryListSize ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				JSONObject jJSONObject = JSONObject.fromObject(record);
				
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.IntgCntrAction_data, jJSONObject);
				}catch(Exception e) {				}
			} else {
				if (listIntgCntrActionLogData == null) {
					listIntgCntrActionLogData = Collections.synchronizedList(new ArrayList<ActionLogData>());
				}
				listIntgCntrActionLogData.add(record);
			}
		}
	}
	//기여전환 적재 로직 
	public void appendContributeConversionData(ConvData record) {
		synchronized (listContributeConvData) {
			if (listContributeConvData.size() > summeryListSize) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				JSONObject jJSONObject = JSONObject.fromObject(record);
				
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.ContributeConvData, jJSONObject);
				} catch (Exception e) {
					
				}
			} else {
				if (listContributeConvData == null) {
					listContributeConvData = Collections.synchronizedList(new ArrayList<ConvData>());
				}
				listContributeConvData.add(record);
			}
		}
		
	}
	
	// 클릭프리컨시 적재 로직
	public void appendChrgLogData(ChrgLogData record) {
		synchronized (listChrgLogData) {
			if (listChrgLogData.size() > summeryListSize) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				JSONObject jJSONObject = JSONObject.fromObject(record);
				
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.ChrgLogData, jJSONObject);
				} catch (Exception e) {
					
				}
			} else {
				if (listChrgLogData == null) {
					listChrgLogData = Collections.synchronizedList(new ArrayList<ChrgLogData>());
					
				}
				listChrgLogData.add(record);
			}
		}
	}
	
	// Ai 캠페인 적재 로직
	public synchronized void appendAiData(BaseCVData record) {
		if( mapAiData.size()>summeryListSize ) {
			long millis = Calendar.getInstance().getTimeInMillis();
			String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			JSONObject jJSONObject = JSONObject.fromObject(record);
			
			try {
				ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.AiData, jJSONObject);
			}catch(Exception e) {				}
		} else {
			// record.generateKey();
			BaseCVData sum = mapAiData.get(record.getKeyCodeAi());
			if (sum == null) {
				mapAiData.put(record.getKeyCodeAi(), record);
			} else {
				sum.sumGethering(record);
			}
		}
	}
	
	// 플러스콜 유효콜 적재
	public void appendPluscallLogData(PluscallLogData record) {
		synchronized (listPluscallLogData) {
			if (listPluscallLogData.size() > summeryListSize) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				JSONObject jJSONObject = JSONObject.fromObject(record);
				
				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.PluscallLogData, jJSONObject);
				} catch (Exception e) {
					
				}
			} else {
				if (listPluscallLogData == null) {
					listPluscallLogData = Collections.synchronizedList(new ArrayList<PluscallLogData>());
					
				}
				listPluscallLogData.add(record);
			}
		}
	}

	//추천 알고리즘 AbTest 군 수집 [노출 클릭]
	public void appendABPcodeRecom(BaseCVData record) {
		synchronized(mapABPcodeRecomData) {
			if( mapABPcodeRecomData.size()>summeryListSize ) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				JSONObject jJSONObject = JSONObject.fromObject(record);

				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.ABPcodeRecomData, jJSONObject);
				}catch(Exception e) {				}
			} else {
				BaseCVData sum = mapABPcodeRecomData.get(record.getKeyCodeABPcodeRecom());
				if (sum == null) {
					mapABPcodeRecomData.put(record.getKeyCodeABPcodeRecom(), record);
				} else {
					sum.sumGethering(record);
				}
			}
		}
	}
	//추천 알고리즘 AbTest 군 수집 [전환]
	public void appendConvABPcodeRecom(ConvData record) {
		synchronized(listABPcodeConvData) {
			if (listABPcodeConvData.size() > summeryListSize) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				JSONObject jJSONObject = JSONObject.fromObject(record);

				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.ABPcodeRecomConvData, jJSONObject);
				} catch (Exception e) {

				}
			} else {
				if (listABPcodeConvData == null) {
					listABPcodeConvData = Collections.synchronizedList(new ArrayList<ConvData>());
				}
				listABPcodeConvData.add(record);
			}
		}
	}

	//추천 알고리즘 AbTest 군 수집 [ACTION_LOG]
	public void appendActionAbPcodeData(ActionLogData record) {
		synchronized(listActionABPcodeData) {
			if (listActionABPcodeData.size() > summeryListSize) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				JSONObject jJSONObject = JSONObject.fromObject(record);

				try {
					ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, G.ActionABPcodeData, jJSONObject);
				} catch (Exception e) {

				}
			} else {
				if (listActionABPcodeData == null) {
					listActionABPcodeData = Collections.synchronizedList(new ArrayList<ActionLogData>());
				}
				listActionABPcodeData.add(record);
			}
		}
	}

	public void appendActionRenewLog(ActionLogData record) {
	   synchronized (listActionRenewLogData) {
		   if (listActionRenewLogData.size() > summeryListSize)  {
			   long millis = Calendar.getInstance().getTimeInMillis();
			   String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
			   JSONObject jsonObject = JSONObject.fromObject(record);

			   try {
				   ConsumerFileUtils.writeLine(logPath+"retry/",writeFileName, G.ActionRenewLogData, jsonObject);
			   } catch (Exception e) {

			   }
		   } else {
			   if (listActionRenewLogData == null) {
				   listActionRenewLogData = Collections.synchronizedList(new ArrayList<ActionLogData>());
			   }
			   listActionRenewLogData.add(record);
		   }
	   }
	}

	// 장바구니 이력 데이터
	public void appendBasketData(BasketData record) {
		synchronized (listBasketData) {
			if (listBasketData.size() > summeryListSize) {
				long millis = Calendar.getInstance().getTimeInMillis();
				String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
				JSONObject jsonObject = JSONObject.fromObject(record);
				try {
					ConsumerFileUtils.writeLine(logPath+"retry/",writeFileName, G.BasketData, jsonObject);
				} catch (Exception e) {

				}
			} else  {
				if (listBasketData == null) {
					listBasketData = Collections.synchronizedList(new ArrayList<BasketData>());
				}
				listBasketData.add(record);
			}
		}
	}
	
	private Map<String, BaseCVData> mapBaseCVData = (new ConcurrentHashMap<String, BaseCVData>());
	private Map<String, BaseCVData> mapADBlockData = (new ConcurrentHashMap<String, BaseCVData>());
	private Map<String, BaseCVData> mapCrossAUIDData = (new ConcurrentHashMap<String, BaseCVData>());
	private Map<String, BaseCVData> mapOpenrtbData = (new ConcurrentHashMap<String, BaseCVData>());
	private Map<String, BaseCVData> mapClickViewPcodeData = (new ConcurrentHashMap<String, BaseCVData>());
	private Map<String, BaseCVData> mapViewPcodeData = (new ConcurrentHashMap<String, BaseCVData>());
	private Map<String, BaseCVData> mapClientEnvirmentData = (new ConcurrentHashMap<String, BaseCVData>());
	private Map<String, BaseCVData> mapAdverClientEnvirmentData = (new ConcurrentHashMap<String, BaseCVData>());
	private Map<String, BaseCVData> mapClientAgeGenderData = (new ConcurrentHashMap<String, BaseCVData>());
	private Map<String, BaseCVData> mapCampMediaRetrnAvalData = (new ConcurrentHashMap<String, BaseCVData>());
	private Map<String, BaseCVData> mapPhoneData = (new ConcurrentHashMap<String, BaseCVData>());
	private Map<String, BaseCVData> mapBaseCVPointData = (new ConcurrentHashMap<String, BaseCVData>());
	private Map<String, BaseCVData> mapBaseCVPoint2Data = (new ConcurrentHashMap<String, BaseCVData>());
	private Map<String, BaseCVData> mapParGatrData = (new ConcurrentHashMap<String, BaseCVData>());
	private Map<String, BaseCVData> mapMediaChrgData = (new ConcurrentHashMap<String, BaseCVData>());
	private Map<String, BaseCVData> mapIntgCntrData = (new ConcurrentHashMap<String, BaseCVData>());
	private Map<String, BaseCVData> mapIntgCntrKgrData = (new ConcurrentHashMap<String, BaseCVData>());
	private Map<String, BaseCVData> mapIntgCntrUMData = (new ConcurrentHashMap<String, BaseCVData>());
	private Map<String, BaseCVData> mapIntgCntrTtimeData = (new ConcurrentHashMap<String, BaseCVData>());
	private Map<String, BaseCVData> mapAiData = (new ConcurrentHashMap<String, BaseCVData>());
	private Map<String, NearData> mapNearData = (new ConcurrentHashMap<String, NearData>());	// near
	private Map<String, AppTargetData> mapAppTargetData = (new ConcurrentHashMap<String, AppTargetData>());	// AppTarget
	private Map<String, ExternalInfoData> mapExternalInfoData = (new ConcurrentHashMap<String, ExternalInfoData>());
	private Map<String, ShopStatsInfoData> mapShopStatsInfoData = (new ConcurrentHashMap<String, ShopStatsInfoData>());
	private Map<String, ShopInfoData> mapShopInfoData = (new ConcurrentHashMap<String, ShopInfoData>());
	private Map<String, ShopInfoData> mapShopInfoMdPcodeData = (new ConcurrentHashMap<String, ShopInfoData>());
	private List<ConvData> listConvData = Collections.synchronizedList(new ArrayList<ConvData>());
	private List<ConvData> listConvAbusingData = Collections.synchronizedList(new ArrayList<ConvData>());
	private List<ConvData> listConvAllData = Collections.synchronizedList(new ArrayList<ConvData>());
	private List<ConvData> listIntgCntrConvData = Collections.synchronizedList(new ArrayList<ConvData>());
	private List<ConvData> listConvPcodeData = Collections.synchronizedList(new ArrayList<ConvData>());
	private List<ActionLogData> listActionLogData = Collections.synchronizedList(new ArrayList<ActionLogData>());
	private List<ActionLogData> listActionPcodeLogData = Collections.synchronizedList(new ArrayList<ActionLogData>());
	private List<ActionLogData> listIntgCntrActionLogData = Collections.synchronizedList(new ArrayList<ActionLogData>());
	//유니크 클릭 
	private Map<String, BaseCVData> mapClickUniqueData = (new ConcurrentHashMap<String, BaseCVData>());
	
	//기여 전화 
	private List<ConvData> listContributeConvData = Collections.synchronizedList(new ArrayList<ConvData>());
	
	//클릭프리컨시
	private List<ChrgLogData> listChrgLogData = Collections.synchronizedList(new ArrayList<ChrgLogData>());
	
	//플러스콜 로그 데이터
	private List<PluscallLogData> listPluscallLogData = Collections.synchronizedList(new ArrayList<PluscallLogData>());

	//추천 알고리즘 데이터 [노출 , 클릭]
	private Map<String,BaseCVData> mapABPcodeRecomData = new ConcurrentHashMap<String , BaseCVData>();
	//추천 알고리즘 데이터 [전환]
	private List<ConvData>	listABPcodeConvData =  Collections.synchronizedList(new ArrayList<ConvData>());
	//추천 알고리즘 데이터 [Action_log]
	private List<ActionLogData> listActionABPcodeData = Collections.synchronizedList(new ArrayList<ActionLogData>());

	//actionLog 통합
	private List<ActionLogData> listActionRenewLogData = Collections.synchronizedList(new ArrayList<ActionLogData>());

	//장바구니 이력 데이터
	private List<BasketData> listBasketData = Collections.synchronizedList(new ArrayList<BasketData>());

	public Map<String, BaseCVData> getMapViewPcodeData () {
		return this.mapViewPcodeData;
	}
	public void setMapViewPcodeData ( Map<String, BaseCVData> mapViewPcodeData) {
		this.mapViewPcodeData = mapViewPcodeData;
	}

	public List<ActionLogData> getListIntgCntrActionLogData() {
		return listIntgCntrActionLogData;
	}

	public void setListIntgCntrActionLogData(List<ActionLogData> listIntgCntrActionLogData) {
		this.listIntgCntrActionLogData = listIntgCntrActionLogData;
	}

	public Map<String, BaseCVData> getMapBaseCVData() {
		return mapBaseCVData;
	}

	public void setMapBaseCVData(Map<String, BaseCVData> mapBaseCVData) {
		this.mapBaseCVData = mapBaseCVData;
	}
	
	public Map<String, BaseCVData> getMapClientEnvirmentData() {
		return mapClientEnvirmentData;
	}

	public void setMapClientEnvirmentData(Map<String, BaseCVData> mapClientEnvirmentData) {
		this.mapClientEnvirmentData = mapClientEnvirmentData;
	}
	
	public Map<String, BaseCVData> getMapAdverClientEnvirmentData() {
		return mapAdverClientEnvirmentData;
	}

	public void setMapAdverClientEnvirmentData(Map<String, BaseCVData> mapAdverClientEnvirmentData) {
		this.mapAdverClientEnvirmentData = mapAdverClientEnvirmentData;
	}
	
	public Map<String, BaseCVData> getMapClientAgeGenderData() {
		return mapClientAgeGenderData;
	}

	public void setMapClientAgeGenderData(Map<String, BaseCVData> mapClientAgeGenderData) {
		this.mapClientAgeGenderData = mapClientAgeGenderData;
	}

	public Map<String, BaseCVData> getMapBaseCVPointData() {
		return mapBaseCVPointData;
	}

	public void setMapBaseCVPointData(Map<String, BaseCVData> mapBaseCVPointData) {
		this.mapBaseCVPointData = mapBaseCVPointData;
	}
	
	public Map<String, ExternalInfoData> getMapExternalInfoData() {
		return mapExternalInfoData;
	}

	public void setMapExternalInfoData(Map<String, ExternalInfoData> mapExternalInfoData) {
		this.mapExternalInfoData = mapExternalInfoData;
	}

	public Map<String, ShopStatsInfoData> getMapShopStatsInfoData() {
		return mapShopStatsInfoData;
	}

	public void setMapShopStatsInfoData(Map<String, ShopStatsInfoData> mapShopStatsInfoData) {
		this.mapShopStatsInfoData = mapShopStatsInfoData;
	}

	public Map<String, ShopInfoData> getMapShopInfoData() {
		return mapShopInfoData;
	}

	public void setMapShopInfoData(Map<String, ShopInfoData> mapShopInfoData) {
		this.mapShopInfoData = mapShopInfoData;
	}

	public List<ConvData> getMapConvData() {
		return listConvData;
	}

	public void setMapConvData(List<ConvData> mapConvData) {
		this.listConvData = mapConvData;
	}

	public List<ActionLogData> getListActionData() {
		return listActionLogData;
	}

	public void setListActionData(List<ActionLogData> listActionData) {
		this.listActionLogData = listActionData;
	}

	public Map<String, NearData> getMapNearData() {
		return mapNearData;
	}

	public void setMapNearData(Map<String, NearData> mapNearData) {
		this.mapNearData = mapNearData;
	}

	public Map<String, AppTargetData> getMapAppTargetData() {
		return mapAppTargetData;
	}

	public void setMapAppTargetData(Map<String, AppTargetData> mapAppTargetData) {
		this.mapAppTargetData = mapAppTargetData;
	}

	public Map<String, BaseCVData> getMapMediaChrgData() {
		return mapMediaChrgData;
	}

	public void setMapMediaChrgData(Map<String, BaseCVData> mapMediaChrgData) {
		this.mapMediaChrgData = mapMediaChrgData;
	}

	public Map<String, BaseCVData> getMapIntgCntrData() {
		return mapIntgCntrData;
	}

	public void setMapIntgCntrData(Map<String, BaseCVData> mapIntgCntrData) {
		this.mapIntgCntrData = mapIntgCntrData;
	}
	
	public List<ConvData> getListIntgCntrConvData() {
		return listIntgCntrConvData;
	}

	public void setListIntgCntrConvData(List<ConvData> listIntgCntrConvData) {
		this.listIntgCntrConvData = listIntgCntrConvData;
	}

	public Map<String, BaseCVData> getMapParGatrData() {
		return mapParGatrData;
	}

	public void setMapParGatrData(Map<String, BaseCVData> mapParGatrData) {
		this.mapParGatrData = mapParGatrData;
	}

	public Map<String, BaseCVData> getMapIntgCntrKgrData() {
		return mapIntgCntrKgrData;
	}

	public void setMapIntgCntrKgrData(Map<String, BaseCVData> mapIntgCntrKgrData) {
		this.mapIntgCntrKgrData = mapIntgCntrKgrData;
	}

	public Map<String, BaseCVData> getMapIntgCntrTtimeData() {
		return mapIntgCntrTtimeData;
	}

	public void setMapIntgCntrTtimeData(Map<String, BaseCVData> mapIntgCntrTtimeData) {
		this.mapIntgCntrTtimeData = mapIntgCntrTtimeData;
	}

	public Map<String, BaseCVData> getMapIntgCntrUMData() {
		return mapIntgCntrUMData;
	}

	public void setMapIntgCntrUMData(Map<String, BaseCVData> mapIntgCntrUMData) {
		this.mapIntgCntrUMData = mapIntgCntrUMData;
	}

	public List<ConvData> getListConvAllData() {
		return listConvAllData;
	}

	public void setListConvAllData(List<ConvData> listConvAllData) {
		this.listConvAllData = listConvAllData;
	}

	public Map<String, BaseCVData> getMapBaseCVPoint2Data() {
		return mapBaseCVPoint2Data;
	}

	public void setMapBaseCVPoint2Data(Map<String, BaseCVData> mapBaseCVPoint2Data) {
		this.mapBaseCVPoint2Data = mapBaseCVPoint2Data;
	}

	public Map<String, BaseCVData> getMapPhoneData() {
		return mapPhoneData;
	}

	public void setMapPhoneData(Map<String, BaseCVData> mapPhoneData) {
		this.mapPhoneData = mapPhoneData;
	}

	public List<ActionLogData> getListActionPcodeLogData() {
		return listActionPcodeLogData;
	}

	public void setListActionPcodeLogData(List<ActionLogData> listActionPcodeLogData) {
		this.listActionPcodeLogData = listActionPcodeLogData;
	}

	public Map<String, BaseCVData> getMapClickViewPcodeData() {
		return mapClickViewPcodeData;
	}

	public void setMapClickViewPcodeData(Map<String, BaseCVData> mapClickViewPcodeData) {
		this.mapClickViewPcodeData = mapClickViewPcodeData;
	}

	public List<ConvData> getListConvPcodeData() {
		return listConvPcodeData;
	}

	public void setListConvPcodeData(List<ConvData> listConvPcodeData) {
		this.listConvPcodeData = listConvPcodeData;
	}

	public List<ConvData> getListConvAbusingData() {
		return listConvAbusingData;
	}

	public void setListConvAbusingData(List<ConvData> listConvAbusingData) {
		this.listConvAbusingData = listConvAbusingData;
	}

	public Map<String, BaseCVData> getMapCampMediaRetrnAvalData() {
		return mapCampMediaRetrnAvalData;
	}

	public void setMapCampMediaRetrnAvalData(Map<String, BaseCVData> mapCampMediaRetrnAvalData) {
		this.mapCampMediaRetrnAvalData = mapCampMediaRetrnAvalData;
	}

	public Map<String, BaseCVData> getMapOpenrtbData() {
		return mapOpenrtbData;
	}

	public void setMapOpenrtbData(Map<String, BaseCVData> mapOpenrtbData) {
		this.mapOpenrtbData = mapOpenrtbData;
	}

	public Map<String, BaseCVData> getMapADBlockData() {
		return mapADBlockData;
	}

	public Map<String, BaseCVData> getMapCrossAUIDData() {
		return mapCrossAUIDData;
	}

	public void setMapCrossAUIDData(Map<String, BaseCVData> mapCrossAUIDData) {
		this.mapCrossAUIDData = mapCrossAUIDData;
	}

	//유니크 클릭 
	public Map<String, BaseCVData> getMapClickUniqueData() {
		return mapClickUniqueData;
	}

	public void setMapClickUniqueData(Map<String, BaseCVData> mapClickUniqueData) {
		this.mapClickUniqueData = mapClickUniqueData;
	}

	public List<ConvData> getListContributeConvData() {
		return listContributeConvData;
	}

	public void setListContributeConvData(List<ConvData> listContributeConvData) {
		this.listContributeConvData = listContributeConvData;
	}

	public Map<String ,BaseCVData> getMapABPcodeRecomData() {
		return mapABPcodeRecomData;
	}

	public void setMapABPcodeRecomData(Map<String ,BaseCVData> mapABPcodeRecomData) {
		this.mapABPcodeRecomData = mapABPcodeRecomData;
	}

	public List<ConvData> getListABPcodeConvData() {
		return listABPcodeConvData;
	}

	public void setListABPcodeConvData(List<ConvData> listABPcodeConvData) {
		this.listABPcodeConvData = listABPcodeConvData;
	}

	public List<ActionLogData> getListActionABPcodeData() {
		return listActionABPcodeData;
	}

	public void setListActionABPcodeData(List<ActionLogData> listActionABPcodeData) {
		this.listActionABPcodeData = listActionABPcodeData;
	}

	public List<ActionLogData> getListActionRenewLogData() {
		return listActionRenewLogData;
	}

	public void setListActionRenewLogData(List<ActionLogData> listActionRenewLogData) {
		this.listActionRenewLogData = listActionRenewLogData;
	}

	public List<BasketData> getListBasketData() {
		return listBasketData;
	}

	public void setListBasketData(List<BasketData> listBasketData) {
		this.listBasketData = listBasketData;
	}


}
