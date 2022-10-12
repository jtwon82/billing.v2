package com.mobon.billing.dump.service.commonimpl;

import com.mobon.billing.dump.repository.ABComFrameSizeRepository;
import com.mobon.billing.dump.repository.FreqSdkDayStatsRepository;
import com.mobon.billing.dump.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @FileName : CommonServiceimpl.java
 * @Project : mobon-billing-dump
 * @Date : 2021. 3. 25.
 * @Author dkchoi
 * @Commnet : 공통 서비스.
 */
@Slf4j
@Service("commonService")
public class CommonServiceimpl implements CommonService {

	@Autowired
	ABComFrameSizeRepository abComFrameSizeRepository;

	@Autowired
	FreqSdkDayStatsRepository freqSdkDayStatsRepository;

	@Override
	public List<Integer> selectFrmeMediaInfoList() {
		return abComFrameSizeRepository.selectFrmeMediaInfoList();
	}

	@Override
	public List<Integer> selectSdkMediaScriptList() {
		return freqSdkDayStatsRepository.selectSdkMediaScriptList();
	}


}
