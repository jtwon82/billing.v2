package com.mobon.billing.branch.consumer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobon.billing.model.v15.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.reportmodel.old.AdChargeData;
import com.adgather.reportmodel.old.DrcData;
import com.adgather.reportmodel.old.RTBDrcData;
import com.adgather.reportmodel.old.RTBReportData;
import com.adgather.reportmodel.old.ShortCutData;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.core.billing.ConsumerProcess;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.dao.SelectDao;
import com.mobon.billing.model.IntgTpCodeData;

import net.sf.json.JSONObject;

@Configuration
@Service
public class ConsumerProcessBranch extends ConsumerProcess{

	private static final Logger	logger	= LoggerFactory.getLogger(ConsumerProcessBranch.class);

	@Autowired
	private SumObjectManager	sumObjectManager;

	@Autowired
	private SelectDao			selectDao;

	@Value("${profile.id}")
	private String	profileId;

	@Value("${ai.adverids:@null}")
	private String	adverids;

	@SuppressWarnings("unused")
	public void processMain(String topic, String message) {
		try {
			String className = "";
			String userId = "";
			boolean noCharge = false;
			long point = 0;
			JSONObject jSONObject = JSONObject.fromObject(message);
			try {
				className = (String) jSONObject.get("className");
				userId = (String) jSONObject.get("userId");

				try {
					// point 비교해서 강제로 통계처리 하기위해 만든것
					noCharge = (boolean) jSONObject.get("noCharge");
				} catch(Exception e) {
					noCharge = false;
				}

				if(StringUtils.isNotEmpty(userId) && userId.length() > 20) { // 이상광고주ID로 인해 크기제한
					logger.error("adverId is too long = " + userId);
					return;
				}

				if(noCharge) { // 과금 처리를 하지 않기 위한 flag값.
					point = jSONObject.getLong("point");
					logger.info("no Charge Log : point = " + point);
					return;
				}
			} catch (Exception e) {
				logger.error("err convert message to map : message - {}, msg - {}", message, e.toString());
				return;
			}
			logger.debug("topic - {}, msg - {}", topic, message);

//			if(topic.startsWith("OpenRTB"))
//				topic= CodeUtils.openrtbTopic(topic);

			if ( G.ClickViewData.equals(topic) ||  G.ClickViewPointData.equals(topic)
					|| topic.startsWith("OpenRTBViewData") || topic.startsWith("OpenRTBClickData")
				) {
				BaseCVData record = null;

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

				Date sendDate = new Date();
				Date targetDate = new Date();
				if (record!=null) {
					BaseCVData aiData = record.clone();
					sendDate.setTime( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse( record.getSendDate() ).getTime());

					// ---------------------------------------------------------------
					// clickview 에서 chrgTpCode 있는것만 처리
					// ---------------------------------------------------------------
//					String []svcTps= new String[] {"02","03","04","05","07","12"};
//					String []chrgTps= new String[] {"01","02","91"};
//					record.setSvcTpCode( (svcTps)[ (int) Math.round(Math.random() * (svcTps.length-1)) ] );
//					record.setChrgTpCode( (chrgTps)[ (int) Math.round(Math.random() * (chrgTps.length-1)) ] );

					if( "ConsumerBranchAction".equals(profileId) ) {
						if( "".equals(record.getChrgTpCode())
								&& "actionCharge".equals(record.getDumpType())
								) {
							record.setSvcTpCode("02");
							record.setChrgTpCode("91");
						}
						if( "".equals(record.getChrgTpCode())
								&& "social".equals(record.getScriptUserId())
								) {
							record.setSvcTpCode("02");
							record.setChrgTpCode("01");
						}
						if ( !record.isNoExposureYN()
								&& !"".equals(record.getChrgTpCode())
							) {
							ChrgLogData BasecvChrgLogData = record.toChrgLogData();
							// svcTpCode 도 넘겨받는다고 하여 제거 BaseCVData 에서 toChrgLogData 로 주입
							// BasecvChrgLogData.setSvcTpCode( selectDao.selectMobonComCodeAdvrtsPrdtCode(record.getProduct()) );
							sumObjectManager.appendChrgLogData(BasecvChrgLogData);
						}
					}

					if( "91".equals(record.getChrgTpCode()) ) {
						//NOTHING
					}
					else {

						if( "ConsumerBranchAction".equals(profileId) ) {

							try {
								targetDate.setTime( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse( "2021-04-29 18:30:00" ).getTime() );
							} catch (ParseException e) {		}

							if( targetDate.getTime() < sendDate.getTime() ) {
								record.setHandlingPointData("clickView");
							} else {
								record.setHandlingPointData("branchAction");
							}

							// ---------------------------------------------------------------
							// clickview 에서 포인트 있는것만 , 미노출건일경우 포인트 차감하지 않음.
							// ---------------------------------------------------------------
							if( (record.getPoint() > 0 || record.getMpoint() > 0) && !record.isNoExposureYN()) {
								BaseCVData BasecvPointData = record.clone();
								// BaseCVData BasecvPoint2Data = SerializationUtils.clone(record);

								sumObjectManager.appendPointData(BasecvPointData, true);
								// sumObjectManager.appendPoint2Data(BasecvPoint2Data);
							}

						} else {
							// google, adfit 클릭카운트 안함
							if( topic.startsWith("OpenRTBPoint") /* "N".equals(record.getStatYn()) */ ) {
								record.setViewCnt(0);
								record.setViewCnt2(0);
								record.setViewCnt3(0);
								record.setClickCnt(0);
								return;
							}

							// ---------------------------------------------------------------
							// 가지 만들기
							// ---------------------------------------------------------------
//							BaseCVData appTargetData = SerializationUtils.clone(record);
							BaseCVData intgCntrData = record.clone();

							if( "ConsumerBranch".equals(profileId) ) {
								if (!Arrays.asList("01","03").contains(record.getInterlock())) {
									record.setScriptNo(record.getScriptHirnkNo());
								}

								// BaseCVData adverProductHHData = SerializationUtils.clone(record);
								BaseCVData nearData = record.clone();
								BaseCVData phoneData = record.clone();

								logger.debug("phoneData {}", phoneData.getAdPhoneNum());
								if(!"".equals(phoneData.getAdPhoneNum())) {
									sumObjectManager.appendPhoneData(phoneData);
								}

								// ---------------------------------------------------------------
								// 과거데이터 처리
								// ---------------------------------------------------------------
								int sendHH = Integer.parseInt(record.getHh());
								int sendYMD = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(sendDate));
								int todayYMD = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date()));
								logger.debug("sendHH-{}, sendYMD-{}, todayYMD-{}, ", sendHH, sendYMD, todayYMD);

								if( sendHH>=6 ) {
									if(sendYMD != todayYMD) {
										sumObjectManager.appendParGatrData(record);
									}
								}

								// ---------------------------------------------------------------
								// 지역코드 유무
								// ---------------------------------------------------------------
								if( nearData.getNearYn() ){
									sumObjectManager.appendNearData(nearData.toNearData());
								}

							}

							// ---------------------------------------------------------------
							// 앱타겟 모수 : 기준시간보다 크면 수집한다.
							// ---------------------------------------------------------------
//							if ( "ConsumerBranch".equals(profileId) ) {
//								// ---------------------------------------------------------------
//								// 앱타겟여부
//								// ---------------------------------------------------------------
//								if( "Y".equals(appTargetData.getFromApp().toUpperCase()) ) {
//									logger.debug("appTargetData - {}", appTargetData);
//									sumObjectManager.appendAppTargetData(appTargetData.toAppTargetData());
//								}
//							}


							// ---------------------------------------------------------------
							// 매체 프리컨시 : 기존 clickview 를 확율로 사용하도록 한다.
							// ---------------------------------------------------------------
							if ( "ConsumerBranch2".equals(profileId) ) {
								BaseCVData mediaChrgData = record.clone();
								BaseCVData mInfo = selectDao.selectMediaScriptChrgInfo(mediaChrgData);

								logger.debug("scriptNo - {}, eprsRestType - {}", record.getScriptNo(), mediaChrgData.getOmitType() );

								if (mInfo != null) {

									if ("C".equals(mediaChrgData.getType())) {

										if ("01".equals(record.getOmitType())) {
											sumObjectManager.appendMediaChrgData(mediaChrgData);
										} else {
											logger.info("MEDIA_CHRG ommitData data - {}", record);
										}
									} else {
										int rndValue = Math.abs((new Random().nextInt(100)));
										int eprsRestRate = mInfo.getEprsRestRate();
										if (rndValue <= eprsRestRate) {
											sumObjectManager.appendMediaChrgData(mediaChrgData);
										} else {
											if("03".equals(G.convertPRDT_CODE(mediaChrgData.getProduct()))) {
												mediaChrgData.setViewCnt(0);
												mediaChrgData.setViewCnt2(0);
												mediaChrgData.setViewCnt3(0);
												sumObjectManager.appendMediaChrgData(mediaChrgData);
											}
											logger.debug("MEDIA_CHRG skipData rndValue - {}, eprsRestRate - {}, data - {}", rndValue, eprsRestRate, record);
										}
									}
								}


								// Ttime data
								if(intgCntrData.gettTime()!=0) {
									if(StringUtils.gAtId("CW,SR,HU,RM,RC,TC,SP", intgCntrData.getAdGubun().toUpperCase(), ",")>-1 ) {
										intgCntrData.setIntgYn("Y");
										intgCntrData.getIntgTpCodes().add(new IntgTpCodeData("07", intgCntrData.gettTime()));
									}
									else {
										logger.info("tTime Data but gubun is {}, key={}", intgCntrData.getAdGubun(), intgCntrData.getKey());
									}
								}
							}

							// ---------------------------------------------------------------
							// 키워드센타, 오디언스, 크로스브라우징, 티타임 데이터
							// ---------------------------------------------------------------
							if ( "ConsumerBranch".equals(profileId) ) {

								if (!Arrays.asList("01","03").contains(record.getInterlock())) {
									record.setScriptNo(record.getScriptHirnkNo());
								}

								if(!"0".equals(intgCntrData.getKwrdSeq())) {
									intgCntrData.setIntgYn("Y");
									intgCntrData.getIntgTpCodes().add(new IntgTpCodeData("01", intgCntrData.getKwrdSeq()));
								}
								if("Y".equals(intgCntrData.getFromApp())) {
									intgCntrData.setIntgYn("Y");
									intgCntrData.getIntgTpCodes().add(new IntgTpCodeData("02", "0"));
								}
								if(!"0".equals(intgCntrData.getAdcSeq())) {
									intgCntrData.setIntgYn("Y");
									intgCntrData.getIntgTpCodes().add(new IntgTpCodeData("03", intgCntrData.getAdcSeq()));
								}
								if("Y".equals(intgCntrData.getCrossbrYn())) {
									intgCntrData.setIntgYn("Y");
									intgCntrData.getIntgTpCodes().add(new IntgTpCodeData("06", "0"));
								}
							} else if ( "ConsumerBranch3".equals(profileId)) {
								if (!Arrays.asList("01","03").contains(record.getInterlock())) {
									record.setScriptNo(record.getScriptHirnkNo());
								}

								String adGubun = record.getAdGubun();
								if(("RC".equals(adGubun) || "CW".equals(adGubun) || "SR".equals(adGubun) || "SP".equals(adGubun))){

									intgCntrData.setIntgYn("Y");
									try {
										long tmplong = Long.parseLong(intgCntrData.getCtgrNo());
									}catch(Exception e) {
										intgCntrData.setCtgrNo("0");
									}
									intgCntrData.getIntgTpCodes().add(new IntgTpCodeData("10", intgCntrData.getCtgrNo()));

								}
	                            if(!"".equals(intgCntrData.getpCode()))
	                            {
	                                logger.debug("intgCntrData pcode:{}, intgCntrData aiCateNo:{}", intgCntrData.getpCode(), Integer.valueOf(intgCntrData.getAiCateNo()));
	                                intgCntrData.setIntgYn("Y");
	                                intgCntrData.getIntgTpCodes().add(new IntgTpCodeData("16", intgCntrData.getAiCateNo()));
	                            }

								// ---------------------------------------------------------------
								// AI 캠페인 생성 : (현재 특정광고주만 적용) AI 를 사용해 생성된
								// 캠페인의 경우 관련테이블에 추가로 데이터 적재 (sjpark3)
								// ---------------------------------------------------------------
	                            if (adverids != null) {
		                            List<String> aiAdverIds = Arrays.asList(adverids.split(","));

		                            if (aiAdverIds.contains(aiData.getAdvertiserId())) {
		                            	if (!aiData.getAiType().isEmpty()) {
		                            		// 캠페인 생성에 AI 사용여부 적재
			                            	if ("N".equals(aiData.getStatYn())) {
			                                	aiData.setViewCnt(0);
			                                	aiData.setViewCnt2(0);
			                                	aiData.setViewCnt3(0);
			                                	aiData.setClickCnt(0);
			            					}
			                            	sumObjectManager.appendAiData(aiData);
			                            }
		                            }
	                            }

							// ---------------------------------------------------------------
							// 플러스콜 : ADVRTS_PRDT_CODE = '08' or product = 'mpw'인 데이터는
							// 플러스콜 전용 테이블에 추가로 적재를 하여 관리한다.
							// 단, 콜타임 1초 이상일 경우에 유효콜 데이터로 판단하고 클릭 카운트 증가시키지 않는다.
							// + DB 전환수 1개 이상일때도 클릭 카운트 하지 않는다. (콜전환, DB전환 둘다 별도 토픽및
							// 별도 전환으로 뽑아낼 필요가 있어보임)
							// (클릭 카운트는 미증가 처리는 DataBuilder.dumpDrcData() 에서 처리되고 있다.)
							// @author  : sjpark3
							// ---------------------------------------------------------------
							if ("08".equals(G.convertPRDT_CODE(record.getProduct()))) {
								BaseCVData pluscallLogData = record.clone();
								sumObjectManager.appendPluscallLogData(pluscallLogData.toPluscallLogData());
							}
						}
						// 190911 성향수집 중지
//						if("UM".equals(intgCntrData.getAdGubun())) {
//							intgCntrData.setIntgYn("Y");
//							intgCntrData.getIntgTpCodes().add(new IntgTpCodeData("11", intgCntrData.getKno()));
//						}
						if( "Y".equals(intgCntrData.getIntgYn()) ) {
							logger.debug("intgCntrData.intgTpcodes- {}", intgCntrData.getIntgTpCodes());

							Iterator it = intgCntrData.getIntgTpCodes().iterator();
							while(it.hasNext()) {
								IntgTpCodeData vo = (IntgTpCodeData) it.next();
								BaseCVData data = intgCntrData.clone();
								data.setIntgTpCode(vo.getIntgTpCode());
								data.setIntgSeq(vo.getIntgSeq());

								logger.debug("split data intgTpCode-{}, intgSeq-{}", data.getIntgTpCode(), data.getIntgSeq());

								// 오디언스 스킵
								if("03".equals(data.getIntgTpCode())) {
								}
								// 티타임데이타
								else if ("07".equals(data.getIntgTpCode())) {
									data.settTime(Integer.parseInt(data.getIntgSeq()));

									if ( "ConsumerBranch2".equals(profileId) ) {

											if (!Arrays.asList("01","03").contains(record.getInterlock())) {
												record.setScriptNo(record.getScriptHirnkNo());
											}

											sumObjectManager.appendIntgCntrTtimeData(data);
										}
									}
									else {
										if ( "ConsumerBranch".equals(profileId) || "ConsumerBranch3".equals(profileId)) {
											sumObjectManager.appendIntgCntrData(data);
										}
									}
								}

								// 키워드그룹 (소셜링크)
								if(!"0".equals(intgCntrData.getKgr())) {
									if ( "ConsumerBranch".equals(profileId) ) {
										sumObjectManager.appendIntgCntrKgrData(intgCntrData);
									}
								}
							}
						}
					}
				}
			}
			else if ( G.ExternalData.equals(topic) ) {
				ExternalInfoData r = processExternalChargeData(jSONObject);
				if(r!=null) {
					if(r.getDumpType().equals(G.EXTERNALBATCH)) {
						// 외부연동도 같이 수집하도록 수정
						//sumObjectManager.appendMediaChrgData(r.toBaseCVData());

						BaseCVData mc = r.toBaseCVData();
						mc.setType(G.VIEWCLICK);
						mc.setHh("99");

						BaseCVData mc2 = mc.clone();

						if ( "ConsumerBranch2".equals(profileId) ) {
							//sumObjectManager.appendMediaChrgData(mc); Conv컨슈머에서 쌓기때문에 중복됨

						} else if ( "ConsumerBranch".equals(profileId) ) {
							sumObjectManager.appendParGatrData(mc2);
						}
					}
				}else {
				}
			}
			else if(topic.startsWith("OpenRTBPoint")) {
				// skip topic
			}
			else if (G.BasketData.equals(topic) &&
					"ConsumerBranch2".equals(profileId)) {
				// 장바구니 topic
				BasketData vo = new ObjectMapper().readValue(message, BasketData.class);

				// 비정상 au_id 필터링
				if (vo.getAuid().length() <= 50) {
					sumObjectManager.appendBasketData(vo);
				}
			}
			else {
				logger.error("else topic chking topic - {},  vo - {}", topic, jSONObject );
			}
		}catch(Exception e) {
			logger.error("err ", e);
		}
	}
}
