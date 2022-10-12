package com.mobon.billing.dump.repository;

import com.mobon.billing.dump.domainmodel.frequency.FreqDayStats;
import com.mobon.billing.dump.domainmodel.frequency.key.FreqDayStatsKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @FileName : FreqDayStatsRepository.java
 * @Project : mobon-billing-dump
 * @Date : 2021. 3. 23.
 * @Author dkchoi
 * @Comment : FREQ_DAY_STATS 테이블용 Repository.
 */
@Repository
public interface FreqDayStatsRepository extends JpaRepository<FreqDayStats, FreqDayStatsKey> {

}