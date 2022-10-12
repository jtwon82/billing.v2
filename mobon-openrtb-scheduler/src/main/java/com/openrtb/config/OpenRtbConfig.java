package com.openrtb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "openrtbconfig")
@Data
public class OpenRtbConfig implements Serializable {

	private static final long serialVersionUID = -7718716895805918428L;

	private boolean devStatus;
	private List<String> itlTpCodeList;

	private String manuallyFilePath;
}
