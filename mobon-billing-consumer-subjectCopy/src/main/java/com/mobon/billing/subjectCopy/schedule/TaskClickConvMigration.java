package com.mobon.billing.subjectCopy.schedule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.InetAddress;
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

import com.mobon.billing.subjectCopy.service.dao.ClickHouseDao;
import com.mobon.billing.subjectCopy.service.dao.SelectDao;
import com.mobon.billing.util.FileUtils;

@Component
public class TaskClickConvMigration {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskClickConvMigration.class);
	
	@Value("${log.recovery}")
	private String recoveryLogPath;
	
	@Autowired
	private ClickHouseDao clickHouseDao;
	
	@Autowired
	private SelectDao selectDao;

	private boolean stats = true;
	
	public void mongoToClickConvMigration () {
		logger.info("TASK MIGRATION CLICK_HOUSE START");
		try {
			InetAddress ip = InetAddress.getLocalHost();
			String serverIp = ip.getHostAddress();
			String batchIp = "192.168.2.70";
			logger.info("Runing Server IP - {}", serverIp);
			if (!serverIp.equals(batchIp)) {
				logger.info("It's not Batch Server - {}", serverIp);
				return ;
			}
		} catch (Exception e) {
			logger.error( "ip Host Exception - {} ", e);
		}


		try {
			Date date = new Date();
			SimpleDateFormat startFormat = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
			SimpleDateFormat endFormat = new SimpleDateFormat("yyyy-MM-dd HH:59:59");
			Calendar cal =  Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.HOUR, -1);
			
			String startTime = startFormat.format(cal.getTime());

			String endTime = endFormat.format(cal.getTime());
			
			Map<String ,Object> param = new HashMap<String ,Object>();
			param.put("startTime", startTime);
			param.put("endTime", endTime);
			
			clickHouseDao.migrationClickHouseData(param);
			
			clickHouseDao.migrationclickHouseFrameData(param);
			
		} catch( Exception e) {
			logger.error("Migration Error - {}", e);
		}
		
	}
	// 소재 카피 데이터 마이그레션시 해당 날짜 데이터 재 마이그레이션 
	public void mongoToClickConvSumMigration() {
		//FIXDAY 파일을 서치 
		logger.info("TASK MIGRATION CLICKCONVSUM START");
		ArrayList <String> statsHHs = new ArrayList<String>();
		statsHHs.add("00");
		statsHHs.add("01");
		statsHHs.add("02");
		statsHHs.add("03");
		statsHHs.add("04");
		statsHHs.add("05");
		statsHHs.add("06");
		statsHHs.add("07");
		statsHHs.add("08");
		statsHHs.add("09");
		statsHHs.add("10");
		statsHHs.add("11");
		statsHHs.add("12");
		statsHHs.add("13");
		statsHHs.add("14");
		statsHHs.add("15");
		statsHHs.add("16");
		statsHHs.add("17");
		statsHHs.add("18");
		statsHHs.add("19");
		statsHHs.add("20");
		statsHHs.add("21");
		statsHHs.add("22");
		statsHHs.add("23");
		String fileName = "";
		FileUtils.mkFolder(recoveryLogPath);
		File file = new File(recoveryLogPath + "/");
		String statsDttm = "";
		
		if (!file.exists()) {
			file.mkdir();
		}
		File [] fileArr = file.listFiles();
		
		if((fileArr == null)||(fileArr.length==0)) {
			logger.info("STATS_DTTM IS NONE");
			return;
		}
		String filePrefix = "FIXDAY";
		boolean fileReadSuc = true;
		
		for (File readFile : fileArr) {
			if (readFile != null) {
				if (readFile.exists()) {
					fileName = readFile.getName();
					// 파일 접두사가 없는 경우 
					if(fileName.indexOf(filePrefix) == -1) {
						fileReadSuc = false;
						continue;
					}
					// 파일 접미사가 _ing 인경우 
					if(fileName.indexOf("_ing")>0) {
						fileReadSuc = false;
						continue;
					}
					// 파일 중복처리를 막기 위해 해당 파일의 이름 변경한다 . 
					long millis = Calendar.getInstance().getTimeInMillis();
					String fileReName = readFile.getAbsolutePath()+"_"+millis+"_ing";
					File fileTmp = new File( fileReName );
					readFile.renameTo( fileTmp );
					
					if (fileReadSuc) {
						try {
							BufferedReader reader = new BufferedReader(new FileReader(fileTmp));
							while ((statsDttm = reader.readLine()) != null) {
								
								if ("".equals(statsDttm.trim())) {
									logger.info("No ReadLine");
									break;
								}
								
								//해당 파일의 날짜를 받아서 해당 날짜의 데이터를 마이그레이션을 한다.
								Map<String , Object> param = new HashMap<String , Object>();
								param.put("STATS_DTTM", statsDttm);
								logger.info("STATS_DTTM - {}", statsDttm);
								for (String statsHH : statsHHs) {
									logger.info("STATS_HH - {}",statsHH);
									param.put("STATS_HH", statsHH);
									clickHouseDao.migrationClickHouseSumData(param);									
								}
							}
							logger.info("TASK MIGRATION CLICKCONVSUM END");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							logger.error("FILE READER FAIL");
						} 
					} else {
						logger.info("FILE NAME IS NOT EXSIT");
						return;
					}
				}
			}
		}
	}
	
	public void uniqClickDataMigration() {
		int procRaw = 2000;
		int firstEnd = 525000000;		// 2021-12-09 제외한 MAX 값
		int firstLimit = (int) Math.ceil(firstEnd / procRaw);
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		long resutTime = 0;
		Map<String, Object> param = new HashMap<>();
		
		List<Map<String , Object>> result = null;
		
		logger.info("uniq click migration START");
		param.put("MODE", "normal");
		for (int i=1; i<=firstLimit; i++) {
			param.put("START_NO", (i-1) * procRaw + 1);
			param.put("END_NO", i * procRaw);
			// 기존 빌링 디비의 유니크 클릭 데이터를 가지고온다. 
			result = selectDao.getUniqueClickData(param);
			logger.info("result size - {}",result.size());
			
			clickHouseDao.insertUniqueClickData(result);
			if (i%500 == 0) {
				logger.info("uniq click migration success NO : {}", i * procRaw);
			}
		}
	
		endTime = System.currentTimeMillis();
		resutTime = endTime - startTime;
		logger.info("uniq click migration END (TBRT) : {}", resutTime + "(ms)");
	}
	/*
	* 매일 새벽 2시 전일자 mob_grp_sum_stats 테이블 광고주 기준으로 재 그룹핑 해서 insert
	*/
	public void adverGrpSumStats(){
		logger.info("MOB_ADVER_GRP_SUM_STATS STARTS");
		try {
			InetAddress ip = InetAddress.getLocalHost();
			String serverIp = ip.getHostAddress();
			String batchIp = "192.168.2.70";
			logger.info("Runing Server IP - {}", serverIp);
			if (!serverIp.equals(batchIp)) {
				logger.info("It's not Batch Server - {}", serverIp);
				return;
			}
		} catch (Exception e) {
			logger.error( "ip Host Exception - {} ", e);
		}
		clickHouseDao.adverGrpSumStats();
		logger.info("MOB_ADVER_GRP_SUM_STATS END");
	}
	/*
	 * mob_adver_grp_sum_stats 마이그레이션
	 */
	public void adverGrpSumStatsMigration() throws InterruptedException {
		int dateDiff = 316;
		if (stats) {
			for (int i = 2 ; i <= dateDiff ; i ++) {
				Map<String ,Object> param = new HashMap<String, Object>();
				param.put("diffDay", i);
				clickHouseDao.adverGrpSumStatsMigration(param);
				Thread.sleep(10000);
			}
			stats = false;
			logger.info("End of Migration");
		}
	}

}
