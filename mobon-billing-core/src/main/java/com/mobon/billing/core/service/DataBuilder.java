package com.mobon.billing.core.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import com.mobon.billing.core.service.dao.*;
import net.sf.json.JSON;
import org.apache.commons.lang3.SerializationUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.reportmodel.old.ExternalLinkageData;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.core.service.impl.old.ConversionServiceSimple;
import com.mobon.billing.core.service.old.ConversionService;
import com.mobon.billing.model.v15.ActionLogData;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.model.v15.ConvData;
import com.mobon.billing.model.v15.ExternalInfoData;
import com.mobon.billing.model.v15.ShopInfoData;
import com.mobon.billing.model.v15.ShopStatsInfoData;
import com.mobon.code.CNVRS_ABUSING_TP_CODE;
import com.mobon.code.CodeUtils;
import com.mobon.code.constant.old.CodeConstants;
import com.mobon.conversion.domain.old.ConversionCode;
import com.mobon.conversion.domain.old.ConversionInfoFilter;

import net.sf.json.JSONObject;

@Service
public class DataBuilder {

	private static final Logger	logger	= LoggerFactory.getLogger(DataBuilder.class);

	@Autowired
	private ConvDataDao			convDataDao;
	@Autowired
	private IntgCntrConvDataDao			IntgCntrconvDataDao;
	@Autowired
	private SelectDao			selectDao;

	@Autowired
	private ShopInfoDataDao		shopInfoDataDao;

	@Autowired
	private ConversionServiceSimple	conversionServiceSimple;

	@Autowired
	private ClickHouseDao clickHouseDao;

	@Autowired
	private SumObjectManager	sumObjectManager;

	@Autowired
	@Qualifier("ClickViewDataWorkQueue")
	private WorkQueueTaskData		clickViewWorkQueue;
	@Autowired
	@Qualifier("IntgCntrActionDataWorkQueue")
	private WorkQueueTaskData		intgCntrActionDataWorkQueue;

	@Value("${conversion.delay.time.minute}")
	private int convDelayTimeMinute;
	@Value("${change.product.adgubun.list}")
	private String	changeProductAdgubunList;

	@Value("${profile.id}")
	private String	profileId;

	@Value("${log.path.action}")
	private String actionLogPath;

	public DataBuilder(){
	}

//	public void codeMapperX(ClickViewData record) {
//		StringTokenizer st1 = new StringTokenizer(changeProductAdgubunList, "[");
//		while(st1.hasMoreTokens()) {
//			StringTokenizer st2 = new StringTokenizer(st1.nextToken(), "]");
//			String tmp1 = st2.nextToken();
//			String tmp2 = st2.nextToken();
//			List adGubunList = Arrays.asList( tmp2.split(",") );
//			if( adGubunList.contains(record.getAdGubun()) ) {
//				record.setProduct(tmp1);
//			}
//		}
//	}
//	public void codeMapperX(ConvData record) {
//		StringTokenizer st1 = new StringTokenizer(changeProductAdgubunList, "[");
//		while(st1.hasMoreTokens()) {
//			StringTokenizer st2 = new StringTokenizer(st1.nextToken(), "]");
//			String tmp1 = st2.nextToken();
//			String tmp2 = st2.nextToken();
//			List adGubunList = Arrays.asList( tmp2.split(",") );
//			if( adGubunList.contains(record.getAdGubun()) ) {
//				record.setProduct(tmp1);
//			}
//		}
//	}
	/**
	 * ?????????????????? ?????????
	 * ?????????????????? ???????????? ??????????????? ???????????? ????????? ???????????????.
	 * @param data
	 */
	public int insertShopData(ShopInfoData data){
		int result = 1;
		try {
			boolean chkCate = false;
			try {
				boolean chkCateSpecialChar = !Pattern.matches("^[???-??????-???a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~`\\u2e80-\\u2eff\\u31c0-\\u31ef\\u3200-\\u32ff\\u3400-\\u4dbf\\u4e00-\\u9fbf\\uf900-\\ufaff\t ]+$", data.getCate());
				boolean chkPnmSpecialChar = !Pattern.matches("^[???-??????-???a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~`\\u2e80-\\u2eff\\u31c0-\\u31ef\\u3200-\\u32ff\\u3400-\\u4dbf\\u4e00-\\u9fbf\\uf900-\\ufaff\t ]+$", data.getPnm());
				chkCate = data.getCate().indexOf("???")>0;
			} catch (Exception e) {
				data.setCate("");
			}

			if( chkCate ) {
 				logger.info("skip ShopInfoData - {}", data.toString().substring(0, 100));
			} else {
				boolean mobileInsert = data.isMobileInsert();
		        boolean webInsert = data.isWebInsert();

				logger.debug("::: data SHOP result ::: {}", data);
				logger.debug("::: web chk ::: {} ::: mobile chk ::: {}", webInsert, mobileInsert);

				{
					if( data.isINSERT_BOTH() ){
						data.setPlatform("D");
						result = 0;

					} else if( !data.isCheckMobileLink() && data.isWebInsert() ) {
						data.setPlatform("W");
						result = 0;

					} else if( data.isCheckMobileLink() && data.isMobileInsert() ) {
						data.setPlatform("M");
						result = 0;
					} else {
						logger.info("ShopInfo nothing platform - {}", data);
					}
					logger.debug("add list - {}", data.generateKey());
				}

				if ( result==0 ) {
					Map <String,Object> param = new HashMap<String,Object>();
					param.put("pCode", data.getpCode());
					param.put("advertiserId", data.getAdvertiserId());
					if ( "M".equals(data.getPlatform()) ) {
						param.put("tableName", "MOB_SHOP_DATA");
					}
					else {
						param.put("tableName", "SHOP_DATA");
					}

					ShopInfoData tmpShopData = null;
					// soldOut ??? 0????????? ????????? ?????? ????????????.
					if ("0".equals(data.getSoldOut())) {
						tmpShopData = shopInfoDataDao.selectShopData(param);
						logger.debug("tmpShopData - {}", tmpShopData);

						if(tmpShopData!=null) {
							logger.debug("selectShopData liveChk - {}, prdtPrmct - {}", tmpShopData.getLiveChk(), tmpShopData.getPrdtPrmct());

							// 0 ????????? liveChk??? ???????????? ?????????
							data.setLiveChk( tmpShopData.getLiveChk() );
						}
						else {
							data.setLiveChk("Y");
						}
					}
					else if ("1".equals(data.getSoldOut())) {
						data.setLiveChk("S");
					}
					else if ("2".equals(data.getSoldOut())) {
						data.setLiveChk("Y");
					}
					else {
						data.setLiveChk("Y");
					}

					logger.debug("data soldout {}, livechk {}", data.getSoldOut(), data.getLiveChk());

					logger.debug("data prdtPrmct 1 - {}", data.getPrdtPrmct());
					// ??????????????? 0?????? ????????? ??????????????? ????????????.
					BigDecimal prdtPrmct = new BigDecimal(data.getPrdtPrmct());

					if( prdtPrmct.intValue()<0) {
						if( tmpShopData==null ) {
							tmpShopData = shopInfoDataDao.selectShopData(param);
						}

						if( tmpShopData!=null) {
							logger.debug("selectShopData tmpShopData - {}", tmpShopData);
							data.setPrdtPrmct(tmpShopData.getPrdtPrmct());
						}
						else {
							data.setPrdtPrmct("0");
						}
					}
					if( prdtPrmct.intValue()<0 ) {
						data.setPrdtPrmct("0");
					}
					logger.debug("data prdtPrmct 2 - {}", data.getPrdtPrmct());
				}
			}
		} catch (Exception e) {
			result = 1;
			logger.error("err insertShopData data - {}", e, data);
		}
		return result;
	}

	/**
	 * >>> Kafka ??? ?????? ??????????????? ??????
	 * ????????? ?????? ??? ????????? ?????? ??????????????? ?????? ?????? (?????? ??? ???????????????????????? ?????? ???????????? ?????????)
	 * @param ShopStatsInfoData data
	 * @return int
	 * @throws IOException
	 */
	public int dumpShopStatsData(ShopStatsInfoData data)  {
		try{
			if(G.MOBILE.equals(data.getPlatform())){
			}else{
			}
			logger.debug("add list - {}", data.generateKey());

			return 0;
		}catch(Exception e){
			logger.error("err dumpShopStatsData msg - {}", e);
			return 1;
		}
	}

	/**
	 * dumpSkyClickData ????????? ????????????.
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

			logger.debug("add list - {}", data.generateKey());
			dumpActData(data );

			return 0;
		}catch(Exception e){
			logger.error("err dumpSkyClickData msg - {}", e);
			return 1;
		}
	}

	/**
	 * dumpSkyChargeData ????????? ????????????.
	 * @param BaseCVData data
	 * @return void
	 * @throws
	 */
	public int dumpSkyChargeData(BaseCVData data) throws IOException{
		try{
			if((G.SKY + G.GUBUN + G.LOWER_M).equals(data.getProduct())){
				// product??? i,b,s ?????? ???????????? ???????????? ?????????.
				data.setProduct(G.MBB);
			}else{
			}
			logger.debug("add list - {}", data.generateKey());

			dumpActData(data );

			return 0;
		}
		catch(Exception e){
			logger.error("err dumpSkyChargeData msg - {}", e);
			return 1;
		}
	}

	/**
	 * dumpChargeLogData ????????? ????????????.
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

			logger.debug("add list - {}", data.generateKey());

			dumpActData( data );

			return 0;
		} catch (Exception e) {
			logger.error("err dumpChargeLogData msg - {}", e);
			return 1;
		}
	}

	/**
	 * dumpDrcData ????????? ????????????.
	 * @param BaseCVData data, MediaScriptData ms, PointData getpoint
	 * @return void
	 * @throws
	 */
	public int dumpDrcData(BaseCVData data) throws IOException {
		int result=1;
		try {
			logger.debug("drcCharge key - {}", data );

//			SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
//			Date date = new Date();
//			String ymd = yyyymmdd.format(date);

//			final String gb = data.getAdGubun(); // data.getGb();
//			final String sc = data.getSiteCode(); // data.getSc();
			boolean clickChk = data.isClickChk();

//			float point = data.getPoint();
//			float mpoint = data.getMpoint();

			String chk_key = G.BANNER + G.GUBUN + data.getAdGubun() + G.GUBUN
					+ data.getMcgb() + G.GUBUN + data.getNo() + G.GUBUN + data.getScriptNo()
					+ G.GUBUN + data.getKno() + G.GUBUN + data.getAdvertiserId();
			if (chk_key.length() > 32) {
				chk_key = chk_key.substring(0, 31);
			}
			data.setKeyCode(chk_key);
//			String chkData = ""; //dao.getClickChkDataInfoBanner(data);
			String kno = "";
			if (StringUtils.isNotEmpty(data.getKno()) && data.getKno().length() > 13) {
				kno = data.getKno().substring(0, 13);
			} else {
				kno = data.getKno() + "";
			}
			data.setYyyymmdd( DateUtils.getDate("yyyyMMdd", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.getSendDate()) ));
			data.setKno(kno);
			data.setType(G.CLICK);

			// ???????????? ????????? ????????? 1??? ???????????? ????????? ??????????????? ??????
			// ???????????? DB?????? ?????? ????????? ????????? ??????????????? ??????
			// data.setClickCnt(1);		// ??????
			if ("08".equals(G.convertPRDT_CODE(data.getProduct()))
					&& (data.getAvalCallTime() >= 1 || data.getDbCnvrsCnt() >= 1)) {		// ??????
				data.setClickCnt(0);
			} else {
				data.setClickCnt(1);
			}

			if (data.getScriptNo()>0 && data.getScriptUserId() != null && data.getScriptUserId().length() > 0) {
				if( G.MCT.equals(data.getProduct()) ) {
					result = 0;
				} else if (G.MBW.equals(data.getProduct()) || G.MBA.equals(data.getProduct())
						|| data.getProduct().equals("mba_no_script")) {
					data.setProduct(G.MBW);
					if ( 8126!=data.getScriptNo() || 8126==data.getScriptNo() && clickChk ) { //!"8126".equals(data.getScriptNo()) || ("8126".equals(data.getScriptNo()) && clickChk)) {
						logger.debug("add list - {}", data.generateKey());
						result=0;
					}
				} else if (!"PE".endsWith(data.getAdGubun() ) && !"CA".endsWith(data.getAdGubun() )) {
					logger.debug("add list - {}", data.generateKey());
					result=0;
				}


				// ????????? ???????????? ?????? ?????????

				//if (StringUtils.isNotEmpty(data.getMobonLinkCate()) && ( 8126!=data.getScriptNo() || 8126==data.getScriptNo() )) {

				if(data.isClickChk()) {
					// ??????????????????

					BaseCVData ms = selectDao.selectMediaInfo(data);

					if( ms!=null ) {
						ExternalLinkageData link = new ExternalLinkageData();
						link.setSdate(data.getYyyymmdd());
						link.setUserid(data.getAdvertiserId()); // ??????????????????
						link.setSite_code(data.getSiteCode()); // ???????????????
						link.setGubun(G.AD); // ???????????????
						link.setMedia_site(data.getMediasiteNo());
						link.setMedia_code(data.getScriptNo());
						link.setMedia_id(data.getScriptUserId());
						link.setAd_type(ms.getAD_TYPE().replace(G.GUBUN, "x"));
						//link.setZoneid(data.getMobonLinkCate());
						link.setType("C");
						link.setClickcnt_mobon(1);
						link.setTransmit(G.SEND);
						link.setPoint(data.getMpoint());
						link.setDumpType(G.EXTERNALCHARGE);

						link.setSend_tp_code("03");
						link.setType("SEND");
						link.setHh(data.getHh());
						link.setClickcnt(1);
//						ExternalInfoData tmp = link.toExternalInfoData();
						//if ( "Consumer".equals(profileId) ) {	// ?????????
							//sumObjectManager.appendData(tmp, false);
						//}
					}
				}
			}
			dumpActData(data );

			return result;
		} catch (Exception e) {
			logger.error("err dumpDrcData msg - {}", e);
			return 1;
		}
	}

	/**
	 * dumpNormalViewLogData ????????? ????????????.
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

					logger.debug("add list - {}", data.generateKey());

					dumpActData(data );

					result = 0;
				}
			}
			return result;
		}catch(Exception e){
			logger.error("err dumpNormalViewLogData msg - {}", e);
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
			logger.error("err dumpNormalViewLogData msg - {}", e);
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
			logger.error("err dumpNormalViewLogData msg - {}", e);
			return 1;
		}
	}


	/**
	 * dumpMobileChargeLogData ????????? ????????????.
	 * @param BaseCVData data
	 * @return void
	 * @throws
	 */
	public int dumpMobileChargeLogData(BaseCVData data) throws IOException{
		try{
			// mba, mbw
			logger.debug("add list - {}", data.generateKey());

			dumpActData(data );

			return 0;
		}catch(Exception e){
			logger.error("err dumpMobileChargeLogData msg - {}", e);
			return 1;
		}
	}

	/**
	 * dumpShopConData ????????? ????????????.
	 * @param BaseCVData data, MediaScriptData ms
	 * @return void
	 * @throws
	 */
	public int dumpShopConData(BaseCVData data) throws IOException{
		try{

			data.setYyyymmdd( DateUtils.getDate("yyyyMMdd", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.getSendDate()) ));
			data.setScriptUserId(data.getScriptUserId());
			data.setPoint(data.getPoint());
			data.setType(G.CLICK);
			data.setClickCnt(1);

			if( G.MBW.equals(data.getProduct()) ){
			}else{
			}

			logger.debug("add list - {}", data.generateKey());

			dumpActData(data );
			return 0;
		}catch(Exception e){
			logger.error("err dumpShopConData msg - {}", e);
			return 1;
		}
	}

	public int dumpRtbDrcData(BaseCVData data) {
		try {
			dumpActData( data );

			BaseCVData rtb = selectDao.selectRtbInfo(data);
			if( rtb!=null ) {
				data.setRtbUseMoneyYn( rtb.getRtbUseMoneyYn() );

				logger.debug("rtb - {}", rtb);
				return 1;
			} else {
				return 0;
			}
		}catch(Exception e) {
			logger.error("err dumpRtbDrcData ", e);
			return 1;
		}
	}

	/**
	 * dumpShortCutData ????????? ????????????.
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

//			SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
//			Date date=new Date();
//			String ymd=yyyymmdd.format(date);
			if (G.CLICK.equals(data.getType())) {
				String chk_key = "";
				data.setKeyCode(chk_key);

				String chkData = ""; //dao.getClickChkDataInfoBanner( data.toBaseCVData() );
				if( data.getYyyymmdd()!=null && "".equals(data.getYyyymmdd()) ) {
					data.setYyyymmdd( DateUtils.getDate("yyyyMMdd", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.getSendDate()) ));
				}
				data.setType(G.CLICK);
				data.setClickCnt(1);

				// ?????? ???????????? ????????????
				if(chkData==null || chkData.length()<1){
					logger.debug("add list - {}", data.generateKey());

					result = 0;
				} else {
				}

				// ?????? ????????? ACTION_LOG ???????????? ?????? (???????????? ?????? ????????? ??????????????? ????????? ???????????? ???????????? ???????????? ??????)
				dumpActData(data);

			}else{
				logger.debug("type is not click");
			}
		}catch(Exception e){
			logger.error("err dumpShortCutData 2 msg - {}", e);
		}
		return result;
	}

	/**
	 * dumpConvLogData ????????? ????????????.
	 * @param ConvData data, PointData point
	 * @return void
	 * @throws
	 */
	public int dumpConvLogData(ConvData data) {
		ConvData rs = null;

		// (s=1999) ????????? ???????????? ????????? ????????????.
		if( data.isConversionDirect() ) {
			return 0;
		}

		String NO,ADPRODUCT,IN_HOUR,ACTGUBUN,ORDCODE;
		NO=ADPRODUCT=IN_HOUR=ORDCODE=ACTGUBUN="";

		int indirect, direct;
		indirect=data.getCookieInDirect();
		direct=data.getCookieDirect();

		logger.debug("data - {}", data );

//		if (Arrays.asList("hec2725","mayblue","livart","stylec2018","chakanshoes","popcon","gayeon3","napkin","vittz1","skmagic2020","dukomall","jayhan","realcoco").contains(data.getAdvertiserId())) {
//			if("rebuild".equals(data.getRegUserId())) {
//
//			}else {
//
//				return 1;
//			}
//		}

		try{
			Date today = new Date();
			Date indirsalas = new Date();
			indirsalas.setTime( ( today.getTime() + (1000L*60*60*24* (indirect * -1)) - (1000L*60*convDelayTimeMinute) ) );	// 5????????? - (1000L*60*5)

			SimpleDateFormat dFormat = new SimpleDateFormat ( "yyyyMMdd" );
			String yyyymmdd = dFormat.format ( indirsalas.getTime ( ) ).toString();

			if( data.getScriptNo()==0 && !"rebuild".equals(data.getRegUserId())){ //StringUtils.isEmpty(data.getScriptNo() ) ){//indirect conversion
				logger.info("chking Conv fail scriptNo is 0, keyIp-{}, ordCode-{}", data.getKeyIp(), data.getOrdCode());
				return 1;
			} else {// direct conversion
				//data.setDirect(direct);

				logger.info("ip {}, loginIp {}, ipinfo {}", data.getKeyIp(), data.getCrossLoginIp(), data.getIpInfoList());
				if( "".equals(data.getSiteCode()) || "".equals(data.getProduct()) || "99".equals(data.getAdGubun())) {
					return 1;
				}
				
				HashMap<String, Object> map = new HashMap<String, Object>();
				ArrayList<String> ipList = new ArrayList<String>();
	        	ipList.add(data.getKeyIp());
		        if( data.getCrossLoginIp().length()>2 && data.getCrossLoginIp().split(",").length>0 ) {
		            JSONArray jsonArray = (JSONArray) new JSONParser().parse(data.getCrossLoginIp());
		            for(int i=0;i<jsonArray.size();i++){
		    	        Object jsonObj = jsonArray.get(i);
		    	        ipList.add(jsonObj.toString());
		            }
		        }
		        StringBuffer ipInfoList= new StringBuffer();
		        StringTokenizer TipInfoList= new StringTokenizer( data.getIpInfoList(), "|" );
		        while(TipInfoList.hasMoreElements()) {
		            if(TipInfoList.hasMoreTokens()) {
			            String ipinfo= TipInfoList.nextToken();
			            boolean bol = false;
			        	if(bol && !ipinfo.equals(data.getKeyIp())) {
			        		ipList.add(ipinfo);
			        		ipInfoList.append(ipinfo+"|");
			        	}
		            }
		        }

		        map.put("keyIp", ipList);
		        map.put("ipInfoList", ipInfoList.toString());
				map.put("advertiserId", data.getAdvertiserId()); //.getUserId().trim());
				map.put("scriptNo", data.getScriptNo()); //.getSite_code());
				map.put("siteCode", data.getSiteCode()); //.getMedia_code());
				map.put("adGubun", data.getAdGubun());
				map.put("product", data.getProduct());
				map.put("direct", direct);
				map.put("yyyymmdd", yyyymmdd);
				map.put("origin_yyyymmdd", data.getYyyymmdd());
				map.put("sendDate", data.getSendDate());
				map.put("adGubunCode", data.getAdGubunCode());
				map.put("productCode", data.getProductCode());
				map.put("unExposureYn","N");

				logger.info("Conv map - {}", map);
				
				if ("90".equals(data.getTrkTpCode())) {
//					if("rebuild".equals(data.getRegUserId()) && "".equals(data.getSiteCode())) {
//						rs = convDataDao.convLogActData_REBUILD(map);
//					} else {
						rs = convDataDao.convLogActData_C(map);
//					}
				} else {
					//?????????????????? lastClickDate ????????? regDate ???????????? ??????
					map.put("lastClickDate", data.getLastClickTime());
					rs = convDataDao.convTrkLogActData(map);
				}


//				boolean bool=false;
//				if(bool){	// yyyymmdd, ip, adver_id
//					ConvData rs2 = convDataDao.ConvLogActData_Test_C(map);
//					// action Log ?????????
//					try {
//						if (rs==null && rs2!=null) {
//							JSONObject originData = JSONObject.fromObject(map);
//							JSONObject rs2Object = JSONObject.fromObject(rs2);
//							String fileName = String.format("%s%s.%s", actionLogPath, "kafka-consumer.action.log", DateUtils.getDate("yyyy-MM-dd_HH"));
//							org.apache.commons.io.FileUtils.writeStringToFile(new File( fileName )
//									, String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), originData, rs2Object), "UTF-8", true);
//						}
//
//					} catch (Exception e) {
//						logger.error("action Data write file Error - {}, IP -{}",e, map.get("keyIp"));
//					}
//				}
//
//				if(bool){	// yyyymmdd, ip(like), adver_id
//					ConvData rs3= convDataDao.ConvLogActData_Test2_C(map);
//					// action Log ?????????
//					try {
//						if (rs==null && rs3!=null) {
//							JSONObject originData = JSONObject.fromObject(map);
//							JSONObject rs2Object = JSONObject.fromObject(rs3);
//							String fileName = String.format("%s%s.%s", actionLogPath, "kafka-consumer.action.iplike.log", DateUtils.getDate("yyyy-MM-dd_HH"));
//							org.apache.commons.io.FileUtils.writeStringToFile(new File( fileName )
//									, String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), originData, rs2Object), "UTF-8", true);
//						}
//
//					} catch (Exception e) {
//						logger.error("action Data write file Error2 - {}, IP -{}",e, map.get("keyIp"));
//					}
//				}

				if(rs == null){ // ?????? ?????? ????????? null ??? ?????? ????????? ???????????? ??????
					rs = convDataDao.convLogUnexposureActData_C(map);
					data.setNoExposureYN(true); // ????????? ?????? true
					map.put("unExposureYn","Y");
				}

				//?????? action_log ????????? ????????? re != null ??? ?????? click house action_renew_log ????????? ?????? ?????? ????????? ??????
				if(rs != null) {
					Map<String , Object> actionAdditionData = clickHouseDao.selectActionLogData(map);
					if (actionAdditionData != null
							&& actionAdditionData.size() > 0) {
						JSONObject obj = new JSONObject();
						obj.put("mobon_ad_grp_id_i", StringUtils.trimToNull2(actionAdditionData.get("MOBON_AD_GRP_ID_I"), ""));
						obj.put("ad_grp_tp_code_i", StringUtils.trimToNull2(actionAdditionData.get("AD_GRP_TP_CODE_I"),""));
						obj.put("image_tp_code", StringUtils.trimToNull2(actionAdditionData.get("IMG_TP_CODE"),""));

						obj.put("mobon_ad_grp_id_c", StringUtils.trimToNull2(actionAdditionData.get("MOBON_AD_GRP_ID_C"),""));
						obj.put("ad_grp_tp_code_c", StringUtils.trimToNull2(actionAdditionData.get("AD_GRP_TP_CODE_C"),""));
						obj.put("cp_tp_code", StringUtils.trimToNull2(actionAdditionData.get("CP_TP_CODE"),""));

						JSONArray arr = new JSONArray();
						arr.add(obj);

						data.setMobAdGrpData(net.sf.json.JSONArray.fromObject(arr));
						data.setAdvrtsStleTpCode(StringUtils.trimToNull2((String) actionAdditionData.get("ADVRTS_STLE_TP_CODE"),""));
					}
					//logger.info("Action_Renew_Log Data - {} - {}" , data.getMobAdGrpData(), data.getAdvrtsStleTpCode());
				}

				if(rs!=null) {
					data.setClickRegDate(rs.getClickRegDate());
//					data.setKeyIp(rs.getKeyIp());
					data.setPosIpinfo(rs.getPosIpinfo());
					data.setPartdt(rs.getPartdt());

					logger.info("Conv keyIp - {}, no - {}, orderno - {}, product - {}, inhour - {}, direct - {}, sendDate - {}, price - {}, diffClickTime -{} , broswerDirect - {}"
							, data.getKeyIp(), rs.getNo(), data.getOrdCode(), rs.getProduct(), rs.getInHour(), data.getDirect(), data.getSendDate(), data.getPrice(), rs.getDiffClickTime(), data.getBrowserDirect());

					ConvData convCnt = convDataDao.selectCONVERSION_LOG_IPCNT(data);
					logger.info("ConvAbusing convCnt: keyIp:{}, ordCnt:{}, 1Min:{}", data.getKeyIp(), convCnt.getOrdCnt(), convCnt.getUnder1Min());

					if( "1".equals(data.getPrice()) && rs.getDiffClickTime()<10 ) {
//						if( rs.getDiffClickTime()<10 ) {
							//rs=null;
							logger.info("Conv fail rs=null, keyIp-{}, ordCode-{}", data.getKeyIp(), data.getOrdCode());

//							ConvData abusingData= SerializationUtils.clone(data);
//			                sumObjectManager.appendConvAbusingData(abusingData, CNVRS_ABUSING_TP_CODE.SEC_10.getValue());
//							logger.info("ConvAbusing sec_10, keyIp:{}, ordCode:{}", data.getKeyIp(), data.getOrdCode());
							data.getAbusingMap().put(CNVRS_ABUSING_TP_CODE.SEC_10.getValue(), CNVRS_ABUSING_TP_CODE.SEC_10.getValue());
//						}
					}
					else if( rs.getDiffClickTime()<=5 ) {
//						ConvData abusingData= SerializationUtils.clone(data);
//		                sumObjectManager.appendConvAbusingData(abusingData, CNVRS_ABUSING_TP_CODE.SEC_5.getValue());
//						logger.info("ConvAbusing sec_5, keyIp:{}, ordCode:{}", data.getKeyIp(), data.getOrdCode());
		                data.getAbusingMap().put(CNVRS_ABUSING_TP_CODE.SEC_5.getValue(), CNVRS_ABUSING_TP_CODE.SEC_5.getValue());
//					}
//					else if( rs.getDiffClickTime()<=10 ) {
//						ConvData abusingData= SerializationUtils.clone(data);
//		                sumObjectManager.appendConvAbusingData(abusingData, CNVRS_ABUSING_TP_CODE.SEC_10.getValue());
//						logger.info("ConvAbusing sec_10, keyIp:{}, ordCode:{}", data.getKeyIp(), data.getOrdCode());
					}
					else if(convCnt.getUnder1Min()>0) {
//						ConvData abusingData= SerializationUtils.clone(data);
//		                sumObjectManager.appendConvAbusingData(abusingData, CNVRS_ABUSING_TP_CODE.UNDER_1MIN.getValue());
//		                logger.info("ConvAbusing under_1sec, keyIp:{}, ordCode:{}", data.getKeyIp(), data.getOrdCode());
		                data.getAbusingMap().put(CNVRS_ABUSING_TP_CODE.UNDER_1MIN.getValue(), CNVRS_ABUSING_TP_CODE.UNDER_1MIN.getValue());
					}

				} else {
				}
				logger.info("Conv IP-{}, rs - {}", data.getKeyIp(), rs);
			}

			if(rs != null){
				try{
					NO = rs.getNo()+""; //.getNO();
					ADPRODUCT = rs.getProduct(); //.getADPRODUCT();
					rs.setAdvertiserId(data.getAdvertiserId());

					/**
					 * ????????? ????????? ????????? ?????? mba ??????, ????????? mbw ??? ?????? (?????????????????? ????????? ???????????? ???????????? ?????? : ????????? ??????)
					 * mbb : ????????????????????????
					 * mba : ????????????
					 * mbw : ????????????
					 * mbe : ????????? ????????????
					 */
					//????????? ???
					if(G.MBA.equals(ADPRODUCT)){
						ADPRODUCT = G.MBW;	// ?????????????????? ???????????? ?????????????????? ?????????.
					}
					if(G.MBA.equals(data.getType())){
						if(data.getDirect()==1){ //.equals("1")){
							IN_HOUR="24";
							data.setDirect(1);
						}else if(data.getDirect()==24){ //.equals("24")){
							IN_HOUR="24";
							data.setDirect(0);
						}else{
							IN_HOUR="0";
							data.setDirect(0);
						}
					}else{
						IN_HOUR = rs.getInHour(); //.getIN_HOUR();
						if( IN_HOUR.equals("0") ) data.setDirect(0);
					}
					data.setInHour(IN_HOUR);


					logger.info("Conv abtest keyIp - {}, adverId-{}, ordcode-{}, direct-{}, inhour-{}, direct2-{}, inhour2-{}"
							, data.getKeyIp(), data.getAdvertiserId(), data.getOrdCode(), data.getDirect(), data.getInHour(), data.getDirect2(), data.getInHour2());
					// ?????? 60??? abtest > 120???
					{
						if(rs.getDiffClickTime()<=21600) {
							data.setDirect2(1);
							data.setInHour2("24");
						} else {
							data.setDirect2(0);
							data.setInHour2(data.getInHour());
						}
					}

					ACTGUBUN = rs.getType(); //.getACTGUBUN();
					if( ACTGUBUN.equals(G.ICO) ) ACTGUBUN=G.CLICK;

					// ??????????????? ??????
					if( "hanasys".equals(data.getAdvertiserId()) ) {
						data.setAbusingMap( new HashMap() );
					}

				}catch(Exception e){
					logger.error("err dumpConvLogData 2 msg - {}", e);
					logger.info("chking data: "+ data);
					return 1;
				}
			}

			if( rs != null && StringUtils.isNotEmpty(NO) ){
				ConvData ordcode = convDataDao.selectCNVRS_NCL(data);
				if(ordcode != null) ORDCODE= ordcode.getOrdCode();
				//logger.info("Conv IP - {}, advertiserId - {}, ORDCODE - {}", data.getKeyIp(), data.getAdvertiserId(), ORDCODE);
				if( !StringUtils.isEmpty(ORDCODE) ){
					if("90".equals(data.getTrkTpCode())) {
						logger.info("Conv fail ordcode!=null, keyIp-{}, ordCode-{}", data.getKeyIp(), data.getOrdCode());
					} else { // ????????? ????????? ?????? ??????????????? ????????? ??? ???????????? ??? ????????? append???.
						logger.info("Conv ordcode!=null, TRK CONV DATA" );
						return 0;
					}
				}
				else {
					String pnm = data.getPnm();
					if(pnm==null)pnm="";
					if(pnm.length()>150) pnm=pnm.substring(0,149);
					if(pnm.indexOf("?")>0) pnm = "";	// pnm ???????????? ??????
					URL url = null;
					try {
						// referer ????????? ????????? ????????? ?????? ??????????????? ?????? (????????? referer ????????? ???????????? ????????? ????????? ???????????? ????????? ?????? ????????? ???!
						url = new URL(data.getOrdRFUrl());
					} catch (MalformedURLException e) {
						logger.error("No connection url [[[[" + data.getOrdRFUrl() + "]]]] !!!");
					}
					// ?????? ?????? ???????????? ???????????? ?????? null ???????????? ??????(????????? ????????? ????????? 0 ?????? ??????)
					/* ms.media_id='shoppul' ????????? ????????? MOBON_YN='Y' @jtwon */
					if("shoppul".equals(rs.getScriptUserId())){ //.getMEDIA_ID()) ){
						data.setMobonYn("Y");
					}

					logger.info("Conv IN_HOUR - {}, direct - {}, indirect - {}, data.getDirect() - {}", IN_HOUR, direct, indirect, data.getDirect());

					//data.setCnvrsTpCode("");
					if( rs.getPosIpinfo()>0 ){
						logger.info("Conv ????????? ????????????");
						data.setDirect(0);
						IN_HOUR = "0";
						data.setCnvrsTpCode("03");

					} else {
						/*
						 * ????????? ???????????? ??????
						 */
						ConversionInfoFilter filter = new ConversionInfoFilter();
						filter.setServiceCode(ConversionCode.SERVICE_CODE);
						filter.setScriptNo( rs.getScriptNo() );
						filter.setUserId(data.getAdvertiserId());
						filter.setPcode(data.getpCode());
						filter.setOrderCode(data.getOrdCode());
						filter.setOrderPrice(StringUtils.number(data.getPrice()));
						filter.setClientId(data.getKeyIp());
						//filter.setPastClickMinute(rs.getPastClickMinute());
						filter.setConversionType(IN_HOUR, data.getDirect()+"" );
						filter.setDirectHour(direct);
						filter.setIndirectHour(indirect*24);
						filter.setActionLogNo(NO);
						filter.setEtc(data.getSiteCode() );
						logger.info("Conv ????????? ?????? ??? : " + rs.getPastClickMinute());

						boolean isInsert = conversionServiceSimple.isAvailableFrequency(filter, data);
						if(!isInsert){	// false
						  logger.info("Conv fail freq, keyIp-{}, ordCode-{}", data.getKeyIp(), data.getOrdCode());

						  return 1;
						}

						IN_HOUR = ConversionService.convertInHourCode(filter.getChargeCode());
						data.setDirect( Integer.parseInt(ConversionService.convertDirectCode(filter.getChargeCode())) );

						// ?????? 60??? abtest
						if(filter.isChargeCodeChanged()){
							data.setInHour2(IN_HOUR);
							data.setDirect2(data.getDirect());
						}
					}

					logger.info("Conv abtest keyIp - {}, adverId-{}, ordcode-{}, direct-{}, inhour-{}, direct2-{}, inhour2-{}"
							, data.getKeyIp(), data.getAdvertiserId(), data.getOrdCode(), data.getDirect(), data.getInHour(), data.getDirect2(), data.getInHour2());

					data.setNo( Long.parseLong(NO) );
					data.setOrdRFUrl( url==null?"":url.getAuthority() );
					data.setOrdPcode("");
					data.setScriptUserId(rs.getScriptUserId());
					data.setScriptNo(rs.getScriptNo());
					data.setSiteCode(rs.getSiteCode());
					data.setAdGubun(rs.getAdGubun());
					data.setProduct(rs.getProduct());
					data.setType(ACTGUBUN);
					data.setMcgb(rs.getMcgb());
					data.setPnm(pnm);
					data.setInHour(IN_HOUR);
					data.setShopconSerealNo(0);
					data.setShopconWeight(0);

					// 2019-09-18
					// ??????????????? type??? product ??? ???????????? ???????????? ????????????
					data.setProduct( rs.getProduct() );
					data.setType( rs.getType() );

					// ??????????????????(?????? ???????????? 30???) ?????? ??????????????? ??????
					if(rs.getDiffClickTime()<=1800 ) {	// 30???
						data.setBrowserDirect("Y");
						data.setInHour("24");
					}
					if(rs.getDiffClickTime()<=(1800*6*2)) {	// 6??????
						data.setDirect(1);
						data.setInHour("24");
					}

					logger.debug("add list - {}", data.generateKey());

					// ????????? ???????????? ?????? ?????????
//					String convType = 1 == data.getDirect() ? "1" :"24".equals(IN_HOUR) ? "24" : "0";


					return 0;
				}
			}

			logger.info("chking Conv fail rs=null, keyIp-{}, ordCode-{}", data.getKeyIp(), data.getOrdCode());
			return 1;
		}catch(SQLException e){
			logger.error("err dumpConvLogData 3 msg - {}", e);
			return 1;
		}catch(Exception e){
			logger.error("err dumpConvLogData 1, data - {}", data, e);
			return 1;
		}
	}
	/**
	 * dumpConvLogData ????????? ????????????.
	 * @param ConvData data, PointData point
	 * @return void
	 * @throws
	 */
	public int dumpConvPcodeLogData(ConvData data) {
		ConvData rs = null;

		// (s=1999) ????????? ???????????? ????????? ????????????.
		if( data.isConversionDirect() ) {
			return 0;
		}

		String NO,ADPRODUCT,IN_HOUR,ACTGUBUN,ORDCODE;
		NO=ADPRODUCT=IN_HOUR=ORDCODE=ACTGUBUN="";

		int indirect, direct;
		indirect=data.getCookieInDirect();
		direct=data.getCookieDirect();

		logger.debug("data - {}", data );

		try{
			Date today = new Date();
			Date indirsalas = new Date();
			indirsalas.setTime( ( today.getTime() + (1000L*60*60*24* (indirect * -1)) - (1000L*60*convDelayTimeMinute) ) );	// 5????????? - (1000L*60*5)

			SimpleDateFormat dFormat = new SimpleDateFormat ( "yyyyMMdd" );
			String yyyymmdd = dFormat.format ( indirsalas.getTime ( ) ).toString();

			if( data.getScriptNo()==0 ){ //StringUtils.isEmpty(data.getScriptNo() ) ){//indirect conversion
				logger.info("ConvPcode fail scriptNo is 0, keyIp-{}, ordCode-{}", data.getKeyIp(), data.getOrdCode());
				return 1;
			} else {// direct conversion
				//data.setDirect(direct);

				logger.info("ip {}, loginIp {}, ipInfoList {}", data.getKeyIp(), data.getCrossLoginIp(), data.getIpInfoList());
				HashMap<String, Object> map = new HashMap<String, Object>();

				ArrayList<String> ipList = new ArrayList<String>();
	        	ipList.add(data.getKeyIp());
		        if( data.getCrossLoginIp().length()>2 && data.getCrossLoginIp().split(",").length>0 ) {
		            JSONArray jsonArray = (JSONArray) new JSONParser().parse(data.getCrossLoginIp());
		            for(int i=0;i<jsonArray.size();i++){
		    	        Object jsonObj = jsonArray.get(i);
		    	        ipList.add(jsonObj.toString());
		            }
		        }
		        StringBuffer ipInfoList= new StringBuffer();
		        StringTokenizer TipInfoList= new StringTokenizer( data.getIpInfoList(), "|" );
		        while(TipInfoList.hasMoreElements()) {
		            if(TipInfoList.hasMoreTokens()) {
			            String ipinfo= TipInfoList.nextToken();
			        	if(!ipinfo.equals(data.getKeyIp())) {
			        		ipList.add(ipinfo);
			        		ipInfoList.append(ipinfo+"|");
			        	}
		            }
		        }

		        map.put("keyIp", ipList);
		        map.put("ipInfoList", ipInfoList.toString());
				map.put("advertiserId", data.getAdvertiserId()); //.getUserId().trim());
				map.put("scriptNo", data.getScriptNo()); //.getSite_code());
				map.put("siteCode", data.getSiteCode()); //.getMedia_code());
				map.put("adGubunCode", data.getAdGubunCode());
				map.put("product", data.getProduct());
				map.put("direct", direct);
				map.put("yyyymmdd", yyyymmdd);
				map.put("origin_yyyymmdd", data.getYyyymmdd());
				map.put("sendDate", data.getSendDate());

				map.put("pcode", data.getpCode());

				logger.info("ConvPcode map - {}", map);
				if ("90".equals(data.getTrkTpCode())) {
					rs = convDataDao.convLogActData_CPcode(map);
				} else {
					//?????????????????? lastClickDate ????????? regDate ???????????? ??????
					map.put("lastClickDate", data.getLastClickTime());
					rs = convDataDao.convTrkLogActData_Pcode(map);
				}

				if(rs!=null) {
					data.setClickRegDate(rs.getClickRegDate());
//					data.setKeyIp(rs.getKeyIp());
					logger.info("ConvPcode keyIp - {}, no - {}, orderno - {}, product - {}, inhour - {}, direct - {}, sendDate - {}, price - {}, diffClickTime -{}"
							, data.getKeyIp(), rs.getNo(), data.getOrdCode(), rs.getProduct(), rs.getInHour(), data.getDirect(), data.getSendDate(), data.getPrice(), rs.getDiffClickTime());

					if( "1".equals(data.getPrice()) ) {
						if( rs.getDiffClickTime()<10 ) {
							rs=null;
							logger.info("ConvPcode fail rs=null, keyIp-{}, ordCode-{}", data.getKeyIp(), data.getOrdCode());
						}
					}
				} else {
				}
				logger.info("ConvPcode IP-{}, rs - {}", data.getKeyIp(), rs);
			}

			if(rs != null){
				try{
					NO = rs.getNo()+""; //.getNO();
					ADPRODUCT = rs.getProduct(); //.getADPRODUCT();

					// ??????????????????(?????? ???????????? 30???) ?????? ??????????????? ??????
					if(rs.getPastClickMinute()<=30) {
						data.setDirect(1);
					}

					/**
					 * ????????? ????????? ????????? ?????? mba ??????, ????????? mbw ??? ?????? (?????????????????? ????????? ???????????? ???????????? ?????? : ????????? ??????)
					 * mbb : ????????????????????????
					 * mba : ????????????
					 * mbw : ????????????
					 * mbe : ????????? ????????????
					 */
					//????????? ???
					if(G.MBA.equals(ADPRODUCT)){
						ADPRODUCT = G.MBW;	// ?????????????????? ???????????? ?????????????????? ?????????.
					}
					if(G.MBA.equals(data.getType())){
						if(data.getDirect()==1){ //.equals("1")){
							IN_HOUR="24";
							data.setDirect(1);
						}else if(data.getDirect()==24){ //.equals("24")){
							IN_HOUR="24";
							data.setDirect(0);
						}else{
							IN_HOUR="0";
							data.setDirect(0);
						}
					}else{
						IN_HOUR = rs.getInHour(); //.getIN_HOUR();
						if( IN_HOUR.equals("0") ) data.setDirect(0);
					}
					data.setInHour(IN_HOUR);

					logger.info("ConvPcode abtest keyIp - {}, adverId-{}, ordcode-{}, direct-{}, inhour-{}, direct2-{}, inhour2-{}"
							, data.getKeyIp(), data.getAdvertiserId(), data.getOrdCode(), data.getDirect(), data.getInHour(), data.getDirect2(), data.getInHour2());
					// ?????? 60??? abtest > 120???
					{
						if(rs.getDiffClickTime()<=21600) {
							data.setDirect2(1);
							data.setInHour2("24");
						} else {
							data.setDirect2(0);
							data.setInHour2(data.getInHour());
						}
					}

					ACTGUBUN = rs.getType(); //.getACTGUBUN();
					if( ACTGUBUN.equals(G.ICO) ) ACTGUBUN=G.CLICK;

				}catch(Exception e){
					logger.error("err dumpConvLogData 2 msg - {}", e);
					return 1;
				}
			}

			if( rs != null && StringUtils.isNotEmpty(NO) ){
				data.setpCode(rs.getpCode());

				ConvData ordcode = convDataDao.selectCNVRS_PCODE_RECOM_NCL(data);
				if(ordcode != null) ORDCODE= ordcode.getOrdCode();
				//logger.info("ConvPcode IP - {}, advertiserId - {}, ORDCODE - {}", data.getKeyIp(), data.getAdvertiserId(), ORDCODE);
				if( !StringUtils.isEmpty(ORDCODE) ){
					logger.info("ConvPcode fail ordcode!=null, keyIp-{}, ordCode-{}", data.getKeyIp(), data.getOrdCode());
				}
				else {
					//logger.info("ConvPcode ::::::ORDCODE:::::: [{}] != [{}] is null goto next step", data.getOrdCode(), ORDCODE);

					String pnm = data.getPnm();
					if(pnm==null)pnm="";
					if(pnm.length()>150) pnm=pnm.substring(0,149);
					if(pnm.indexOf("?")>0) pnm = "";	// pnm ???????????? ??????
					URL url = null;
					try {
						// referer ????????? ????????? ????????? ?????? ??????????????? ?????? (????????? referer ????????? ???????????? ????????? ????????? ???????????? ????????? ?????? ????????? ???!
						url = new URL(data.getOrdRFUrl());
					} catch (MalformedURLException e) {
						logger.error("No connection url [[[[" + data.getOrdRFUrl() + "]]]] !!!");
					}
					// ?????? ?????? ???????????? ???????????? ?????? null ???????????? ??????(????????? ????????? ????????? 0 ?????? ??????)
					/* ms.media_id='shoppul' ????????? ????????? MOBON_YN='Y' @jtwon */
					if("shoppul".equals(rs.getScriptUserId())){ //.getMEDIA_ID()) ){
						data.setMobonYn("Y");
					}

					logger.info("ConvPcode IN_HOUR - {}, direct - {}, indirect - {}, data.getDirect() - {}", IN_HOUR, direct, indirect, data.getDirect());


					//data.setCnvrsTpCode("");
					if( rs.getPosIpinfo()>0 ){
						logger.info("ConvPcode ????????? ????????????");
						data.setDirect(0);
						IN_HOUR = "0";
						data.setCnvrsTpCode("03");

					} else {
						/*
						 * ????????? ???????????? ??????
						 * */
						ConversionInfoFilter filter = new ConversionInfoFilter();
						filter.setServiceCode(ConversionCode.SERVICE_CONVERSION_PCODE);
						filter.setScriptNo( rs.getScriptNo() );
						filter.setUserId(data.getAdvertiserId());
						filter.setPcode(rs.getpCode());
						filter.setOrderCode(data.getOrdCode());
						filter.setOrderPrice(StringUtils.number(data.getPrice()));
						filter.setClientId(data.getKeyIp());
						//filter.setPastClickMinute(rs.getPastClickMinute());
						filter.setConversionType(IN_HOUR, data.getDirect()+"" );
						filter.setDirectHour(direct);
						filter.setIndirectHour(indirect*24);
						filter.setActionLogNo(NO);
						filter.setEtc(data.getSiteCode() );
						logger.info("ConvPcode ????????? ?????? ??? : " + rs.getPastClickMinute());

						boolean isInsert = conversionServiceSimple.isAvailableFrequency(filter, data);
						if(!isInsert){	// false
						  logger.info("ConvPcode fail freq, keyIp-{}, ordCode-{}", data.getKeyIp(), data.getOrdCode());
						  return 1;
						}

						IN_HOUR = ConversionService.convertInHourCode(filter.getChargeCode());
						data.setDirect( Integer.parseInt(ConversionService.convertDirectCode(filter.getChargeCode())) );

						// ?????? 60??? abtest
						if(filter.isChargeCodeChanged()){
							data.setInHour2(IN_HOUR);
							data.setDirect2(data.getDirect());
						}
					}

					logger.info("ConvPcode abtest keyIp - {}, adverId-{}, ordcode-{}, direct-{}, inhour-{}, direct2-{}, inhour2-{}"
							, data.getKeyIp(), data.getAdvertiserId(), data.getOrdCode(), data.getDirect(), data.getInHour(), data.getDirect2(), data.getInHour2());

					//logger.info("ConvPcode ordcode - {}, inHour - {}, direct - {}, product - {}", data.getOrdCode(), IN_HOUR, data.getDirect(), data.getProduct());


//					data.setAdvertiserId(rs.getAdvertiserId());
//					data.setRecomTpCode(rs.getRecomTpCode());
//					data.setSubadgubun(rs.getSubadgubun());
//					data.setRecomTpCode( CodeUtils.getRecomTpCode(data.getAdGubun(), data.getSubadgubun()) );

					if (! rs.getSubadgubun().equals(data.getSubadgubun()) || ! rs.getRecomAlgoCode().equals(data.getRecomAlgoCode()) ) {
						data.setRecomTpCode(rs.getRecomTpCode());
						data.setSubadgubun(rs.getSubadgubun());
						data.setRecomAlgoCode(rs.getRecomAlgoCode());
					} else {
						data.setRecomTpCode( CodeUtils.getRecomTpCode(data.getAdGubun(), data.getSubadgubun()) );
						data.setRecomAlgoCode( CodeUtils.getRecomAlgoCode(data.getSubadgubun(), data.getErgdetail(), ""));
					}

					data.setNo( Long.parseLong(NO) );
					data.setOrdRFUrl( url==null?"":url.getAuthority() );
					data.setOrdPcode("");
					data.setScriptUserId(rs.getScriptUserId());
					data.setScriptNo(rs.getScriptNo());
					data.setSiteCode(rs.getSiteCode());
					data.setAdGubun(rs.getAdGubun());
					data.setProduct(rs.getProduct());
					data.setType(ACTGUBUN);
					data.setMcgb(rs.getMcgb());
					data.setPnm(pnm);
					data.setInHour(IN_HOUR);
					data.setShopconSerealNo(0);
					data.setShopconWeight(0);
					// ??????????????? ????????? action_log ??? ??????????????? ????????????
					data.setClickRegDate(rs.getClickRegDate());

					//data.setRecomTpCode(rs.getRecomTpCode());

					// 2019-09-18
					// ??????????????? type??? product ??? ???????????? ???????????? ????????????
					data.setProduct( rs.getProduct() );
					data.setType( rs.getType() );

					logger.info("data: yyyymmdd:{} advertiserId:{} ordCode:{} platform:{} product:{} adGubunCode:{} subAdGubunCode:{} recomTpCode:{} siteCode:{} scriptNo:{} pCode:{} mobonYn:{} clickRegDate:{} price:{} ordQty:{} "
								, data.getYyyymmdd(), data.getAdvertiserId(), data.getOrdCode()
								, data.getPlatform(), data.getProduct(), data.getAdGubun(), data.getSubAdGubunCode(), data.getRecomTpCode(), data.getSiteCode(), data.getScriptNo(), data.getpCode(), data.getMobonYn(), data.getClickRegDate(), data.getPrice(), data.getOrdQty()
								);

					// ????????? ???????????? ?????? ?????????
//					String convType = 1 == data.getDirect() ? "1" :"24".equals(IN_HOUR) ? "24" : "0";

					return 0;
				}
			}

			logger.info("ConvPcode fail rs=null, keyIp-{}, ordCode-{}", data.getKeyIp(), data.getOrdCode());
			return 1;
		}catch(SQLException e){
			logger.error("err dumpConvLogData 3 msg - {}", e);
			return 1;
		}catch(Exception e){
			logger.error("err dumpConvLogData 1, data - {}", data, e);
			return 1;
		}
	}

	public int dumpConvLogDataRetry(ConvData data) {
		ConvData rs = null;

		// (s=1999) ????????? ???????????? ????????? ????????????.
		if( data.isConversionDirect() ) {
			return 0;
		}

		String NO,ADPRODUCT,IN_HOUR,ACTGUBUN,ORDCODE;
		NO=ADPRODUCT=IN_HOUR=ORDCODE=ACTGUBUN="";

		int indirect, direct;
		indirect=data.getCookieInDirect();
		direct=data.getCookieDirect();

		logger.debug("data - {}", data );

		try{
			Date today = new Date();
			Date indirsalas = new Date();
			indirsalas.setTime( ( today.getTime() + (1000L*60*60*24* (indirect * -1)) - (1000L*60*convDelayTimeMinute) ) );	// 5????????? - (1000L*60*5)

			SimpleDateFormat dFormat = new SimpleDateFormat ( "yyyyMMdd" );
			String yyyymmdd = dFormat.format ( indirsalas.getTime ( ) ).toString();

			if( data.getScriptNo()==0 ){ //StringUtils.isEmpty(data.getScriptNo() ) ){//indirect conversion
				logger.info("Conv fail scriptNo is 0, keyIp-{}, ordCode-{}", data.getKeyIp(), data.getOrdCode());
				return 1;
			} else {// direct conversion
				//data.setDirect(direct);

				HashMap<String, Object> map = new HashMap<String, Object>();

				ArrayList<String> ipList = new ArrayList<String>();
	        	ipList.add(data.getKeyIp());
		        if( data.getCrossLoginIp().length()>2 && data.getCrossLoginIp().split(",").length>0 ) {
		            JSONArray jsonArray = (JSONArray) new JSONParser().parse(data.getCrossLoginIp());
		            for(int i=0;i<jsonArray.size();i++){
		    	        Object jsonObj = jsonArray.get(i);
		    	        ipList.add(jsonObj.toString());
		            }
		        }
		        StringBuffer ipInfoList= new StringBuffer();
		        StringTokenizer TipInfoList= new StringTokenizer( data.getIpInfoList(), "|" );
		        while(TipInfoList.hasMoreElements()) {
		            if(TipInfoList.hasMoreTokens()) {
			            String ipinfo= TipInfoList.nextToken();
			        	if(!ipinfo.equals(data.getKeyIp())) {
			        		ipList.add(ipinfo);
			        		ipInfoList.append(ipinfo+"|");
			        	}
		            }
		        }

		        map.put("keyIp", ipList);
		        map.put("ipInfoList", ipInfoList.toString());
				map.put("advertiserId", data.getAdvertiserId()); //.getUserId().trim());
				map.put("scriptNo", data.getScriptNo()); //.getSite_code());
				map.put("siteCode", data.getSiteCode()); //.getMedia_code());
				map.put("adGubun", data.getAdGubun());
				map.put("product", data.getProduct());
				map.put("direct", direct);
				map.put("yyyymmdd", yyyymmdd);
				map.put("origin_yyyymmdd", data.getYyyymmdd());
				map.put("sendDate", data.getSendDate());

				logger.info("Conv map - {}", map);
				if ("90".equals(data.getTrkTpCode())) {
					rs = convDataDao.convLogActData_CRetry(map);
				} else {
					//?????????????????? lastClickDate ????????? regDate ???????????? ??????
					map.put("lastClickDate", data.getLastClickTime());
					rs = convDataDao.convTrkLogActData_Retry(map);
				}

				if(rs!=null) {
					data.setClickRegDate(rs.getClickRegDate());
					data.setKeyIp(rs.getKeyIp());

					logger.info("Conv keyIp - {}, no - {}, orderno - {}, product - {}, inhour - {}, direct - {}, sendDate - {}, price - {}, diffClickTime -{}"
							, data.getKeyIp(), rs.getNo(), data.getOrdCode(), rs.getProduct(), rs.getInHour(), data.getDirect(), data.getSendDate(), data.getPrice(), rs.getDiffClickTime());

					if( "1".equals(data.getPrice()) ) {
						if( rs.getDiffClickTime()<10 ) {
							rs=null;
							logger.info("Conv fail rs=null, keyIp-{}, ordCode-{}", data.getKeyIp(), data.getOrdCode());
						}
					}
				} else {
				}
				logger.info("Conv IP-{}, rs - {}", data.getKeyIp(), rs);
			}

			if(rs != null){
				try{
					NO = rs.getNo()+""; //.getNO();
					ADPRODUCT = rs.getProduct(); //.getADPRODUCT();
					rs.setAdvertiserId(data.getAdvertiserId());
					/**
					 * ????????? ????????? ????????? ?????? mba ??????, ????????? mbw ??? ?????? (?????????????????? ????????? ???????????? ???????????? ?????? : ????????? ??????)
					 * mbb : ????????????????????????
					 * mba : ????????????
					 * mbw : ????????????
					 * mbe : ????????? ????????????
					 */
					//????????? ???
					if(G.MBA.equals(ADPRODUCT)){
						ADPRODUCT = G.MBW;	// ?????????????????? ???????????? ?????????????????? ?????????.
					}
					if(G.MBA.equals(data.getType())){
						if(data.getDirect()==1){ //.equals("1")){
							IN_HOUR="24";
							data.setDirect(1);
						}else if(data.getDirect()==24){ //.equals("24")){
							IN_HOUR="24";
							data.setDirect(0);
						}else{
							IN_HOUR="0";
							data.setDirect(0);
						}
					}else{
						IN_HOUR = rs.getInHour(); //.getIN_HOUR();
						if( IN_HOUR.equals("0") ) data.setDirect(0);
					}
					data.setInHour(IN_HOUR);

					// ??????????????? ????????? action_log ??? ??????????????? ????????????
					data.setClickRegDate(rs.getClickRegDate());


					logger.info("Conv abtest keyIp - {}, adverId-{}, ordcode-{}, direct-{}, inhour-{}, direct2-{}, inhour2-{}"
							, data.getKeyIp(), data.getAdvertiserId(), data.getOrdCode(), data.getDirect(), data.getInHour(), data.getDirect2(), data.getInHour2());
					// ?????? 60??? abtest
					{
						if(rs.getDiffClickTime()<=3600) {
							data.setDirect2(1);
							data.setInHour2("24");
						} else {
							data.setDirect2(0);
							data.setInHour2(data.getInHour());
						}
					}

					ACTGUBUN = rs.getType(); //.getACTGUBUN();
					if( ACTGUBUN.equals(G.ICO) ) ACTGUBUN=G.CLICK;

				}catch(Exception e){
					logger.error("err dumpConvLogData 2 msg - {}", e);
					return 1;
				}
			}

			if( rs != null && StringUtils.isNotEmpty(NO) ){
				ConvData ordcode = convDataDao.selectCNVRS_NCL(data);
				if(ordcode != null) ORDCODE= ordcode.getOrdCode();
				//logger.info("Conv IP - {}, advertiserId - {}, ORDCODE - {}", data.getKeyIp(), data.getAdvertiserId(), ORDCODE);
				if( !StringUtils.isEmpty(ORDCODE) ){
					logger.info("Conv fail ordcode!=null, keyIp-{}, ordCode-{}", data.getKeyIp(), data.getOrdCode());
				}
				else {
					//logger.info("Conv ::::::ORDCODE:::::: [{}] != [{}] is null goto next step", data.getOrdCode(), ORDCODE);

					String pnm = data.getPnm();
					if(pnm==null)pnm="";
					if(pnm.length()>150) pnm=pnm.substring(0,149);
					if(pnm.indexOf("?")>0) pnm = "";	// pnm ???????????? ??????
					URL url = null;
					try {
						// referer ????????? ????????? ????????? ?????? ??????????????? ?????? (????????? referer ????????? ???????????? ????????? ????????? ???????????? ????????? ?????? ????????? ???!
						url = new URL(data.getOrdRFUrl());
					} catch (MalformedURLException e) {
						logger.error("No connection url [[[[" + data.getOrdRFUrl() + "]]]] !!!");
					}
					// ?????? ?????? ???????????? ???????????? ?????? null ???????????? ??????(????????? ????????? ????????? 0 ?????? ??????)
					/* ms.media_id='shoppul' ????????? ????????? MOBON_YN='Y' @jtwon */
					if("shoppul".equals(rs.getScriptUserId())){ //.getMEDIA_ID()) ){
						data.setMobonYn("Y");
					}

					logger.info("Conv IN_HOUR - {}, direct - {}, indirect - {}, data.getDirect() - {}", IN_HOUR, direct, indirect, data.getDirect());

					data.setCnvrsTpCode("");
					if( rs.getPosIpinfo()>0 ){
						logger.info("Conv ????????? ????????????");
						data.setDirect(0);
						IN_HOUR = "0";
						data.setCnvrsTpCode("03");

					} else {
						/*
						 * ????????? ???????????? ??????
						 * */
						ConversionInfoFilter filter = new ConversionInfoFilter();
						filter.setServiceCode(ConversionCode.SERVICE_CODE);
						filter.setScriptNo( rs.getScriptNo() );
						filter.setUserId(data.getAdvertiserId());
						filter.setPcode(data.getpCode());
						filter.setOrderCode(data.getOrdCode());
						filter.setOrderPrice(StringUtils.number(data.getPrice()));
						filter.setClientId(data.getKeyIp());
						//filter.setPastClickMinute(rs.getPastClickMinute());
						filter.setConversionType(IN_HOUR, data.getDirect()+"" );
						filter.setDirectHour(direct);
						filter.setIndirectHour(indirect*24);
						filter.setActionLogNo(NO);
						filter.setEtc(data.getSiteCode() );
						logger.info("Conv ????????? ?????? ??? : " + rs.getPastClickMinute());

//						boolean isInsert = conversionServiceSimple.isAvailableFrequency(filter, data);
//						if(!isInsert){	// false
//						  logger.info("Conv fail freq, keyIp-{}, ordCode-{}", data.getKeyIp(), data.getOrdCode());
//						  return 1;
//						}

						IN_HOUR = ConversionService.convertInHourCode(filter.getChargeCode());
						data.setDirect( Integer.parseInt(ConversionService.convertDirectCode(filter.getChargeCode())) );

						// ?????? 60??? abtest
						if(filter.isChargeCodeChanged()){
							data.setInHour2(IN_HOUR);
							data.setDirect2(data.getDirect());
						}
					}

					logger.info("Conv abtest keyIp - {}, adverId-{}, ordcode-{}, direct-{}, inhour-{}, direct2-{}, inhour2-{}"
							, data.getKeyIp(), data.getAdvertiserId(), data.getOrdCode(), data.getDirect(), data.getInHour(), data.getDirect2(), data.getInHour2());

					//logger.info("Conv ordcode - {}, inHour - {}, direct - {}, product - {}", data.getOrdCode(), IN_HOUR, data.getDirect(), data.getProduct());


					data.setNo( Long.parseLong(NO) );
					data.setOrdRFUrl( url==null?"":url.getAuthority() );
					data.setOrdPcode("");
					data.setScriptUserId(rs.getScriptUserId());
					data.setScriptNo(rs.getScriptNo());
					data.setSiteCode(rs.getSiteCode());
					data.setAdGubun(rs.getAdGubun());
					data.setProduct(rs.getProduct());
					data.setType(ACTGUBUN);
					data.setMcgb(rs.getMcgb());
					data.setPnm(pnm);
					data.setInHour(IN_HOUR);
					data.setShopconSerealNo(0);
					data.setShopconWeight(0);

					// 2019-09-18
					// ??????????????? type??? product ??? ???????????? ???????????? ????????????
					data.setProduct( rs.getProduct() );
					data.setType( rs.getType() );

					logger.debug("add list - {}", data.generateKey());

					// ????????? ???????????? ?????? ?????????
//					String convType = 1 == data.getDirect() ? "1" :"24".equals(IN_HOUR) ? "24" : "0";

					return 0;
				}
			}

			logger.info("Conv fail rs=null, keyIp-{}, ordCode-{}", data.getKeyIp(), data.getOrdCode());
			return 1;
		}catch(SQLException e){
			logger.error("err dumpConvLogData 3 msg - {}", e);
			return 1;
		}catch(Exception e){
			logger.error("err dumpConvLogData 1, data - {}", data, e);
			return 1;
		}
	}

	public int dumpConvLogDataV2(ConvData data) {
		if (data.getScriptNo()==0) {
			return 1;
		}
		// (s=1999) ????????? ???????????? ????????? ????????????.
		if( data.isConversionDirect() ) {
			return 0;
		}

		logger.debug("data - {}", data );

		BaseCVData mInfo = selectDao.selectMediaInfo(data.toBaseCVData());
		if(mInfo!=null) {
			data.setScriptUserId(mInfo.getScriptUserId());
		}

		// ??????????????? ??????????????? ????????????.
		if("".equals(data.getLastClickTime())) {
			try {
				data.setLastClickTime(DateUtils.getDate("yyyyMMddHHmmss", DateUtils.getDate("yyyy-MM-dd HH:mm:ss", data.getSendDate())));
			} catch (ParseException e) {
				data.setLastClickTime(DateUtils.getDate("yyyyMMddHHmmss"));
			}
		}

		// ???????????? ????????????.
		if(data.getOrdCode().length()>30) {
			data.setOrdCode(data.getOrdCode().substring(0, 30));
		}

		try{
			// ??????, ??????, ??????
			// direct=1 : ??????
			// CASE WHEN s.direct<>0 THEN '??????' ELSE CASE WHEN s.in_hour<>0 THEN '??????' ELSE '??????' END END
			// getCookieDirect 48h, getCookieInDirect 15d

			Date today = new Date();
			Date indirsalas = new Date();
			indirsalas.setTime( ( today.getTime() + (1000L*60*60*24* (data.getCookieInDirect() * -1)) - (1000L*60*convDelayTimeMinute) ) );	// 5?????????
			String yyyymmdd = DateUtils.getDate("yyyyMMdd", indirsalas); // ????????? ?????? ?????? ????????????

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("keyIp", data.getKeyIp());
			map.put("advertiserId", data.getAdvertiserId());
			map.put("siteCode", data.getSiteCode());
			map.put("scriptUserId", data.getScriptUserId());
			map.put("scriptNo", data.getScriptNo());
			map.put("yyyymmdd", yyyymmdd);
			map.put("origin_yyyymmdd", data.getYyyymmdd());
			map.put("sendDate", data.getSendDate());
			map.put("crossbrYn", data.getCrossbrYn());

			map.put("product", data.getProduct());
			map.put("adGubun", data.getAdGubun());
			map.put("adGubunCode", data.getAdGubunCode());
			map.put("productCode", data.getProductCode());

			map.put("svcTpCode", data.getSvcTpCode());
			map.put("advrtsTpCode", data.getAdvrtsTpCode());

			map.put("cookieDirect", data.getCookieDirect());

			logger.info("ConvV2 map - {}", map);

			ConvData rs = IntgCntrconvDataDao.selectActLog(map);
			if( rs!=null ) {
				data.setNo(rs.getNo());
				data.setInHour( rs.getInHour() );
				data.setIntgSeq( rs.getIntgSeq() );
				data.setIntgSeqs( rs.getIntgSeqs() );

				logger.info("ConvV2 intgSeqs-{}", data.getIntgSeqs());
				logger.info("ConvV2 keyIp-{}, no - {}, orderno - {}, product - {}, inhour - {}, direct - {}, sendDate - {}, diffClickTime - {}"
						, data.getKeyIp(), data.getNo(), data.getOrdCode(), data.getProduct(), data.getInHour(), data.getDirect(), data.getSendDate(), rs.getDiffClickTime());

				if( data.getInHour().equals("0") ) data.setDirect(0); // ???????????? ????????? ????????????

				if( "1".equals(data.getPrice()) ) {
					if( rs.getDiffClickTime()<10 ) {
						rs=null;
						logger.info("ConvV2 keyIp - {} rs change null ", data.getKeyIp() );
					}
				}
			} else {
			}
			logger.info("ConvV2 IP-{}, rs - {}", data.getKeyIp(), rs);

			if( rs != null && StringUtils.isNotEmpty(rs.getNo()+"") ){
				String ORDCODE="";
				ConvData ordcode = IntgCntrconvDataDao.selectCnvrsLog(data);
				if(ordcode != null) ORDCODE= ordcode.getOrdCode();
				if( !StringUtils.isEmpty(ORDCODE) ){
					logger.info("ORDCODE - '{}'", ORDCODE);
				}
				else {
					URL url = null;
					try {
						url = new URL(data.getOrdRFUrl());
					} catch (MalformedURLException e) {
						logger.error("No connection url [[[[" + data.getOrdRFUrl() + "]]]] !!!");
					}

					logger.info("ConvV2 keyIp - {}, advertiserId - {}, ordCode - {}, cookieDirect - {}, cookieIndirect - {}, IN_HOUR - {}, data.getDirect() - {}"
							, data.getKeyIp(), data.getAdvertiserId(), data.getOrdCode(), data.getCookieDirect(), data.getCookieInDirect(), data.getInHour(), data.getDirect());

					/*
					 * ????????? ???????????? ??????
					 * */
					ConversionInfoFilter filter = new ConversionInfoFilter();
					filter.setServiceCode(ConversionCode.SERVICE_CONVERSION_V2);
					filter.setScriptNo(data.getScriptNo());
					filter.setUserId(data.getAdvertiserId());
					filter.setPcode(data.getpCode());
					filter.setOrderCode(data.getOrdCode());
					filter.setOrderPrice(StringUtils.number(data.getPrice()));
					filter.setClientId(data.getKeyIp());
					filter.setConversionType(data.getInHour(), data.getDirect()+"" );
					filter.setDirectHour( data.getCookieDirect() );
					filter.setIndirectHour( data.getCookieInDirect() * 24);
					filter.setActionLogNo(data.getNo()+"");
					filter.setEtc(data.getSiteCode() );
					logger.info("ConvV2 ????????? ?????? ??? : " + rs.getPastClickMinute());

					boolean isInsert = conversionServiceSimple.isAvailableFrequencyV2(filter, data);
					if(!isInsert){
					  logger.info("ConvV2 ????????? ?????????!!! data - {}", data);
					  return 1;
					}

					String tmpInHour = ConversionService.convertInHourCode(filter.getChargeCode());
					String tmpDirect = ConversionService.convertDirectCode(filter.getChargeCode());
					logger.info("ConvV2 keyIp - {}, advertiserId - {}, ordCode - {}, inHour - {}, direct - {}, tmpDirect - {}, tmpInHour - {}"
							, data.getKeyIp(), data.getAdvertiserId(), data.getOrdCode(), data.getInHour(), data.getDirect(), tmpDirect, tmpInHour);

					//IN_HOUR = ConversionService.convertInHourCode(filter.getChargeCode());
					//data.setDirect( Integer.parseInt(ConversionService.convertDirectCode(filter.getChargeCode())) );
					data.setInHour(tmpInHour);
					data.setDirect(Integer.parseInt(tmpDirect));
					data.setOrdRFUrl( url==null?"":url.getAuthority() );
					//data.setOrdPcode("");
					data.setType("C");
					data.setShopconSerealNo(0);
					data.setShopconWeight(0);

					HashMap<Integer, String> itlInfo = selectDao.selectItlInfo();
					if( "kakao".equals(itlInfo.get(data.getScriptNo())) ) {
						data.setCnvrsTpCode("01");
					} else if( "daisy".equals(itlInfo.get(data.getScriptNo())) ) {
						data.setCnvrsTpCode("02");
					}else if("Y".equals(data.getCrossbrYn())) {
						data.setCnvrsTpCode("03");
					} else {
						data.setCnvrsTpCode("02");
					}
					if("shoppul".equals(data.getScriptUserId())){
						data.setMobonYn("Y");
					}

					logger.debug("add list - {}", data.generateKey());
					return 0;
				}
			}
			return 1;
		}catch(Exception e){
			logger.error("err dumpConvLogData 1, data - {}", data, e);
			return 1;
		}
	}

	/*
	 * ACTION_LOG ???????????? ?????? ??? ????????? ?????? ????????? ?????? ??????!
	 * ????????? ???????????? ?????? ?????? DataDbUtil ???????????? ??????.
	 * ?????? ????????? ?????? ?????? ???????????? ??????
	 * @param data
	 * @param fail
	 * @param dao
	 * @return
	 */
	public int dumpActData(BaseCVData data ) {
		try{
			// google, adfit actionlog ????????????
			//if( "04".equals(data.getInterlock()) || "05".equals(data.getInterlock()) ) {
			logger.debug("data-{}",data);
			if( "N".equals(data.getStatYn()) ) {
				logger.info("chking adfit else {}", data);
				return 1;
			}

			//????????? ????????? ?????? ??????
//			BaseCVData record2= data.clone();
			BaseCVData record2 = SerializationUtils.clone(data);

			if( "91".equals(data.getChrgTpCode()) ) {
				//NOTHING

				if(record2.getProductCode().equals("03")) {
					// ???????????? ????????? ?????? = ??????????????? ?????? ????????? click ?????? ??????
					record2.setType(G.CLICK);
					record2.setClickCnt(record2.getViewCnt());
				}
				if("ConsumerBranchAction".equals(profileId)) {
					sumObjectManager.appendClickUniquekData(record2);
				}
			}
			else if( "Dev".equals(profileId) || "ConsumerBranchAction".equals(profileId) ) {
				if(
					!G.CLICK.equals(data.getType()) && 				// CLICK 'C' ??? ?????????
					!G.MBB.equals(data.getProduct()) && 			// ????????????????????????(MBB)??? ?????????
					!G.ICO.equals(data.getProduct()) &&				// ???????????? ?????????
					!G.MBE.equals(data.getProduct()) && 			// ???????????????????????? ?????????
					!"trueman75".equals(data.getAdvertiserId() )){							// ?????? ???????????? ?????????
					return 1;
				}

				if( StringUtils.isEmpty(data.getKno()) ){
					data.setKno("0");
				}
				if( data.getAu_id().length()>50 ) {
					data.setAu_id( data.getAu_id().substring(0, 50) );
				}
				logger.debug("actionlog data - {}", data);

				// ??????????????? ???????????? ?????????????????? map?????? ????????? ??????
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("yyyymmdd",  DateUtils.getDate("yyyyMMdd", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.getSendDate()) ) ); // 1
				map.put("hh", data.getHh());
				map.put("keyIp", data.getKeyIp()); // 2 .getIp());
				map.put("au_id", data.getAu_id()); // 2 .au_id());
				map.put("pCode", data.getpCode()); // 3 .getPCODE());
				map.put("shoplogNo",StringUtils.longCast(data.getNo())); // 4 .getNO()));
				map.put("siteCode", data.getSiteCode()); // 5 .getSite_code());
				map.put("advertiserId", data.getAdvertiserId()); // 6 .getScriptUserId()); //.getUserId());
				map.put("scriptNo", data.getScriptNo()); // 7 .getMedia_code());
				map.put("scriptUserId", data.getScriptUserId()); // 8
				// 13?????? ???????????? ????????? ?????? ??????????????? ??????
				if(StringUtils.isNotEmpty(data.getKno()) && data.getKno().length() > 13){
					map.put("kno", data.getKno().substring(0,13)); // 9
				}else{
					map.put("kno", data.getKno()+"");	// 9
				}
				if(G.VIEW.equals(data.getType())){
					map.put("vcnt", data.getViewCnt2()+1); // .getViewcnt2()+1);
					map.put("vcnt2", data.getViewCnt2()); // .getViewcnt2());
					map.put("ccnt", 0);
				}else if(G.CLICK.equals(data.getType())){
					map.put("vcnt", 0);		//10
					map.put("vcnt2", 0);	//11
					map.put("ccnt", 1);		//12
				}
				map.put("point", data.getPoint());		//13
				map.put("actGubun", data.getType());	//14
				map.put("adGubun", data.getAdGubun());	//15
				map.put("product", data.getProduct().substring(0, 3));	//16
				map.put("mcgb", data.getMcgb());		//17
				map.put("sendDate", data.getSendDate());
				map.put("bHandlingStatsMobon", data.isbHandlingStatsMobon());
				map.put("bHandlingStatsPointMobon", data.isbHandlingStatsPointMobon());
				map.put("subadgubun", data.getSubadgubun());
				map.put("sendDate", data.getSendDate());
				map.put("noExposureYN", data.isNoExposureYN());
				map.put("abTests", data.getAbTest());
				map.put("mobAdGrpData", data.getMobAdGrpData());
				map.put("prdtTpCode", data.getPrdtTpCode());
				map.put("advrtsStleTpCode", data.getAdvrtsStleTpCode());

				if( G.ICO.equals(map.get("product")) || G.MBE.equals(map.get("product")) ){
					map.put("actGubun", "C");
				}
				else if( "floating".equals(map.get("product")) ){
					map.put("product", "nor");
				}

				// ??????????????? ???????????? ????????? ???????????? ?????????.
				if(G.MBE.equals(data.getProduct())){
					//data.setType(G.CLICK);
				}

				// ---------------------------------------------------------------
				// clickview THREAD ????????? ?????????1 ??????
				// > ?????? ?????? clickview ????????? action_log ??? ?????????.
				// ---------------------------------------------------------------
//			if( clickViewWorkQueue.getThreadsCount()>0 ) {
				logger.debug("add actionlog - {}", map);
				ActionLogData actionVo = ActionLogData.fromHashMap(map);
				sumObjectManager.appendData(actionVo, true);

				//????????? ????????? ?????? ??????
				if(record2.getProductCode().equals("03")) {
					// ???????????? ????????? ?????? = ??????????????? ?????? ????????? click ?????? ??????
					record2.setType(G.CLICK);
					record2.setClickCnt(record2.getViewCnt());
				}
				if( "".equals(record2.getChrgTpCode())
						&& "actionCharge".equals(record2.getDumpType())
						) {
					record2.setSvcTpCode("02");
					record2.setChrgTpCode("91");
				}
				if( "".equals(record2.getChrgTpCode())
						&& "social".equals(record2.getScriptUserId())
						) {
					record2.setSvcTpCode("02");
					record2.setChrgTpCode("01");
					record2.setAu_id(record2.getKeyIp());
				}
				sumObjectManager.appendClickUniquekData(record2);

				// ????????????
				ActionLogData aPcodeVo = ActionLogData.fromHashMap(map);
				aPcodeVo.setMainPcode(data.getpCode());
				aPcodeVo.setClickPcode(data.getpCode());
				aPcodeVo.setFromApp(data.getFromApp());
				aPcodeVo.setFreqCnt(data.getFreqLog());

//				if( aPcodeVo.getSubadgubun().equals(aPcodeVo.getAdGubun()) ) {
//					aPcodeVo.setRecomTpCode("02");
//				}
//				else if( !aPcodeVo.getSubadgubun().equals(aPcodeVo.getAdGubun()) ) {
//					aPcodeVo.setRecomTpCode("01");
//
//					List<String> filter= Arrays.asList("SM","HS","CT","CR","LS","SS");
//					if (!filter.contains(aPcodeVo.getSubadgubun())) {
//						aPcodeVo.setRecomTpCode("02");
//					}
//					if("".equals(aPcodeVo.getSubadgubun())
//							|| Arrays.asList("RP","SC").contains(aPcodeVo.getSubadgubun())
//						) {
//						aPcodeVo.setRecomTpCode("03");
//					}
//				}

				if(aPcodeVo.getProduct().length()>3) {
					aPcodeVo.setProduct(aPcodeVo.getProduct().substring(0,3));
				}

				// ???????????? ??????
				aPcodeVo.setRecomTpCode(CodeUtils.getRecomTpCode(aPcodeVo.getAdGubun(), aPcodeVo.getSubadgubun()));
				// ???????????? ??????
				aPcodeVo.setRecomAlgoCode( CodeUtils.getRecomAlgoCode(data.getSubadgubun(), data.getErgdetail(), "") );
				sumObjectManager.appendActionPcodeData(aPcodeVo);

				// ABTEST ?????? ?????? ??? ("00","09","10","11","12","13","14","15","16","17","18","19","22","25","33","34") BI ??????  ACTION_AB_PCODE_RECOM_LOG ??????
				String [] algoCodeList = {"00","09","10","11","12","13","14","15","16","17","18","19","22","25","33","34"};
				boolean checkAlgoCodeResult = false;

				for (String algoCode :  algoCodeList) {
					if (algoCode.equals(aPcodeVo.getRecomAlgoCode())) {
						checkAlgoCodeResult = true;
					}
				}

				if (checkAlgoCodeResult) {
					//logger.info("ABPcode Recom Action Data Builder - {}", aPcodeVo.getRecomAlgoCode());
					// BI ??? ?????? ?????????
					ActionLogData actionABPcodeData = SerializationUtils.clone(aPcodeVo);
					String abTest = actionABPcodeData.getAbTests();
					if (! "".equals(abTest) && abTest != null) {
						String[] spAbTest = abTest.split("[|]");
						for (String abTestTy : spAbTest) {
							if (abTestTy.startsWith("BI")) {
								actionABPcodeData.setAbTestTy(abTestTy);
								sumObjectManager.appendActionAbPcodeData(actionABPcodeData);
							}
						}
					}
				}

				// ACTION_LOG ????????? ??????
				ActionLogData actionRenewLog = SerializationUtils.clone(aPcodeVo);

				if (actionRenewLog.getMobAdGrp() != null) {
					actionRenewLog = this.setImageCopyData(actionRenewLog);
				}
				sumObjectManager.appendActionRenewLog(actionRenewLog);

				// branch ????????? actionData thread ??? 1?????????
				// ????????? ????????? ?????????. ???????????? ??????????????? ???????????? intgYn ??? Y??? ??????

				if(("RC".equals(data.getAdGubun()) || "CW".equals(data.getAdGubun()) || "SR".equals(data.getAdGubun()) || "SP".equals(data.getAdGubun()))) {
					data.setIntgYn("Y");
				} else {
					if(!"0".equals(data.getKwrdSeq())) {
						data.setIntgYn("Y");
					}
					if("Y".equals(data.getFromApp())) {
						data.setIntgYn("Y");
					}
					if(!"0".equals(data.getAdcSeq())) {
						data.setIntgYn("Y");
					}
					if("Y".equals(data.getCrossbrYn())) {
						data.setIntgYn("Y");
					}
					if(data.gettTime()!=0) {
						data.setIntgYn("Y");
					}
				}

//				if("Y".equals(data.getCtgrYn())) {
//					data.setIntgYn("Y");
//					try {
//						long tmplong = Long.parseLong(data.getCtgrNo());
//					}catch(Exception e) {
//						data.setIntgYn("N");
//					}
//				}
				if("UM".equals(data.getAdGubun())) {
					data.setIntgYn("Y");
				}
				if("Y".equals(data.getIntgYn())) {
					// ---------------------------------------------------------------
					// ?????????, ????????????, ??????, ??????, ????????? ??? ???????????????
					// > ClickViewData??? ????????????????????? ????????? ??????????????? ?????????????????????.
					// fromApp ; keyIp = auid
					// ---------------------------------------------------------------

					// ?????? ???????????????. MOB_ACT_LOG, MOB_ACT_INTG_LOG ??? ?????? ???????????????
					map.put("intgYn", data.getIntgYn());
					map.put("crossbrYn", data.getCrossbrYn());
					map.put("fromApp", data.getFromApp());
					map.put("kwrdSeq", data.getKwrdSeq());
					map.put("adcSeq", data.getAdcSeq());
					map.put("tTime", data.gettTime());
					map.put("ctgrNo", data.getCtgrNo());
					map.put("ctgrYn", data.getCtgrYn());
					map.put("UM", data.getKno());
					map.put("chrgTpCode", "01");
					map.put("svcTpCode", selectDao.selectMobonComCodeAdvrtsPrdtCode(data.getProduct()));
					map.put("advrtsTpCode", selectDao.convertAdvrtsTpCode(data.getAdGubun()));
					map.put("aiCateNo", data.getAiCateNo());
					map.put("pCode", data.getpCode());
					logger.debug("map data - {}", map);

					ActionLogData vo = ActionLogData.fromHashMap(map);
					if(!"0".equals(vo.getKwrdSeq())) {	vo.getIntgSeq().put("kwrd", data.getKwrdSeq());	}
					if("Y".equals(vo.getFromApp()))	{	vo.getIntgSeq().put("fromApp", "0");	}
					if(!"0".equals(vo.getAdcSeq())) {	vo.getIntgSeq().put("audience", data.getAdcSeq());	}
					if("Y".equals(vo.getCrossbrYn())) {	vo.getIntgSeq().put("crossbrYn", "0");	}
					if(vo.gettTime()!=0) {				vo.getIntgSeq().put("tTime", data.gettTime()+"");	}
//					if("Y".equals(vo.getCtgrYn())) {	vo.getIntgSeq().put("category", data.getCtgrNo());	}
					if(("RC".equals(vo.getAdGubun()) || "CW".equals(vo.getAdGubun()) || "SR".equals(vo.getAdGubun()) || "SP".equals(vo.getAdGubun()))) {
						vo.getIntgSeq().put("category", data.getCtgrNo());
					}
					if("UM".equals(vo.getAdGubun())) {	vo.getIntgSeq().put("UM", data.getKno());	}
					if(!"".equals( data.getpCode()) ) {	vo.getIntgSeq().put("ai_shop_cate", data.getAiCateNo());	}

					vo.setIntgLogCnt(vo.getIntgSeq().entrySet().size());
					logger.debug("intgSeq.size - {}, intgSeq - {}", vo.getIntgSeq().entrySet().size(), vo.getIntgSeq());

					if( vo.getIntgSeq().size()>0 ) {
						sumObjectManager.appendIntgCntrActionLogData(vo);
					}
				}
			}

			return 0;
		}catch(Exception e){
			logger.error("err dumpActData data - {}", data, e);
			return 1;
		}
	}

	private ActionLogData setImageCopyData(ActionLogData vo) {
		net.sf.json.JSONArray array = vo.getMobAdGrp();
		if (array != null) {
			for (int i = 0; i < array.size() ; i++) {
				JSONObject obj = array.getJSONObject(i);
				//??????
				String imgGrpId = StringUtils.trimToNull2(obj.get("mobon_ad_grp_id_i"), "");
				String imgGrpTpCode = StringUtils.trimToNull2(obj.get("ad_grp_tp_code_i"), "");
				String imgTpCode = StringUtils.trimToNull2(obj.get("image_tp_code"), "");
				//??????
				String copyGrpId = StringUtils.trimToNull2(obj.get("mobon_ad_grp_id_c"), "");
				String copyGrpTpCode = StringUtils.trimToNull2(obj.get("ad_grp_tp_code_c"), "");
				String copyTpCode = StringUtils.trimToNull2(obj.get("cp_tp_code"), "");

				boolean isImgData = ! "".equals(imgGrpId) && ! "".equals(imgGrpTpCode) && ! "".equals(imgTpCode);
				boolean isCopyData = ! "".equals(copyGrpId) && ! "".equals(copyGrpTpCode) && ! "".equals(copyTpCode);

				if (isImgData) {
					vo.setMobonAdGrpIdI(imgGrpId);
					vo.setAdGrpTpCodeI(imgGrpTpCode);
					vo.setImageTpCode(imgTpCode);
				}
				if (isCopyData ) {
					vo.setMobonAdGrpIdC(copyGrpId);
					vo.setAdGrpTpCodeC(copyGrpTpCode);
					vo.setCpTpCode(copyTpCode);
				}

			}
		}

		return vo;
	}


	public void dumpExternalBatch(ExternalInfoData data) {
		if( StringUtils.isEmpty(data.getPlatform()) || StringUtils.isEmpty(data.getProduct()) ) {
			BaseCVData vo2 = data.toBaseCVData();
			BaseCVData minfo = selectDao.selectMediaInfo(vo2);
			if( minfo!=null ) {
				if( StringUtils.isEmpty(data.getPlatform()) ) {
					data.setPlatform( StringUtils.trimToNull2(minfo.getPlatform(), "m").toUpperCase() );
				}
			}
		}
		if( StringUtils.isEmpty(data.getScriptUserId()) ) {
			BaseCVData vo2 = data.toBaseCVData();
			BaseCVData minfo = selectDao.selectMediaInfo(vo2);
			if( minfo!=null ) {
				data.setScriptUserId(minfo.getScriptUserId());
			}
		}
	}




}
