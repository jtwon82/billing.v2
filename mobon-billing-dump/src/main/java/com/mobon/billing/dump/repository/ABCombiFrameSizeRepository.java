package com.mobon.billing.dump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobon.billing.dump.domainmodel.abtest.ABCombiFrameSize;
import com.mobon.billing.dump.domainmodel.abtest.key.ABCombiFrameSizeKey;

/**
 * @FileName : ABComFrameSizeRepository.java
 * @Project : mobon-billing-dump
 * @Date : 2020. 3. 2. 
 * @Author dkchoi
 * @Comment : AB_COM_FRAME_SIZE 테이블용 Repository.
 */
@Repository
public interface ABCombiFrameSizeRepository extends JpaRepository<ABCombiFrameSize, ABCombiFrameSizeKey> {

}
