package com.mobon.billing.base.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.reportmodel.old.BounceRateData;
import com.adgather.util.old.StringUtils;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.model.v15.BaseCVData;

import net.sf.json.JSONObject;

@Configuration
@Service
public class ConsumerProcessEnv {

	private static final Logger	logger	= LoggerFactory.getLogger(ConsumerProcessEnv.class);

	@Autowired
	private SumObjectManager	sumObjectManager;

	@Value("${profile.id}")
	private String	profileId;
	
	@SuppressWarnings("unused")
	public void processMain(String topic, String message) {
		try {
			String className = "";
			String userId = "";
			
			JSONObject jSONObject = JSONObject.fromObject(message);
			try {
				className = (String) jSONObject.get("className");
				userId = (String) jSONObject.get("userId");
				
				if(StringUtils.isNotEmpty(userId) && userId.length() > 20) { // 이상광고주ID로 인해 크기제한
					logger.error("adverId is too long = " + userId);
					return;
				}
				
			} catch (Exception e) {
				logger.error("err convert message to map : message - {}, msg - {}", message, e.toString());
				return;
			}
			//logger.debug("topic - {}, msg - {}", topic, message);
			
			if ( G.BounceRateData.equals(topic) ) {
				BounceRateData bouncdeRateData = BounceRateData.fromHashMap(jSONObject);
				
				BaseCVData record = bouncdeRateData.toBaseCVData();

				if( "91".equals(record.getChrgTpCode()) ) {
					// NOTHING
				}
				else
				{
					// 2022-2-8 ClientEnv, ClientEnvHA 의 deep copy 방법 clone -> custom method 로 변경
					// BaseCVData campMediaRetrnStats = SerializationUtils.clone(clientEnvStats);
					BaseCVData campMediaRetrnStats = record.clone();

					sumObjectManager.appendClientEnvironmentData(record);
					sumObjectManager.appendCampMediaRetrnAvalData(campMediaRetrnStats);
				}
			} else {
				logger.error("else topic chking topic - {},  vo - {}", topic, jSONObject );
			}
		} catch(Exception e) {
			logger.error("err ", e);
		}
	}

}
