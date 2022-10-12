package com.mobon.billing.dump.service.frequencyimpl;

import com.mobon.billing.dump.constants.GlobalConstants;
import com.mobon.billing.dump.domainmodel.frequency.FreqCampDayStats;
import com.mobon.billing.dump.domainmodel.frequency.FreqDayStats;
import com.mobon.billing.dump.domainmodel.frequency.FreqMediaScriptDayStats;
import com.mobon.billing.dump.domainmodel.frequency.FreqSdkDayStats;
import com.mobon.billing.dump.repository.FreqCampDayStatsRepository;
import com.mobon.billing.dump.repository.FreqDayStatsRepository;
import com.mobon.billing.dump.repository.FreqMediaScriptDayStatsRepository;
import com.mobon.billing.dump.repository.FreqSdkDayStatsRepository;
import com.mobon.billing.dump.service.DumpSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * @FileName : FreqSaveServiceimpl.java
 * @Project : mobon-billing-dump
 * @Date : 2021. 3. 22.
 * @Author dkchoi
 * @Commnet : Summary된 Frequency파일 데이터를 DB에 등록하는 서비스.
 */
@Slf4j
@Service("freqSaveService")
public class FreqSaveServiceimpl implements DumpSaveService {

	@Autowired
	FreqDayStatsRepository freqDayStatsRepository;

	@Autowired
	FreqSdkDayStatsRepository freqSdkDayStatsRepository;

	@Autowired
	FreqCampDayStatsRepository freqCampDayStatsRepository;

	@Autowired
	FreqMediaScriptDayStatsRepository freqMediaScriptDayStatsRepository;


	/* (non-Javadoc)
	 * @see com.mobon.billing.dump.service.DumpSaveService@SaveDumpData(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public void SaveDumpData(Map<String, Object> totResultData) {

		if(!ObjectUtils.isEmpty(totResultData.get(GlobalConstants.FREQDAYSTATS_SUMMARY_RESULT))) {
			SaveFreqDayStats((Map<String, FreqDayStats>) totResultData.get(GlobalConstants.FREQDAYSTATS_SUMMARY_RESULT));
		}
		if(!ObjectUtils.isEmpty(totResultData.get(GlobalConstants.FREQSDKDAYSTATS_SUMMARY_RESULT))) {
			SaveFreqSdkDayStats((Map<String, FreqSdkDayStats>) totResultData.get(GlobalConstants.FREQSDKDAYSTATS_SUMMARY_RESULT));
		}
		if(!ObjectUtils.isEmpty(totResultData.get(GlobalConstants.FREQCAMPDAYSTATS_SUMMARY_RESULT))) {
			SaveFreqCampDayStats((Map<String, FreqCampDayStats>) totResultData.get(GlobalConstants.FREQCAMPDAYSTATS_SUMMARY_RESULT));
		}
		if(!ObjectUtils.isEmpty(totResultData.get(GlobalConstants.FREQMEDIASCRIPTDAYSTATS_SUMMARY_RESULT))) {
			SaveFreqMediaScriptDayStats((Map<String, FreqMediaScriptDayStats>) totResultData.get(GlobalConstants.FREQMEDIASCRIPTDAYSTATS_SUMMARY_RESULT));
		}
	}

	/* (non-Javadoc)
	 * @see com.mobon.billing.dump.service.DumpSaveService@SaveDumpData(java.util.Map)
	 */
	@Async
	@SuppressWarnings("unchecked")
	public Future<Object> SaveDumpData(Map<String, Object> totResultData , String DataKey) {

		Object result = null;
		if(!ObjectUtils.isEmpty(totResultData.get(GlobalConstants.FREQDAYSTATS_SUMMARY_RESULT))
				&& GlobalConstants.FREQDAYSTATS_SUMMARY_RESULT.equals(DataKey)) {
			result = SaveFreqDayStats((Map<String, FreqDayStats>) totResultData.get(GlobalConstants.FREQDAYSTATS_SUMMARY_RESULT));

		} else if(!ObjectUtils.isEmpty(totResultData.get(GlobalConstants.FREQSDKDAYSTATS_SUMMARY_RESULT))
				&& GlobalConstants.FREQSDKDAYSTATS_SUMMARY_RESULT.equals(DataKey)) {
			result = SaveFreqSdkDayStats((Map<String, FreqSdkDayStats>) totResultData.get(GlobalConstants.FREQSDKDAYSTATS_SUMMARY_RESULT));

		} else if(!ObjectUtils.isEmpty(totResultData.get(GlobalConstants.FREQCAMPDAYSTATS_SUMMARY_RESULT))
				&& GlobalConstants.FREQCAMPDAYSTATS_SUMMARY_RESULT.equals(DataKey)) {
			result = SaveFreqCampDayStats((Map<String, FreqCampDayStats>) totResultData.get(GlobalConstants.FREQCAMPDAYSTATS_SUMMARY_RESULT));

		} else if(!ObjectUtils.isEmpty(totResultData.get(GlobalConstants.FREQMEDIASCRIPTDAYSTATS_SUMMARY_RESULT))
				&& GlobalConstants.FREQMEDIASCRIPTDAYSTATS_SUMMARY_RESULT.equals(DataKey)) {
			result = SaveFreqMediaScriptDayStats((Map<String, FreqMediaScriptDayStats>) totResultData.get(GlobalConstants.FREQMEDIASCRIPTDAYSTATS_SUMMARY_RESULT));

		}

		return new AsyncResult<Object>(result);
	}

	/**
	 * @Method Name : SaveFreqDayStats
	 * @Date : 2021. 3. 23.
	 * @Author : dkchoi
	 * @Comment : Summary된 Data를 FREQ_DAY_STATS 테이블에 등록한다.
	 * @param summaryMap
	 */
	private Object SaveFreqDayStats(Map<String, FreqDayStats> summaryMap) {

		Object result = null;
		List<FreqDayStats> saveEntities = new ArrayList<FreqDayStats>();

		try {

			log.info("@ summaryFreqDayStats Map size : " + summaryMap.size());

			summaryMap.forEach((summaryKey, summaryFreqDayStats) -> {

				Optional<FreqDayStats> freqDayStatsOptional = freqDayStatsRepository.findById(summaryFreqDayStats.getId());

				freqDayStatsOptional.ifPresent((findedFreqDayStats) -> {
					summaryFreqDayStats.addTotEprsCntFreq(findedFreqDayStats.getTotEprsCntFreq());
					summaryFreqDayStats.addParEprsCntFreq(findedFreqDayStats.getParEprsCntFreq());
					summaryFreqDayStats.addClickCntFreq(findedFreqDayStats.getClickCntFreq());
					summaryFreqDayStats.addAdvrtesAmtFreq(findedFreqDayStats.getAdvrtsAmtFreq());
					summaryFreqDayStats.addMediaPymntAmtFreq(findedFreqDayStats.getMediaPymntAmtFreq());
					summaryFreqDayStats.addTotOrderCntFreq(findedFreqDayStats.getTotOrderCntFreq());
					summaryFreqDayStats.addTotOrderAmtFreq(findedFreqDayStats.getTotOrderAmtFreq());
					summaryFreqDayStats.addSessOrderCntFreq(findedFreqDayStats.getSessOrderCntFreq());
					summaryFreqDayStats.addSessOrderAmtFreq(findedFreqDayStats.getSessOrderAmtFreq());
					summaryFreqDayStats.addDirectOrderCntFreq(findedFreqDayStats.getDirectOrderCntFreq());
					summaryFreqDayStats.addDirectOrderAmtFreq(findedFreqDayStats.getDirectOrderAmtFreq());
					summaryFreqDayStats.setRegUserId(findedFreqDayStats.getRegUserId());

				});
				freqDayStatsRepository.save(summaryFreqDayStats);
			});

		} catch (Exception e){
			log.error("SaveFreqDayStats Save Error");

			summaryMap.forEach((summaryKey, summaryFreqDayStats) -> saveEntities.add(summaryFreqDayStats));

			result = saveEntities;
			log.error(e.getMessage());
		}

		log.info("@ summaryFreqDayStats Insert Update End");

		return result;
	}

	/**
	 * @Method Name : SaveFreqSdkDayStats
	 * @Date : 2021. 4. 29.
	 * @Author : dkchoi
	 * @Comment : Summary된 Data를 FREQ_SDK_DAY_STATS 테이블에 등록한다.
	 * @param summaryMap
	 */
	private Object SaveFreqSdkDayStats(Map<String, FreqSdkDayStats> summaryMap) {

		Object result = null;
		List<FreqSdkDayStats> saveEntities = new ArrayList<FreqSdkDayStats>();

		try {

			log.info("@ summaryFreqSdkDayStats Map size : " + summaryMap.size());

			summaryMap.forEach((summaryKey, summaryFreqSdkDayStats) -> {

				Optional<FreqSdkDayStats> freqSdkDayStatsOptional = freqSdkDayStatsRepository.findById(summaryFreqSdkDayStats.getId());

				freqSdkDayStatsOptional.ifPresent((findedFreqSdkDayStats) -> {
					summaryFreqSdkDayStats.addTotEprsCntFreq(findedFreqSdkDayStats.getTotEprsCntFreq());
					summaryFreqSdkDayStats.addParEprsCntFreq(findedFreqSdkDayStats.getParEprsCntFreq());
					summaryFreqSdkDayStats.addClickCntFreq(findedFreqSdkDayStats.getClickCntFreq());
					summaryFreqSdkDayStats.addAdvrtesAmtFreq(findedFreqSdkDayStats.getAdvrtsAmtFreq());
					summaryFreqSdkDayStats.addMediaPymntAmtFreq(findedFreqSdkDayStats.getMediaPymntAmtFreq());
					summaryFreqSdkDayStats.addTotOrderCntFreq(findedFreqSdkDayStats.getTotOrderCntFreq());
					summaryFreqSdkDayStats.addTotOrderAmtFreq(findedFreqSdkDayStats.getTotOrderAmtFreq());
					summaryFreqSdkDayStats.addSessOrderCntFreq(findedFreqSdkDayStats.getSessOrderCntFreq());
					summaryFreqSdkDayStats.addSessOrderAmtFreq(findedFreqSdkDayStats.getSessOrderAmtFreq());
					summaryFreqSdkDayStats.addDirectOrderCntFreq(findedFreqSdkDayStats.getDirectOrderCntFreq());
					summaryFreqSdkDayStats.addDirectOrderAmtFreq(findedFreqSdkDayStats.getDirectOrderAmtFreq());
					summaryFreqSdkDayStats.setRegUserId(findedFreqSdkDayStats.getRegUserId());

				});
				freqSdkDayStatsRepository.save(summaryFreqSdkDayStats);
			});

		} catch (Exception e){
			log.error("SaveFreqSdkDayStats Save Error");

			summaryMap.forEach((summaryKey, summaryFreqSdkDayStats) -> saveEntities.add(summaryFreqSdkDayStats));

			result = saveEntities;
			log.error(e.getMessage());
		}

		log.info("@ summaryFreqSdkDayStats Insert Update End");

		return result;
	}

	/**
	 * @Method Name : SaveFreqCampDayStats
	 * @Date : 2021. 3. 23.
	 * @Author : dkchoi
	 * @Comment : Summary된 Data를 FREQ_CAMP_DAY_STATS 테이블에 등록한다.
	 * @param summaryMap
	 */
	private Object SaveFreqCampDayStats(Map<String, FreqCampDayStats> summaryMap) {

		Object result = null;
		List<FreqCampDayStats> saveEntities = new ArrayList<FreqCampDayStats>();

		try {

			log.info("@ summaryFreqCampDayStats Map size : " + summaryMap.size());

			summaryMap.forEach((summaryKey, summaryFreqCampDayStats) -> {

				Optional<FreqCampDayStats> freqCampDayStatsOptional = freqCampDayStatsRepository.findById(summaryFreqCampDayStats.getId());

				freqCampDayStatsOptional.ifPresent((findedFreqCampDayStats) -> {
					summaryFreqCampDayStats.addTotEprsCntFreq(findedFreqCampDayStats.getTotEprsCntFreq());
					summaryFreqCampDayStats.addParEprsCntFreq(findedFreqCampDayStats.getParEprsCntFreq());
					summaryFreqCampDayStats.addClickCntFreq(findedFreqCampDayStats.getClickCntFreq());
					summaryFreqCampDayStats.addAdvrtesAmtFreq(findedFreqCampDayStats.getAdvrtsAmtFreq());
					summaryFreqCampDayStats.addMediaPymntAmtFreq(findedFreqCampDayStats.getMediaPymntAmtFreq());
					summaryFreqCampDayStats.addTotOrderCntFreq(findedFreqCampDayStats.getTotOrderCntFreq());
					summaryFreqCampDayStats.addTotOrderAmtFreq(findedFreqCampDayStats.getTotOrderAmtFreq());
					summaryFreqCampDayStats.addSessOrderCntFreq(findedFreqCampDayStats.getSessOrderCntFreq());
					summaryFreqCampDayStats.addSessOrderAmtFreq(findedFreqCampDayStats.getSessOrderAmtFreq());
					summaryFreqCampDayStats.addDirectOrderCntFreq(findedFreqCampDayStats.getDirectOrderCntFreq());
					summaryFreqCampDayStats.addDirectOrderAmtFreq(findedFreqCampDayStats.getDirectOrderAmtFreq());
					summaryFreqCampDayStats.setRegUserId(findedFreqCampDayStats.getRegUserId());

				});
				freqCampDayStatsRepository.save(summaryFreqCampDayStats);
			});

		} catch (Exception e){
			log.error("SaveFreqCampDayStats Save Error");

			summaryMap.forEach((summaryKey, summaryFreqCampDayStats) -> saveEntities.add(summaryFreqCampDayStats));

			result = saveEntities;
			log.error(e.getMessage());
		}

		log.info("@ summaryFreqCampDayStats Insert Update End");

		return result;
	}

	/**
	 * @Method Name : SaveFreqMediaScriptDayStats
	 * @Date : 2021. 3. 23.
	 * @Author : dkchoi
	 * @Comment : Summary된 Data를 FREQ_MEDIA_SCRIPT_DAY_STATS 테이블에 등록한다.
	 * @param summaryMap
	 */
	private Object SaveFreqMediaScriptDayStats(Map<String, FreqMediaScriptDayStats> summaryMap) {

		Object result = null;
		List<FreqMediaScriptDayStats> saveEntities = new ArrayList<FreqMediaScriptDayStats>();

		try {

			log.info("@ summaryFreqMediaScriptDayStats Map size : " + summaryMap.size());

			summaryMap.forEach((summaryKey, summaryFreqMediaScriptDayStats) -> {

				Optional<FreqMediaScriptDayStats> freqMediaScriptDayStatsOptional = freqMediaScriptDayStatsRepository.findById(summaryFreqMediaScriptDayStats.getId());

				freqMediaScriptDayStatsOptional.ifPresent((findedFreqMediaScriptDayStats) -> {
					summaryFreqMediaScriptDayStats.addTotEprsCntFreq(findedFreqMediaScriptDayStats.getTotEprsCntFreq());
					summaryFreqMediaScriptDayStats.addParEprsCntFreq(findedFreqMediaScriptDayStats.getParEprsCntFreq());
					summaryFreqMediaScriptDayStats.addClickCntFreq(findedFreqMediaScriptDayStats.getClickCntFreq());
					summaryFreqMediaScriptDayStats.addAdvrtesAmtFreq(findedFreqMediaScriptDayStats.getAdvrtsAmtFreq());
					summaryFreqMediaScriptDayStats.addMediaPymntAmtFreq(findedFreqMediaScriptDayStats.getMediaPymntAmtFreq());
					summaryFreqMediaScriptDayStats.addTotOrderCntFreq(findedFreqMediaScriptDayStats.getTotOrderCntFreq());
					summaryFreqMediaScriptDayStats.addTotOrderAmtFreq(findedFreqMediaScriptDayStats.getTotOrderAmtFreq());
					summaryFreqMediaScriptDayStats.addSessOrderCntFreq(findedFreqMediaScriptDayStats.getSessOrderCntFreq());
					summaryFreqMediaScriptDayStats.addSessOrderAmtFreq(findedFreqMediaScriptDayStats.getSessOrderAmtFreq());
					summaryFreqMediaScriptDayStats.addDirectOrderCntFreq(findedFreqMediaScriptDayStats.getDirectOrderCntFreq());
					summaryFreqMediaScriptDayStats.addDirectOrderAmtFreq(findedFreqMediaScriptDayStats.getDirectOrderAmtFreq());
					summaryFreqMediaScriptDayStats.setRegUserId(findedFreqMediaScriptDayStats.getRegUserId());

				});
				freqMediaScriptDayStatsRepository.save(summaryFreqMediaScriptDayStats);
			});

		} catch (Exception e){
			log.error("SaveFreqMediaScriptDayStats Save Error");

			summaryMap.forEach((summaryKey, summaryFreqMediaScriptDayStats) -> saveEntities.add(summaryFreqMediaScriptDayStats));

			result = saveEntities;
			log.error(e.getMessage());
		}

		log.info("@ summaryFreqMediaScriptDayStats Insert Update End");

		return result;
	}
}
