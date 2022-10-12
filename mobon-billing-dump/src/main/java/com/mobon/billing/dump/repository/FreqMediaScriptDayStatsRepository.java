package com.mobon.billing.dump.repository;

import com.mobon.billing.dump.domainmodel.frequency.FreqMediaScriptDayStats;
import com.mobon.billing.dump.domainmodel.frequency.key.FreqMediaScriptDayStatsKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @FileName : FreqMediaScriptDayStatsRepository.java
 * @Project : mobon-billing-dump
 * @Date : 2021. 3. 23.
 * @Author dkchoi
 * @Comment : FREQ_MEDIA_SCRIPT_DAY_STATS 테이블용 Repository.
 */
@Repository
public interface FreqMediaScriptDayStatsRepository extends JpaRepository<FreqMediaScriptDayStats, FreqMediaScriptDayStatsKey> {

}