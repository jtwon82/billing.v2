package com.adgather.resource.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adgather.constants.GlobalConstants;
//import com.adgather.dao.dataMapper.DataMapperDao;
//import com.adgather.resource.db.DAOManager;
//import com.adgather.servlet.ConfigServlet;
//import com.adgather.util.NetworkUtils;
import com.adgather.util.PropertyHandler;

/**
 * property 값은 모두 trim 된 상태로 보관한다.
 */
public class PropertyLoader {
	private static final Logger logger = LoggerFactory.getLogger(PropertyLoader.class);
	private static PropertyLoader instance = null;
	private static Properties properties = new Properties();
	private final String sysid = "SYSMBN"; // SYSTEM MOBION

	public static synchronized PropertyLoader getInstance() {
		if (instance == null) {
			instance = new PropertyLoader();
		}
		return instance;
	}

	private static void _loadPropertiesUsingCommonsConfiguration(Properties props, File file)
			throws ConfigurationException {
		final PropertiesConfiguration config = new PropertiesConfiguration();
		config.setDelimiterParsingDisabled(true);
		config.setFile(file);
		config.load();
		final Iterator<String> it = config.getKeys();
		while (it.hasNext()) {
			final String sKey = it.next();
			PropertyHandler.properties.put(sKey, config.getString(sKey));
		}
	}

	public void loadProperties(String[] paramArrayOfString) {
		final Properties props = new Properties();
		final ArrayList<String> lstFilename = new ArrayList<String>();
		for (String sPath : paramArrayOfString) {
			File file = new File(sPath);
			try {
				_loadPropertiesUsingCommonsConfiguration(props, file);
				lstFilename.add(file.getName());
				logger.info("[PropertyLoader] {} is loaded.", sPath);
			} catch (Exception e) {
				logger.error("[PropertyLoader] Can't load {}", sPath);
			}
		}
		if (lstFilename.size() == paramArrayOfString.length) { // 파일 로딩이 모두 성공하면, 기존 properties 를 대체한다.
			properties = props;
		}

		// 프로퍼티에서 값을 가져온 후 DB에서 값을 가져온다.
//		try {
//			if (!"N".equals(PropertyHandler.getProperty("DEFAULT_PROPERTIES","Y")) && !ConfigServlet.allStopShoppul) {
//				Map<String, Object> hmap = new HashMap<String, Object>();
//				hmap.put("sysid", sysid);
//				hmap.put("cdid", lstFilename);
//				DataMapperDao dao = (DataMapperDao) DAOManager.getDAO(GlobalConstants.DREAMDB, DataMapperDao.class);
//				ArrayList<Map<String, String>> properyList = dao.selectConfigDtl(hmap);
//				for (int i = 0; i < properyList.size(); i++) {
//					Map<String, String> map = properyList.get(i);
//					ConfigServlet.property.put(map.get("cddetailid"), map.get("cdkey"));
//					logger.debug("[PropertyLoader-DB] " + map.get("cdid") + "###" + map.get("cddetailid") + " is "
//							+ map.get("cdkey"));
//				}
//			}
//		} catch (Exception e) {
//			logger.error("[PropertyLoader-DB] Can't load database");
//		}
	}

	public Properties getProperties() {
		return PropertyLoader.properties;
	}

	public static void main(String args[]) {
		PropertyLoader.getInstance().loadProperties(new String[]{"C:/workset/workspace_consumer/mobon-billing-v1/mobon-billing-resource/src/main/resources-realSDK/mongo_mobon.properties"});
		
		System.out.println(PropertyHandler.getString("MONGO_KAKAO_USER"));
	}
}
