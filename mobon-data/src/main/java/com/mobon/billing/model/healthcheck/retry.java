package com.mobon.billing.model.healthcheck;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class retry implements Serializable{
	int fileLength=0;
	ArrayList fileList;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	public int getFileLength() {
		return fileLength;
	}
	public void setFileLength(int fileLength) {
		this.fileLength = fileLength;
	}
	public ArrayList getFileList() {
		return fileList;
	}
	public void setFileList(ArrayList fileList) {
		this.fileList = fileList;
	}
}