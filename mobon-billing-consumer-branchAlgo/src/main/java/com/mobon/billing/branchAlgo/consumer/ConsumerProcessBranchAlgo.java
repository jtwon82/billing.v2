package com.mobon.billing.branchAlgo.consumer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.constants.old.GlobalConstants;
import com.adgather.reportmodel.old.AdChargeData;
import com.adgather.reportmodel.old.DrcData;
import com.adgather.reportmodel.old.RTBDrcData;
import com.adgather.reportmodel.old.RTBReportData;
import com.adgather.reportmodel.old.ShortCutData;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.core.service.DataBuilder;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.dao.SelectDao;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.model.v15.ShortCutInfoData;
import com.mobon.code.CodeUtils;

import net.sf.json.JSONObject;

@Configuration
@Service
public class ConsumerProcessBranchAlgo extends Consumer{
	private static final Logger	logger	= LoggerFactory.getLogger(ConsumerProcessBranchAlgo.class);

	@Autowired
	private DataBuilder			dataBuilder;
	
	@Autowired
	private SumObjectManager	sumObjectManager;

	@Autowired
	private SelectDao			selectDao;
	
	@Value("${profile.id}")
	private String	profileId;
	
	public void processMain(String topic, String message) {
		boolean noCharge = false;
		String className = "";
		String userId = "";
		JSONObject jSONObject = JSONObject.fromObject(message);
		
		try {
			noCharge = (boolean) jSONObject.get("noCharge");
		} catch(Exception e) {
			noCharge = false;
		}
		
		className = (String) jSONObject.get("className");
		userId = (String) jSONObject.get("userId");
		
		if(StringUtils.isNotEmpty(userId) && userId.length() > 20) { // 이상광고주ID로 인해 크기제한
			logger.error("adverId is too long = " + userId);
			return;
		}
		if(noCharge) { 
			logger.info("Retry Point Data");
			return;
		}
		BaseCVData  record= null;
		if (G.ClickViewData.equals(topic) || G.ClickViewPointData.equals(topic) ||
				G.AlgoViewData.equals(topic)) {
			
			if ( G.AdChargeData.equals(className) ) {
				AdChargeData tmpAdCharge = AdChargeData.fromHashMap(jSONObject);
				record = tmpAdCharge.toBaseCVData();
				record = processBaseCVData( record );		// AdChargeData
				
			} else if ( G.DrcData.equals(className) ) {
				DrcData tmpDrc = DrcData.fromHashMap(jSONObject);
				record = tmpDrc.toBaseCVData();
				record = processBaseCVData( record );
				
			} else if ( G.ShortCutData.equals(className) ) {
				ShortCutData a = ShortCutData.fromHashMap(jSONObject);
				ShortCutInfoData tmp = a.toShortCutInfoData();
				record = tmp.toBaseCVData();
				record = processShortCutInfoData(record);
				
			} else if ( G.RTBReportData.equals(className) || G.RTBDrcData.equals(className) ) {
				if( G.RTBReportData.equals(className) ) {
					RTBReportData tmp = RTBReportData.fromHashMap(jSONObject);
					record = tmp.toBaseCVData();
					record = processRtbViewData(record);
				} else {
					RTBDrcData tmp = RTBDrcData.fromHashMap(jSONObject);
					record = tmp.toBaseCVData();
					record = processRtbClickData(record);
				}
			}
		}
		// record 값이 변환이 안된경우 return
		if ( record == null) {
			logger.debug("record is null - {}" ,record);
			return;
		}
		if( "91".equals(record.getChrgTpCode()) ) {
			return;
		}
		
		Date sendDate = new Date();
		Date targetDate = new Date();
		
		try {
			sendDate.setTime( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse( record.getSendDate() ).getTime() );
			targetDate.setTime( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse( "2021-01-20 15:00:00" ).getTime() );
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (targetDate.getTime() > sendDate.getTime()) {
			return;
		}
		
//		BaseCVData record = SerializationUtils.clone(record);
		// 추천구분코드
		record.setRecomTpCode(CodeUtils.getRecomTpCode(record.getAdGubun(), record.getSubadgubun()));
		// 알고리즘종류
		record.setRecomAlgoCode(CodeUtils.getRecomAlgoCode(record.getSubadgubun(), record.getErgdetail(), ""));
		
		boolean targetAdGubun = ("04".equals(record.getAdGubunCode()) || "10".equals(record.getAdGubunCode()) 
								|| "16".equals(record.getAdGubunCode()) || "17".equals(record.getAdGubunCode())
								|| "37".equals(record.getAdGubunCode()) || "40".equals(record.getAdGubunCode())
								|| "41".equals(record.getAdGubunCode()) || "42".equals(record.getAdGubunCode())
								|| "47".equals(record.getAdGubunCode()) || "49".equals(record.getAdGubunCode())
								|| "50".equals(record.getAdGubunCode()));

		boolean checkProduct = ("01".equals(record.getProductCode()));
		
		
		boolean checkAppendResult = targetAdGubun && checkProduct;

		String abTestTy = this.getABtestResult(record);
		
		if (! checkAppendResult) {
			logger.debug("It's not AlgoTarget Data - {}", record.toString());
			return;
		}
		BaseCVData record2 = null;
		BaseCVData record3 = null;

		try {
			record2 = record.clone();
			record3 = record.clone();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (G.AlgoViewData.equals(topic)) {
			try {
				logger.debug("AlgoViewData : {} {} {} {} {}", record.getType(), record.getProductCode(), record.getAdGubunCode(), record.getSubAdGubunCode(), record.getAdGubun());

				if ("V".equals(record.getType())) {

					sumObjectManager.appendViewPcodeData(record);

					logger.debug( "AlgoViewData : gb:{}, sgb:{}, rtp:{}, alg:{}, "
							, record.getAdGubun(), record.getSubadgubun(), record.getRecomTpCode()
							, record.getRecomAlgoCode());
					if (abTestTy != null && record2 != null) {

						record2.setAbTestTy(abTestTy);
						sumObjectManager.appendABPcodeRecom(record2);
					}
				}
			} catch(Exception e) {
				logger.error("err ", e);
			}
		} else if (G.ClickViewData.equals(topic) ||
				G.ClickViewPointData.equals(topic)) {
	
			if ( "C".equals(record.getType())) {
				
				sumObjectManager.appendClickViewPcodeData(record);
		
				logger.debug("clickViewPcode : gb:{}, sgb:{}, rtp:{}, alg:{}, "
						, record.getAdGubun(), record.getSubadgubun(), record.getRecomTpCode(), record.getRecomAlgoCode());

				if (abTestTy != null && record3 != null) {

					record3.setAbTestTy(abTestTy);
					sumObjectManager.appendABPcodeRecom(record3);
				}
			}
		} else {
			logger.error("else topic chking topic - {},  vo - {}", topic, jSONObject );
		}

	}

	//ab test 값 확인
	private String getABtestResult(BaseCVData record) {
		String abTest = record.getAbTest();
		String recomCode = record.getRecomAlgoCode();
		String [] algoCodeArr = {"00","04","06","09","10","11","12","13","14","15","16","17","18","19","22","25","33","34"};
		boolean algoCodeResult = false;

		for (String algoCode : algoCodeArr) {
			if (recomCode.equals(algoCode)) {
				algoCodeResult = true;
			}
		}

		//abTest 데이터가 null 이거나 length 가 0 이면 해당 데이터를 쌓지 않는다 .
		if (abTest == null || abTest.length() == 0) {
			return null;
		}

		String[] spAbTest = abTest.split("[|]");
		if (algoCodeResult) {

			for (String abTestData : spAbTest) {
				if (abTestData.startsWith("BI")) {
					return abTestData;
				}
			}
		}
		return null;
	}

	/**
	 * processRtbClickViewData 처리프로세스
	 * @param String className, Integer partition, Long offset, String message
	 * @return void
	 * @throws Exception
	 */
	private BaseCVData processRtbClickData(BaseCVData result) {
		try {
//			RTBDrcData record = RTBDrcData.fromHashMap(message);
//			result = record.toBaseCVData();
			
			// 
			result.setPlatform(result.getPlatform().substring(0, 1).toUpperCase());
			result.setType("C");
			result.setClickCnt(1);
			
			result.setYyyymmdd( DateUtils.getDate("yyyyMMdd", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(result.getSendDate()) ));
			
			if( "kakao".equals(result.getScriptUserId()) || "mkakao".equals(result.getScriptUserId()) ) {
				result.setInterlock("03");
			
			} else if ( "daisy".equals(result.getScriptUserId()) || "mdaisy".equals(result.getScriptUserId()) ) {
				result.setInterlock("02");
			}
			
			logger.debug("result - {}", result);

			dataBuilder.dumpRtbDrcData(result);
			
		} catch (Exception e) {
			logger.error("err processRtbClickData >> {} {}", result, e);
		}
		if(result!=null) {
			result.generateKey();
		}
		return result;
	}

	private BaseCVData processRtbViewData(BaseCVData result) {
		try {
//			RTBReportData record = RTBReportData.fromHashMap(result);
//			result = record.toBaseCVData();

			// 
			result.setPlatform(result.getPlatform().substring(0, 1).toUpperCase());
			result.setType("V");

			//01: 모비온, 02: 데이지, 03: 카카오, 99:외부연동
			if( "kakao".equals(result.getScriptUserId()) || "mkakao".equals(result.getScriptUserId()) ) {
				result.setInterlock("03");
				
			} else if ( "daisy".equals(result.getScriptUserId()) || "mdaisy".equals(result.getScriptUserId()) ) {
				result.setInterlock("02");
			}
			
			logger.debug("record - {}", result);
			
		} catch (Exception e) {
			logger.error("err processRtbViewData >> {} {}", result, e);
		}
		if(result!=null) {
			result.generateKey();
		}
		return result;
	}
	/**
	 * <pre>
	 * 바콘
	 * </pre>
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @author jtwon
	 * @date 2017. 9. 6.
	*/
	private BaseCVData processShortCutInfoData(BaseCVData result) {
		try {
			int intResult = dataBuilder.dumpShortCutData(result);
			if(intResult==1){
				logger.debug("ShortCutInfoData result = 1 : record - {}", result);
				result = null;
			}
		} catch (Exception e) {
			logger.error("ERROR >> {} {}", result, e);
		}
		if(result!=null) {
			result.generateKey();
		}
		return result;
	}	
	
	/**
	 * <pre>
	 * 클릭뷰 처리
	 * </pre>
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @author jtwon
	 * @date 2017. 9. 1.
	*/
	private BaseCVData processBaseCVData(BaseCVData record) {
		try {
			if( "".equals(record.getKey()) ){
				record.setKey("0");
			}
			if( StringUtils.isEmpty(record.getNo()+"") ){
				record.setNo(0);
			}
//			if( 0==record.getMediasiteNo() ) {
//				logger.error("Missing required mediasiteNo record - {}", record);
//				return null;
//			}
			if( StringUtils.isEmpty(record.getScriptUserId()) ) {
				logger.debug("Missing required scriptUserId record - {}", record);

				// 180307 이런경우는 없어야됨 모비온에 관련로직 구현함 나중에 삭제하자 
				BaseCVData ms = selectDao.selectMediaInfo(record);
				if( ms!=null ) {
					record.setScriptUserId( ms.getScriptUserId());
				}
				//logger.info("empty data seting scriptNo-{}, scriptUserId - {}", record.getScriptNo(), record);
			}
			if( !"0".equals(record.getKno()) && !StringUtils.isNumeric(record.getKno()) ) {
				record.setKno("0");
				logger.info("chking kno is not integer record - ", record);
			}
			if( "KP".equals(record.getAdGubun()) ) {
				record.setAdGubun( "KL" );
			}
			
			// sky 로직
			if ( G.SKYCHARGE.equals(record.getDumpType()) ){
				if ( GlobalConstants.CLICK.equals(record.getType()) ){
					int result = dataBuilder.dumpSkyClickData(record);
					if(result==1){
						logger.error("SKYCHARGE result = 1 : record - {}", record);
						record = null;
					}
				}else{
					int result = dataBuilder.dumpSkyChargeData(record);
					if(result==1){
						logger.error("SKYCHARGE result = 1 : record - {}", record);
						record = null;
					}
				}
			}
			// icover  로직
			else if ( G.ICOCHARGE.equals(record.getDumpType()) ){
				int result = dataBuilder.dumpChargeLogData(record);
				if(result==1){
					logger.error("ICOCHARGE result = 1 : record - {}", record);
					record = null;
				}
			}
			// plCharge 로직
			else if ( G.PLAY_LINK_CHARGE.equals(record.getDumpType()) ){
				if (GlobalConstants.CLICK.equals(record.getType())) {
					int result = dataBuilder.dumpPlayLinkClickData(record);
					if(result==1) {
						record = null;
					}
				} else {
					int result = dataBuilder.dumpPlayLinkChargeData(record);
					if(result==1) {
						record = null;
					}
				}
			}
			// normalCharge 로직
			else if ( G.NORMALCHARGE.equals(record.getDumpType()) ){
				int result = dataBuilder.dumpNormalViewLogData(record);
				if(result==1){
					logger.error("NORMALCHARGE result = 1 : record - {}", record);
					record = null;
				}
			}
			// mobileCharge 
			else if ( G.MOBILECHARGE.equals(record.getDumpType()) ){
				int result = dataBuilder.dumpMobileChargeLogData(record);
				if(result==1){
					logger.error("MOBILECHARGE result = 1 : record - {}", record);
					record = null;
				}
			}
			// drcCharge 
			else if ( G.DRCCHARGE.equals(record.getDumpType() ) ){
				int result = dataBuilder.dumpDrcData(record);
				if(result==1){
					logger.error("DRCCHARGE result = 1 : record - {}", record);
					record = null;
				}
			}
			// shopCon
			else if ( G.SHOPCONCHARGE.equals(record.getDumpType()) ){
				int result = dataBuilder.dumpShopConData(record);
				if(result==1){
					logger.error("SHOPCONCHARGE result = 1 : record - {}", record);
					record = null;
				}
			}
			// 누락데이터 재처리
			else if ( G.ADDCHARGE.equals(record.getDumpType()) ) {
				record.setViewCnt(0);
				record.setViewCnt2(0);
				record.setViewCnt3(0);
				record.setClickCnt(0);
				if("i".equals(record.getProduct())){
					record.setViewCnt(1);
					
				} else if("s".equals(record.getProduct())){
					record.setViewCnt(1);
					record.setViewCnt3(1);
				
				} else {
					record.setClickCnt(1);
					record.setType("C");
				}
				logger.debug("addCharge:{}", record);
			}
			// actionlog만 
			else if ( G.ACTIONCHARGE.equals(record.getDumpType()) ) {
				dataBuilder.dumpActData(record);
				record = null;
				
			}
			else {
				logger.error("other - {}", record);
			}
//			else if( G.SHOPCONCHARGE.equals(record.getDumpType()) ) {
//				kafKaProducerSingle.getInstance().send("ConversionData", record);
//			}
			
		} catch (Exception e) {
			record=null;
			logger.error("ERROR processBaseCVData >> {} {}", record, e);
		}
		if(record!=null) {
			record.generateKey();
		}
		return record;
	}

}
