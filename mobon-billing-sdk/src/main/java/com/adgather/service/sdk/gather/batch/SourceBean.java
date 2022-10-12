package com.adgather.service.sdk.gather.batch;

import com.adgather.user.inclinations.ConsumerInclinations;

public class SourceBean {
	private Object data;
	
	private Object dataRM;
	
	private ConsumerInclinations ci;
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	public Object getDataRM() {
		return dataRM;
	}
	public void setDataRM(Object dataRM) {
		this.dataRM = dataRM;
	}
	
	public ConsumerInclinations getCi() {
		return ci;
	}
	public void setCi(ConsumerInclinations ci) {
		this.ci = ci;
	}
}
