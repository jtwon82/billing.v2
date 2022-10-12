package org.mobon.billing.report;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.mobon.billing.util.FileUtils;

import net.sf.json.JSONObject;

public class LogParserSvctpcode {

	public static void main(String[] args) {
		File file_Tmp = new File( args[0] );
		BufferedReader fr=null;
		if (file_Tmp.isFile()) {
			try {
				fr = new BufferedReader(new FileReader(file_Tmp));
				String lineData;
				while ((lineData = fr.readLine()) != null) {
					if ("".equals(lineData.trim()))continue;
					if (lineData.length() < 41)continue;
					
					String [] row= lineData.split("\t");
					String data= row[2];
					
					JSONObject b= JSONObject.fromObject(data);
					
					String keyVal= String.format("%s	%s	%s	%s	%s	%s	%s	%s	%s	%s	%s	%s"
						, b.get("ip"), b.get("product"), b.get("adGubun"), b.get("site_code"), b.get("media_code"), b.get("userId"), b.get("scriptUserId"), b.get("type"), b.get("ip")
						, b.get("serviceCode"), b.get("chargeCode"), b.get("noExposureYN")
					);
					
					System.out.println( keyVal );
					FileUtils.appendStringToFile(args[0], ".output.log", keyVal);
					
				}
			}catch(Exception e) {
				System.out.println( e );
			}
		}
	}

}
