package com.mobon.report.dao.old;

public class ViewInfo extends ReportInfo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int viewCnt1; //총노출
	private int viewCnt2; //광고주 노출
	private int viewCnt3; //구좌노출
	/**
	 * 노출된 날짜
	 * yyyymmdd 형태의 날짜
	 */
	private String viewDate;
	
	private float mpoint = 0;
	
	public int getViewCnt1() {
		return viewCnt1;
	}
	public void setViewCnt1(int viewCnt1) {
		this.viewCnt1 = viewCnt1;
	}
	public int getViewCnt2() {
		return viewCnt2;
	}
	public void setViewCnt2(int viewCnt2) {
		this.viewCnt2 = viewCnt2;
	}
	public int getViewCnt3() {
		return viewCnt3;
	}
	public void setViewCnt3(int viewCnt3) {
		this.viewCnt3 = viewCnt3;
	}
	public String getViewDate() {
		return viewDate;
	}
	public void setViewDate(String viewDate) {
		this.viewDate = viewDate;
	}
	public float getMpoint() {
		return mpoint;
	}
	public void setMpoint(float mpoint) {
		this.mpoint = mpoint;
	}
	
}
