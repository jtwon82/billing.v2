package com.mobon.billing.branchUM.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.mobon.billing.util.ShellUtils;

@Component
public class TaskShell {

	private static final Logger logger = LoggerFactory.getLogger(TaskShell.class);

	public void mongoCollectionRework() {
		try {
			ShellUtils.shellCmd("sh createCollection.sh");
			
			logger.info("sh createCollection.sh END");
		} catch (Exception e) {
			logger.error("err shell ", e);
		}
	}
}
