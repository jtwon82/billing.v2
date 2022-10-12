package com.mobon.billing.branchUM.config;

import com.adgather.constants.G;
import com.mobon.billing.branchUM.config.InitDataConfig;
import com.mobon.billing.branchUM.service.dao.CommonDao;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitDataConfig {
	private static final Logger logger = LoggerFactory.getLogger(InitDataConfig.class);

	@Autowired
	private CommonDao commonDao;

	@PostConstruct
	public void setInitData() {
		logger.info("SET G.ADVRTS_TP_CODE START");
		G.ADVRTS_TP_CODE = this.commonDao.getAdvrtsTpCode();
		logger.info("SET G.ADVRTS_TP_CODE END");
	}
}
