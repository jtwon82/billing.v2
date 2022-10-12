package com.mobon.billing.core.config;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.adgather.constants.G;
import com.mobon.billing.core.service.dao.CommonDao;

@Component
public class InitDataConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(InitDataConfig.class);

	@Autowired
	private CommonDao	commonDao;
	
	
	@PostConstruct
    public void setInitData() {
		
		/* 서버 구동시 ADVRTS_TP_CODE 셋팅 */
		logger.info("SET G.ADVRTS_TP_CODE START");
		G.ADVRTS_TP_CODE = commonDao.getAdvrtsTpCode();
		logger.info("SET G.ADVRTS_TP_CODE END");
		
    }


}
