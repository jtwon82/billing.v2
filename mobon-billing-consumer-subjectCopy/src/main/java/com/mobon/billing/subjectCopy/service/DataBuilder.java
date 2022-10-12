package com.mobon.billing.subjectCopy.service;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.reportmodel.old.ExternalLinkageData;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.subjectCopy.service.dao.SelectDao;
import com.mobon.billing.model.v15.ActionLogData;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.model.v15.ExternalInfoData;
import com.mobon.billing.model.v15.ShopStatsInfoData;
import com.mobon.code.CodeUtils;
import com.mobon.code.constant.old.CodeConstants;


@Service
public class DataBuilder {
	private static final Logger logger = LoggerFactory.getLogger(DataBuilder.class);
	  @Autowired
	  private SelectDao selectDao;
	  
	  @Autowired
	  private SumObjectManager sumObjectManager;
	  
	  @Value("${conversion.delay.time.minute}")
	  private int convDelayTimeMinute;
	  
	  @Value("${change.product.adgubun.list}")
	  private String changeProductAdgubunList;
	  
	  @Value("${profile.id}")
	  private String profileId;

	public int dumpRtbDrcData(BaseCVData data) {
		try {
			// dumpActData( data );
			
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

	public int dumpShortCutData(BaseCVData data) {
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
				
				// 바콘 부정클릭 판단로직
				if(chkData==null || chkData.length()<1){
					logger.debug("add list - {}", data.generateKey());

					result = 0;
				} else {
				}

				// 클릭 정보를 ACTION_LOG 테이블에 저장 (일반배너 또한 한시간 프리퀀시가 없다고 하더라도 컨버젼이 잡히도록 변경)
				// dumpActData(data);
				
			}else{
				logger.debug("type is not click");
			}
		}catch(Exception e){
			logger.error("err dumpShortCutData 2 msg - {}", e);
		}
		return result;
	}

	public int dumpSkyClickData(BaseCVData data) {
		try{
			data.setType(G.CLICK);
			data.setClickCnt(1);

			if((G.SKY + G.GUBUN + G.LOWER_M).equals(data.getProduct()) ||
				G.MBB.equals(data.getProduct())){
			}else{
			}
			
			logger.debug("add list - {}", data.generateKey());
		//	dumpActData(data );
			
			return 0;
		}catch(Exception e){
			logger.error("err dumpSkyClickData msg - {}", e);
			return 1;
		}
	}

	public int dumpSkyChargeData(BaseCVData data) {
		try{
			if((G.SKY + G.GUBUN + G.LOWER_M).equals(data.getProduct())){
				// product를 i,b,s 이외 다른값은 허용하지 않는다.
				data.setProduct(G.MBB);
			}else{
			}
			logger.debug("add list - {}", data.generateKey());

			//dumpActData(data );
			
			return 0;
		}
		catch(Exception e){
			logger.error("err dumpSkyChargeData msg - {}", e);
			return 1;
		}
	}

	public int dumpChargeLogData(BaseCVData data) {
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

			//dumpActData( data );
			
			return 0;
		} catch (Exception e) {
			logger.error("err dumpChargeLogData msg - {}", e);
			return 1;
		}
	}

	public int dumpPlayLinkClickData(BaseCVData data) {
		int result = 1;
		try{
			
			data.setClickCnt(1);

		//	dumpActData(data );
			result = 0;
			return result;
		}catch(Exception e){
			logger.error("err dumpNormalViewLogData msg - {}", e);
			return 1;
		}
	}

	public int dumpPlayLinkChargeData(BaseCVData data) {
		int result = 1;
		try{

			if (CodeConstants.AD.equalsIgnoreCase(data.getChargeType())) {
				data.setMpoint(0);
			}

			if (CodeConstants.MEDIA.equalsIgnoreCase(data.getChargeType())) {
				data.setPoint(0);
			}

		//	dumpActData(data );
			
			result = 0;
			
			return result;
		}catch(Exception e){
			logger.error("err dumpNormalViewLogData msg - {}", e);
			return 1;
		}
	}

	public int dumpNormalViewLogData(BaseCVData data) {
		int result = 1;
		try{
			if(StringUtils.isNotEmpty(data.getAdvertiserId() ) && !"PE".endsWith(data.getAdGubun()) && !"CA".endsWith(data.getAdGubun())){
				if(StringUtils.isEmpty(data.getAdGubun())){
				} else {
					if( data.getViewCnt()<1 ) {
						data.setViewCnt(1);
					}
					
					logger.debug("add list - {}", data.generateKey());

				//	dumpActData(data );
					
					result = 0;
				}
			}
			return result;
		}catch(Exception e){
			logger.error("err dumpNormalViewLogData msg - {}", e);
			return 1;
		}
	}

	public int dumpMobileChargeLogData(BaseCVData data) {
		try{
			// mba, mbw
			logger.debug("add list - {}", data.generateKey());

		//	dumpActData(data );
			
			return 0;
		}catch(Exception e){
			logger.error("err dumpMobileChargeLogData msg - {}", e);
			return 1;
		}
	}

	public int dumpDrcData(BaseCVData data) {
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

			// 플러스콜 유효콜 타임이 1초 이상이면 클릭수 증가시키지 않음
			// 플러스콜 DB전환 값이 있으면 클릭수 증가시키지 않음
			// data.setClickCnt(1);		// 기존
			if ("08".equals(G.convertPRDT_CODE(data.getProduct()))
					&& (data.getAvalCallTime() >= 1 || data.getDbCnvrsCnt() >= 1)) {		// 변경
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
				
				
				// 사이클 처리하는 로직 있었음

				//if (StringUtils.isNotEmpty(data.getMobonLinkCate()) && ( 8126!=data.getScriptNo() || 8126==data.getScriptNo() )) {
				
				if(data.isClickChk()) {
					// 클릭수치올림
					
					BaseCVData ms = selectDao.selectMediaInfo(data);
					
					if( ms!=null ) {
						ExternalLinkageData link = new ExternalLinkageData();
						link.setSdate(data.getYyyymmdd());
						link.setUserid(data.getAdvertiserId()); // 광고주아이디
						link.setSite_code(data.getSiteCode()); // 사이트코드
						link.setGubun(G.AD); // 베이스광고
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
						//if ( "Consumer".equals(profileId) ) {	// 삭제됨
							//sumObjectManager.appendData(tmp, false);
						//}
					}
				}
			}
		//	dumpActData(data );
			
			return result;
		} catch (Exception e) {
			logger.error("err dumpDrcData msg - {}", e);
			return 1;
		}
	}

	public int dumpShopConData(BaseCVData data) {
		try{
//			SimpleDateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
//			Date date=new Date();
//			String ymd=yyyymmdd.format(date);
			
			data.setYyyymmdd( DateUtils.getDate("yyyyMMdd", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.getSendDate()) ));
			data.setScriptUserId(data.getScriptUserId()); 
			data.setPoint(data.getPoint());
			data.setType(G.CLICK);
			data.setClickCnt(1);
			
			if( G.MBW.equals(data.getProduct()) ){
			}else{
			}

			logger.debug("add list - {}", data.generateKey());

		//	dumpActData(data );
			return 0;
		}catch(Exception e){
			logger.error("err dumpShopConData msg - {}", e);
			return 1;
		}
	}

	public int dumpActData(BaseCVData data) {
		try{
			// google, adfit actionlog 처리안함
			//if( "04".equals(data.getInterlock()) || "05".equals(data.getInterlock()) ) {
			logger.debug("data-{}",data);
			if( "N".equals(data.getStatYn()) ) {
				logger.info("chking adfit else {}", data);
				return 1;
			}
			if( "ConsumerBranchAction".equals(profileId) ) {
				if(
					!G.CLICK.equals(data.getType()) && 				// CLICK 'C' 가 아니고
					!G.MBB.equals(data.getProduct()) && 			// 모바일브랜드링크(MBB)가 아니고
					!G.ICO.equals(data.getProduct()) &&				// 아이커버 아니고
					!G.MBE.equals(data.getProduct()) && 			// 모바일아이커버가 아니고
					!"trueman75".equals(data.getAdvertiserId() )){							// 특정 광고주가 아니면
					return 1;
				}
				
				if( StringUtils.isEmpty(data.getKno()) ){
					data.setKno("0");
				}
				logger.debug("actionlog data - {}", data);
				
				// 다른곳에서 변경되면 안되기때문에 map으로 넣어서 처리
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("yyyymmdd",  DateUtils.getDate("yyyyMMdd", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.getSendDate()) ) ); // 1
				map.put("keyIp", data.getKeyIp()); // 2 .getIp());
				map.put("pCode", data.getpCode()); // 3 .getPCODE());
				map.put("shoplogNo",StringUtils.longCast(data.getNo())); // 4 .getNO()));
				map.put("siteCode", data.getSiteCode()); // 5 .getSite_code());
				map.put("advertiserId", data.getAdvertiserId()); // 6 .getScriptUserId()); //.getUserId());
				map.put("scriptNo", data.getScriptNo()); // 7 .getMedia_code());
				map.put("scriptUserId", data.getScriptUserId()); // 8 
				// 13자리 초과되는 이슈가 있어 임시적으로 처리
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

				if( G.ICO.equals(map.get("product")) || G.MBE.equals(map.get("product")) ){
					map.put("actGubun", "C");
				}
				else if( "floating".equals(map.get("product")) ){
					map.put("product", "nor");
				}
				
				// 아이커버의 경우에는 타입을 클릭으로 바꾼다.
				if(G.MBE.equals(data.getProduct())){
					//data.setType(G.CLICK);
				}
			
				// ---------------------------------------------------------------
				// clickview THREAD 갯수로 컨슈머1 판단
				// > 오직 메인 clickview 에서만 action_log 에 쌓는다.
				// ---------------------------------------------------------------
//			if( clickViewWorkQueue.getThreadsCount()>0 ) {
				logger.debug("add actionlog - {}", map);
				// 추천여부
				ActionLogData aPcodeVo = ActionLogData.fromHashMap(map);
				//aPcodeVo.setAU_ID(data.getAU_ID());
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
				
				// 추천구분 종류
				aPcodeVo.setRecomTpCode(CodeUtils.getRecomTpCode(aPcodeVo.getAdGubun(), aPcodeVo.getSubadgubun()));
				// 알고리즘 종류
				aPcodeVo.setRecomAlgoCode( CodeUtils.getRecomAlgoCode(data.getSubadgubun(), data.getErgdetail(), "") );
				
				// branch 에서만 actionData thread 를 1로했음
				// 임시로 조건을 넣었음. 앞으로는 이런식으로 받지말기 intgYn 을 Y로 유도
				
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
					// 키워드, 오디언스, 지역, 할인, 앱타겟 등 추가될것임
					// > ClickViewData를 여러컨슈머에서 쓸경우 액션로그가 많이쌓일수있다.
					// fromApp ; keyIp = auid
					// ---------------------------------------------------------------
					
					// 종합 데이터모음. MOB_ACT_LOG, MOB_ACT_INTG_LOG 로 이동 되게하려고 
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
					
					
				}
			}
			
			return 0;
		}catch(Exception e){
			logger.error("err dumpActData data - {}", data, e);
			return 1;
		}
		
	}




}
