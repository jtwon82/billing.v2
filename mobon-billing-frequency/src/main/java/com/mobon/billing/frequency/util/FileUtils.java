package com.mobon.billing.frequency.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

public class FileUtils {

	public static void mkFolder(String path) {
		File dir = new File(path);
		try {
			if (!dir.exists()) { // 폴더 없으면 폴더 생성
				dir.mkdirs();
			}
		} catch (Exception e) {
		}
	}
	public static void appendStringToFile(String destDir, String writeFile, String strData) throws IOException {

		mkFolder(destDir);
		
		File file = new File( destDir + writeFile );

		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
		out.println(strData);
		out.close();
	}
	public static void appendStringToFile(String writeFile, String strData) throws IOException{
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(writeFile, true)));
	    out.println(strData);
	    out.close();
	}

	public static String readFile(String destFile){
		BufferedReader fr=null;
		StringBuffer sb=new StringBuffer();
		try{
			File f=new File(destFile);
			if (f.isFile()) {
				fr = new BufferedReader(new FileReader(f));
				String line;
				while((line=fr.readLine())!=null){
					sb.append(line +"\n");
				}
			} else {
				return "";
			}
			return sb.toString(); 
		}catch(Exception e){
			return "";
		}finally{
			try {
				if(fr!=null)fr.close();
			} catch (IOException e) {
			}
		}
	}
	public static void saveObjectToFile(String destDir, String filename, Object v) {
		FileOutputStream fileout = null;
		ObjectOutputStream out = null;
		try {
			mkFolder(destDir);

			File tmpfile = File.createTempFile(filename, "", new File(destDir));

			fileout = new FileOutputStream(tmpfile);
			out = new ObjectOutputStream(fileout);
			out.writeObject(v);
		} catch (IOException e) {
		} finally {
			try {
				if (out != null)
					out.close();
				if (fileout != null)
					fileout.close();
				out = null;
				fileout = null;
			} catch (Exception e) {
			}
		}
	}
}
