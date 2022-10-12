package com.mobon.billing.logging.consumer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.adgather.constants.G;
import com.adgather.reportmodel.old.AdChargeData;
import com.adgather.reportmodel.old.DrcData;
import com.adgather.reportmodel.old.RTBDrcData;
import com.adgather.reportmodel.old.RTBReportData;
import com.adgather.reportmodel.old.ShortCutData;
import com.adgather.util.old.DateUtils;
import com.google.common.base.Joiner;
import com.mobon.billing.core.ImpressionClick;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.model.v15.ShortCutInfoData;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.FileUtils;
import com.mobon.code.CodeUtils;

import net.sf.json.JSONObject;

@Component
public class ConsumerCollector {

	private static final Logger	logger	= LoggerFactory.getLogger(ConsumerCollector.class);
	
	@Value("${log.path}")
	private String logPath;
	@Value("${log.path.clickview}")
	private String logPathClickview;
	@Value("${log.path.cart}")
	private String logPathCart;
	@Value("${log.path.point}")
	private String logPathPoint;
	@Value("${log.path.clickviewpoint}")
	private String logPathClickviewPoint;
	@Value("${log.path.conv}")
	private String logPathConv;
	@Value("${log.path.frequency}")
	private String logPathFrequency;
	@Value("${log.path.shop}")
	private String logPathShop;
	@Value("${log.path.external}")
	private String logPathExternal;
	@Value("${log.path.fromapp}")
	private String logPathfromapp;
	@Value("${log.path.near}")
	private String logPathnear;
	@Value("${log.path.rfdata}")
	private String logPathRfData;
	@Value("${log.path.advertiser}")
	private String logPathAdvertiser;
	@Value("${log.path.algo}")
	private String logPathAlgo;
	@Value("${log.path.openrtb}")
	private String logPathOpenrtb;
	@Value("${log.path.convsucc2}")
	private String logPathSuccConv;
	@Value("${log.path.bounceRate}")
	private String logPathBounceRate;
	@Value("${log.path.basketData}")
	private String logPathBasketData;
	@Value("${log.path.insiteClickView}")
	private String logPathInsiteClickView;
	@Value("${log.path.insiteConversion}")
	private String logPathInsiteConversion;
	@Value("${log.path.uniid}")
	private String logPathUniid;
	
	@Value("${profile.id}")
	private String profileId;

	public void clickview(String topic, JSONObject jSONObject) {
		try {
			String fileName = "";
			if ( G.ClickViewData.equals(topic) || G.OpenRTBClickData.equals(topic) || G.OpenRTBViewData.equals(topic) ) {
				fileName = String.format("%s%s.%s", logPathClickview, "kafka-consumer.logging.log", DateUtils.getDate("yyyy-MM-dd_HH"));

				if("Y".equals(jSONObject.get("fromApp"))) {
					String fileNameSub = String.format("%s%s.%s", logPathfromapp, "kafka-consumer.logging.log", DateUtils.getDate("yyyy-MM-dd_HH"));
					org.apache.commons.io.FileUtils.writeStringToFile(new File( fileNameSub )
							, String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), topic, jSONObject), "UTF-8", true);
				}
				
//				if(Boolean.parseBoolean(jSONObject.get("nearYn").toString())) {
//					String fileNameSub = String.format("%s%s.%s", logPathnear, "kafka-consumer.logging.log", DateUtils.getDate("yyyy-MM-dd_HH"));
//					org.apache.commons.io.FileUtils.writeStringToFile(new File( fileNameSub )
//							, String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), topic, jSONObject), "UTF-8", true);
//				}
			}
			else if( G.ExternalData.equals(topic) ) {
				if( "externalBatch".equals(jSONObject.get("dumpType")) ) {
					fileName = String.format("%s%s.%s", logPathExternal, "kafka-consumer.logging.log", DateUtils.getDate("yyyy-MM-dd_HH"));
				}
//				else {
//					fileName = String.format("%s%s.%s", logPathExternal, "kafka-consumer.logging.log", DateUtils.getDate("yyyy-MM-dd_HH"));
//				}
			}
			
			if( !"".equals(fileName) ) {
				org.apache.commons.io.FileUtils.writeStringToFile(new File( fileName )
						, String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), topic, jSONObject), "UTF-8", true);
			}
		} catch (IOException e) {
			logger.error("logging err", e);
		}
	}
	
	public void clickviewpoint(String topic, JSONObject jSONObject) {
		try {
			String fileName = "";
			if (G.ClickViewPointData.equals(topic)) {
				fileName = String.format("%s%s.%s", logPathClickviewPoint, "kafka-consumer.logging.log", DateUtils.getDate("yyyy-MM-dd_HH"));

				if("Y".equals(jSONObject.get("fromApp"))) {
					String fileNameSub = String.format("%s%s.%s", logPathfromapp, "kafka-consumer.logging.log", DateUtils.getDate("yyyy-MM-dd_HH"));
					org.apache.commons.io.FileUtils.writeStringToFile(new File( fileNameSub )
							, String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), topic, jSONObject), "UTF-8", true);
				}
			}
			
			if( !"".equals(fileName) ) {
				org.apache.commons.io.FileUtils.writeStringToFile(new File( fileName )
						, String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), topic, jSONObject), "UTF-8", true);
			}
		} catch (IOException e) {
			logger.error("logging err", e);
		}
	}
	
	public void cart(String topic, JSONObject jSONObject) {
		try {
			String fileName = "";
			if ("advertiser.cart".equals(topic)) {
				fileName = String.format("%s%s.%s", logPathCart, "kafka-consumer.logging.log", DateUtils.getDate("yyyy-MM-dd_HH"));

			}
			
			if( !"".equals(fileName) ) {
				org.apache.commons.io.FileUtils.writeStringToFile(new File( fileName )
						, String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), topic, jSONObject), "UTF-8", true);
			}
		} catch (IOException e) {
			logger.error("logging err", e);
		}
	}

	private String getVal(Object obj) {
		String result = "";
		try {
			result = obj.toString();
		}catch(Exception e) {
		}
		return result;
	}
	
	public void point(String topic, String message) {
		try {
			String fileNamePoint= "";
			JSONObject jSONObject2= null;
			if ( G.ClickViewData.equals(topic) || G.ClickViewPointData.equals(topic) || G.OpenRTBClickData.equals(topic) || G.OpenRTBViewData.equals(topic) ) {
//				PollingData item= new ObjectMapper().readValue(clickView, PollingData.class);
				
				JSONObject jSONObject = JSONObject.fromObject(message);
				Boolean noCharge = false; 
				
				try {
					noCharge = jSONObject.containsKey("noCharge")? (Boolean) jSONObject.get("noCharge") : false;					
				} catch (Exception e) {
					noCharge = false;
				}
				
				if (noCharge) {
					logger.debug("no Charge Log : point = "  +jSONObject.get("point"));
					
					return;
				} 
				
				BaseCVData r = ImpressionClick.getInstance().AvailableData(message);
				
//				String p= getVal(json.get("point"));	if("".equals(p))p="0";
				
				if ( r!=null && r.getPoint()!=0 ) { //!"0".equals(p) ) {
//					String ymd= getVal(json.get("yyyymmdd"));
//					String uid= getVal(json.get("userId"));
//					String sc= getVal(json.get("site_code"));
//					String sid= getVal(json.get("scriptUserId"));
//					String s= getVal(json.get("media_code"));
//					
//					if( ymd.equals("") ) {
//						String sendDate= json.get("sendDate").toString();
//						ymd= String.format("%s", DateUtils.getDate("yyyyMMdd", DateUtils.getStrToDate("yyyy-MM-dd HH:mm:ss", sendDate))) ;
//					}
//					if("".equals(uid))uid= getVal(json.get("userid"));
//					if("".equals(uid))uid= getVal(json.get("u"));
//					if("".equals(sc))sc= getVal(json.get("sc"));
//					if("".equals(sid))sid= getVal(json.get("scriptId"));
//					if("".equals(sid))sid= getVal(json.get("mediaid"));
//					if("".equals(s))s= getVal(json.get("s"));
//					if("".equals(s))s= getVal(json.get("script_no"));

					BaseCVData tmp= new BaseCVData();
					tmp.setScriptUserId(r.getScriptUserId());
					tmp= CodeUtils.setInterLock(tmp);
					
					jSONObject2= new JSONObject();
//					jSONObject2.put("key", json.get("key").toString());
					jSONObject2.put("ymd", r.getYyyymmdd());
					jSONObject2.put("hh", r.getHh());
					jSONObject2.put("uid", r.getAdvertiserId());
					jSONObject2.put("sc", r.getSiteCode());
					jSONObject2.put("sid", r.getScriptUserId());
					jSONObject2.put("s", r.getScriptNo());
					jSONObject2.put("p", r.getPoint());
					jSONObject2.put("itl", tmp.getItlTpCode());
					
					fileNamePoint = String.format("%s%s.%s", logPathPoint, "kafka-consumer.logging.log", DateUtils.getDate("yyyy-MM-dd_HH"));
					
					
				}
			}
			
			if( !"".equals(fileNamePoint) ) {
				org.apache.commons.io.FileUtils.writeStringToFile(new File( fileNamePoint )
						, String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), topic, jSONObject2), "UTF-8", true);
			}
		} catch (Exception e) {
			logger.error("logging err", e);
		}
	}
	
	public void advertiser(String topic, JSONObject jSONObject) {
		try {
			String fileName = "";
			if ( topic.startsWith("advertiser.conversion") || topic.startsWith("advertiser.common") ) {
				fileName = String.format("%s%s.%s", logPathAdvertiser, "kafka-consumer.logging.log", DateUtils.getDate("yyyy-MM-dd_HH"));
			}
			
			if( !"".equals(fileName) ) {
				org.apache.commons.io.FileUtils.writeStringToFile(new File( fileName )
						, String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), topic, jSONObject), "UTF-8", true);
			}
		} catch (IOException e) {
			logger.error("logging err", e);
		}
	}
	public void shop(String topic, JSONObject jSONObject) {
		try {
			String fileName = "";
			if ( G.ShopInfoData.equals(topic) || G.ShopStatsInfoData.equals(topic) ) {
				fileName = String.format("%s%s.%s", logPathShop, "kafka-consumer.logging.log", DateUtils.getDate("yyyy-MM-dd_HH"));
			}
			
			if( !"".equals(fileName) ) {
				org.apache.commons.io.FileUtils.writeStringToFile(new File( fileName )
						, String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), topic, jSONObject), "UTF-8", true);
			}
		} catch (IOException e) {
		}
	}
	
	public void conv(String topic, JSONObject jSONObject) {
		try {
			if(G.ConversionData.equals(topic)) {
				String fileName = String.format("%s%s.%s", logPathConv, "kafka-consumer.logging.log", DateUtils.getDate("yyyy-MM-dd_HH"));
				
				if( !"".equals(fileName) ) {
					org.apache.commons.io.FileUtils.writeStringToFile(new File( fileName )
							, String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), topic, jSONObject), "UTF-8", true);
				}
			}
		} catch (IOException e) {
		}
	}
	
	public void rfdata(String topic, JSONObject jSONObject) {
		try {
			if(G.RfData.equals(topic)) {
				String fileName = String.format("%s%s.%s", logPathRfData, "kafka-consumer.logging.log", DateUtils.getDate("yyyy-MM-dd_HH"));
				
				if( !"".equals(fileName) ) {
					org.apache.commons.io.FileUtils.writeStringToFile(new File( fileName )
							, String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), topic, jSONObject), "UTF-8", true);
				}
			}
		} catch (Exception e) {
		}
	}

	public void adExternalXXX(String topic, JSONObject jso) {
		if ( G.ExternalData.equals(topic) ) {
			try {
				String writeFileName = String.format("kafka-consumer.logging.log.%s_%s", G.ExternalData, DateUtils.getDate("yyyyMMdd_HH"));
				ConsumerFileUtils.writeLine( logPath +"collect/", writeFileName, topic, jso);
			} catch (IOException e) {}
		}
	}
	public void adConversion(String topic, JSONObject jso) {
		if ( G.ConversionData.equals(topic) ) {
			try {
				String writeFileName = String.format("kafka-consumer.logging.log.%s_%s", G.ConversionData, DateUtils.getDate("yyyyMMdd_HH"));
				ConsumerFileUtils.writeLine( logPath +"collect/", writeFileName, topic, jso);
			} catch (IOException e) {}
		}
	}
	public void adShopData(String topic, JSONObject jso) {
		if ( G.ShopInfoData.equals(topic) 
				&& "56".equals(jso.get("key").toString().split("-")[5]) )
		{
			String writeFileName = String.format("kafka-consumer.logging.log.%s_%s", G.ShopInfoData, DateUtils.getDate("yyyyMMdd_HH"));
			try {
				ConsumerFileUtils.writeLine( logPath +"collect/", writeFileName, topic, jso);
			} catch (IOException e) {
			}
		}
	}
	
	public void makeTopicLogFile(String topic, JSONObject jso) {
 
			String writeFileName = String.format("kafka-consumer.logging.log.topic.%s_%s", topic, DateUtils.getDate("yyyyMMdd_HH"));
			try {
				ConsumerFileUtils.writeLine( logPath +"log4j/topic/", writeFileName, topic, jso);
			} catch (IOException e) {
			}
 
	}
	
	public void frequencyLog(String topic, String className, JSONObject jso) {
		if ( G.ClickViewData.equals(topic) || G.ClickViewPointData.equals(topic) ) {
			BaseCVData row = null;
			
			if ( G.AdChargeData.equals(className) ) {
				AdChargeData tmpAdCharge = AdChargeData.fromHashMap(jso);
				row = tmpAdCharge.toBaseCVData();
				
			} else if ( G.DrcData.equals(className) ) {
				DrcData tmpDrc = DrcData.fromHashMap(jso);
				row = tmpDrc.toBaseCVData();
				
			} else if ( G.ShortCutData.equals(className) ) {
				ShortCutData a = ShortCutData.fromHashMap(jso);
				ShortCutInfoData tmp = a.toShortCutInfoData();
				row = tmp.toBaseCVData();
				
			} else if ( G.RTBReportData.equals(className) || G.RTBDrcData.equals(className) ) {
				if( G.RTBReportData.equals(className) ) {
					RTBReportData tmp = RTBReportData.fromHashMap(jso);
					row = tmp.toBaseCVData();
				} else {
					RTBDrcData tmp = RTBDrcData.fromHashMap(jso);
					row = tmp.toBaseCVData();
				}
			}
			
			if( row!=null ) {
				boolean chk = false;
				
				if( G.NORMALCHARGE.equals(row.getDumpType()) || G.MOBILECHARGE.equals(row.getDumpType()) ) {
					if( row.getViewCnt3()>0 ){
						chk = true;
					}
				} else {
					chk = true;
				}
				
				if( chk ) {
					SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
					String strLog = Joiner.on("\t").useForNull("").join(SDF.format(new Date())
							, row.getScriptNo(), row.getSiteCode(), row.getPlatform().toLowerCase().substring(0,1).equals("w")?"W":"M"
							, row.getProduct(), row.getAdGubun(), row.getFreqLog()
							, (row.getType().equals(G.CLICK)?G.CLICK:G.VIEW), 1, null, row.getPoint(), null, null
							, row.getKeyIp(), row.getGender(), row.getUserAge(), row.getServiceHostId(), row.getAbTest());
					try {
						String fileName = String.format("%s%s_%s.log", logPathFrequency, "collect_freq", DateUtils.getDate("yyyy-MM-dd_HH") );
						org.apache.commons.io.FileUtils.writeStringToFile(new File( fileName ), strLog + "\n", "UTF-8", true);
					} catch (IOException e) {
					}
				}
				
//				FrequencyFileLog.log(row, row.getType());
			}
		}
	}
	
	
	public void frequencyLogClickView(String topic, String className, JSONObject jso) {
		if ( G.ClickViewData.equals(topic) || G.ClickViewPointData.equals(topic) || G.OpenRTBClickData.equals(topic) || G.OpenRTBViewData.equals(topic)) {
			BaseCVData row = null;

			if ( G.AdChargeData.equals(className) ) {
				AdChargeData tmpAdCharge = AdChargeData.fromHashMap(jso);
				row = tmpAdCharge.toBaseCVData();
				
			} else if ( G.DrcData.equals(className) ) {
				DrcData tmpDrc = DrcData.fromHashMap(jso);
				row = tmpDrc.toBaseCVData();
				
			} else if ( G.ShortCutData.equals(className) ) {
				ShortCutData a = ShortCutData.fromHashMap(jso);
				ShortCutInfoData tmp = a.toShortCutInfoData();
				row = tmp.toBaseCVData();
				
			} else if ( G.RTBReportData.equals(className) || G.RTBDrcData.equals(className) ) {
				if( G.RTBReportData.equals(className) ) {
					RTBReportData tmp = RTBReportData.fromHashMap(jso);
					row = tmp.toBaseCVData();
					row.setScriptNo(row.getScriptHirnkNo()); // 부모지면이 빌링에서 ms
				} else {
					RTBDrcData tmp = RTBDrcData.fromHashMap(jso);
					row = tmp.toBaseCVData();
					row.setScriptNo(row.getScriptHirnkNo()); // 부모지면이 빌링에서 ms
				}
			}
			
			if( row!=null ) {
				boolean chk = false;
				
				if (G.ACTIONCHARGE.equals(row.getDumpType()) || row.isNoExposureYN()){ // 액션차지 & 미노출은 로그 적재 하지 않음.
					chk = false;
				} else {
					chk = true;
				}

				if( "91".equals(row.getChrgTpCode()) ) {	// 미인정데이터는 적제하지 않음.
					chk = false;
				}

				if( chk ) {

					CodeUtils.setInterLock(row);
					
//					SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
					String strLog = 
//							Joiner.on("\t").useForNull("").join(SDF.format(new Date())
							Joiner.on("\t").useForNull("").join(row.getSendDate()
							, row.getScriptNo(), row.getSiteCode(), row.getPlatform().toLowerCase().substring(0,1).equals("w")?"01":"02"
							, row.getProduct(), row.getAdGubun(), row.getFreqLog()
							, (row.getType().equals(G.CLICK)?G.CLICK:G.VIEW), 1, null, row.getPoint(), null, null
							, row.getKeyIp(), row.getGender(), row.getUserAge(), row.getServiceHostId(), row.getAbTest()
							, row.getInterlock()
							, row.getType().equals(G.VIEW)&&row.getViewCnt()<1?1:row.getViewCnt(), row.getViewCnt3(), row.getMpoint()
							, row.getAdvertiserId() ,row.getErgabt() , row.getStatYn(), row.getFrameId(), row.getPrdtTpCode(), row.getFrameCombiKey(), 
							row.getFrameType(), row.getFrameMatrExposureYN(), row.getFrameSendTpCode(), row.getFrameCombiTargetYn()
							);

					try {
						String fileName = String.format("%s%s_%s.log", logPathFrequency, "collect_freq_clickview", DateUtils.getDate("yyyy-MM-dd_HH") );
						org.apache.commons.io.FileUtils.writeStringToFile(new File( fileName ), strLog + "\n", "UTF-8", true);
					} catch (IOException e) {
					}
				}
				
//				FrequencyFileLog.log(row, row.getType());
			}
		}
	}
	
	public void algoLogViewData (String topic , JSONObject jSONObject) {
		try {
			String fileName = "";
			if ( G.AlgoViewData.equals(topic)) {
				fileName = String.format("%s%s.%s", logPathAlgo, "kafka-consumer.logging.log", DateUtils.getDate("yyyy-MM-dd_HH"));
			
				String fileNameSub = String.format("%s%s.%s", logPathAlgo, "kafka-consumer.logging.log", DateUtils.getDate("yyyy-MM-dd_HH"));
				org.apache.commons.io.FileUtils.writeStringToFile(new File( fileNameSub )
						, String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), topic, jSONObject), "UTF-8", true);							
			}
			
//			if( !"".equals(fileName) ) {
//				org.apache.commons.io.FileUtils.writeStringToFile(new File( fileName )
//						, String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), topic, jSONObject), "UTF-8", true);
//			}
			
		} catch (IOException e) {
			logger.error("AlgoViewData logging err", e);
		}
	}
	public void openRtbViewClickConvData (String topic , JSONObject jSONObject) {
		try {
			String fileName = "";
			List collectList = Arrays.asList("OpenRTBPoint_Google", "OpenRTBView_Google", "OpenRTBPoint_Igaworks",
					"OpenRTBView_Igaworks", "OpenRTBPoint_Mixer", "OpenRTBView_Mixer", "OpenRTBBid_Adfit",
					"OpenRTBBid_Google", "OpenRTBBid_Kakao", "OpenRTBBid_Mixer", "OpenRTBBid_Taboola",
					"OpenRTBClick_Adfit", "OpenRTBClick_Google", "OpenRTBClick_Igaworks", "OpenRTBClick_Mixer",
					"OpenRTBClick_Taboola","OpenRTBConversion_Adfit", "OpenRTBConversion_Google",
					"OpenRTBConversion_Igaworks", "OpenRTBConversion_Mixer", "OpenRTBConversion_Taboola",
					"OpenRTBPoint_Adfit", "OpenRTBPoint_Kakao", "OpenRTBPoint_Taboola", "OpenRTBView_Adfit",
					"OpenRTBView_Kakao", "OpenRTBView_Taboola");
			if (collectList.contains(topic)) {
				fileName = String.format("%s%s.%s", logPathAlgo, "kafka-consumer.logging.log", DateUtils.getDate("yyyy-MM-dd_HH"));
			
				String fileNameSub = String.format("%s%s.%s", logPathOpenrtb, "kafka-consumer.logging.log", DateUtils.getDate("yyyy-MM-dd_HH"));
				org.apache.commons.io.FileUtils.writeStringToFile(new File( fileNameSub )
						, String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), topic, jSONObject), "UTF-8", true);							
			}
		} catch (IOException e) {
			logger.error("openRtbViewClickConvData logging err", e);
		}
	}

	public void convSuccData(String topic, JSONObject jSONObject) {
		String fileName = "";

		try {
			if (G.SuccConversion.equals(topic)) {
				fileName = String.format("%s%s.%s", logPathSuccConv, "kafka-consumer.logging.log",DateUtils.getDate("yyyy-MM-dd_HH"));

				String fileNameSub = String.format("%s%s.%s", logPathSuccConv, "kafka-consumer.logging.log",DateUtils.getDate("yyyy-MM-dd_HH"));
				org.apache.commons.io.FileUtils.writeStringToFile(new File( fileNameSub ), 
						String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), topic, jSONObject), "UTF-8", true);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("ConversionSuccData logging err", e);
		}

	}

	public void bounceRateData(String topic, JSONObject jSONObject) {
		String fileName = "";

		try {
			if (G.BounceRateData.equals(topic)) {
				fileName = String.format("%s%s.%s", logPathBounceRate, "kafka-consumer.logging.log",DateUtils.getDate("yyyy-MM-dd_HH"));

				String fileNameSub = String.format("%s%s.%s", logPathBounceRate, "kafka-consumer.logging.log",DateUtils.getDate("yyyy-MM-dd_HH"));
				org.apache.commons.io.FileUtils.writeStringToFile(new File( fileNameSub ),
						String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), topic, jSONObject), "UTF-8", true);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("BounceRate logging err", e);
		}
	}

    public void basketData(String topic, JSONObject jSONObject) {
		String fileName = "";
		try {
			if (G.BasketData.equals(topic)) {
				fileName = String.format("%s%s.%s", logPathBasketData, "kafka-consumer.logging.log",DateUtils.getDate("yyyy-MM-dd_HH"));

				String fileNameSub = String.format("%s%s.%s", logPathBasketData, "kafka-consumer.logging.log",DateUtils.getDate("yyyy-MM-dd_HH"));
				org.apache.commons.io.FileUtils.writeStringToFile(new File( fileNameSub ),
						String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), topic, jSONObject), "UTF-8", true);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("BasketData logging err", e);
		}
    }

    public void insiteClickView(String topic, JSONObject jSONObject) {
		String fileName = "";
		try {
			if (G.InsiteClickView.equals(topic)) {
				fileName = String.format("%s%s.%s", logPathInsiteClickView, "kafka-consumer.logging.log",DateUtils.getDate("yyyy-MM-dd_HH"));

				String fileNameSub = String.format("%s%s.%s", logPathInsiteClickView, "kafka-consumer.logging.log",DateUtils.getDate("yyyy-MM-dd_HH"));
				org.apache.commons.io.FileUtils.writeStringToFile(new File( fileNameSub ),
						String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), topic, jSONObject), "UTF-8", true);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("InsiteClickView logging err", e);
		}
    }

	public void insiteConversion(String topic, JSONObject jSONObject) {
		String fileName = "";
		try {
			if (G.InsiteConversion.equals(topic)) {
				fileName = String.format("%s%s.%s", logPathInsiteConversion, "kafka-consumer.logging.log",DateUtils.getDate("yyyy-MM-dd_HH"));

				String fileNameSub = String.format("%s%s.%s", logPathInsiteConversion, "kafka-consumer.logging.log",DateUtils.getDate("yyyy-MM-dd_HH"));
				org.apache.commons.io.FileUtils.writeStringToFile(new File( fileNameSub ),
						String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), topic, jSONObject), "UTF-8", true);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("InsiteConversion logging err", e);
		}
	}

	public void uniidData(String topic, JSONObject jSONObject) {
		if (G.ClickViewData.equals(topic)) {
			String uniid;
			try {
				uniid = (String) jSONObject.get("uniId");
			} catch (JSONException je) {
				uniid = "";
			}

			try {
				if (uniid!=null && !uniid.trim().isEmpty()) {
					try {
						String fileNameSub = String.format("%s%s.%s", logPathUniid, "kafka-consumer.logging.log", DateUtils.getDate("yyyy-MM-dd_HH"));
						org.apache.commons.io.FileUtils.writeStringToFile(new File(fileNameSub),
								String.format("[%s]	%s	%s\n", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), topic, jSONObject), "UTF-8", true);
					} catch (IOException ie) {
						logger.error("BounceRate logging err", ie);
					}
				}
			}catch(Exception e) {
				logger.error("err ", e);
			}
		}
	}
}
