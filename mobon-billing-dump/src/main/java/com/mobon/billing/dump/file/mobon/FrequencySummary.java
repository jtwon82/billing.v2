package com.mobon.billing.dump.file.mobon;

import com.google.gson.Gson;
import com.mobon.billing.dump.constants.GlobalConstants;
import com.mobon.billing.dump.domainmodel.frequency.FreqCampDayStats;
import com.mobon.billing.dump.domainmodel.frequency.FreqDayStats;
import com.mobon.billing.dump.domainmodel.frequency.FreqMediaScriptDayStats;
import com.mobon.billing.dump.domainmodel.frequency.FreqSdkDayStats;
import com.mobon.billing.dump.domainmodel.frequency.key.FreqCampDayStatsKey;
import com.mobon.billing.dump.domainmodel.frequency.key.FreqDayStatsKey;
import com.mobon.billing.dump.domainmodel.frequency.key.FreqMediaScriptDayStatsKey;
import com.mobon.billing.dump.domainmodel.frequency.key.FreqSdkDayStatsKey;
import com.mobon.billing.dump.file.mobon.vo.FrequencyDataVO;
import com.mobon.billing.dump.file.mobon.vo.MobonFileDataVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @FileName : FrequencySummary.java
 * @Project : mobon-billing-dump
 * @Date : 2021. 3. 30.
 * @Author dkchoi
 * @Comment : 전달된 파일 라인을 도메인별로 구분하여 데이터를 Summary하는 클래스.
 */

@Slf4j
public class FrequencySummary extends DataFilter {

	private Map<String, FrequencyDataVO> frequencyDataVOSdkDayMap = new HashMap<>();
	private Map<String, FrequencyDataVO> frequencyDataVODayMap = new HashMap<>();
	private Map<String, FrequencyDataVO> frequencyDataVOCampMap = new HashMap<>();
	private Map<String, FrequencyDataVO> frequencyDataVOMediaScriptMap = new HashMap<>();

	private Map<String, FreqDayStats> freqDayStatsMap = new HashMap<String, FreqDayStats>();
	private Map<String, FreqSdkDayStats> freqSdkDayStatsMap = new HashMap<String, FreqSdkDayStats>();
	private Map<String, FreqCampDayStats> freqCampDayStatsMap = new HashMap<String, FreqCampDayStats>();
	private Map<String, FreqMediaScriptDayStats> freqMediaScriptDayStatsMap = new HashMap<String, FreqMediaScriptDayStats>();

	/**
	 * @Method Name : getFrequencyData
	 * @Date : 2020. 08. 20.
	 * @Author : dkchoi
	 * @Comment : Summary된 데이터를 return 받는다.
	 * @param
	 * @return totSumDataList
	 */
	public Map<String, Object> getFrequencyData() {
		Map<String, Object> totSumDataList = new HashMap<String, Object>();

		totSumDataList.put(GlobalConstants.FREQDAYSTATS_SUMMARY_RESULT, freqDayStatsMap);
		totSumDataList.put(GlobalConstants.FREQSDKDAYSTATS_SUMMARY_RESULT, freqSdkDayStatsMap);
		totSumDataList.put(GlobalConstants.FREQCAMPDAYSTATS_SUMMARY_RESULT, freqCampDayStatsMap);
		totSumDataList.put(GlobalConstants.FREQMEDIASCRIPTDAYSTATS_SUMMARY_RESULT, freqMediaScriptDayStatsMap);

		return totSumDataList;
	}

	/**
	 * @Method Name : setPreProFrequencyData
	 * @Date : 2021. 4. 1.
	 * @Author : dkchoi
	 * @Comment : 파일 라인을 JSON 객체로 변환하기 전에 1차 Summary한다.
	 * @param fLine
	 * @return
	 */
	public void setPreProFrequencyData(MobonFileDataVO fLine) {

		if(!StringUtils.isEmpty(fLine.getStatsDttm())) {

			if(!adverIDLengthCheck(fLine)) { // 광고주 ID길이가 테이블 필드 길이보다 길 경우 return;
				log.error("AdverID Error , Values = [" + fLine.getAdverId() + "]");
				return;
			}

			if(noCountFrequency(fLine)) // 비상품이면서 구좌노출이 0인 데이터 프리퀀시 카운트 하지않음. (채우기)
				return;

			setPreProFrequencyDayData(fLine);
			setPreProFrequencySdkDayData(fLine);
			setPreProFrequencyCampData(fLine);
			setPreProFrequencyMediaScriptData(fLine);

		}

	}

	/**
	 * @Method Name : setPreProFrequencyDayData
	 * @Date : 2021. 4. 1.
	 * @Author : dkchoi
	 * @Comment : 파일 라인을 JSON 객체로 변환하기 전에 FREQ_DAY_STATS 기준으로 1차 Summary한다.
	 * @param fLine
	 * @return
	 */
	public void setPreProFrequencyDayData(MobonFileDataVO fLine) {

		FrequencyDataVO frequencyDataVO = frequencyDataVODayMap.getOrDefault(fLine.getFrequencyPreProDaySumKey(),
				FrequencyDataVO.builder()
						.statsDttm(fLine.getStatsDttm())
						.pltfomTpCode(fLine.getPltfomTpCode())
						.advrtsPrdtCode(fLine.getAdvrtsPrdtCode())
						.advrtsTpCode(fLine.getAdvrtsTpCode())
						.totEprsCntMap(new HashMap<>())
						.parEprsCntMap(new HashMap<>())
						.clickCntMap(new HashMap<>())
						.advrtsAmtMap(new HashMap<>())
						.mediaPymntAmtMap(new HashMap<>())
						.totOrderCntMap(new HashMap<>())
						.totOrderAmtMap(new HashMap<>())
						.sessOrderCntMap(new HashMap<>())
						.sessOrderAmtMap(new HashMap<>())
						.directOrderCntMap(new HashMap<>())
						.directOrderAmtMap(new HashMap<>())
						.build()
		);

		frequencyDataVO.addTotEprsCnt(fLine.getFreq(), "V".equals(fLine.getAction())?fLine.getTotEprsCnt():0);
		frequencyDataVO.addParEprsCnt(fLine.getFreq(), "V".equals(fLine.getAction())?fLine.getParEprsCnt():0);
		frequencyDataVO.addClickCnt(fLine.getFreq(), "C".equals(fLine.getAction())?fLine.getClickCnt():0);
		frequencyDataVO.addAdvrtsAmt(fLine.getFreq(), fLine.getAdvrtsAmt());
		frequencyDataVO.addMediaPymntAmt(fLine.getFreq(), fLine.getMediaPymntAmt());
		frequencyDataVO.addTotOrderCnt(fLine.getFreq(), fLine.getOrderCnt());
		frequencyDataVO.addTotOrderAmt(fLine.getFreq(), fLine.getOrderAmt());
		frequencyDataVO.addSessOrderCnt(fLine.getFreq(), "1".equals(fLine.getDirect())?fLine.getOrderCnt():0);
		frequencyDataVO.addSessOrderAmt(fLine.getFreq(), "1".equals(fLine.getDirect())?fLine.getOrderAmt():0);
		frequencyDataVO.addDirectOrderCnt(fLine.getFreq(), "24".equals(fLine.getInHour())?fLine.getOrderCnt():0);
		frequencyDataVO.addDirectOrderAmt(fLine.getFreq(), "24".equals(fLine.getInHour())?fLine.getOrderAmt():0);

		frequencyDataVODayMap.put(fLine.getFrequencyPreProDaySumKey(), frequencyDataVO);
	}

	/**
	 * @Method Name : setPreProFrequencySdkDayData
	 * @Date : 2021. 4. 29.
	 * @Author : dkchoi
	 * @Comment : 파일 라인을 JSON 객체로 변환하기 전에 FREQ_SDK_DAY_STATS 기준으로 1차 Summary한다.
	 * @param fLine
	 * @return
	 */
	public void setPreProFrequencySdkDayData(MobonFileDataVO fLine) {

		if( !GlobalConstants.FREQSDKDAYSTATS_SDK_MS_NO.contains(fLine.getMediaScriptNo()) )
			return;

		FrequencyDataVO frequencyDataVO = frequencyDataVOSdkDayMap.getOrDefault(fLine.getFrequencyPreProSdkDaySumKey(),
				FrequencyDataVO.builder()
						.statsDttm(fLine.getStatsDttm())
						.advrtsPrdtCode(fLine.getAdvrtsPrdtCode())
						.advrtsTpCode(fLine.getAdvrtsTpCode())
						.totEprsCntMap(new HashMap<>())
						.parEprsCntMap(new HashMap<>())
						.clickCntMap(new HashMap<>())
						.advrtsAmtMap(new HashMap<>())
						.mediaPymntAmtMap(new HashMap<>())
						.totOrderCntMap(new HashMap<>())
						.totOrderAmtMap(new HashMap<>())
						.sessOrderCntMap(new HashMap<>())
						.sessOrderAmtMap(new HashMap<>())
						.directOrderCntMap(new HashMap<>())
						.directOrderAmtMap(new HashMap<>())
						.build()
		);

		frequencyDataVO.addTotEprsCnt(fLine.getFreq(), "V".equals(fLine.getAction())?fLine.getTotEprsCnt():0);
		frequencyDataVO.addParEprsCnt(fLine.getFreq(), "V".equals(fLine.getAction())?fLine.getParEprsCnt():0);
		frequencyDataVO.addClickCnt(fLine.getFreq(), "C".equals(fLine.getAction())?fLine.getClickCnt():0);
		frequencyDataVO.addAdvrtsAmt(fLine.getFreq(), fLine.getAdvrtsAmt());
		frequencyDataVO.addMediaPymntAmt(fLine.getFreq(), fLine.getMediaPymntAmt());
		frequencyDataVO.addTotOrderCnt(fLine.getFreq(), fLine.getOrderCnt());
		frequencyDataVO.addTotOrderAmt(fLine.getFreq(), fLine.getOrderAmt());
		frequencyDataVO.addSessOrderCnt(fLine.getFreq(), "1".equals(fLine.getDirect())?fLine.getOrderCnt():0);
		frequencyDataVO.addSessOrderAmt(fLine.getFreq(), "1".equals(fLine.getDirect())?fLine.getOrderAmt():0);
		frequencyDataVO.addDirectOrderCnt(fLine.getFreq(), "24".equals(fLine.getInHour())?fLine.getOrderCnt():0);
		frequencyDataVO.addDirectOrderAmt(fLine.getFreq(), "24".equals(fLine.getInHour())?fLine.getOrderAmt():0);

		frequencyDataVOSdkDayMap.put(fLine.getFrequencyPreProSdkDaySumKey(), frequencyDataVO);
	}
	/**
	 * @Method Name : setPreProFrequencyCampData
	 * @Date : 2021. 4. 1.
	 * @Author : dkchoi
	 * @Comment : 파일 라인을 JSON 객체로 변환하기 전에 FREQ_CAMP_DAY_STATS 기준으로 1차 Summary한다.
	 * @param fLine
	 * @return
	 */
	public void setPreProFrequencyCampData(MobonFileDataVO fLine) {

		FrequencyDataVO frequencyDataVO = frequencyDataVOCampMap.getOrDefault(fLine.getFrequencyPreProCampSumKey(),
				FrequencyDataVO.builder()
						.statsDttm(fLine.getStatsDttm())
						.pltfomTpCode(fLine.getPltfomTpCode())
						.advrtsPrdtCode(fLine.getAdvrtsPrdtCode())
						.advrtsTpCode(fLine.getAdvrtsTpCode())
						.siteCode(fLine.getSiteCode())
						.totEprsCntMap(new HashMap<>())
						.parEprsCntMap(new HashMap<>())
						.clickCntMap(new HashMap<>())
						.advrtsAmtMap(new HashMap<>())
						.mediaPymntAmtMap(new HashMap<>())
						.totOrderCntMap(new HashMap<>())
						.totOrderAmtMap(new HashMap<>())
						.sessOrderCntMap(new HashMap<>())
						.sessOrderAmtMap(new HashMap<>())
						.directOrderCntMap(new HashMap<>())
						.directOrderAmtMap(new HashMap<>())
						.build()
		);

		frequencyDataVO.addTotEprsCnt(fLine.getFreq(), "V".equals(fLine.getAction())?fLine.getTotEprsCnt():0);
		frequencyDataVO.addParEprsCnt(fLine.getFreq(), "V".equals(fLine.getAction())?fLine.getParEprsCnt():0);
		frequencyDataVO.addClickCnt(fLine.getFreq(), "C".equals(fLine.getAction())?fLine.getClickCnt():0);
		frequencyDataVO.addAdvrtsAmt(fLine.getFreq(), fLine.getAdvrtsAmt());
		frequencyDataVO.addMediaPymntAmt(fLine.getFreq(), fLine.getMediaPymntAmt());
		frequencyDataVO.addTotOrderCnt(fLine.getFreq(), fLine.getOrderCnt());
		frequencyDataVO.addTotOrderAmt(fLine.getFreq(), fLine.getOrderAmt());
		frequencyDataVO.addSessOrderCnt(fLine.getFreq(), "1".equals(fLine.getDirect())?fLine.getOrderCnt():0);
		frequencyDataVO.addSessOrderAmt(fLine.getFreq(), "1".equals(fLine.getDirect())?fLine.getOrderAmt():0);
		frequencyDataVO.addDirectOrderCnt(fLine.getFreq(), "24".equals(fLine.getInHour())?fLine.getOrderCnt():0);
		frequencyDataVO.addDirectOrderAmt(fLine.getFreq(), "24".equals(fLine.getInHour())?fLine.getOrderAmt():0);

		frequencyDataVOCampMap.put(fLine.getFrequencyPreProCampSumKey(), frequencyDataVO);
	}

	/**
	 * @Method Name : setPreProFrequencyMediaScriptData
	 * @Date : 2021. 4. 1.
	 * @Author : dkchoi
	 * @Comment : 파일 라인을 JSON 객체로 변환하기 전에 FREQ_MEDIA_SCRIPT_DAY_STATS 기준으로 1차 Summary한다.
	 * @param fLine
	 * @return
	 */
	public void setPreProFrequencyMediaScriptData(MobonFileDataVO fLine) {

		FrequencyDataVO frequencyDataVO = frequencyDataVOMediaScriptMap.getOrDefault(fLine.getFrequencyPreProSumMediaScriptKey(),
				FrequencyDataVO.builder()
						.statsDttm(fLine.getStatsDttm())
						.pltfomTpCode(fLine.getPltfomTpCode())
						.advrtsPrdtCode(fLine.getAdvrtsPrdtCode())
						.advrtsTpCode(fLine.getAdvrtsTpCode())
						.mediaScriptNo(fLine.getMediaScriptNo())
						.itlTpCode(fLine.getItlTpCode())
						.totEprsCntMap(new HashMap<>())
						.parEprsCntMap(new HashMap<>())
						.clickCntMap(new HashMap<>())
						.advrtsAmtMap(new HashMap<>())
						.mediaPymntAmtMap(new HashMap<>())
						.totOrderCntMap(new HashMap<>())
						.totOrderAmtMap(new HashMap<>())
						.sessOrderCntMap(new HashMap<>())
						.sessOrderAmtMap(new HashMap<>())
						.directOrderCntMap(new HashMap<>())
						.directOrderAmtMap(new HashMap<>())
						.build()
		);

		frequencyDataVO.addTotEprsCnt(fLine.getFreq(), "V".equals(fLine.getAction())?fLine.getTotEprsCnt():0);
		frequencyDataVO.addParEprsCnt(fLine.getFreq(), "V".equals(fLine.getAction())?fLine.getParEprsCnt():0);
		frequencyDataVO.addClickCnt(fLine.getFreq(), "C".equals(fLine.getAction())?fLine.getClickCnt():0);
		frequencyDataVO.addAdvrtsAmt(fLine.getFreq(), fLine.getAdvrtsAmt());
		frequencyDataVO.addMediaPymntAmt(fLine.getFreq(), fLine.getMediaPymntAmt());
		frequencyDataVO.addTotOrderCnt(fLine.getFreq(), fLine.getOrderCnt());
		frequencyDataVO.addTotOrderAmt(fLine.getFreq(), fLine.getOrderAmt());
		frequencyDataVO.addSessOrderCnt(fLine.getFreq(), "1".equals(fLine.getDirect())?fLine.getOrderCnt():0);
		frequencyDataVO.addSessOrderAmt(fLine.getFreq(), "1".equals(fLine.getDirect())?fLine.getOrderAmt():0);
		frequencyDataVO.addDirectOrderCnt(fLine.getFreq(), "24".equals(fLine.getInHour())?fLine.getOrderCnt():0);
		frequencyDataVO.addDirectOrderAmt(fLine.getFreq(), "24".equals(fLine.getInHour())?fLine.getOrderAmt():0);

		frequencyDataVOMediaScriptMap.put(fLine.getFrequencyPreProSumMediaScriptKey(), frequencyDataVO);
	}



	/**
	 * @Method Name : setFrequencyData
	 * @Date : 2021. 4. 1.
	 * @Author : dkchoi
	 * @Comment : 1차 Summary 된 데이터를 도메인별로 다시 Summary한다.
	 * @param
	 * @return
	 */
	public void setFrequencyData() {

		frequencyDataVODayMap.forEach((sumKey , sumValue) -> SummaryFreqDayStats(sumValue));
		frequencyDataVOSdkDayMap.forEach((sumKey , sumValue) -> SummaryFreqSdkDayStats(sumValue));
		frequencyDataVOCampMap.forEach((sumKey , sumValue) -> SummaryFreqCampDayStats(sumValue));
		frequencyDataVOMediaScriptMap.forEach((sumKey , sumValue) -> SummaryFreqMediaScriptDayStats(sumValue));
	}

	/**
	 * @Method Name : SummaryFreqDayStats
	 * @Date : 2021. 3. 30.
	 * @Author : dkchoi
	 * @Comment : FREQ_DAY_STATS 도메인을 기준으로 데이터를 Summary하는 메소드.
	 * @param lineSummary
	 */
	private void SummaryFreqDayStats(FrequencyDataVO lineSummary) {

		FreqDayStats freqDayStats = freqDayStatsMap.getOrDefault(lineSummary.getFreqDayStatsKey(),
				FreqDayStats.builder().id(FreqDayStatsKey.builder()
						.statsDttm(Integer.parseInt(lineSummary.getStatsDttm()))
						.pltfomTpCode(lineSummary.getPltfomTpCode())
						.advrtsPrdtCode(lineSummary.getAdvrtsPrdtCode())
						.advrtsTpCode(lineSummary.getAdvrtsTpCode()).build())
						.totEprsCntFreq("{}")
						.parEprsCntFreq("{}")
						.clickCntFreq("{}")
						.advrtsAmtFreq("{}")
						.mediaPymntAmtFreq("{}")
						.totOrderCntFreq("{}")
						.totOrderAmtFreq("{}")
						.sessOrderCntFreq("{}")
						.sessOrderAmtFreq("{}")
						.directOrderCntFreq("{}")
						.directOrderAmtFreq("{}")
						.regUserId("FREQUENCY_BATCH")
						.build()
		);

		if(MapUtils.isNotEmpty(lineSummary.getTotEprsCntMap())) {
			Gson gson = new Gson();
			freqDayStats.setTotEprsCntFreq(gson.toJson(lineSummary.getTotEprsCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getParEprsCntMap())) {
			Gson gson = new Gson();
			freqDayStats.setParEprsCntFreq(gson.toJson(lineSummary.getParEprsCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getClickCntMap())) {
			Gson gson = new Gson();
			freqDayStats.setClickCntFreq(gson.toJson(lineSummary.getClickCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getAdvrtsAmtMap())) {
			Gson gson = new Gson();
			freqDayStats.setAdvrtsAmtFreq(gson.toJson(lineSummary.getAdvrtsAmtMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getMediaPymntAmtMap())) {
			Gson gson = new Gson();
			freqDayStats.setMediaPymntAmtFreq(gson.toJson(lineSummary.getMediaPymntAmtMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getTotOrderCntMap())) {
			Gson gson = new Gson();
			freqDayStats.setTotOrderCntFreq(gson.toJson(lineSummary.getTotOrderCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getSessOrderCntMap())) {
			Gson gson = new Gson();
			freqDayStats.setSessOrderCntFreq(gson.toJson(lineSummary.getSessOrderCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getDirectOrderCntMap())) {
			Gson gson = new Gson();
			freqDayStats.setDirectOrderCntFreq(gson.toJson(lineSummary.getDirectOrderCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getTotOrderAmtMap())) {
			Gson gson = new Gson();
			freqDayStats.setTotOrderAmtFreq(gson.toJson(lineSummary.getTotOrderAmtMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getSessOrderAmtMap())) {
			Gson gson = new Gson();
			freqDayStats.setSessOrderAmtFreq(gson.toJson(lineSummary.getSessOrderAmtMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getDirectOrderAmtMap())) {
			Gson gson = new Gson();
			freqDayStats.setDirectOrderAmtFreq(gson.toJson(lineSummary.getDirectOrderAmtMap()));
		}

		freqDayStatsMap.put(lineSummary.getFreqDayStatsKey(), freqDayStats);
	}

	/**
	 * @Method Name : SummaryFreqSdkDayStats
	 * @Date : 2021. 4. 29.
	 * @Author : dkchoi
	 * @Comment : FREQ_SDK_DAY_STATS 도메인을 기준으로 데이터를 Summary하는 메소드.
	 * @param lineSummary
	 */
	private void SummaryFreqSdkDayStats(FrequencyDataVO lineSummary) {

		FreqSdkDayStats freqSdkDayStats = freqSdkDayStatsMap.getOrDefault(lineSummary.getFreqSdkDayStatsKey(),
				FreqSdkDayStats.builder().id(FreqSdkDayStatsKey.builder()
						.statsDttm(Integer.parseInt(lineSummary.getStatsDttm()))
						.advrtsPrdtCode(lineSummary.getAdvrtsPrdtCode())
						.advrtsTpCode(lineSummary.getAdvrtsTpCode()).build())
						.totEprsCntFreq("{}")
						.parEprsCntFreq("{}")
						.clickCntFreq("{}")
						.advrtsAmtFreq("{}")
						.mediaPymntAmtFreq("{}")
						.totOrderCntFreq("{}")
						.totOrderAmtFreq("{}")
						.sessOrderCntFreq("{}")
						.sessOrderAmtFreq("{}")
						.directOrderCntFreq("{}")
						.directOrderAmtFreq("{}")
						.regUserId("FREQUENCY_BATCH")
						.build()
		);

		if(MapUtils.isNotEmpty(lineSummary.getTotEprsCntMap())) {
			Gson gson = new Gson();
			freqSdkDayStats.setTotEprsCntFreq(gson.toJson(lineSummary.getTotEprsCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getParEprsCntMap())) {
			Gson gson = new Gson();
			freqSdkDayStats.setParEprsCntFreq(gson.toJson(lineSummary.getParEprsCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getClickCntMap())) {
			Gson gson = new Gson();
			freqSdkDayStats.setClickCntFreq(gson.toJson(lineSummary.getClickCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getAdvrtsAmtMap())) {
			Gson gson = new Gson();
			freqSdkDayStats.setAdvrtsAmtFreq(gson.toJson(lineSummary.getAdvrtsAmtMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getMediaPymntAmtMap())) {
			Gson gson = new Gson();
			freqSdkDayStats.setMediaPymntAmtFreq(gson.toJson(lineSummary.getMediaPymntAmtMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getTotOrderCntMap())) {
			Gson gson = new Gson();
			freqSdkDayStats.setTotOrderCntFreq(gson.toJson(lineSummary.getTotOrderCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getSessOrderCntMap())) {
			Gson gson = new Gson();
			freqSdkDayStats.setSessOrderCntFreq(gson.toJson(lineSummary.getSessOrderCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getDirectOrderCntMap())) {
			Gson gson = new Gson();
			freqSdkDayStats.setDirectOrderCntFreq(gson.toJson(lineSummary.getDirectOrderCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getTotOrderAmtMap())) {
			Gson gson = new Gson();
			freqSdkDayStats.setTotOrderAmtFreq(gson.toJson(lineSummary.getTotOrderAmtMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getSessOrderAmtMap())) {
			Gson gson = new Gson();
			freqSdkDayStats.setSessOrderAmtFreq(gson.toJson(lineSummary.getSessOrderAmtMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getDirectOrderAmtMap())) {
			Gson gson = new Gson();
			freqSdkDayStats.setDirectOrderAmtFreq(gson.toJson(lineSummary.getDirectOrderAmtMap()));
		}

		freqSdkDayStatsMap.put(lineSummary.getFreqSdkDayStatsKey(), freqSdkDayStats);
	}

	/**
	 * @Method Name : SummaryFreqCampDayStats
	 * @Date : 2021. 3. 30.
	 * @Author : dkchoi
	 * @Comment : FREQ_CAMP_DAY_STATS 도메인을 기준으로 데이터를 Summary하는 메소드.
	 * @param lineSummary
	 */
	private void SummaryFreqCampDayStats(FrequencyDataVO lineSummary) {

		FreqCampDayStats freqCampDayStats = freqCampDayStatsMap.getOrDefault(lineSummary.getFreqCampDayStatsKey(),
				FreqCampDayStats.builder().id(FreqCampDayStatsKey.builder()
						.statsDttm(Integer.parseInt(lineSummary.getStatsDttm()))
						.siteCode(lineSummary.getSiteCode())
						.pltfomTpCode(lineSummary.getPltfomTpCode())
						.advrtsPrdtCode(lineSummary.getAdvrtsPrdtCode())
						.advrtsTpCode(lineSummary.getAdvrtsTpCode()).build())
						.totEprsCntFreq("{}")
						.parEprsCntFreq("{}")
						.clickCntFreq("{}")
						.advrtsAmtFreq("{}")
						.mediaPymntAmtFreq("{}")
						.totOrderCntFreq("{}")
						.totOrderAmtFreq("{}")
						.sessOrderCntFreq("{}")
						.sessOrderAmtFreq("{}")
						.directOrderCntFreq("{}")
						.directOrderAmtFreq("{}")
						.regUserId("FREQUENCY_BATCH")
						.build()
		);

		if(MapUtils.isNotEmpty(lineSummary.getTotEprsCntMap())) {
			Gson gson = new Gson();
			freqCampDayStats.setTotEprsCntFreq(gson.toJson(lineSummary.getTotEprsCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getParEprsCntMap())) {
			Gson gson = new Gson();
			freqCampDayStats.setParEprsCntFreq(gson.toJson(lineSummary.getParEprsCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getClickCntMap())) {
			Gson gson = new Gson();
			freqCampDayStats.setClickCntFreq(gson.toJson(lineSummary.getClickCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getAdvrtsAmtMap())) {
			Gson gson = new Gson();
			freqCampDayStats.setAdvrtsAmtFreq(gson.toJson(lineSummary.getAdvrtsAmtMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getMediaPymntAmtMap())) {
			Gson gson = new Gson();
			freqCampDayStats.setMediaPymntAmtFreq(gson.toJson(lineSummary.getMediaPymntAmtMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getTotOrderCntMap())) {
			Gson gson = new Gson();
			freqCampDayStats.setTotOrderCntFreq(gson.toJson(lineSummary.getTotOrderCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getSessOrderCntMap())) {
			Gson gson = new Gson();
			freqCampDayStats.setSessOrderCntFreq(gson.toJson(lineSummary.getSessOrderCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getDirectOrderCntMap())) {
			Gson gson = new Gson();
			freqCampDayStats.setDirectOrderCntFreq(gson.toJson(lineSummary.getDirectOrderCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getTotOrderAmtMap())) {
			Gson gson = new Gson();
			freqCampDayStats.setTotOrderAmtFreq(gson.toJson(lineSummary.getTotOrderAmtMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getSessOrderAmtMap())) {
			Gson gson = new Gson();
			freqCampDayStats.setSessOrderAmtFreq(gson.toJson(lineSummary.getSessOrderAmtMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getDirectOrderAmtMap())) {
			Gson gson = new Gson();
			freqCampDayStats.setDirectOrderAmtFreq(gson.toJson(lineSummary.getDirectOrderAmtMap()));
		}

		freqCampDayStatsMap.put(lineSummary.getFreqCampDayStatsKey(), freqCampDayStats);
	}

	/**
	 * @Method Name : SummaryFreqMediaScriptDayStats
	 * @Date : 2021. 3. 30.
	 * @Author : dkchoi
	 * @Comment : FREQ_MEDIA_SCRIPT_DAY_STATS 도메인을 기준으로 데이터를 Summary하는 메소드.
	 * @param lineSummary
	 */
	private void SummaryFreqMediaScriptDayStats(FrequencyDataVO lineSummary) {

		FreqMediaScriptDayStats freqMediaScriptDayStats = freqMediaScriptDayStatsMap.getOrDefault(lineSummary.getFreqMediaScriptDayStatsKey(),
				FreqMediaScriptDayStats.builder().id(FreqMediaScriptDayStatsKey.builder()
						.statsDttm(Integer.parseInt(lineSummary.getStatsDttm()))
						.mediaScriptNo(lineSummary.getMediaScriptNo())
						.itlTpCode(lineSummary.getItlTpCode())
						.pltfomTpCode(lineSummary.getPltfomTpCode())
						.advrtsPrdtCode(lineSummary.getAdvrtsPrdtCode())
						.advrtsTpCode(lineSummary.getAdvrtsTpCode()).build())
						.totEprsCntFreq("{}")
						.parEprsCntFreq("{}")
						.clickCntFreq("{}")
						.advrtsAmtFreq("{}")
						.mediaPymntAmtFreq("{}")
						.totOrderCntFreq("{}")
						.totOrderAmtFreq("{}")
						.sessOrderCntFreq("{}")
						.sessOrderAmtFreq("{}")
						.directOrderCntFreq("{}")
						.directOrderAmtFreq("{}")
						.regUserId("FREQUENCY_BATCH")
						.build()
		);


		if(MapUtils.isNotEmpty(lineSummary.getTotEprsCntMap())) {
			Gson gson = new Gson();
			freqMediaScriptDayStats.setTotEprsCntFreq(gson.toJson(lineSummary.getTotEprsCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getParEprsCntMap())) {
			Gson gson = new Gson();
			freqMediaScriptDayStats.setParEprsCntFreq(gson.toJson(lineSummary.getParEprsCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getClickCntMap())) {
			Gson gson = new Gson();
			freqMediaScriptDayStats.setClickCntFreq(gson.toJson(lineSummary.getClickCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getAdvrtsAmtMap())) {
			Gson gson = new Gson();
			freqMediaScriptDayStats.setAdvrtsAmtFreq(gson.toJson(lineSummary.getAdvrtsAmtMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getMediaPymntAmtMap())) {
			Gson gson = new Gson();
			freqMediaScriptDayStats.setMediaPymntAmtFreq(gson.toJson(lineSummary.getMediaPymntAmtMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getTotOrderCntMap())) {
			Gson gson = new Gson();
			freqMediaScriptDayStats.setTotOrderCntFreq(gson.toJson(lineSummary.getTotOrderCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getSessOrderCntMap())) {
			Gson gson = new Gson();
			freqMediaScriptDayStats.setSessOrderCntFreq(gson.toJson(lineSummary.getSessOrderCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getDirectOrderCntMap())) {
			Gson gson = new Gson();
			freqMediaScriptDayStats.setDirectOrderCntFreq(gson.toJson(lineSummary.getDirectOrderCntMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getTotOrderAmtMap())) {
			Gson gson = new Gson();
			freqMediaScriptDayStats.setTotOrderAmtFreq(gson.toJson(lineSummary.getTotOrderAmtMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getSessOrderAmtMap())) {
			Gson gson = new Gson();
			freqMediaScriptDayStats.setSessOrderAmtFreq(gson.toJson(lineSummary.getSessOrderAmtMap()));
		}
		if(MapUtils.isNotEmpty(lineSummary.getDirectOrderAmtMap())) {
			Gson gson = new Gson();
			freqMediaScriptDayStats.setDirectOrderAmtFreq(gson.toJson(lineSummary.getDirectOrderAmtMap()));
		}

		freqMediaScriptDayStatsMap.put(lineSummary.getFreqMediaScriptDayStatsKey(), freqMediaScriptDayStats);
	}

	public void putFreqDayStatsMap(FreqDayStats freqDayStatsMap) {
		this.freqDayStatsMap.put( freqDayStatsMap.getId().toString(),  freqDayStatsMap);
	}

	public void putFreqSdkDayStatsMap(FreqSdkDayStats freqSdkDayStatsMap) {
		this.freqSdkDayStatsMap.put( freqSdkDayStatsMap.getId().toString(),  freqSdkDayStatsMap);
	}

	public void putFreqCampDayStatsMap(FreqCampDayStats freqCampDayStatsMap) {
		this.freqCampDayStatsMap.put( freqCampDayStatsMap.getId().toString(),  freqCampDayStatsMap);
	}

	public void putFreqMediaScriptDayStatsMap(FreqMediaScriptDayStats freqMediaScriptDayStatsMap) {
		this.freqMediaScriptDayStatsMap.put( freqMediaScriptDayStatsMap.getId().toString(),  freqMediaScriptDayStatsMap);
	}

	public void getFrequencyDataVOMapSize(){
		log.info("@ frequencyDataVODayMap Size = " +  frequencyDataVODayMap.size());
		log.info("@ frequencyDataVOSdkDayMap Size = " +  frequencyDataVOSdkDayMap.size());
		log.info("@ frequencyDataVOCampMap Size = " +  frequencyDataVOCampMap.size());
		log.info("@ frequencyDataVOMediaScriptMap Size = " +  frequencyDataVOMediaScriptMap.size());
	}
}
