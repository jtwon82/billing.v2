package com.adgather.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class KafkaFileUtils {


//	public static final void main(String []ar){
//		KafkaFileUtils.fileModify("offset", "2", "12111");
//	}
	

	/**
	 * <pre>
	 * 카프카의 파티션별 마지막 offset 값을 파일에서 읽어온다.
	 * </pre>
	 * @param
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> mongoDataLastOffset(String fileName) {

		Map<String, String> offsetMap = new HashMap<String, String>();
		String filePath = "/home/kafka/public_html/kafka_batch/"+ fileName +".json";
		File file = new File(filePath);
		if (file.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));

				String readLine = null;
				while ((readLine = br.readLine()) != null) {
					//offsetMap = (HashMap<String, String>) new Gson().fromJson(readLine, (new HashMap<String, String>()).getClass());
					if (offsetMap == null) {
						offsetMap = new HashMap<String, String>();
					}
				}
				br.close();
			} catch (IOException e) {
			}
		}
		return offsetMap;
	}
	
	/**
	 * <pre>
	 * 파일에 저장한 설정정보 읽어온다.
	 * </pre>
	 * 
	 * @param
	 * @return
	 * @throws Exception
	*/
	public static String fileInfo(String filename, String type) {
		String offset = "0";
		String filePath = "/home/kafka/public_html/kafka_batch/" + filename + ".json";

		// 저장된 파일 정보 읽기
		File file = new File(filePath);
		HashMap<String, String> map = null;
		if (file.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));

				String readLine = null;
				while ((readLine = br.readLine()) != null) {
					//map = (HashMap<String, String>) new Gson().fromJson(readLine, (new HashMap<String, String>()).getClass());
					if (map == null) {
						map = new HashMap<String, String>();
					}
					offset = map.get(type);
				}
				br.close();
			} catch (IOException e) {
			}

			if (offset == null) {
				offset = "0";
				fileModify(filename, type, offset);
			}
		}
		else {
			// 파일이 존재하지 않으면 새로 생성
			fileModify(filename, type, offset);
		}
		return offset;
	}

	/**
	 * <pre>
	 * 파일로 저장한 설정정보를 업데이트한다.
	 * </pre>
	 * 
	 * @param
	 * @return
	 * @throws Exception
	*/
	public static void fileModify(String filename, String type, String offset) {
		String filePath = "/home/kafka/public_html/kafka_batch/" + filename + ".json";

		// 저장된 파일 정보 읽어서 수정하기
		File file = new File(filePath);
		HashMap<String, String> map = null;
		BufferedWriter bw = null;
		if (file.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String readLine = null;
				while ((readLine = br.readLine()) != null) {
					//map = (HashMap<String, String>) new Gson().fromJson(readLine, (new HashMap<String, String>()).getClass());
					if (map == null) {
						map = new HashMap<String, String>();
					}
					map.put(type, offset);
				}
				br.close();

			} catch (Exception e) {
			}
		}
		else {
			map = new HashMap<String, String>();
			map.put(type, offset);
		}

		try {
			bw = new BufferedWriter(new FileWriter(filePath));
			//bw.write(new Gson().toJson(map));
			bw.flush();
			bw.close();
		} catch (Exception e) {
		}
	}
}
