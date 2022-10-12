package com.mobon.billing.dump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobon.billing.dump.domainmodel.abtest.ABFrameSize;
import com.mobon.billing.dump.domainmodel.abtest.key.ABFrameSizeKey;

/**
 * @FileName : ABFrameSizeRepository.java
 * @Project : mobon-billing-dump
 * @Date : 2021. 1. 4.
 * @Author dkchoi
 * @Comment : AB_FRAME_SIZE 테이블용 Repository.
 */
@Repository
public interface ABFrameSizeRepository extends JpaRepository<ABFrameSize, ABFrameSizeKey> {


}
