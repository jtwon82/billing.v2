package org.mobon.billing.report;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mobon.billing.util.FileUtils;

import net.sf.json.JSONObject;
import scala.collection.concurrent.Map;

public class LogParserConversion2 {
	private static final Logger	logger	= LoggerFactory.getLogger(LogParserConversion2.class);

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
					String logInfo= row[0];
					String originData= row[1];

					JSONObject org= JSONObject.fromObject(originData);

					org.remove("PNm");
					org.remove("continueConv");
					org.remove("longContinueConv");
					org.remove("abTests");
					org.remove("ordRFUrl");
					org.remove("keyCode");
					org.remove("pnm");
					org.remove("inflowRoute");
					org.remove("convKey");
					org.remove("browserCodeVersion");
					org.remove("ergabt");
					org.remove("ergdetail");
					org.remove("frameCombiKey");
					org.remove("abusingMap");
					
					String keyVal= String.format("%s	%s	%s	%s	%s	%s	%s	%s	%s	%s	%s	%s", logInfo
							, org.get("ordCode").toString()
							, org.get("keyIp").toString()
							, org.get("advertiserId").toString()
							, org.get("siteCode").toString()
							, org.get("scriptUserId").toString()
							, org.get("scriptNo").toString()
							, org.get("platform").toString()
							, org.get("product").toString()
							, org.get("adGubun").toString()
							, org.get("sendDate").toString()
							, org.get("osCode").toString()
							, org.get("au_id").toString()
							, org.toString());
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
