package com.mobon.billing.sample.consumer;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.adgather.util.old.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobon.billing.sample.model.PollingData;
import com.mobon.billing.sample.model.UseridVo;
import com.mobon.billing.sample.service.SumObjectManager;
import com.mobon.billing.sample.service.dao.SelectDao;

@Configuration
@Service
public class ConsumerProcess {

	private static final Logger	logger	= LoggerFactory.getLogger(ConsumerProcess.class);

	@Autowired
	private SumObjectManager	sumObjectManager;

	@Autowired
	private SelectDao	selectDao;
	
	
	public void processMain(String topic, String message) {
		try {
			logger.debug("topic - {}, msg - {}", topic, message);
			
			PollingData obj = new ObjectMapper().readValue(message, PollingData.class);
//			obj = validation(obj);
			
			if( obj!=null ) {
//				UseridVo useridVo= obj.toUseridVo();
//				if(useridVo.getScriptUserId()==null && "".equals(useridVo.getScriptUserId()) ) {
//					Map<String,String> map = selectDao.selectMedia(obj.toUseridVo());
//					useridVo.setScriptUserId(map.get("userid"));
//				}
				UseridVo useridVo= obj.toUseridVo();
				if("".equals(useridVo.getAdvertiserId()) || "".equals(useridVo.getSiteCode())) {
					logger.info("vo:{}", useridVo);
				}
				sumObjectManager.appendUseridVo(useridVo);
			}
			
		}catch(Exception e) {
			logger.error("err ", e);
		}
	}

	public PollingData validation(PollingData obj) {

		// 테이블 파티션
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, -14);
		String minyyyymmdd = DateUtils.getDate("yyyyMMdd", new Date(c.getTimeInMillis()));
		String yyyymmdd = obj.getYyyymmdd();
		if( Integer.parseInt(yyyymmdd) < Integer.parseInt(minyyyymmdd) ) {
			return null;
		}
		
		// 필수값
		if (obj.getYyyymmdd()==null || obj.getPlatform()==null || obj.getAdGubun()==null || obj.getType()==null 
			|| obj.getSiteCode()==null || "".equals(obj.getMediaCode())
			|| obj.getAdvertiserId()==null ) {
			logger.error("Missing required, vo - {}", obj.toString());
			return null;
		}
		
		// 코드변환 - 개선해주세요
		if( !StringUtils.isNumeric(obj.getPlatform()) ) obj.setPlatform(G.convertPLATFORM_CODE(obj.getPlatform()));
		if( !StringUtils.isNumeric(obj.getProduct()) ) obj.setProduct(G.convertPRDT_CODE(obj.getProduct()));
		if( !StringUtils.isNumeric(obj.getAdGubun()) ) obj.setAdGubun(G.convertTP_CODE(obj.getAdGubun()));
		
		return obj;
	}
}
