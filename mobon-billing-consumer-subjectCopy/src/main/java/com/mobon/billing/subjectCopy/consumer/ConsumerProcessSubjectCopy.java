package com.mobon.billing.subjectCopy.consumer;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.reportmodel.old.AdChargeData;
import com.adgather.reportmodel.old.DrcData;
import com.adgather.reportmodel.old.RTBDrcData;
import com.adgather.reportmodel.old.RTBReportData;
import com.adgather.reportmodel.old.ShortCutData;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobon.billing.subjectCopy.service.DataBuilder;
import com.mobon.billing.subjectCopy.service.SumObjectManager;
import com.mobon.billing.subjectCopy.service.dao.SelectDao;
import com.mobon.billing.subjectCopy.vo.ConversionPolling;
import com.mobon.code.CodeUtils;
import com.mobon.billing.model.ClickViewData;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.model.v15.ShortCutInfoData;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Configuration
@Service
public class ConsumerProcessSubjectCopy {
	private static final Logger logger = LoggerFactory.getLogger(ConsumerProcessSubjectCopy.class);

	@Autowired
	private SumObjectManager sumObjectManager;

	@Autowired
	private SelectDao selectDao;

	@Autowired
	private DataBuilder dataBuilder;


	public void processMain(String topic, String message) {
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

		}

		if (G.ClickViewData.equals(topic) || G.ClickViewPointData.equals(topic)
				|| "OpenRTBClickData".equals(topic) || "OpenRTBViewData".equals(topic)
				) {
			// 해당 cateSeq 및 , Copy 데이터가 있는 경우만 적재 
			BaseCVData record=null;
			//CopyCate 데이터 확인  

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

			if (record != null) {
				if( "91".equals(record.getChrgTpCode()) ) {
					return;
				}
				String regUserId = "BATCH";
				try {
					InetAddress ip = InetAddress.getLocalHost();
					String serverIp = ip.getHostAddress();
					String[] spServerIp = serverIp.split("\\.");
					regUserId = regUserId + "_" + spServerIp[spServerIp.length-1];
				} catch (Exception e) {
					logger.error( "ip Host Exception - {} ", e);
				}
				record.setRegUserId(regUserId);
				
				if( "N".equals(record.getStatYn()) ) {
					record.setViewCnt(0);
					record.setViewCnt2(0);
					record.setViewCnt3(0);
					record.setClickCnt(0);
				}
				
				record = CodeUtils.setInterLock(record);
				
				if ("OpenRTBClickData".equals(topic) || "OpenRTBViewData".equals(topic)) {
					record.setScriptNo(record.getScriptHirnkNo());
				}

				BaseCVData record2 = null;
				BaseCVData record3 = null;
				BaseCVData record4 = null;
				BaseCVData record5 = null;
				BaseCVData record6 = null;
				
				try {

					record2 = record.clone();
					record3 = record.clone();
					record4 = record.clone();
					record5 = record.clone();
					record6 = record.clone();

				//미노출 전환 데이터 제외, 소재 카피 데이터가 있는경우에만

				if (! record.isNoExposureYN() &&
						record.getMobAdGrpData() != null 
						) {
					JSONArray mobAdgrpData = record.getMobAdGrpData();

					//소재  카피 데이터인경우 
					for (int i = 0; i < mobAdgrpData.size() ; i++) {
						JSONObject obj = mobAdgrpData.getJSONObject(i);
						Map<String, ArrayList<String>> adGrpData = this.getGrpData(obj);						
						// 소재 
						if (adGrpData.containsKey("01")) {
							ArrayList<String> grpData = adGrpData.get("01");
							record.setGrpId(grpData.get(0));
							record.setGrpTpCode(grpData.get(1));
							record.setSubjectCopyTpCode(grpData.get(2));
							if (record.getAdvrtsStleTpCode().equals("02")) {
								sumObjectManager.appendSubjectCopyClickViewData(record);
							}
						}
						//카피
						if (adGrpData.containsKey("02")) {
							ArrayList<String> grpData = adGrpData.get("02");
							record2.setGrpId(grpData.get(0));
							record2.setGrpTpCode(grpData.get(1));
							record2.setSubjectCopyTpCode(grpData.get(2));
							sumObjectManager.appendSubjectCopyClickViewData(record2);

						}
						// 소재 + 카피 
						if (adGrpData.containsKey("01") && adGrpData.containsKey("02")) {
							if (record3.getAdvrtsStleTpCode().equals("02")) {
								ArrayList<String> subjectGrpData = adGrpData.get("01");
								ArrayList<String> copyGrpData = adGrpData.get("02");
								record3.setGrpId(subjectGrpData.get(0) +"-"+copyGrpData.get(0));
								record3.setGrpTpCode("03");
								record3.setSubjectCopyTpCode(subjectGrpData.get(2) + "-" + copyGrpData.get(2));
								sumObjectManager.appendSubjectCopyClickViewData(record3);
							}
						}

					}
					// 소재 카피 데이터가 있는경우  무조건 하나만 적재
					record4.setGrpTpCode("04");
					record4.setGrpId("00");
					record4.setSubjectCopyTpCode("99");
					sumObjectManager.appendSubjectCopyClickViewData(record4);
					
				} else if (!record.isNoExposureYN()) {
					//소재 카피 데이터가 없는경우 및 모든 광고 구분 적재 ( 미노출 제외 )
					record5.setGrpTpCode("05");
					record5.setGrpId("00");
					record5.setSubjectCopyTpCode("99");
					sumObjectManager.appendSubjectCopyClickViewData(record5);
					
				} else if (record.isNoExposureYN()) {
					// 미노출 데이터 적재
					record6.setGrpTpCode("06");
					record6.setGrpId("00");
					record6.setSubjectCopyTpCode("99");
					sumObjectManager.appendSubjectCopyClickViewData(record6);
				}

				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} else if (G.SuccConversion.equals(topic)) {
			// 전환은 새로운 토픽명의 데이터 가 있는경우 해당 데이터 바로 적재 
			try {
				ConversionPolling item = new ObjectMapper().readValue(message, ConversionPolling.class);
				JSONArray mobAdgrpData = item.getMobAdGrpData();
				ConversionPolling item2 = null;
				ConversionPolling item3 = null;
				ConversionPolling item4 = null;
				ConversionPolling item5 = null;
				
				//itl_tp_code 변환 
				BaseCVData vo = new BaseCVData();
				vo.setScriptUserId(item.getScriptUserId());				
				vo = CodeUtils.setInterLock(vo);				
				item.setItlTpCode(vo.getInterlock());
				
				/*send Date 에서 시간만 빼서 hh */
				String sendDateStr = item.getSendDate();
				SimpleDateFormat trans = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat trans2 = new SimpleDateFormat("HH");
				Date sendDate = trans.parse(sendDateStr);
				
				item.setHh(trans2.format(sendDate));
				item2 = item.clone();
				item3 = item.clone();
				item4 = item.clone();
				item5 = item.clone();

				if (mobAdgrpData != null) {
					for (int i = 0; i < mobAdgrpData.size() ; i++) {
						JSONObject obj = mobAdgrpData.getJSONObject(i);
						Map<String, ArrayList<String>> adGrpData = this.getGrpData(obj);
						//소재
						if (adGrpData.containsKey("01")) {
							if (item.getAdvrtsStleTpCode().equals("02")) {
								ArrayList<String> grpData = adGrpData.get("01");
								item.setGrpId(grpData.get(0));
								item.setGrpTpCode(grpData.get(1));
								item.setSubjectCopyTpCode(grpData.get(2));
								sumObjectManager.appendSubjectCopyConvData(item);
							}
						}
						//카피
						if (adGrpData.containsKey("02")) {
							ArrayList<String> grpData = adGrpData.get("02");
							item2.setGrpId(grpData.get(0));
							item2.setGrpTpCode(grpData.get(1));
							item2.setSubjectCopyTpCode(grpData.get(2));
							sumObjectManager.appendSubjectCopyConvData(item2);
						}
						//소재 + 카피
						if (adGrpData.containsKey("01") && adGrpData.containsKey("02")) {
							if (item3.getAdvrtsStleTpCode().equals("02")) {
								ArrayList<String> subjectGrpData = adGrpData.get("01");
								ArrayList<String> copyGrpData = adGrpData.get("02");
								item3.setGrpId(subjectGrpData.get(0) +"-"+copyGrpData.get(0));
								item3.setGrpTpCode("03");
								item3.setSubjectCopyTpCode(subjectGrpData.get(2) + "-" + copyGrpData.get(2));
								sumObjectManager.appendSubjectCopyConvData(item3);
							}
						}
						// 소재 카피 데이터가 하나라도 있는경우  무조건 하나만 적재
						if (adGrpData.containsKey("01") || adGrpData.containsKey("02")) {
							item4.setGrpTpCode("04");
							item4.setGrpId("00");
							item4.setSubjectCopyTpCode("99");
							sumObjectManager.appendSubjectCopyConvData(item4);
						}
						//소재 카피 데이터 둘다 없는 경우 또는 둘다 공백인 경우
						if (! adGrpData.containsKey("01") && !adGrpData.containsKey("02")) {
							item5.setGrpTpCode("05");
							item5.setGrpId("00");
							item5.setSubjectCopyTpCode("99");
							sumObjectManager.appendSubjectCopyConvData(item5);
						}
					}
					//해당 mobAdgrpData의 사이즈 자체가 0 인 경우
					if (mobAdgrpData.size() == 0) {
						item5.setGrpTpCode("05");
						item5.setGrpId("00");
						item5.setSubjectCopyTpCode("99");
						sumObjectManager.appendSubjectCopyConvData(item5);
					}
				} else {
					//소재 카피 데이터가 아예 없는경우 및 모든 광고 구분 적재 ( 미노출 포함 )
					item5.setGrpTpCode("05");
					item5.setGrpId("00");
					item5.setSubjectCopyTpCode("99");
					sumObjectManager.appendSubjectCopyConvData(item5);					
				}
			} catch (Exception e) {
				logger.error("ConversionPolling Fail -{}", message);
			}
		} 
	}	

	private Map<String, ArrayList<String>> getGrpData(JSONObject obj) {
		//소재  
		String imgGrpId = StringUtils.trimToNull2(obj.get("mobon_ad_grp_id_i"), "");						
		String imgGrpTpCode = StringUtils.trimToNull2(obj.get("ad_grp_tp_code_i"), "");						
		String imgTpCode = StringUtils.trimToNull2(obj.get("image_tp_code"), "");
		//카피
		String copyGrpId = StringUtils.trimToNull2(obj.get("mobon_ad_grp_id_c"), "");						
		String copyGrpTpCode = StringUtils.trimToNull2(obj.get("ad_grp_tp_code_c"), "");						
		String copyTpCode = StringUtils.trimToNull2(obj.get("cp_tp_code"), "");	

		boolean isImgData = ! "".equals(imgGrpId) && ! "".equals(imgGrpTpCode) && ! "".equals(imgTpCode);
		boolean isCopyData = ! "".equals(copyGrpId) && ! "".equals(copyGrpTpCode) && ! "".equals(copyTpCode);

		HashMap <String , ArrayList<String>> result = new HashMap<String , ArrayList<String>>();
		if (isImgData) {
			ArrayList <String> data = new ArrayList<String> ();
			data.add(imgGrpId);
			data.add(imgGrpTpCode);
			data.add(imgTpCode);
			result.put("01", data);
		}
		if (isCopyData ) {
			ArrayList <String> data = new ArrayList<String> ();
			data.add(copyGrpId);
			data.add(copyGrpTpCode);
			data.add(copyTpCode);
			result.put("02", data);
		}

		return result;
	}

	public BaseCVData processShortCutInfoData(BaseCVData result) {
		try {
			int intResult = this.dataBuilder.dumpShortCutData(result);
			if (intResult == 1) {
				logger.debug("ShortCutInfoData result = 1 : record - {}", result);
				result = null;
			}
		} catch (Exception e) {
			logger.error("ERROR >> {} {}", result, e);
		}
		if (result != null) {
			result.generateKey();
		}
		return result;
	}	

	public BaseCVData processBaseCVData(BaseCVData record) {
		try {
			if ("".equals(record.getKey())) {
				record.setKey("0");
			}
			if (StringUtils.isEmpty(record.getNo() + "")) {
				record.setNo(0L);
			}

			if (StringUtils.isEmpty(record.getScriptUserId())) {
				logger.debug("Missing required scriptUserId record - {}", record);

				BaseCVData ms = this.selectDao.selectMediaInfo((ClickViewData) record);
				if (ms != null) {
					record.setScriptUserId(ms.getScriptUserId());
				}
			}

			if (!"0".equals(record.getKno()) && !StringUtils.isNumeric(record.getKno())) {
				record.setKno("0");
				logger.info("chking kno is not integer record - ", record);
			}
			if ("KP".equals(record.getAdGubun())) {
				record.setAdGubun("KL");
			}

			if ("skyCharge".equals(record.getDumpType())) {
				if ("C".equals(record.getType())) {
					int result = this.dataBuilder.dumpSkyClickData(record);
					if (result == 1) {
						logger.error("SKYCHARGE result = 1 : record - {}", record);
						record = null;
					}
				} else {
					int result = this.dataBuilder.dumpSkyChargeData(record);
					if (result == 1) {
						logger.error("SKYCHARGE result = 1 : record - {}", record);
						record = null;
					}

				}

			} else if ("icoCharge".equals(record.getDumpType())) {
				int result = this.dataBuilder.dumpChargeLogData(record);
				if (result == 1) {
					logger.error("ICOCHARGE result = 1 : record - {}", record);
					record = null;
				}

			} else if ("plCharge".equals(record.getDumpType())) {
				if ("C".equals(record.getType())) {
					int result = this.dataBuilder.dumpPlayLinkClickData(record);
					if (result == 1) {
						record = null;
					}
				} else {
					int result = this.dataBuilder.dumpPlayLinkChargeData(record);
					if (result == 1) {
						record = null;
					}
				}

			} else if ("normalCharge".equals(record.getDumpType())) {
				int result = this.dataBuilder.dumpNormalViewLogData(record);
				if (result == 1) {
					logger.error("NORMALCHARGE result = 1 : record - {}", record);
					record = null;
				}

			} else if ("mobileCharge".equals(record.getDumpType())) {
				int result = this.dataBuilder.dumpMobileChargeLogData(record);
				if (result == 1) {
					logger.error("MOBILECHARGE result = 1 : record - {}", record);
					record = null;
				}

			} else if ("drcCharge".equals(record.getDumpType())) {
				int result = this.dataBuilder.dumpDrcData(record);
				if (result == 1) {
					logger.error("DRCCHARGE result = 1 : record - {}", record);
					record = null;
				}

			} else if ("shopconCharge".equals(record.getDumpType())) {
				int result = this.dataBuilder.dumpShopConData(record);
				if (result == 1) {
					logger.error("SHOPCONCHARGE result = 1 : record - {}", record);
					record = null;
				}

			} else if ("actionCharge".equals(record.getDumpType())) {
				this.dataBuilder.dumpActData(record);
				record = null;
			} else {

				logger.error("other - {}", record);

			}

		} catch (Exception e) {
			record = null;
			logger.error("ERROR processBaseCVData >> {} {}", record, e);
		}
		if (record != null) {
			record.generateKey();
		}
		return record;
	}

	public BaseCVData processRtbClickData(BaseCVData result) {
		try {
			result.setPlatform(result.getPlatform().substring(0, 1).toUpperCase());
			result.setType("C");
			result.setClickCnt(1);

			result.setYyyymmdd(DateUtils.getDate("yyyyMMdd",
					(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(result.getSendDate())));

			if ("kakao".equals(result.getScriptUserId()) || "mkakao".equals(result.getScriptUserId())) {
				result.setInterlock("03");
			} else if ("daisy".equals(result.getScriptUserId()) || "mdaisy".equals(result.getScriptUserId())) {
				result.setInterlock("02");
			}

			logger.debug("result - {}", result);

			this.dataBuilder.dumpRtbDrcData(result);
		} catch (Exception e) {
			logger.error("err processRtbClickData >> {} {}", result, e);
		}
		if (result != null) {
			result.generateKey();
		}
		return result;
	}

	public BaseCVData processRtbViewData(BaseCVData result) {
		try {
			result.setPlatform(result.getPlatform().substring(0, 1).toUpperCase());
			result.setType("V");

			if ("kakao".equals(result.getScriptUserId()) || "mkakao".equals(result.getScriptUserId())) {
				result.setInterlock("03");
			} else if ("daisy".equals(result.getScriptUserId()) || "mdaisy".equals(result.getScriptUserId())) {
				result.setInterlock("02");
			}

			logger.debug("record - {}", result);
		} catch (Exception e) {
			logger.error("err processRtbViewData >> {} {}", result, e);
		}
		if (result != null) {
			result.generateKey();
		}
		return result;
	}



}
