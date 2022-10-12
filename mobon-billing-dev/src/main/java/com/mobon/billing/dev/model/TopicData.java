package com.mobon.billing.dev.model;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

public class TopicData implements Serializable{
	
	private static final Logger logger = LoggerFactory.getLogger(TopicData.class);
	
	private String topic;
	private String sendData;
	
	public TopicData(String topic, String sendData) {
		JSONObject map = JSONObject.fromObject(sendData);
		String point = "";
		String mpoint = "";
		try {
			point = map.getString("point");
		}catch(Exception e) {
			point="0";
		}
		try {
			mpoint = map.getString("mpoint");
		}catch(Exception e) {
			mpoint="0";
		}
		if(!"0".equals(point) || !"0".equals(mpoint)) {
			topic = "ClickViewPointData";
		}
		this.topic = topic;
		this.sendData = sendData;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getSendData() {
		return sendData;
	}
	public void setSendData(String sendData) {
		this.sendData = sendData;
	}
}
