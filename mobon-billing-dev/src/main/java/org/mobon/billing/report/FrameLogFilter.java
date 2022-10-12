package org.mobon.billing.report;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adgather.util.old.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobon.billing.util.FileUtils;

public class FrameLogFilter {
	private static final Logger	logger	= LoggerFactory.getLogger(FrameLogFilter.class);

	public static void main(String[] args) {
		test2(args);
	}

	public static void test2(String[] args) {
		try {
			FileInputStream inputStream= new FileInputStream(args[0]);
			Scanner sc= new Scanner(inputStream, "UTF-8");
			int fileRowCnt=0;
			while (sc.hasNextLine()) {
				String lineData= sc.nextLine();
				if ("".equals(lineData.trim())) {
					continue;
				}
				if (lineData.length() < 41) {
					continue;
				}
				
				String [] row = lineData.split("\t");
				String topic= row[1];
				String lineDataSub= row[2];

				PollingData item= null;
				try {
					item = new ObjectMapper().readValue(lineDataSub, PollingData.class);
				} catch (IOException e) {
				}
				
				if(item!=null) {
					if(StringUtils.isNotEmpty(item.getFrameId())) {
						try {
							FileUtils.appendStringToFile(args[0], ".output.log", lineDataSub);
						} catch (IOException e) {
						}
						
					} else if(StringUtils.isNotEmpty(item.getFrameCombiKey())) {
						try {
							FileUtils.appendStringToFile(args[0], ".output.log", lineDataSub);
						} catch (IOException e) {
						}
					}
				}
				
				fileRowCnt++;

				if((fileRowCnt % 100000)==0) {
					DecimalFormat df = new DecimalFormat("#,###");
					logger.info("TOPIC FILE READCNT => {}", df.format(fileRowCnt));
				}
			

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	public static void test1(String[] args) {
		File file_Tmp = new File( args[0] );
		BufferedReader fr=null;

		if (file_Tmp.isFile()) {
			try {
				fr = new BufferedReader(new FileReader(file_Tmp), 1000 * 8192);
				
				String lineData, topic, lineDataSub;
				int fileRowCnt=0;
				
				while ((lineData = fr.readLine()) != null) {
					if ("".equals(lineData.trim())) {
						continue;
					}
					if (lineData.length() < 41) {
						continue;
					}
					
					String [] row = lineData.split("\t");
					topic= row[1];
					lineDataSub= row[2];

					PollingData item= new ObjectMapper().readValue(lineDataSub, PollingData.class);
					
					if(StringUtils.isNotEmpty(item.getFrameId())) {
						FileUtils.appendStringToFile(args[0], ".output.log", lineDataSub);
						
					} else if(StringUtils.isNotEmpty(item.getFrameCombiKey())) {
						FileUtils.appendStringToFile(args[0], ".output.log", lineDataSub);
					}
					
					fileRowCnt++;

					if((fileRowCnt % 100000)==0) {
						DecimalFormat df = new DecimalFormat("#,###");
						logger.info("TOPIC FILE READCNT => {}", df.format(fileRowCnt));
					}
				}
			}catch(Exception e) {
				System.out.println( e );
			}
		}
	}

}
