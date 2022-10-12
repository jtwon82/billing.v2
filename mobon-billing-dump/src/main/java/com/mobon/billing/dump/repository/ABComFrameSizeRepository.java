package com.mobon.billing.dump.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mobon.billing.dump.domainmodel.abtest.ABComFrameSize;
import com.mobon.billing.dump.domainmodel.abtest.key.ABComFrameSizeKey;

/**
 * @FileName : ABComFrameSizeRepository.java
 * @Project : mobon-billing-dump
 * @Date : 2020. 3. 2. 
 * @Author dkchoi
 * @Comment : AB_COM_FRAME_SIZE 테이블용 Repository.
 */
@Repository
public interface ABComFrameSizeRepository extends JpaRepository<ABComFrameSize, ABComFrameSizeKey> {

	@Query(value = "SELECT MEDIA_SCRIPT_NO FROM dreamsearch.FRME_MEDIA_INFO WHERE PAR_TP_CODE!='01'"
			, nativeQuery = true)
	List<Integer> selectFrmeMediaInfoList();
}
