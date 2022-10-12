package com.mobon.billing.viewclicklog.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.adgather.constants.G;
import com.mobon.billing.viewclicklog.service.dao.SelectDao;

@Component
public class InitDataConfig {
	
	private static final Logger logger= LoggerFactory.getLogger(InitDataConfig.class);

	@Autowired
	private SelectDao selectDao;
	
	
	@PostConstruct
    public void setInitData() {
		
		/* 서버 구동시 ADVRTS_TP_CODE 셋팅 */
		logger.info("SET G.ADVRTS_TP_CODE START");
		G.ADVRTS_TP_CODE = selectDao.getAdvrtsTpCode();
		logger.info("SET G.ADVRTS_TP_CODE END");
		
    }


}
