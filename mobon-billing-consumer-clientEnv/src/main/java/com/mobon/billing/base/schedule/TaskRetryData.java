package com.mobon.billing.base.schedule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.adgather.constants.G;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.core.service.DataBuilder;
import com.mobon.billing.core.service.SumObjectManager;
import com.mobon.billing.core.service.WorkQueue;
import com.mobon.billing.base.handler.ContextClosedHandler;
import com.mobon.billing.model.v15.ActionLogData;
import com.mobon.billing.model.v15.AppTargetData;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.model.v15.ConvData;
import com.mobon.billing.model.v15.ExternalInfoData;
import com.mobon.billing.model.v15.NearData;
import com.mobon.billing.model.v15.ShopInfoData;
import com.mobon.billing.model.v15.ShopStatsInfoData;
import com.mobon.billing.util.ConsumerFileUtils;
import com.mobon.billing.util.FileUtils;

import net.sf.json.JSONObject;

@Component
public class TaskRetryData {

	private static final Logger logger = LoggerFactory.getLogger(TaskRetryData.class);

	@Autowired
	private SumObjectManager sumObjectManager;

	@Autowired
	@Qualifier("RetryWorkQueue")
	private WorkQueue workQueue;

	@Autowired
	private ContextClosedHandler contextClosedHandler;

	@Value("${log.path}")
	private String logPath;

	private static int threadCnt = 0;

	public static void setThreadCnt(int threadCnt) {
		TaskRetryData.threadCnt = threadCnt;
	}

	public static synchronized void increaseThreadCnt() {
		TaskRetryData.threadCnt++;
	}

	public static synchronized void decreaseThreadCnt() {
		TaskRetryData.threadCnt--;
	}

	public static int getThreadCnt() {
		return TaskRetryData.threadCnt;
	}

	public void consumerRetry() {
//		logger.info(">> START consumerRetry");

		// 종료되면 재처리 안함
		if (contextClosedHandler.closed) {
			logger.info("ContextClosedHandler closed");
			return;
		}

		workQueue.execute(new RetryTaskerV3());

	}

	private class RetryTaskerV3 implements Runnable {

		private RetryTaskerV3() {
		}

		public void run() {
			try {
				FileInputStream fileinput = null;
				BufferedReader reader = null;

				FileUtils.mkFolder( logPath +"retry/" );
				File []files = new File( logPath +"retry/" ).listFiles();

				Arrays.sort(files, new Comparator() {
					@Override
					public int compare(Object f1, Object f2) {
						File f11 = (File) f1;
						File f22 = (File) f2;
						if (f11.lastModified() > f22.lastModified()) {
							return 1;
						} else if (f11.lastModified() < f22.lastModified()) {
							return -1;
						} else {
							return 0;
						}
					}
				});
				
				logger.info("retry files.length - {}", files.length);

				if(files.length>0) {
					for( File f : files ) {
						logger.info("retry_files {}	{}	{}", f.lastModified(), new Date(f.lastModified()), f.getName() );
					}
				}

				String fail_folder = String.format("%s%s", logPath, "retryfail/" );
				FileUtils.mkFolder( fail_folder );
				
				boolean exceptionYN = false;
				int j=0;
//				for( int j=0; j<files.length; j++ ) {
				if ( files.length>0 ) {
					
					if(!files[j].isFile()){
						return ;
					}
					
					logger.info("files[j] - {}", files[j].getName());
					
					if( files[j].getName().indexOf("insertIntoError")<0 ){
						return ;
					}
					
					if( files[j].getName().indexOf("_ing")>-1 ) {
						return ;
					}
					
					String tmp_name = files[j].getAbsolutePath() + "_ing";
					File tmp_file = new File( tmp_name );
					files[j].renameTo( tmp_file );
					
					try {
						fileinput = new FileInputStream( tmp_name );
						reader = new BufferedReader(new InputStreamReader(fileinput));
						String line = null;
						String topic = null;
						String msg = null;
						
						while( (line = reader.readLine()) != null){
							String []lines = line.split("\t");
							
							try {
								topic = lines[1];
								msg = lines[2];
							}catch(Exception e) {
								logger.error("err {}", msg);
								continue;
							}
							
							
							long millis = Calendar.getInstance().getTimeInMillis();
							String writeFileName = String.format("%s_%s", "insertIntoError", DateUtils.getDate("yyyyMMdd_HHmm"), millis);
							
							JSONObject jJSONObject = null;
							try {
								jJSONObject = JSONObject.fromObject(msg);
							}catch(Exception e) {
								continue;
							}
							
							//logger.debug("jJSONObject - {}", jJSONObject);
							
							if( contextClosedHandler.closed ) {
								
								// 종료되면 바로 파일로 저장.
								ConsumerFileUtils.writeLine( logPath +"retry/", writeFileName, topic, msg);
								
								exceptionYN = false;
								
							} else {
								if( G.client_environment.equals( topic ) ) {
									try {
										BaseCVData item = BaseCVData.fromHashMap(jJSONObject);
										
										item.setPlatform(G.convertPLATFORM_CODE_BACK(item.getPlatform()));
										item.setAdGubun(G.convertTP_CODE_BACK(item.getAdGubun()));
										
										sumObjectManager.appendClientEnvironmentData(item);
										
									}catch(Exception e) {
										logger.error("err data - {}", jJSONObject.toString().substring(0,  200), e);
										ConsumerFileUtils.writeLine( logPath +"retryfail/", writeFileName, G.client_environment, jJSONObject);
									}
									
								} else if( G.click_view_adblock.equals( topic ) ) {
									try {
										BaseCVData item = BaseCVData.fromHashMap(jJSONObject);

										if(item.getAdvertiserId().length() > 30) {
											logger.error("adverId is too long = " + item.getAdvertiserId());
											return;
										}

										item.setPlatform(G.convertPLATFORM_CODE_BACK(item.getPlatform()));
										item.setAdGubun(G.convertTP_CODE_BACK(item.getAdGubun()));


										sumObjectManager.appendADBlockData(item);

									}catch(Exception e) {
										logger.error("err data - {}", jJSONObject.toString().substring(0,  200), e);
										ConsumerFileUtils.writeLine( logPath +"retryfail/", writeFileName, G.click_view_adblock, jJSONObject);
									}

								} else if( G.click_view_crossAUID.equals( topic ) ) {
									try {
										BaseCVData item = BaseCVData.fromHashMap(jJSONObject);

										if(item.getAdvertiserId().length() > 30) {
											logger.error("adverId is too long = " + item.getAdvertiserId());
											return;
										}

										item.setPlatform(G.convertPLATFORM_CODE_BACK(item.getPlatform()));
										item.setAdGubun(G.convertTP_CODE_BACK(item.getAdGubun()));


										sumObjectManager.appendCrossAUIDData(item);

									}catch(Exception e) {
										logger.error("err data - {}", jJSONObject.toString().substring(0,  200), e);
										ConsumerFileUtils.writeLine( logPath +"retryfail/", writeFileName, G.click_view_crossAUID, jJSONObject);
									}

								} else if( G.adver_client_environment.equals( topic ) ) {
									try {
										BaseCVData item = BaseCVData.fromHashMap(jJSONObject);
										
										item.setPlatform(G.convertPLATFORM_CODE_BACK(item.getPlatform()));
										item.setAdGubun(G.convertTP_CODE_BACK(item.getAdGubun()));
										
										sumObjectManager.appendAdverClientEnvironmentData(item);
										
									}catch(Exception e) {
										logger.error("err data - {}", jJSONObject.toString().substring(0,  200), e);
										ConsumerFileUtils.writeLine( logPath +"retryfail/", writeFileName, G.adver_client_environment, jJSONObject);
									}
								} else if( G.client_age_gender.equals( topic ) ) {
									try {
										BaseCVData item = BaseCVData.fromHashMap(jJSONObject);
										
										item.setPlatform(G.convertPLATFORM_CODE_BACK(item.getPlatform()));
										item.setAdGubun(G.convertTP_CODE_BACK(item.getAdGubun()));
										
										sumObjectManager.appendClientAgeGenderData(item);
										
									}catch(Exception e) {
										logger.error("err data - {}", jJSONObject.toString().substring(0,  200), e);
										ConsumerFileUtils.writeLine( logPath +"retryfail/", writeFileName, G.client_age_gender, jJSONObject);
									}
								} else if( G.camp_media_retrn_aval.equals( topic ) ) {
									try {
										BaseCVData item = BaseCVData.fromHashMap(jJSONObject);
										
										item.setPlatform(G.convertPLATFORM_CODE_BACK(item.getPlatform()));
										item.setAdGubun(G.convertTP_CODE_BACK(item.getAdGubun()));
										
										sumObjectManager.appendCampMediaRetrnAvalData(item);
										
									}catch(Exception e) {
										logger.error("err data - {}", jJSONObject.toString().substring(0,  200), e);
										ConsumerFileUtils.writeLine( logPath +"retryfail/", writeFileName, G.camp_media_retrn_aval, jJSONObject);									}
								} 
							}
						}
						 
						exceptionYN = false;
						
					}catch(Exception e) {
						
						logger.error("err ", e);
						exceptionYN = true;
						
					}finally {
						 
						reader.close();
						
						fileinput.close();
						
						
						if(!exceptionYN) {
						
							File fileD = new File(tmp_name);
							
							if(fileD.exists()&&fileD.isFile()) {
								fileD.delete();
							}
							
						} 
						
						
						if(exceptionYN) {
							
							String filePathName = tmp_file.getAbsolutePath();
							
							if(filePathName.indexOf("_ing")>0) {								
								
								File fileT  = new File (filePathName.replace("_ing",""));								
								tmp_file.renameTo(fileT);
								
							}
							
						}
						
					}
				}
				logger.info("retryOk files.length - {}", files.length);
				
			}catch(Exception e) {
				logger.error("err", e);
			}finally {
			}
		}
	}

}
