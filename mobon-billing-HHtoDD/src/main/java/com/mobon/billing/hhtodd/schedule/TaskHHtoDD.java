package com.mobon.billing.hhtodd.schedule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mobon.billing.util.TimeToLiveCollection;
import com.mobon.code.MapUtils;

@Component
public class TaskHHtoDD {

	private static final Logger		logger		= LoggerFactory.getLogger(TaskHHtoDD.class);

	public static final String	NAMESPACE	= "hHtoDDMapper";

	@Resource (name = "sqlSessionTemplateBilling")
	private SqlSessionTemplate sqlSessionTemplateBilling;
	
	@Autowired
	private WorkQueue				workQueue;

	private static TimeToLiveCollection	workingKey	= new TimeToLiveCollection();
	
	private static int				threadCnt	= 0;
	public static boolean			hhtoddRuning= false;

	@Value("${log.path}")
	private String logPath;
	
	public static void setThreadCnt(int threadCnt) {
		TaskHHtoDD.threadCnt = threadCnt;
	}
	public static synchronized void increaseThreadCnt() {
		TaskHHtoDD.threadCnt++;
	}
	public static synchronized void decreaseThreadCnt() {
		TaskHHtoDD.threadCnt--;
	}
	
//	@Scheduled(fixedDelay = 1000*60*5)	// 5m
	public void runBatch() {
		if(hhtoddRuning) return;
		logger.info("DD== START 5minute batch");
		
		{	// 임의로 지난날짜의 일자테이블을 보정할경우.
			String fileName_prepix = "FIXDAY";
			File file  =  new File (logPath +"../");
			try {
				File [] fileArr = file.listFiles();
				for (File readFile : fileArr) {
					if (readFile != null && readFile.exists()) {
						// 파일 이름을 가져옴
						String fileName = readFile.getName(); 
						// 파일 이름에 데이타 파일 프리픽스가 있을 경우만 처리
						if(fileName.indexOf(fileName_prepix) == -1) {
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
							if (lineData.length() < 8) {
								continue;
							}
							String []lineDatas= lineData.split(",");
							//sumObjectMapper.appendYmdMapData(MapUtils.map("STATS_DTTM",lineDatas[0]).map("ADVRTS_TP_CODE", lineDatas[1]).getMap());
							workQueue.execute(MapUtils.map("STATS_DTTM",lineDatas[0]).map("ADVRTS_TP_CODE", lineDatas[1]).getMap());
							logger.info("DD== workQueue appended {}", lineDatas);
						}
					}
				}
			} catch(Exception e) {
				logger.error("", e);
			} finally {
				file=null;
			}
			
			List<Map> listTmp2= sqlSessionTemplateBilling.selectList(String.format("%s.%s", NAMESPACE, "sql_select_mod_date_dd"));
			for(Map row:listTmp2) {
//				sumObjectMapper.appendYmdMapData(row);
				workQueue.execute(row);
				logger.info("DD== workQueue appended2 {}", row);
			}
		}
		
//		workQueue.execute(new RetryTaskerV3());
	}
//	private class RetryTaskerV3 implements Runnable {
//
//		private RetryTaskerV3() {
//		}
//
//		public void run() {
//			try {
//				hHtoDDDao.transectionRuningDDtoDD();
//			}catch(Exception e) {
//				logger.error("err", e);
//			}finally {
//			}
//		}
//	}

//	public boolean chkingServerXXX(boolean isSkip) {
//		boolean result = true;
//		List<String> ipList = Arrays.asList(mobonRealServerList.split(","));
//        for (String ip : ipList) {
//
//        	Map<String, String> param = new HashMap();
//        	param.put("url", ip);
//        	String response = null;
//        	try {
//            	response = restTemplate.getForObject("http://{url}/cronwork/monitoring/kafka_error_retrycnt.txt", String.class, param);
//            } catch (Exception e) {
//                logger.error("err ", e);
//                if(isSkip) {
//                	response="0";
//                } else {
//	                result = false;
//	                break;
//                }
//            }
//            int retrycnt = Integer.valueOf(response.trim());
//            if( retrycnt > wasRetryLogCnt ) {
//            	logger.info("param - {}, response - {}", param, response);
//            	result = false;
//            	break;
//            }
//            
//            logger.debug("ip - {}, cnt - {}", ip, retrycnt );
//            
//        }
//        logger.info("chkingServer - {}", result);
//        return result;
//	}
	
//	public boolean chkingKafkaXXX() {
//		boolean result = false;
//		try {
//			String response = restTemplate.getForObject(kafkaGroupSummeryUrl, String.class);
//			
//			JsonObject root = new JsonParser().parse(response).getAsJsonObject();
//			Gson gson = new Gson();
//			for (Entry<String, JsonElement> entry : root.entrySet()) {
//			    KafkaGroupSummary summery = gson.fromJson(entry.getValue(), KafkaGroupSummary.class);
//			    logger.debug("lag - {}, entry - {}", summery.getTotalLag(), entry.getKey() );
//			    
//			    if( "ClickViewData".equals(entry.getKey()) ) {
//				    if( summery.getTotalLag() < consumerTotallagCnt ) {
//				    	logger.info("summery - {}", summery);
//				    	result = true;
//				    	break;
//				    }
//			    }
//			}
//		}catch(Exception e) {
//			logger.error("err ", e);
//			result = false;
//		}
//		logger.info("chkingKafka - {}", result);
//		return result;
//	}
//	
//	public void excuteXXX() {
//		logger.info(">> START TaskHHtoDD execute THREAD COUNT - {}", threadCnt);
//		
//		boolean isSkip = false;
//		if( Integer.parseInt(DateUtils.getDate("mm"))>45 ) {
//			isSkip = true;
//		}
//		
//		if( ! chkingServer(isSkip) ) {
//			return;
//		}
//		
//		if( ! chkingKafka() ) {
//			return;
//		}
//		
//		if( workingKey.contains("main") ) {
//			return;
//		}
//
//		ArrayList<Map> list = hHtoDDDao.selectGroupKey();
//		String [][] group_key = new String [list.size()][];
//		int i=0;
//		for( Map row : list ) {
//			logger.debug("row - {}", row);
//			group_key[i++] = new String [] { row.get("CODE_VAL").toString(), row.get("w").toString(), row.get("ymd").toString(), row.get("hh").toString() };
//		}
//		
//		workingKey.add("main", 1);
//		
//		for( int rTime = 48; rTime>0; rTime-- ) {
//			for (String[] group1 : group_key) {
//				
//				String [] group= new String[] {group1[0], group1[1],"",""};
//				// 24시간전부터 2시간전까지
//				Date rDate = new Date();
//				rDate.setTime( ( new Date().getTime() + (1000L*60*60* rTime * (-1)) ) );
//				group[2] = DateUtils.getDate("yyyyMMdd", rDate);
//				group[3] = DateUtils.getDate("HH", rDate);
//				String _id = Arrays.asList(group).toString();
//				
//				Map map = hHtoDDDao.selectHistory(group);
//				if( map == null ) {
//					if ( workingKey.contains(_id) ) {
//						logger.info("workingKey.contains - {}", _id);
//						
//					} else {
//						logger.info("_id - {}", _id);
//						
//						workingKey.add( _id, 3 );
//						workQueue.execute(new TaskData(G.HHtoDD, group, null));
//					}
//				} else {
//					logger.info("selectHistory map is not null - {}", map);
//				}
//			}
//		}
//		workingKey.remove("main");
//	}
//	
//	public void mongoToMariaV3XXX(TaskData taskData) {
//
//		boolean result = false;
//		
//		increaseThreadCnt();
//		
//		logger.info("retryCnt - {}, _id - {}", taskData.getRetryCnt(), taskData.getId());
//		try {
//			if ( taskData.increaseRetryCnt() < retryConfig.maxRetryCount ) {
//				result = hHtoDDMariaDB.intoMariaHHtoDDMariaDBV3(taskData.getGroup(), taskData.getFiltering(), false);
//			} else {
//				result = hHtoDDMariaDB.intoMariaHHtoDDMariaDBV3(taskData.getGroup(), taskData.getFiltering(), true);
//			}
//		} catch (Exception e) {
//			logger.error("err msg - {}, _id - {}", e, taskData.getId());
//		}
//		
//		decreaseThreadCnt();
//		
//		if (result) {
//			int i=3;
//			while(!workingKey.remove( taskData.getId() )) {
//				if(--i<0) {
//					logger.error("while(!workingKey.remove( _id )) 1 _id - {}", taskData.getId());
//					break;
//				}
//			}
//			
//			hHtoDDDao.insertHistory(taskData.getGroup());
//			
//			logger.debug("taskData.getGroup() - {}", Arrays.asList(taskData.getGroup()).toString() );
//			logger.info("succ offsetModifyV3 _id - {}, retryCnt - {}", taskData.getId(), taskData.getRetryCnt());
//			
//		} else {
//			workQueue.execute(taskData);
//			logger.info("retry getGroup {}", taskData.getGroup());
//		}
//	}
	
}
