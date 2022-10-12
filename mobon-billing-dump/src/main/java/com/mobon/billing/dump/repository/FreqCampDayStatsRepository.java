package com.mobon.billing.dump.repository;

import com.mobon.billing.dump.domainmodel.frequency.FreqCampDayStats;
import com.mobon.billing.dump.domainmodel.frequency.key.FreqCampDayStatsKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @FileName : FreqCampDayStatsRepository.java
 * @Project : mobon-billing-dump
 * @Date : 2021. 3. 23.
 * @Author dkchoi
 * @Comment : FREQ_CAMP_DAY_STATS 테이블용 Repository.
 */
@Repository
public interface FreqCampDayStatsRepository extends JpaRepository<FreqCampDayStats, FreqCampDayStatsKey> {

}