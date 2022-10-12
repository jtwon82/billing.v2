package com.mobon.billing.report.disk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Directory {
	String realPath = null;
	
	public void mkdir(String dir, String parent) {
		mkdir(dir.toCharArray(), parent.split(File.separator+File.separator));
	}
	
	public void mkdir(char[] dir, String[] parent) {
		List dirs = new ArrayList();
		
		for(int k=0; k<parent.length; k++) {
			dirs.add(parent[k]);
		}
		
		for(int i=0; i<4; i++) {
			dirs.add(String.valueOf(dir[i]));
		}
		mkdir(dirs, null);
	}
	
	public void mkdir(List<String> dir, String parent) {
		boolean result;
		String path = (parent == null) ? dir.remove(0) : parent+File.separator+dir.remove(0);
		File f = new File(path);
		
		// 최 하위 디렉토리에 대해서만 생성을 함.
		// 최 하위 디렉토리의 바루 상위 디렉토리가 존재하지 않을 경우,
		// 디렉토리가 생성되지 못하고, false를 리턴함
		result = f.mkdir();
		if(dir.size() > 0) {
			this.realPath = path;
			mkdir(dir, path);
		} else {
			this.realPath = path;
		}
		
	}
	
	public static void main(String args[]) {
		
//		List<String> dir = new ArrayList<String>();
//		dir.add("data");
//		dir.add("a");
//		dir.add("3");
		String dir = "data2-data1";
		Directory _dir = new Directory();
//		_dir.mkdir(dir, ".");
//		System.out.println(_dir.realPath + " :: " + "test".toString().hashCode());
//		System.out.println(_dir.realPath + " :: " + "test2".toString().hashCode());
		
		_dir.mkdir(dir, "./data"+File.separator+DateUtils.getToDayYYMMDD());

	}
}
