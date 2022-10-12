package com.mobon.billing.dump.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mobon.billing.dump.domainmodel.point.MobCampMediaStandardStats;
import com.mobon.billing.dump.domainmodel.point.key.MobCampMediaStatsKey;
@Repository
public interface MobCampMediaStandardStatsRepository extends JpaRepository<MobCampMediaStandardStats, MobCampMediaStatsKey>{

	
	@Query(value = "SELECT STATS_DTTM, (CASE WHEN PLTFOM_TP_CODE='01' THEN 'W' ELSE 'M' END) AS PLTFOM_TP_CODE, (SELECT CODE_VAL FROM dreamsearch.MOBON_COM_CODE WHERE CODE_TP_ID='ADVRTS_PRDT_CODE' AND CODE_ID=ADVRTS_PRDT_CODE) AS ADVRTS_PRDT_CODE,(SELECT CODE_VAL FROM dreamsearch.MOBON_COM_CODE WHERE CODE_TP_ID='ADVRTS_TP_CODE' AND CODE_ID=ADVRTS_TP_CODE) AS ADVRTS_TP_CODE, MEDIA_SCRIPT_NO, SITE_CODE, ADVER_ID, MEDIA_ID, CLICK_CNT, ADVRTS_AMT "
			+ "FROM MOB_CAMP_MEDIA_STATS "
			+ "WHERE STATS_DTTM = :statsDttm "
			+ "AND SITE_CODE = :siteCode "
			+ "AND MEDIA_ID = :mediaId "
			+ "AND CLICK_CNT != 0 "
			+ "AND (ADVRTS_AMT/CLICK_CNT) * FLOOR(:diffPoint/(ADVRTS_AMT/CLICK_CNT)) = :diffPoint"
			, nativeQuery = true)
	List<MobCampMediaStandardStats> selectDiffData(@Param("statsDttm")int statsDttm, @Param("siteCode")String siteCode, @Param("mediaId")String mediaId, @Param("diffPoint")BigDecimal diffPoint);
	
	@Query(value = "SELECT STATS_DTTM, (CASE WHEN PLTFOM_TP_CODE='01' THEN 'W' ELSE 'M' END) AS PLTFOM_TP_CODE, (SELECT CODE_VAL FROM dreamsearch.MOBON_COM_CODE WHERE CODE_TP_ID='ADVRTS_PRDT_CODE' AND CODE_ID=ADVRTS_PRDT_CODE) AS ADVRTS_PRDT_CODE,(SELECT CODE_VAL FROM dreamsearch.MOBON_COM_CODE WHERE CODE_TP_ID='ADVRTS_TP_CODE' AND CODE_ID=ADVRTS_TP_CODE) AS ADVRTS_TP_CODE, MEDIA_SCRIPT_NO, SITE_CODE, ADVER_ID, MEDIA_ID, CLICK_CNT, ADVRTS_AMT "
			+ "FROM MOB_CAMP_MEDIA_STATS "
			+ "WHERE STATS_DTTM = :statsDttm "
			+ "AND SITE_CODE = :siteCode "
			+ "AND MEDIA_ID = :mediaId "
			+ "AND CLICK_CNT != 0  LIMIT 1"
			, nativeQuery = true)
	List<MobCampMediaStandardStats> selectNotEqDiffData(@Param("statsDttm")int statsDttm, @Param("siteCode")String siteCode, @Param("mediaId")String mediaId);

}
