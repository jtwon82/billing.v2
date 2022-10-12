package com.mobon.billing.schduler;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobon.billing.service.DataRecoveryService;
import com.mobon.billing.vo.DataBaseVo;

import net.sf.json.JSONObject;

/**
 * @FileName : DataRecovery.java
 * @Date : 2021. 2. 10.
 * @작성자 : dhlim
 */

@Component
public class DataRecovery {
	Logger log = LoggerFactory.getLogger(DataRecovery.class);
	
	@Value("${recovery.file.path}")
	private String filePath;
	
	@Autowired
	private DataRecoveryService  dataRecoveryService;
	
	private String fileSuffix =  ".json";
	
	/**
	 * @MethodName : mongoToMariaDB
	 * @Date : 2021. 2. 10.
	 * @작성자 :  dhlim
	 * @기능  : 스케줄 메인 method
	 */
	public void mongoToMariaDB() {		
		
		File file = this.FileReader();
		
		if (file == null) {
			log.info("#### JSON File is not Exit ####");
			return;
		}
		Map<String , DataBaseVo> result = this.JsonReader(file.getName());
		
		Boolean checkResult = dataRecoveryService.groupingData(result , file.getName());
		
		if (checkResult) {
			log.info("success DataRecovery");
			this.FileDelete();
		}
	}
	
	
	
	/**
	 * @MethodName : FileDelete
	 * @Date : 2021. 2. 10.
	 * @작성자 :  dhlim
	 * @기능  : 읽은 파일 삭제 
	 */
	private void FileDelete() {
		File dir = new File(filePath);
		
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (!file.isFile()) {
					continue;
				}
				if (file.getName().contains(fileSuffix)) {					
					log.info("#####Json Delete fileName ##### " + file.getName());					
					file.delete();
					return ;
				}
			}
		} catch (Exception e) {
			log.error("#####Json fileName Data Error #####" + e);
		}		
	}



	/**
	 * @MethodName : FileReader
	 * @Date : 2021. 2. 10.
	 * @작성자 :  dhlim
	 * @기능  :  csvFile 명 Reader
	 */
	public File FileReader() {
		File dir = new File(filePath);
		
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (!file.isFile()) {
					continue;
				}
				if (file.getName().contains(fileSuffix)) {					
					log.info("#####Json fileName ##### " + file.getName());					
					return file;
				}
			}
		} catch (Exception e) {
			log.error("#####Json fileName Data Error #####" + e);
		}
		
		
		return null;
	}
	
	/**
	 * @MethodName : CsvReader
	 * @Date : 2021. 2. 10.
	 * @작성자 :  dhlim
	 * @기능  :  csvReader
	 */
	public Map<String , DataBaseVo> JsonReader(String fileName) {
		JSONParser parser = new JSONParser();
		Map<String , DataBaseVo> result = new HashMap<String, DataBaseVo>();
		try {
			JSONArray array = (JSONArray) parser.parse(new java.io.FileReader(filePath+fileName));  
			for (int i = 0; i < array.size(); i++) {
				DataBaseVo vo = new ObjectMapper().readValue(array.get(i).toString(), DataBaseVo.class);
				result.put(String.valueOf(i), vo);				
			}
			return result;
		}catch (Exception e) {
			log.error("#####Data Error #####" + e);
		}
		
		return null;
	}
	
	
	
}
