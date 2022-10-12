package org.mobon.billing.report;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.mobon.billing.report.WebPlatformApiTest.Root;
import org.mobon.billing.report.WebPlatformApiTest.Row;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mobon.billing.dev.service.WorkQueue;
import com.mobon.billing.util.FileUtils;

public class WebPlatformApiTest {

	public static void test2() {
		WorkQueue wq= new WorkQueue("WorkQueue", 2);
		
		String filename= "C:\\workset\\1sender\\MEDIA_ID.log";
		File file_Tmp = new File( filename );
		BufferedReader fr=null;
		
		if (file_Tmp.isFile()) {
			try {
				fr = new BufferedReader(new FileReader(file_Tmp));
				String lineData, mediaId, topic, lineDataSub;
				
				while ((lineData = fr.readLine()) != null) {
					if ("".equals(lineData.trim())) continue;
					
					String [] row = lineData.split("\t");
					mediaId= row[0];
					
					wq.execute(new Printer(new RestTemplate(), "20210323", mediaId));
					Thread.sleep(100);
				}
			} catch(Exception e) {
				System.out.println( e );
			}
		}
	}

	
	public static void main(String[] args) {

	    RestTemplate restTemplate= new RestTemplate();
	    
	    String filename= "C:\\workset\\1sender\\MEDIA_ID.log";
	    int sdate= 20210301;
	    
	    for(int curDate=sdate; curDate<=20210431; curDate++) {
//	    	String Url= "http://10.251.0.67:11080/api/dspt/admin/stats/mediaTotal?sDate="+curDate+"&eDate="+curDate
//		    		+"&rowCnt=10000&platformType=00&productType=00&adGubun=00&exYn=00&sspYn=00&nativeYn=00&serviceType=00&appTrackerYn=00&mediaSearchKeyword=";
	    	String Url= "http://10.251.0.67:11080/api/dspt/admin/stats/mediaTotal?sDate="+curDate+"&eDate="+curDate
	    			+"&rowCnt=10000&platformType=00&productType=03&adGubun=00&exYn=00&sspYn=00&nativeYn=00&serviceType=00&appTrackerYn=00";
	    	
			try {
				String response = restTemplate.getForObject(Url, String.class);
				
				JsonObject document= new JsonParser().parse(response).getAsJsonObject();
				
				Gson gson= new Gson();
				Root root= gson.fromJson(document, Root.class);
				
			    for(Row i: root.contents) {
			    	String keyVal= String.format("%s	%s	%s	api", curDate, i.mediaId, i.exPoint);
					FileUtils.appendStringToFile(filename, String.format("%s", ".output.log"), keyVal);
			    }
			}catch(Exception e) {
				System.out.println( e );
			}
	    }
	}
	
	class Root{
		public String MEDIA_ID;
		public List<Row> contents;
	}
	class Row{
		public String currentDttm;
		public String mediaId;
		public String exPoint;
	}
}
class Printer implements Runnable {

	RestTemplate restTemplate;
    private final String s;
    private final String sdate;

    public Printer(RestTemplate t, String sdate, String s) {
    	this.restTemplate= t;
        this.s = s;
        this.sdate = sdate;
    }

    @Override
    public void run() {
    	
    	String filename= "C:\\workset\\1sender\\MEDIA_ID.log";
    	String Url= "http://10.251.0.67:11080/api/dspt/admin/stats/mediaTotal?sDate="+sdate+"&eDate="+sdate
    			+"&platformType=00&productType=00&adGubun=00&exYn=00&sspYn=00&nativeYn=00&serviceType=00&appTrackerYn=00&mediaSearchKeyword="+ s;
		try {
			String response= restTemplate.getForObject(Url, String.class);
			JsonObject document= new JsonParser().parse(response).getAsJsonObject();
			
			Gson gson= new Gson();
			Root root= gson.fromJson(document, Root.class);
			
		    for(Row i: root.contents) {
		    	if(i.mediaId.equals("total")) {
			    	String keyVal= String.format("%s	%s	%s", sdate, s, i.exPoint);
					FileUtils.appendStringToFile(filename, ".output.log", keyVal);
		    	}
		    }
		}catch(Exception e) {
			System.out.println( e );
		}
    }
}