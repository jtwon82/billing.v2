package com.mobon.billing.report.schedule;

import java.io.File;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.mobon.billing.report.disk.search.IndexFiles;

@Configuration
@Service
public class IndexTask {
	
	public void execute() {
		String indexPath = "./index";
		String docsPath = "./data";
		String[] dirs = new String[]{"0", "1"};
		for(int i=0; i<dirs.length; i++) { 
			IndexFiles.index(docsPath+File.separator+dirs[i]);
		}
	} 
	
}


