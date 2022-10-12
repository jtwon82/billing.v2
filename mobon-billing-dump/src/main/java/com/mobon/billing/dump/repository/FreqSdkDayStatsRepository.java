package com.mobon.billing.dump.repository;

import com.mobon.billing.dump.domainmodel.frequency.FreqSdkDayStats;
import com.mobon.billing.dump.domainmodel.frequency.key.FreqSdkDayStatsKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @FileName : FreqSdkDayStatsRepository.java
 * @Project : mobon-billing-dump
 * @Date : 2021. 4. 29.
 * @Author dkchoi
 * @Comment : FREQ_DAY_STATS 테이블용 Repository.
 */
@Repository
public interface FreqSdkDayStatsRepository extends JpaRepository<FreqSdkDayStats, FreqSdkDayStatsKey> {

    @Query(value = "SELECT msc.no FROM dreamsearch.media_script msc WHERE msc.MEDIA_ITL_TP_CODE = '02'"
            , nativeQuery = true)
    List<Integer> selectSdkMediaScriptList();

}