package com.mobon.billing.pargatr.consumer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.adgather.constants.G;
import com.adgather.reportmodel.old.AdChargeData;
import com.adgather.reportmodel.old.DrcData;
import com.adgather.reportmodel.old.RTBDrcData;
import com.adgather.reportmodel.old.RTBReportData;
import com.adgather.reportmodel.old.ShortCutData;
import com.mobon.billing.core.billing.ConsumerProcess;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.model.v15.ExternalInfoData;
import com.mobon.billing.model.v15.ShortCutInfoData;

import net.sf.json.JSONObject;

@Configuration
@Service
public class ConsumerProcessPargatr extends ConsumerProcess{

	private static final Logger	logger	= LoggerFactory.getLogger(ConsumerProcessPargatr.class);

	@Autowired
	private SumObjectManager	sumObjectManager;
	
	
	@SuppressWarnings("unused")
	public void processMain(String topic, String message) {
		try {
			String className = "";
			JSONObject jSONObject = JSONObject.fromObject(message);
			try {
				className = (String) jSONObject.get("className");
			} catch (Exception e) {
				logger.error("err convert message to map : message - {}, msg - {}", message, e.toString());
				return;
			}
			
			if ( G.ClickViewData.equals(topic) || G.ClickViewPointData.equals(topic) ) {
				BaseCVData record = null;
				
				if ( G.AdChargeData.equals(className) ) {
					AdChargeData tmpAdCharge = AdChargeData.fromHashMap(jSONObject);
					record = tmpAdCharge.toBaseCVData();
					record = processBaseCVData( record );		// AdChargeData
					
				} else if ( G.DrcData.equals(className) ) {
					DrcData tmpDrc = DrcData.fromHashMap(jSONObject);
					record = tmpDrc.toBaseCVData();
					record = processBaseCVData( record );
					
				} else if ( G.ShortCutData.equals(className) ) {
					ShortCutData a = ShortCutData.fromHashMap(jSONObject);
					ShortCutInfoData tmp = a.toShortCutInfoData();
					record = tmp.toBaseCVData();
					record = processShortCutInfoData(record);
					
				} else if ( G.RTBReportData.equals(className) || G.RTBDrcData.equals(className) ) {
					if( G.RTBReportData.equals(className) ) {
						RTBReportData tmp = RTBReportData.fromHashMap(jSONObject);
						record = tmp.toBaseCVData();
						record = processRtbViewData(record);
					} else {
						RTBDrcData tmp = RTBDrcData.fromHashMap(jSONObject);
						record = tmp.toBaseCVData();
						record = processRtbClickData(record);
					}
				}

				if( record!=null ) {
					try {
						Date sendDate = new Date(new SimpleDateFormat("yyyyMMdd").parse( record.getYyyymmdd() ).getTime());
						int sendHH = Integer.parseInt(record.getHh());
						int sendYMD = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(sendDate));
						int todayYMD = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date()));
						logger.debug("sendHH-{}, sendYMD-{}, todayYMD-{}, ", sendHH, sendYMD, todayYMD);
						
						if( sendHH>=6 ) {
							if(sendYMD != todayYMD) {
								logger.debug("profileId {}", record);
								sumObjectManager.appendParGatrData(record);
							}
						}
					} catch (ParseException e) {
						logger.error("err data - {}", record, e);
					}
				}
			}
			else if ( G.ExternalData.equals(topic) ) {
				ExternalInfoData r = processExternalChargeData(jSONObject);
				if(r!=null) {
					if(r.getDumpType().equals(G.EXTERNALBATCH)) {
						// 외부연동도 같이 수집하도록 수정
						
						BaseCVData mc = r.toBaseCVData();
						mc.setType(G.VIEWCLICK);
						mc.setHh("99");
						
						logger.debug("yyyymmdd-{}, userid-{}, scriptUserId-{}, site_code-{}, scriptNo-{}, platform-{}, product-{}, adgubun-{}, itlTpCode-{}, viewcnt-{}, clickcnt-{}, point-{}, "
								, mc.getYyyymmdd(), mc.getAdvertiserId(), mc.getScriptUserId(), mc.getSiteCode(), mc.getScriptNo(), mc.getPlatform(), mc.getProduct(), mc.getAdGubun(), mc.getInterlock()
								, mc.getViewCnt(), mc.getClickCnt(), mc.getPoint());
						logger.debug("type-{}, viewcnt-{}, clickcnt-{}", mc.getType(), mc.getViewCnt(), mc.getClickCnt());

						sumObjectManager.appendParGatrData(mc);
					}
				}else {
				}
			}
			else {
				logger.error("else topic chking topic - {},  vo - {}", topic, jSONObject );
			}
		}catch(Exception e) {
			logger.error("err ", e);
		}
	}
	

}
