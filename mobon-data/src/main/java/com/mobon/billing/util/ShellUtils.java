package com.mobon.billing.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShellUtils {

	private static final Logger logger = LoggerFactory.getLogger(ShellUtils.class);

	public static void shellCmd(String command) throws Exception {

	        Runtime runtime = Runtime.getRuntime();

	        Process process = runtime.exec(command);

	        InputStream is = process.getInputStream();

	        InputStreamReader isr = new InputStreamReader(is);

	        BufferedReader br = new BufferedReader(isr);

	        String line;

	        while((line = br.readLine()) != null) {

	        	logger.info(line);

	        }

	}
}
