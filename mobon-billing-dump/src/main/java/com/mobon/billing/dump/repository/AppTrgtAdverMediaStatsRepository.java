package com.mobon.billing.dump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mobon.billing.dump.domainmodel.apptrgt.AppTrgtAdverMediaStats;
import com.mobon.billing.dump.domainmodel.apptrgt.key.AppTrgtAdverMediaStatsKey;

/**
 * @FileName : 
 * @Project : mobon-billing-dump
 * @Date : 2020. 5. 12. 
 * @Author 
 * @Comment : 
 */
@Repository
public interface AppTrgtAdverMediaStatsRepository extends JpaRepository<AppTrgtAdverMediaStats, AppTrgtAdverMediaStatsKey> {
	
	@Modifying
	@Query(value = "INSERT INTO APP_TRGT_ADVER_STATS(" + 
			"  STATS_DTTM" + 
			", PLTFOM_TP_CODE" + 
			", ADVRTS_TP_CODE" + 
			", ADVRTS_PRDT_CODE" + 
			", ADVER_ID" + 
			", TOT_EPRS_CNT" + 
			", PAR_EPRS_CNT" + 
			", CLICK_CNT" + 
			", ADVRTS_AMT" + 
			", DPLK_AMT" + 
			", REG_USER_ID" + 
			", REG_DTTM" + 
			")" + 
			"SELECT * FROM (" +
			"SELECT STATS_DTTM" + 
			"     , PLTFOM_TP_CODE" + 
			"     , ADVRTS_TP_CODE" + 
			"     , ADVRTS_PRDT_CODE" + 
			"     , ADVER_ID" + 
			"     , SUM(TOT_EPRS_CNT) AS TOT_EPRS_CNT" + 
			"     , SUM(PAR_EPRS_CNT) AS PAR_EPRS_CNT" + 
			"     , SUM(CLICK_CNT) AS CLICK_CNT" + 
			"     , SUM(ADVRTS_AMT) AS ADVRTS_AMT" + 
			"     , SUM(DPLK_AMT) AS DPLK_AMT" + 
			"     , 'BILLING_DUMP_BATCH' AS REG_USER_ID" + 
			"     , NOW() AS REG_DTTM" + 
			"  FROM BILLING.APP_TRGT_ADVER_MEDIA_STATS" + 
			" WHERE STATS_DTTM = :statsDttm" + 
			" GROUP BY STATS_DTTM" + 
			"     , PLTFOM_TP_CODE" + 
			"     , ADVRTS_TP_CODE" + 
			"     , ADVRTS_PRDT_CODE" + 
			"     , ADVER_ID ) Z" + 
			" ON DUPLICATE KEY UPDATE" + 
			"  APP_TRGT_ADVER_STATS.TOT_EPRS_CNT = Z.TOT_EPRS_CNT" + 
			", APP_TRGT_ADVER_STATS.PAR_EPRS_CNT = Z.PAR_EPRS_CNT" + 
			", APP_TRGT_ADVER_STATS.CLICK_CNT = Z.CLICK_CNT" + 
			", APP_TRGT_ADVER_STATS.ADVRTS_AMT = Z.ADVRTS_AMT" + 
			", APP_TRGT_ADVER_STATS.DPLK_AMT = Z.DPLK_AMT" + 
			", APP_TRGT_ADVER_STATS.ALT_USER_ID = 'BILLING_DUMP_BATCH'" + 
			", APP_TRGT_ADVER_STATS.ALT_DTTM = NOW()"
			, nativeQuery = true)
	int insertAppTrgtAdverStats(@Param("statsDttm") int statsDttm);
	
	@Modifying
	@Query(value = "INSERT INTO APP_TRGT_DAY_STATS(" + 
			"  STATS_DTTM" + 
			", PLTFOM_TP_CODE" + 
			", ADVRTS_TP_CODE" + 
			", ADVRTS_PRDT_CODE" + 
			", TOT_EPRS_CNT" + 
			", PAR_EPRS_CNT" + 
			", CLICK_CNT" + 
			", ADVRTS_AMT" + 
			", DPLK_AMT" + 
			", REG_USER_ID" + 
			", REG_DTTM" + 
			")" + 
			"SELECT * FROM (" +
			"SELECT STATS_DTTM" + 
			"     , PLTFOM_TP_CODE" + 
			"     , ADVRTS_TP_CODE" + 
			"     , ADVRTS_PRDT_CODE" + 
			"     , SUM(TOT_EPRS_CNT) AS TOT_EPRS_CNT" + 
			"     , SUM(PAR_EPRS_CNT) AS PAR_EPRS_CNT" + 
			"     , SUM(CLICK_CNT) AS CLICK_CNT" + 
			"     , SUM(ADVRTS_AMT) AS ADVRTS_AMT" + 
			"     , SUM(DPLK_AMT) AS DPLK_AMT" + 
			"     , 'BILLING_DUMP_BATCH' AS REG_USER_ID" + 
			"     , NOW() AS REG_DTTM" + 
			"  FROM BILLING.APP_TRGT_ADVER_MEDIA_STATS" + 
			" WHERE STATS_DTTM = :statsDttm" + 
			" GROUP BY STATS_DTTM" + 
			"     , PLTFOM_TP_CODE" + 
			"     , ADVRTS_TP_CODE" + 
			"     , ADVRTS_PRDT_CODE) Z" + 
			" ON DUPLICATE KEY UPDATE" + 
			"  APP_TRGT_DAY_STATS.TOT_EPRS_CNT = Z.TOT_EPRS_CNT" + 
			", APP_TRGT_DAY_STATS.PAR_EPRS_CNT = Z.PAR_EPRS_CNT" + 
			", APP_TRGT_DAY_STATS.CLICK_CNT = Z.CLICK_CNT" + 
			", APP_TRGT_DAY_STATS.ADVRTS_AMT = Z.ADVRTS_AMT" + 
			", APP_TRGT_DAY_STATS.DPLK_AMT = Z.DPLK_AMT" + 
			", APP_TRGT_DAY_STATS.ALT_USER_ID = 'BILLING_DUMP_BATCH'" + 
			", APP_TRGT_DAY_STATS.ALT_DTTM = NOW()"
			, nativeQuery = true)
	int insertAppTrgtDayStats(@Param("statsDttm") int statsDttm);
	
	@Modifying
	@Query(value = "INSERT INTO APP_TRGT_MEDIA_STATS(" + 
			"  STATS_DTTM" + 
			", PLTFOM_TP_CODE" + 
			", ADVRTS_TP_CODE" + 
			", ADVRTS_PRDT_CODE" + 
			", MEDIA_SCRIPT_NO" + 
			", MEDIA_ID" + 
			", TOT_EPRS_CNT" + 
			", PAR_EPRS_CNT" + 
			", CLICK_CNT" + 
			", ADVRTS_AMT" + 
			", DPLK_AMT" + 
			", REG_USER_ID" + 
			", REG_DTTM" + 
			")" + 
			"SELECT * FROM (" + 
			"SELECT STATS_DTTM" + 
			"     , PLTFOM_TP_CODE" + 
			"     , ADVRTS_TP_CODE" + 
			"     , ADVRTS_PRDT_CODE" + 
			"     , MEDIA_SCRIPT_NO" + 
			"     , msc.userid AS MEDIA_ID" + 
			"     , SUM(TOT_EPRS_CNT) AS TOT_EPRS_CNT" + 
			"     , SUM(PAR_EPRS_CNT) AS PAR_EPRS_CNT" + 
			"     , SUM(CLICK_CNT) AS CLICK_CNT" + 
			"     , SUM(ADVRTS_AMT) AS ADVRTS_AMT" + 
			"     , SUM(DPLK_AMT) AS DPLK_AMT" + 
			"     , 'BILLING_DUMP_BATCH' AS REG_USER_ID" + 
			"     , NOW() AS REG_DTTM" + 
			"  FROM BILLING.APP_TRGT_ADVER_MEDIA_STATS ATAMS" + 
			"     , dreamsearch.media_script msc" +
			" WHERE STATS_DTTM = :statsDttm" + 
			"   AND ATAMS.MEDIA_SCRIPT_NO = msc.no" +
			" GROUP BY STATS_DTTM" + 
			"     , PLTFOM_TP_CODE" + 
			"     , ADVRTS_TP_CODE" + 
			"     , ADVRTS_PRDT_CODE" + 
			"     , MEDIA_SCRIPT_NO " + 
			"     , MEDIA_ID ) Z " + 
			" ON DUPLICATE KEY UPDATE" + 
			"  APP_TRGT_MEDIA_STATS.TOT_EPRS_CNT = Z.TOT_EPRS_CNT" + 
			", APP_TRGT_MEDIA_STATS.PAR_EPRS_CNT = Z.PAR_EPRS_CNT" + 
			", APP_TRGT_MEDIA_STATS.CLICK_CNT = Z.CLICK_CNT" + 
			", APP_TRGT_MEDIA_STATS.ADVRTS_AMT = Z.ADVRTS_AMT" + 
			", APP_TRGT_MEDIA_STATS.DPLK_AMT = Z.DPLK_AMT" + 
			", APP_TRGT_MEDIA_STATS.ALT_USER_ID = 'BILLING_DUMP_BATCH'" + 
			", APP_TRGT_MEDIA_STATS.ALT_DTTM = NOW()"
			, nativeQuery = true)
	int insertAppTrgtMediaStats(@Param("statsDttm") int statsDttm);
}