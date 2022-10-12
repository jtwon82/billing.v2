package com.mobon.billing.hhtodd.schedule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.mobon.billing.hhtodd.service.HHtoDDMariaDB;
import com.mobon.billing.util.TimeToLiveCollection;

@Component
public class TaskDDtoMTH {

	private static final Logger		logger		= LoggerFactory.getLogger(TaskDDtoMTH.class);

	@Autowired
	private HHtoDDMariaDB			hHtoDDMariaDB;

	@Autowired
	private RestTemplate			restTemplate;

	private static TimeToLiveCollection	workingKey	= new TimeToLiveCollection();

	private static int				threadCnt	= 0;

	@Value("${log.path}")					private String	logPath;
	@Value("${batch.list.size}")			private String	batchListSize;
	@Value("${was.retry.log.cnt}")			private int		wasRetryLogCnt;
	@Value("${kafka.groupsummery.url}")		private String	kafkaGroupSummeryUrl;
	@Value("${mobon.real.server.list}")		private String	mobonRealServerList;
	@Value("${consumer.totallag.cnt}")		private long	consumerTotallagCnt;

	public static void setThreadCnt(int threadCnt) {
		TaskDDtoMTH.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskDDtoMTH.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskDDtoMTH.threadCnt--;
	}

	/**
	 * 월별시간통계  
	 * MOB_*_MTH_STATS
	 */
	public void mongoToParGatrMTH() {
		try {
			Map param = new HashMap();
			hHtoDDMariaDB.intoParGatrMTHStats(param);
		}catch(Exception e) {
			logger.error("err ", e);
		}
	}

	/*
	 * 월별통계 보정 매월
	 * */
	public void revisionMTHhart() {
		logger.info("hart bit");
	}
	public void revisionMTHfixmonth() {
		StringBuffer STATS_MTH = new StringBuffer();
		try {

			{	// 임의로 지난날짜의 일자테이블을 보정할경우.
				File file  =  new File (logPath +"../");
				logger.info("{}", file.getAbsolutePath());
				File [] fileArr = file.listFiles();
				for (File readFile : fileArr) {
					if (readFile != null && readFile.exists()) {
						String fileName = readFile.getName(); 
						if(fileName.indexOf("FIXMONTH") == -1) {
							continue;
						}
						if(fileName.indexOf("_ing")>0) {
							continue;
						}

						long millis = Calendar.getInstance().getTimeInMillis();
						String file_reName = readFile.getAbsolutePath() +"_"+ millis +"_ing";
						File file_Tmp = new File( file_reName );
						readFile.renameTo( file_Tmp );

						BufferedReader fr = new BufferedReader(new FileReader(file_Tmp));
						String lineData = "";
						while ((lineData = fr.readLine()) != null) {
							if ("".equals(lineData.trim())) {
								continue;
							}
							if (lineData.length() < 6) {
								continue;
							}
							STATS_MTH.append( lineData );
							logger.info("table list2 add {}", lineData);
						}
						file_Tmp=null;

					}
				}
			}

			if(STATS_MTH.length()>0) {

				logger.info("step START");
				hHtoDDMariaDB.intoMTHRevision("step", Integer.parseInt(STATS_MTH.toString()));

				logger.info("step2 START");
				hHtoDDMariaDB.intoMTHRevision("step2", Integer.parseInt(STATS_MTH.toString()));

				logger.info("step3 START");
				hHtoDDMariaDB.intoMTHRevision("step3", Integer.parseInt(STATS_MTH.toString()));
				
				Map<String, Object> param = this.getNowFirstToLastDate(STATS_MTH.toString());
				hHtoDDMariaDB.intoCnvrsRenewMTHStatsRevision(param);
			}

		}catch(Exception e) {
			logger.error("err ", e);
		}
	}

	
	public void revisionMTH() {
		try {
			SimpleDateFormat form1 = new SimpleDateFormat("yyyyMMdd");
			Date time = new Date();			
			String statsDttm = form1.format(time);
			Map<String, Object> param = this.getFirstDateToLastDate(statsDttm);		
			String firstDate = (String) param.get("firstDate");
			int statsMth = Integer.parseInt(firstDate.substring(0,6));

			hHtoDDMariaDB.intoMTHRevision("step", statsMth);	//sub table
		}catch(Exception e) {
			logger.error("err ", e);
		}
	}
	public void revisionMTH2() {
		try {
			SimpleDateFormat form1 = new SimpleDateFormat("yyyyMMdd");
			Date time = new Date();			
			String statsDttm = form1.format(time);
			Map<String, Object> param = this.getFirstDateToLastDate(statsDttm);		
			String firstDate = (String) param.get("firstDate");
			int statsMth = Integer.parseInt(firstDate.substring(0,6));

			hHtoDDMariaDB.intoMTHRevision("step2", statsMth);	//camp par
		}catch(Exception e) {
			logger.error("err ", e);
		}
	}
	public void revisionMTH3() {
		try {
			SimpleDateFormat form1 = new SimpleDateFormat("yyyyMMdd");
			Date time = new Date();			
			String statsDttm = form1.format(time);
			Map<String, Object> param = this.getFirstDateToLastDate(statsDttm);		
			String firstDate = (String) param.get("firstDate");
			int statsMth = Integer.parseInt(firstDate.substring(0,6));

			hHtoDDMariaDB.intoMTHRevision("step3", statsMth);	//adver par
		}catch(Exception e) {
			logger.error("err ", e);
		}
	}

	/*mob_cnvrs_renew_mth_stats 적재 */
	public void cnvrsMTHStats(){
		try {
			Map param = new HashMap();
			hHtoDDMariaDB.intoCnvrsMTHStats(param);	//adver par
		}catch(Exception e) {
			logger.error("err ", e);
		}
	}
	/*mob_cnvrs_renew_mth_stats 보정 배치 매월  1일 부터 4일까지 새벽 4시*/
	public void cnvrsMTHStatsRevision() {
		logger.info("START MOB_CNVRS_RENEW_MTH_STATS REVISION");
		//해당 데이터를 Map 형태로 DB 보정배치 시작 
		SimpleDateFormat form1 = new SimpleDateFormat("yyyyMMdd");
		Date time = new Date();			
		String statsDttm = form1.format(time);
		Map<String, Object> param = this.getFirstDateToLastDate(statsDttm);		
		try {
			hHtoDDMariaDB.intoCnvrsRenewMTHStatsRevision(param);
		} catch (Exception e) {
			logger.error("err ", e);
		}

	}



	//이전달의 첫번째 날과 마지막날을 구한다
	private Map<String, Object> getFirstDateToLastDate(String statsDttm) {		

		int year = Integer.parseInt(statsDttm.substring(0, 4));
		int month = Integer.parseInt(statsDttm.substring(4, 6));
		int day = Integer.parseInt(statsDttm.substring(6,8));
		Calendar cal = Calendar.getInstance(); 
		cal.set(year, month-2, day); 
		String firstDate = String.valueOf(cal.get(Calendar.YEAR))+String.format("%02d", cal.get(Calendar.MONTH)+1)+String.format("%02d", cal.getMinimum(Calendar.DAY_OF_MONTH));	
		String lastDate = String.valueOf(cal.get(Calendar.YEAR))+String.format("%02d", cal.get(Calendar.MONTH)+1)+String.format("%02d", cal.getActualMaximum(Calendar.DAY_OF_MONTH));

		logger.info("firstDate Time - {}, lastDate Time - {}", firstDate, lastDate);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("firstDate", firstDate);
		result.put("lastDate", lastDate);

		return result;
	}
	// 재처리시 해당 날짜의 첫번째 날과 마지막날을 구한다.
	private Map<String, Object> getNowFirstToLastDate(String STATS_MTH) {
		Map<String, Object> result = new HashMap<String,Object>();
		String firstDate = STATS_MTH+"01";
		
		int year = Integer.parseInt(firstDate.substring(0, 4));
		int month = Integer.parseInt(firstDate.substring(4, 6));
		int day = Integer.parseInt(firstDate.substring(6,8));
		
		Calendar cal = Calendar.getInstance(); 
		cal.set(year, month-1, day);
		
		String lastDate = String.valueOf(cal.get(Calendar.YEAR))+String.format("%02d", cal.get(Calendar.MONTH)+1)+String.format("%02d", cal.getActualMaximum(Calendar.DAY_OF_MONTH));

		result.put("firstDate", firstDate);
		result.put("lastDate", lastDate);
		
		return result;
	}
	
	/*
	 * 마이그레이션 전용
	 * */
	public void dataMigration() {
		try {
			List<String> list = new ArrayList();
			//			list.add("20190628");
			//			list.add("20190629");
			//			list.add("20190630");

			for( String stats_dttm : list) {
				logger.info("stats_dttm-{}", stats_dttm);
				hHtoDDMariaDB.dataMigration(stats_dttm);				
			}
		}catch(Exception e) {
			logger.error("err", e);
		}
	}
}
