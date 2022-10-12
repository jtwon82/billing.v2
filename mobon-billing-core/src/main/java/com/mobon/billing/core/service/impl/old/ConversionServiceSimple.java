package com.mobon.billing.core.service.impl.old;

import java.util.List;

import com.mobon.billing.core.service.dao.ShopInfoDataDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.dao.ConvDataDao;
import com.mobon.billing.core.service.dao.IntgCntrConvDataDao;
import com.mobon.billing.core.service.old.ConversionService;
import com.mobon.billing.model.v15.ConvData;
import com.mobon.code.CNVRS_ABUSING_TP_CODE;
import com.mobon.conversion.domain.old.ConversionCode;
import com.mobon.conversion.domain.old.ConversionInfo;
import com.mobon.conversion.domain.old.ConversionInfoFilter;

@Service
public class ConversionServiceSimple implements ConversionService {
	private static final Logger logger = LoggerFactory.getLogger(ConversionServiceSimple.class);
	private static final boolean USE_NEW_FREQ_POLICY = true;	// 신규 프리퀀시 정책 적용 여부 (기존 정책 사용시 false로)

	@Autowired
	private ConvDataDao			convDataDao;
//	@Autowired
//	private ShopInfoDataDao		shopInfoDataDao;
//	@Autowired
//	private IntgCntrConvDataDao IntgCntrconvDataDao;
	@Autowired
	private SumObjectManager	sumObjectManager;

	@Override
	public boolean isAvailableFrequency(ConversionInfoFilter filter, ConvData data) {
		boolean isAvailalbeConv = false;
		String freqVer = "1";
		try {
			logger.info("Conv convStatus - {}, directHour -{}, indirectHour -{}, serviceCode -{}, chargeCode -{}, scriptNo -{}, userId -{}, clientId -{}"
					, filter.getConvStatus(), filter.getDirectHour(), filter.getIndirectHour(), filter.getServiceCode(), filter.getChargeCode(), filter.getScriptNo(), filter.getUserId(), filter.getClientId());
			logger.info("Conv filter - {}", filter);

            if(Integer.parseInt(data.getPrice())>100000000) {
                filter.setChargeCode(ConversionCode.INVALID_PRICE_OVER_CODE);
                logger.debug("price over 100000000 {}, {}", data.getKeyIp(), data.getOrdCode());

                if(ConversionCode.SERVICE_CODE.equals(filter.getServiceCode())){
                	if( "hanasys".equals(data.getAdvertiserId()) ) {
                	}else {
	                	ConvData abusingData= SerializationUtils.clone(data);
	                    sumObjectManager.appendConvAbusingData(abusingData, CNVRS_ABUSING_TP_CODE.ONE_HUNDRED_MILLION.getValue());
                	}
//    				logger.info("ConvAbusing one_hundred_million, keyIp:{}, ordCode:{}", data.getKeyIp(), data.getOrdCode());
//	                data.getAbusingMap().put(CNVRS_ABUSING_TP_CODE.ONE_HUNDRED_MILLION.getValue(), CNVRS_ABUSING_TP_CODE.ONE_HUNDRED_MILLION.getValue());
                }
                
            } else if (filter.getConvStatus() == ConversionCode.CONV_INVALID) {		// 3
                filter.setChargeCode(ConversionCode.INVALID_CONV_CODE);
                
                if(ConversionCode.SERVICE_CODE.equals(filter.getServiceCode())){
//	                ConvData abusingData= SerializationUtils.clone(data);
//	                sumObjectManager.appendConvAbusingData(abusingData, CNVRS_ABUSING_TP_CODE.ETC.getValue());
//					logger.info("ConvAbusing etc, keyIp:{}, ordCode:{}", data.getKeyIp(), data.getOrdCode());
//	                data.getAbusingMap().put(CNVRS_ABUSING_TP_CODE.ETC.getValue(), CNVRS_ABUSING_TP_CODE.ETC.getValue());
                }
                
            } else {
                isAvailalbeConv = USE_NEW_FREQ_POLICY
						? isAvailableConv(filter, data, freqVer)
						: isAvailableConvOld(filter, data, freqVer);
            }

            logger.info("Conv serviceCode - {}, chargeCode - {}, scriptNo - {}, userId - {}, convStatus - {}, "
                    , filter.getServiceCode(), filter.getChargeCode(), filter.getScriptNo()
                    , filter.getUserId(), filter.getConvStatus());

	        convDataDao.insConvLog(filter, data);

        } catch (Exception e) {
			logger.info("Conv err - {}", e);
			isAvailalbeConv = false;
		}
		return isAvailalbeConv;
	}

	@Override
	public boolean isAvailableFrequencyV2(ConversionInfoFilter filter, ConvData data) {
		boolean isAvailalbeConv = false;
        String freqVer = "2";
        try {
			logger.info("ConvV2 convStatus - {}, directHour -{}, indirectHour -{}, serviceCode -{}, chargeCode -{}, scriptNo -{}, userId -{}, clientId -{}"
					, filter.getConvStatus(), filter.getDirectHour(), filter.getIndirectHour(), filter.getServiceCode(), filter.getChargeCode(), filter.getScriptNo(), filter.getUserId(), filter.getClientId());
			logger.info("ConvV2 filter - {}", filter);

            if(Integer.parseInt(data.getPrice())>100000000) {
                filter.setChargeCode(ConversionCode.INVALID_PRICE_OVER_CODE);
                logger.debug("price over 100000000 {}, {}", data.getKeyIp(), data.getOrdCode());
            } else if (filter.getConvStatus() == ConversionCode.CONV_INVALID) {		// 3
                filter.setChargeCode(ConversionCode.INVALID_CONV_CODE);
            } else {
				isAvailalbeConv = USE_NEW_FREQ_POLICY
						? isAvailableConv(filter, data, freqVer)
						: isAvailableConvOld(filter, data, freqVer);
            }

            logger.info("ConvV2 serviceCode - {}, chargeCode - {}, scriptNo - {}, userId - {}, convStatus - {}, "
                    , filter.getServiceCode(), filter.getChargeCode(), filter.getScriptNo()
                    , filter.getUserId(), filter.getConvStatus());

            convDataDao.insConvLog(filter, data);
		} catch (Exception e) {
			logger.info("ConvV2 err - {}", e);
			isAvailalbeConv = false;
		}
		return isAvailalbeConv;
	}

	private int getLogCnt(ConversionInfoFilter filter, ConvData data, String freqVer) {
		List<ConversionInfo> logs = convDataDao.selConvLogs(filter, data);
		int logCnt = 0;
		if (CollectionUtils.isNotEmpty(logs)) {
			logCnt = logs.size();
		}

		String logKey = freqVer.equals("1")
                ? ""
                : "V" + freqVer;
		logger.info("Conv" + logKey + " logCnt - {}", logCnt);
		return logCnt;
	}

	/**
	 * 신규 컨버전 프리퀀시 정책
	 */
	private boolean isAvailableConv(ConversionInfoFilter filter, ConvData data, String freqVer) {
		int originalConvStatus = filter.getConvStatus();  // 기존 컨버전 타입
		int compareConvStatus = filter.getConvStatus();  // 비교할 컨버전 타입
		boolean isAvailableConv = false;  // 컨버전 유효여부

		/* 신규 프리퀀시 적용 전 inHour, direct */
		data.setBefore_inHour(data.getInHour());
		data.setBefore_direct(data.getDirect());

		if (compareConvStatus == ConversionCode.CONV_SESSION) {     // 0
			if (getLogCnt(filter, data, freqVer) < 11) {
				isAvailableConv = true;
				filter.setChargeCode(ConversionCode.VALID_SESSION_CODE);
			} else {
				// 세션 무효 --> 직접 유효
				compareConvStatus = ConversionCode.CONV_DIRECT;
				filter.setChargeCode(ConversionCode.VALID_DIRECT_CODE);
				data.setInHour("24");
				data.setDirect(0);
				filter.setChargeCodeChanged(true);

//                ConvData abusingData= SerializationUtils.clone(data);
//                sumObjectManager.appendConvAbusingData(abusingData, CNVRS_ABUSING_TP_CODE.SSN.getValue());
//				logger.info("ConvAbusing ssn, keyIp:{}, ordCode:{}", data.getKeyIp(), data.getOrdCode());
			}
		}

		if (!isAvailableConv && compareConvStatus == ConversionCode.CONV_DIRECT) {      // 1
			if (getLogCnt(filter, data, freqVer) < 15) {
				isAvailableConv = true;
				filter.setChargeCode(ConversionCode.VALID_DIRECT_CODE);
			} else {
				// 직접 무효 --> 간접 유효
				// or 세션 무효 --> 직접 무효 --> 간접 유효
				compareConvStatus = ConversionCode.CONV_INDIRECT;
				filter.setChargeCode(ConversionCode.VALID_INDIRECT_CODE);
                data.setInHour("0");
                data.setDirect(0);
                filter.setChargeCodeChanged(true);

//                ConvData abusingData= SerializationUtils.clone(data);
//                sumObjectManager.appendConvAbusingData(abusingData, CNVRS_ABUSING_TP_CODE.DIRECT.getValue());
//				logger.info("ConvAbusing direct, keyIp:{}, ordCode:{}", data.getKeyIp(), data.getOrdCode());
			}
		}

		if (!isAvailableConv && compareConvStatus == ConversionCode.CONV_INDIRECT) {        // 2
			if (getLogCnt(filter, data, freqVer) < 19) {
				isAvailableConv = true;
				filter.setChargeCode(ConversionCode.VALID_INDIRECT_CODE);
			} else {
				filter.setChargeCode(ConversionCode.INVALID_CONV_CODE);
                data.setInHour("0");
                data.setDirect(1);
                filter.setChargeCodeChanged(true);

                if(ConversionCode.SERVICE_CODE.equals(filter.getServiceCode())){
	                ConvData abusingData= SerializationUtils.clone(data);
	                sumObjectManager.appendConvAbusingData(abusingData, CNVRS_ABUSING_TP_CODE.INDIRECT.getValue());
//					logger.info("ConvAbusing indirect, keyIp:{}, ordCode:{}", data.getKeyIp(), data.getOrdCode());
//	                data.getAbusingMap().put(CNVRS_ABUSING_TP_CODE.INDIRECT.getValue(), CNVRS_ABUSING_TP_CODE.INDIRECT.getValue());
                }
			}
		}

		if (!isAvailableConv) {
			if (originalConvStatus == ConversionCode.CONV_SESSION) {
				filter.setChargeCode(ConversionCode.INVALID_SESSION_CODE);
			} else if (originalConvStatus == ConversionCode.CONV_DIRECT) {
				filter.setChargeCode(ConversionCode.INVALID_DIRECT_CODE);
			} else if (originalConvStatus == ConversionCode.CONV_INDIRECT) {
				filter.setChargeCode(ConversionCode.INVALID_INDIRECT_CODE);
			}
		}

		/*
		 * A/B 테스트 타입 설정
		 * status가 변경된 경우 신규 프리퀀시 정책 로직을 통과한 것으로 판단
		 */
		if (originalConvStatus == compareConvStatus) {
			data.setAbType("A");	// AS-IS
		} else {
			data.setAbType("B");	// TO-BE
		}
		//shopInfoDataDao.insConvFreqABTest(data);

		return isAvailableConv;
	}

	/**
	 * 기존 컨버전 프리퀀시 정책
	 */
	private boolean isAvailableConvOld(ConversionInfoFilter filter, ConvData data, String freqVer) {
		boolean isAvailableConv = false;  // 컨버전 유효여부
		int logCnt = getLogCnt(filter, data, freqVer);

		if (filter.getConvStatus() == ConversionCode.CONV_SESSION) {	// 0
			if (logCnt >= 2) {
				filter.setChargeCode(ConversionCode.INVALID_SESSION_CODE);
			} else {
				isAvailableConv = true;
			}
		} else if (filter.getConvStatus() == ConversionCode.CONV_DIRECT) {		// 1
			if (logCnt >= 3) {
				filter.setChargeCode(ConversionCode.INVALID_DIRECT_CODE);
			} else {
				isAvailableConv = true;
			}
		} else if (filter.getConvStatus() == ConversionCode.CONV_INDIRECT) {	// 2
			if (logCnt >= 4) {
				filter.setChargeCode(ConversionCode.INVALID_INDIRECT_CODE);
			} else {
				isAvailableConv = true;
			}
		}

		return isAvailableConv;
	}
}
