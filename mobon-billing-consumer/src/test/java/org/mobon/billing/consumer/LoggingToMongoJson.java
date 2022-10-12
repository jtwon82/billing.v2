package org.mobon.billing.consumer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.adgather.constants.G;
import com.adgather.reportmodel.old.AdChargeData;
import com.adgather.reportmodel.old.DrcData;
import com.adgather.reportmodel.old.RTBDrcData;
import com.adgather.reportmodel.old.RTBReportData;
import com.adgather.reportmodel.old.ShortCutData;
import com.adgather.util.old.DateUtils;
import com.mobon.billing.model.v15.BaseCVData;
import com.mobon.billing.model.v15.ShortCutInfoData;

import net.sf.json.JSONObject;

public class LoggingToMongoJson {

	public static final void main(String []ar) {

		String logPath = "C:\\w\\jobs\\mobon1.5\\빌링검증\\0705-고클린왓스업\\";
		File[] files = new File(logPath).listFiles();

		
		for (File f : files) {
			if( f.getName().indexOf("kafka-consumer.logging.log.20180705_20180705_12")<0 ) {
				continue;
			}
			System.out.println(f.getName());

			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "euc-kr"));
			} catch (Exception e) {
				System.out.println(e);
			}
			

			File file = new File( "C:\\home\\dreamsearch\\" + String.format("%s.%s.csv", f.getName(), DateUtils.getDate("yyyy-MM-dd")) );
			Writer out = null;
			try {
//				FileWriter fileWriter = new FileWriter(file);
//				out = new PrintWriter(new BufferedWriter(fileWriter));
				
				out = new BufferedWriter(new OutputStreamWriter(
					    new FileOutputStream(file), "euc-kr"));
				
			} catch (IOException e1) {
				System.out.println(e1.getMessage());
			}
			
			String line = null;
			String topic = null;
			String msg = null;
			String className = null;
			
			try {
				while ((line = reader.readLine()) != null) {
					String[] lines = line.split("\t");
					try {
						topic = lines[1];
						msg = lines[2];
					} catch (Exception e) {
						continue;
					}

					BaseCVData data = null;
					JSONObject json = JSONObject.fromObject(msg);
					className = (String) json.get("className");
					
					if( "ClickViewData".equals(lines[1]) ) {
//						FileUtils.appendStringToFile(logPath,
//								String.format("%s.%s.json", "kafka-consumer.logging.log", DateUtils.getDate("yyyy-MM-dd")), msg);

						if ( G.ClickViewData.equals(topic) ) {
							if ( G.AdChargeData.equals(className) ) {
								AdChargeData tmpAdCharge = AdChargeData.fromHashMap(json);
								data = tmpAdCharge.toBaseCVData();
							
							} else if ( G.DrcData.equals(className) ) {
								DrcData tmpDrc = DrcData.fromHashMap(json);
								data = tmpDrc.toBaseCVData();

							} else if ( G.ShortCutData.equals(className) ) {
								ShortCutData a = ShortCutData.fromHashMap(json);
								ShortCutInfoData tmp = a.toShortCutInfoData();
								data = tmp.toBaseCVData();
								
							} else if ( G.RTBReportData.equals(className)) {
								RTBReportData record = RTBReportData.fromHashMap(json);
								data = record.toBaseCVData();

							} else if ( G.RTBDrcData.equals(className)) {
								RTBDrcData record = RTBDrcData.fromHashMap(json);
								data = record.toBaseCVData();
								
							} else {
							}
						}
						StringBuffer outtxt = new StringBuffer();
						outtxt.append(String.format("%s\t", data.yyyymmdd));
						outtxt.append(String.format("%s\t", data.hh));
						outtxt.append(String.format("%s\t", data.getDumpType()));
						outtxt.append(String.format("%s\t", data.className));
						outtxt.append(String.format("%s\t", data.platform));
						outtxt.append(String.format("%s\t", data.adGubun));
						outtxt.append(String.format("%s\t", data.scriptNo));
						outtxt.append(String.format("%s\t", data.viewCnt));
						outtxt.append(String.format("%s\t", data.viewCnt2));
						outtxt.append(String.format("%s\t", data.viewCnt3));
						outtxt.append(String.format("%s\t", data.clickCnt));
						outtxt.append(String.format("%s\t", data.point));
						outtxt.append(String.format("%s", data.mpoint));
						
						out.write( outtxt.toString() +"\n\r");
//						out.write( JSONObject.fromObject(map).toString() +"\n");
					}
				}
			} catch (Exception e) {
				System.out.println(e);
			}
			try {
				out.close();
			} catch (IOException e) {
			}
		}
		
	}
}
