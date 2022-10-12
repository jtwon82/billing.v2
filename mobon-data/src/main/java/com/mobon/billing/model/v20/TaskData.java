package com.mobon.billing.model.v20;

import java.util.Arrays;
import java.util.List;

public class TaskData {

	private String task;
	private String id;
	private String []group;
	private List<?> filtering;
	private int retryCnt = 0;

	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String [] getGroup() {
		return group;
	}
	public void setGroup(String [] group) {
		this.group = group;
	}
	public List<?> getFiltering() {
		return filtering;
	}
	public void setFiltering(List<?> filtering) {
		this.filtering = filtering;
	}
	public int increaseRetryCnt() {
		this.setRetryCnt( this.getRetryCnt()+1 );
		return this.getRetryCnt();
	}
	public int getRetryCnt() {
		return retryCnt;
	}
	public void setRetryCnt(int retryCnt) {
		this.retryCnt = retryCnt;
	}

	public TaskData(String _task, String _id, List<?> filtering2) {
		task = _task;
		id = _id;
		filtering = filtering2;
	}

	public TaskData(String _task, String [] _group, List<?> filtering2) {
		task = _task;
		id = Arrays.asList(_group).toString();
		setGroup(_group);
		filtering = filtering2;
	}

	public TaskData(String _task, String _id, List<?> _filtering, int _retryCnt) {
		task = _task;
		id = _id;
		filtering = _filtering;
		retryCnt = _retryCnt;
	}
}
