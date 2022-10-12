package org.mobon.billing.report;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mobon.billing.util.FileUtils;

import net.sf.json.JSONObject;

public class LogParser_social0331 {
	private static final Logger	logger	= LoggerFactory.getLogger(LogParser_social0331.class);

	public static void main(String[] args) {
		File file_Tmp = new File( args[0] );
		BufferedReader fr=null;
		if (file_Tmp.isFile()) {
			try {
				fr = new BufferedReader(new FileReader(file_Tmp));
				
				String lineData;
				int fileRowCnt=0;
				
				while ((lineData = fr.readLine()) != null) {

					if ("".equals(lineData.trim())) {
						continue;
					}
					
					if (lineData.length() < 41) {
						continue;
					}
					
					String [] row = lineData.split("\t");
					String map= row[1];
					String action= row[2];

					JSONObject a= JSONObject.fromObject(map);
					JSONObject b= JSONObject.fromObject(action);
					
					b.put("brDirect", "N");
					b.put("Direct", "N");
					b.put("inhour", "N");
					if(Integer.parseInt(b.get("diffClickTime").toString())<=1800 ) {
						b.put("brDirect", "Y");
						b.put("inhour", "24");
					}
					if(Integer.parseInt(b.get("diffClickTime").toString())<=(1800*6*2)) {
						b.put("Direct", "Y");
						b.put("inhour", "24");
					}
					
					String keyVal= String.format("%s	%s	%s	%s	%s	%s	%s	%s	%s	%s	%s	%s	%s	%s	%s	%s	%s	%s	%s"
							, a.get("yyyymmdd"), a.get("origin_yyyymmdd"), a.get("sendDate"), a.get("keyIp"), a.get("advertiserId"), a.get("siteCode"), a.get("adGubun"), a.get("scriptNo"), a.get("product")
							, b.get("no"), b.get("clickRegDate"), b.get("keyIp"), b.get("siteCode"), b.get("adGubun"), b.get("scriptNo"), b.get("product")
							, b.get("diffClickTime"), b.get("brDirect"), b.get("Direct"), b.get("inhour")
							);
					System.out.println( keyVal );
					FileUtils.appendStringToFile(args[0], ".output.log", keyVal);
					
					fileRowCnt++;
					
				} // end file read while
			}catch(Exception e) {
				System.out.println( e );
			}
		}
	}

}
