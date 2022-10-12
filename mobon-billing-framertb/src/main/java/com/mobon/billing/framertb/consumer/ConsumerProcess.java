package com.mobon.billing.framertb.consumer;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobon.billing.framertb.model.PollingData;
import com.mobon.billing.framertb.service.SumObjectManager;
import com.mobon.billing.framertb.service.dao.SelectDao;
import com.mobon.billing.framertb.util.StringUtil;
import com.mobon.billing.model.v15.FrameRtbData;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Configuration
@Service
public class ConsumerProcess {

	private static final Logger	logger	= LoggerFactory.getLogger(ConsumerProcess.class);

	@Autowired
	private SumObjectManager	sumObjectManager;
	@Autowired
	private SelectDao selectDao;




	public void processMain(String topic, String message) {
		try {
			if(message.contains("frameId") || message.contains("frameCombiKey") ) {
				if(message.contains("frameSelector") ) {
					if ( G.ClickViewData.equals(topic) || G.ConversionData.equals(topic)
							|| G.framertb_info.equals(topic) || G.SuccConversion.equals(topic)) {
						PollingData item = new ObjectMapper().readValue(message, PollingData.class);
						item = processConvert(item);

						if( "91".equals(item.getChrgTpCode()) ) {
							//NOTHING
						}
						else{

							//재처리시 topic 명이 framertb_info 로 들어오기때문에 해당 type 별로 topic 명 지정 
							if (G.framertb_info.equals(topic)) {

								item.setPlatform(G.convertPLATFORM_CODE_BACK(item.getPlatform()));
								item.setAdGubun(G.convertTP_CODE_BACK(item.getAdGubun()));

								if (item.getType().equals("V") || item.getType().equals("C") || item.getType().equals("A")) {
									topic = "ClickViewData";
								} else {
									topic = "ConversionData";
								}
							}						
							if (item.getFrameSelector().equals("Y") || 
									item.getFrameSelector().equals("N")) {							
								logger.info("frameSelector Data is not Integer");
								return;
							}


							if(!"".equals(item.getFrameId()) || !"".equals(item.getFrameCombiKey()) ) {
								boolean condition = true;
								if(!"".equals(item.getMediaCode()) /* && !"".equals(item.getFrameSelector())*/  /*&& !"".equals(item.getPrdtTpCode())*/ ) {

									// 기존 일반 프레임
									if(StringUtils.isNotEmpty(item.getFrameId())) {
										if(StringUtils.isEmpty(item.getFrameSelector()))		condition = false;					
										if(!StringUtil.isNumeric(item.getFrameSelector()))		condition = false;
										if(item.getFrameCycleNum() < 0)							condition = false;
										if(!(item.getFrameId().startsWith("W") || item.getFrameId().startsWith("M") || item.getFrameId().startsWith("G")))
										{
											condition = false;
										}

									} else if(StringUtils.isNotEmpty(item.getFrameCombiKey())) {

										if(!item.getFrameCombiKey().startsWith("A"))					condition = false;
									}

									//								if(StringUtil.isNumeric(item.getFrameSelector()) && item.getFrameCycleNum() >= 0 && StringUtils.isNotEmpty(item.getFrameId())) {

									if(G.VIEW.equals(item.getType())) {
										//if("PS".equals(G.VIEW.equals(item.getType())) )  condition = false;
										if("shoppul123".equals(item.getUserId()))			condition = false;
										if(item.getViewcnt3() <= 0)							condition = false;
										//										if(!StringUtil.isNumeric(item.getFrameSelector())) condition = false;
										//								if (data.getViewcnt3() > 0 && StringUtils.isNotEmpty(data.getFrameId()) && data.getFrameCycleNum() >= 0 && mongoDumpObj != null) {

									} else if(G.CLICK.equals(item.getType())) {
										if("shoppul123".equals(item.getUserId()))			condition = false;
									} 
									else if(G.CONVERSION.equals(item.getType()) || G.CONVERSIONCHARGE.equals(item.getDumpType()) ) {
										if("".equals(item.getOrdCode()))					condition = false;
										//if(!"1".equals(item.getDirect()))					condition = false;
										if( "".equals(item.getAdvrtsTpCode()))				item.setAdvrtsTpCode(item.getAdGubun());
										item = this.setDirectSessionData(item); 
									}
									//시간 기준으로 해당 topic 부분 분리 
									Date sendDate = new Date();
									Date targetDate = new Date();
									sendDate.setTime( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse( item.getSendDate() ).getTime());

									try {
										targetDate.setTime( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse( "2021-06-09 15:00:00" ).getTime() );
									} catch (ParseException e) {		}

									//									if (targetDate.getTime() < sendDate.getTime() && 
									//											G.SuccConversion.equals(topic)) {
									//										condition = true;
									//									} else if (G.SuccConversion.equals(topic)){
									//										condition = false;
									//									} else if ( G.ConversionData.equals(topic)) {
									//										condition = true;
									//									}
									
									if (item.getMediaCode().equals("0")) {
										condition = false;
									}
									
									if(condition) {
										if (item.getFrameSendTpCode().length() > 2) {
											item.setFrameSendTpCode(item.getFrameSendTpCode().substring(0, 2));
										}
										if (G.SuccConversion.equals(topic) || 
												G.ClickViewData.equals(topic)) {
											if (StringUtils.isNotEmpty(item.getFrameId())) {
												sumObjectManager.appendFrameCodeStats( item.toFrameRtbData() );																																	
											}
										}
										
										if (targetDate.getTime() > sendDate.getTime()) {
											if (G.SuccConversion.equals(topic)) {
												return;
											}
											
										}  else {
											if (G.ConversionData.equals(topic)) {
												return;
											}
										}
										
										if(StringUtils.isNotEmpty(item.getFrameCombiKey()) ) {

											if ("201".equals(item.getFrameRtbTypeCode())) {
												sumObjectManager.appendFrameCombiDatStats(item.toFrameRtbData());												
											} else {
												sumObjectManager.appendFrameKaistCombiDayStats(item.toFrameRtbData());												
											}
											
										} else if(StringUtils.isNotEmpty(item.getFrameId()) ) {
											sumObjectManager.appendFrameCycleLog( item.toFrameRtbData() );
											sumObjectManager.appendFrameTrnLog( item.toFrameRtbData() );
											sumObjectManager.appendFrameMediaAdverStats( item.toFrameRtbData() );

											//FRME_ADVER_PRDT_CTGR_DAY_STATS
											if (this.processFrameAdverPrdtCtgrDayStats(item)) {
												sumObjectManager.appendFrameAdverPrdtCtgrDayStats(item.toFrameRtbData());												
											}
											
											
											// FrameRtbData result = this.transFrameSendTpCode(item);										
											
											sumObjectManager.appendFrameDayStats( this.transFrameSendTpCode(item) );
											sumObjectManager.appendFrameCtgrDayStats( this.transFrameSendTpCode(item) );
											
											if (this.processFrameAdverDayStats(item)) {
												sumObjectManager.appendFrameAdverDayStats( this.transFrameSendTpCode(item));
											}
											//FRME_DAY_AB_STATS, FRME_ADVER_AB_STATS
											List<FrameRtbData> list= this.processFrameAdverDayAbTyStats(item);
											if (list.size() > 1) {
												logger.info("item abtest - {}, {}" , item.getAbtest() , item.getSiteCode());
											}
											for(FrameRtbData t: list) {
												if (this.processFrameAdverDayStats(item) && this.processFrameAdverDayStatsDisCollect(item)) {												
													sumObjectManager.appendFrameAdverDayAbStats(t);
												}
											}
											
											List<FrameRtbData> list2= this.processFrameAdverDayAbTyStats(item);
											for(FrameRtbData t: list2) {
												sumObjectManager.appendFrameDayAbStats(t);
											}
		
										}
										
										if (item.getAdverProdData() != null
												&& ! ("".equals(item.getFrameId()) && 
														"".equals(item.getFrameCombiKey()))
												&& ! item.getExpousreYN()) {
											
											JSONArray adverProdData = item.getAdverProdData();
											for (int i = 0; i  <  adverProdData.size(); i++) {
												if (G.VIEW.equals(item.getType())) {
													String divisionView = String.format("%.5f", item.getViewcnt3() / (double) adverProdData.size());
													//logger.info("division View Cnt : "+ divisionView);
													item.setDivisionViewCnt(new BigDecimal(divisionView));												
												}
												
												JSONObject obj = new JSONObject();
												obj = (JSONObject) adverProdData.get(i);											
												item = this.processSetFrameSizeStats(item, obj , topic);
												if (item == null) {
													return;
												}
												sumObjectManager.appendFrameSizeStats(item.toFrameRtbData());																							
											}
										}									
									}
								}
							}
						
						}
					}
				}
			}
		}catch(Exception e) {
			logger.error("err ", e);
		}
	}

	private FrameRtbData transFrameSendTpCode(PollingData item) {
		FrameRtbData result = new FrameRtbData();
		result = item.toFrameRtbData();
		if (item.getAbtest() == "" || item.getAbtest() == null) {
			result.setFrameSendTpCode("96");
		} else {
			if (!item.getAbtest().contains("AZ")) {
				result.setFrameSendTpCode("95");
			}
		}
		return result;
	}

	private boolean processFrameAdverDayStatsDisCollect(PollingData item) {
		int mediaScriptNo = Integer.parseInt(item.getMediaCode());
		for (Integer disCollectMediaScriptNo : G.ABFRAMESIZE_DISCOLLECT_MS_NO) {
			if (mediaScriptNo == disCollectMediaScriptNo) {
				return false;
			}
		}
		return true;
	}

	private PollingData setDirectSessionData(PollingData item) {		
			item.setBrowserSessionOrderAmt("Y".equals(item.getBrowserDirect())? item.getPrice() : 0);
			item.setBrowserSessionOrderCnt("Y".equals(item.getBrowserDirect())? item.getOrdercnt() : 0);
			item.setSessionOrderAmt("1".equals(item.getDirect())? item.getPrice() : 0);
			item.setSessionOrderCnt("1".equals(item.getDirect())? item.getOrdercnt() : 0);
			item.setDirectOrderAmt("24".equals(item.getInHour())? item.getPrice() : 0);
			item.setDirectOrderCnt("24".equals(item.getInHour())? item.getOrdercnt() : 0);		
		return item;
	}

	private boolean processFrameAdverDayStats(PollingData item) {
		//FrameSendTpCode 01,02,03,07 만 적재 
		String [] frameSendTpCodes = G.FRAME_TP_CODE.split("[|]");
		boolean checkAdverStatsCondition = false;
		for (String frameCode : frameSendTpCodes) {
			if (item.getFrameSendTpCode().equals(frameCode)) {
				checkAdverStatsCondition = true;
			}
		}
		return checkAdverStatsCondition;
	}

	private List<FrameRtbData> processFrameAdverDayAbTyStats(PollingData item) {
		List <FrameRtbData> result= new ArrayList <FrameRtbData>();

		FrameRtbData tmp = item.toFrameRtbData();
		
		if (item.getAbtest() == "" || item.getAbtest() == null) {
			tmp.setAbTestTy("00000");
			result.add(tmp);
		} else {
			if (!item.getAbtest().contains("AZ")) {
				tmp.setAbTestTy("00001");
				result.add(tmp);
			} else {				
				String [] abTyArr= item.getAbtest().split("[|]");
				for (String row : abTyArr) {
					if( !"".equals(row) && row.startsWith("AZ") ) {
						tmp.setAbTestTy(row);
						result.add(tmp);
					}
				}	
			}
		}
		
		return result;
	}


	private boolean processFrameAdverPrdtCtgrDayStats(PollingData item) {

		FrameRtbData rtbData = item.toFrameRtbData();
		//		
		//		String []adverArr = (G.FRAME_ADVER_ID).split("[|]");
		//				
		//		String frameSize = G.FRAME_SIZE;
		//		
		//		for (String adverId : adverArr) {
		//				if (adverId.equals(item.getUserId()) && 
		//						frameSize.equals(rtbData.getBnrCode())) {
		//					return true;
		//			}
		//		}

		if (G.frameAdverSize.containsKey(rtbData.getUserId())) {
			String bnrCode = G.frameAdverSize.get(rtbData.getUserId());
			if (bnrCode.equals(rtbData.getBnrCode())) {
				return true;
			}
		}

		return false;
	}


	private PollingData processSetFrameSizeStats(PollingData item, JSONObject obj, String topic) {
		String imgTpCode = (String) obj.get("imgTpCode");
		String cate1 = (String) obj.get("cate1Seq");
		String pCode = (String) obj.get("pCode");
		String matrAlgmSeq = StringUtils.trimToNull2((String) obj.get("matrAlgmSeq"), "0");


		item.setImgTpCode(imgTpCode);
		item.setCate1(cate1);
		item.setpCode(pCode);
		item.setMatrAlgmSeq(matrAlgmSeq);


		//Click 으로 들오는 경우 해당 데이터는 FRAME_ACTION_LOG 테이블에 insert 한다 . 

		if (G.CLICK.equals(item.getType())) {
			sumObjectManager.appendFrameActionLog(item.toFrameRtbData());
		}
		String broswerSession = "0";
				
		//direct 와 inHour 셋팅 
		if (G.ConversionData.equals(topic)) {

			Date today = new Date();
			Date indirsalas = new Date();
			indirsalas.setTime( ( today.getTime() + (1000L*60*60*24* (item.getCookieInDirect() * -1)) - (1000L*60*1) ) );	// 5분지연

			String yyyymmdd = DateUtils.getDate("yyyyMMdd", indirsalas); // 광고주 직접 허용 날짜범위
			item.setRecognitionYyyymmdd(Integer.parseInt(yyyymmdd));
			logger.info("check cookie Direct -{}",item.getRecognitionYyyymmdd());
			Map<String, Object> result = selectDao.getActionLog(item);

			if ( result == null) {
				return null;
			}
			broswerSession = String.valueOf(result.get("broswerSession"));
			String inHours = String.valueOf(result.get("inHour"));
			item.setInHour(inHours);

			if ("1".equals(item.getDirect()) || "1".equals(broswerSession)) {
				item.setInHour("24");
			}
			item.setBrowserSessionOrderAmt("Y".equals(item.getBrowserDirect()) || "1".equals(broswerSession)? item.getPrice() : 0);
			item.setBrowserSessionOrderCnt("Y".equals(item.getBrowserDirect()) || "1".equals(broswerSession)? item.getOrdercnt() : 0);
			item.setSessionOrderAmt("1".equals(item.getDirect())? item.getPrice() : 0);
			item.setSessionOrderCnt("1".equals(item.getDirect())? item.getOrdercnt() : 0);
			item.setDirectOrderAmt("24".equals(item.getInHour())? item.getPrice() : 0);
			item.setDirectOrderCnt("24".equals(item.getInHour())? item.getOrdercnt() : 0);
		} 

			return item;
		}

		public PollingData processConvert(PollingData item) {

			//		// 파티션때문에 
			if( !StringUtils.isNumeric(item.getPlatform()) ) item.setPlatform(G.convertPLATFORM_CODE(item.getPlatform()));
			if( !StringUtils.isNumeric(item.getProduct()) ) item.setProduct(G.convertPRDT_CODE(StringUtils.trimToNull2(item.getProduct(), "nor")));
			if( !StringUtils.isNumeric(item.getAdGubun()) ) item.setAdGubun(G.convertTP_CODE(item.getAdGubun()));

			return item;
		}

	}
