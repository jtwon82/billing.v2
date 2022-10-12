package com.mobon.billing.core;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.adgather.constants.G;
import com.adgather.constants.old.GlobalConstants;
import com.adgather.reportmodel.old.AdChargeData;
import com.adgather.reportmodel.old.DrcData;
import com.adgather.reportmodel.old.RTBDrcData;
import com.adgather.reportmodel.old.RTBReportData;
import com.adgather.reportmodel.old.ShortCutData;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.model.v15.ShortCutInfoData;
import com.mobon.code.constant.old.CodeConstants;

import net.sf.json.JSONObject;

public class ImpressionClick {
//	private static final Logger	logger	= LoggerFactory.getLogger(ImpressionClick.class);
	
	public BaseCVData AvailableData(String message) throws Exception {
		BaseCVData record= null;
		
//		PollingData item= new ObjectMapper().readValue(message, PollingData.class);
		
		JSONObject jSONObject= JSONObject.fromObject(message);
		
//		record= item.toBaseCVData();
//		String className= record.getClassName();
		String className= (String) jSONObject.get("className");
		
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
		return record;
	}

	public BaseCVData processBaseCVData(BaseCVData record){
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
//				logger.info("Missing required scriptUserId record - {}", record);

				// TODO : 180307 이런경우는 없어야됨 모비온에 관련로직 구현함 나중에 삭제하자 
//				BaseCVData ms = selectDao.selectMediaInfo(record);
//				if( ms!=null ) {
//					record.setScriptUserId( ms.getScriptUserId());
//				}
				//logger.info("empty data seting scriptNo-{}, scriptUserId - {}", record.getScriptNo(), record);
			}
			if( !"0".equals(record.getKno()) && !StringUtils.isNumeric(record.getKno()) ) {
				record.setKno("0");
//				logger.info("chking kno is not integer record - ", record);
			}
			if( "KP".equals(record.getAdGubun()) ) {
				record.setAdGubun( "KL" );
			}
			
			// sky 로직
			if ( G.SKYCHARGE.equals(record.getDumpType()) ){
				if ( GlobalConstants.CLICK.equals(record.getType()) ){
					int result = dumpSkyClickData(record);
					if(result==1){
//						logger.error("SKYCHARGE result = 1 : record - {}", record);
						record = null;
					}
				}else{
					int result = dumpSkyChargeData(record);
					if(result==1){
//						logger.error("SKYCHARGE result = 1 : record - {}", record);
						record = null;
					}
				}
			}
			// icover  로직
			else if ( G.ICOCHARGE.equals(record.getDumpType()) ){
				int result = dumpChargeLogData(record);
				if(result==1){
//					logger.error("ICOCHARGE result = 1 : record - {}", record);
					record = null;
				}
			}
			// plCharge 로직
			else if ( G.PLAY_LINK_CHARGE.equals(record.getDumpType()) ){
				if (GlobalConstants.CLICK.equals(record.getType())) {
					int result = dumpPlayLinkClickData(record);
					if(result==1) {
						record = null;
					}
				} else {
					int result = dumpPlayLinkChargeData(record);
					if(result==1) {
						record = null;
					}
				}
			}
			// normalCharge 로직
			else if ( G.NORMALCHARGE.equals(record.getDumpType()) ){
				int result = dumpNormalViewLogData(record);
				if(result==1){
//					logger.error("NORMALCHARGE result = 1 : record - {}", record);
					record = null;
				}
			}
			// mobileCharge 
			else if ( G.MOBILECHARGE.equals(record.getDumpType()) ){
				int result = dumpMobileChargeLogData(record);
				if(result==1){
//					logger.error("MOBILECHARGE result = 1 : record - {}", record);
					record = null;
				}
			}
			// drcCharge 
			else if ( G.DRCCHARGE.equals(record.getDumpType() ) ){
				int result = dumpDrcData(record);
				if(result==1){
//					logger.error("DRCCHARGE result = 1 : record - {}", record);
					record = null;
				}
			}
			// shopCon
			else if ( G.SHOPCONCHARGE.equals(record.getDumpType()) ){
				int result = dumpShopConData(record);
				if(result==1){
//					logger.error("SHOPCONCHARGE result = 1 : record - {}", record);
					record = null;
				}
			}
			// actionlog만 
			else if ( G.ACTIONCHARGE.equals(record.getDumpType()) ) {
				dumpActData(record);
				record = null;
				
			}
			else {
//				logger.error("other - {}", record);
			}
			
		} catch (Exception e) {
			record=null;
//			logger.error("ERROR processBaseCVData >> {} {}", record, e);
		}
		if(record!=null) {
			record.generateKey();
		}
		return record;
	}
	
	public BaseCVData processShortCutInfoData(BaseCVData result){
		try {
			int intResult = dumpShortCutData(result);
			if(intResult==1){
//				logger.debug("ShortCutInfoData result = 1 : record - {}", result);
				result = null;
			}
		} catch (Exception e) {
//			logger.error("ERROR >> {} {}", result, e);
		}
		if(result!=null) {
			result.generateKey();
		}
		return result;
	}
	public BaseCVData processRtbViewData(BaseCVData result) {
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
			
//			logger.debug("record - {}", result);
			
		} catch (Exception e) {
//			logger.error("err processRtbViewData >> {} {}", result, e);
		}
		if(result!=null) {
			result.generateKey();
		}
		return result;
	}
	public BaseCVData processRtbClickData(BaseCVData result) {
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
			
//			logger.debug("result - {}", result);

			dumpRtbDrcData(result);
			
		} catch (Exception e) {
//			logger.error("err processRtbClickData >> {} {}", result, e);
		}
		if(result!=null) {
			result.generateKey();
		}
		return result;
	}

	/**
	 * dumpSkyClickData 정보를 처리한다.
	 * @param BaseCVData data
	 * @return void
	 * @throws 
	 */
	public int dumpSkyClickData(BaseCVData data) throws IOException{
		try{
			data.setType(G.CLICK);
			data.setClickCnt(1);

			if((G.SKY + G.GUBUN + G.LOWER_M).equals(data.getProduct()) ||
				G.MBB.equals(data.getProduct())){
			}else{
			}
			
//			logger.debug("add list - {}", data.generateKey());
			dumpActData(data );
			
			return 0;
		}catch(Exception e){
//			logger.error("err dumpSkyClickData msg - {}", e);
			return 1;
		}
	}
	/**
	 * dumpSkyChargeData 정보를 처리한다.
	 * @param BaseCVData data
	 * @return void
	 * @throws 
	 */
	public int dumpSkyChargeData(BaseCVData data) throws IOException{
		try{
			if((G.SKY + G.GUBUN + G.LOWER_M).equals(data.getProduct())){
				data.setProduct(G.MBB);
			}else{
			}
//			logger.debug("add list - {}", data.generateKey());

			dumpActData(data );
			
			return 0;
		}
		catch(Exception e){
//			logger.error("err dumpSkyChargeData msg - {}", e);
			return 1;
		}
	}
	
	/**
	 * dumpChargeLogData 정보를 처리한다.
	 * @param BaseCVData data, MediaScriptData ms
	 * @return void
	 * @throws 
	 */
	public int dumpChargeLogData(BaseCVData data) throws IOException {
		try {
			if( !data.isChargeAble() ) {
			}
			
			if ((G.ICO + G.GUBUN + G.LOWER_M).equals(data.getProduct())
					|| "m".equals(data.getPlatform())) {
				data.setProduct(G.MBE);
			} else {
			}
			
			if( data.getViewCnt()<1 ) {
				data.setViewCnt(1);
			}
			
//			logger.debug("add list - {}", data.generateKey());

			dumpActData( data );
			
			return 0;
		} catch (Exception e) {
//			logger.error("err dumpChargeLogData msg - {}", e);
			return 1;
		}
	}
	public int dumpPlayLinkClickData(BaseCVData data) throws IOException{
		int result = 1;
		try{
			
			data.setClickCnt(1);

			dumpActData(data );
			result = 0;
			return result;
		}catch(Exception e){
//			logger.error("err dumpNormalViewLogData msg - {}", e);
			return 1;
		}
	}
	public int dumpPlayLinkChargeData(BaseCVData data) throws IOException{
		int result = 1;
		try{

			if (CodeConstants.AD.equalsIgnoreCase(data.getChargeType())) {
				data.setMpoint(0);
			}

			if (CodeConstants.MEDIA.equalsIgnoreCase(data.getChargeType())) {
				data.setPoint(0);
			}

			dumpActData(data );
			
			result = 0;
			
			return result;
		}catch(Exception e){
//			logger.error("err dumpNormalViewLogData msg - {}", e);
			return 1;
		}
	}
	/**
	 * dumpNormalViewLogData 정보를 처리한다.
	 * @param BaseCVData data
	 * @return void
	 * @throws 
	 */
	public int dumpNormalViewLogData(BaseCVData data) throws IOException{
		int result = 1;
		try{
			if(StringUtils.isNotEmpty(data.getAdvertiserId() ) && !"PE".endsWith(data.getAdGubun()) && !"CA".endsWith(data.getAdGubun())){
				if(StringUtils.isEmpty(data.getAdGubun())){
				} else {
					if( data.getViewCnt()<1 ) {
						data.setViewCnt(1);
					}
					
//					logger.debug("add list - {}", data.generateKey());

					dumpActData(data );
					
					result = 0;
				}
			}
			return result;
		}catch(Exception e){
//			logger.error("err dumpNormalViewLogData msg - {}", e);
			return 1;
		}
	}
	/**
	 * dumpMobileChargeLogData 정보를 처리한다.
	 * @param BaseCVData data
	 * @return void
	 * @throws 
	 */
	public int dumpMobileChargeLogData(BaseCVData data) throws IOException{
		try{
			// mba, mbw
//			logger.debug("add list - {}", data.generateKey());

			dumpActData(data );
			
			return 0;
		}catch(Exception e){
//			logger.error("err dumpMobileChargeLogData msg - {}", e);
			return 1;
		}
	}
	/**
	 * dumpDrcData 정보를 처리한다.
	 * @param BaseCVData data, MediaScriptData ms, PointData getpoint
	 * @return void
	 * @throws 
	 */
	public int dumpDrcData(BaseCVData data) throws IOException {
		int result=1;
		try {
//			logger.debug("drcCharge key - {}", data );
			
			SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date();
			String ymd = yyyymmdd.format(date);
			
			final String gb = data.getAdGubun(); // data.getGb();
			final String sc = data.getSiteCode(); // data.getSc();
			boolean clickChk = data.isClickChk();

			float point = data.getPoint();
			float mpoint = data.getMpoint();

			String chk_key = G.BANNER + G.GUBUN + data.getAdGubun() + G.GUBUN
					+ data.getMcgb() + G.GUBUN + data.getNo() + G.GUBUN + data.getScriptNo()
					+ G.GUBUN + data.getKno() + G.GUBUN + data.getAdvertiserId();
			if (chk_key.length() > 32) {
				chk_key = chk_key.substring(0, 31);
			}
			data.setKeyCode(chk_key);
			String chkData = ""; //dao.getClickChkDataInfoBanner(data);
			String kno = "";
			if (StringUtils.isNotEmpty(data.getKno()) && data.getKno().length() > 13) {
				kno = data.getKno().substring(0, 13);
			} else {
				kno = data.getKno() + "";
			}
			data.setYyyymmdd( DateUtils.getDate("yyyyMMdd", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.getSendDate()) ));
			data.setKno(kno);
			data.setType(G.CLICK);
			data.setClickCnt(1);

			if (data.getScriptNo()>0 && data.getScriptUserId() != null && data.getScriptUserId().length() > 0) {
				if( G.MCT.equals(data.getProduct()) ) {
					result = 0;
				} else if (G.MBW.equals(data.getProduct()) || G.MBA.equals(data.getProduct())
						|| data.getProduct().equals("mba_no_script")) {
					data.setProduct(G.MBW);
					if ( 8126!=data.getScriptNo() || 8126==data.getScriptNo() && clickChk ) { //!"8126".equals(data.getScriptNo()) || ("8126".equals(data.getScriptNo()) && clickChk)) {
//						logger.debug("add list - {}", data.generateKey());
						result=0;
					}
				} else if (!"PE".endsWith(data.getAdGubun() ) && !"CA".endsWith(data.getAdGubun() )) {
//					logger.debug("add list - {}", data.generateKey());
					result=0;
				}
				
				
				// 사이클 처리하는 로직 있었음

				//if (StringUtils.isNotEmpty(data.getMobonLinkCate()) && ( 8126!=data.getScriptNo() || 8126==data.getScriptNo() )) {
				
//				if(data.isClickChk()) {
//					// 클릭수치올림
//					
//					BaseCVData ms = selectDao.selectMediaInfo(data);
//					
//					if( ms!=null ) {
//						ExternalLinkageData link = new ExternalLinkageData();
//						link.setSdate(data.getYyyymmdd());
//						link.setUserid(data.getAdvertiserId()); // 광고주아이디
//						link.setSite_code(data.getSiteCode()); // 사이트코드
//						link.setGubun(G.AD); // 베이스광고
//						link.setMedia_site(data.getMediasiteNo());
//						link.setMedia_code(data.getScriptNo());
//						link.setMedia_id(data.getScriptUserId());
//						link.setAd_type(ms.getAD_TYPE().replace(G.GUBUN, "x"));
//						//link.setZoneid(data.getMobonLinkCate());
//						link.setType("C");
//						link.setClickcnt_mobon(1);
//						link.setTransmit(G.SEND);
//						link.setPoint(data.getMpoint());
//						link.setDumpType(G.EXTERNALCHARGE);
//						
//						link.setSend_tp_code("03");
//						link.setType("SEND");
//						link.setHh(data.getHh());
//						link.setClickcnt(1);
//						ExternalInfoData tmp = link.toExternalInfoData();
//						//if ( "Consumer".equals(profileId) ) {	// 삭제됨
//							//sumObjectManager.appendData(tmp, false);
//						//}
//					}
//				}
			}
			dumpActData(data );
			
			return result;
		} catch (Exception e) {
//			logger.error("err dumpDrcData msg - {}", e);
			return 1;
		}
	}
	/**
	 * dumpShopConData 정보를 처리한다.
	 * @param BaseCVData data, MediaScriptData ms
	 * @return void
	 * @throws 
	 */
	public int dumpShopConData(BaseCVData data) throws IOException{
		try{
			SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
			Date date=new Date();
			String ymd=yyyymmdd.format(date);
			
			data.setYyyymmdd( DateUtils.getDate("yyyyMMdd", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.getSendDate()) ));
			data.setScriptUserId(data.getScriptUserId()); 
			data.setPoint(data.getPoint());
			data.setType(G.CLICK);
			data.setClickCnt(1);
			
			if( G.MBW.equals(data.getProduct()) ){
			}else{
			}

//			logger.debug("add list - {}", data.generateKey());

			dumpActData(data );
			return 0;
		}catch(Exception e){
//			logger.error("err dumpShopConData msg - {}", e);
			return 1;
		}
	}
	/**
	 * dumpShortCutData 정보를 처리한다.
	 * @param BaseCVData data, MediaScriptData ms, PointData getpoint
	 * @return void
	 * @throws 
	 */
	public int dumpShortCutData(BaseCVData data) throws IOException{
		int result = 1;
		try{
			data.setPlatform(G.UPPER_M);
			data.setProduct(G.MBW);
			data.setServiceHostId(data.getServiceHostId());
			data.setScriptUserId(data.getScriptUserId());
			
			SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
			Date date=new Date();
			String ymd=yyyymmdd.format(date);
			if (G.CLICK.equals(data.getType())) {
				String chk_key = "";
				data.setKeyCode(chk_key);
				
				String chkData = ""; //dao.getClickChkDataInfoBanner( data.toBaseCVData() );
				if( data.getYyyymmdd()!=null && "".equals(data.getYyyymmdd()) ) {
					data.setYyyymmdd( DateUtils.getDate("yyyyMMdd", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.getSendDate()) ));
				}
				data.setType(G.CLICK);
				data.setClickCnt(1);
				
				// 바콘 부정클릭 판단로직
				if(chkData==null || chkData.length()<1){
//					logger.debug("add list - {}", data.generateKey());

					result = 0;
				} else {
				}

				// 클릭 정보를 ACTION_LOG 테이블에 저장 (일반배너 또한 한시간 프리퀀시가 없다고 하더라도 컨버젼이 잡히도록 변경)
				dumpActData(data);
				
			}else{
//				logger.debug("type is not click");
			}
		}catch(Exception e){
//			logger.error("err dumpShortCutData 2 msg - {}", e);
		}
		return result;
	}
	public int dumpRtbDrcData(BaseCVData data) {
		try {
			dumpActData( data );
			
			//TODO BaseCVData rtb = selectDao.selectRtbInfo(data);
//			BaseCVData rtb = selectDao.selectRtbInfo(data);
//			if( rtb!=null ) {
//				data.setRtbUseMoneyYn( rtb.getRtbUseMoneyYn() );
//				
//				logger.debug("rtb - {}", rtb);
//				return 1;
//			} else {
				return 0;
//			}
		}catch(Exception e) {
//			logger.error("err dumpRtbDrcData ", e);
			return 1;
		}
	}
	public void dumpActData(BaseCVData data) {
		
	}
	
	
	
	
	public static ImpressionClick INS;

	public static ImpressionClick getInstance() {
		if(INS==null) {
			INS = new ImpressionClick();
		}
		return INS;
	}
}
