package com.mobon.billing.dump.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mobon.billing.dump.domainmodel.point.MobCampMediaStandardStats;
import com.mobon.billing.dump.domainmodel.point.MobCampMediaStats;
import com.mobon.billing.dump.domainmodel.point.key.MobCampMediaStatsKey;
@Repository
public interface MobCampStatsRepository extends JpaRepository<MobCampMediaStats, MobCampMediaStatsKey>{
	
	@Query(value= "SELECT STATS_DTTM, SITE_CODE, MEDIA_ID , SUM(ADVRTS_AMT) AS ADVRTS_AMT " + 
			"FROM MOB_CAMP_MEDIA_STATS " + 
			"WHERE STATS_DTTM = :statsDttm " + 
			"AND ADVRTS_AMT !=0.00" + 
			"GROUP BY STATS_DTTM, SITE_CODE,MEDIA_ID", nativeQuery = true)
	List<MobCampMediaStats> selectList(@Param("statsDttm")int yesterDayToInt);
	
	
	@Query(value = "SELECT STATS_DTTM, SITE_CODE, MEDIA_ID, '0' AS ADVRTS_AMT "
			+ "FROM MOB_CAMP_MEDIA_STATS "
			+ "WHERE STATS_DTTM = :statsDttm "
			+ "AND ADVER_ID = :adverId "
			+ "LIMIT 1", nativeQuery = true)
	List<MobCampMediaStats> selectAdverIdData(@Param("statsDttm") String statsDttm, @Param("adverId")String adverId);
	

}
