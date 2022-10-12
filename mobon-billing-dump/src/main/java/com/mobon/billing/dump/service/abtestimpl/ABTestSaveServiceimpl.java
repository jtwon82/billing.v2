package com.mobon.billing.dump.service.abtestimpl;

import com.mobon.billing.dump.constants.GlobalConstants;
import com.mobon.billing.dump.domainmodel.abtest.*;
import com.mobon.billing.dump.repository.*;
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
import java.util.concurrent.Future;

/**
 * @FileName : ABTestSaveServiceimpl.java
 * @Project : mobon-billing-dump
 * @Date : 2020. 3. 2.
 * @Author dkchoi
 * @Commnet : Summary된 ABTEST파일 데이터를 DB에 등록하는 서비스.
 */
@Slf4j
@Service("abTestSaveService")
public class ABTestSaveServiceimpl implements DumpSaveService {

	@Autowired
	ABComStatsRepository abComStatsRepository;

	@Autowired
	ABParStatsRepository abParStatsRepository;

	@Autowired
	ABAdverStatsWebRepository abAdverStatsWebRepository;

	@Autowired
	ABAdverStatsMobileRepository abAdverStatsMobileRepository;

	@Autowired
	ABComFrameSizeRepository abComFrameSizeRepository;

	@Autowired
	ABCombiFrameSizeRepository abCombiFrameSizeRepository;

	@Autowired
	ABFrameSizeRepository abFrameSizeRepository;

	/* (non-Javadoc)
	 * @see com.mobon.billing.dump.service.DumpSaveService#SaveDumpData(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public void SaveDumpData(Map<String, Object> totResultData) {

		if(!ObjectUtils.isEmpty(totResultData.get(GlobalConstants.ABCOMSTATS_SUMMARY_RESULT))) {
			SaveABComStats((Map<String, ABComStats>) totResultData.get(GlobalConstants.ABCOMSTATS_SUMMARY_RESULT));
		}
		if(!ObjectUtils.isEmpty(totResultData.get(GlobalConstants.ABPARSTATS_SUMMARY_RESULT))) {
			SaveABParStats((Map<String, ABParStats>) totResultData.get(GlobalConstants.ABPARSTATS_SUMMARY_RESULT));
		}
		if(!ObjectUtils.isEmpty(totResultData.get(GlobalConstants.ABADVERSTATSWEB_SUMMARY_RESULT))) {
			SaveABAdverStatsWeb((Map<String, ABAdverStatsWeb>) totResultData.get(GlobalConstants.ABADVERSTATSWEB_SUMMARY_RESULT));
		}
		if(!ObjectUtils.isEmpty(totResultData.get(GlobalConstants.ABADVERSTATSMOBILE_SUMMARY_RESULT))) {
			SaveABAdverStatsMobile((Map<String, ABAdverStatsMobile>) totResultData.get(GlobalConstants.ABADVERSTATSMOBILE_SUMMARY_RESULT));
		}
		if(!ObjectUtils.isEmpty(totResultData.get(GlobalConstants.ABCOMFRAMESIZE_SUMMARY_RESULT))) {
			SaveABComFrameSize((Map<String, ABComFrameSize>) totResultData.get(GlobalConstants.ABCOMFRAMESIZE_SUMMARY_RESULT));
		}
		if(!ObjectUtils.isEmpty(totResultData.get(GlobalConstants.ABCOMBIFRAMESIZE_SUMMARY_RESULT))) {
			SaveABCombiFrameSize((Map<String, ABCombiFrameSize>) totResultData.get(GlobalConstants.ABCOMBIFRAMESIZE_SUMMARY_RESULT));
		}
		if(!ObjectUtils.isEmpty(totResultData.get(GlobalConstants.ABFRAMESIZE_SUMMARY_RESULT))) {
			SaveABFrameSize((Map<String, ABFrameSize>) totResultData.get(GlobalConstants.ABFRAMESIZE_SUMMARY_RESULT));
		}

	}

	/* (non-Javadoc)
	 * @see com.mobon.billing.dump.service.DumpSaveService#SaveDumpData(java.util.Map)
	 */
	@Async
	@SuppressWarnings("unchecked")
	public Future<Object> SaveDumpData(Map<String, Object> totResultData , String DataKey) {

		Object result = null;

		if(!ObjectUtils.isEmpty(totResultData.get(GlobalConstants.ABCOMSTATS_SUMMARY_RESULT))
				&& GlobalConstants.ABCOMSTATS_SUMMARY_RESULT.equals(DataKey)) {

			result = SaveABComStats((Map<String, ABComStats>) totResultData.get(GlobalConstants.ABCOMSTATS_SUMMARY_RESULT));

		} else if(!ObjectUtils.isEmpty(totResultData.get(GlobalConstants.ABPARSTATS_SUMMARY_RESULT))
				&& GlobalConstants.ABPARSTATS_SUMMARY_RESULT.equals(DataKey)) {

			result = SaveABParStats((Map<String, ABParStats>) totResultData.get(GlobalConstants.ABPARSTATS_SUMMARY_RESULT));

		} else if(!ObjectUtils.isEmpty(totResultData.get(GlobalConstants.ABADVERSTATSWEB_SUMMARY_RESULT))
				&& GlobalConstants.ABADVERSTATSWEB_SUMMARY_RESULT.equals(DataKey)) {

			result = SaveABAdverStatsWeb((Map<String, ABAdverStatsWeb>) totResultData.get(GlobalConstants.ABADVERSTATSWEB_SUMMARY_RESULT));

		} else if(!ObjectUtils.isEmpty(totResultData.get(GlobalConstants.ABADVERSTATSMOBILE_SUMMARY_RESULT))
				&& GlobalConstants.ABADVERSTATSMOBILE_SUMMARY_RESULT.equals(DataKey)) {

			result = SaveABAdverStatsMobile((Map<String, ABAdverStatsMobile>) totResultData.get(GlobalConstants.ABADVERSTATSMOBILE_SUMMARY_RESULT));

		} else if(!ObjectUtils.isEmpty(totResultData.get(GlobalConstants.ABCOMFRAMESIZE_SUMMARY_RESULT))
				&& GlobalConstants.ABCOMFRAMESIZE_SUMMARY_RESULT.equals(DataKey)) {

			result = SaveABComFrameSize((Map<String, ABComFrameSize>) totResultData.get(GlobalConstants.ABCOMFRAMESIZE_SUMMARY_RESULT));

		} else if(!ObjectUtils.isEmpty(totResultData.get(GlobalConstants.ABCOMBIFRAMESIZE_SUMMARY_RESULT))
				&& GlobalConstants.ABCOMBIFRAMESIZE_SUMMARY_RESULT.equals(DataKey)) {

			result = SaveABCombiFrameSize((Map<String, ABCombiFrameSize>) totResultData.get(GlobalConstants.ABCOMBIFRAMESIZE_SUMMARY_RESULT));

		} else if(!ObjectUtils.isEmpty(totResultData.get(GlobalConstants.ABFRAMESIZE_SUMMARY_RESULT))
				&& GlobalConstants.ABFRAMESIZE_SUMMARY_RESULT.equals(DataKey)) {

			result = SaveABFrameSize((Map<String, ABFrameSize>) totResultData.get(GlobalConstants.ABFRAMESIZE_SUMMARY_RESULT));

		}

		return new AsyncResult<Object>(result);
	}

	/**
	 * @Method Name : SaveABComStats
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : Summary된 Data를 AB_COM_STATS 테이블에 등록한다.
	 * @param summaryMap
	 */
	private Object SaveABComStats(Map<String, ABComStats> summaryMap) {


		log.info("# summaryABComStats Map size : " + summaryMap.size());

		Object result = null;
		List<ABComStats> saveEntities = new ArrayList<ABComStats>();

		summaryMap.forEach((summaryKey, summaryABComStats) -> saveEntities.add(summaryABComStats));

		try {
			abComStatsRepository.saveAll(saveEntities);
		} catch (Exception e){
			log.error("SaveABComStats Save Error");
			result = saveEntities;
			log.error(e.getMessage());
		}

		log.info("# summaryABComStats Insert Update End");

		return result;
	}

	/**
	 * @Method Name : SaveABParStats
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : Summary된 Data를 AB_PAR_STATS 테이블에 등록한다.
	 * @param summaryMap
	 */
	private Object SaveABParStats(Map<String, ABParStats> summaryMap) {


		log.info("# summaryABParStats Map size : " + summaryMap.size());

		Object result = null;
		List<ABParStats> saveEntities = new ArrayList<ABParStats>();

		summaryMap.forEach((summaryKey, summaryABParStats) -> saveEntities.add(summaryABParStats));

		try {
			abParStatsRepository.saveAll(saveEntities);
		} catch (Exception e){
			log.error("SaveABParStats Save Error");
			result = saveEntities;
			log.error(e.getMessage());
		}

		log.info("# summaryABParStats Insert Update End");

		return result;
	}

	/**
	 * @Method Name : SaveABAdverStatsWeb
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : Summary된 Data를 AB_ADVER_STATS_WEB 테이블에 등록한다.
	 * @param summaryMap
	 */
	private Object SaveABAdverStatsWeb(Map<String, ABAdverStatsWeb> summaryMap) {

		log.info("# summaryABAdverStatsWeb Map size : " + summaryMap.size());

		Object result = null;
		List<ABAdverStatsWeb> saveEntities = new ArrayList<ABAdverStatsWeb>();

		summaryMap.forEach((summaryKey, summaryABAdverStatsWeb) -> saveEntities.add(summaryABAdverStatsWeb));

		try {
			abAdverStatsWebRepository.saveAll(saveEntities);
		} catch (Exception e){
			log.error("SaveABAdverStatsWeb Save Error");
			result = saveEntities;
			log.error(e.getMessage());
		}

		log.info("# summaryABAdverStatsWeb Insert Update End");

		return result;

	}

	/**
	 * @Method Name : SaveABAdverStatsMobile
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : Summary된 Data를 AB_ADVER_STATS_MOBILE 테이블에 등록한다.
	 * @param summaryMap
	 */
	private Object SaveABAdverStatsMobile(Map<String, ABAdverStatsMobile> summaryMap) {

		log.info("# summaryABAdverStatsMobile Map size : " + summaryMap.size());

		Object result = null;
		List<ABAdverStatsMobile> saveEntities = new ArrayList<ABAdverStatsMobile>();

		summaryMap.forEach((summaryKey, summaryABAdverStatsMobile) -> saveEntities.add(summaryABAdverStatsMobile));

		try {
			abAdverStatsMobileRepository.saveAll(saveEntities);
		} catch (Exception e){
			log.error("SaveABAdverStatsMobile Save Error");
			result = saveEntities;
			log.error(e.getMessage());
		}

		log.info("# summaryABAdverStatsMobile Insert Update End");

		return result;
	}

	/**
	 * @Method Name : SaveABComFrameSize
	 * @Date : 2020. 3. 2.
	 * @Author : dkchoi
	 * @Comment : Summary된 Data를 AB_COM_FRAME_SIZE 테이블에 등록한다.
	 * @param summaryMap
	 */
	private Object SaveABComFrameSize(Map<String, ABComFrameSize> summaryMap) {

		log.info("# summaryABComFrameSize Map size : " + summaryMap.size());

		Object result = null;
		List<ABComFrameSize> saveEntities = new ArrayList<ABComFrameSize>();

		summaryMap.forEach((summaryKey, summaryABComFrameSize) -> saveEntities.add(summaryABComFrameSize));

		try {
			abComFrameSizeRepository.saveAll(saveEntities);
		} catch (Exception e){
			log.error("SaveABComFrameSize Save Error");
			result = saveEntities;
			log.error(e.getMessage());
		}

		log.info("# summaryABComFrameSize Insert Update End");

		return result;

	}

	/**
	 * @Method Name : SaveABCombiFrameSize
	 * @Date : 2020. 7. 2.
	 * @Author : dkchoi
	 * @Comment : Summary된 Data를 AB_COMBI_FRAME_SIZE 테이블에 등록한다.
	 * @param summaryMap
	 */
	private Object SaveABCombiFrameSize(Map<String, ABCombiFrameSize> summaryMap) {

		log.info("# summaryABCombiFrameSize Map size : " + summaryMap.size());

		Object result = null;
		List<ABCombiFrameSize> saveEntities = new ArrayList<ABCombiFrameSize>();

		summaryMap.forEach((summaryKey, summaryABCombiFrameSize) -> saveEntities.add(summaryABCombiFrameSize));

		try {
			abCombiFrameSizeRepository.saveAll(saveEntities);
		} catch (Exception e){
			log.error("SaveABCombiFrameSize Save Error");
			result = saveEntities;
			log.error(e.getMessage());
		}

		log.info("# summaryABCombiFrameSize Insert Update End");

		return result;
	}

	/**
	 * @Method Name : SaveABFrameSize
	 * @Date : 2021. 1. 4.
	 * @Author : dkchoi
	 * @Comment : Summary된 Data를 AB_FRAME_SIZE 테이블에 등록한다.
	 * @param summaryMap
	 */
	private Object SaveABFrameSize(Map<String, ABFrameSize> summaryMap) {

		log.info("# summaryABFrameSize Map size : " + summaryMap.size());

		Object result = null;
		List<ABFrameSize> saveEntities = new ArrayList<ABFrameSize>();

		summaryMap.forEach((summaryKey, summaryABFrameSize) -> saveEntities.add(summaryABFrameSize));

		try {
			abFrameSizeRepository.saveAll(saveEntities);
		} catch (Exception e){
			log.error("SaveABFrameSize Save Error");
			result = saveEntities;
			log.error(e.getMessage());
		}

		log.info("# summaryABFrameSize Insert Update End");

		return result;

	}

}
