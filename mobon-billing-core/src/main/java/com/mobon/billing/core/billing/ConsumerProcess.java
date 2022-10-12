package com.mobon.billing.core.billing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.adgather.reportmodel.old.ExternalLinkageData;
import com.adgather.reportmodel.old.RTBDrcData;
import com.adgather.reportmodel.old.RTBReportData;
import com.adgather.reportmodel.old.ShopData;
import com.adgather.reportmodel.old.ShopStatsData;
import com.adgather.reportmodel.old.ShortCutData;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.core.service.DataBuilder;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.dao.SelectDao;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.model.v15.ChrgLogData;
import com.mobon.billing.model.v15.ConvData;
import com.mobon.billing.model.v15.ExternalInfoData;
import com.mobon.billing.model.v15.ShopInfoData;
import com.mobon.billing.model.v15.ShopStatsInfoData;
import com.mobon.billing.model.v15.ShortCutInfoData;
import com.mobon.code.CodeUtils;

import net.sf.json.JSONObject;

@Configuration
@Service
public class ConsumerProcess {

	private static final Logger	logger	= LoggerFactory.getLogger(ConsumerProcess.class);

	@Autowired
	private DataBuilder			dataBuilder;
	@Autowired
	private SelectDao			selectDao;
	@Autowired
	private SumObjectManager	sumObjectManager;

	@Value("${profile.id}")
	private String	profileId;

	@Value("${log.path.action}")
	private String actionLogPath;
	
	public void processMain(String topic, String message) {
		try {
			String className = "";
			String userId = "";
			JSONObject jSONObject = JSONObject.fromObject(message);
			try {
				className = (String) jSONObject.get("className");
				userId = (String) jSONObject.get("userId");
				if(StringUtils.isNotEmpty(userId) && userId.length() > 20) {
					logger.error("adverId is too long = " + userId);
					return;
				}
			} catch (Exception e) {
				logger.error("err convert message to map : message - {}, msg - {}", message, e.toString());
				return;
			}
			//logger.info("topic - {}, msg - {}", topic, message);

//			if(topic.startsWith("OpenRTB"))
//				topic= CodeUtils.openrtbTopic(topic);

			if ( G.ClickViewData.equals(topic) ||  G.ClickViewPointData.equals(topic) 
					|| topic.startsWith("OpenRTBViewData") || topic.startsWith("OpenRTBClickData")
				) {
				BaseCVData record=null;
//				BaseCVData record = ImpressionClick.getInstance().AvailableData(message);
				
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
				
				if( record!=null ) {
					
					if( "91".equals(record.getChrgTpCode()) ) {
						//NOTHING
					}
					else
					{

						Date sendDate = new Date();
						Date targetDate = new Date();
						sendDate.setTime( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse( record.getSendDate() ).getTime() );
						
						// ---------------------------------------------------------------
						// 필수조건
						// ---------------------------------------------------------------
						// google, adfit 클릭카운트 안함
						if( "N".equals(record.getStatYn()) ) {
							record.setViewCnt(0);
							record.setViewCnt2(0);
							record.setViewCnt3(0);
							record.setClickCnt(0);
						}
						// 포인트 차감 여부
						record.setPointChargeAble("false");
						
						try {
							targetDate.setTime( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse( "2021-04-29 18:30:00" ).getTime() );
						} catch (ParseException e) {		}
						
						if( targetDate.getTime() < sendDate.getTime() ) {
							record.setHandlingPointData("clickView");
						} else {
							record.setHandlingPointData("branchAction");
						}
						
						
						{
							/* 컨슈머 이중화에따른 지면 분리 */
							int sDivision = record.getScriptNo() % 10;
							logger.debug("sDivision {}", sDivision);
							if(
									("Dev".equals(profileId))
									||
									("Consumer".equals(profileId) && (sDivision == 0 || sDivision == 1 || sDivision == 2 || sDivision == 4))
									||
									("ConsumerHA".equals(profileId) && (sDivision == 3 || sDivision == 5 || sDivision == 6 || sDivision == 7 || sDivision == 8 || sDivision == 9))
								)
							{
								// ---------------------------------------------------------------
								// 기존 clickview
								// ---------------------------------------------------------------
								sumObjectManager.appendClickViewData(record);
								
							} else if ("Openrtb".equals(profileId)) {
								if (record!=null && !Arrays.asList("01","03").contains(record.getInterlock())) {
//									BaseCVData hirnkNo= selectDao.selectOpenRtbHIRNK_NO(record);
//									logger.info("scriptNo {}, hirnkNo {}", record.getScriptNo(), hirnkNo.getScriptHirnkNo());
//									record.setScriptNo(hirnkNo.getScriptHirnkNo());
									sumObjectManager.appendOpenrtbData(record);
								}
							}
							
							
							if(G.ClickViewData.equals(topic)) {
								
								String adGubun = record.getAdGubun();
								boolean checkTargetAd = ("CW".equals(adGubun) || "SR".equals(adGubun) || "RC".equals(adGubun) || "TC".equals(adGubun) || "PR".equals(adGubun) || "SP".equals(adGubun)
										|| "WC".equals(adGubun) || "MC".equals(adGubun) || "TV".equals(adGubun));
								boolean checkPartitionAd = (("UM".equals(adGubun) || "AU".equals(adGubun) || "MT".equals(adGubun) ||"GS".equals(adGubun)) || checkTargetAd);
								




								//Ad 구분에 따른 이중화 
								try {
									targetDate.setTime( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse( "2021-05-31 15:00:00" ).getTime() );
								} catch (ParseException e) {		}
								if( targetDate.getTime() > sendDate.getTime() ) {


//									if("ConsumerClientEnvHA".equals(profileId))
//										sumObjectManager.appendAdverClientEnvironmentData(recordAdverEnv);
	//
//									if ("Dev".equals(profileId) ||
//											("ConsumerClientEnv".equals(profileId) && checkPartitionAd) ||
//											("ConsumerClientEnvHA".equals(profileId) && !checkPartitionAd))
//									{
//										sumObjectManager.appendClientEnvironmentData(recordEnv);
//										sumObjectManager.appendADBlockData(recordADBlock);
	//
//										if (checkTargetAd &&
//												(G.VIEW.equals(record.getType()) && StringUtils.isNotEmpty(record.getUserAge()) && StringUtils.isNotEmpty(record.getGender()))) {
//											// 사용자 성별 및 연령대 정보
//											String[] ageList = record.getUserAge().split(",");
//											String[] genderList = record.getGender().split(",");
	//
//											for(String gender : genderList) {
//												for(String age : ageList) {
	//
//													BaseCVData recordAgeGender = SerializationUtils.clone(record);
//													recordAgeGender.setUserAge(age);
//													recordAgeGender.setGender(gender);
//													sumObjectManager.appendClientAgeGenderData(recordAgeGender);
	//
//												}
//											}
	//
//										}
//									}
									
									
									
									
								} else {


									if ("Dev".equals(profileId) || "ConsumerClientEnvHA".equals(profileId)) {
										// 2022-2-8 ClientEnv, ClientEnvHA 의 deep copy 방법 clone -> custom method 로 변경
										// BaseCVData recordAdverEnv = SerializationUtils.clone(record);
										BaseCVData recordAdverEnv = record.clone();

										sumObjectManager.appendAdverClientEnvironmentData(recordAdverEnv);

										if (checkTargetAd &&
												(G.VIEW.equals(record.getType()) && StringUtils.isNotEmpty(record.getUserAge()) && StringUtils.isNotEmpty(record.getGender()))) {
											// 사용자 성별 및 연령대 정보
											String[] ageList = record.getUserAge().split(",");
											String[] genderList = record.getGender().split(",");

											for (String gender : genderList) {
												for (String age : ageList) {
													// 2022-2-8 ClientEnv, ClientEnvHA 의 deep copy 방법 clone -> custom method 로 변경
													// BaseCVData recordAgeGender = SerializationUtils.clone(record);
													BaseCVData recordAgeGender = record.clone();
													recordAgeGender.setUserAge(age);
													recordAgeGender.setGender(gender);
													sumObjectManager.appendClientAgeGenderData(recordAgeGender);
												}
											}
										}
									}

									if ("Dev".equals(profileId) || "ConsumerClientEnv".equals(profileId)) {
										// 2022-2-8 ClientEnv, ClientEnvHA 의 deep copy 방법 clone -> custom method 로 변경
										/*BaseCVData recordEnv = SerializationUtils.clone(record);
										BaseCVData recordADBlock = SerializationUtils.clone(record);
										BaseCVData recordCrossAUID = SerializationUtils.clone(record);*/
										BaseCVData recordEnv = record.clone();
										BaseCVData recordADBlock = record.clone();
										BaseCVData recordCrossAUID = record.clone();

										sumObjectManager.appendClientEnvironmentData(recordEnv);
										sumObjectManager.appendADBlockData(recordADBlock);
										sumObjectManager.appendCrossAUIDData(recordCrossAUID);
									}
								}

							}

						}

					
					}
				}
			}
			else if ( G.ShopInfoData.equals(topic) ) {
				ShopInfoData r = processShopInfoData( jSONObject );
				if(r!=null){
					logger.debug("r - {}", r);
					sumObjectManager.appendShopInfoData(r);

					// MD추천 이 있으면 저장하기
					if( r.getMdPcode().size()>0 ) {
						logger.debug("r.getMdPcode().size() - {}", r.getMdPcode().size());
						Iterator it = r.getMdPcode().iterator();
						while( it.hasNext() ) {
							String tmp = (String) it.next();
							ShopInfoData tmpShopInfo = SerializationUtils.clone(r);
							tmpShopInfo.setRcmdPrdtCode(tmp);
							sumObjectManager.appendMdPcodeData(tmpShopInfo);
						}
					}
						
				}else{
					// TODO : 상품정보에서 웹모바일 구분이 안되면 platform:E로 리턴하도록 함.. E에대한 처리 필요
				}
			}
			else if ( G.ShopStatsInfoData.equals(topic) ) {
				ShopStatsInfoData r = processShopStatsInfoData(jSONObject);
				if(r!=null){
					sumObjectManager.appendShopStatsData(r);
				}else{
					logger.error("chking topic	{}	{}", topic, jSONObject );
				}
			}
			else if ( G.ConversionData.equals(topic) ) {
				ConvData record = new ConvData();
				
				JSONObject tmp1 = JSONObject.fromObject(message);
				tmp1.remove("regdate");
				tmp1.remove("actionLogList");
				tmp1.remove("debugString");
				
				AdChargeData tmpDrc = AdChargeData.fromHashMap(tmp1);
				tmpDrc.setProduct(tmpDrc.getType());	// product 가 type 으로 넘어옴
				record =  tmpDrc.toConvData();

//				if (Arrays.asList("hec2725","mayblue","livart","stylec2018","chakanshoes","popcon","gayeon3","napkin","vittz1","skmagic2020","dukomall","jayhan","realcoco").contains(record.getAdvertiserId())) {
//					if("rebuild".equals(record.getRegUserId())) {
//						String fileName = String.format("%s%s.%s", actionLogPath, "kafka-consumer.conv2.rebuild.log", DateUtils.getDate("yyyy-MM-dd_HH"));
//						try {
//							org.apache.commons.io.FileUtils.writeStringToFile(new File( fileName )
//									, String.format("[%s]	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), JSONObject.fromObject(record).toString()), "UTF-8", true);
//						} catch (IOException e) {
//							logger.error("err ", e);
//						}
//					}else {
//						String fileName = String.format("%s%s.%s", actionLogPath, "kafka-consumer.conv2.legacy.log", DateUtils.getDate("yyyy-MM-dd_HH"));
//						try {
//							org.apache.commons.io.FileUtils.writeStringToFile(new File( fileName )
//									, String.format("[%s]	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), JSONObject.fromObject(record).toString()), "UTF-8", true);
//						} catch (IOException e) {
//							logger.error("err ", e);
//						}
//					}
//				}
				
				// 모든 컨버전 데이타 쌓기
				ConvData convAllData = SerializationUtils.clone(record);
				convAllData = processConversionDataAll(convAllData);
				sumObjectManager.appendConvAllData(convAllData);
				
				record=processConversionData(record);
				if(record!=null){

//					ConvData intgCntrConvData = SerializationUtils.clone(record);
//					ConvData pcodeConvData = SerializationUtils.clone(record);
					
					// 컨버전 데이타 쌓기
					sumObjectManager.appendData(record, false);
					
					// ---------------------------------------------------------------
					// 통합컨버전용 ( 2020-07 직열화 작업으로 여기서 처리안함 )
					// ---------------------------------------------------------------
//					sumObjectManager.appendIntgCntrConvData(intgCntrConvData);
					
					// ---------------------------------------------------------------
					// pcode통계 컨버전용
					// ---------------------------------------------------------------
//					if("01".equals(pcodeConvData.getProductCode())
//							&& ("04".equals(pcodeConvData.getAdGubunCode()) || "10".equals(pcodeConvData.getAdGubunCode()) || "17".equals(pcodeConvData.getAdGubunCode()))) {
//						sumObjectManager.appendConvPcodeData(pcodeConvData);
//					}
					
				}else{
					// TODO : 컨버전 실패냐 액션로그 없는거냐 확인하기
				}
			}
			else if ( G.ExternalData.equals(topic) ) {
				ExternalInfoData r = processExternalChargeData(jSONObject);
				if(r!=null) {
					sumObjectManager.appendData(r, true);
				}else {
				}
			}
			else if(topic.startsWith("OpenRTBPoint")) {
				// skip topic
			}
			else {
				logger.error("else topic chking topic - {},  vo - {}", topic, jSONObject );
			}
		}
		catch(Exception e) {
			logger.error("err ", e);
		}
	}
	
	/*
	 * <pre>
	 *	샵데이타, 샵통계 처리프로세스
	 * </pre>
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @author jtwon
	 * @date 2017. 9. 1.
	*/
	public ShopInfoData processShopInfoData( JSONObject message ) {
		ShopInfoData record = null ;
		try {
			ShopData tmp1 = ShopData.fromHashMap(message);
			record = tmp1.toShopInfoData();
			logger.debug("ShopInfoData key - {}, cate - {}, pnm - {}", record.generateKey(), record.getCate(), record.getPnm());

			if( record.getPlatform()==null ) {
				//return null;
			}
			
			if( record.getUrl()!=null && record.getUrl().length()>2000 ) {
				record.setUrl( record.getUrl().substring(0,  2000) );
			}
			if( record.getImgPath()!=null && record.getImgPath().length()>2000 ) {
				record.setImgPath( record.getImgPath().substring(0, 2000) );
			}
			if( record.getpUrl()!=null && record.getpUrl().length()>2000 ) {
				record.setpUrl( record.getpUrl().substring(0, 2000) );
			}
			if(record.getCate()!=null) {
				record.setCate(record.getCate().replaceAll("[^\\u0000-\\uFFFF]", ""));
				if(record.getCate().length()>15) {
					record.setCate(record.getCate().substring(0,  15));
				}
			}
			if(record.getCate2()!=null) {
				record.setCate(record.getCate().replaceAll("[^\\u0000-\\uFFFF]", ""));
				if(record.getCate2().length()>15) {
					record.setCate2(record.getCate2().substring(0,15));
				}
			}
			
			record.setPnm(record.getPnm().replaceAll("[^\\u0000-\\uFFFF]", ""));
			
			if(record.getpCode().length()>100) {
				logger.debug("pcode = {}", record.getpCode());
				record.setpCode(record.getpCode().substring(0,100));
				logger.debug("pcode = {}", record.getpCode());
			}
			
			logger.debug("record - {}", record.generateKey());
			
			List<String> fileNm = new ArrayList<String>();
			if( record.getpCode()!=null && record.getpCode().length()>0 &&
				record.getPnm()!=null && record.getPnm().length()>0 &&
				record.getImgPath()!=null && record.getImgPath().length()>0 &&
				record.getPrice() >= 0 && 
				!fileNm.contains( StringUtils.buldString(record.getAdvertiserId(),record.getpCode(),record.getPnm(),record.getPrice()+"") )){
				
				/*
				 * 
				if (record.getPrice() == 0 && !"pkmall".equals(record.getAdvertiserId())) {
					record = null;					
					return record;
				}
				*/
				
				// 상품 중복제거용
				fileNm.add(StringUtils.buldString(record.getAdvertiserId(),record.getpCode(),record.getPnm(),record.getPrice()+""));
				
				int result = dataBuilder.insertShopData( record );
				if(result==1){
					logger.debug("ShopInfoData result = 1 : record - {}", record.generateKey());
					record = null;
				} else {
					logger.debug("add {}", record);
				}
			}else{
				record=null;
			}

		} catch (Exception e) {
			logger.error("ERROR processShopInfoData >> {}", message, e);
			record = null;
		}
		if(record!=null) {
			record.generateKey();
		}
		return record;
	}
	
	public ShopStatsInfoData processShopStatsInfoData(JSONObject message){
		ShopStatsInfoData record = null;
		try {
			ShopStatsData tmp1 = ShopStatsData.fromHashMap(message); //(ShopStatsData) JSONObject.toBean(message, ShopStatsData.class);
			record = tmp1.toShopStatsInfoData();
			logger.debug("ShopStatsInfoData key - {}", record.generateKey() );

			if( record.getCate()!=null && record.getCate().length()>15 ) {		
				record.setCate( record.getCate().substring(0,  15) );
			}
			if( record.getAdvertiserId()==null ) {
				record.setAdvertiserId("");
			}	
			try {
				record.setCate(record.getCate().replaceAll("[^\\u0000-\\uFFFF]", ""));				
			} catch (Exception e) {
				record.setCate("");
			}
			 		
			if( record.getCate().length()>15 ) {
				record.setCate(record.getCate().substring(0, 15));
			}
			if( record.getpCode().length()>100) {
				return null;
			}

			// insert shopStatsData
			int result = dataBuilder.dumpShopStatsData( record );
			if(result==1){
				logger.debug("ShopStatsInfoData result = 1 : record - {}", record);
				record = null;
			}
			
		} catch (Exception e) {
			record = null;
			logger.error("ERROR processShopStatsInfoData >> {} {}", record, e.toString());
		}
		return record;
	}
	
	/*
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
	public BaseCVData processShortCutInfoData(BaseCVData result){
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

	/*
	 * ConversionData 처리프로세스
	 * @param String className, Integer partition, Long offset, String message
	 * @return void
	 * @throws Exception
	 */
	public ConvData processConversionData(ConvData data) {
		try {
			if( data.getOrdCode().length()>30 ) {
				data.setOrdCode( data.getOrdCode().substring(0, 30) );
				if(data.getScriptNo()==1999 || data.getScriptNo()==19872 || data.getScriptNo()==22370) {
					logger.debug("ordcode - {}", data.getOrdCode());
					String [] ordcodeArr = data.getOrdCode().split(" ");
					data.setOrdCode(ordcodeArr[0]);
					logger.debug("ordcode - {}", data.getOrdCode());
				}
			}
			
			// 상품정보 필수채크
			String pnm = data.getPnm();
			if(pnm==null)pnm="";
			if(pnm.length()>150) pnm=pnm.substring(0,149);
			if(pnm.indexOf("?")>0) pnm = "";
			data.setPnm(pnm);

			// 상품코드 길이제한
			if(data.getpCode().length()>100) {
				data.setpCode(data.getpCode().substring(0,100));
			}
			// 지역아이피 길이제한
			if(data.getRealIp().length()>15) {
				data.setRealIp(data.getRealIp().substring(0,15));
			}
			// 브라우져 버전
			if(data.getBrowserVersion().length()>10) {
				data.setBrowserVersion(data.getBrowserVersion().substring(0,10));
			}
			if(Integer.parseInt(data.getOrdQty())<0) {
				data.setOrdQty("1");
			}
//			if("0".equals(data.getPrice())) {
//				data.setOrdCode("");
//			}
			// api에서 ymdhms를 세팅해주면 senddate에 반영하기
			if( data.isUseYmdhms() ) data.setSendDate( data.getYmdhms() );
			
			if("".equals(data.getYyyymmdd())) {
				data.setYyyymmdd( DateUtils.getDate("yyyyMMdd", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.getSendDate()) ));
			}
			
			// TpCode 코드변환
			data.setSvcTpCode( selectDao.selectMobonComCodeAdvrtsPrdtCode(data.getProduct()) );
			data.setAdvrtsTpCode(selectDao.convertAdvrtsTpCode(data.getAdGubun()));
			data.setPltfomTpCode( G.convertPLATFORM_CODE(data.getPlatform()) );
			
			logger.debug("product - {}, svcTpCode - {}", data.getProduct(), data.getSvcTpCode());
			logger.debug("s:{}, socialYn:{}", data.getScriptNo(), data.getSocialYn());
			
			String minyyyymmdd= DateUtils.getDate("yyyyMMdd", DateUtils.getPreviousDate(2));
			String yyyymmdd= data.getYyyymmdd();
			
			if (Integer.parseInt(yyyymmdd) < Integer.parseInt(minyyyymmdd) && 
					data.getScriptNo() != 1999 ) {
				logger.info("chking Conv past date - {}", data);
				data = null;
				
			}else if( "".equals(data.getAdvertiserId()) ) {
				logger.info("chking Conv userid is null - {}", data);
				data = null;
				
			}else if(data.getScriptNo()==1999 
					|| ( "Y".equals(data.getSocialYn()) && (data.getScriptNo()==19872 || data.getScriptNo()==22370) ) ) { 	// 쇼플의경우 강제로 컨버전처리 
				
				if(Integer.parseInt(data.getPrice())>100000000) {
					logger.info("chking Conv over price 100000000 data - {}", data);
					data=null;
				}
				else {
					data.setConversionDirect(true);	// 컨버전이 강제로 일어나도록
					
					// social 지면은 간접으로
					if(data.getScriptNo()==19872 || data.getScriptNo()==22370) {
						data.setDirect(0);
						data.setInHour("0");
						data.setRegUserId("SOCIAL");
						data.setScriptUserId("social");
					}
					
					data.setCnvrsTpCode("");
					data.setShopconSerealNo(0);
					data.setRtbType("");
					data.setGender("0");
					data.setpCode("1");
					//record.setDirect(0);
					data.setPlatform( data.getPlatform().toUpperCase() );
					data.setMobonYn("Y");
					
					// 쇼플의경우 클릭시간이 안넘어옴.
//					if("".equals(data.getLastClickTime())) {
//						data.setLastClickTime(DateUtils.getDate("yyyyMMddHHmmss", DateUtils.getDate("yyyy-MM-dd HH:mm:ss", data.getSendDate())));
//					}
					
					// 강제로 인서트할경우 클릭시간이 없다.
					data.setClickRegDate(DateUtils.getDate("yyyyMMddHHmmss", DateUtils.getDate("yyyy-MM-dd HH:mm:ss", data.getSendDate())));
					if( "".equals(data.getpCode()) ) data.setpCode("1");
					if( "".equals(data.getInHour()) ) data.setInHour("0");	// 정의하기
					if( "".equals(data.getMobonYn()) ) data.setMobonYn("N");	// 정의하기
					if( "".equals(data.getScriptUserId()) ) data.setScriptUserId("shoppul");
					if( "".equals(data.getProduct()) ) data.setProduct("b");	// 정의하기
					if( "".equals(data.getType()) || data.getType().length()>1 ) data.setType("C");
					data.setYyyymmdd( DateUtils.getDate("yyyyMMdd", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.getSendDate()) ));
					
					data.generateKey();
				}

			}else if( "Y".equals(data.getIn1hourYn()) ) {
				logger.info("chking rebuild conv data in1Hour {}", data);
				data = null;
				
			}else if( data.getScriptNo()!=0 ) {
				logger.debug("Conv record - {}", data);
				
				List<Map> listExternalUser = selectDao.selectExternalUserInfo();
				ArrayList<String> extList = new ArrayList<>();
				for( Map row : listExternalUser ) {
					extList.add( row.get("userId").toString() );
				}

				// 01: 모비온, 02: 데이지, 03: 카카오, 99:외부연동
				HashMap<Integer, String> itlInfo = selectDao.selectItlInfo();
				
				logger.debug("userid - {}, scriptNo - {}", data.getAdvertiserId(), data.getScriptNo());

				if (extList.contains(data.getAdvertiserId())) {
					data.setInterlock("99");
					
				} else if( "kakao".equals(itlInfo.get(data.getScriptNo())) ) {
					data.setInterlock("03");
					
				} else if( "daisy".equals(itlInfo.get(data.getScriptNo())) ) {
					data.setInterlock("02");
					
				} else {
					data.setInterlock("01");
				}
				
				logger.debug("record - {}", data.getInterlock());

			} else if ("rebuild".equals(data.getRegUserId())){
				
			} else {			
				logger.debug("Conv else skip - {}", data);
				data = null;
			}
			
		} catch (Exception e) {
			logger.error("err - {}, data - {}", data, e);
			data=null;
		}
		if(data!=null) {
			data.generateKey();
		}
		return data;
	}
	public ConvData processConversionDataAll(ConvData data) {
		try {
			if( data.getOrdCode().length()>30 ) {
				data.setOrdCode( data.getOrdCode().substring(0, 30) );
				if(data.getScriptNo()==1999 || data.getScriptNo()==19872 || data.getScriptNo()==22370) {
					logger.debug("ordcode - {}", data.getOrdCode());
					String [] ordcodeArr = data.getOrdCode().split(" ");
					data.setOrdCode(ordcodeArr[0]);
					logger.debug("ordcode - {}", data.getOrdCode());
				}
			}
			
			// 상품정보 필수채크
			String pnm = data.getPnm();
			if(pnm==null)pnm="";
			if(pnm.length()>150) pnm=pnm.substring(0,149);
			if(pnm.indexOf("?")>0) pnm = "";
			data.setPnm(pnm);
			
			// 상품코드 길이제한
			if(data.getpCode().length()>100) {
				data.setpCode(data.getpCode().substring(0,100));
			}
			// 지역아이피 길이제한
			if(data.getRealIp().length()>15) {
				data.setRealIp(data.getRealIp().substring(0,15));
			}
			// lastclick 확인
			if(data.getLastClickTime()==null || "".equals(data.getLastClickTime()) ) {
				data.setLastClickTime(data.getSendDate());
			}
			if(data.getKeywordUrl().length()>700) {
				data.setKeywordUrl(data.getKeywordUrl().substring(0,700));
			}
			
			// TpCode 코드변환
			data.setSvcTpCode( selectDao.selectMobonComCodeAdvrtsPrdtCode(data.getProduct()) );
			data.setAdvrtsTpCode(selectDao.convertAdvrtsTpCode(data.getAdGubun()));
			data.setPltfomTpCode( G.convertPLATFORM_CODE(data.getPlatform()) );
			
			logger.debug("product - {}, svcTpCode - {}", data.getProduct(), data.getSvcTpCode());
			
			if( "".equals(data.getAdvertiserId()) ) {
				logger.info("chking ConvAll userid is null - {}", data);
//				data = null;
				
			}else if(data.getScriptNo()==1999 
					|| ( "Y".equals(data.getSocialYn()) && (data.getScriptNo()==19872 || data.getScriptNo()==22370) )) { 	// 쇼플의경우 강제로 컨버전처리 
				
				if(Integer.parseInt(data.getPrice())>100000000) {
					logger.info("chking ConvAll over price 100000000 data - {}", data);
					data=null;
				}
				else {
					data.setConversionDirect(true);	// 컨버전이 강제로 일어나도록
					
					// social 지면은 간접으로
					if(data.getScriptNo()==19872 || data.getScriptNo()==22370) {
						data.setDirect(0);
						data.setInHour("0");
						data.setRegUserId("SOCIAL");
					}
					
					data.setShopconSerealNo(0);
					data.setRtbType("");
					data.setGender("0");
					data.setpCode("1");
					//record.setDirect(0);
					data.setPlatform( data.getPlatform().toUpperCase() );
					data.setMobonYn("Y");
					
					// api에서 ymdhms를 세팅해주면 senddate에 반영하기
					if( data.isUseYmdhms() ) data.setSendDate( data.getYmdhms() );
					
					// 쇼플의경우 클릭시간이 안넘어옴.
					if("".equals(data.getLastClickTime())) {
						data.setLastClickTime(DateUtils.getDate("yyyyMMddHHmmss", DateUtils.getDate("yyyy-MM-dd HH:mm:ss", data.getSendDate())));
					}
					
					if( "".equals(data.getpCode()) ) data.setpCode("1");
					if( "".equals(data.getInHour()) ) data.setInHour("0");	// 정의하기
					if( "".equals(data.getMobonYn()) ) data.setMobonYn("N");	// 정의하기
					if( "".equals(data.getScriptUserId()) ) data.setScriptUserId("shoppul");
					if( "".equals(data.getProduct()) ) data.setProduct("b");	// 정의하기
					if( "".equals(data.getType()) || data.getType().length()>1 ) data.setType("C");
					data.setYyyymmdd( DateUtils.getDate("yyyyMMdd", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.getSendDate()) ));
					
					data.generateKey();
				}
				
			}else if( data.getScriptNo()!=0 ) {
				logger.debug("ConvAll record - {}", data);
				
				List<Map> listExternalUser = selectDao.selectExternalUserInfo();
				ArrayList<String> extList = new ArrayList<>();
				for( Map row : listExternalUser ) {
					extList.add( row.get("userId").toString() );
				}

				// 01: 모비온, 02: 데이지, 03: 카카오, 99:외부연동
				HashMap<Integer, String> itlInfo = selectDao.selectItlInfo();
				
				logger.debug("userid - {}, scriptNo - {}", data.getAdvertiserId(), data.getScriptNo());

				if (extList.contains(data.getAdvertiserId())) {
					data.setInterlock("99");
					
				} else if( "kakao".equals(itlInfo.get(data.getScriptNo())) ) {
					data.setInterlock("03");
					
				} else if( "daisy".equals(itlInfo.get(data.getScriptNo())) ) {
					data.setInterlock("02");
					
				} else {
					data.setInterlock("01");
				}
				
				logger.debug("record - {}", data.getInterlock());

			} else {
//				data = null;
			}
			
		} catch (Exception e) {
			logger.error("err - {}, data - {}", data, e);
			data=null;
		}
		if(data!=null) {
			data.generateKey();
		}
		return data;
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
				
				// chrgLog 처리
				if (StringUtils.isNotEmpty(record.getChrgTpCode()) &&
						"ConsumerBranchAction".equals(profileId)) {
					ChrgLogData BasecvChrgLogData = record.toChrgLogData();
					sumObjectManager.appendChrgLogData(BasecvChrgLogData);
				}
				
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

	
	/*
	 * processRtbClickViewData 처리프로세스
	 * @param String className, Integer partition, Long offset, String message
	 * @return void
	 * @throws Exception
	 */
	public BaseCVData processRtbClickData(BaseCVData result) {
		try {
//			RTBDrcData record = RTBDrcData.fromHashMap(message);
//			result = record.toBaseCVData();
			
			// 
			result.setPlatform(result.getPlatform().substring(0, 1).toUpperCase());
			result.setType("C");
			result.setClickCnt(1);
			
			result.setYyyymmdd( DateUtils.getDate("yyyyMMdd", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(result.getSendDate()) ));
			
//			if( "kakao".equals(result.getScriptUserId()) || "mkakao".equals(result.getScriptUserId()) ) {
//				result.setInterlock("03");
//			
//			} else if ( "daisy".equals(result.getScriptUserId()) || "mdaisy".equals(result.getScriptUserId()) ) {
//				result.setInterlock("02");
//			}
			CodeUtils.setInterLock(result);
			
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
	public BaseCVData processRtbViewData(BaseCVData result) {
		try {
//			RTBReportData record = RTBReportData.fromHashMap(result);
//			result = record.toBaseCVData();

			// 
			result.setPlatform(result.getPlatform().substring(0, 1).toUpperCase());
			result.setType("V");

			//01: 모비온, 02: 데이지, 03: 카카오, 99:외부연동
//			if( "kakao".equals(result.getScriptUserId()) || "mkakao".equals(result.getScriptUserId()) ) {
//				result.setInterlock("03");
//				
//			} else if ( "daisy".equals(result.getScriptUserId()) || "mdaisy".equals(result.getScriptUserId()) ) {
//				result.setInterlock("02");
//			}
			CodeUtils.setInterLock(result);
			
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
	 * empty
	 * </pre>
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 * @author jtwon
	 * @date 2017. 11. 6.
	*/
	public ExternalInfoData processExternalChargeData(Map<String, String> message){
		ExternalInfoData result = null;
		try {
			ExternalLinkageData tmp = ExternalLinkageData.fromHashMap(message);
			result = tmp.toExternalInfoData();
			//result = ExternalInfoData.fromHashMap(message);	// 배치
			
			if( result.getAdvertiserId()==null ) {
				result.setAdvertiserId("");
			}

			//if( !StringUtils.isNumeric(result.getPlatform()) ) result.setPlatform(G.convertPLATFORM_CODE_BACK(result.getPlatform()));
			//if( !StringUtils.isNumeric(result.getAdGubun()) ) result.setAdGubun(G.convertTP_CODE_BACK(result.getAdGubun()));
			
			dataBuilder.dumpExternalBatch(result);
			
			//외부연동데이터는 시간을 99로저장하자
			//result.setHh("99");
			//result.setProduct("nor");
			if( result.getDumpType().equals( GlobalConstants.EXTERNALCHARGE ) ){
				
				//미노출이면 viewcntMobon 올리기
				int viewcnt_mobon = result.getViewCntMobon();
				if (viewcnt_mobon < 1 && !GlobalConstants.NOEXPOSURE.equals(result.getAdvertiserId()) ){
					viewcnt_mobon = 1;
				}
				
				// 외부연동 타입별 카운트할 수치증가
				if ("P".equals(result.getType())) {
					result.setPassbackCnt(1);
				}
				else if ("C".equals(result.getType())) {
					result.setClickCntMobon(1);
				}
				else {
					result.setType("V");
					result.setViewCntMobon(viewcnt_mobon);
				}
				// passback, viewcnt 를 같이 저장하기위해 디비에 row lock 해소를 위해
				if( "P".equals(result.getType())|| "V".equals(result.getType()) ) {
					result.setType("PV");
				}
				
				
				// 새로정의된 TYPE
				if(!"0".equals(result.getExl_seq())) {
					result.setType("RCV");
				}
				else if(!"".equals(result.getSend_tp_code())) {
					result.setType("SEND");
					if ( "03".equals(result.getSend_tp_code()) ) {
//						result.setViewCnt(result.getViewCntMobon());
//						result.setPassbackCnt(0);
			            result.setViewCnt(viewcnt_mobon);
			            result.setClickCnt(result.getPassbackCnt());
			            result.setPassbackCnt(0);
					}
				}
				else {
					logger.info("chking external vo - {}", result);
				}
				
			} else if( result.getDumpType().equals( GlobalConstants.EXTERNALBATCH )){
				List<String> filter= Arrays.asList(""); // nativecriteo","nativeiw
				//logger.info("{}", result.getAdvertiserId());
				if (filter.contains(result.getAdvertiserId())) {
					result.setViewCnt(0);
				} else {
					result.setViewCnt3(result.getViewCnt());
				}
				
//				if( excepts.indexOf(String.format(",%s", result.getAdvertiserId()))>-1 ) {
//					result.setViewCnt3(0);
//				} else {
//					result.setViewCnt3(result.getViewCnt());
//				}
				
			} else if( result.getDumpType().equals( GlobalConstants.EXTERNALSSP ) ){
				result.setScriptNo(result.getMediaSeq());	// ssp seq처리
				
			}
			
		} catch (Exception e) {
			logger.error("err data - {}, msg - {}", result, e);
		}
		if(result!=null) {
			result.generateKey();
		}
		return result;
	}

}
