//package com.mobon.billing.branchUM.consumer;
//
//import com.adgather.constants.G;
//import com.adgather.reportmodel.old.AdChargeData;
//import com.adgather.reportmodel.old.DrcData;
//import com.adgather.reportmodel.old.ExternalLinkageData;
//import com.adgather.reportmodel.old.RTBDrcData;
//import com.adgather.reportmodel.old.RTBReportData;
//import com.adgather.reportmodel.old.ShopStatsData;
//import com.adgather.reportmodel.old.ShortCutData;
//import com.adgather.util.old.DateUtils;
//import com.adgather.util.old.StringUtils;
//import com.mobon.billing.branchUM.consumer.ConsumerProcess;
//import com.mobon.billing.branchUM.service.DataBuilder;
//import com.mobon.billing.branchUM.service.SumObjectManager;
//import com.mobon.billing.branchUM.service.dao.SelectDao;
//import com.mobon.billing.model.ClickViewData;
//import com.mobon.billing.model.v15.BaseCVData;
//import com.mobon.billing.model.v15.ConvData;
//import com.mobon.billing.model.v15.ExternalInfoData;
//import com.mobon.billing.model.v15.ShopStatsInfoData;
//import com.mobon.billing.model.v15.ShortCutInfoData;
//import java.io.Serializable;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import net.sf.json.JSONObject;
//import org.apache.commons.lang3.SerializationUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Service;
//
//@Configuration
//@Service
//public class ConsumerProcess {
//	private static final Logger logger = LoggerFactory.getLogger(ConsumerProcess.class);
//
//	@Autowired
//	private DataBuilder dataBuilder;
//
//	@Autowired
//	private SelectDao selectDao;
//
//	@Autowired
//	private SumObjectManager sumObjectManager;
//	@Value("${profile.id}")
//	private String profileId;
//
//	public BaseCVData processMain(String topic, String message) {
//		try {
//			String className = "";
//			JSONObject jSONObject = JSONObject.fromObject(message);
//			try {
//				className = (String) jSONObject.get("className");
//			} catch (Exception e) {
//				logger.error("err convert message to map : message - {}, msg - {}", message, e.toString());
//				return null;
//			}
//
//			if (G.ClickViewData.equals(topic) || G.ClickViewPointData.equals(topic)) {
//				BaseCVData record = null;
//
//				if (G.AdChargeData.equals(className)) {
//					AdChargeData tmpAdCharge = AdChargeData.fromHashMap((Map) jSONObject);
//					record = tmpAdCharge.toBaseCVData();
//					record = processBaseCVData(record);
//				} else if (G.DrcData.equals(className)) {
//					DrcData tmpDrc = DrcData.fromHashMap((Map) jSONObject);
//					record = tmpDrc.toBaseCVData();
//					record = processBaseCVData(record);
//				} else if (G.ShortCutData.equals(className)) {
//					ShortCutData a = ShortCutData.fromHashMap((Map) jSONObject);
//					ShortCutInfoData tmp = a.toShortCutInfoData();
//					record = tmp.toBaseCVData();
//					record = processShortCutInfoData(record);
//				} else if (G.RTBReportData.equals(className) || G.RTBDrcData.equals(className)) {
//					if (G.RTBReportData.equals(className)) {
//						RTBReportData tmp = RTBReportData.fromHashMap((Map) jSONObject);
//						record = tmp.toBaseCVData();
//						record = processRtbViewData(record);
//					} else {
//						RTBDrcData tmp = RTBDrcData.fromHashMap((Map) jSONObject);
//						record = tmp.toBaseCVData();
//						record = processRtbClickData(record);
//					}
//				}
//
//				if (record != null) {
//					Date sendDate = new Date();
//					Date targetDate = new Date();
//					sendDate.setTime(
//							(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(record.getSendDate()).getTime());
//
//					if ("N".equals(record.getStatYn())) {
//						record.setViewCnt(0);
//						record.setViewCnt2(0);
//						record.setViewCnt3(0);
//						record.setClickCnt(0);
//					}
//
//					record.setPointChargeAble("false");
//
//				}
//
//			} else {
//
//				logger.error("else topic chking topic - {},  vo - {}", topic, jSONObject);
//			}
//
//		} catch (Exception e) {
//			logger.error("err ", e);
//		}
//	}
//
//	public ShopStatsInfoData processShopStatsInfoData(Map<String, String> message) {
//		ShopStatsInfoData record = null;
//		try {
//			ShopStatsData tmp1 = ShopStatsData.fromHashMap(message);
//			record = tmp1.toShopStatsInfoData();
//
//			logger.debug("ShopStatsInfoData key - {}", record.generateKey());
//
//			if (record.getCate() != null && record.getCate().length() > 15) {
//				record.setCate(record.getCate().substring(0, 15));
//			}
//			if (record.getAdvertiserId() == null) {
//				record.setAdvertiserId("");
//			}
//
//			if (record.getCate().length() > 15) {
//				record.setCate(record.getCate().substring(0, 15));
//			}
//
//			int result = this.dataBuilder.dumpShopStatsData(record);
//			if (result == 1) {
//				logger.debug("ShopStatsInfoData result = 1 : record - {}", record);
//				record = null;
//			}
//
//		} catch (Exception e) {
//			record = null;
//			logger.error("ERROR processShopStatsInfoData >> {} {}", record, e.toString());
//		}
//		return record;
//	}
//
//	public BaseCVData processShortCutInfoData(BaseCVData result) {
//		try {
//			int intResult = this.dataBuilder.dumpShortCutData(result);
//			if (intResult == 1) {
//				logger.debug("ShortCutInfoData result = 1 : record - {}", result);
//				result = null;
//			}
//		} catch (Exception e) {
//			logger.error("ERROR >> {} {}", result, e);
//		}
//		if (result != null) {
//			result.generateKey();
//		}
//		return result;
//	}
//
//	public ConvData processConversionData(ConvData data) {
//		try {
//			if (data.getOrdCode().length() > 30) {
//				data.setOrdCode(data.getOrdCode().substring(0, 30));
//				if (data.getScriptNo() == 1999) {
//					logger.debug("ordcode - {}", data.getOrdCode());
//					String[] ordcodeArr = data.getOrdCode().split(" ");
//					data.setOrdCode(ordcodeArr[0]);
//					logger.debug("ordcode - {}", data.getOrdCode());
//				}
//			}
//
//			String pnm = data.getPnm();
//			if (pnm == null)
//				pnm = "";
//			if (pnm.length() > 200)
//				pnm = pnm.substring(0, 199);
//			if (pnm.indexOf("?") > 0)
//				pnm = "";
//			data.setPnm(pnm);
//
//			data.setSvcTpCode(this.selectDao.selectMobonComCodeAdvrtsPrdtCode(data.getProduct()));
//			data.setAdvrtsTpCode(this.selectDao.convertAdvrtsTpCode(data.getAdGubun()));
//			data.setPltfomTpCode(G.convertPLATFORM_CODE(data.getPlatform()));
//
//			logger.debug("product - {}, svcTpCode - {}", data.getProduct(), data.getSvcTpCode());
//
//			if ("".equals(data.getAdvertiserId())) {
//				logger.info("chking Conv userid is null - {}", data);
//				data = null;
//			} else if (data.getScriptNo() == 1999) {
//
//				if (Integer.parseInt(data.getPrice()) > 100000000) {
//					logger.info("chking Conv over price 100000000 data - {}", data);
//					data = null;
//				} else {
//
//					data.setConversionDirect(true);
//
//					data.setShopconSerealNo(0L);
//					data.setRtbType("");
//					data.setGender("0");
//					data.setpCode("1");
//
//					data.setPlatform(data.getPlatform().toUpperCase());
//					data.setMobonYn("Y");
//
//					if (data.isUseYmdhms())
//						data.setSendDate(data.getYmdhms());
//
//					if ("".equals(data.getLastClickTime())) {
//						data.setLastClickTime(DateUtils.getDate("yyyyMMddHHmmss",
//								DateUtils.getDate("yyyy-MM-dd HH:mm:ss", data.getSendDate())));
//					}
//
//					if ("".equals(data.getpCode()))
//						data.setpCode("1");
//					if ("".equals(data.getInHour()))
//						data.setInHour("0");
//					if ("".equals(data.getMobonYn()))
//						data.setMobonYn("N");
//					if ("".equals(data.getScriptUserId()))
//						data.setScriptUserId("shoppul");
//					if ("".equals(data.getProduct()))
//						data.setProduct("b");
//					if ("".equals(data.getType()) || data.getType().length() > 1)
//						data.setType("C");
//					data.setYyyymmdd(DateUtils.getDate("yyyyMMdd",
//							(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(data.getSendDate())));
//
//					data.generateKey();
//				}
//
//			} else if (data.getScriptNo() != 0) {
//				logger.debug("Conv record - {}", data);
//
//				List<Map> listExternalUser = this.selectDao.selectExternalUserInfo();
//				ArrayList<String> extList = new ArrayList<>();
//				for (Map row : listExternalUser) {
//					extList.add(row.get("userId").toString());
//				}
//
//				HashMap<Integer, String> itlInfo = this.selectDao.selectItlInfo();
//
//				logger.debug("userid - {}, scriptNo - {}", data.getAdvertiserId(), Integer.valueOf(data.getScriptNo()));
//
//				if (extList.contains(data.getAdvertiserId())) {
//					data.setInterlock("99");
//				} else if ("kakao".equals(itlInfo.get(Integer.valueOf(data.getScriptNo())))) {
//					data.setInterlock("03");
//				} else if ("daisy".equals(itlInfo.get(Integer.valueOf(data.getScriptNo())))) {
//					data.setInterlock("02");
//				} else {
//
//					data.setInterlock("01");
//				}
//
//				logger.debug("record - {}", data.getInterlock());
//			} else {
//
//				logger.debug("Conv else skip - {}", data);
//				data = null;
//			}
//
//		} catch (Exception e) {
//			logger.error("err - {}, data - {}", data, e);
//			data = null;
//		}
//		if (data != null) {
//			data.generateKey();
//		}
//		return data;
//	}
//
//	public BaseCVData processBaseCVData(BaseCVData record) {
//		try {
//			if ("".equals(record.getKey())) {
//				record.setKey("0");
//			}
//			if (StringUtils.isEmpty(record.getNo() + "")) {
//				record.setNo(0L);
//			}
//
//			if (StringUtils.isEmpty(record.getScriptUserId())) {
//				logger.debug("Missing required scriptUserId record - {}", record);
//
//				BaseCVData ms = this.selectDao.selectMediaInfo((ClickViewData) record);
//				if (ms != null) {
//					record.setScriptUserId(ms.getScriptUserId());
//				}
//			}
//
//			if (!"0".equals(record.getKno()) && !StringUtils.isNumeric(record.getKno())) {
//				record.setKno("0");
//				logger.info("chking kno is not integer record - ", record);
//			}
//			if ("KP".equals(record.getAdGubun())) {
//				record.setAdGubun("KL");
//			}
//
//			if ("skyCharge".equals(record.getDumpType())) {
//				if ("C".equals(record.getType())) {
//					int result = this.dataBuilder.dumpSkyClickData(record);
//					if (result == 1) {
//						logger.error("SKYCHARGE result = 1 : record - {}", record);
//						record = null;
//					}
//				} else {
//					int result = this.dataBuilder.dumpSkyChargeData(record);
//					if (result == 1) {
//						logger.error("SKYCHARGE result = 1 : record - {}", record);
//						record = null;
//					}
//
//				}
//
//			} else if ("icoCharge".equals(record.getDumpType())) {
//				int result = this.dataBuilder.dumpChargeLogData(record);
//				if (result == 1) {
//					logger.error("ICOCHARGE result = 1 : record - {}", record);
//					record = null;
//				}
//
//			} else if ("plCharge".equals(record.getDumpType())) {
//				if ("C".equals(record.getType())) {
//					int result = this.dataBuilder.dumpPlayLinkClickData(record);
//					if (result == 1) {
//						record = null;
//					}
//				} else {
//					int result = this.dataBuilder.dumpPlayLinkChargeData(record);
//					if (result == 1) {
//						record = null;
//					}
//				}
//
//			} else if ("normalCharge".equals(record.getDumpType())) {
//				int result = this.dataBuilder.dumpNormalViewLogData(record);
//				if (result == 1) {
//					logger.error("NORMALCHARGE result = 1 : record - {}", record);
//					record = null;
//				}
//
//			} else if ("mobileCharge".equals(record.getDumpType())) {
//				int result = this.dataBuilder.dumpMobileChargeLogData(record);
//				if (result == 1) {
//					logger.error("MOBILECHARGE result = 1 : record - {}", record);
//					record = null;
//				}
//
//			} else if ("drcCharge".equals(record.getDumpType())) {
//				int result = this.dataBuilder.dumpDrcData(record);
//				if (result == 1) {
//					logger.error("DRCCHARGE result = 1 : record - {}", record);
//					record = null;
//				}
//
//			} else if ("shopconCharge".equals(record.getDumpType())) {
//				int result = this.dataBuilder.dumpShopConData(record);
//				if (result == 1) {
//					logger.error("SHOPCONCHARGE result = 1 : record - {}", record);
//					record = null;
//				}
//
//			} else if ("actionCharge".equals(record.getDumpType())) {
//				this.dataBuilder.dumpActData(record);
//				record = null;
//			} else {
//
//				logger.error("other - {}", record);
//
//			}
//
//		} catch (Exception e) {
//			record = null;
//			logger.error("ERROR processBaseCVData >> {} {}", record, e);
//		}
//		if (record != null) {
//			record.generateKey();
//		}
//		return record;
//	}
//
//	public BaseCVData processRtbClickData(BaseCVData result) {
//		try {
//			result.setPlatform(result.getPlatform().substring(0, 1).toUpperCase());
//			result.setType("C");
//			result.setClickCnt(1);
//
//			result.setYyyymmdd(DateUtils.getDate("yyyyMMdd",
//					(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(result.getSendDate())));
//
//			if ("kakao".equals(result.getScriptUserId()) || "mkakao".equals(result.getScriptUserId())) {
//				result.setInterlock("03");
//			} else if ("daisy".equals(result.getScriptUserId()) || "mdaisy".equals(result.getScriptUserId())) {
//				result.setInterlock("02");
//			}
//
//			logger.debug("result - {}", result);
//
//			this.dataBuilder.dumpRtbDrcData(result);
//		} catch (Exception e) {
//			logger.error("err processRtbClickData >> {} {}", result, e);
//		}
//		if (result != null) {
//			result.generateKey();
//		}
//		return result;
//	}
//
//	public BaseCVData processRtbViewData(BaseCVData result) {
//		try {
//			result.setPlatform(result.getPlatform().substring(0, 1).toUpperCase());
//			result.setType("V");
//
//			if ("kakao".equals(result.getScriptUserId()) || "mkakao".equals(result.getScriptUserId())) {
//				result.setInterlock("03");
//			} else if ("daisy".equals(result.getScriptUserId()) || "mdaisy".equals(result.getScriptUserId())) {
//				result.setInterlock("02");
//			}
//
//			logger.debug("record - {}", result);
//		} catch (Exception e) {
//			logger.error("err processRtbViewData >> {} {}", result, e);
//		}
//		if (result != null) {
//			result.generateKey();
//		}
//		return result;
//	}
//
//	public ExternalInfoData processExternalChargeData(Map<String, String> message) {
//		ExternalInfoData result = null;
//		try {
//			ExternalLinkageData tmp = ExternalLinkageData.fromHashMap(message);
//			result = tmp.toExternalInfoData();
//
//			if (result.getAdvertiserId() == null) {
//				result.setAdvertiserId("");
//			}
//
//			logger.debug("message - {}", result);
//
//			this.dataBuilder.dumpExternalBatch(result);
//
//			result.setProduct("nor");
//			if (result.getDumpType().equals("externalCharge")) {
//
//				int viewcnt_mobon = result.getViewCntMobon();
//				if (viewcnt_mobon < 1 && !"shoppul123".equals(result.getAdvertiserId())) {
//					viewcnt_mobon = 1;
//				}
//
//				if ("P".equals(result.getType())) {
//					result.setPassbackCnt(1);
//				} else if ("C".equals(result.getType())) {
//					result.setClickCntMobon(1);
//				} else {
//
//					result.setType("V");
//					result.setViewCntMobon(viewcnt_mobon);
//				}
//
//				if ("P".equals(result.getType()) || "V".equals(result.getType())) {
//					result.setType("PV");
//				}
//
//				if (!"0".equals(result.getExl_seq())) {
//					result.setType("RCV");
//				} else if (!"".equals(result.getSend_tp_code())) {
//					result.setType("SEND");
//					if ("03".equals(result.getSend_tp_code())) {
//						result.setViewCnt(result.getViewCntMobon());
//						result.setPassbackCnt(0);
//					}
//				} else {
//
//					logger.info("chking external vo - {}", result);
//				}
//
//			} else if (result.getDumpType().equals("externalBatch")) {
//
//			}
//
//			logger.debug("result - {}", result);
//		} catch (Exception e) {
//			logger.error("err data - {}, msg - {}", result, e);
//		}
//		if (result != null) {
//			result.generateKey();
//		}
//		return result;
//	}
//}
