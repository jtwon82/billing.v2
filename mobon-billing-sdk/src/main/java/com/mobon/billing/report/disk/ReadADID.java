package com.mobon.billing.report.disk;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;

import net.sf.json.JSONObject;

public class ReadADID {

	public class ModifiedDate implements Comparator	{
		public int compare(Object o1, Object o2) {
			File f1 = (File)o1;
			File f2 = (File)o2;
			if (f1.lastModified() > f2.lastModified())
				return -1;

			if (f1.lastModified() == f2.lastModified())
				return 0;

			return 1;
		}
	}
	
	public void read(String path, IADID adid) throws IOException {
		File f = new File(path);
		File[] files = f.listFiles();
		if(files != null) {
			Arrays.sort(files, new ModifiedDate());
			for(int i=0; i<files.length; i++) {
				File file = files[i];
				if(file.isDirectory()) {
					String _path = path+File.separator+file.getName();
//					System.out.println(_path);
					read(_path, adid);
				} else {
					adid.execute(readPackage(file));
				}
			}
		}
	}
	
	public interface IADID {
		public void execute(JSONObject adid);
	}
	
	
	public JSONObject readPackage(File file) throws IOException {
		int i;
		String str;
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(file));
			StringBuffer buffer = new StringBuffer(); 
			byte[] b = new byte[4096]; 
			while( (i = is.read(b)) != -1) { 
				buffer.append(new String(b, 0, i)); 
			} 
			str = buffer.toString();
		} finally {
			if(is != null) {
				is.close();
			}
		}
		return JSONObject.fromObject(str);
	}
	
	public static void main(String args[]) {
		try {
			new ReadADID().read("../mobon-billing-sdk/data", new IADID() {
				@Override
				public void execute(JSONObject adid) {
					// TODO Auto-generated method stub
					System.out.println(adid);
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
