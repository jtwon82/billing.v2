package org.mobon.billing.report;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adgather.util.old.DateUtils;
import com.mobon.billing.util.FileUtils;

import net.sf.json.JSONObject;

public class LogParserSocial {
	private static final Logger	logger	= LoggerFactory.getLogger(LogParserSocial.class);
	
	public static void main(String[] args) {
		File file_Tmp = new File( args[0] );
		BufferedReader fr=null;
		if (file_Tmp.isFile()) {
			try {
				fr = new BufferedReader(new FileReader(file_Tmp));
				
				String lineData;
				while ((lineData = fr.readLine()) != null) {
					if ("".equals(lineData.trim())) continue;
					if (lineData.length() < 41) continue;
					
					String [] row= lineData.split("	");
					HashMap b= new HashMap();
					b.put("STATS_DTTM", row[0]);
					b.put("STATS_HH", row[1]);
					b.put("PLTFOM_TP_CODE", row[2]);
					b.put("ADVRTS_PRDT_CODE", row[3]);
					b.put("ADVRTS_TP_CODE", row[4]);
					b.put("SITE_CODE", row[5]);
					b.put("MEDIA_SCRIPT_NO", row[6]);
					b.put("ITL_TP_CODE", row[7]);
					b.put("TOT_EPRS_CNT", row[8]);
					b.put("CLICK_CNT", row[9]);
					b.put("ADVRTS_AMT", row[10]);
					b.put("MEDIA_PYMNT_AMT", row[11]);
					b.put("ADVER_ID", row[12]);
					b.put("MEDIA_ID", row[13]);

					String query= String.format("INSERT INTO billing.mob_camp_media_hh_stats"
							+ "(STATS_DTTM,STATS_HH,PLTFOM_TP_CODE,ADVRTS_PRDT_CODE,ADVRTS_TP_CODE,SITE_CODE,MEDIA_SCRIPT_NO,ITL_TP_CODE"
							+ ",TOT_EPRS_CNT,CLICK_CNT,ADVRTS_AMT,MEDIA_PYMNT_AMT,ADVER_ID,MEDIA_ID, STATS_ALT_DT)"
							+ "VALUES('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s', %s);"
							, row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7], row[8], row[9], row[10], row[11], row[12], row[13], "CAST(DATE_FORMAT(NOW(),'%Y%m%d%H%i') AS INT)");
					if(row[0].indexOf("STATS")>-1) {
					}else {
						FileUtils.appendStringToFile(args[0], ".output.sql", query );
					}
					
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
