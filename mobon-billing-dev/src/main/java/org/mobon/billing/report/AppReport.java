package org.mobon.billing.report;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@ImportResource({ "classpath:batch-config.xml", "classpath:bean-config.xml" })
@ComponentScan(basePackages = {"com.mobon.billing","org.mobon.billing"})
@EnableScheduling
public class AppReport implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(AppReport.class);

    
	public static void main(String[] args) {
		logger.debug("AppReport");
		new SpringApplicationBuilder().sources(AppReport.class).web(WebApplicationType.SERVLET).run(args);
	}
	
	@Override
	public void run(String... strings) throws Exception {
        logger.info("start ");
    }
	
	class HttpConnectionExample{
		String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36";
		
		private void sendGet(String targetUrl) throws Exception {

			URL url = new URL(targetUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("GET"); // optional default is GET
			con.setRequestProperty("User-Agent", user_agent); // add request header

			int responseCode = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println("HTTP 응답 코드 : " + responseCode);
			System.out.println("HTTP body : " + response.toString());
		}

		// HTTP POST request
		private void sendPost(String targetUrl, String parameters) throws Exception {

			URL url = new URL(targetUrl);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

			con.setRequestMethod("POST"); // HTTP POST 메소드 설정
			con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
			con.setRequestProperty("Accept", "text/plain");
			con.setRequestProperty("User-Agent", user_agent);
			con.setDoOutput(true); // POST 파라미터 전달을 위한 설정

			// Send post request
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(parameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println("HTTP 응답 코드 : " + responseCode);
			System.out.println("HTTP body : " + response.toString());

		}
	}
}
