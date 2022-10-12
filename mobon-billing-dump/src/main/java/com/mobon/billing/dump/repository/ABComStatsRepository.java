package com.mobon.billing.dump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobon.billing.dump.domainmodel.abtest.ABComStats;
import com.mobon.billing.dump.domainmodel.abtest.key.ABComStatsKey;

/**
 * @FileName : ABComStatsRepository.java
 * @Project : mobon-billing-dump
 * @Date : 2020. 3. 2. 
 * @Author dkchoi
 * @Comment : AB_COM_STATS 테이블용 Repository.
 */
@Repository
public interface ABComStatsRepository extends JpaRepository<ABComStats, ABComStatsKey> {

}