package com.mobon.billing.dump.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mobon.billing.dump.domainmodel.point.PointDataStats;
import com.mobon.billing.dump.domainmodel.point.key.PointDataStatsKey;

@Repository
public interface PointStatsRepository extends JpaRepository<PointDataStats, PointDataStatsKey>{
	

	@Query(value = "SELECT * "
			+ "FROM MOB_POINT_DATA_STATS "
			+ "WHERE STATS_DTTM = :statsDttm ", nativeQuery = true)
	List<PointDataStats> selectList(@Param("statsDttm") int yesterDayToInt);


}
