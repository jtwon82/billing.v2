package com.mobon.billing.hhtodd.schedule;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.adgather.util.old.DateUtils;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mobon.billing.hhtodd.service.HHtoDDMariaDB;
import com.mobon.billing.model.BillingVo;
import com.mobon.billing.model.v15.KafkaGroupSummary;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.TimeToLiveCollection;

@Component
public class TaskHHtoHHDD {

	private static final Logger		logger		= LoggerFactory.getLogger(TaskHHtoHHDD.class);

	@Autowired
	private HHtoDDMariaDB			hHtoDDMariaDB;

    @Autowired
    private RestTemplate			restTemplate;

	private static TimeToLiveCollection	workingKey	= new TimeToLiveCollection();
	
	private static int				threadCnt	= 0;
	private static boolean          HHSchedulerRunning = false;
	
	@Value("${log.path}")					private String	logPath;
	@Value("${batch.list.size}")			private String	batchListSize;
	@Value("${was.retry.log.cnt}")			private int		wasRetryLogCnt;
	@Value("${kafka.groupsummery.url}")		private String	kafkaGroupSummeryUrl;
	@Value("${mobon.real.server.list}")		private String	mobonRealServerList;
	@Value("${consumer.totallag.cnt}")		private long	consumerTotallagCnt;
	@Value("${shopDataNo.path}")            private String  shopDataNoPath;
	
	public static void setThreadCnt(int threadCnt) {
		TaskHHtoHHDD.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskHHtoHHDD.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskHHtoHHDD.threadCnt--;
	}
	
	public void heartBeat() {
		logger.info("heartBeat");
	}
	
	/*
	 * 모비온 was에 카프카 전송 지연파일이 있는지 확인
	 * */
	public boolean chkingServerXXX(boolean isSkip) {
		boolean result = true;
		List<String> ipList = Arrays.asList(mobonRealServerList.split(","));
        for (String ip : ipList) {

        	Map<String, String> param = new HashMap();
        	param.put("url", ip);
        	String response = null;
        	try {
            	response = restTemplate.getForObject("http://{url}/cronwork/monitoring/kafka_error_retrycnt.txt", String.class, param);
            } catch (Exception e) {
                logger.error("err ", e);
                if(isSkip) {
                	response="0";
                } else {
	                result = false;
	                break;
                }
            }
            int retrycnt = Integer.valueOf(response.trim());
            if( retrycnt > wasRetryLogCnt ) {
            	logger.info("param - {}, response - {}", param, response);
            	result = false;
            	break;
            }
            
            logger.debug("ip - {}, cnt - {}", ip, retrycnt );
            
        }
        logger.info("chkingServer - {}", result);
        return result;
	}
	
	/*
	 * 카프카 그룹의 lag 채크
	 * > 헬스체크로 이동했음
	 * */
	public boolean chkingKafkaXXX() {
		boolean result = false;
		try {
			String response = restTemplate.getForObject(kafkaGroupSummeryUrl, String.class);
			
			JsonObject root = new JsonParser().parse(response).getAsJsonObject();
			Gson gson = new Gson();
			for (Entry<String, JsonElement> entry : root.entrySet()) {
			    KafkaGroupSummary summery = gson.fromJson(entry.getValue(), KafkaGroupSummary.class);
			    logger.debug("lag - {}, entry - {}", summery.getTotalLag(), entry.getKey() );
			    
			    if( "ClickViewData".equals(entry.getKey()) ) {
				    if( summery.getTotalLag() < consumerTotallagCnt ) {
				    	logger.info("summery - {}", summery);
				    	result = true;
				    	break;
				    }
			    }
			}
		}catch(Exception e) {
			logger.error("err ", e);
			result = false;
		}
		logger.info("chkingKafka - {}", result);
		return result;
	}
	
	  /**
	   *  임시 시간  =>  시간 데이타 처리 
	   * 1) cunsumer에서 insert 한 임시 테이블에서  시간 테이블로 이동 
	   * 2) 시간테이블로 이동할때 기존 값과 비교하여  차이값을 가지고 가서 기존값에 더해줌 
	   * 
	   */
	public void mongoToMariaHH() {

		while(HHSchedulerRunning) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		boolean result = false;
		String queryId="hhTohhInsert";

		/*시간 데이타 생성*/
		try {
			
			HHSchedulerRunning = true;
			
			Map param = new HashMap();			
			param.put("exeType", "H");
			
			result =hHtoDDMariaDB.intoHHtoHHDB(param);
			
		} catch (Exception e) {
			logger.error("mongoToMariaHH err msg - {}", e.toString());
		} finally {
			HHSchedulerRunning = false;
		}
		
 
	}
	
	  /**
	   *  과거 변경분 데이터가 한번에 밀려 들어올 경우  
	   *  과거분을 처리하느라 실시간 데이터가 변경되지 않는 이슈로 인해
	   *  화면에서 참조하고 있는 통계테이블 (MOB_COM_HH_STATS_INFO)의 데이터를 우선 처리하는 로직.
	   */
	public void mongoToMariaCOMINFODefHH() {

		/*시간 데이타 생성*/
		try {
			
			hHtoDDMariaDB.intoHHtoCOMINFODefHHDB();
			
		} catch (Exception e) {
			logger.error("mongoToMariaHH err msg - {}", e.toString());
		}
		
	}
	
	/** 
	 * 일자 데이타 처리 
	 * 1) 시간 임시 테이블에서 일자별 임시 테이블로 이동
	 * 2) 일자 임시 테이블에서 각 임 테이블로 이동
	 */
	public void mongoToMariaDD() {

		boolean result = false;
		String queryId="";

		/*시간 데이타 생성*/
		try {
			
			 Map param = new HashMap();
			  param.put("queryId", queryId);
				
				result =hHtoDDMariaDB.intoHHtoDDDB(param);
			
		} catch (Exception e) {
			logger.error("mongoToMariaDD err msg - {}", e.toString());
		}finally {
		
		
			//컨버젼 데이타는 무조건 돌게 
			try {
				
				mongoToMariaConv();
			
				} catch (Exception e) {
					logger.error("err msg - {}", e.toString());
				}
			
		}
		
	}
	public void mongoToMariaDDConv() {
		boolean result = false;
		String queryId = "";
		try {

			Map param = new HashMap();
			result = hHtoDDMariaDB.intoHHtoDDConv(param);

		} catch (Exception e) {
			logger.error("mongoToMariaDDConv err msg - {}", e.toString());
		} finally {

		}
	}
	public void mongoToMariaDDMediaChrg() {
		boolean result = false;
		String queryId = "";
		try {

			Map param = new HashMap();
			result = hHtoDDMariaDB.intoHHtoDDMediaChrg(param);

		} catch (Exception e) {
			logger.error("mongoToMariaDDMediaChrg err msg - {}", e.toString());
		} finally {

		}
	}
	public void mongoToMariaDDMediaChrgReBuild() {
		boolean result = false;
		String queryId = "";
		try {
			
			Map param = new HashMap();
			result = hHtoDDMariaDB.intoHHtoDDMediaChrgReBuild(param);
			
		} catch (Exception e) {
			logger.error("mongoToMariaDDMediaChrg err msg - {}", e.toString());
		} finally {
			
		}
	}
	public void mongoToMariaDDIntgCntr() {
		boolean result = false;
		String queryId = "";
		try {
			
			Map param = new HashMap();
			result = hHtoDDMariaDB.intoHHtoDDIntgCntr(param);
			
		} catch (Exception e) {
			logger.error("mongoToMariaDDIntgCntr err msg - {}", e.toString());
		} finally {
			
		}
	}
	public void mongoToMariaDDIntgCntrConv() {
		boolean result = false;
		String queryId = "";
		try {
			
			Map param = new HashMap();
			result = hHtoDDMariaDB.mongoToMariaDDIntgCntrConv(param);
			
		} catch (Exception e) {
			logger.error("mongoToMariaDDIntgCntrConv err msg - {}", e.toString());
		} finally {
			
		}
	}
	
	/**
	 * 컨버젼 데이타를 시간 테이블에서 일자별 테이블로 올립니다 
	 * 
	 */
	public void mongoToMariaConv() {
		

		boolean result = false;
  
		try {
			long startTime = System.currentTimeMillis();
			
			// 컨버전 통계
			Map <String, String> param = new HashMap <String, String>();
			param.put("queryId", "MOB_CNVRS_STATS");
			result =hHtoDDMariaDB.intoConvDB(param);
			
			// KPI 통계
			param.put("queryId", "MOB_CNVRS_KPI_STATS");
			result =hHtoDDMariaDB.intoConvDB(param);
			
			//MOB_CNVRS_RENEW_NCL
			param.put("queryId", "MOB_CNVRS_RENEW_STATS");
			result = hHtoDDMariaDB.intoConvDB(param);
			//MOB_CNVRS_RENEW_ADVER_STATS
			param.put("queryId", "MOB_CNVRS_RENEW_ADVER_STATS");
			result = hHtoDDMariaDB.intoConvDB(param);
			//MOB_CNVRS_RENEW_MEDIA_STATS
			param.put("queryId", "MOB_CNVRS_RENEW_MEDIA_STATS");
			result = hHtoDDMariaDB.intoConvDB(param);
			//MOB_CNVRS_RENEW_COM_STATS
			param.put("queryId", "MOB_CNVRS_RENEW_COM_STATS");
			result = hHtoDDMariaDB.intoConvDB(param);
			//MOB_CNVRS_RENEW_KPI_STATS
			param.put("queryId", "MOB_CNVRS_RENEW_KPI_STATS");
			result = hHtoDDMariaDB.intoConvDB(param);
			
			//MOB_CNVRS_RENEW_CTGR_STATS [매시간마다 말리는 방법으로 변경]
			//param.put("queryId", "MOB_CNVRS_RENEW_CTGR_STATS");
			//result = hHtoDDMariaDB.intoConvDB(param);
			
			logger.info("DD== MOB_CNVRS_RENEW_* :{} ,{}" , System.currentTimeMillis() - startTime + "(ms)");
			
		} catch (Exception e) {
			logger.error("mongoToMariaDDConv err msg - {}", e.toString());
		}
		
	}
	
	
	
	/**
	 * 지역테이블을 상세테이블로 이동합니다 
	 * 
	 */
	public void mongoToMariaNear() {
		
		boolean result = false;
		
		try {
			
			Map param = new HashMap();			
			result =hHtoDDMariaDB.intoNearDB(param);
			
		} catch (Exception e) {
			logger.error("mongoToMariaNear err msg - {}", e.toString());
		}
		
	}
	
	/**
	 * 플레이 링크 테이블에 데이타 생성 
	 * 
	 */
	public void mongoToMariaPlank() {
		
		boolean result = false;
		
		try {
			
			Map param = new HashMap();			
			result =hHtoDDMariaDB.intoPlinkDB(param);
			
		} catch (Exception e) {
			logger.error("mongoToMariaPlank err msg - {}", e.toString());
		}
		
	}
	 
	/*
	 * 보정 배치 
	 */
	public void mongoToMariaRevisionHH() {

		while(HHSchedulerRunning) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/*시간 데이타 생성*/
		try {
			HHSchedulerRunning = true; 
			
			Map param = new HashMap();
			 param.put("exeType", "R");
			 
			hHtoDDMariaDB.intoHHtoHHDB(param);
			
		} catch (Exception e) {
			logger.error("mongoToMariaRevisionHH err msg - {}", e.toString());
		} finally {
			HHSchedulerRunning = false;
		}
		

	}
	
	

	/**
	 * status_conversion 데이타 생성  
	 * 
	 */
	public void mongoToDreamConvDB() {
		
		boolean result = false;
		
		try {
			Map param = new HashMap();			
			result =hHtoDDMariaDB.intoDreamConvDB(param);
			
		} catch (Exception e) {
			logger.error("mongoToDreamConvDB err msg - {}", e.toString());
		}
		
	}
	

	/**
	 * 지역 보정배치  
	 * 
	 */
	public void mongoToNearRevision() {
		
		boolean result = false;
		
		try {
			Map param = new HashMap();			
			result =hHtoDDMariaDB.intoNearRevision(param);
			
		} catch (Exception e) {
			logger.error("mongoToDreamConvDB err msg - {}", e.toString());
		}
		
	}
	

	/**
	 * 타겟 광고주 월별시간통계  
	 * TRGT_ADVER_MTH_HH_STATS
	 */
	public void mongoToMariaAdverMTHhh() {
		try {
			Map param = new HashMap();
			hHtoDDMariaDB.intoAdverMTHhhStats(param);
		}catch(Exception e) {
			logger.error("err ", e);
		}
	}
	
	public void mongoToMariaCNVRS_ADVERID() {
		try {
			this.hHtoDDMariaDB.dataCNVRS_ADVERID();
		} catch (Exception e) {
			logger.error("err ", e);
		}
	}

	public void snapShotMediaScript() {
		List<Map<String, String>> list = hHtoDDMariaDB.snapShotMediaScript();
		long millis = Calendar.getInstance().getTimeInMillis();
		String writeFileName = String.format("%s_%s", "MOB_MEDIA_SCRIPT_STATS", DateUtils.getDate("yyyyMMdd_HHmmss"), millis);

		for (Map<String, String> r : list) {
			try {
				String format_str = Joiner.on("\t").useForNull("").join(r.get("STATS_DTTM"), r.get("PLTFOM_TP_CODE"), r.get("ADVRTS_PRDT_CODE")
						, r.get("ADVRTS_TP_CODE"), r.get("MEDIA_SCRIPT_NO"), r.get("ITL_TP_CODE"), r.get("MEDIA_ID"), r.get("TOT_EPRS_CNT"), r.get("PAR_EPRS_CNT"), r.get("CLICK_CNT")
						, r.get("ADVRTS_AMT"), r.get("MEDIA_PYMNT_AMT"), r.get("TRGT_EPRS_CNT"), r.get("TRGT_PAR_EPRS_CNT"), r.get("TRGT_CLICK_CNT"), r.get("TRGT_ADVRTS_AMT"));
				
				ConsumerFileUtils.writeLine( "logs/media/", writeFileName, "MOB_MEDIA_SCRIPT_STATS", format_str);
				
			} catch (IOException e) {
				logger.error("err ",e);
			}
		}
		logger.info("succ snapShotMediaScript {}", writeFileName);

		// MOB_MEDIA_SCRIPT_CHRG_STATS 추가
		List<Map<String, String>> chrgList = hHtoDDMariaDB.snapShotMediaScriptChrg();
		String writeChrgFileName = String.format("%s_%s", "MOB_MEDIA_SCRIPT_CHRG_STATS", DateUtils.getDate("yyyyMMdd_HHmmss"), millis);

		for (Map<String, String> chrgRow : chrgList) {
			try {
				String format_str = Joiner.on("\t").useForNull("").join(chrgRow.get("STATS_DTTM"), chrgRow.get("PLTFOM_TP_CODE"), chrgRow.get("ADVRTS_PRDT_CODE")
						, chrgRow.get("ADVRTS_TP_CODE"), chrgRow.get("MEDIA_SCRIPT_NO"), chrgRow.get("ITL_TP_CODE"), chrgRow.get("MEDIA_ID"), chrgRow.get("TOT_EPRS_CNT"), chrgRow.get("PAR_EPRS_CNT"), chrgRow.get("CLICK_CNT")
						, chrgRow.get("ADVRTS_AMT"), chrgRow.get("MEDIA_PYMNT_AMT"), chrgRow.get("TRGT_EPRS_CNT"), chrgRow.get("TRGT_PAR_EPRS_CNT"), chrgRow.get("TRGT_CLICK_CNT"), chrgRow.get("TRGT_ADVRTS_AMT"));

				ConsumerFileUtils.writeLine( "logs/media/", writeChrgFileName, "MOB_MEDIA_SCRIPT_CHRG_STATS", format_str);

			} catch (IOException e) {
				logger.error("err ",e);
			}
		}
		logger.info("succ snapShotMediaScriptChrg {}", writeChrgFileName);
	}
	
	/**
	 * 광고주 상품 카테고리 정보 등록 스케쥴  
	 * 
	 */
	public void insertadverPrdtCtgrInfo() {
		BufferedReader fr = null;
		FileWriter fw = null;
		try {
			
			BillingVo billingVo = new BillingVo();
			File file  =  new File (shopDataNoPath+"no.txt");
			
			fr = new BufferedReader(new FileReader(file));
			
			String lineData = "";
			while ((lineData = fr.readLine()) != null) {
				if(lineData.contains("shopDataNo")) {
					billingVo.setShopDataNo(Long.parseLong(lineData.substring(11,lineData.length())));
				}
				if(lineData.contains("mobShopDataNo")) {
					billingVo.setMobShopDataNo(Long.parseLong(lineData.substring(14,lineData.length())));
				}
			}
			
			/* 이전 스케쥴의 동작 실패로 인해 shopNo 값이 변경되었을 경우 새로 조회하여 처리하는 방어 로직 */
			if(billingVo.getShopDataNo()==0 || billingVo.getMobShopDataNo()==0) {
				logger.info("ShopDataNo ReSelect");
				billingVo = hHtoDDMariaDB.selectMaxShopNo();
				logger.info("ShopDataNo ReSelect Result == " + "shopDataNo = "+billingVo.getShopDataNo()+" mobShopDataNo = "+billingVo.getMobShopDataNo());
				billingVo = hHtoDDMariaDB.intoPrdtCtgrInfo(billingVo);
			} else {
				billingVo = hHtoDDMariaDB.intoPrdtCtgrInfo(billingVo);				
			}
			
			String shopDataNo = "shopDataNo="+billingVo.getShopDataNo()+"\n"+"mobShopDataNo="+billingVo.getMobShopDataNo();
			
			logger.info("ShopDataNo Insert After === " + "shopDataNo = "+billingVo.getShopDataNo()+" mobShopDataNo = "+billingVo.getMobShopDataNo());
			
			fw = new FileWriter(file,false);
			fw.write(shopDataNo);
			
		} catch (Exception e) {
			logger.error("insertadverPrdtCtgrInfo err msg - {}", e.toString());
		} finally {
			try {
				if(fr != null) {fr.close();}
				if(fw != null) {fw.close();}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	/*
	 * 주차 별 CTR 통계 insert 
	 * */
	public void insertCtrWeekInfo () {
		try {
			Map param = new HashMap();
			hHtoDDMariaDB.intoMediaWeekStats(param);
		}catch(Exception e) {
			logger.error("insertCtrWeekInfo err msg - {} ", e.toString());
		}
	}
	
	/*12시 30분 이후에 들어오는 성과 데이터 NCL -> RENEW_NCL*/
	public void insertBeforeRenewData () {
		try {
			Map param = new HashMap();
			hHtoDDMariaDB.insertBeforeRenewData(param);
		} catch (Exception e) {
			logger.error("insertBeforeRenewData err msg - {} ", e.toString());
		}
	}
	/*주차별 ctr 통계 마이그래이션 로직*/
	public void insertBeforeCtrData () {
		String intervalDay = "";
		String location = "/home/dreamsearch/hhtodd/";
		String fileName = "CTR_INTERVAL_DATA.txt";
		ArrayList <String> intervalList = new ArrayList<String>();
		File file = new File(location+fileName);
		
		try {
			logger.info("CTR DATA Migration START");
						
			FileReader filereader = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(filereader);
			String line = "";
			while ((line = bufReader.readLine()) != null) {
				intervalList.add(line);
			}
			if (intervalList.size() == 0 || "".equals(intervalDay)) {				
				logger.info("End CTR DATA Migration");
			} else {
				intervalDay = intervalList.get(0);				
			}
			bufReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info(intervalDay);
		
		if ("".equals(intervalDay)) {
			logger.info("End CTR DATA Migration intervalDay :::" + intervalDay);
			return;
		}
		
		HashMap<String, Object> param = new HashMap<String, Object>();		
		param.put("intervalDay", intervalDay);
		
		
		try {
			hHtoDDMariaDB.insertBeforeCtrData(param);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				if (file.isFile() && file.canWrite()) {
					for( int i = 1; i < intervalList.size(); i++) {
						bw.write(intervalList.get(i));
						bw.newLine();
					}
					bw.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	/*MOB_MEDIA_SCRIPT_CHRG_STATS 차이 보정*/
	public void updateDiffMobMediaScriptChrgStats() {	
		try {
			Map param = new HashMap();
			hHtoDDMariaDB.updateDiffMobMediaScriptChrgStats(param);
		} catch (Exception e) {
			logger.error("updateDiffMobMediaScriptChrgStats err msg - {} ", e.toString());
		}
	}
	
}
