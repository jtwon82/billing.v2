package com.adgather.lang.old;

import net.sf.json.JSONObject;

public class ObjectToString {

	/**
	 * 조회용
	 */
	protected String className;
	protected String sendDate;
	protected String startDate;
	protected String endDate;
	protected String targetDate;
	protected int limit;
	protected String orderBy;
	private boolean isEmpty;
	protected String hh;

	public String getHh() {
		return hh;
	}
	public void setHh(String hh) {
		this.hh = hh;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getTargetDate() {
    return targetDate;
  }
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
  public void setTargetDate(String targetDate) {
    this.targetDate = targetDate;
  }
  public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	@Override
	public String toString() {
		try {
			return getClass().getSimpleName() + " "+ JSONObject.fromObject(this).toString();
		} catch (Exception e) {
			return getClass().getSimpleName() + " - Not Convert JsonString. Ref[" + super.toString() + "]";
		}
	}
	public boolean isEmpty() {
		return isEmpty;
	}
	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}
}
