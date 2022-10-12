package com.mobon.billing.model.healthcheck;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

public class consumerInfo implements Serializable{
	
	public queue queue;
	public retry retry;
	public retry errorlog;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	public retry getRetry() {
		return retry;
	}
	public void setRetry(retry retry) {
		this.retry = retry;
	}
	public queue getQueue() {
		return queue;
	}
	public void setQueue(queue queue) {
		this.queue = queue;
	}
	
}