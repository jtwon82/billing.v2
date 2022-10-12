package org.mobon.billing.report;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mobon.billing.util.FileUtils;

import net.sf.json.JSONObject;

public class LogParser {
	private static final Logger	logger	= LoggerFactory.getLogger(LogParser.class);

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
					
					try {

						JSONObject b= JSONObject.fromObject(row[2]);
						
						String keyVal= String.format("%s,%s,%s,%s,%s"
								, b.get("key")
								, b.get("ip")
								, b.get("au_id")
								, b.get("browserCode")
								, b.get("browserCodeVersion")
								);
						System.out.println( keyVal );
						
						FileUtils.appendStringToFile(args[0], ".output.log", keyVal);
						
						fileRowCnt++;
					}catch(Exception e) {
						System.out.println(row[0]);
						e.printStackTrace();
					}
					
				} // end file read while
			}catch(Exception e) {
				System.out.println( e );
			}
		}
	}

}
