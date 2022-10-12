package org.mobon.billing.report;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mobon.billing.util.FileUtils;

import net.sf.json.JSONObject;

public class LogParserConversion {
	private static final Logger	logger	= LoggerFactory.getLogger(LogParserConversion.class);

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
					
					String keyVal= String.format("%s	%s", logInfo, org.toString());
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
