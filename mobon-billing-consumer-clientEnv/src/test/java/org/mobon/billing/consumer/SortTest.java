package org.mobon.billing.consumer;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import com.adgather.util.old.DateUtils;

public class SortTest {

	public static final void main(String[]ar) {
		File []files = new File( "C:\\home\\dreamsearch\\logs\\log4j" ).listFiles();
		Arrays.sort(files, new Comparator() {
			@Override
			public int compare(Object f1, Object f2) {
				File f11 = (File) f1;
				File f12 = (File) f2;
				if (f11.lastModified() > f12.lastModified()) {
					return 1;
				} else if (f11.lastModified() < f12.lastModified()) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		
		for( File f : files ) {
			System.out.println( f.lastModified() +"	"+ DateUtils.getDate("YYYY-MM-dd HH:mm:ss", new Date(f.lastModified())) +"	"+ f.getName() );
		}
	}
}
