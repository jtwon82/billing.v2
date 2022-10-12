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

public class LogParserAdver_sjinny {
	private static final Logger	logger	= LoggerFactory.getLogger(LogParserAdver_sjinny.class);

	public static void main(String[] args) {
		File file_Tmp = new File( args[0] );
		BufferedReader fr=null;
		if (file_Tmp.isFile()) {
			try {
				fr = new BufferedReader(new FileReader(file_Tmp));
				
				String lineData;
				int fileRowCnt=0;
				
				while ((lineData = fr.readLine()) != null) {
					if ("".equals(lineData.trim())) continue;
					if (lineData.length() < 41) continue;
					
					String [] row = lineData.split("\t");
					String i= row[0];
					String logInfo= row[1];
					String originData= row[2];

					JSONObject org= JSONObject.fromObject(originData);

//					org.remove("PNm");
//					org.remove("continueConv");
//					org.remove("longContinueConv");
//					org.remove("abTests");
//					org.remove("ordRFUrl");
//					org.remove("keyCode");
//					org.remove("pnm");
//					org.remove("inflowRoute");
//					org.remove("convKey");
//					org.remove("browserCodeVersion");
//					org.remove("ergabt");
//					org.remove("ergdetail");
//					org.remove("frameCombiKey");
//					org.remove("abusingMap");
					
					String keyVal="";
					try {
						keyVal= String.format("%s	%s	%s	%s	%s	%s", logInfo
								, org.get("key").toString()
								, org.get("sc").toString()
								, org.get("s").toString()
								, org.get("point").toString()
								, org.get("sendDate").toString()
								, org.toString());
					}catch(Exception ee) {
					}
					
					System.out.println( keyVal );
					FileUtils.appendStringToFile(args[0], ".output.log", keyVal);
					
					fileRowCnt++;
					
				} // end file read while
			}catch(Exception e) {
				System.out.println( e.getMessage() );
			}
		}
	}

}
