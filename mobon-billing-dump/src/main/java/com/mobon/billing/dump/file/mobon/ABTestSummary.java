package com.mobon.billing.dump.file.mobon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.jcraft.jsch.Logger;
import com.mobon.billing.dump.constants.GlobalConstants;
import com.mobon.billing.dump.domainmodel.abtest.ABAdverStatsMobile;
import com.mobon.billing.dump.domainmodel.abtest.ABAdverStatsWeb;
import com.mobon.billing.dump.domainmodel.abtest.ABComFrameSize;
import com.mobon.billing.dump.domainmodel.abtest.ABFrameSize;
import com.mobon.billing.dump.domainmodel.abtest.ABComStats;
import com.mobon.billing.dump.domainmodel.abtest.ABCombiFrameSize;
import com.mobon.billing.dump.domainmodel.abtest.ABParStats;
import com.mobon.billing.dump.domainmodel.abtest.key.ABAdverStatsMobileKey;
import com.mobon.billing.dump.domainmodel.abtest.key.ABAdverStatsWebKey;
import com.mobon.billing.dump.domainmodel.abtest.key.ABComFrameSizeKey;
import com.mobon.billing.dump.domainmodel.abtest.key.ABFrameSizeKey;
import com.mobon.billing.dump.domainmodel.abtest.key.ABComStatsKey;
import com.mobon.billing.dump.domainmodel.abtest.key.ABCombiFrameSizeKey;
import com.mobon.billing.dump.domainmodel.abtest.key.ABParStatsKey;
import com.mobon.billing.dump.file.mobon.vo.MobonFileDataVO;

import lombok.extern.slf4j.Slf4j;

/**
 * @FileName : ABTestSummary.java
 * @Project : mobon-billing-dump
 * @Date : 2020. 3. 2.
 * @Author dkchoi
 * @Comment : 전달된 파일 라인을 도메인별로 구분하여 데이터를 Summary하는 클래스.
 */

@Slf4j
public class ABTestSummary extends DataFilter {

	private Map<String, ABComStats> abComStatsMap = new HashMap<String, ABComStats>();
	private Map<String, ABParStats> abParStatsMap = new HashMap<String, ABParStats>();
	private Map<String, ABAdverStatsWeb> abAdverStatsWebMap = new HashMap<String, ABAdverStatsWeb>();
	private Map<String, ABAdverStatsMobile> abAdverStatsMobileMap = new HashMap<String, ABAdverStatsMobile>();
	private Map<String, ABComFrameSize> abComFrameSizeMap = new HashMap<String, ABComFrameSize>();
	private Map<String, ABFrameSize> abFrameSizeMap = new HashMap<String, ABFrameSize>();
	private Map<String, ABCombiFrameSize> abCombiFrameSizeMap = new HashMap<String, ABCombiFrameSize>();


	/**
	 * @Method Name : getABTestData
	 * @Date : 2020. 08. 20.
	 * @Author : dkchoi
	 * @Comment : Summary된 데이터를 return 받는다.
	 * @param
	 * @return totSumDataList
	 */
	public Map<String, Object> getABTestData() {
		Map<String, Object> totSumDataList = new HashMap<String, Object>();

		totSumDataList.put(GlobalConstants.ABCOMSTATS_SUMMARY_RESULT, abComStatsMap);
		totSumDataList.put(GlobalConstants.ABPARSTATS_SUMMARY_RESULT, abParStatsMap);
		totSumDataList.put(GlobalConstants.ABADVERSTATSWEB_SUMMARY_RESULT, abAdverStatsWebMap);
		totSumDataList.put(GlobalConstants.ABADVERSTATSMOBILE_SUMMARY_RESULT, abAdverStatsMobileMap);
		totSumDataList.put(GlobalConstants.ABCOMFRAMESIZE_SUMMARY_RESULT, abComFrameSizeMap);
		totSumDataList.put(GlobalConstants.ABCOMBIFRAMESIZE_SUMMARY_RESULT, abCombiFrameSizeMap);
		totSumDataList.put(GlobalConstants.ABFRAMESIZE_SUMMARY_RESULT, abFrameSizeMap);

		return totSumDataList;
	}

	/**
	 * @Method Name : setABTestData
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : 파일 라인을 도메인별로 Summary한다.
	 * @param fLine
	 * @return
	 */
	public void setABTestData(MobonFileDataVO fLine) {

		if(!StringUtils.isEmpty(fLine.getStatsDttm())) {

			if(!StringUtils.isEmpty(fLine.getAbtestTypes()) && isCollectionMsNo(fLine)) { // ABTEST 타입 값 이상 유무 확인, 집계 예외지면 Skip.
				String[] abtestTypes = fLine.getAbtestTypes();

				if(!adverIDLengthCheck(fLine)) { // 광고주 ID길이가 테이블 필드 길이보다 길 경우 return;
					log.error("AdverID Error , Values = [" + fLine.getAdverId() + "]");
					return;
				}

				for (int i = 0;  i < abtestTypes.length; i++) {

					if(abtestTypes[i].length() > 9 ) { // ABTEST 타입 값이 테이블 필드 값 보다 클 경우 Continue
						log.error("abtestTypes Error , Values = [" + abtestTypes[i] + "]");
						continue;
					}

					SummaryABComStats(fLine, abtestTypes[i]);
					SummaryABParStats(fLine, abtestTypes[i]);
					SummaryABAdverStatsWeb(fLine, abtestTypes[i]);
					SummaryABAdverStatsMobile(fLine, abtestTypes[i]);
					SummaryABComFrameSize(fLine, abtestTypes[i]);
					SummaryABCombiFrameSize(fLine, abtestTypes[i]);
					SummaryABFrameSize(fLine, abtestTypes[i]);
				}

			}

		}

	}


	/**
	 * @Method Name : SummaryABComStats
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : AB_COM_STATS 도메인을 기준으로 데이터를 Summary하는 메소드.
	 * @param fLine
	 * @param abtestTypes
	 */
	private void SummaryABComStats(MobonFileDataVO fLine, String abtestTypes) {
		ABComStats abComStats = abComStatsMap.getOrDefault(fLine.getABComStatsKey()+abtestTypes,
				ABComStats.builder().id(ABComStatsKey.builder()
						.statsDttm(Integer.parseInt(fLine.getStatsDttm()))
						.pltfomTpCode(fLine.getPltfomTpCode())
						.advrtsPrdtCode(fLine.getAdvrtsPrdtCode())
						.advrtsTpCode(fLine.getAdvrtsTpCode())
						.abTestTy(abtestTypes)
						.itlTpCode(fLine.getItlTpCode()).build())
						.totEprsCnt(0)
						.parEprsCnt(0)
						.clickCnt(0)
						.advrtsAmt(BigDecimal.ZERO)
						.mediaPymntAmt(BigDecimal.ZERO)
						.totOrderCnt(0)
						.totOrderAmt(0)
						.sessOrderCnt(0)
						.sessOrderAmt(0)
						.directOrderCnt(0)
						.directOrderAmt(0)
						.regUserId("ABTEST_BATCH")
						.build()
		);

		abComStats.addTotEprsCnt("V".equals(fLine.getAction())?fLine.getTotEprsCnt():0);
		abComStats.addParEprsCnt("V".equals(fLine.getAction())?fLine.getParEprsCnt():0);
		abComStats.addClickCnt("C".equals(fLine.getAction())?fLine.getClickCnt():0);
		abComStats.addAdvrtsAmt(fLine.getAdvrtsAmt());
		abComStats.addMediaPymntAmt(fLine.getMediaPymntAmt());
		abComStats.addTotOrderCnt(fLine.getOrderCnt());
		abComStats.addTotOrderAmt(fLine.getOrderAmt());
		abComStats.addSessOrderCnt("1".equals(fLine.getDirect())?fLine.getOrderCnt():0);
		abComStats.addSessOrderAmt("1".equals(fLine.getDirect())?fLine.getOrderAmt():0);
		abComStats.addDirectOrderCnt("24".equals(fLine.getInHour())?fLine.getOrderCnt():0);
		abComStats.addDirectOrderAmt("24".equals(fLine.getInHour())?fLine.getOrderAmt():0);

		abComStatsMap.put(fLine.getABComStatsKey()+abtestTypes, abComStats);
	}

	/**
	 * @Method Name : SummaryABParStats
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : AB_PAR_STATS 도메인을 기준으로 데이터를 Summary하는 메소드.
	 *            지면별 전체 데이터를 수집할 경우 너무 많은 ROW가 발생하므로 ABPARSTATS_COLLECT_MS_NO에 포함되어 있는 지면만 Summary한다.
	 * @param fLine
	 * @param abtestTypes
	 */
	private void SummaryABParStats(MobonFileDataVO fLine, String abtestTypes) {

		// 수집지면
		if(GlobalConstants.ABPARSTATS_COLLECT_MS_NO.indexOf(","+fLine.getMediaScriptNo()+",") == -1)
			return;
		
		// 수집군 타입 (BD 군만 수집 가능하도록 추가)
		if (!abtestTypes.startsWith("BD")) {
			return;
		}

		ABParStats abParStats = abParStatsMap.getOrDefault(fLine.getABParStatsKey()+abtestTypes,
				ABParStats.builder().id(ABParStatsKey.builder()
						.statsDttm(Integer.parseInt(fLine.getStatsDttm()))
						.pltfomTpCode(fLine.getPltfomTpCode())
						.advrtsPrdtCode(fLine.getAdvrtsPrdtCode())
						.advrtsTpCode(fLine.getAdvrtsTpCode())
						.mediaScriptNo(fLine.getMediaScriptNo())
						.abTestTy(abtestTypes)
						.itlTpCode(fLine.getItlTpCode()).build())
						.totEprsCnt(0)
						.parEprsCnt(0)
						.clickCnt(0)
						.advrtsAmt(BigDecimal.ZERO)
						.mediaPymntAmt(BigDecimal.ZERO)
						.totOrderCnt(0)
						.totOrderAmt(0)
						.sessOrderCnt(0)
						.sessOrderAmt(0)
						.directOrderCnt(0)
						.directOrderAmt(0)
						.regUserId("ABTEST_BATCH")
						.build()
		);

		abParStats.addTotEprsCnt("V".equals(fLine.getAction())?fLine.getTotEprsCnt():0);
		abParStats.addParEprsCnt("V".equals(fLine.getAction())?fLine.getParEprsCnt():0);
		abParStats.addClickCnt("C".equals(fLine.getAction())?fLine.getClickCnt():0);
		abParStats.addAdvrtsAmt(fLine.getAdvrtsAmt());
		abParStats.addMediaPymntAmt(fLine.getMediaPymntAmt());
		abParStats.addTotOrderCnt(fLine.getOrderCnt());
		abParStats.addTotOrderAmt(fLine.getOrderAmt());
		abParStats.addSessOrderCnt("1".equals(fLine.getDirect())?fLine.getOrderCnt():0);
		abParStats.addSessOrderAmt("1".equals(fLine.getDirect())?fLine.getOrderAmt():0);
		abParStats.addDirectOrderCnt("24".equals(fLine.getInHour())?fLine.getOrderCnt():0);
		abParStats.addDirectOrderAmt("24".equals(fLine.getInHour())?fLine.getOrderAmt():0);

		abParStatsMap.put(fLine.getABParStatsKey()+abtestTypes, abParStats);
	}

	/**
	 * @Method Name : SummaryABAdverStatsWeb
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : AB_ADVER_STATS_WEB 도메인을 기준으로 데이터를 Summary하는 메소드. (웹)
	 *            광고주별 추천별 전체 데이터를 수집할 경우 너무 많은 ROW가 발생하므로 A와 C군 ABTEST데이터만 Summary한다.
	 * @param fLine
	 * @param abtestTypes
	 */
	private void SummaryABAdverStatsWeb(MobonFileDataVO fLine, String abtestTypes) {

		if(!GlobalConstants.PLTFOM_TP_CODE_WEB.equals(fLine.getPltfomTpCode()))
			return;

		if(!abtestTypes.startsWith("AC"))
			return;

		// 오디언스와 카테고리 매칭만
		if(!"26".equals(fLine.getAdvrtsTpCode()) && !"28".equals(fLine.getAdvrtsTpCode()))
			return;

		ABAdverStatsWeb abAdverStatsWeb = abAdverStatsWebMap.getOrDefault(fLine.getABAdverStatsWebKey()+abtestTypes,
				ABAdverStatsWeb.builder().id(ABAdverStatsWebKey.builder()
						.statsDttm(Integer.parseInt(fLine.getStatsDttm()))
						.pltfomTpCode(fLine.getPltfomTpCode())
						.advrtsPrdtCode(fLine.getAdvrtsPrdtCode())
						.advrtsTpCode(fLine.getAdvrtsTpCode())
						.adverId(fLine.getAdverId())
						.abTestTy(abtestTypes)
						.itlTpCode(fLine.getItlTpCode())
						.rcmmYn(fLine.getRcmmYn()).build())
						.totEprsCnt(0)
						.parEprsCnt(0)
						.clickCnt(0)
						.advrtsAmt(BigDecimal.ZERO)
						.mediaPymntAmt(BigDecimal.ZERO)
						.totOrderCnt(0)
						.totOrderAmt(0)
						.sessOrderCnt(0)
						.sessOrderAmt(0)
						.directOrderCnt(0)
						.directOrderAmt(0)
						.regUserId("ABTEST_BATCH")
						.build()
		);

		abAdverStatsWeb.addTotEprsCnt("V".equals(fLine.getAction())?fLine.getTotEprsCnt():0);
		abAdverStatsWeb.addParEprsCnt("V".equals(fLine.getAction())?fLine.getParEprsCnt():0);
		abAdverStatsWeb.addClickCnt("C".equals(fLine.getAction())?fLine.getClickCnt():0);
		abAdverStatsWeb.addAdvrtsAmt(fLine.getAdvrtsAmt());
		abAdverStatsWeb.addMediaPymntAmt(fLine.getMediaPymntAmt());
		abAdverStatsWeb.addTotOrderCnt(fLine.getOrderCnt());
		abAdverStatsWeb.addTotOrderAmt(fLine.getOrderAmt());
		abAdverStatsWeb.addSessOrderCnt("1".equals(fLine.getDirect())?fLine.getOrderCnt():0);
		abAdverStatsWeb.addSessOrderAmt("1".equals(fLine.getDirect())?fLine.getOrderAmt():0);
		abAdverStatsWeb.addDirectOrderCnt("24".equals(fLine.getInHour())?fLine.getOrderCnt():0);
		abAdverStatsWeb.addDirectOrderAmt("24".equals(fLine.getInHour())?fLine.getOrderAmt():0);

		abAdverStatsWebMap.put(fLine.getABAdverStatsWebKey()+abtestTypes, abAdverStatsWeb);
	}

	/**
	 * @Method Name : SummaryABAdverStatsMobile
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : AB_ADVER_STATS_MOBILE 도메인을 기준으로 데이터를 Summary하는 메소드. (모바일)
	 *            광고주별 추천별 전체 데이터를 수집할 경우 너무 많은 ROW가 발생하므로 A와 C군 ABTEST데이터만 Summary한다.
	 * @param fLine
	 * @param abtestTypes
	 */
	private void SummaryABAdverStatsMobile(MobonFileDataVO fLine, String abtestTypes) {

		if(!GlobalConstants.PLTFOM_TP_CODE_MOBILE.equals(fLine.getPltfomTpCode()))
			return;

		// AC군만
		if(!abtestTypes.startsWith("AC"))
			return;

		// 오디언스와 카테고리 매칭만
		if(!"26".equals(fLine.getAdvrtsTpCode()) && !"28".equals(fLine.getAdvrtsTpCode()))
			return;

		ABAdverStatsMobile abAdverStatsMobile = abAdverStatsMobileMap.getOrDefault(fLine.getABAdverStatsMobileKey()+abtestTypes,
				ABAdverStatsMobile.builder().id(ABAdverStatsMobileKey.builder()
						.statsDttm(Integer.parseInt(fLine.getStatsDttm()))
						.pltfomTpCode(fLine.getPltfomTpCode())
						.advrtsPrdtCode(fLine.getAdvrtsPrdtCode())
						.advrtsTpCode(fLine.getAdvrtsTpCode())
						.adverId(fLine.getAdverId())
						.abTestTy(abtestTypes)
						.itlTpCode(fLine.getItlTpCode())
						.rcmmYn(fLine.getRcmmYn()).build())
						.totEprsCnt(0)
						.parEprsCnt(0)
						.clickCnt(0)
						.advrtsAmt(BigDecimal.ZERO)
						.mediaPymntAmt(BigDecimal.ZERO)
						.totOrderCnt(0)
						.totOrderAmt(0)
						.sessOrderCnt(0)
						.sessOrderAmt(0)
						.directOrderCnt(0)
						.directOrderAmt(0)
						.regUserId("ABTEST_BATCH")
						.build()
		);

		abAdverStatsMobile.addTotEprsCnt("V".equals(fLine.getAction())?fLine.getTotEprsCnt():0);
		abAdverStatsMobile.addParEprsCnt("V".equals(fLine.getAction())?fLine.getParEprsCnt():0);
		abAdverStatsMobile.addClickCnt("C".equals(fLine.getAction())?fLine.getClickCnt():0);
		abAdverStatsMobile.addAdvrtsAmt(fLine.getAdvrtsAmt());
		abAdverStatsMobile.addMediaPymntAmt(fLine.getMediaPymntAmt());
		abAdverStatsMobile.addTotOrderCnt(fLine.getOrderCnt());
		abAdverStatsMobile.addTotOrderAmt(fLine.getOrderAmt());
		abAdverStatsMobile.addSessOrderCnt("1".equals(fLine.getDirect())?fLine.getOrderCnt():0);
		abAdverStatsMobile.addSessOrderAmt("1".equals(fLine.getDirect())?fLine.getOrderAmt():0);
		abAdverStatsMobile.addDirectOrderCnt("24".equals(fLine.getInHour())?fLine.getOrderCnt():0);
		abAdverStatsMobile.addDirectOrderAmt("24".equals(fLine.getInHour())?fLine.getOrderAmt():0);

		abAdverStatsMobileMap.put(fLine.getABAdverStatsMobileKey()+abtestTypes, abAdverStatsMobile);
	}

	/**
	 * @Method Name : SummaryABComFrameSize
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : AB_COM_FRAME_SIZE 도메인을 기준으로 데이터를 Summary하는 메소드.
	 * @param fLine
	 * @param abtestTypes
	 */
	private void SummaryABComFrameSize(MobonFileDataVO fLine, String abtestTypes) {

		// frameId가 null이면 return
		if(StringUtils.isEmpty(fLine.getFrameId()))
			return;

		// ABTEST Type이 A인건만.
//		if(!abtestTypes.startsWith("A"))
//			return;

		// 상풉구분(프레임ID에서 2번째)이 Y인 값만.
//		if(!"Y".equals(fLine.getFrameId().substring(1, 2)))
//			return;
//
//		if(!"01".equals(fLine.getPrdtTpCode()))
//			return;
//
//		// 배너구분코드가 W01, W02, W06, W12, M01, M05, M13 인건만.
//		if(!"W01".equals(fLine.getFrameId().substring(10, 13))
//				&& !"W02".equals(fLine.getFrameId().substring(10, 13))
//				&& !"W06".equals(fLine.getFrameId().substring(10, 13))
//				&& !"W12".equals(fLine.getFrameId().substring(10, 13))
//				&& !"M01".equals(fLine.getFrameId().substring(10, 13))
//				&& !"M05".equals(fLine.getFrameId().substring(10, 13))
//				&& !"M13".equals(fLine.getFrameId().substring(10, 13)))
//			return;
//
//		// 수집예외광고주
//		if(GlobalConstants.ABFRAMESIZE_DISCOLLECT_ADVER_ID.indexOf(","+fLine.getAdverId()+",") != -1)
//			return;
//
//		// 수집예외지면
////		if(GlobalConstants.ABFRAMESIZE_DISCOLLECT_MS_NO.indexOf(","+fLine.getMediaScriptNo()+",") != -1)
////			return;
//		if( GlobalConstants.ABFRAMESIZE_DISCOLLECT_MS_NO.contains(fLine.getMediaScriptNo()) )
//			return;
//
//		// 수집예외지면(테스트 지면)
//		if(GlobalConstants.ABFRAMESIZE_DISCOLLECT_TEST_MS_NO.indexOf(","+fLine.getMediaScriptNo()+",") != -1)
//			return;
		
		if ("N".equals(fLine.getFrameCombiTargetYN())) {
			return;
		}
		
		
		ABComFrameSize abComFrameSize = abComFrameSizeMap.getOrDefault(fLine.getABComFrameSizeKey()+abtestTypes,
				ABComFrameSize.builder().id(ABComFrameSizeKey.builder()
						.statsDttm(Integer.parseInt(fLine.getStatsDttm()))
						.pltfomTpCode(fLine.getPltfomTpCode())
						.advrtsPrdtCode(fLine.getAdvrtsPrdtCode())
						.advrtsTpCode(fLine.getAdvrtsTpCode())
						.abTestTy(abtestTypes)
						.itlTpCode(fLine.getItlTpCode())
						.bnrCode(fLine.getFrameId().substring(10, 13))
						.parTpCode(fLine.getParTpCode())
						.frmeType(fLine.getFrameType()).build())
						.totEprsCnt(0)
						.parEprsCnt(0)
						.clickCnt(0)
						.advrtsAmt(BigDecimal.ZERO)
						.mediaPymntAmt(BigDecimal.ZERO)
						.totOrderCnt(0)
						.totOrderAmt(0)
						.sessOrderCnt(0)
						.sessOrderAmt(0)
						.directOrderCnt(0)
						.directOrderAmt(0)
						.regUserId("ABTEST_BATCH")
						.build()
		);

		abComFrameSize.addTotEprsCnt("V".equals(fLine.getAction())?fLine.getTotEprsCnt():0);
		abComFrameSize.addParEprsCnt("V".equals(fLine.getAction())?fLine.getParEprsCnt():0);
		abComFrameSize.addClickCnt("C".equals(fLine.getAction())?fLine.getClickCnt():0);
		abComFrameSize.addAdvrtsAmt(fLine.getAdvrtsAmt());
		abComFrameSize.addMediaPymntAmt(fLine.getMediaPymntAmt());
		abComFrameSize.addTotOrderCnt(fLine.getOrderCnt());
		abComFrameSize.addTotOrderAmt(fLine.getOrderAmt());
		abComFrameSize.addSessOrderCnt("1".equals(fLine.getDirect())?fLine.getOrderCnt():0);
		abComFrameSize.addSessOrderAmt("1".equals(fLine.getDirect())?fLine.getOrderAmt():0);
		abComFrameSize.addDirectOrderCnt("24".equals(fLine.getInHour())?fLine.getOrderCnt():0);
		abComFrameSize.addDirectOrderAmt("24".equals(fLine.getInHour())?fLine.getOrderAmt():0);

		abComFrameSizeMap.put(fLine.getABComFrameSizeKey()+abtestTypes, abComFrameSize);
	}

	/**
	 * @Method Name : SummaryABCombiFrameSize
	 * @Date : 2020. 7. 2.
	 * @Author : dkchoi
	 * @Comment : AB_COMBI_FRAME_SIZE 도메인을 기준으로 데이터를 Summary하는 메소드.
	 * @param fLine
	 * @param abtestTypes
	 */
	private void SummaryABCombiFrameSize(MobonFileDataVO fLine, String abtestTypes) {

		// frameCombiKey가 null이면 return
		if(StringUtils.isEmpty(fLine.getFrameCombiKey()))
			return;

		// ABTEST Type이 A인건만.
		if(!abtestTypes.startsWith("A"))
			return;

		// 수집예외광고주
		if(GlobalConstants.ABFRAMESIZE_DISCOLLECT_ADVER_ID.indexOf(","+fLine.getAdverId()+",") != -1)
			return;

		ABCombiFrameSize abCombiFrameSize = abCombiFrameSizeMap.getOrDefault(fLine.getABCombiFrameSizeKey()+abtestTypes,
				ABCombiFrameSize.builder().id(ABCombiFrameSizeKey.builder()
						.statsDttm(Integer.parseInt(fLine.getStatsDttm()))
						.pltfomTpCode(fLine.getPltfomTpCode())
						.advrtsPrdtCode(fLine.getAdvrtsPrdtCode())
						.advrtsTpCode(fLine.getAdvrtsTpCode())
						.abTestTy(abtestTypes)
						.itlTpCode(fLine.getItlTpCode())
						.bnrCode(fLine.getFrameCombiKey().substring(3, 6))
						.parTpCode(fLine.getParTpCode())
						.frmeType(fLine.getFrameType()).build())
						.totEprsCnt(0)
						.parEprsCnt(0)
						.clickCnt(0)
						.advrtsAmt(BigDecimal.ZERO)
						.mediaPymntAmt(BigDecimal.ZERO)
						.totOrderCnt(0)
						.totOrderAmt(0)
						.sessOrderCnt(0)
						.sessOrderAmt(0)
						.directOrderCnt(0)
						.directOrderAmt(0)
						.regUserId("ABTEST_BATCH")
						.build()
		);

		abCombiFrameSize.addTotEprsCnt("V".equals(fLine.getAction())?fLine.getTotEprsCnt():0);
		abCombiFrameSize.addParEprsCnt("V".equals(fLine.getAction())?fLine.getParEprsCnt():0);
		abCombiFrameSize.addClickCnt("C".equals(fLine.getAction())?fLine.getClickCnt():0);
		abCombiFrameSize.addAdvrtsAmt(fLine.getAdvrtsAmt());
		abCombiFrameSize.addMediaPymntAmt(fLine.getMediaPymntAmt());
		abCombiFrameSize.addTotOrderCnt(fLine.getOrderCnt());
		abCombiFrameSize.addTotOrderAmt(fLine.getOrderAmt());
		abCombiFrameSize.addSessOrderCnt("1".equals(fLine.getDirect())?fLine.getOrderCnt():0);
		abCombiFrameSize.addSessOrderAmt("1".equals(fLine.getDirect())?fLine.getOrderAmt():0);
		abCombiFrameSize.addDirectOrderCnt("24".equals(fLine.getInHour())?fLine.getOrderCnt():0);
		abCombiFrameSize.addDirectOrderAmt("24".equals(fLine.getInHour())?fLine.getOrderAmt():0);

		abCombiFrameSizeMap.put(fLine.getABCombiFrameSizeKey()+abtestTypes, abCombiFrameSize);
	}


	/**
	 * @Method Name : SummaryABFrameSize
	 * @Date : 2021. 1. 4.
	 * @Author : dkchoi
	 * @Comment : AB_FRAME_SIZE 도메인을 기준으로 데이터를 Summary하는 메소드.
	 * @param fLine
	 * @param abtestTypes
	 */
	private void SummaryABFrameSize(MobonFileDataVO fLine, String abtestTypes) {

		// frameId가 null이면 return
		if(StringUtils.isEmpty(fLine.getFrameId()))
			return;
		
		String anumberPattern = "A[0-9]+$"; // A군

		// ABTEST Type이 A, AE, AZ인건만.
		if(!Pattern.matches(anumberPattern, abtestTypes)
				&& !abtestTypes.startsWith("AE")
				&& !abtestTypes.startsWith("AZ"))
			return;

		if(abtestTypes.startsWith("AZ")) {
			if (GlobalConstants.ABFRAMESIZE_DISCOLLECT_MS_NO.contains(fLine.getMediaScriptNo()))
				return;

			// FrameSendTpCode 01,02,03,07 만 적재
			 String [] frameSendTpCodes = GlobalConstants.FRAME_TP_CODE.split("[|]");
			 boolean checkFrameTpCode = true;
			 for (String frameCode : frameSendTpCodes) {
				 if (fLine.getFrameSendTpCode().equals(frameCode)) {
					 checkFrameTpCode = false;
				 }
			 }
			 
			 if (checkFrameTpCode) {
				 return;
			 }

		}
		
		//Frame Size AE 군이면서 frameMatrExposureYN  조건 비교 
		
		 if (abtestTypes.startsWith("AE") &&  "N".equals(fLine.getFrameMatrExposureYN())) {
		 	return;
		 }
		 
		 
		ABFrameSize abFrameSize = abFrameSizeMap.getOrDefault(fLine.getABFrameSizeKey()+abtestTypes,
				ABFrameSize.builder().id(ABFrameSizeKey.builder()
						.statsDttm(Integer.parseInt(fLine.getStatsDttm()))
						.pltfomTpCode(fLine.getPltfomTpCode())
						.advrtsPrdtCode(fLine.getAdvrtsPrdtCode())
						.advrtsTpCode(fLine.getAdvrtsTpCode())
						.abTestTy(abtestTypes)
						.itlTpCode(fLine.getItlTpCode())
						.bnrCode(fLine.getFrameId().substring(10, 13))
						.parTpCode(fLine.getParTpCode())
						.frmeType(fLine.getFrameType())
						.prdtTpCode(fLine.getPrdtTpCode())
						.build())
						.totEprsCnt(0)
						.parEprsCnt(0)
						.clickCnt(0)
						.advrtsAmt(BigDecimal.ZERO)
						.mediaPymntAmt(BigDecimal.ZERO)
						.totOrderCnt(0)
						.totOrderAmt(0)
						.sessOrderCnt(0)
						.sessOrderAmt(0)
						.directOrderCnt(0)
						.directOrderAmt(0)
						.regUserId("ABTEST_BATCH")
						.build()
		);

		abFrameSize.addTotEprsCnt("V".equals(fLine.getAction())?fLine.getTotEprsCnt():0);
		abFrameSize.addParEprsCnt("V".equals(fLine.getAction())?fLine.getParEprsCnt():0);
		abFrameSize.addClickCnt("C".equals(fLine.getAction())?fLine.getClickCnt():0);
		abFrameSize.addAdvrtsAmt(fLine.getAdvrtsAmt());
		abFrameSize.addMediaPymntAmt(fLine.getMediaPymntAmt());
		abFrameSize.addTotOrderCnt(fLine.getOrderCnt());
		abFrameSize.addTotOrderAmt(fLine.getOrderAmt());
		abFrameSize.addSessOrderCnt("1".equals(fLine.getDirect())?fLine.getOrderCnt():0);
		abFrameSize.addSessOrderAmt("1".equals(fLine.getDirect())?fLine.getOrderAmt():0);
		abFrameSize.addDirectOrderCnt("24".equals(fLine.getInHour())?fLine.getOrderCnt():0);
		abFrameSize.addDirectOrderAmt("24".equals(fLine.getInHour())?fLine.getOrderAmt():0);

		abFrameSizeMap.put(fLine.getABFrameSizeKey()+abtestTypes, abFrameSize);
	}


	/**
	 * @Method Name : isCollectionMsNo
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : 집계 예외지면을 처리하는 로직.
	 * @param Fline
	 * @return
	 */
	private boolean isCollectionMsNo(MobonFileDataVO Fline) {
		List<String> disCollectMsNoList =  new ArrayList<String>();
		if(!StringUtils.isEmpty(GlobalConstants.ABTEST_LOG_DISCOLLECT_MS_NO)){
			try {
				disCollectMsNoList.addAll(Arrays.asList(GlobalConstants.ABTEST_LOG_DISCOLLECT_MS_NO.split(",")));
			} catch (Exception e) {}
		}

		return !disCollectMsNoList.contains(Integer.toBinaryString(Fline.getMediaScriptNo()));
	}

	public void putAbComStatsMap(ABComStats abComStatsMap) {
		this.abComStatsMap.put(abComStatsMap.getId().toString(), abComStatsMap);
	}


	public void putAbParStatsMap(ABParStats abParStatsMap) {
		this.abParStatsMap.put(abParStatsMap.getId().toString(), abParStatsMap);
	}


	public void putAbAdverStatsWebMap(ABAdverStatsWeb abAdverStatsWebMap) {
		this.abAdverStatsWebMap.put(abAdverStatsWebMap.getId().toString(), abAdverStatsWebMap);
	}


	public void putAbAdverStatsMobileMap(ABAdverStatsMobile abAdverStatsMobileMap) {
		this.abAdverStatsMobileMap.put(abAdverStatsMobileMap.getId().toString(), abAdverStatsMobileMap);
	}


	public void putAbComFrameSizeMap(ABComFrameSize abComFrameSizeMap) {
		this.abComFrameSizeMap.put(abComFrameSizeMap.getId().toString(), abComFrameSizeMap);
	}

	public void putAbFrameSizeMap(ABFrameSize abFrameSizeMap) {
		this.abFrameSizeMap.put(abFrameSizeMap.getId().toString(), abFrameSizeMap);
	}

	public void putAbCombiFrameSizeMap(ABCombiFrameSize abCombiFrameSizeMap) {
		this.abCombiFrameSizeMap.put(abCombiFrameSizeMap.getId().toString(), abCombiFrameSizeMap);
	}

}
