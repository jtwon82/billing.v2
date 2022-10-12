package org.mobon.billing.report;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Stream;

public class FileParallStreamTest {
	public static void main(String[] args) throws IOException {
		FileParallStreamTest.test2();
	}
	
	public static void test2() throws IOException {
		
		long startTime= System.currentTimeMillis();
		
		File fileName = new File("D:\\workset\\1\\_select_STATS_DTTM_STATS_HH_ADVRTS_PRDT_CODE_ADVRTS_TP_CODE_SUM__202106041122.csv");
        
        // Create a Stream of String type
        // Using the lines() method to read one line at a time
        // from the text file
        Stream<String> text = Files.lines(fileName.toPath());
          
        // Use StreamObject.parallel() to create parallel streams
        // Use forEach() to print the lines on the console
        text.parallel().forEach(System.out::println);
          
        // Close the Stream
        text.close();
        
        System.out.println(System.currentTimeMillis()-startTime);
	}
	public static void test1() throws IOException {

		long startTime= System.currentTimeMillis();
		
		File fileName = new File("D:\\workset\\1\\_select_STATS_DTTM_STATS_HH_ADVRTS_PRDT_CODE_ADVRTS_TP_CODE_SUM__202106041122.csv");

		// Create a List
		// Using readAllLines() read the lines of the text file
		List<String> text = Files.readAllLines(fileName.toPath());

		// Using parallelStream() to create parallel streams
		text.parallelStream().forEach(System.out::println);
		
        System.out.println(System.currentTimeMillis()-startTime);
	}
}
