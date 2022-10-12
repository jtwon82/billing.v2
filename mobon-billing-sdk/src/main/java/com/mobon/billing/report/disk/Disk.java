package com.mobon.billing.report.disk;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import net.sf.ehcache.Element;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.mobon.billing.report.config.CacheConfig;
import com.mobon.billing.report.service.dao.PackageDao;

@Configuration
@Service
public class Disk {

	private static final Logger	logger	= LoggerFactory.getLogger(Disk.class);
	
	@Autowired
	private PackageDao packageDao;
	
	Queue<Map> queue = new LinkedList<Map>();
	
	public void write(JSONObject appInfo) {
		write(appInfo, ".");
	}
	
	public void write(JSONObject appInfo, String root) {
//		JSONObject appInfo = JSONObject.fromObject(message);
//		String pkg = appInfo.getString("APP_PKG_NM");
		String adid = appInfo.getString("ADID");

//		System.out.println(adid);
//		System.out.println(pkg);
		Directory dir = new Directory();	
		dir.mkdir(adid, root);
		
//		System.out.println(dir);
		
		String fname = dir.realPath+File.separator+adid;
//		System.out.println(fname);
		
		BufferedWriter bw = null;
		FileWriter fw = null;
		File file = new File(fname);
				
		try {
			boolean isExist = file.exists();
//			logger.info(isExist + " : " + fname);
			if(isExist) {
				logger.info(adid + " - file existent");
				fw = new FileWriter(fname);
				bw = new BufferedWriter(fw);
				bw.write(appInfo.toString());
			} else {
				logger.info(adid + " - file nonexistentt");
				fw = new FileWriter(fname);
				bw = new BufferedWriter(fw);
				bw.write(appInfo.toString());
			}
			
			if(!isExist) {
				
//			if(true) {
//				IndexFiles.index(fname);
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("TRML_UNQ_VAL", adid); // 단말기고유값
				parameters.put("OS_TP_CODE", appInfo.getString("OS")); // OS구분코드
				parameters.put("REG_USER_ID", ""); // 등록사용자ID
				parameters.put("REG_DTTM", new Date()); // 등록일자
				parameters.put("ALT_USER_ID", ""); // 수정사용자ID
				parameters.put("ALT_DTTM", new Date()); // 수정일자
				
//				packageDao.insertAPP_INFO(null);
				packageDao.insertAPP_USER_TRML_INFO(parameters);
				
//				logger.info("packageDao.selectAppInfo() : " + packageDao.selectAppInfo());
				JSONArray rows = appInfo.getJSONArray("PACKAGE");
				
				Element e = CacheConfig.getCachePackage().get("package");
				Map _map = null;
				if(e != null) {
					_map = (Map) e.getObjectValue();
				} else {
					_map = new HashMap();
				}
				for(int i=0; i<rows.size(); i++) {
					JSONObject row = rows.getJSONObject(i);
					String key = String.valueOf(row.keys().next());
					if(CacheConfig.getCachePackageFinished().get(appInfo.getString("OS")+"_"+key) == null) {
						_map.put(appInfo.getString("OS")+"_"+key, row.getString(key));
					}
				}
				CacheConfig.getCachePackage().put(new Element("package", _map));
				
				if(CacheConfig.getCacheCount().get("count") != null) {
//					System.out.println(":: " + CacheConfig.getCacheCount().get("count").getObjectValue());
					int count = (int) CacheConfig.getCacheCount().get("count").getObjectValue();
					CacheConfig.getCacheCount().put(new Element("count", count+appInfo.getJSONArray("PACKAGE").size()));
				} else {
					CacheConfig.getCacheCount().put(new Element("count", appInfo.getJSONArray("PACKAGE").size()));
				}
			}
			
			if(CacheConfig.getCacheADID().get(adid) == null) {
				CacheConfig.getCacheADID().put(new Element(adid, new Element(DateUtils.getToDayYYMMDD(), true)));
			}
			
//			System.out.println("Done");
			
			
			
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				if (bw != null) bw.close();
				if (fw != null) fw.close();
			} catch (IOException ex) {
				logger.error(ex.getMessage(), ex);
			}
		}
//		cacheManager.getCache("manager").
	}
	
	public void cacheLog() {
		
	}
	
	public void read() {
		
	}
	
	public static void main(String args[]) {
		
//		JSONObject appInfo = JSONObject.fromObject("{'ADID':'182720859f-45a4-4800-cdad-7za911cffbe5e888b1423','PACKAGE':[{'com.croquis.zigzag':'20180709'}],'OS':'ao'}");
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter("test.txt");
			bw = new BufferedWriter(fw);
			bw.write("1wwwww");
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
				
	}
}
