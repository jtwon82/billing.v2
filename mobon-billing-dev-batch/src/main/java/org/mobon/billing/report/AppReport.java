//package org.mobon.billing.report;
//
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//import javax.net.ssl.HttpsURLConnection;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.WebApplicationType;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.context.annotation.ComponentScan;
//
//import com.adgather.util.old.StringUtils;

//@SpringBootApplication
//@ImportResource({ "classpath:batch-config.xml", "classpath:bean-config.xml" })
//@ComponentScan(basePackages = "com.mobon.billing")
//
//public class AppReport implements CommandLineRunner {
//	private static final Logger logger = LoggerFactory.getLogger(AppReport.class);
//
//	public static void main(String[] args) {
//		logger.debug("AppReport");
//		new SpringApplicationBuilder().sources(AppReport.class).web(WebApplicationType.SERVLET).run(args);
//	}
//
//	@Override
//	public void run(String... args) throws Exception {
//		// TODO Auto-generated method stub
//		
//	}
//}
