package com.mobon.billing.dump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobon.billing.dump.domainmodel.abtest.ABAdverStatsWeb;
import com.mobon.billing.dump.domainmodel.abtest.key.ABAdverStatsWebKey;

/**
 * @FileName : ABAdverStatsWebRepository.java
 * @Project : mobon-billing-dump
 * @Date : 2020. 3. 2. 
 * @Author dkchoi
 * @Comment : AB_ADVER_STATS_WEB 테이블용 Repository.
 */
@Repository
public interface ABAdverStatsWebRepository extends JpaRepository<ABAdverStatsWeb, ABAdverStatsWebKey> {

}
