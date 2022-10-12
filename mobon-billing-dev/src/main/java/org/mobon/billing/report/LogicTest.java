package org.mobon.billing.report;

public class LogicTest {

	public static void main(String[] args) {

		String []arr= new String[] {"01","02","91"};
		System.out.println(arr.length);
		
		for(int i=0; i<100; i++)
			 System.out.println( (arr)[ (int) Math.round(Math.random() * (arr.length-1)) ] );

	}

}
