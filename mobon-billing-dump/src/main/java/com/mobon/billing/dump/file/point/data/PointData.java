package com.mobon.billing.dump.file.point.data;


import java.math.BigDecimal;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Setter
@ToString
@Slf4j
public class PointData {
	
	public int statsDttm;
	public String statsHh;
	public String adverId;
	public String siteCode;
	public String mediaId;
	public String mediaScriptNo;
	public BigDecimal point = BigDecimal.ZERO;
	
	public PointData(String fileData) {
		JSONParser parser = null;
		Object obj = null;
		JSONObject jsonObj = null;
		
		try {
			parser = new JSONParser();
			obj = new Object();
			jsonObj = new JSONObject();
			
			obj = parser.parse(fileData);
			jsonObj = (JSONObject) obj;
			
		}catch (Exception e) {
			log.error("####JSON Parsing Error####"+e);
		}
		this.statsDttm = Integer.parseInt((String) jsonObj.get("ymd"));
		this.statsHh = (String) jsonObj.get("hh");
		this.adverId = (String) jsonObj.get("uid");
		this.siteCode = (String) jsonObj.get("sc");
		this.mediaId = (String) jsonObj.get("sid");
		this.mediaScriptNo = String.valueOf(jsonObj.get("s"));
		this.point = new BigDecimal(String.valueOf(jsonObj.get("p")));
	
	}

	public String getPointDataStatskey() {
		StringBuffer sb = new StringBuffer()
				.append(this.statsDttm).append("_")
				.append(this.siteCode).append("_")
				.append(this.mediaId);
		return sb.toString();
	}

	public void addPoint(BigDecimal point2) {
		this.point = this.point.add(point2);
		this.setPoint(this.point);
	}
}
