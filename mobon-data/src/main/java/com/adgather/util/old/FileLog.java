package com.adgather.util.old;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;

public abstract class FileLog {
	private String prefix;
	private String subfix;
	private String extension;
	private String dirPath;
	private boolean dayRename;
	private long reinitTime;
	private String fileName;
	private File logFile;
	
	// 일반 로그 출력
	public FileLog(String dirPath, String prefix, String subfix, String extension) {
		this(dirPath, prefix, subfix, extension, false);
	}
	
	// 날짜별 로그 출력
	public FileLog(String dirPath, String prefix, String extension, boolean dayRename) {
		this(dirPath, prefix, "", extension, dayRename);
	}
	private FileLog(String dirPath, String prefix, String subfix,  String extension, boolean dayRename) {
		this.dirPath = dirPath;
		this.prefix = prefix;
		this.subfix = subfix;
		this.extension = extension;
		this.dayRename = dayRename;
		
		try {
			init(this.dayRename);
		} catch (Exception e) {}
	}
	
	private void init(boolean dayRename) throws IOException {
		Calendar calendar = Calendar.getInstance();
		if(dayRename) {
			String date = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
			fileName = prefix + date + extension;
			
			calendar.add(Calendar.DATE, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			reinitTime = calendar.getTimeInMillis();
		} else {
			fileName = prefix + subfix + extension;
		}

		File dir = new File(dirPath);
		if(!dir.exists() || !dir.isDirectory()) {
			dir.setWritable(true, true);
			dir.setExecutable(true, true);
			dir.setReadable(true, true);
			boolean b = dir.mkdir();

		} 
		logFile = new File(dirPath + "/" + fileName);
		if(!logFile.exists() || !logFile.isFile()) {
			logFile.setWritable(true, true);
			logFile.setExecutable(true, true);
			logFile.setReadable(true, true);
			boolean b= logFile.createNewFile();
		}
	}
	
	private void check() {
		if( this.dayRename && System.currentTimeMillis() >= reinitTime) {
			try {
				init(this.dayRename);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void appendLn(String log) throws IOException{
		if(logFile == null)
			return;
		
		check();
		FileUtils.write(logFile, log + "\n", "UTF-8", true);
	}
	public void append(String log) throws IOException{
		if(logFile == null)
			return;
		
		check();
		FileUtils.write(logFile, log, "UTF-8", true);
	}
	
}
